package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.dao.PmEmployeeTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeTemp;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeTempMapper;
import com.sunten.hrms.pm.service.*;
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
 * 人员临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeTempServiceImpl extends ServiceImpl<PmEmployeeTempDao, PmEmployeeTemp> implements PmEmployeeTempService {
    private final PmEmployeeTempDao pmEmployeeTempDao;
    private final PmEmployeeTempMapper pmEmployeeTempMapper;
    private final PmEmployeeService pmEmployeeService;
    private final PmEmployeeDao pmEmployeeDao;
    private final PmEmployeeEducationService pmEmployeeEducationService;
    private final PmEmployeeVocationalService pmEmployeeVocationalService;
    private final PmEmployeeFamilyService pmEmployeeFamilyService;
    private final PmEmployeePostotherService pmEmployeePostotherService;
    private final PmEmployeePoliticalService pmEmployeePoliticalService;
    private final PmEmployeeTitleService pmEmployeeTitleService;
    private final PmEmployeeWorkhistoryService pmEmployeeWorkhistoryService;
    private final PmEmployeeHobbyService pmEmployeeHobbyService;
    private final PmEmployeeSocialrelationsService pmEmployeeSocialrelationsService;
    private final PmEmployeeContractService pmEmployeeContractService;


    public PmEmployeeTempServiceImpl(PmEmployeeTempDao pmEmployeeTempDao, PmEmployeeTempMapper pmEmployeeTempMapper,
                                     PmEmployeeService pmEmployeeService, PmEmployeeDao pmEmployeeDao, PmEmployeeEducationService pmEmployeeEducationService,
                                     PmEmployeeVocationalService pmEmployeeVocationalService, PmEmployeeFamilyService pmEmployeeFamilyService,
                                     PmEmployeePostotherService pmEmployeePostotherService, PmEmployeePoliticalService pmEmployeePoliticalService,
                                     PmEmployeeTitleService pmEmployeeTitleService, PmEmployeeWorkhistoryService pmEmployeeWorkhistoryService,
                                     PmEmployeeHobbyService pmEmployeeHobbyService, PmEmployeeSocialrelationsService pmEmployeeSocialrelationsService,
                                     PmEmployeeContractService pmEmployeeContractService) {
        this.pmEmployeeTempDao = pmEmployeeTempDao;
        this.pmEmployeeTempMapper = pmEmployeeTempMapper;
        this.pmEmployeeService = pmEmployeeService;
        this.pmEmployeeDao = pmEmployeeDao;
        this.pmEmployeeEducationService = pmEmployeeEducationService;
        this.pmEmployeeVocationalService = pmEmployeeVocationalService;
        this.pmEmployeeFamilyService = pmEmployeeFamilyService;
        this.pmEmployeePostotherService = pmEmployeePostotherService;
        this.pmEmployeePoliticalService = pmEmployeePoliticalService;
        this.pmEmployeeTitleService = pmEmployeeTitleService;
        this.pmEmployeeWorkhistoryService = pmEmployeeWorkhistoryService;
        this.pmEmployeeHobbyService = pmEmployeeHobbyService;
        this.pmEmployeeSocialrelationsService = pmEmployeeSocialrelationsService;
        this.pmEmployeeContractService = pmEmployeeContractService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeTempDTO insert(PmEmployeeTemp employeeTempNew) {
        PmEmployeeTemp pmEmployeeTemp = pmEmployeeTempDao.getByEmployeeId(employeeTempNew.getEmployee().getId());
        if (pmEmployeeTemp != null && pmEmployeeTemp.getId() != null) {//如果存在修改记录，则失效前修改记录
            pmEmployeeTemp.setEnabledFlag(false);
            pmEmployeeTempDao.updateEnableFlag(pmEmployeeTemp);
        }
        pmEmployeeTempDao.insertAllColumn(employeeTempNew);
        return pmEmployeeTempMapper.toDto(employeeTempNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeTemp employeeTemp = new PmEmployeeTemp();
        employeeTemp.setId(id);
        employeeTemp.setEnabledFlag(false);
        this.delete(employeeTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeTemp employeeTemp) {
        // 确认删除前是否需要做检查,只失效，不删除
        pmEmployeeTempDao.updateEnableFlag(employeeTemp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeTemp employeeTempNew) {
        PmEmployeeTemp employeeTempInDb = Optional.ofNullable(pmEmployeeTempDao.getByKey(employeeTempNew.getId())).orElseGet(PmEmployeeTemp::new);
        ValidationUtil.isNull(employeeTempInDb.getId(), "EmployeeTemp", "id", employeeTempNew.getId());
        employeeTempNew.setId(employeeTempInDb.getId());
        pmEmployeeTempDao.updateAllColumnByKey(employeeTempNew);
    }

    @Override
    public PmEmployeeTempDTO getByKey(Long id) {
        PmEmployeeTemp employeeTemp = Optional.ofNullable(pmEmployeeTempDao.getByKey(id)).orElseGet(PmEmployeeTemp::new);
        ValidationUtil.isNull(employeeTemp.getId(), "EmployeeTemp", "id", id);
        return pmEmployeeTempMapper.toDto(employeeTemp);
    }

    @Override
    public List<PmEmployeeTempDTO> listAll(PmEmployeeTempQueryCriteria criteria) {
        return pmEmployeeTempMapper.toDto(pmEmployeeTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeTempQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeTemp> page = PageUtil.startPage(pageable);
        List<PmEmployeeTemp> employeeTemps = pmEmployeeTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeTempMapper.toDto(employeeTemps), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeTempDTO> employeeTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeTempDTO employeeTempDTO : employeeTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeTempDTO.getEmployeeDTO().getId());
            map.put("身高", employeeTempDTO.getHeight());
            map.put("体重", employeeTempDTO.getWeight());
            map.put("婚姻状态", employeeTempDTO.getMaritalStatus());
            map.put("户口性质", employeeTempDTO.getHouseholdCharacter());
            map.put("现在住址", employeeTempDTO.getAddress());
            map.put("现住邮编", employeeTempDTO.getZipcode());
            map.put("户口地址", employeeTempDTO.getHouseholdAddress());
            map.put("户口邮编", employeeTempDTO.getHouseholdZipcode());
            map.put("是否集体户口", employeeTempDTO.getCollectiveHouseholdFlag());
            map.put("集体户口所在地", employeeTempDTO.getCollectiveAddress());
            map.put("籍贯", employeeTempDTO.getNativePlace());
            map.put("操作标记", employeeTempDTO.getInstructionsFlag());
            map.put("校核标记", employeeTempDTO.getCheckFlag());
            map.put("备注", employeeTempDTO.getRemarks());
            map.put("弹性域1", employeeTempDTO.getAttribute1());
            map.put("弹性域2", employeeTempDTO.getAttribute2());
            map.put("弹性域3", employeeTempDTO.getAttribute3());
            map.put("弹性域4", employeeTempDTO.getAttribute4());
            map.put("弹性域5", employeeTempDTO.getAttribute5());
            map.put("有效标记默认值", employeeTempDTO.getEnabledFlag());
            map.put("id", employeeTempDTO.getId());
            map.put("updateTime", employeeTempDTO.getUpdateTime());
            map.put("updateBy", employeeTempDTO.getUpdateBy());
            map.put("createTime", employeeTempDTO.getCreateTime());
            map.put("createBy", employeeTempDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PmEmployeeTempDTO getByEmployeeId(Long employeeId) {
        PmEmployeeTemp pmEmployeeTemp = pmEmployeeTempDao.getByEmployeeId(employeeId);
        PmEmployeeTempDTO pmEmployeeTempDTO = null;
        if (pmEmployeeTemp == null) {
            pmEmployeeTempDTO = new PmEmployeeTempDTO();
            pmEmployeeTempDTO.setChangeFlag(false);
        } else {
            pmEmployeeTempDTO = pmEmployeeTempMapper.toDto(pmEmployeeTemp);
            pmEmployeeTempDTO.setChangeFlag(true);
        }
        PmEmployeeDTO pmEmployeeDTO = pmEmployeeService.getByKey(employeeId);
        pmEmployeeTempDTO.setEmployeeDTO(pmEmployeeDTO);//员工正式数据
        // 学历信息
        pmEmployeeTempDTO.setEducations(new LinkedHashSet<>(pmEmployeeEducationService.listAllByCheck(employeeId)));
        // 职业资格
        pmEmployeeTempDTO.setVocationals(new LinkedHashSet<>(pmEmployeeVocationalService.listAllByCheck(employeeId)));
        //家庭情况
        pmEmployeeTempDTO.setFamilys(new LinkedHashSet<>(pmEmployeeFamilyService.listAllByCheck(employeeId)));
        // 职称级别
        pmEmployeeTempDTO.setTitles(new LinkedHashSet<>(pmEmployeeTitleService.listAllByCheck(employeeId)));
        // 政治面貌
        pmEmployeeTempDTO.setPoliticals(new LinkedHashSet<>(pmEmployeePoliticalService.listAllByCheck(employeeId)));
        // 工作经历
        pmEmployeeTempDTO.setWorkhistorys(new LinkedHashSet<>(pmEmployeeWorkhistoryService.listAllByCheck(employeeId)));
        // 工作外职务
        pmEmployeeTempDTO.setPostothers(new LinkedHashSet<>(pmEmployeePostotherService.listAllByCheck(employeeId)));
        // 社会关系
        pmEmployeeTempDTO.setSocialrelations(new LinkedHashSet<>(pmEmployeeSocialrelationsService.listAllByCheck(employeeId)));
        // 技术爱好
        pmEmployeeTempDTO.setHobbys(new LinkedHashSet<>(pmEmployeeHobbyService.listAllByCheck(employeeId)));
        // 合同情况
        pmEmployeeTempDTO.setContracts(new LinkedHashSet<>(pmEmployeeContractService.getAllByemployeeId(employeeId)));
        return pmEmployeeTempDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCheckFlag(PmEmployeeTemp employeeTempNew) {
        //如果校核通过，则修改员工正式表数据,不通过则填写校核不通过原因
        if (employeeTempNew != null) {
            if ("Y".equals(employeeTempNew.getCheckFlag())) {
                //  修改正式表数据
                PmEmployee pmEmployee = new PmEmployee();
                pmEmployee.setId(employeeTempNew.getEmployee().getId());
                pmEmployee.setHeight(employeeTempNew.getHeight());
                pmEmployee.setWeight(employeeTempNew.getWeight());
                pmEmployee.setMaritalStatus(employeeTempNew.getMaritalStatus());
                pmEmployee.setHouseholdCharacter(employeeTempNew.getHouseholdCharacter());
                pmEmployee.setAddress(employeeTempNew.getAddress());
                pmEmployee.setZipcode(employeeTempNew.getZipcode());
                pmEmployee.setHouseholdAddress(employeeTempNew.getHouseholdAddress());
                pmEmployee.setHouseholdZipcode(employeeTempNew.getHouseholdZipcode());
                pmEmployee.setCollectiveHouseholdFlag(employeeTempNew.getCollectiveHouseholdFlag());
                pmEmployee.setCollectiveAddress(employeeTempNew.getCollectiveAddress());
                pmEmployee.setNativePlace(employeeTempNew.getNativePlace());
                pmEmployeeDao.updateColumnByCheck(pmEmployee);
                pmEmployeeTempDao.updateCheckFlag(employeeTempNew);
            }
            pmEmployeeTempDao.updateCheckFlag(employeeTempNew);
        }

    }
}
