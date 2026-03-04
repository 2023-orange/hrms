package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeePoliticalDao;
import com.sunten.hrms.pm.domain.PmEmployeePolitical;
import com.sunten.hrms.pm.domain.PmEmployeePoliticalTemp;
import com.sunten.hrms.pm.dao.PmEmployeePoliticalTempDao;
import com.sunten.hrms.pm.mapper.PmEmployeePoliticalCheckMapper;
import com.sunten.hrms.pm.service.PmEmployeePoliticalTempService;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeePoliticalTempMapper;
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
 * 员工政治面貌临时表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeePoliticalTempServiceImpl extends ServiceImpl<PmEmployeePoliticalTempDao, PmEmployeePoliticalTemp> implements PmEmployeePoliticalTempService {
    private final PmEmployeePoliticalTempDao pmEmployeePoliticalTempDao;
    private final PmEmployeePoliticalTempMapper pmEmployeePoliticalTempMapper;
    private final FndUserService fndUserService;
    private final PmEmployeePoliticalDao pmEmployeePoliticalDao;
    private final PmEmployeePoliticalCheckMapper pmEmployeePoliticalCheckMapper;

    public PmEmployeePoliticalTempServiceImpl(PmEmployeePoliticalTempDao pmEmployeePoliticalTempDao, PmEmployeePoliticalTempMapper pmEmployeePoliticalTempMapper, FndUserService fndUserService, PmEmployeePoliticalDao pmEmployeePoliticalDao, PmEmployeePoliticalCheckMapper pmEmployeePoliticalCheckMapper) {
        this.pmEmployeePoliticalTempDao = pmEmployeePoliticalTempDao;
        this.pmEmployeePoliticalTempMapper = pmEmployeePoliticalTempMapper;
        this.fndUserService = fndUserService;
        this.pmEmployeePoliticalDao = pmEmployeePoliticalDao;
        this.pmEmployeePoliticalCheckMapper = pmEmployeePoliticalCheckMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeePoliticalTempDTO insert(PmEmployeePoliticalTemp employeePoliticalTempNew) {
        pmEmployeePoliticalTempDao.insertAllColumn(employeePoliticalTempNew);
        return pmEmployeePoliticalTempMapper.toDto(employeePoliticalTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeePoliticalTemp employeePoliticalTempInDb = Optional.ofNullable(pmEmployeePoliticalTempDao.getByKey(id)).orElseGet(PmEmployeePoliticalTemp::new);
        ValidationUtil.isNull(employeePoliticalTempInDb.getId() ,"EmployeePoliticalTemp", "id", id);
        if (!fndUserService.isActiveUser(employeePoliticalTempInDb.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeePoliticalTempInDb.setEnabledFlag(false);
        this.delete(employeePoliticalTempInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeePoliticalTemp employeePoliticalTemp) {
        // 确认删除前是否需要做检查，只失效，不删除

        pmEmployeePoliticalTempDao.updateEnableFlag(employeePoliticalTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeePoliticalTemp employeePoliticalTempNew) {
        PmEmployeePoliticalTemp employeePoliticalTempInDb = Optional.ofNullable(pmEmployeePoliticalTempDao.getByKey(employeePoliticalTempNew.getId())).orElseGet(PmEmployeePoliticalTemp::new);
        ValidationUtil.isNull(employeePoliticalTempInDb.getId() ,"EmployeePoliticalTemp", "id", employeePoliticalTempNew.getId());
        employeePoliticalTempNew.setId(employeePoliticalTempInDb.getId());
        pmEmployeePoliticalTempDao.updateAllColumnByKey(employeePoliticalTempNew);
    }

    @Override
    public PmEmployeePoliticalTempDTO getByKey(Long id) {
        PmEmployeePoliticalTemp employeePoliticalTemp = Optional.ofNullable(pmEmployeePoliticalTempDao.getByKey(id)).orElseGet(PmEmployeePoliticalTemp::new);
        ValidationUtil.isNull(employeePoliticalTemp.getId() ,"EmployeePoliticalTemp", "id", id);
        return pmEmployeePoliticalTempMapper.toDto(employeePoliticalTemp);
    }

    @Override
    public List<PmEmployeePoliticalTempDTO> listAll(PmEmployeePoliticalTempQueryCriteria criteria) {
        return pmEmployeePoliticalTempMapper.toDto(pmEmployeePoliticalTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeePoliticalTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeePoliticalTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeePoliticalTemp> employeePoliticalTemps = pmEmployeePoliticalTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeePoliticalTempMapper.toDto(employeePoliticalTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeePoliticalTempDTO> employeePoliticalTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeePoliticalTempDTO employeePoliticalTempDTO : employeePoliticalTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("政治面貌表id", employeePoliticalTempDTO.getEmployeePolitical().getId());
            map.put("员工id", employeePoliticalTempDTO.getEmployee().getId());
            map.put("政治面貌", employeePoliticalTempDTO.getPolitical());
            map.put("加入时间", employeePoliticalTempDTO.getJoiningTime());
            map.put("性质", employeePoliticalTempDTO.getNature());
            map.put("转正时间", employeePoliticalTempDTO.getFormalTime());
            map.put("职务", employeePoliticalTempDTO.getPost());
            map.put("开始时间", employeePoliticalTempDTO.getStartTime());
            map.put("结束时间", employeePoliticalTempDTO.getEndTime());
            map.put("操作标记", employeePoliticalTempDTO.getInstructionsFlag());
            map.put("校核标记", employeePoliticalTempDTO.getCheckFlag());
            map.put("enabledFlag", employeePoliticalTempDTO.getEnabledFlag());
            map.put("id", employeePoliticalTempDTO.getId());
            map.put("createTime", employeePoliticalTempDTO.getCreateTime());
            map.put("updateTime", employeePoliticalTempDTO.getUpdateTime());
            map.put("createBy", employeePoliticalTempDTO.getCreateBy());
            map.put("updateBy", employeePoliticalTempDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void check(PmEmployeePoliticalTemp employeePoliticalTemp) {
        pmEmployeePoliticalTempDao.updateCheckFlag(employeePoliticalTemp);
        PmEmployeePoliticalTemp politicalTemp = pmEmployeePoliticalTempDao.getByKey(employeePoliticalTemp.getId());
        if ("Y".equals(politicalTemp.getCheckFlag())) {
            PmEmployeePolitical pmEmployeePolitical = pmEmployeePoliticalCheckMapper.toEntity(politicalTemp);
            if ("A".equals(politicalTemp.getInstructionsFlag())) {
                pmEmployeePoliticalDao.insertAllColumn(pmEmployeePolitical);
            } else if ("U".equals(politicalTemp.getInstructionsFlag())) {
                pmEmployeePoliticalDao.updateAllColumnByKey(pmEmployeePolitical);
            }
        }
    }
}
