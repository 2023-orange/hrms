package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeHobbyDao;
import com.sunten.hrms.pm.domain.PmEmployeeHobby;
import com.sunten.hrms.pm.domain.PmEmployeeHobbyTemp;
import com.sunten.hrms.pm.dao.PmEmployeeHobbyTempDao;
import com.sunten.hrms.pm.mapper.PmEmployeeHobbyCheckMapper;
import com.sunten.hrms.pm.service.PmEmployeeHobbyTempService;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeHobbyTempMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 技术爱好临时表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeHobbyTempServiceImpl extends ServiceImpl<PmEmployeeHobbyTempDao, PmEmployeeHobbyTemp> implements PmEmployeeHobbyTempService {
    private final PmEmployeeHobbyTempDao pmEmployeeHobbyTempDao;
    private final PmEmployeeHobbyTempMapper pmEmployeeHobbyTempMapper;
    private final PmEmployeeHobbyDao pmEmployeeHobbyDao;
    private final PmEmployeeHobbyCheckMapper pmEmployeeHobbyCheckMapper;
    private final FndUserService fndUserService;

    public PmEmployeeHobbyTempServiceImpl(PmEmployeeHobbyTempDao pmEmployeeHobbyTempDao, PmEmployeeHobbyTempMapper pmEmployeeHobbyTempMapper, PmEmployeeHobbyDao pmEmployeeHobbyDao, PmEmployeeHobbyCheckMapper pmEmployeeHobbyCheckMapper, FndUserService fndUserService) {
        this.pmEmployeeHobbyTempDao = pmEmployeeHobbyTempDao;
        this.pmEmployeeHobbyTempMapper = pmEmployeeHobbyTempMapper;
        this.pmEmployeeHobbyDao = pmEmployeeHobbyDao;
        this.pmEmployeeHobbyCheckMapper = pmEmployeeHobbyCheckMapper;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeHobbyTempDTO insert(PmEmployeeHobbyTemp employeeHobbyTempNew) {
        pmEmployeeHobbyTempDao.insertAllColumn(employeeHobbyTempNew);
        return pmEmployeeHobbyTempMapper.toDto(employeeHobbyTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeHobbyTemp employeeHobbyTemp = Optional.ofNullable(pmEmployeeHobbyTempDao.getByKey(id)).orElseGet(PmEmployeeHobbyTemp::new);
        ValidationUtil.isNull(employeeHobbyTemp.getId(), "PmEmployeeHobbyTemp", "id", employeeHobbyTemp.getId());
        if (!fndUserService.isActiveUser(employeeHobbyTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeeHobbyTemp.setEnabledFlag(false);
        this.delete(employeeHobbyTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeHobbyTemp employeeHobbyTemp) {
        // 确认删除前是否需要做检查，只失效，不删除

        pmEmployeeHobbyTempDao.updateEnableFlag(employeeHobbyTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeHobbyTemp employeeHobbyTempNew) {
        PmEmployeeHobbyTemp employeeHobbyTempInDb = Optional.ofNullable(pmEmployeeHobbyTempDao.getByKey(employeeHobbyTempNew.getId())).orElseGet(PmEmployeeHobbyTemp::new);
        ValidationUtil.isNull(employeeHobbyTempInDb.getId() ,"EmployeeHobbyTemp", "id", employeeHobbyTempNew.getId());
        employeeHobbyTempNew.setId(employeeHobbyTempInDb.getId());
        pmEmployeeHobbyTempDao.updateAllColumnByKey(employeeHobbyTempNew);
    }

    @Override
    public PmEmployeeHobbyTempDTO getByKey(Long id) {
        PmEmployeeHobbyTemp employeeHobbyTemp = Optional.ofNullable(pmEmployeeHobbyTempDao.getByKey(id)).orElseGet(PmEmployeeHobbyTemp::new);
        ValidationUtil.isNull(employeeHobbyTemp.getId() ,"EmployeeHobbyTemp", "id", id);
        return pmEmployeeHobbyTempMapper.toDto(employeeHobbyTemp);
    }

    @Override
    public List<PmEmployeeHobbyTempDTO> listAll(PmEmployeeHobbyTempQueryCriteria criteria) {
        return pmEmployeeHobbyTempMapper.toDto(pmEmployeeHobbyTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeHobbyTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeHobbyTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeHobbyTemp> employeeHobbyTemps = pmEmployeeHobbyTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeHobbyTempMapper.toDto(employeeHobbyTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeHobbyTempDTO> employeeHobbyTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeHobbyTempDTO employeeHobbyTempDTO : employeeHobbyTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("技术爱好表id", employeeHobbyTempDTO.getEmployeeHobby().getId());
            map.put("员工id", employeeHobbyTempDTO.getEmployee().getId());
            map.put("技能爱好", employeeHobbyTempDTO.getHobby());
            map.put("级别", employeeHobbyTempDTO.getLevelMyself());
            map.put("认证等级", employeeHobbyTempDTO.getLevelMechanism());
            map.put("备注", employeeHobbyTempDTO.getRemarks());
            map.put("操作标记", employeeHobbyTempDTO.getInstructionsFlag());
            map.put("校核标记", employeeHobbyTempDTO.getCheckFlag());
            map.put("id", employeeHobbyTempDTO.getId());
            map.put("enabledFlag", employeeHobbyTempDTO.getEnabledFlag());
            map.put("createBy", employeeHobbyTempDTO.getCreateBy());
            map.put("updateTime", employeeHobbyTempDTO.getUpdateTime());
            map.put("updateBy", employeeHobbyTempDTO.getUpdateBy());
            map.put("createTime", employeeHobbyTempDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void check(PmEmployeeHobbyTemp employeeHobbyTemp) {
//校核
        pmEmployeeHobbyTempDao.updateCheckFlag(employeeHobbyTemp);
        PmEmployeeHobbyTemp vocationalTemp = pmEmployeeHobbyTempDao.getByKey(employeeHobbyTemp.getId());
        if ("Y".equals(vocationalTemp.getCheckFlag())) {
            PmEmployeeHobby pmEmployeeHobby = pmEmployeeHobbyCheckMapper.toEntity(vocationalTemp);
            if ("A".equals(vocationalTemp.getInstructionsFlag())) {
                pmEmployeeHobbyDao.insertAllColumn(pmEmployeeHobby);
            } else if ("U".equals(vocationalTemp.getInstructionsFlag())) {
                pmEmployeeHobbyDao.updateAllColumnByKey(pmEmployeeHobby);
            }
        }
    }
}
