package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeePoliticalDao;
import com.sunten.hrms.pm.dao.PmEmployeePoliticalTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeePolitical;
import com.sunten.hrms.pm.domain.PmEmployeePoliticalTemp;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalDTO;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeePoliticalCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeePoliticalMapper;
import com.sunten.hrms.pm.service.PmEmployeePoliticalService;
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
 * 政治面貌表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeePoliticalServiceImpl extends ServiceImpl<PmEmployeePoliticalDao, PmEmployeePolitical> implements PmEmployeePoliticalService {
    private final PmEmployeePoliticalDao pmEmployeePoliticalDao;
    private final PmEmployeePoliticalMapper pmEmployeePoliticalMapper;
    private final PmEmployeePoliticalCheckMapper pmEmployeePoliticalCheckMapper;
    private final PmEmployeePoliticalTempDao pmEmployeePoliticalTempDao;

    public PmEmployeePoliticalServiceImpl(PmEmployeePoliticalDao pmEmployeePoliticalDao, PmEmployeePoliticalMapper pmEmployeePoliticalMapper, PmEmployeePoliticalCheckMapper pmEmployeePoliticalCheckMapper, PmEmployeePoliticalTempDao pmEmployeePoliticalTempDao) {
        this.pmEmployeePoliticalDao = pmEmployeePoliticalDao;
        this.pmEmployeePoliticalMapper = pmEmployeePoliticalMapper;
        this.pmEmployeePoliticalCheckMapper = pmEmployeePoliticalCheckMapper;
        this.pmEmployeePoliticalTempDao = pmEmployeePoliticalTempDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeePoliticalDTO insert(PmEmployeePolitical employeePoliticalNew) {
        pmEmployeePoliticalDao.insertAllColumn(employeePoliticalNew);
        return pmEmployeePoliticalMapper.toDto(employeePoliticalNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeePolitical employeePolitical = new PmEmployeePolitical();
        employeePolitical.setId(id);
        employeePolitical.setEnabledFlag(false);
        this.delete(employeePolitical);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeePolitical employeePolitical) {
        //   确认删除前是否需要做检查,只失效，不删除
        pmEmployeePoliticalDao.updateEnableFlag(employeePolitical);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeePolitical employeePoliticalNew) {
        PmEmployeePolitical employeePoliticalInDb = Optional.ofNullable(pmEmployeePoliticalDao.getByKey(employeePoliticalNew.getId())).orElseGet(PmEmployeePolitical::new);
        ValidationUtil.isNull(employeePoliticalInDb.getId(), "EmployeePolitical", "id", employeePoliticalNew.getId());
        employeePoliticalNew.setId(employeePoliticalInDb.getId());
        pmEmployeePoliticalDao.updateAllColumnByKey(employeePoliticalNew);
    }

    @Override
    public PmEmployeePoliticalDTO getByKey(Long id) {
        PmEmployeePolitical employeePolitical = Optional.ofNullable(pmEmployeePoliticalDao.getByKey(id)).orElseGet(PmEmployeePolitical::new);
        ValidationUtil.isNull(employeePolitical.getId(), "EmployeePolitical", "id", id);
        return pmEmployeePoliticalMapper.toDto(employeePolitical);
    }

    @Override
    public List<PmEmployeePoliticalDTO> listAll(PmEmployeePoliticalQueryCriteria criteria) {
        return pmEmployeePoliticalMapper.toDto(pmEmployeePoliticalDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeePoliticalQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeePolitical> page = PageUtil.startPage(pageable);
        List<PmEmployeePolitical> employeePoliticals = pmEmployeePoliticalDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeePoliticalMapper.toDto(employeePoliticals), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeePoliticalDTO> employeePoliticalDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeePoliticalDTO employeePoliticalDTO : employeePoliticalDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeePoliticalDTO.getEmployee().getId());
            map.put("政治面貌", employeePoliticalDTO.getPolitical());
            map.put("加入时间", employeePoliticalDTO.getJoiningTime());
            map.put("性质", employeePoliticalDTO.getNature());
            map.put("转正时间", employeePoliticalDTO.getFormalTime());
            map.put("职务", employeePoliticalDTO.getPost());
            map.put("开始时间", employeePoliticalDTO.getStartTime());
            map.put("结束时间", employeePoliticalDTO.getEndTime());
            map.put("弹性域1", employeePoliticalDTO.getAttribute1());
            map.put("弹性域2", employeePoliticalDTO.getAttribute2());
            map.put("弹性域3", employeePoliticalDTO.getAttribute3());
            map.put("有效标记默认值", employeePoliticalDTO.getEnabledFlag());
            map.put("id", employeePoliticalDTO.getId());
            map.put("创建时间", employeePoliticalDTO.getCreateTime());
            map.put("创建人ID", employeePoliticalDTO.getCreateBy());
            map.put("修改时间", employeePoliticalDTO.getUpdateTime());
            map.put("修改人ID", employeePoliticalDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeePoliticalDTO> batchInsert(List<PmEmployeePolitical> pmEmployeePoliticals, Long employeeId) {
        for (PmEmployeePolitical pep : pmEmployeePoliticals) {
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
                    pmEmployeePoliticalDao.insertAllColumn(pep);
                } else {
                    pmEmployeePoliticalDao.updateAllColumnByKey(pep);
                }
            } else {
                throw new InfoCheckWarningMessException("政治面貌批量插入时ID不能为空");
            }

        }
        return pmEmployeePoliticalMapper.toDto(pmEmployeePoliticals);
    }

    @Override
    public List<PmEmployeePoliticalDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeePolitical> pmEmployeePoliticals = pmEmployeePoliticalDao.listAllAndTempByEmployee(employeeId);//正式学历及其temp记录sssss

        if (pmEmployeePoliticals.size() > 0) {
            for (PmEmployeePolitical pee : pmEmployeePoliticals) {//循环将temp修改记录添加进children
                pee.setIdKey("P" + pee.getId().toString());
                if (pee.getPoliticalTemp() != null) {
                    PmEmployeePolitical tempPolitical = pmEmployeePoliticalCheckMapper.toEntity(pee.getPoliticalTemp());
                    tempPolitical.setId(pee.getPoliticalTemp().getId());
                    tempPolitical.setIdKey("S" + tempPolitical.getId().toString());
                    pee.setChildren(new HashSet<>());
                    pee.getChildren().add(tempPolitical);
                    pee.setPoliticalTemp(null);
                }
            }
        }
        /*查询新添加的待校核数据*/
        PmEmployeePoliticalTempQueryCriteria PoliticalTempQueryCriteria = new PmEmployeePoliticalTempQueryCriteria();
        PoliticalTempQueryCriteria.setEnabled(true);//生效
        PoliticalTempQueryCriteria.setEmployeeId(employeeId);//员工
        PoliticalTempQueryCriteria.setInstructionsFlag("A");
        PoliticalTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeePoliticalTemp> pmEmployeePoliticalTemps = pmEmployeePoliticalTempDao.listAllByCriteria(PoliticalTempQueryCriteria);
        if (pmEmployeePoliticalTemps.size() > 0) {
            PmEmployeePolitical addTempPolitical = new PmEmployeePolitical();
            addTempPolitical.setPolitical("新增");
            addTempPolitical.setChildren(new HashSet<>());
            addTempPolitical.setIdKey("NEW");
            for (PmEmployeePoliticalTemp peet : pmEmployeePoliticalTemps) {
                PmEmployeePolitical childrenPolitical = pmEmployeePoliticalCheckMapper.toEntity(peet);
                childrenPolitical.setId(peet.getId());
                childrenPolitical.setIdKey("S" + peet.getId().toString());
                addTempPolitical.getChildren().add(childrenPolitical);
            }
            pmEmployeePoliticals.add(addTempPolitical);
        }

        return pmEmployeePoliticalMapper.toDto(pmEmployeePoliticals);
    }
}
