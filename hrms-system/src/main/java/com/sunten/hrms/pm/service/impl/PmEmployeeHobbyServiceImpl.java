package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeHobbyDao;
import com.sunten.hrms.pm.dao.PmEmployeeHobbyTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeHobby;
import com.sunten.hrms.pm.domain.PmEmployeeHobbyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyDTO;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeHobbyCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeHobbyMapper;
import com.sunten.hrms.pm.service.PmEmployeeHobbyService;
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
 * 技术爱好表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeHobbyServiceImpl extends ServiceImpl<PmEmployeeHobbyDao, PmEmployeeHobby> implements PmEmployeeHobbyService {
    private final PmEmployeeHobbyDao pmEmployeeHobbyDao;
    private final PmEmployeeHobbyMapper pmEmployeeHobbyMapper;
    private final PmEmployeeHobbyCheckMapper pmEmployeeHobbyCheckMapper;
    private final PmEmployeeHobbyTempDao pmEmployeeHobbyTempDao;

    public PmEmployeeHobbyServiceImpl(PmEmployeeHobbyDao pmEmployeeHobbyDao, PmEmployeeHobbyMapper pmEmployeeHobbyMapper, PmEmployeeHobbyCheckMapper pmEmployeeHobbyCheckMapper, PmEmployeeHobbyTempDao pmEmployeeHobbyTempDao) {
        this.pmEmployeeHobbyDao = pmEmployeeHobbyDao;
        this.pmEmployeeHobbyMapper = pmEmployeeHobbyMapper;
        this.pmEmployeeHobbyCheckMapper = pmEmployeeHobbyCheckMapper;
        this.pmEmployeeHobbyTempDao = pmEmployeeHobbyTempDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeHobbyDTO insert(PmEmployeeHobby employeeHobbyNew) {
        pmEmployeeHobbyDao.insertAllColumn(employeeHobbyNew);
        return pmEmployeeHobbyMapper.toDto(employeeHobbyNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeHobby employeeHobby = new PmEmployeeHobby();
        employeeHobby.setId(id);
        employeeHobby.setEnabledFlag(false);
        this.delete(employeeHobby);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeHobby employeeHobby) {
        //   确认删除前是否需要做检查,只失效，不删除
        pmEmployeeHobbyDao.updateEnableFlag(employeeHobby);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeHobby employeeHobbyNew) {
        PmEmployeeHobby employeeHobbyInDb = Optional.ofNullable(pmEmployeeHobbyDao.getByKey(employeeHobbyNew.getId())).orElseGet(PmEmployeeHobby::new);
        ValidationUtil.isNull(employeeHobbyInDb.getId(), "EmployeeHobby", "id", employeeHobbyNew.getId());
        employeeHobbyNew.setId(employeeHobbyInDb.getId());
        pmEmployeeHobbyDao.updateAllColumnByKey(employeeHobbyNew);
    }

    @Override
    public PmEmployeeHobbyDTO getByKey(Long id) {
        PmEmployeeHobby employeeHobby = Optional.ofNullable(pmEmployeeHobbyDao.getByKey(id)).orElseGet(PmEmployeeHobby::new);
        ValidationUtil.isNull(employeeHobby.getId(), "EmployeeHobby", "id", id);
        return pmEmployeeHobbyMapper.toDto(employeeHobby);
    }

    @Override
    public List<PmEmployeeHobbyDTO> listAll(PmEmployeeHobbyQueryCriteria criteria) {
        return pmEmployeeHobbyMapper.toDto(pmEmployeeHobbyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeHobbyQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeHobby> page = PageUtil.startPage(pageable);
        List<PmEmployeeHobby> employeeHobbys = pmEmployeeHobbyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeHobbyMapper.toDto(employeeHobbys), page.getTotal());
    }

    @Override
    public Map<String, Object>listAllSummary(PmEmployeeHobbyQueryCriteria criteria) {
        List<PmEmployeeHobby> employeeHobbys = pmEmployeeHobbyDao.listAllBySummary(criteria);
        return PageUtil.toPage(pmEmployeeHobbyMapper.toDto(employeeHobbys),employeeHobbys.size());
    }

    @Override
    public void download(List<PmEmployeeHobbyDTO> employeeHobbyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeHobbyDTO employeeHobbyDTO : employeeHobbyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeHobbyDTO.getEmployee().getId());
            map.put("技能爱好", employeeHobbyDTO.getHobby());
            map.put("级别", employeeHobbyDTO.getLevelMyself());
            map.put("认证等级", employeeHobbyDTO.getLevelMechanism());
            map.put("备注", employeeHobbyDTO.getRemarks());
            map.put("弹性域1", employeeHobbyDTO.getAttribute1());
            map.put("弹性域2", employeeHobbyDTO.getAttribute2());
            map.put("弹性域3", employeeHobbyDTO.getAttribute3());
            map.put("有效标记默认值", employeeHobbyDTO.getEnabledFlag());
            map.put("id", employeeHobbyDTO.getId());
            map.put("创建时间", employeeHobbyDTO.getCreateTime());
            map.put("创建人ID", employeeHobbyDTO.getCreateBy());
            map.put("修改时间", employeeHobbyDTO.getUpdateTime());
            map.put("修改人ID", employeeHobbyDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeHobbyDTO> batchInsert(List<PmEmployeeHobby> pmEmployeeHobbies, Long employeeId) {
        for (PmEmployeeHobby peh : pmEmployeeHobbies) {
            if (employeeId != null) {
                if (peh.getEmployee() == null) {
                    peh.setEmployee(new PmEmployee());
                }
                peh.getEmployee().setId(employeeId);
            }
            if (peh.getEmployee() == null || peh.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (peh.getId() != null) {
                if (peh.getId().equals(-1L)) {
                    pmEmployeeHobbyDao.insertAllColumn(peh);
                } else {
                    pmEmployeeHobbyDao.updateAllColumnByKey(peh);
                }
            } else {
                throw new InfoCheckWarningMessException("技术爱好批量插入时ID不能为空");
            }
        }

        return pmEmployeeHobbyMapper.toDto(pmEmployeeHobbies);
    }

    @Override
    public List<PmEmployeeHobbyDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeeHobby> pmEmployeeHobbys = pmEmployeeHobbyDao.listAllAndTempByEmployee(employeeId);//正式学历及其temp记录sssss

        if (pmEmployeeHobbys.size() > 0) {
            for (PmEmployeeHobby pee : pmEmployeeHobbys) {//循环将temp修改记录添加进children
                pee.setIdKey("P" + pee.getId().toString());
                if (pee.getHobbyTemp() != null) {
                    PmEmployeeHobby tempHobby = pmEmployeeHobbyCheckMapper.toEntity(pee.getHobbyTemp());
                    tempHobby.setId(pee.getHobbyTemp().getId());
                    tempHobby.setIdKey("S" + tempHobby.getId().toString());
                    pee.setChildren(new HashSet<>());
                    pee.getChildren().add(tempHobby);
                    pee.setHobbyTemp(null);
                }
            }
        }
        /*查询新添加的待校核数据*/
        PmEmployeeHobbyTempQueryCriteria HobbyTempQueryCriteria = new PmEmployeeHobbyTempQueryCriteria();
        HobbyTempQueryCriteria.setEnabled(true);//生效
        HobbyTempQueryCriteria.setEmployeeId(employeeId);//员工
        HobbyTempQueryCriteria.setInstructionsFlag("A");
        HobbyTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeeHobbyTemp> pmEmployeeHobbyTemps = pmEmployeeHobbyTempDao.listAllByCriteria(HobbyTempQueryCriteria);
        if (pmEmployeeHobbyTemps.size() > 0) {
            PmEmployeeHobby addTempHobby = new PmEmployeeHobby();
            addTempHobby.setHobby("新增");
            addTempHobby.setChildren(new HashSet<>());
            addTempHobby.setIdKey("NEW");
            for (PmEmployeeHobbyTemp peet : pmEmployeeHobbyTemps) {
                PmEmployeeHobby childrenHobby = pmEmployeeHobbyCheckMapper.toEntity(peet);
                childrenHobby.setId(peet.getId());
                childrenHobby.setIdKey("S" + peet.getId().toString());
                addTempHobby.getChildren().add(childrenHobby);
            }
            pmEmployeeHobbys.add(addTempHobby);
        }

        return pmEmployeeHobbyMapper.toDto(pmEmployeeHobbys);
    }
}
