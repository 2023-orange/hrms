package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeWorkhistoryDao;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistory;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistoryTemp;
import com.sunten.hrms.pm.dao.PmEmployeeWorkhistoryTempDao;
import com.sunten.hrms.pm.mapper.PmEmployeeWorkhistoryCheckMapper;
import com.sunten.hrms.pm.service.PmEmployeeWorkhistoryTempService;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeWorkhistoryTempMapper;
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
 * 工作经历临时表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeWorkhistoryTempServiceImpl extends ServiceImpl<PmEmployeeWorkhistoryTempDao, PmEmployeeWorkhistoryTemp> implements PmEmployeeWorkhistoryTempService {
    private final PmEmployeeWorkhistoryTempDao pmEmployeeWorkhistoryTempDao;
    private final PmEmployeeWorkhistoryTempMapper pmEmployeeWorkhistoryTempMapper;
    private final FndUserService fndUserService;
    private final PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao;
    private final PmEmployeeWorkhistoryCheckMapper pmEmployeeWorkhistoryCheckMapper;

    public PmEmployeeWorkhistoryTempServiceImpl(PmEmployeeWorkhistoryTempDao pmEmployeeWorkhistoryTempDao, PmEmployeeWorkhistoryTempMapper pmEmployeeWorkhistoryTempMapper, FndUserService fndUserService, PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao, PmEmployeeWorkhistoryCheckMapper pmEmployeeWorkhistoryCheckMapper) {
        this.pmEmployeeWorkhistoryTempDao = pmEmployeeWorkhistoryTempDao;
        this.pmEmployeeWorkhistoryTempMapper = pmEmployeeWorkhistoryTempMapper;
        this.fndUserService = fndUserService;
        this.pmEmployeeWorkhistoryDao = pmEmployeeWorkhistoryDao;
        this.pmEmployeeWorkhistoryCheckMapper = pmEmployeeWorkhistoryCheckMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeWorkhistoryTempDTO insert(PmEmployeeWorkhistoryTemp employeeWorkhistoryTempNew) {
        pmEmployeeWorkhistoryTempDao.insertAllColumn(employeeWorkhistoryTempNew);
        return pmEmployeeWorkhistoryTempMapper.toDto(employeeWorkhistoryTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeWorkhistoryTemp employeeWorkhistoryTempInDb = Optional.ofNullable(pmEmployeeWorkhistoryTempDao.getByKey(id)).orElseGet(PmEmployeeWorkhistoryTemp::new);
        ValidationUtil.isNull(employeeWorkhistoryTempInDb.getId() ,"EmployeeWorkhistoryTemp", "id", id);
        if (!fndUserService.isActiveUser(employeeWorkhistoryTempInDb.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeeWorkhistoryTempInDb.setEnabledFlag(false);
        this.delete(employeeWorkhistoryTempInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp) {
        // TODO    确认删除前是否需要做检查
        pmEmployeeWorkhistoryTempDao.updateEnableFlag(employeeWorkhistoryTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeWorkhistoryTemp employeeWorkhistoryTempNew) {
        PmEmployeeWorkhistoryTemp employeeWorkhistoryTempInDb = Optional.ofNullable(pmEmployeeWorkhistoryTempDao.getByKey(employeeWorkhistoryTempNew.getId())).orElseGet(PmEmployeeWorkhistoryTemp::new);
        ValidationUtil.isNull(employeeWorkhistoryTempInDb.getId() ,"EmployeeWorkhistoryTemp", "id", employeeWorkhistoryTempNew.getId());
        employeeWorkhistoryTempNew.setId(employeeWorkhistoryTempInDb.getId());
        pmEmployeeWorkhistoryTempDao.updateAllColumnByKey(employeeWorkhistoryTempNew);
    }

    @Override
    public PmEmployeeWorkhistoryTempDTO getByKey(Long id) {
        PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp = Optional.ofNullable(pmEmployeeWorkhistoryTempDao.getByKey(id)).orElseGet(PmEmployeeWorkhistoryTemp::new);
        ValidationUtil.isNull(employeeWorkhistoryTemp.getId() ,"EmployeeWorkhistoryTemp", "id", id);
        return pmEmployeeWorkhistoryTempMapper.toDto(employeeWorkhistoryTemp);
    }

    @Override
    public List<PmEmployeeWorkhistoryTempDTO> listAll(PmEmployeeWorkhistoryTempQueryCriteria criteria) {
        return pmEmployeeWorkhistoryTempMapper.toDto(pmEmployeeWorkhistoryTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeWorkhistoryTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeWorkhistoryTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeWorkhistoryTemp> employeeWorkhistoryTemps = pmEmployeeWorkhistoryTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeWorkhistoryTempMapper.toDto(employeeWorkhistoryTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeWorkhistoryTempDTO> employeeWorkhistoryTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeWorkhistoryTempDTO employeeWorkhistoryTempDTO : employeeWorkhistoryTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeWorkhistoryTempDTO.getEmployee().getName());
            map.put("单位", employeeWorkhistoryTempDTO.getCompany());
            map.put("职务", employeeWorkhistoryTempDTO.getPost());
            map.put("开始时间", employeeWorkhistoryTempDTO.getStartTime());
            map.put("结束时间", employeeWorkhistoryTempDTO.getEndTime());
            map.put("联系电话", employeeWorkhistoryTempDTO.getTele());
            map.put("备注", employeeWorkhistoryTempDTO.getRemarks());
            map.put("操作标记", employeeWorkhistoryTempDTO.getInstructionsFlag());
            map.put("校核标记", employeeWorkhistoryTempDTO.getCheckFlag());
            map.put("enabledFlag", employeeWorkhistoryTempDTO.getEnabledFlag());
            map.put("id", employeeWorkhistoryTempDTO.getId());
            map.put("updateTime", employeeWorkhistoryTempDTO.getUpdateTime());
            map.put("createBy", employeeWorkhistoryTempDTO.getCreateBy());
            map.put("createTime", employeeWorkhistoryTempDTO.getCreateTime());
            map.put("updateBy", employeeWorkhistoryTempDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void check(PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp) {
        //校核
        pmEmployeeWorkhistoryTempDao.updateCheckFlag(employeeWorkhistoryTemp);
        PmEmployeeWorkhistoryTemp workhistoryTemp = pmEmployeeWorkhistoryTempDao.getByKey(employeeWorkhistoryTemp.getId());
        if ("Y".equals(workhistoryTemp.getCheckFlag())) {
            PmEmployeeWorkhistory pmEmployeeWorkhistory = pmEmployeeWorkhistoryCheckMapper.toEntity(workhistoryTemp);
            if ("A".equals(workhistoryTemp.getInstructionsFlag())) {
                pmEmployeeWorkhistoryDao.insertAllColumn(pmEmployeeWorkhistory);
            } else if ("U".equals(workhistoryTemp.getInstructionsFlag())) {
                pmEmployeeWorkhistoryDao.updateAllColumnByKey(pmEmployeeWorkhistory);
            }
        }
    }
}
