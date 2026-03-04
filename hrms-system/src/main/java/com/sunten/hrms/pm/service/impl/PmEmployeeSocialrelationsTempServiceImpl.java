package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeSocialrelationsDao;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelations;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelationsTemp;
import com.sunten.hrms.pm.dao.PmEmployeeSocialrelationsTempDao;
import com.sunten.hrms.pm.mapper.PmEmployeeSocialrelationsCheckMapper;
import com.sunten.hrms.pm.service.PmEmployeeSocialrelationsTempService;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeSocialrelationsTempMapper;
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
 * 社会关系临时表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeSocialrelationsTempServiceImpl extends ServiceImpl<PmEmployeeSocialrelationsTempDao, PmEmployeeSocialrelationsTemp> implements PmEmployeeSocialrelationsTempService {
    private final PmEmployeeSocialrelationsTempDao pmEmployeeSocialrelationsTempDao;
    private final PmEmployeeSocialrelationsTempMapper pmEmployeeSocialrelationsTempMapper;
    private final FndUserService fndUserService;
    private final PmEmployeeSocialrelationsCheckMapper pmEmployeeSocialrelationsCheckMapper;
    private final PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao;

    public PmEmployeeSocialrelationsTempServiceImpl(PmEmployeeSocialrelationsTempDao pmEmployeeSocialrelationsTempDao, PmEmployeeSocialrelationsTempMapper pmEmployeeSocialrelationsTempMapper, FndUserService fndUserService, PmEmployeeSocialrelationsCheckMapper pmEmployeeSocialrelationsCheckMapper, PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao) {
        this.pmEmployeeSocialrelationsTempDao = pmEmployeeSocialrelationsTempDao;
        this.pmEmployeeSocialrelationsTempMapper = pmEmployeeSocialrelationsTempMapper;
        this.fndUserService = fndUserService;
        this.pmEmployeeSocialrelationsCheckMapper = pmEmployeeSocialrelationsCheckMapper;
        this.pmEmployeeSocialrelationsDao = pmEmployeeSocialrelationsDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeSocialrelationsTempDTO insert(PmEmployeeSocialrelationsTemp employeeSocialrelationsTempNew) {
        pmEmployeeSocialrelationsTempDao.insertAllColumn(employeeSocialrelationsTempNew);
        return pmEmployeeSocialrelationsTempMapper.toDto(employeeSocialrelationsTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeSocialrelationsTemp employeeSocialrelationsTempInDb = Optional.ofNullable(pmEmployeeSocialrelationsTempDao.getByKey(id)).orElseGet(PmEmployeeSocialrelationsTemp::new);
        ValidationUtil.isNull(employeeSocialrelationsTempInDb.getId() ,"EmployeeSocialrelationsTemp", "id", id);
        if (!fndUserService.isActiveUser(employeeSocialrelationsTempInDb.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeeSocialrelationsTempInDb.setEnabledFlag(false);
        this.delete(employeeSocialrelationsTempInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp) {
        // TODO    确认删除前是否需要做检查
        pmEmployeeSocialrelationsTempDao.updateEnableFlag(employeeSocialrelationsTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeSocialrelationsTemp employeeSocialrelationsTempNew) {
        PmEmployeeSocialrelationsTemp employeeSocialrelationsTempInDb = Optional.ofNullable(pmEmployeeSocialrelationsTempDao.getByKey(employeeSocialrelationsTempNew.getId())).orElseGet(PmEmployeeSocialrelationsTemp::new);
        ValidationUtil.isNull(employeeSocialrelationsTempInDb.getId() ,"EmployeeSocialrelationsTemp", "id", employeeSocialrelationsTempNew.getId());
        employeeSocialrelationsTempNew.setId(employeeSocialrelationsTempInDb.getId());
        pmEmployeeSocialrelationsTempDao.updateAllColumnByKey(employeeSocialrelationsTempNew);
    }

    @Override
    public PmEmployeeSocialrelationsTempDTO getByKey(Long id) {
        PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp = Optional.ofNullable(pmEmployeeSocialrelationsTempDao.getByKey(id)).orElseGet(PmEmployeeSocialrelationsTemp::new);
        ValidationUtil.isNull(employeeSocialrelationsTemp.getId() ,"EmployeeSocialrelationsTemp", "id", id);
        return pmEmployeeSocialrelationsTempMapper.toDto(employeeSocialrelationsTemp);
    }

    @Override
    public List<PmEmployeeSocialrelationsTempDTO> listAll(PmEmployeeSocialrelationsTempQueryCriteria criteria) {
        return pmEmployeeSocialrelationsTempMapper.toDto(pmEmployeeSocialrelationsTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeSocialrelationsTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeSocialrelationsTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeSocialrelationsTemp> employeeSocialrelationsTemps = pmEmployeeSocialrelationsTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeSocialrelationsTempMapper.toDto(employeeSocialrelationsTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeSocialrelationsTempDTO> employeeSocialrelationsTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeSocialrelationsTempDTO employeeSocialrelationsTempDTO : employeeSocialrelationsTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("社会关系表id", employeeSocialrelationsTempDTO.getEmployeeSocialrelations().getId());
            map.put("员工id", employeeSocialrelationsTempDTO.getEmployee().getId());
            map.put("姓名", employeeSocialrelationsTempDTO.getName());
            map.put("关系", employeeSocialrelationsTempDTO.getRelationship());
            map.put("单位", employeeSocialrelationsTempDTO.getCompany());
            map.put("职务", employeeSocialrelationsTempDTO.getPost());
            map.put("电话", employeeSocialrelationsTempDTO.getTele());
            map.put("是否在厂工作", employeeSocialrelationsTempDTO.getInFactoryFlag());
            map.put("操作标记", employeeSocialrelationsTempDTO.getInstructionsFlag());
            map.put("校核标记", employeeSocialrelationsTempDTO.getCheckFlag());
            map.put("id", employeeSocialrelationsTempDTO.getId());
            map.put("enabledFlag", employeeSocialrelationsTempDTO.getEnabledFlag());
            map.put("createBy", employeeSocialrelationsTempDTO.getCreateBy());
            map.put("updateTime", employeeSocialrelationsTempDTO.getUpdateTime());
            map.put("createTime", employeeSocialrelationsTempDTO.getCreateTime());
            map.put("updateBy", employeeSocialrelationsTempDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void check(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp) {
        //校核
        pmEmployeeSocialrelationsTempDao.updateCheckFlag(employeeSocialrelationsTemp);
        PmEmployeeSocialrelationsTemp socialrelationsTemp = pmEmployeeSocialrelationsTempDao.getByKey(employeeSocialrelationsTemp.getId());
        if ("Y".equals(socialrelationsTemp.getCheckFlag())) {
            PmEmployeeSocialrelations pmEmployeeSocialrelations = pmEmployeeSocialrelationsCheckMapper.toEntity(socialrelationsTemp);
            if ("A".equals(socialrelationsTemp.getInstructionsFlag())) {
                pmEmployeeSocialrelationsDao.insertAllColumn(pmEmployeeSocialrelations);
            } else if ("U".equals(socialrelationsTemp.getInstructionsFlag())) {
                pmEmployeeSocialrelationsDao.updateAllColumnByKey(pmEmployeeSocialrelations);
            }
        }
    }
}
