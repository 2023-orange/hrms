package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.PmEmployeeFamilyDao;
import com.sunten.hrms.pm.dao.PmEmployeeFamilyTempDao;
import com.sunten.hrms.pm.domain.PmEmployeeFamily;
import com.sunten.hrms.pm.domain.PmEmployeeFamilyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeFamilyCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeFamilyTempMapper;
import com.sunten.hrms.pm.service.PmEmployeeFamilyTempService;
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
 * 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2020-08-25
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeFamilyTempServiceImpl extends ServiceImpl<PmEmployeeFamilyTempDao, PmEmployeeFamilyTemp> implements PmEmployeeFamilyTempService {
    private final PmEmployeeFamilyTempDao pmEmployeeFamilyTempDao;
    private final PmEmployeeFamilyTempMapper pmEmployeeFamilyTempMapper;
    private final PmEmployeeFamilyCheckMapper pmEmployeeFamilyCheckMapper;
    private final PmEmployeeFamilyDao pmEmployeeFamilyDao;
    private final FndUserService fndUserService;

    public PmEmployeeFamilyTempServiceImpl(PmEmployeeFamilyTempDao pmEmployeeFamilyTempDao, PmEmployeeFamilyTempMapper pmEmployeeFamilyTempMapper, PmEmployeeFamilyCheckMapper pmEmployeeFamilyCheckMapper, PmEmployeeFamilyDao pmEmployeeFamilyDao, FndUserService fndUserService) {
        this.pmEmployeeFamilyTempDao = pmEmployeeFamilyTempDao;
        this.pmEmployeeFamilyTempMapper = pmEmployeeFamilyTempMapper;
        this.pmEmployeeFamilyCheckMapper = pmEmployeeFamilyCheckMapper;
        this.pmEmployeeFamilyDao = pmEmployeeFamilyDao;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeFamilyTempDTO insert(PmEmployeeFamilyTemp employeeFamilyTempNew) {
        pmEmployeeFamilyTempDao.insertAllColumn(employeeFamilyTempNew);
        return pmEmployeeFamilyTempMapper.toDto(employeeFamilyTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeFamilyTemp employeeFamilyTemp = Optional.ofNullable(pmEmployeeFamilyTempDao.getByKey(id)).orElseGet(PmEmployeeFamilyTemp::new);
        ValidationUtil.isNull(employeeFamilyTemp.getId(), "EmployeeFamilyTemp", "id", id);
        if (!fndUserService.isActiveUser(employeeFamilyTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法删除");
        }
        employeeFamilyTemp.setEnabledFlag(false);
        this.delete(employeeFamilyTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeFamilyTemp employeeFamilyTemp) {
        //  确认删除前是否需要做检查,只失效，不删除
        pmEmployeeFamilyTempDao.updateEnableFlag(employeeFamilyTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeFamilyTemp employeeFamilyTempNew) {
        PmEmployeeFamilyTemp employeeFamilyTempInDb = Optional.ofNullable(pmEmployeeFamilyTempDao.getByKey(employeeFamilyTempNew.getId())).orElseGet(PmEmployeeFamilyTemp::new);
        ValidationUtil.isNull(employeeFamilyTempInDb.getId(), "EmployeeFamilyTemp", "id", employeeFamilyTempNew.getId());
        employeeFamilyTempNew.setId(employeeFamilyTempInDb.getId());
        pmEmployeeFamilyTempDao.updateAllColumnByKey(employeeFamilyTempNew);
    }

    @Override
    public PmEmployeeFamilyTempDTO getByKey(Long id) {
        PmEmployeeFamilyTemp employeeFamilyTemp = Optional.ofNullable(pmEmployeeFamilyTempDao.getByKey(id)).orElseGet(PmEmployeeFamilyTemp::new);
        ValidationUtil.isNull(employeeFamilyTemp.getId(), "EmployeeFamilyTemp", "id", id);
        return pmEmployeeFamilyTempMapper.toDto(employeeFamilyTemp);
    }

    @Override
    public List<PmEmployeeFamilyTempDTO> listAll(PmEmployeeFamilyTempQueryCriteria criteria) {
        return pmEmployeeFamilyTempMapper.toDto(pmEmployeeFamilyTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeFamilyTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeFamilyTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeFamilyTemp> employeeFamilyTemps = pmEmployeeFamilyTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeFamilyTempMapper.toDto(employeeFamilyTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeFamilyTempDTO> employeeFamilyTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeFamilyTempDTO employeeFamilyTempDTO : employeeFamilyTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeFamilyTempDTO.getEmployee().getId());
            map.put("姓名", employeeFamilyTempDTO.getName());
            map.put("关系", employeeFamilyTempDTO.getRelationship());
            map.put("单位", employeeFamilyTempDTO.getCompany());
            map.put("职务", employeeFamilyTempDTO.getPost());
            map.put("电话", employeeFamilyTempDTO.getTele());
            map.put("性别", employeeFamilyTempDTO.getGender());
            map.put("出生日期", employeeFamilyTempDTO.getBirthday());
            map.put("手机", employeeFamilyTempDTO.getMobilePhone());
            map.put("弹性域1", employeeFamilyTempDTO.getAttribute1());
            map.put("弹性域2", employeeFamilyTempDTO.getAttribute2());
            map.put("弹性域3", employeeFamilyTempDTO.getAttribute3());
            map.put("有效标记默认值", employeeFamilyTempDTO.getEnabledFlag());
            map.put("操作标记，A添加，U修改", employeeFamilyTempDTO.getInstructionsFlag());
            map.put("校核标记，D待校核，Y通过，N不通过", employeeFamilyTempDTO.getCheckFlag());
            map.put("备注", employeeFamilyTempDTO.getRemarks());
            map.put("原家庭情况表ID", employeeFamilyTempDTO.getEmployeeFamily().getId());
            map.put("id", employeeFamilyTempDTO.getId());
            map.put("创建时间", employeeFamilyTempDTO.getCreateTime());
            map.put("创建人ID", employeeFamilyTempDTO.getCreateBy());
            map.put("修改时间", employeeFamilyTempDTO.getUpdateTime());
            map.put("修改人ID", employeeFamilyTempDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void check(PmEmployeeFamilyTemp pmEmployeeFamilyTemp) {
        //校核通过时，反写进正式表
        pmEmployeeFamilyTempDao.updateCheckFlag(pmEmployeeFamilyTemp);
        PmEmployeeFamilyTemp familyTemp = pmEmployeeFamilyTempDao.getByKey(pmEmployeeFamilyTemp.getId());
        if ("Y".equals(pmEmployeeFamilyTemp.getCheckFlag())) {
            PmEmployeeFamily pmEmployeeFamily = pmEmployeeFamilyCheckMapper.toEntity(familyTemp);
            if ("A".equals(familyTemp.getInstructionsFlag())) {// 添加
                pmEmployeeFamilyDao.insertAllColumn(pmEmployeeFamily);
            } else if ("U".equals(familyTemp.getInstructionsFlag())) {// 修改
                pmEmployeeFamilyDao.updateAllColumnByKey(pmEmployeeFamily);
            }
        }
    }
}
