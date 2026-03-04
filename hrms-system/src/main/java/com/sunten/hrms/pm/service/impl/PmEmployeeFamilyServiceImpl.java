package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeFamilyDao;
import com.sunten.hrms.pm.dao.PmEmployeeFamilyTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeFamily;
import com.sunten.hrms.pm.domain.PmEmployeeFamilyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyDTO;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeFamilyCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeFamilyMapper;
import com.sunten.hrms.pm.service.PmEmployeeFamilyService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 家庭情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeFamilyServiceImpl extends ServiceImpl<PmEmployeeFamilyDao, PmEmployeeFamily> implements PmEmployeeFamilyService {
    private final PmEmployeeFamilyDao pmEmployeeFamilyDao;
    private final PmEmployeeFamilyMapper pmEmployeeFamilyMapper;
    private final PmEmployeeFamilyTempDao pmEmployeeFamilyTempDao;
    private final PmEmployeeFamilyCheckMapper pmEmployeeFamilyCheckMapper;


    public PmEmployeeFamilyServiceImpl(PmEmployeeFamilyDao pmEmployeeFamilyDao, PmEmployeeFamilyMapper pmEmployeeFamilyMapper,
                                       PmEmployeeFamilyTempDao pmEmployeeFamilyTempDao, PmEmployeeFamilyCheckMapper pmEmployeeFamilyCheckMapper) {
        this.pmEmployeeFamilyDao = pmEmployeeFamilyDao;
        this.pmEmployeeFamilyMapper = pmEmployeeFamilyMapper;
        this.pmEmployeeFamilyTempDao = pmEmployeeFamilyTempDao;
        this.pmEmployeeFamilyCheckMapper = pmEmployeeFamilyCheckMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeFamilyDTO insert(PmEmployeeFamily employeeFamilyNew) {
        pmEmployeeFamilyDao.insertAllColumn(employeeFamilyNew);
        return pmEmployeeFamilyMapper.toDto(employeeFamilyNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeFamily employeeFamily = new PmEmployeeFamily();
        employeeFamily.setId(id);
        employeeFamily.setEnabledFlag(false);
        this.delete(employeeFamily);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeFamily employeeFamily) {
        //  确认删除前是否需要做检查,只失效，不删除
        pmEmployeeFamilyDao.updateEnableFlag(employeeFamily);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeFamily employeeFamilyNew) {
        PmEmployeeFamily employeeFamilyInDb = Optional.ofNullable(pmEmployeeFamilyDao.getByKey(employeeFamilyNew.getId())).orElseGet(PmEmployeeFamily::new);
        ValidationUtil.isNull(employeeFamilyInDb.getId(), "EmployeeFamily", "id", employeeFamilyNew.getId());
        employeeFamilyNew.setId(employeeFamilyInDb.getId());
        pmEmployeeFamilyDao.updateAllColumnByKey(employeeFamilyNew);
    }

    @Override
    public PmEmployeeFamilyDTO getByKey(Long id) {
        PmEmployeeFamily employeeFamily = Optional.ofNullable(pmEmployeeFamilyDao.getByKey(id)).orElseGet(PmEmployeeFamily::new);
        ValidationUtil.isNull(employeeFamily.getId(), "EmployeeFamily", "id", id);
        return pmEmployeeFamilyMapper.toDto(employeeFamily);
    }

    @Override
    public List<PmEmployeeFamilyDTO> listAll(PmEmployeeFamilyQueryCriteria criteria) {
        return pmEmployeeFamilyMapper.toDto(pmEmployeeFamilyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeFamilyQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeFamily> page = PageUtil.startPage(pageable);
        List<PmEmployeeFamily> employeeFamilys = pmEmployeeFamilyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeFamilyMapper.toDto(employeeFamilys), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeFamilyDTO> employeeFamilyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeFamilyDTO employeeFamilyDTO : employeeFamilyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeFamilyDTO.getEmployee().getId());
            map.put("姓名", employeeFamilyDTO.getName());
            map.put("关系", employeeFamilyDTO.getRelationship());
            map.put("单位", employeeFamilyDTO.getCompany());
            map.put("职务", employeeFamilyDTO.getPost());
            map.put("电话", employeeFamilyDTO.getTele());
            map.put("性别", employeeFamilyDTO.getGender());
            map.put("出生日期", employeeFamilyDTO.getBirthday());
            map.put("手机", employeeFamilyDTO.getMobilePhone());
            map.put("弹性域1", employeeFamilyDTO.getAttribute1());
            map.put("弹性域2", employeeFamilyDTO.getAttribute2());
            map.put("弹性域3", employeeFamilyDTO.getAttribute3());
            map.put("有效标记默认值", employeeFamilyDTO.getEnabledFlag());
            map.put("id", employeeFamilyDTO.getId());
            map.put("创建时间", employeeFamilyDTO.getCreateTime());
            map.put("创建人ID", employeeFamilyDTO.getCreateBy());
            map.put("修改时间", employeeFamilyDTO.getUpdateTime());
            map.put("修改人ID", employeeFamilyDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PmEmployeeFamilyDTO> batchInsert(List<PmEmployeeFamily> pmEmployeeFamilies, Long employeeId) {
        for (PmEmployeeFamily pef : pmEmployeeFamilies) {
            if (employeeId != null) {
                if (pef.getEmployee() == null) {
                    pef.setEmployee(new PmEmployee());
                }
                pef.getEmployee().setId(employeeId);
            }
            if (pef.getEmployee() == null || pef.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pef.getId() != null) {
                if (pef.getId().equals(-1L)) {
                    pmEmployeeFamilyDao.insertAllColumn(pef);
                } else {
                    pmEmployeeFamilyDao.updateAllColumnByKey(pef);
                }
            } else {
                throw new InfoCheckWarningMessException("家庭情况批量插入时ID不能为空");
            }
        }
        return pmEmployeeFamilyMapper.toDto(pmEmployeeFamilies);
    }

    @Override
    public List<PmEmployeeFamilyDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeeFamily> pmEmployeeFamilies = pmEmployeeFamilyDao.listAllAndTempByEmployye(employeeId);

        if (pmEmployeeFamilies.size() > 0) {
            for (PmEmployeeFamily pef : pmEmployeeFamilies) {//循环将temp修改记录添加进children
                pef.setIdKey("P" + pef.getId().toString());
                if (pef.getFamilyTemp() != null) {
                    PmEmployeeFamily tempfamily = pmEmployeeFamilyCheckMapper.toEntity(pef.getFamilyTemp());
                    System.out.println(tempfamily.toString());
                    tempfamily.setId(pef.getFamilyTemp().getId());
                    tempfamily.setIdKey("S" + tempfamily.getId().toString());
                    pef.setChildren(new HashSet<>());
                    pef.getChildren().add(tempfamily);
                    pef.setFamilyTemp(null);
                }
            }
        }
        /*待添加的校核数据*/
        PmEmployeeFamilyTempQueryCriteria familyTempQueryCriteria = new PmEmployeeFamilyTempQueryCriteria();
        familyTempQueryCriteria.setEnabled(true);
        familyTempQueryCriteria.setEmployeeId(employeeId);
        familyTempQueryCriteria.setInstructionsFlag("A");
        familyTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeeFamilyTemp> familyTemps = pmEmployeeFamilyTempDao.listAllByCriteria(familyTempQueryCriteria);

        if (familyTemps.size() > 0) {
            PmEmployeeFamily addTempfamily = new PmEmployeeFamily();
            addTempfamily.setName("新增");
            addTempfamily.setChildren(new HashSet<>());
            addTempfamily.setIdKey("NEW");
            for (PmEmployeeFamilyTemp peft : familyTemps) {
                PmEmployeeFamily childrenFamily = pmEmployeeFamilyCheckMapper.toEntity(peft);
                childrenFamily.setId(peft.getId());
                childrenFamily.setIdKey("S" + peft.getId().toString());
                addTempfamily.getChildren().add(childrenFamily);
            }

            pmEmployeeFamilies.add(addTempfamily);
        }

        return pmEmployeeFamilyMapper.toDto(pmEmployeeFamilies);
    }

    @Override
    public List<PmEmployeeFamilyDTO> getChild(Long employeeId) {
        List<PmEmployeeFamily> pmEmployeeFamilyList = pmEmployeeFamilyDao.getChild(employeeId);
        if (!(pmEmployeeFamilyList.size() > 0)) {
            throw new InfoCheckWarningMessException("人事模块中未检测到该申请人存在小孩信息！");
        } else {
            return pmEmployeeFamilyMapper.toDto(pmEmployeeFamilyList);
        }
    }
}
