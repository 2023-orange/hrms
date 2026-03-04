package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcLeaveFormDao;
import com.sunten.hrms.ac.domain.AcReqFlowdata;
import com.sunten.hrms.ac.domain.AcReqParameter;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.*;
import com.sunten.hrms.pm.domain.*;
import com.sunten.hrms.pm.dto.*;
import com.sunten.hrms.pm.mapper.PmEmployeeMapper;
import com.sunten.hrms.pm.service.*;
import com.sunten.hrms.pm.vo.CheckPmEmployeeVo;
import com.sunten.hrms.pm.vo.PmLeaderVo;
import com.sunten.hrms.pm.vo.PmManagerVo;
import com.sunten.hrms.pm.vo.PmMsgVo;
import com.sunten.hrms.re.service.ReRecruitmentService;
import com.sunten.hrms.tool.dao.ToolEmailServerDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import com.sunten.hrms.vo.ElTreeBaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 人事档案表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeServiceImpl extends ServiceImpl<PmEmployeeDao, PmEmployee> implements PmEmployeeService {
    private final PmEmployeeDao pmEmployeeDao;
    private final PmEmployeeMapper pmEmployeeMapper;
    private final PmEmployeeJobDao pmEmployeeJobDao;
    private final FndUserDao fndUserDao;
    private final PmEmployeeJobService pmEmployeeJobService;
    private final PmEmployeeEducationService pmEmployeeEducationService;
    private final PmEmployeeFamilyService pmEmployeeFamilyService;
    private final PmEmployeeHobbyService pmEmployeeHobbyService;
    private final PmEmployeeTitleService pmEmployeeTitleService;
    private final PmEmployeeVocationalService pmEmployeeVocationalService;
    private final PmEmployeeWorkhistoryService pmEmployeeWorkhistoryService;
    private final ReRecruitmentService reRecruitmentService;
    private final FndUserService fndUserService;
    private final FndDeptDao fndDeptDao;
    private final PmEmployeeLeaveofficeDao pmEmployeeLeaveofficeDao;
    private final AcLeaveFormDao acLeaveFormDao;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final ToolEmailServerDao toolEmailServerDao;

    @Autowired
    private PmEmployeeAwardService pmEmployeeAwardService;
    @Autowired
    private PmEmployeeEntryService pmEmployeeEntryService;
    @Autowired
    private PmEmployeeContractService pmEmployeeContractService;
    @Autowired
    private PmEmployeeEmergencyService pmEmployeeEmergencyService;
    @Autowired
    private PmEmployeePoliticalService pmEmployeePoliticalService;
    @Autowired
    private PmEmployeePostotherService pmEmployeePostotherService;
    @Autowired
    private PmEmployeeSocialrelationsService pmEmployeeSocialrelationsService;
    @Autowired
    private PmEmployeeTeleService pmEmployeeTeleService;
    private final PmEmployeePhotoDao pmEmployeePhotoDao;
    private final PmEmployeeJobTransferDao pmEmployeeJobTransferDao;
    private final PmEmployeeJobTransferService pmEmployeeJobTransferService;

    @Value("${file.pmPhoto}")
    private String pmPhoto;

    public PmEmployeeServiceImpl(PmEmployeeDao pmEmployeeDao, PmEmployeeMapper pmEmployeeMapper,
                                 PmEmployeeJobDao pmEmployeeJobDao,
                                 PmEmployeeJobService pmEmployeeJobService,
                                 PmEmployeeEducationService pmEmployeeEducationService, PmEmployeeFamilyService pmEmployeeFamilyService,
                                 PmEmployeeHobbyService pmEmployeeHobbyService, PmEmployeeTitleService pmEmployeeTitleService,
                                 PmEmployeeVocationalService pmEmployeeVocationalService, PmEmployeeWorkhistoryService pmEmployeeWorkhistoryService,
                                 ReRecruitmentService reRecruitmentService, FndUserService fndUserService, PmEmployeePhotoDao pmEmployeePhotoDao,
                                 PmEmployeeJobTransferDao pmEmployeeJobTransferDao, FndDeptDao fndDeptDao, PmEmployeeJobTransferService pmEmployeeJobTransferService,
                                 FndUserDao fndUserDao, PmEmployeeLeaveofficeDao pmEmployeeLeaveofficeDao, AcLeaveFormDao acLeaveFormDao, ToolEmailServerDao toolEmailServerDao, ToolEmailInterfaceService toolEmailInterfaceService, ToolEmailServerDao toolEmailServerDao1) {
        this.pmEmployeeDao = pmEmployeeDao;
        this.pmEmployeeMapper = pmEmployeeMapper;
        this.pmEmployeeJobDao = pmEmployeeJobDao;
        this.pmEmployeeJobService = pmEmployeeJobService;
        this.pmEmployeeEducationService = pmEmployeeEducationService;
        this.pmEmployeeFamilyService = pmEmployeeFamilyService;
        this.pmEmployeeHobbyService = pmEmployeeHobbyService;
        this.pmEmployeeTitleService = pmEmployeeTitleService;
        this.pmEmployeeVocationalService = pmEmployeeVocationalService;
        this.pmEmployeeWorkhistoryService = pmEmployeeWorkhistoryService;
        this.reRecruitmentService = reRecruitmentService;
        this.fndUserService = fndUserService;
        this.pmEmployeePhotoDao = pmEmployeePhotoDao;
        this.pmEmployeeJobTransferDao = pmEmployeeJobTransferDao;
        this.fndDeptDao = fndDeptDao;
        this.pmEmployeeJobTransferService =  pmEmployeeJobTransferService;
        this.fndUserDao = fndUserDao;
        this.pmEmployeeLeaveofficeDao = pmEmployeeLeaveofficeDao;
        this.acLeaveFormDao = acLeaveFormDao;
        this.toolEmailServerDao = toolEmailServerDao1;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeDTO insert(PmEmployee employeeNew) {
        PmEmployeeTele pmEmployeeTele = new PmEmployeeTele();
        PmEmployeeEmergency pmEmployeeEmergency = new PmEmployeeEmergency();
        if (employeeNew.getId() == null || employeeNew.getId().equals(-1L)) {
            // 验证新增员工的工牌号是否已存在
            PmEmployeeQueryCriteria criteria = new PmEmployeeQueryCriteria();
            criteria.setEnabledFlag(true);
            criteria.setWorkCard(employeeNew.getWorkCard());
            if (pmEmployeeDao.listAllByCriteria(criteria).size() > 0) {
                throw new InfoCheckWarningMessException("该工牌号已存在！");
            }
            PmEmployeeQueryCriteria idNumberCriteria = new PmEmployeeQueryCriteria();
            idNumberCriteria.setIdNumber(employeeNew.getIdNumber());
            if (this.listForIdNumberOrWorkCardExist(idNumberCriteria).size() > 0) {
                throw new InfoCheckWarningMessException("该身份证号正在使用！");
            }
            pmEmployeeDao.insertAllColumn(employeeNew);
            // 新增完成时查看是否属于再入职，如果是再入职则将所有子集复制一份给新员工信息
            PmEmployee oldEmp = pmEmployeeDao.newLeaveofficeEmployeeInfo(employeeNew.getIdNumber());
            if (oldEmp != null && oldEmp.getId() != null && !oldEmp.getId().equals(0L)) {
                // 执行复制子表信息的存储过程
                pmEmployeeDao.executeCopyEmployeeChildrenProcedure(oldEmp.getId(),employeeNew.getId(),employeeNew.getCreateBy());
            }

            if (employeeNew.getRecruitment() != null) {
                PmEmployee temp = new PmEmployee();
                temp.setId(employeeNew.getId());
                employeeNew.getRecruitment().setEmployee(temp);
            }
            //插入关系表
            PmEmployee pm = new PmEmployee();
            pm.setId(employeeNew.getId());
            PmEmployeeJob pmEmployeeJob = employeeNew.getMainJob();
            pmEmployeeJob.setEmployee(pm);
            pmEmployeeJob.setGroupId(pmEmployeeJobService.getMaxGroupId());
            pmEmployeeJobDao.insertAllColumn(pmEmployeeJob);

            //入职时新增一条岗位调动记录
            PmEmployeeJobTransfer jobTransfer = new PmEmployeeJobTransfer();
            jobTransfer.setEmployee(pm);
            jobTransfer.setGroupId(pmEmployeeJob.getGroupId());
            jobTransfer.setNewJob(pmEmployeeJob.getJob());
            jobTransfer.setNewDept(pmEmployeeJob.getDept());
            jobTransfer.setRemarks("初始入职岗位");
            jobTransfer.setTransferType("初始化");
            jobTransfer.setEnabledFlag(true);
            jobTransfer.setTransferState("FINISHED");
            jobTransfer.setStartTime(LocalDate.now());
            pmEmployeeJobTransferDao.insertAllColumn(jobTransfer);

            // 新增登录用户
            fndUserService.insertByEmployee(employeeNew);
        } else {
            this.update(employeeNew);
        }
        // 判断相关子集是否为空，不为空则插入
//        个人电话
        if (employeeNew.getRecruitment()!= null){
            if (employeeNew.getRecruitment().getMobilePhone() != null) {
                pmEmployeeTele.setTele(employeeNew.getRecruitment().getMobilePhone());
                pmEmployeeTeleService.insertRecruitmentTele(pmEmployeeTele, employeeNew.getId());
        }}
//        //紧急联系电话
        if (employeeNew.getRecruitment()!= null){
            if (employeeNew.getRecruitment().getEmergencyContactPhone() != null) {
                pmEmployeeEmergency.setEmergencyTele(employeeNew.getRecruitment().getEmergencyContactPhone());
                pmEmployeeEmergency.setEmergencyContact(employeeNew.getRecruitment().getEmergencyContactName());
                pmEmployeeEmergencyService.insertRecruitmentEmergency(pmEmployeeEmergency, employeeNew.getId()); }
        }
        //教育经历
        if (employeeNew.getEmployeeEducations() != null && employeeNew.getEmployeeEducations().size() > 0)
            pmEmployeeEducationService.batchInsert(new ArrayList<>(employeeNew.getEmployeeEducations()), employeeNew.getId());
        //职业资格
        if (employeeNew.getEmployeeVocationals() != null && employeeNew.getEmployeeVocationals().size() > 0)
            pmEmployeeVocationalService.batchInsert(new ArrayList(employeeNew.getEmployeeVocationals()), employeeNew.getId());
        //职称
        if (employeeNew.getEmployeeTitles() != null && employeeNew.getEmployeeTitles().size() > 0)
            pmEmployeeTitleService.batchInsert(new ArrayList<>(employeeNew.getEmployeeTitles()), employeeNew.getId());
        //家庭情况
        if (employeeNew.getEmployeeFamilies() != null && employeeNew.getEmployeeFamilies().size() > 0)
            pmEmployeeFamilyService.batchInsert(new ArrayList<>(employeeNew.getEmployeeFamilies()), employeeNew.getId());
        //工作经历
        if (employeeNew.getEmployeeWorkhistories() != null && employeeNew.getEmployeeWorkhistories().size() > 0)
            pmEmployeeWorkhistoryService.batchInsert(new ArrayList(employeeNew.getEmployeeWorkhistories()), employeeNew.getId());
        //奖罚情况
        if (employeeNew.getEmployeeAwards() != null && employeeNew.getEmployeeAwards().size() > 0)
            pmEmployeeAwardService.batchInsert(new ArrayList<>(employeeNew.getEmployeeAwards()), employeeNew.getId());
        //技术爱好
        if (employeeNew.getEmployeeHobbies() != null && employeeNew.getEmployeeHobbies().size() > 0)
            pmEmployeeHobbyService.batchInsert(new ArrayList<>(employeeNew.getEmployeeHobbies()), employeeNew.getId());
        //合同
        if (employeeNew.getEmployeeContracts() != null && employeeNew.getEmployeeContracts().size() > 0)
            pmEmployeeContractService.batchInsert(new ArrayList<>(employeeNew.getEmployeeContracts()), employeeNew.getId());
        //紧急电话
        if (employeeNew.getEmployeeEmergencies() != null && employeeNew.getEmployeeEmergencies().size() > 0)
            pmEmployeeEmergencyService.batchInsert(new ArrayList<>(employeeNew.getEmployeeEmergencies()), employeeNew.getId());
        //政治面貌
        if (employeeNew.getEmployeePoliticals() != null && employeeNew.getEmployeePoliticals().size() > 0)
            pmEmployeePoliticalService.batchInsert(new ArrayList<>(employeeNew.getEmployeePoliticals()), employeeNew.getId());
        // 工作外职务
        if (employeeNew.getEmployeePostothers() != null && employeeNew.getEmployeePostothers().size() > 0)
            pmEmployeePostotherService.batchInsert(new ArrayList<>(employeeNew.getEmployeePostothers()), employeeNew.getId());
        //社会关系
        if (employeeNew.getEmployeeSocialrelations() != null && employeeNew.getEmployeeSocialrelations().size() > 0)
            pmEmployeeSocialrelationsService.batchInsert(new ArrayList<>(employeeNew.getEmployeeSocialrelations()), employeeNew.getId());
        //办公电话
        if (employeeNew.getEmployeeTeles() != null && employeeNew.getEmployeeTeles().size() > 0)
            pmEmployeeTeleService.batchInsert(new ArrayList<>(employeeNew.getEmployeeTeles()), employeeNew.getId());
        //招聘数据
        if (employeeNew.getRecruitment() != null) reRecruitmentService.insert(employeeNew.getRecruitment());
        // 入职
        if (employeeNew.getEmployeeEntry() != null) {
            if (employeeNew.getEmployeeEntry().getId() != null && !employeeNew.getEmployeeEntry().getId().equals(-1L)) {
                pmEmployeeEntryService.update(employeeNew.getEmployeeEntry());
            } else {
                if (employeeNew.getEmployeeEntry().getEmployee() == null)
                    employeeNew.getEmployeeEntry().setEmployee(new PmEmployee());
                employeeNew.getEmployeeEntry().getEmployee().setId(employeeNew.getId());

                pmEmployeeEntryService.insert(employeeNew.getEmployeeEntry());
            }
        }

        return pmEmployeeMapper.toDto(employeeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployee employee = new PmEmployee();
        employee.setId(id);
        employee.setEnabledFlag(false);
        this.delete(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployee employee) {
        // 确认删除前是否需要做检查，只失效，不删除
        pmEmployeeDao.updateEnableFlag(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployee employeeNew) {
        // 先获取原有的pmEmployee信息
        PmEmployee employeeInDb = Optional.ofNullable(pmEmployeeDao.getByKey(employeeNew.getId(), null)).orElseGet(PmEmployee::new);
        ValidationUtil.isNull(employeeInDb.getId(), "Employee", "id", employeeNew.getId());
        employeeNew.setId(employeeInDb.getId());
        // 验证employeeNew的工号是否存在
        CheckPmEmployeeVo checkPmEmployeeVo = pmEmployeeDao.checkPmEmployeeBeforeUpdate(employeeNew.getWorkCard(), employeeNew.getIdNumber(), employeeNew.getId());
        if (checkPmEmployeeVo.getHaveSameWorkCardFlag()) {
            throw new InfoCheckWarningMessException("该工牌号已存在！");
        }
        if (checkPmEmployeeVo.getHaveSameIdNumberFlag()) {
            throw new InfoCheckWarningMessException("该身份证号正在使用！");
        }
        // 检查岗位关系是否有变化
        if (!employeeNew.getMainJob().getDept().getId().equals(employeeInDb.getMainJob().getDept().getId())
                || !employeeNew.getMainJob().getJob().getId().equals(employeeInDb.getMainJob().getJob().getId())) {
            PmEmployeeJob pmEmployeeJob = pmEmployeeJobDao.getByKey(employeeNew.getMainJob().getId());
            pmEmployeeJob.setJob(employeeNew.getMainJob().getJob());
            pmEmployeeJob.setDept(employeeNew.getMainJob().getDept());
            pmEmployeeJobService.update(pmEmployeeJob);
            // 更新岗位调动记录
            PmEmployeeJobTransferQueryCriteria pmEmployeeJobTransferQueryCriteria = new PmEmployeeJobTransferQueryCriteria();
            pmEmployeeJobTransferQueryCriteria.setTransferType(false);
            pmEmployeeJobTransferQueryCriteria.setEnabledFlag(true);
            pmEmployeeJobTransferQueryCriteria.setEmployeeId(employeeInDb.getId());
            List<PmEmployeeJobTransfer> pmEmployeeJobTransfers = pmEmployeeJobTransferDao.listAllByCriteria(pmEmployeeJobTransferQueryCriteria);
            if (pmEmployeeJobTransfers.size() == 1) {
                PmEmployeeJobTransfer pmEmployeeJobTransfer = pmEmployeeJobTransfers.get(0);
                pmEmployeeJobTransfer.setNewJob(employeeNew.getMainJob().getJob());
                pmEmployeeJobTransfer.setNewDept(employeeNew.getMainJob().getDept());
                pmEmployeeJobTransferService.update(pmEmployeeJobTransfer);
            }
        }
        // 检查工牌号是否发生变化、 姓名是否变化
        if (!employeeNew.getWorkCard().equals(employeeInDb.getWorkCard()) || !employeeNew.getName().equals(employeeInDb.getName())) {
            // 更新fnd_user
            FndUser fndUser = fndUserDao.getByUsername(employeeInDb.getWorkCard());
            if (!employeeNew.getWorkCard().equals(employeeInDb.getWorkCard())) {
                fndUser.setUsername(employeeNew.getWorkCard());
                fndUserService.updateUserName(fndUser);
            }
            if (!employeeNew.getName().equals(employeeInDb.getName())) {
                fndUser.setDescription(employeeNew.getName());
                fndUserService.updateDescription(fndUser);
            }
        }


        // 检查身份证是否发生变化
        if (!employeeNew.getIdNumber().equals(employeeInDb.getIdNumber())) {
            // 检查旧的IdNumber是否为再入职
            PmEmployee oldEmp = pmEmployeeDao.newLeaveofficeEmployeeInfo(employeeInDb.getIdNumber());
            if (null != oldEmp && null != oldEmp.getId() && !oldEmp.getId().equals(0L)) {
               // 旧的IdNumber为再入职则需要失效旧IdNumber时自动生成的子集
                pmEmployeeDao.executeDisabledEmployeeChildrenProcedure(employeeInDb.getId(), employeeInDb.getCreateBy());
            }
            // 检查新的IDNumber是否为再入职
            PmEmployee newEmp = pmEmployeeDao.newLeaveofficeEmployeeInfo(employeeNew.getIdNumber());
            if (null != newEmp && null != newEmp.getId() && !newEmp.getId().equals(0L)) {
                // 执行复制子表信息的存储过程
                pmEmployeeDao.executeCopyEmployeeChildrenProcedure(newEmp.getId(), employeeInDb.getId(), employeeInDb.getCreateBy());
            }
        }

//        //验证岗位部门是否改变,如果改变，则更改部门岗位关系表
//        PmEmployeeJob jobOld = pmEmployeeJobDao.getMainJObByKey(employeeInDb.getId());
//        PmEmployeeJob jobNow = employeeNew.getMainJob();
//        if (!(jobOld.getJob().getId().equals(jobNow.getJob().getId()) && jobOld.getDept().getId().equals(jobNow.getDept().getId()))) {
//            jobNow.setEmployee(employeeInDb);
//            jobNow.setGroupId(jobOld.getGroupId());
//            pmEmployeeJobService.delete(jobOld.getId());//删除原有关系
//            pmEmployeeJobService.insert(jobNow);//新增关系
//        }
        pmEmployeeDao.updateAllColumnByKey(employeeNew);
    }

    @Override
    public PmEmployeeDTO getByKey(Long id) {
        return this.getByKey(id, null);
    }

    @Override
    public PmEmployeeDTO getByKey(Long id, Set<Long> deptIdsInDataScope) {
        PmEmployee employee = Optional.ofNullable(pmEmployeeDao.getByKey(id, deptIdsInDataScope)).orElseGet(PmEmployee::new);
        ValidationUtil.isNull(employee.getId(), "Employee", "id", id);

        // 获取相同身份证的其它离职人员信息,
        List<PmEmployee> otherEmployees = pmEmployeeDao.otherLeaveEmpInfoByIdNumber(employee.getIdNumber(),id);
        if(otherEmployees != null && otherEmployees.size() >0) {
            employee.setOtherEmployees(otherEmployees);
        }

        return pmEmployeeMapper.toDto(employee);
    }

    @Override
    public List<PmEmployeeDTO> listAll(PmEmployeeQueryCriteria criteria) {
        //员工基本信息
        List<PmEmployee> employees = pmEmployeeDao.listAllByCriteria(criteria);
        return pmEmployeeMapper.toDto(employees);
    }

    @Override
    public List<PmEmployeeDTO> baseEmployeeListByPage(PmEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployee> page = PageUtil.startPage(pageable);
        List<PmEmployee> employees = pmEmployeeDao.listAllByCriteriaPage(page, criteria);
        return pmEmployeeMapper.toDto(employees);
    }

    /**
     * 根据部门名称模糊查询其下的员工信息
     *
     * @param deptname
     * @return
     */
    @Override
    public List<PmEmployeeDTO> getListByDeptName(String deptname) {
        // TODO 模糊匹配部门名称

        return null;
    }

    /**
     * 模糊查询员工信息
     */
    @Override
    public List<ElTreeBaseVo> getListByNameOrCard(PmEmployeeLikeQueryCriteria criteria) {
        List<ElTreeBaseVo> resList = new LinkedList<>();
//        List<PmEmployeeDTO> pmEmployees = pmEmployeeMapper.toDto(pmEmployeeDao.listEmployeeLikeByCriteria(criteria));

        List<PmEmployeeDTO> pmEmployees = pmEmployeeMapper.toDto(pmEmployeeDao.listEmployeeByWorkCardOrName(criteria));

        if (pmEmployees.size() > 0) {
            String currentIdNumber = "";
            ElTreeBaseVo elTreeBaseVo = null;
            for (PmEmployeeDTO pe : pmEmployees) {
                if (elTreeBaseVo == null || !pe.getIdNumber().equals(currentIdNumber)) {
                    currentIdNumber = pe.getIdNumber();
                    elTreeBaseVo = new ElTreeBaseVo(pe.getId(), pe.getName() + "/" + pe.getWorkCard() + "/" + (pe.getLeaveFlag() ? "离职" : "在职"), pe, null);
                    resList.add(elTreeBaseVo);
                }
            }
        }

        return resList;
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployee> page = PageUtil.startPage(pageable);
//        List<PmEmployee> employees = getEmployeeDeptInfo(pmEmployeeDao.listAllByCriteriaPage(page, criteria));
        List<PmEmployee> employees = pmEmployeeDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeMapper.toDto(employees), page.getTotal());
    }

    @Override
    public void download(PmEmployeeQueryCriteria criteria, Pageable pageable, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String dateStr = date.format(fmt);
        Page<PmEmployee> page = PageUtil.startPage(pageable);
//        List<PmEmployeeDTO> employeeDTOS = pmEmployeeMapper.toDto(getEmployeeDeptInfo(pmEmployeeDao.listAllByCriteriaPage(page, criteria)));
//        List<PmEmployeeDTO> employeeDTOS = pmEmployeeMapper.toDto(pmEmployeeDao.listAllByCriteriaPage(page, criteria));
        List<PmEmployeeDTO> employeeDTOS = pmEmployeeMapper.toDto(pmEmployeeDao.listDownloadByCriteriaPage(page, criteria));
        for (PmEmployeeDTO employeeDTO : employeeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", employeeDTO.getWorkCard());//工牌号
            map.put("姓名", employeeDTO.getName());//姓名
            map.put("部门", employeeDTO.getDeptName());//部门
            map.put("科室", employeeDTO.getDepartment());//科室
            map.put("班组", employeeDTO.getTeam());//班组
            map.put("岗位", null == employeeDTO.getMainJob() || null == employeeDTO.getMainJob().getJob() ? "":employeeDTO.getMainJob().getJob().getJobName());//岗位
            map.put("员工性质", null == employeeDTO.getEmployeeType() ? "" : employeeDTO.getEmployeeType());
            map.put("性别", employeeDTO.getGender());//性别
            map.put("民族", employeeDTO.getNation());//民族
            map.put("年龄", employeeDTO.getAge());//年龄
            map.put("婚姻状态", employeeDTO.getMaritalStatus());//婚姻状态
            map.put("出生日期", employeeDTO.getBirthday().format(fmt));//出生日期
            map.put("身份证号", employeeDTO.getIdNumber());//身份证号
            if (null == employeeDTO.getCompanyAge()) {
                map.put("公司工龄", "");
            } else {
                map.put("公司工龄", employeeDTO.getCompanyAge());
            }
            if (null == employeeDTO.getWorkedYears()){
                map.put("社保工龄", "");
            } else {
                map.put("社保工龄", employeeDTO.getWorkedYears());
            }
            if (null == employeeDTO.getEmployeeEntry().getEntryTime()) {
                map.put("入职时间", "");
            } else {
                map.put("入职时间", employeeDTO.getEmployeeEntry().getEntryTime().format(fmt));//入职时间
            }
            map.put("备注", employeeDTO.getRemarks());//备注
            map.put("职业类别", employeeDTO.getOccupationCategory());//职业类别
            map.put("职业种类", employeeDTO.getOccupationType());//职业种类
            map.put("最高学历", employeeDTO.getBetterEducation().getEducation());//最高学历
            if (null == employeeDTO.getBetterEducation().getGraduationTime()) {
                map.put("毕业时间", "");
            } else {
                map.put("毕业时间", employeeDTO.getBetterEducation().getGraduationTime().format(fmt));//毕业时间
            }
            map.put("最高职称", employeeDTO.getBetterTitle().getTitleName());//最高职称
            map.put("职称级别", employeeDTO.getBetterTitle().getTitleLevel());//职称级别
            map.put("岗位级别", employeeDTO.getBetterVocational().getVocationalLevel());//岗位级别
            map.put("户口性质", employeeDTO.getHouseholdCharacter());//户口性质
            map.put("户口所在地", employeeDTO.getRegisteredResidence());//户口所在地
            map.put("现在住址", null != employeeDTO.getAddress() ? employeeDTO.getAddress() : "");
            map.put("户口地址", null != employeeDTO.getHouseholdAddress() ? employeeDTO.getHouseholdAddress() : "");
            map.put("籍贯", employeeDTO.getNativePlace());//籍贯
            map.put("内部邮箱", employeeDTO.getEmailInside());//内部邮箱
            map.put("姓名拼音", employeeDTO.getNameAbbreviation());//姓名拼音
            map.put("类别区分", employeeDTO.getAdministrationCategory());//类别
            if (null == employeeDTO.getEmployeeTele() || null == employeeDTO.getEmployeeTele().getTele()) {
                map.put("移动电话", "");
            } else {
                map.put("移动电话", employeeDTO.getEmployeeTele().getTele());
            }
            if (null == employeeDTO.getEmployeeEmergencie() || null == employeeDTO.getEmployeeEmergencie().getEmergencyContact()) {
                map.put("紧急联系人", "");
            } else {
                map.put("紧急联系人", employeeDTO.getEmployeeEmergencie().getEmergencyContact());
            }
            if (null == employeeDTO.getEmployeeEmergencie() || null == employeeDTO.getEmployeeEmergencie().getEmergencyTele()) {
                map.put("紧急联系方式", "");
            } else {
                map.put("紧急联系方式", employeeDTO.getEmployeeEmergencie().getEmergencyTele());
            }
            if (null == employeeDTO.getUpdateByStr()) {
                map.put("最后修改人", "");
            } else {
                map.put("最后修改人", employeeDTO.getUpdateByStr());
            }
            map.put("最后修改时间", DateUtil.localDateTimeToStr(employeeDTO.getUpdateTime()));


//            map.put("workAge", employeeDTO.getBirthday());//工龄

//            map.put("graduationTime", employeeDTO.getBetterEducation().getGraduationTime().format(fmt));//毕业时间


            //map.put("height", employeeDTO.getHeight());//身高
            //map.put("weight", employeeDTO.getWeight());//体重
            //map.put("calendar", employeeDTO.getCalendar());//农历公历

            //map.put("address", employeeDTO.getAddress());//现在住址
            //map.put("zipcode", employeeDTO.getZipcode());//现住邮编
            //map.put("householdAddress", employeeDTO.getHouseholdAddress());//户口地址
            // map.put("householdZipcode", employeeDTO.getHouseholdZipcode());//户口邮编
            //map.put("collectiveHouseholdFlag", employeeDTO.getCollectiveHouseholdFlag());//是否集体户口
            //map.put("collectiveAddress", employeeDTO.getCollectiveAddress());//集体户口所在地

            //map.put("workAttendanceFlag", employeeDTO.getWorkAttendanceFlag());//是否考勤
            //map.put("workshopAttendanceFlag", employeeDTO.getWorkshopAttendanceFlag());//是否排班
            //map.put("rehireFlag", employeeDTO.getRehireFlag());//是否返聘
            //map.put("employeeFlag", employeeDTO.getEmployeeFlag());//是否正式工
            // map.put("leaveFlag", employeeDTO.getLeaveFlag());//是否离职



            //map.put("attribute1", employeeDTO.getAttribute1());//弹性域1
            //map.put("attribute2", employeeDTO.getAttribute2());//弹性域2
            //map.put("attribute3", employeeDTO.getAttribute3());//弹性域3
            //map.put("attribute4", employeeDTO.getAttribute4());//弹性域4
            //map.put("attribute5", employeeDTO.getAttribute5());//弹性域5
            //map.put("enabledFlag", employeeDTO.getEnabledFlag());//有效标记默认值
            //map.put("id", employeeDTO.getId());//id
            //map.put("创建时间", employeeDTO.getCreateTime());//创建时间
            //map.put("创建人ID", employeeDTO.getCreateBy());//创建人ID
            //map.put("修改时间", employeeDTO.getUpdateTime());//修改时间
            //map.put("修改人ID", employeeDTO.getUpdateBy());//修改人ID
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    /**
     * 设置部门岗位属性
     */
    public void setEmployeeDeptInfo(PmEmployee pe) {
        //主要岗位
        PmEmployeeJob mainJob = pe.getMainJob();
//        PmEmployeeJob mainJob = Optional.ofNullable(pmEmployeeJobDao.getMainJObByKey(pe.getId())).orElseGet(PmEmployeeJob::new);
//        pe.setMainJob(mainJob);
        //部门、科室、班组
//        PmEmployee pmEmployee = setEmployeesDeptName(mainJob.getDept().getId());
//        pe.setDeptName(pmEmployee.getDeptName());
//        pe.setDepartment(pmEmployee.getDepartment());
//        pe.setTeam(pmEmployee.getTeam());
        //入职情况
//        PmEmployeeEntry pmEmployeeEntry = Optional.ofNullable(pmEmployeeEntryDao.getByEmployeeId(pe.getId())).orElseGet(PmEmployeeEntry::new);
//        pe.setEmployeeEntry(pmEmployeeEntry);
        // 离职情况
//        PmEmployeeLeaveoffice employeeLeaveoffice = Optional.ofNullable(pmEmployeeLeaveofficeDao.getByEmployeeId(pe.getId())).orElseGet(PmEmployeeLeaveoffice::new);
//        pe.setEmployeeLeaveoffice(employeeLeaveoffice);
    }

    /**
     * 获取有待校核数据的人员信息
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Override
    public Map<String, Object> listAllByCheck(PmEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployee> page = PageUtil.startPage(pageable);
//        List<PmEmployee> employees = getEmployeeDeptInfo(pmEmployeeDao.listCheckByPage(page, criteria));
        List<PmEmployee> employees = pmEmployeeDao.listCheckByPage(page, criteria);
        return PageUtil.toPage(pmEmployeeMapper.toDto(employees), page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhoto(MultipartFile multipartFile, Long employeeId) {
        //获取旧路径信息
        PmEmployee pmEmployee = pmEmployeeDao.getByKey(employeeId, null);
        PmEmployeePhoto employeePhoto = pmEmployee.getEmployeePhoto();
        String oldPath = "";
        if (employeePhoto != null) {
            oldPath = employeePhoto.getPath();
        }

        String filePath = pmPhoto + employeeId + "\\";
        File file = FileUtil.upload(multipartFile, filePath);

        PmEmployeePhoto newPhoto = new PmEmployeePhoto();
        newPhoto.setPath(file.getPath());//路径
        newPhoto.setRealName(file.getName());//照片名称
        newPhoto.setAvaterSize(FileUtil.getSize(multipartFile.getSize()));//图片大小
        //插入照片信息
        pmEmployeePhotoDao.insertAllColumn(newPhoto);
        //修改人员照片路径
        pmEmployee.setEmployeePhoto(newPhoto);
        pmEmployeeDao.updatePhotoPath(pmEmployee);
        //删除照片物理路径
        if (StringUtils.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
    }

    @Override
    public List<PmEmployeeDTO> listAllByJobId(Long jobId) {
        PmEmployeeQueryCriteria employeeQueryCriteria = new PmEmployeeQueryCriteria();
        employeeQueryCriteria.setEnabledFlag(true);
        List<PmEmployeeJob> jobList = pmEmployeeJobDao.listByJobId(jobId);
        employeeQueryCriteria.setEmployeeJObs(jobList);

        return pmEmployeeMapper.toDto(pmEmployeeDao.listAllByCriteria(employeeQueryCriteria));
    }

    @Override
    public List<PmEmployeeDTO> listForIdNumberOrWorkCardExist(PmEmployeeQueryCriteria criteria) {
        // 目前此接口只查生效的在职员工的身份证
        criteria.setLeaveFlag(false);
        criteria.setEnabledFlag(true);
        List<PmEmployee> employees = pmEmployeeDao.listForIdNumberOrWorkCardExist(criteria);
        return pmEmployeeMapper.toDto(employees);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateCompanyAge() {
        pmEmployeeDao.batchUpdateCompanyAge();
    }

    @Override
    public Map<String,Object> listForAttendanceSet(PmEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployee> page = PageUtil.startPage(pageable);
        List<PmEmployee> employees = pmEmployeeDao.listForAttendanceSet(page,criteria);
        return PageUtil.toPage(pmEmployeeMapper.toDto(employees),page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttendanceFlag(PmEmployee employee) {
        pmEmployeeDao.updateEmployeeForAttendanceSet(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttendanceFlagBatch(List<PmEmployee> employees) {
        employees.forEach(employee -> {
            pmEmployeeDao.updateEmployeeForAttendanceSet(employee);
        });
    }

    @Override
    public List<PmEmployeeDTO> listOtherEmployees(String idNumber, long id) {
        return pmEmployeeMapper.toDto(pmEmployeeDao.otherNoLeaveofficeEmpInfo(idNumber, id));
    }

    @Override
    public List<PmEmployeeDTO> listLeaderEmployees() {
        return pmEmployeeMapper.toDto(pmEmployeeDao.listLeaderEmployee());
    }

    @Override
    public List<PmEmployeeDTO> listEmployeesBaseInfoList(PmEmployeeQueryCriteria criteria) {
        return  pmEmployeeMapper.toDto(pmEmployeeDao.listEmployeesBaseInfoList(criteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhotoPath(PmEmployee pmEmployee) {
        pmEmployeeDao.updatePhotoPath(pmEmployee);
    }

    @Override
    public PmMsgVo getPmMsgByUserId(Long userId) {
        return pmEmployeeDao.getPmMsgByUserId(userId);
    }

    @Override
    public PmLeaderVo getCurrentManagerAndSuperior(Long userId) {
        return pmEmployeeDao.getCurrentManagerAndSuperior(userId);
    }

    @Override
    public PmLeaderVo getManagerAndSuperiorByDeptId(Long deptId) {
        return pmEmployeeDao.getManagerAndSuperiorByDeptId(deptId);
    }

    @Override
    public Set<String> getWorkListByRolePermission(String permission) {
        return pmEmployeeDao.getWorkListByRolePermission(permission);
    }

    @Override
    public List<PmManagerVo> getTeamByDeptNameAndDepartmentNameAndTeamName(Map<String, Object> map) {
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        if (null == user.getDept()) {
//            return new ArrayList<>();
//        } else {
//            Map<String, Object> map = new HashMap<>();
//            map.put("deptName", user.getDept().getExtDeptName());
//            map.put("departmentName", user.getDept().getExtDepartmentName());
//            map.put("teamName", user.getDept().getExtTeamName());
//            map.put("workCard", useWorkCardFlag ? user.getUsername() : null);
//            return pmEmployeeDao.getTeamByDeptNameAndDepartmentNameAndTeamName(map);
//        }
        return pmEmployeeDao.getTeamByDeptNameAndDepartmentNameAndTeamName(map);
    }

    @Override
    public List<PmManagerVo> getSuperiorByDeptNameAndDepartmentName(Map<String, Object> map) {
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        if (null == user.getDept()) {
//            return new ArrayList<>();
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("deptName", user.getDept().getExtDeptName());
//        map.put("departmentName", user.getDept().getExtDepartmentName());
//        map.put("workCard", useWorkCardFlag ? user.getUsername() : null);
        return pmEmployeeDao.getSuperiorByDeptNameAndDepartmentName(map);
    }

    @Override
    public List<PmManagerVo> getManagerByDeptName(Map<String, Object> map) {
//        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
//        if (null == user.getDept()) {
//            return new ArrayList<>();
//        } else {
//            return pmEmployeeDao.getManagerByDeptName(map);
//        }
        return pmEmployeeDao.getManagerByDeptName(map);
    }

    @Override
    public List<PmManagerVo> getLeaderList() {
        return pmEmployeeDao.getLeader();
    }

    @Override
    public PmEmployeeDTO getLeaderByDeptId(Long deptId) {
        PmEmployee pmEmployee = pmEmployeeDao.getLeaderByDeptId(deptId);
        if (null == pmEmployee) {
            FndDept fndDept = fndDeptDao.getByKey(deptId);
            pmEmployee = pmEmployeeDao.getLeaderByDeptId(fndDept.getParentId());
        }
        return pmEmployeeMapper.toDto(pmEmployee);
    }

    public PmMsgVo createPmMsgVo(PmEmployee pmEmployee) {
        return new PmMsgVo(
                pmEmployee.getId(),
                null,
                null,
                null,
                pmEmployee.getDeptName(),
                pmEmployee.getDepartment(),
                pmEmployee.getTeam(),
                pmEmployee.getJobName(),
                pmEmployee.getName(),
                pmEmployee.getWorkCard(),
                null,
                null,
                null
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaidAnnualLeaveEveryDay() {
        // 更新在职人员的带薪年假
        Integer dayNum = new GregorianCalendar().isLeapYear(LocalDate.now().getYear()) ? 366 : 365;
        pmEmployeeDao.updatePaidAnnualLeaveEveryDay(null, dayNum);
        // 更新将要离职的在职人员的带薪年假， 必须在updatePaidAnnualLeaveEveryDay之后执行，不然基数越来越小，年假越来越少
        pmEmployeeDao.updatePaidAnnualLeaveForWillLeaveEveryDay(null, dayNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaidAnnualLeaveEveryDayAfterTenYearUpdate(String workCard) {
        // 更新在职人员的带薪年假
        Integer dayNum = new GregorianCalendar().isLeapYear(LocalDate.now().getYear()) ? 366 : 365;
        pmEmployeeDao.updatePaidAnnualLeaveEveryDay(workCard, dayNum);
        // 更新将要离职的在职人员的带薪年假， 必须在updatePaidAnnualLeaveEveryDay之后执行，不然基数越来越小，年假越来越少
        pmEmployeeDao.updatePaidAnnualLeaveForWillLeaveEveryDay(workCard, dayNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTenYearDate(PmEmployee pe) {
        pmEmployeeDao.updateTenYearDate(pe);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTenYearDateSetNull(PmEmployee pe) {
        pmEmployeeDao.updateTenYearDateSetNull(pe);
    }

    @Override
    public Map<String,Object> getPmEmailList(Pageable pageable, PmEmployeeQueryCriteria pmEmployeeQueryCriteria) {
        Page<PmEmployee> page = PageUtil.startPage(pageable);
        List<PmEmployee> pmEmployees = pmEmployeeDao.getPmEmailList(page, pmEmployeeQueryCriteria);
        return PageUtil.toPage(pmEmployeeMapper.toDto(pmEmployees), page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePmEmail(PmEmployee pe) {
        pmEmployeeDao.savePmEmail(pe);
        fndUserService.updateUserEmail(pe.getId(), pe.getEmailInside());
    }

    @Override
    public Map<String,Object> getPmTelList(Pageable pageable, PmEmployeeQueryCriteria pmEmployeeQueryCriteria) {
        Page<PmEmployee> page = PageUtil.startPage(pageable);
        List<PmEmployee> pmEmployees = pmEmployeeDao.getPmTelList(page, pmEmployeeQueryCriteria);
        return PageUtil.toPage(pmEmployeeMapper.toDto(pmEmployees), page.getTotal());
    }

    @Override
    public void inspectionResignedPersonnel() {
        ToolEmailInterface emailInterface;
        emailInterface = new ToolEmailInterface();
        LocalDate now = LocalDate.now();
        LocalDate startDate= now.plusDays(-15);
//        LocalDate startDate2= now.plusDays(-2);
//        System.out.println("startDate = " + startDate);
//        System.out.println("startDate2 = " + startDate2);
        PmEmployeeLeaveofficeQueryCriteria criteria;
        criteria= new PmEmployeeLeaveofficeQueryCriteria();
        //邮件内容
        String strEmailContent = "";
        String strReqParameterContent = "";
        String strFlowdataContent = "";
//        criteria.setLeaveTimeStart(startDate);
//        criteria.setLeaveTimeEnd(now);
        //获取离职人员信息
        List<PmEmployeeLeaveoffice> pmEmployeeLeaveoffices=pmEmployeeLeaveofficeDao.listAllByCriteria(criteria);
        //循环对比相关表
        for (PmEmployeeLeaveoffice pel : pmEmployeeLeaveoffices){
            List<AcReqParameter> acReqParameters=acLeaveFormDao.getReqParameter(pel.getEmployee().getWorkCard());
            for(AcReqParameter arp : acReqParameters){
                strReqParameterContent=strReqParameterContent+arp.getTextField()+"----"+arp.getValueField()+"<br>";
            }
            List<AcReqFlowdata> acReqFlowdatas=acLeaveFormDao.getReqFlowdata(pel.getEmployee().getWorkCard());
            for(AcReqFlowdata arf : acReqFlowdatas){
                strFlowdataContent=strFlowdataContent+arf.getTypeCode()+":"+arf.getVariableKey()+"--"+
                        arf.getVariableName()+"--"+arf.getVariable()+"<br>";
            }
        }
        if(strReqParameterContent.length()>0){
            strReqParameterContent="流程选择人员变更：<br>" +
                    "请到数据表req_parameter.valueFiled中对比替换<br>"+strReqParameterContent;
            strEmailContent=strEmailContent+strReqParameterContent+"<br>";
        }
        if(strFlowdataContent.length()>0){
            strFlowdataContent="流程固定人员变更：<br>" +
                    "请到数据表req_flowdata.variable中对比替换<br>"+strFlowdataContent;
            strEmailContent=strEmailContent+strFlowdataContent+"<br>";
        }

        if(strEmailContent.length()>0){
            //增加操作说明
            strEmailContent="离职人员名单操作说明：<br>"+
            "离职人员名单与OA审批流程有关，请到MySQL数据库中处理；<br>"+
            "处理数据前请跟相关部门主管进行确认由何人接手。"+strEmailContent;
            // 拿server
            // 组建Email
            // 拿取列表
            ToolEmailServerQueryCriteria toolEmailServerQueryCriteria = new ToolEmailServerQueryCriteria();
            toolEmailServerQueryCriteria.setFromUser("HR_system@in-sunten.com");
            List<ToolEmailServer> toolEmailServers = toolEmailServerDao.listAllByCriteria(toolEmailServerQueryCriteria);
            if (toolEmailServers.size() <= 0) {
                throw new InfoCheckWarningMessException("没找到相应的server");
            }
//            System.out.println("toolEmailServers = " + toolEmailServers);
            //查到有信息需要保存下来发邮件给luojb@in-sunten.com.cn
            emailInterface.setMailSubject("员工离职提醒" );
            emailInterface.setStatus("PLAN");
            emailInterface.setEmailServer(toolEmailServers.get(0));
            // 计划发送时间
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
            emailInterface.setPlannedDate(dateTime);
            emailInterface.setSendTo("luojb@in-sunten.com");
            emailInterface.setMailContent(strEmailContent);
//            System.out.println("emailInterface = " + emailInterface);
            toolEmailInterfaceService.insert(emailInterface);
        }


    }
}
