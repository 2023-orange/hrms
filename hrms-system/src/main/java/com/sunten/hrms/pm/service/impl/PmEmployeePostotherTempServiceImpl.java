package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeePostotherDao;
import com.sunten.hrms.pm.domain.PmEmployeePostother;
import com.sunten.hrms.pm.domain.PmEmployeePostotherTemp;
import com.sunten.hrms.pm.dao.PmEmployeePostotherTempDao;
import com.sunten.hrms.pm.mapper.PmEmployeePostotherCheckMapper;
import com.sunten.hrms.pm.service.PmEmployeePostotherTempService;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeePostotherTempMapper;
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
 * 工作外职务临时表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeePostotherTempServiceImpl extends ServiceImpl<PmEmployeePostotherTempDao, PmEmployeePostotherTemp> implements PmEmployeePostotherTempService {
    private final PmEmployeePostotherTempDao pmEmployeePostotherTempDao;
    private final PmEmployeePostotherTempMapper pmEmployeePostotherTempMapper;
    private final PmEmployeePostotherCheckMapper pmEmployeePostotherCheckMapper;
    private final PmEmployeePostotherDao pmEmployeePostotherDao;
    private final FndUserService fndUserService;

    public PmEmployeePostotherTempServiceImpl(PmEmployeePostotherTempDao pmEmployeePostotherTempDao, PmEmployeePostotherTempMapper pmEmployeePostotherTempMapper, PmEmployeePostotherCheckMapper pmEmployeePostotherCheckMapper, PmEmployeePostotherDao pmEmployeePostotherDao, FndUserService fndUserService) {
        this.pmEmployeePostotherTempDao = pmEmployeePostotherTempDao;
        this.pmEmployeePostotherTempMapper = pmEmployeePostotherTempMapper;
        this.pmEmployeePostotherCheckMapper = pmEmployeePostotherCheckMapper;
        this.pmEmployeePostotherDao = pmEmployeePostotherDao;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeePostotherTempDTO insert(PmEmployeePostotherTemp employeePostotherTempNew) {
        pmEmployeePostotherTempDao.insertAllColumn(employeePostotherTempNew);
        return pmEmployeePostotherTempMapper.toDto(employeePostotherTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeePostotherTemp employeePostotherTempInDb = Optional.ofNullable(pmEmployeePostotherTempDao.getByKey(id)).orElseGet(PmEmployeePostotherTemp::new);
        ValidationUtil.isNull(employeePostotherTempInDb.getId() ,"EmployeePostotherTemp", "id", id);
        if (!fndUserService.isActiveUser(employeePostotherTempInDb.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeePostotherTempInDb.setEnabledFlag(false);
        this.delete(employeePostotherTempInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeePostotherTemp employeePostotherTemp) {
        // TODO    确认删除前是否需要做检查
        pmEmployeePostotherTempDao.updateEnableFlag(employeePostotherTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeePostotherTemp employeePostotherTempNew) {
        PmEmployeePostotherTemp employeePostotherTempInDb = Optional.ofNullable(pmEmployeePostotherTempDao.getByKey(employeePostotherTempNew.getId())).orElseGet(PmEmployeePostotherTemp::new);
        ValidationUtil.isNull(employeePostotherTempInDb.getId() ,"EmployeePostotherTemp", "id", employeePostotherTempNew.getId());
        employeePostotherTempNew.setId(employeePostotherTempInDb.getId());
        pmEmployeePostotherTempDao.updateAllColumnByKey(employeePostotherTempNew);
    }

    @Override
    public PmEmployeePostotherTempDTO getByKey(Long id) {
        PmEmployeePostotherTemp employeePostotherTemp = Optional.ofNullable(pmEmployeePostotherTempDao.getByKey(id)).orElseGet(PmEmployeePostotherTemp::new);
        ValidationUtil.isNull(employeePostotherTemp.getId() ,"EmployeePostotherTemp", "id", id);
        return pmEmployeePostotherTempMapper.toDto(employeePostotherTemp);
    }

    @Override
    public List<PmEmployeePostotherTempDTO> listAll(PmEmployeePostotherTempQueryCriteria criteria) {
        return pmEmployeePostotherTempMapper.toDto(pmEmployeePostotherTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeePostotherTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeePostotherTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeePostotherTemp> employeePostotherTemps = pmEmployeePostotherTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeePostotherTempMapper.toDto(employeePostotherTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeePostotherTempDTO> employeePostotherTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeePostotherTempDTO employeePostotherTempDTO : employeePostotherTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("政治面貌表id", employeePostotherTempDTO.getEmployeePostother().getId());
            map.put("员工id", employeePostotherTempDTO.getEmployee().getId());
            map.put("单位", employeePostotherTempDTO.getCompany());
            map.put("职务", employeePostotherTempDTO.getPost());
            map.put("备注", employeePostotherTempDTO.getRemarks());
            map.put("操作标记", employeePostotherTempDTO.getInstructionsFlag());
            map.put("校核标记", employeePostotherTempDTO.getCheckFlag());
            map.put("enabledFlag", employeePostotherTempDTO.getEnabledFlag());
            map.put("id", employeePostotherTempDTO.getId());
            map.put("updateTime", employeePostotherTempDTO.getUpdateTime());
            map.put("updateBy", employeePostotherTempDTO.getUpdateBy());
            map.put("createTime", employeePostotherTempDTO.getCreateTime());
            map.put("createBy", employeePostotherTempDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void check(PmEmployeePostotherTemp employeePostotherTemp) {
        //校核
        pmEmployeePostotherTempDao.updateCheckFlag(employeePostotherTemp);
        PmEmployeePostotherTemp postotherTemp = pmEmployeePostotherTempDao.getByKey(employeePostotherTemp.getId());
        if ("Y".equals(postotherTemp.getCheckFlag())) {
            PmEmployeePostother pmEmployeeHobby = pmEmployeePostotherCheckMapper.toEntity(postotherTemp);
            if ("A".equals(postotherTemp.getInstructionsFlag())) {
                pmEmployeePostotherDao.insertAllColumn(pmEmployeeHobby);
            } else if ("U".equals(postotherTemp.getInstructionsFlag())) {
                pmEmployeePostotherDao.updateAllColumnByKey(pmEmployeeHobby);
            }
        }
    }
}
