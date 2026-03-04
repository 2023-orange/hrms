package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.*;
import com.sunten.hrms.pm.domain.*;
import com.sunten.hrms.pm.dto.*;
import com.sunten.hrms.pm.mapper.PmEmployeeRehireMapper;
import com.sunten.hrms.pm.service.PmEmployeeRehireService;
import com.sunten.hrms.pm.service.PmEmployeeService;
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
 * 返聘协议表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeRehireServiceImpl extends ServiceImpl<PmEmployeeRehireDao, PmEmployeeRehire> implements PmEmployeeRehireService {
    private final PmEmployeeRehireDao pmEmployeeRehireDao;
    private final PmEmployeeRehireMapper pmEmployeeRehireMapper;
    private final PmEmployeeDao pmEmployeeDao;
    // @Autowired
    private final PmEmployeeService pmEmployeeService;

    private final PmEmployeeEducationDao pmEmployeeEducationDao;
    private final PmEmployeeVocationalDao pmEmployeeVocationalDao;
    private final PmEmployeeTitleDao pmEmployeeTitleDao;
    private final PmEmployeeFamilyDao pmEmployeeFamilyDao;
    private final PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao;
    private final PmEmployeeAwardDao pmEmployeeAwardDao;
    private final PmEmployeeHobbyDao pmEmployeeHobbyDao;
    private final PmEmployeeEmergencyDao pmEmployeeEmergencyDao;
    private final PmEmployeePoliticalDao pmEmployeePoliticalDao;
    private final PmEmployeePostotherDao pmEmployeePostotherDao;
    private final PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao;
    private final PmEmployeeEntryDao pmEmployeeEntryDao;
    private final FndUserService fndUserService;


    public PmEmployeeRehireServiceImpl(PmEmployeeRehireDao pmEmployeeRehireDao, PmEmployeeRehireMapper pmEmployeeRehireMapper, PmEmployeeDao pmEmployeeDao,
                                       PmEmployeeService pmEmployeeService, PmEmployeeEducationDao pmEmployeeEducationDao, PmEmployeeVocationalDao pmEmployeeVocationalDao,
                                       PmEmployeeTitleDao pmEmployeeTitleDao, PmEmployeeFamilyDao pmEmployeeFamilyDao,
                                       PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao, PmEmployeeAwardDao pmEmployeeAwardDao,
                                       PmEmployeeHobbyDao pmEmployeeHobbyDao, PmEmployeeEmergencyDao pmEmployeeEmergencyDao,
                                       PmEmployeePoliticalDao pmEmployeePoliticalDao, PmEmployeePostotherDao pmEmployeePostotherDao,
                                       PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao, PmEmployeeEntryDao pmEmployeeEntryDao, FndUserService fndUserService) {
        this.pmEmployeeRehireDao = pmEmployeeRehireDao;
        this.pmEmployeeRehireMapper = pmEmployeeRehireMapper;
        this.pmEmployeeDao = pmEmployeeDao;
        this.pmEmployeeService = pmEmployeeService;
        this.pmEmployeeEducationDao = pmEmployeeEducationDao;
        this.pmEmployeeVocationalDao = pmEmployeeVocationalDao;
        this.pmEmployeeTitleDao = pmEmployeeTitleDao;
        this.pmEmployeeFamilyDao = pmEmployeeFamilyDao;
        this.pmEmployeeWorkhistoryDao = pmEmployeeWorkhistoryDao;
        this.pmEmployeeAwardDao = pmEmployeeAwardDao;
        this.pmEmployeeHobbyDao = pmEmployeeHobbyDao;
        this.pmEmployeeEmergencyDao = pmEmployeeEmergencyDao;
        this.pmEmployeePoliticalDao = pmEmployeePoliticalDao;
        this.pmEmployeePostotherDao = pmEmployeePostotherDao;
        this.pmEmployeeSocialrelationsDao = pmEmployeeSocialrelationsDao;
        this.pmEmployeeEntryDao = pmEmployeeEntryDao;
        this.fndUserService = fndUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeRehireDTO insert(PmEmployeeRehire employeeRehireNew) {
        PmEmployee oldEmployee = pmEmployeeDao.getByKey(employeeRehireNew.getEmployee().getId(), null);
        oldEmployee.setWorkCard(employeeRehireNew.getEmployee().getWorkCard());//新工牌
        oldEmployee.setLeaveFlag(false);
        if (employeeRehireNew.getEndTime() != null) {
            oldEmployee.setEmployeeType("返聘人员");//员工标识，1、正式，2、临时，3、返聘
        } else {
            oldEmployee.setEmployeeType("正式员工");
        }


        Long oldEmployeeId = oldEmployee.getId();//保留旧Id，取旧子集数据
        oldEmployee.setId(-1L);
        PmEmployee newEmployee = new PmEmployee();
        newEmployee.setId(-1L);
        //岗位关系
        PmEmployeeJob pmJob = new PmEmployeeJob();
        pmJob.setJob(employeeRehireNew.getJob());
        pmJob.setDept(employeeRehireNew.getDept());
        pmJob.setJobMainFlag(true);
        pmJob.setEnabledFlag(true);
        oldEmployee.setMainJob(pmJob);

        // 获取各子表信息
        //教育经历
        List<PmEmployeeEducation> educations = pmEmployeeEducationDao.listAllAndTempByEmployee(oldEmployeeId);
        for (PmEmployeeEducation pee : educations) {
            pee.setEmployee(newEmployee);
            pee.setId(-1L);
        }
        oldEmployee.setEmployeeEducations(new LinkedHashSet<>(educations));
        //职业资格
        List<PmEmployeeVocational> vocationals = pmEmployeeVocationalDao.listAllAndTempByEmployee(oldEmployeeId);
        for (PmEmployeeVocational pev : vocationals) {
            pev.setEmployee(newEmployee);
            pev.setId(-1L);
        }
        oldEmployee.setEmployeeVocationals(new LinkedHashSet<>(vocationals));
        //职称
        PmEmployeeTitleQueryCriteria titleQueryCriteria = new PmEmployeeTitleQueryCriteria();
        titleQueryCriteria.setEmployeeId(oldEmployeeId);
        titleQueryCriteria.setEnabled(true);
        List<PmEmployeeTitle> titles = pmEmployeeTitleDao.listAllByCriteria(titleQueryCriteria);
        for (PmEmployeeTitle pet : titles) {
            pet.setEmployee(newEmployee);
            pet.setId(-1L);
        }
        oldEmployee.setEmployeeTitles(new LinkedHashSet<>(titles));
        //家庭情况
        List<PmEmployeeFamily> families = pmEmployeeFamilyDao.listAllAndTempByEmployye(oldEmployeeId);
        for (PmEmployeeFamily pef : families) {
            pef.setEmployee(newEmployee);
            pef.setId(-1L);
        }
        oldEmployee.setEmployeeFamilies(new LinkedHashSet<>(families));
        //工作经历
        PmEmployeeWorkhistoryQueryCriteria workhistoryQueryCriteria = new PmEmployeeWorkhistoryQueryCriteria();
        workhistoryQueryCriteria.setEmployeeId(oldEmployeeId);
        workhistoryQueryCriteria.setEnabled(true);
        List<PmEmployeeWorkhistory> workhistories = pmEmployeeWorkhistoryDao.listAllByCriteria(workhistoryQueryCriteria);
        for (PmEmployeeWorkhistory pew : workhistories) {
            pew.setEmployee(newEmployee);
            pew.setId(-1L);
        }
        oldEmployee.setEmployeeWorkhistories(new LinkedHashSet<>(workhistories));
        //奖罚情况
        PmEmployeeAwardQueryCriteria awardQueryCriteria = new PmEmployeeAwardQueryCriteria();
        awardQueryCriteria.setEmployeeId(oldEmployeeId);
        awardQueryCriteria.setEnabled(true);
        List<PmEmployeeAward> awards = pmEmployeeAwardDao.listAllByCriteria(awardQueryCriteria);
        for (PmEmployeeAward pea : awards) {
            pea.setEmployee(newEmployee);
            pea.setId(-1L);
        }
        oldEmployee.setEmployeeAwards(new LinkedHashSet<>(awards));
        //技术爱好
        PmEmployeeHobbyQueryCriteria hobbyQueryCriteria = new PmEmployeeHobbyQueryCriteria();
        hobbyQueryCriteria.setEmployeeId(oldEmployeeId);
        hobbyQueryCriteria.setEnabled(true);
        List<PmEmployeeHobby> hobbies = pmEmployeeHobbyDao.listAllByCriteria(hobbyQueryCriteria);
        for (PmEmployeeHobby peh : hobbies) {
            peh.setEmployee(newEmployee);
            peh.setId(-1L);
        }
        oldEmployee.setEmployeeHobbies(new LinkedHashSet<>(hobbies));
        //合同，暂时不用
        //紧急电话
        PmEmployeeEmergencyQueryCriteria emergencyQueryCriteria = new PmEmployeeEmergencyQueryCriteria();
        emergencyQueryCriteria.setEmployeeId(oldEmployeeId);
        emergencyQueryCriteria.setEnabled(true);
        List<PmEmployeeEmergency> emergencies = pmEmployeeEmergencyDao.listAllByCriteria(emergencyQueryCriteria);
        for (PmEmployeeEmergency pee : emergencies) {
            pee.setEmployee(newEmployee);
            pee.setId(-1L);
        }
        oldEmployee.setEmployeeEmergencies(new LinkedHashSet<>(emergencies));
        //政治面貌
        PmEmployeePoliticalQueryCriteria politicalQueryCriteria = new PmEmployeePoliticalQueryCriteria();
        politicalQueryCriteria.setEmployeeId(oldEmployeeId);
        politicalQueryCriteria.setEnabled(true);
        List<PmEmployeePolitical> politicals = pmEmployeePoliticalDao.listAllByCriteria(politicalQueryCriteria);
        for (PmEmployeePolitical pep : politicals) {
            pep.setEmployee(newEmployee);
            pep.setId(-1L);
        }
        oldEmployee.setEmployeePoliticals(new LinkedHashSet<>(politicals));
        // 工作外职务
        PmEmployeePostotherQueryCriteria postotherQueryCriteria = new PmEmployeePostotherQueryCriteria();
        postotherQueryCriteria.setEmployeeId(oldEmployeeId);
        postotherQueryCriteria.setEnabled(true);
        List<PmEmployeePostother> postothers = pmEmployeePostotherDao.listAllByCriteria(postotherQueryCriteria);
        for (PmEmployeePostother pep : postothers) {
            pep.setEmployee(newEmployee);
            pep.setId(-1L);
        }
        oldEmployee.setEmployeePostothers(new LinkedHashSet<>(postothers));
        //社会关系
        PmEmployeeSocialrelationsQueryCriteria socialrelationsQueryCriteria = new PmEmployeeSocialrelationsQueryCriteria();
        socialrelationsQueryCriteria.setEmployeeId(oldEmployeeId);
        socialrelationsQueryCriteria.setEnabled(true);
        List<PmEmployeeSocialrelations> socialrelations = pmEmployeeSocialrelationsDao.listAllByCriteria(socialrelationsQueryCriteria);
        for (PmEmployeeSocialrelations pee : socialrelations) {
            pee.setEmployee(newEmployee);
            pee.setId(-1L);
        }
        oldEmployee.setEmployeeSocialrelations(new LinkedHashSet<>(socialrelations));
        //办公电话，暂时不用
        //招聘数据，暂时不用

        //新增一条员工信息,将入职信息置空
        oldEmployee.setEmployeeEntry(null);
        oldEmployee.setRecruitment(null);
        // pmEmployeeService.insert 已包含新增一条员工信息步骤
        PmEmployeeDTO employeeDTO = pmEmployeeService.insert(oldEmployee);
        PmEmployee pe = new PmEmployee();
        pe.setId(employeeDTO.getId());
        if (employeeRehireNew.getEndTime() != null) {// 如果结束时间存在，则认为该次操作是返聘，否则视为再入职
            employeeRehireNew.setEmployee(pe);
            pmEmployeeRehireDao.insertAllColumn(employeeRehireNew);
        } else {
            PmEmployeeEntry employeeEntry = pmEmployeeEntryDao.getByEmployeeId(oldEmployeeId);
            employeeEntry.setEntryTime(employeeRehireNew.getStartTime());
            employeeEntry.setEmployee(pe);
            pmEmployeeEntryDao.insertAllColumn(employeeEntry);
        }

        return pmEmployeeRehireMapper.toDto(employeeRehireNew);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeRehire employeeRehire = new PmEmployeeRehire();
        employeeRehire.setId(id);
        this.delete(employeeRehire);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeRehire employeeRehire) {
        // TODO    确认删除前是否需要做检查
        pmEmployeeRehireDao.deleteByEntityKey(employeeRehire);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeRehire employeeRehireNew) {
        PmEmployeeRehire employeeRehireInDb = Optional.ofNullable(pmEmployeeRehireDao.getByKey(employeeRehireNew.getId())).orElseGet(PmEmployeeRehire::new);
        ValidationUtil.isNull(employeeRehireInDb.getId(), "EmployeeRehire", "id", employeeRehireNew.getId());
        employeeRehireNew.setId(employeeRehireInDb.getId());
        pmEmployeeRehireDao.updateAllColumnByKey(employeeRehireNew);
    }

    @Override
    public PmEmployeeRehireDTO getByKey(Long id) {
        PmEmployeeRehire employeeRehire = Optional.ofNullable(pmEmployeeRehireDao.getByKey(id)).orElseGet(PmEmployeeRehire::new);
        ValidationUtil.isNull(employeeRehire.getId(), "EmployeeRehire", "id", id);
        return pmEmployeeRehireMapper.toDto(employeeRehire);
    }

    @Override
    public List<PmEmployeeRehireDTO> listAll(PmEmployeeRehireQueryCriteria criteria) {
        return pmEmployeeRehireMapper.toDto(pmEmployeeRehireDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeRehireQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeRehire> page = PageUtil.startPage(pageable);
        List<PmEmployeeRehire> employeeRehires = pmEmployeeRehireDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeRehireMapper.toDto(employeeRehires), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeRehireDTO> employeeRehireDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeRehireDTO employeeRehireDTO : employeeRehireDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeRehireDTO.getEmployee().getId());
            map.put("岗位id", employeeRehireDTO.getJob().getId());
            map.put("岗位", employeeRehireDTO.getJob().getJobName());
            map.put("部门科室id", employeeRehireDTO.getDept().getId());
            map.put("部门科室", employeeRehireDTO.getDept().getDeptName());
            map.put("开始时间", employeeRehireDTO.getStartTime());
            map.put("结束时间", employeeRehireDTO.getEndTime());
            map.put("备注", employeeRehireDTO.getRemarks());
            map.put("弹性域1", employeeRehireDTO.getAttribute1());
            map.put("弹性域2", employeeRehireDTO.getAttribute2());
            map.put("弹性域3", employeeRehireDTO.getAttribute3());
            map.put("有效标记默认值", employeeRehireDTO.getEnabledFlag());
            map.put("id", employeeRehireDTO.getId());
            map.put("创建时间", employeeRehireDTO.getCreateTime());
            map.put("创建人ID", employeeRehireDTO.getCreateBy());
            map.put("修改时间", employeeRehireDTO.getUpdateTime());
            map.put("修改人ID", employeeRehireDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
