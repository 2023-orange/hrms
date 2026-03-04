package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeePostotherDao;
import com.sunten.hrms.pm.dao.PmEmployeePostotherTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeePostother;
import com.sunten.hrms.pm.domain.PmEmployeePostotherTemp;
import com.sunten.hrms.pm.dto.PmEmployeePostotherDTO;
import com.sunten.hrms.pm.dto.PmEmployeePostotherQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeePostotherCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeePostotherMapper;
import com.sunten.hrms.pm.service.PmEmployeePostotherService;
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
 * 工作外职务表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeePostotherServiceImpl extends ServiceImpl<PmEmployeePostotherDao, PmEmployeePostother> implements PmEmployeePostotherService {
    private final PmEmployeePostotherDao pmEmployeePostotherDao;
    private final PmEmployeePostotherMapper pmEmployeePostotherMapper;
    private final PmEmployeePostotherCheckMapper pmEmployeePostotherCheckMapper;
    private final PmEmployeePostotherTempDao pmEmployeePostotherTempDao;

    public PmEmployeePostotherServiceImpl(PmEmployeePostotherDao pmEmployeePostotherDao, PmEmployeePostotherMapper pmEmployeePostotherMapper, PmEmployeePostotherCheckMapper pmEmployeePostotherCheckMapper, PmEmployeePostotherTempDao pmEmployeePostotherTempDao) {
        this.pmEmployeePostotherDao = pmEmployeePostotherDao;
        this.pmEmployeePostotherMapper = pmEmployeePostotherMapper;
        this.pmEmployeePostotherCheckMapper = pmEmployeePostotherCheckMapper;
        this.pmEmployeePostotherTempDao = pmEmployeePostotherTempDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeePostotherDTO insert(PmEmployeePostother employeePostotherNew) {
        pmEmployeePostotherDao.insertAllColumn(employeePostotherNew);
        return pmEmployeePostotherMapper.toDto(employeePostotherNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeePostother employeePostother = new PmEmployeePostother();
        employeePostother.setId(id);
        employeePostother.setEnabledFlag(false);
        this.delete(employeePostother);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeePostother employeePostother) {
        //    确认删除前是否需要做检查.只失效，不删除
        pmEmployeePostotherDao.updateEnableFlag(employeePostother);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeePostother employeePostotherNew) {
        PmEmployeePostother employeePostotherInDb = Optional.ofNullable(pmEmployeePostotherDao.getByKey(employeePostotherNew.getId())).orElseGet(PmEmployeePostother::new);
        ValidationUtil.isNull(employeePostotherInDb.getId(), "EmployeePostother", "id", employeePostotherNew.getId());
        employeePostotherNew.setId(employeePostotherInDb.getId());
        pmEmployeePostotherDao.updateAllColumnByKey(employeePostotherNew);
    }

    @Override
    public PmEmployeePostotherDTO getByKey(Long id) {
        PmEmployeePostother employeePostother = Optional.ofNullable(pmEmployeePostotherDao.getByKey(id)).orElseGet(PmEmployeePostother::new);
        ValidationUtil.isNull(employeePostother.getId(), "EmployeePostother", "id", id);
        return pmEmployeePostotherMapper.toDto(employeePostother);
    }

    @Override
    public List<PmEmployeePostotherDTO> listAll(PmEmployeePostotherQueryCriteria criteria) {
        return pmEmployeePostotherMapper.toDto(pmEmployeePostotherDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeePostotherQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeePostother> page = PageUtil.startPage(pageable);
        List<PmEmployeePostother> employeePostothers = pmEmployeePostotherDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeePostotherMapper.toDto(employeePostothers), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeePostotherDTO> employeePostotherDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeePostotherDTO employeePostotherDTO : employeePostotherDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeePostotherDTO.getEmployee().getId());
            map.put("单位", employeePostotherDTO.getCompany());
            map.put("职务", employeePostotherDTO.getPost());
            map.put("备注", employeePostotherDTO.getRemarks());
            map.put("弹性域1", employeePostotherDTO.getAttribute1());
            map.put("弹性域2", employeePostotherDTO.getAttribute2());
            map.put("弹性域3", employeePostotherDTO.getAttribute3());
            map.put("有效标记默认值", employeePostotherDTO.getEnabledFlag());
            map.put("id", employeePostotherDTO.getId());
            map.put("创建时间", employeePostotherDTO.getCreateTime());
            map.put("创建人ID", employeePostotherDTO.getCreateBy());
            map.put("修改时间", employeePostotherDTO.getUpdateTime());
            map.put("修改人ID", employeePostotherDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeePostotherDTO> batchInsert(List<PmEmployeePostother> pmEmployeePostothers, Long employeeId) {
        for (PmEmployeePostother pep : pmEmployeePostothers) {
            if (employeeId != null) {
                if (pep.getEmployee() == null) {
                    pep.setEmployee(new PmEmployee());
                }
                pep.getEmployee().setId(employeeId);
            }
            if (pep.getEmployee() == null || pep.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pep.getId() != null) {
                if (pep.getId().equals(-1L)) {
                    pmEmployeePostotherDao.insertAllColumn(pep);
                } else {
                    pmEmployeePostotherDao.updateAllColumnByKey(pep);
                }
            } else {
                throw new InfoCheckWarningMessException("工作外经历批量插入时ID不能为空");
            }
        }

        return pmEmployeePostotherMapper.toDto(pmEmployeePostothers);
    }

    @Override
    public List<PmEmployeePostotherDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeePostother> pmEmployeePostothers = pmEmployeePostotherDao.listAllAndTempByEmployee(employeeId);//正式学历及其temp记录sssss

        if (pmEmployeePostothers.size() > 0) {
            for (PmEmployeePostother pee : pmEmployeePostothers) {//循环将temp修改记录添加进children
                pee.setIdKey("P" + pee.getId().toString());
                if (pee.getPostotherTemp() != null) {
                    PmEmployeePostother tempPostother = pmEmployeePostotherCheckMapper.toEntity(pee.getPostotherTemp());
                    tempPostother.setId(pee.getPostotherTemp().getId());
                    tempPostother.setIdKey("S" + tempPostother.getId().toString());
                    pee.setChildren(new HashSet<>());
                    pee.getChildren().add(tempPostother);
                    pee.setPostotherTemp(null);
                }
            }
        }
        /*查询新添加的待校核数据*/
        PmEmployeePostotherTempQueryCriteria PostotherTempQueryCriteria = new PmEmployeePostotherTempQueryCriteria();
        PostotherTempQueryCriteria.setEnabled(true);//生效
        PostotherTempQueryCriteria.setEmployeeId(employeeId);//员工
        PostotherTempQueryCriteria.setInstructionsFlag("A");
        PostotherTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeePostotherTemp> pmEmployeePostotherTemps = pmEmployeePostotherTempDao.listAllByCriteria(PostotherTempQueryCriteria);
        if (pmEmployeePostotherTemps.size() > 0) {
            PmEmployeePostother addTempPostother = new PmEmployeePostother();
            addTempPostother.setPost("新增");
            addTempPostother.setChildren(new HashSet<>());
            addTempPostother.setIdKey("NEW");
            for (PmEmployeePostotherTemp peet : pmEmployeePostotherTemps) {
                PmEmployeePostother childrenPostother = pmEmployeePostotherCheckMapper.toEntity(peet);
                childrenPostother.setId(peet.getId());
                childrenPostother.setIdKey("S" + peet.getId().toString());
                addTempPostother.getChildren().add(childrenPostother);
            }
            pmEmployeePostothers.add(addTempPostother);
        }

        return pmEmployeePostotherMapper.toDto(pmEmployeePostothers);
    }
}
