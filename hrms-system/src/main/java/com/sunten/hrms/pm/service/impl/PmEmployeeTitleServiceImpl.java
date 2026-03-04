package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeTitleDao;
import com.sunten.hrms.pm.dao.PmEmployeeTitleTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeTitle;
import com.sunten.hrms.pm.domain.PmEmployeeTitleTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTitleDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTitleQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeTitleCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeTitleMapper;
import com.sunten.hrms.pm.service.PmEmployeeTitleService;
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
 * 职称情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeTitleServiceImpl extends ServiceImpl<PmEmployeeTitleDao, PmEmployeeTitle> implements PmEmployeeTitleService {
    private final PmEmployeeTitleDao pmEmployeeTitleDao;
    private final PmEmployeeTitleMapper pmEmployeeTitleMapper;
    private final PmEmployeeTitleCheckMapper pmEmployeeTitleCheckMapper;
    private final PmEmployeeTitleTempDao pmEmployeeTitleTempDao;

    public PmEmployeeTitleServiceImpl(PmEmployeeTitleDao pmEmployeeTitleDao, PmEmployeeTitleMapper pmEmployeeTitleMapper, PmEmployeeTitleCheckMapper pmEmployeeTitleCheckMapper, PmEmployeeTitleTempDao pmEmployeeTitleTempDao) {
        this.pmEmployeeTitleDao = pmEmployeeTitleDao;
        this.pmEmployeeTitleMapper = pmEmployeeTitleMapper;
        this.pmEmployeeTitleCheckMapper = pmEmployeeTitleCheckMapper;
        this.pmEmployeeTitleTempDao = pmEmployeeTitleTempDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeTitleDTO insert(PmEmployeeTitle employeeTitleNew) {
        if (employeeTitleNew.getNewTitleFlag() != null && employeeTitleNew.getNewTitleFlag()) {
            //验证是否最高职称
            PmEmployeeTitle pmEmployeeTitle = pmEmployeeTitleDao.getMainTitleByKey(employeeTitleNew.getEmployee().getId());
            if (pmEmployeeTitle != null) {
                pmEmployeeTitleDao.updateNewTitleFlag(pmEmployeeTitle.setNewTitleFlag(false));
            }
        }
        pmEmployeeTitleDao.insertAllColumn(employeeTitleNew);
        return pmEmployeeTitleMapper.toDto(employeeTitleNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeTitle employeeTitle = new PmEmployeeTitle();
        employeeTitle.setId(id);
        employeeTitle.setEnabledFlag(false);
        this.delete(employeeTitle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeTitle employeeTitle) {
        //  确认删除前是否需要做检查,只失效，不删除
        pmEmployeeTitleDao.updateEnableFlag(employeeTitle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeTitle employeeTitleNew) {
        if (employeeTitleNew.getNewTitleFlag()) {
            //验证是否最高职称
            PmEmployeeTitle pmEmployeeTitle = pmEmployeeTitleDao.getMainTitleByKey(employeeTitleNew.getEmployee().getId());
            if (pmEmployeeTitle != null) {
                pmEmployeeTitleDao.updateNewTitleFlag(pmEmployeeTitle.setNewTitleFlag(false));
            }
        }
        PmEmployeeTitle employeeTitleInDb = Optional.ofNullable(pmEmployeeTitleDao.getByKey(employeeTitleNew.getId())).orElseGet(PmEmployeeTitle::new);
        ValidationUtil.isNull(employeeTitleInDb.getId(), "EmployeeTitle", "id", employeeTitleNew.getId());
        employeeTitleNew.setId(employeeTitleInDb.getId());
        pmEmployeeTitleDao.updateAllColumnByKey(employeeTitleNew);
    }

    @Override
    public PmEmployeeTitleDTO getByKey(Long id) {
        PmEmployeeTitle employeeTitle = Optional.ofNullable(pmEmployeeTitleDao.getByKey(id)).orElseGet(PmEmployeeTitle::new);
        ValidationUtil.isNull(employeeTitle.getId(), "EmployeeTitle", "id", id);
        return pmEmployeeTitleMapper.toDto(employeeTitle);
    }

    @Override
    public List<PmEmployeeTitleDTO> listAll(PmEmployeeTitleQueryCriteria criteria) {
        return pmEmployeeTitleMapper.toDto(pmEmployeeTitleDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeTitleQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeTitle> page = PageUtil.startPage(pageable);
        List<PmEmployeeTitle> employeeTitles = pmEmployeeTitleDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeTitleMapper.toDto(employeeTitles), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeTitleDTO> employeeTitleDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeTitleDTO employeeTitleDTO : employeeTitleDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeTitleDTO.getEmployee().getId());
            map.put("职称级别", employeeTitleDTO.getTitleLevel());
            map.put("职称名称", employeeTitleDTO.getTitleName());
            map.put("评定时间", employeeTitleDTO.getEvaluationTime());
            map.put("是否最高职称", employeeTitleDTO.getNewTitleFlag());
            map.put("弹性域1", employeeTitleDTO.getAttribute1());
            map.put("弹性域2", employeeTitleDTO.getAttribute2());
            map.put("弹性域3", employeeTitleDTO.getAttribute3());
            map.put("有效标记默认值", employeeTitleDTO.getEnabledFlag());
            map.put("id", employeeTitleDTO.getId());
            map.put("创建时间", employeeTitleDTO.getCreateTime());
            map.put("创建人ID", employeeTitleDTO.getCreateBy());
            map.put("修改时间", employeeTitleDTO.getUpdateTime());
            map.put("修改人ID", employeeTitleDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeTitleDTO> batchInsert(List<PmEmployeeTitle> employeeTitleNews, Long employeeId) {
        for (PmEmployeeTitle pet : employeeTitleNews) {
            if (employeeId != null) {
                if (pet.getEmployee() == null) {
                    pet.setEmployee(new PmEmployee());
                }
                pet.getEmployee().setId(employeeId);
            }
            if (pet.getEmployee() == null || pet.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pet.getId() != null) {
                if (pet.getId().equals(-1L)) {
                    pmEmployeeTitleDao.insertAllColumn(pet);
                } else {
                    pmEmployeeTitleDao.updateAllColumnByKey(pet);
                }
            } else {
                throw new InfoCheckWarningMessException("职称批量插入传入集合中 id 包含空值！");
            }
        }

        return pmEmployeeTitleMapper.toDto(employeeTitleNews);
    }

    @Override
    public List<PmEmployeeTitleDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeeTitle> pmEmployeeTitles = pmEmployeeTitleDao.listAllAndTempByEmployee(employeeId);//正式学历及其temp记录sssss

        if (pmEmployeeTitles.size() > 0) {
            for (PmEmployeeTitle pee : pmEmployeeTitles) {//循环将temp修改记录添加进children
                pee.setIdKey("P" + pee.getId().toString());
                if (pee.getTitleTemp() != null) {
                    PmEmployeeTitle tempTitle = pmEmployeeTitleCheckMapper.toEntity(pee.getTitleTemp());
                    tempTitle.setId(pee.getTitleTemp().getId());
                    tempTitle.setIdKey("S" + tempTitle.getId().toString());
                    pee.setChildren(new HashSet<>());
                    pee.getChildren().add(tempTitle);
                    pee.setTitleTemp(null);
                }
            }
        }
        /*查询新添加的待校核数据*/
        PmEmployeeTitleTempQueryCriteria TitleTempQueryCriteria = new PmEmployeeTitleTempQueryCriteria();
        TitleTempQueryCriteria.setEnabled(true);//生效
        TitleTempQueryCriteria.setEmployeeId(employeeId);//员工
        TitleTempQueryCriteria.setInstructionsFlag("A");
        TitleTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeeTitleTemp> pmEmployeeTitleTemps = pmEmployeeTitleTempDao.listAllByCriteria(TitleTempQueryCriteria);
        if (pmEmployeeTitleTemps.size() > 0) {
            PmEmployeeTitle addTempTitle = new PmEmployeeTitle();
            addTempTitle.setTitleName("新增");
            addTempTitle.setChildren(new HashSet<>());
            addTempTitle.setIdKey("NEW");
            for (PmEmployeeTitleTemp peet : pmEmployeeTitleTemps) {
                PmEmployeeTitle childrenTitle = pmEmployeeTitleCheckMapper.toEntity(peet);
                childrenTitle.setId(peet.getId());
                childrenTitle.setIdKey("S" + peet.getId().toString());
                addTempTitle.getChildren().add(childrenTitle);
            }
            pmEmployeeTitles.add(addTempTitle);
        }

        return pmEmployeeTitleMapper.toDto(pmEmployeeTitles);
    }
}
