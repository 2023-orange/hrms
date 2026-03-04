package com.sunten.hrms.re.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
import com.sunten.hrms.re.domain.*;
import com.sunten.hrms.re.dao.ReRecruitmentTempInterfaceDao;
import com.sunten.hrms.re.service.*;
import com.sunten.hrms.re.dto.ReRecruitmentTempInterfaceDTO;
import com.sunten.hrms.re.dto.ReRecruitmentTempInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReRecruitmentTempInterfaceMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 应聘全部数据导入的临时表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReRecruitmentTempInterfaceServiceImpl extends ServiceImpl<ReRecruitmentTempInterfaceDao, ReRecruitmentTempInterface> implements ReRecruitmentTempInterfaceService {
    private final ReRecruitmentTempInterfaceDao reRecruitmentTempInterfaceDao;
    private final ReRecruitmentTempInterfaceMapper reRecruitmentTempInterfaceMapper;
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;
    private final ReRecruitmentService reRecruitmentService;
    private final ReTitleService reTitleService;
    private final ReFamilyService reFamilyService;
    private final ReEducationService reEducationService;
    private final ReWorkhistoryService reWorkhistoryService;
    private final ReTrainService reTrainService;
    private final ReAwardService reAwardService;
    private final ReHobbyService reHobbyService;
    @Autowired
    ReRecruitmentTempInterfaceService instance;

    public ReRecruitmentTempInterfaceServiceImpl(ReRecruitmentTempInterfaceDao reRecruitmentTempInterfaceDao, ReRecruitmentTempInterfaceMapper reRecruitmentTempInterfaceMapper,
                                                 FndInterfaceOperationRecordService fndInterfaceOperationRecordService, ReRecruitmentService reRecruitmentService,
                                                 ReTitleService reTitleService, ReFamilyService reFamilyService, ReEducationService reEducationService,
                                                 ReWorkhistoryService reWorkhistoryService, ReTrainService reTrainService, ReAwardService reAwardService,
                                                 ReHobbyService reHobbyService) {
        this.reRecruitmentTempInterfaceDao = reRecruitmentTempInterfaceDao;
        this.reRecruitmentTempInterfaceMapper = reRecruitmentTempInterfaceMapper;
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
        this.reRecruitmentService = reRecruitmentService;
        this.reTitleService = reTitleService;
        this.reFamilyService = reFamilyService;
        this.reEducationService = reEducationService;
        this.reWorkhistoryService = reWorkhistoryService;
        this.reTrainService = reTrainService;
        this.reAwardService = reAwardService;
        this.reHobbyService = reHobbyService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReRecruitmentTempInterfaceDTO insert(ReRecruitmentTempInterface recruitmentTempInterfaceNew) {
        reRecruitmentTempInterfaceDao.insertAllColumn(recruitmentTempInterfaceNew);
        return reRecruitmentTempInterfaceMapper.toDto(recruitmentTempInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReRecruitmentTempInterface recruitmentTempInterface = new ReRecruitmentTempInterface();
        recruitmentTempInterface.setId(id);
        this.delete(recruitmentTempInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReRecruitmentTempInterface recruitmentTempInterface) {
        // TODO    确认删除前是否需要做检查
        reRecruitmentTempInterfaceDao.deleteByEntityKey(recruitmentTempInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReRecruitmentTempInterface recruitmentTempInterfaceNew) {
        ReRecruitmentTempInterface recruitmentTempInterfaceInDb = Optional.ofNullable(reRecruitmentTempInterfaceDao.getByKey(recruitmentTempInterfaceNew.getId())).orElseGet(ReRecruitmentTempInterface::new);
        ValidationUtil.isNull(recruitmentTempInterfaceInDb.getId() ,"RecruitmentTempInterface", "id", recruitmentTempInterfaceNew.getId());
        recruitmentTempInterfaceNew.setId(recruitmentTempInterfaceInDb.getId());
        reRecruitmentTempInterfaceDao.updateAllColumnByKey(recruitmentTempInterfaceNew);
    }

    @Override
    public ReRecruitmentTempInterfaceDTO getByKey(Long id) {
        ReRecruitmentTempInterface recruitmentTempInterface = Optional.ofNullable(reRecruitmentTempInterfaceDao.getByKey(id)).orElseGet(ReRecruitmentTempInterface::new);
        ValidationUtil.isNull(recruitmentTempInterface.getId() ,"RecruitmentTempInterface", "id", id);
        return reRecruitmentTempInterfaceMapper.toDto(recruitmentTempInterface);
    }

    @Override
    public List<ReRecruitmentTempInterfaceDTO> listAll(ReRecruitmentTempInterfaceQueryCriteria criteria) {
        return reRecruitmentTempInterfaceMapper.toDto(reRecruitmentTempInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReRecruitmentTempInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReRecruitmentTempInterface> page = PageUtil.startPage(pageable);
        List<ReRecruitmentTempInterface> recruitmentTempInterfaces = reRecruitmentTempInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reRecruitmentTempInterfaceMapper.toDto(recruitmentTempInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReRecruitmentTempInterfaceDTO> recruitmentTempInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReRecruitmentTempInterfaceDTO recruitmentTempInterfaceDTO : recruitmentTempInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("数据分组id", recruitmentTempInterfaceDTO.getGroupId());
            map.put("操作码", recruitmentTempInterfaceDTO.getOperationCode());
            map.put("错误信息", recruitmentTempInterfaceDTO.getErrorMsg());
            map.put("数据状态", recruitmentTempInterfaceDTO.getDataStatus());
            map.put("部门", recruitmentTempInterfaceDTO.getDeptName());
            map.put("岗位", recruitmentTempInterfaceDTO.getJobName());
            map.put("姓名", recruitmentTempInterfaceDTO.getName());
            map.put("性别", recruitmentTempInterfaceDTO.getGender());
            map.put("身高（厘米）", recruitmentTempInterfaceDTO.getHeight());
            map.put("体重（公斤）", recruitmentTempInterfaceDTO.getWeight());
            map.put("出生日期", recruitmentTempInterfaceDTO.getBirthday());
            map.put("籍贯", recruitmentTempInterfaceDTO.getNativePlace());
            map.put("民族", recruitmentTempInterfaceDTO.getNation());
            map.put("婚姻状况", recruitmentTempInterfaceDTO.getMaritalStatus());
            map.put("政治面貌", recruitmentTempInterfaceDTO.getPolitical());
            map.put("手机号码", recruitmentTempInterfaceDTO.getMobilePhone());
            map.put("E-mail", recruitmentTempInterfaceDTO.getEmail());
            map.put("现居住地址", recruitmentTempInterfaceDTO.getAddress());
            map.put("身份证号码", recruitmentTempInterfaceDTO.getIdNumber());
            map.put("是否与原来单位解除劳动合同", recruitmentTempInterfaceDTO.getOriginalContractFlag());
            map.put("是否有职业病史", recruitmentTempInterfaceDTO.getOccupationalDiseasesFlag());
            map.put("是否与原单位解除保密协议", recruitmentTempInterfaceDTO.getConfidentialityRestrictionsFlag());
            map.put("是否有影响应聘岗位的健康情况", recruitmentTempInterfaceDTO.getHealthFlag());
            map.put("如被录用，可到职时间", recruitmentTempInterfaceDTO.getEntryTime());
            map.put("配偶姓名", recruitmentTempInterfaceDTO.getHalfName());
            map.put("配偶工作单位", recruitmentTempInterfaceDTO.getHalfCompany());
            map.put("配偶职务", recruitmentTempInterfaceDTO.getHalfPost());
            map.put("配偶电话", recruitmentTempInterfaceDTO.getHalfTele());
            map.put("父亲姓名", recruitmentTempInterfaceDTO.getFatherName());
            map.put("父亲工作单位", recruitmentTempInterfaceDTO.getFatherCompany());
            map.put("父亲职务", recruitmentTempInterfaceDTO.getFatherPost());
            map.put("父亲电话", recruitmentTempInterfaceDTO.getFatherTele());
            map.put("母亲姓名", recruitmentTempInterfaceDTO.getMotherName());
            map.put("母亲工作单位", recruitmentTempInterfaceDTO.getMotherCompany());
            map.put("母亲职务", recruitmentTempInterfaceDTO.getMotherPost());
            map.put("母亲电话", recruitmentTempInterfaceDTO.getMotherTele());
            map.put("子女1姓名", recruitmentTempInterfaceDTO.getFirstChildName());
            map.put("子女1性别", recruitmentTempInterfaceDTO.getFirstChildSex());
            map.put("子女1出生日期", recruitmentTempInterfaceDTO.getFirstChildBirthday());
            map.put("子女1工作单位/学习地点", recruitmentTempInterfaceDTO.getFirstChildCompany());
            map.put("子女2姓名", recruitmentTempInterfaceDTO.getSecondChildName());
            map.put("子女2性别", recruitmentTempInterfaceDTO.getSecondChildSex());
            map.put("子女2出生日期", recruitmentTempInterfaceDTO.getSecondChildBirthday());
            map.put("子女2工作单位/学习地点", recruitmentTempInterfaceDTO.getSecondChildCompany());
            map.put("是否有亲属在我公司工作", recruitmentTempInterfaceDTO.getRelationshipFlag());
            map.put("亲属姓名", recruitmentTempInterfaceDTO.getRelationshipName());
            map.put("亲属所在部门/科室", recruitmentTempInterfaceDTO.getRelationshipDeptName());
            map.put("与本人关系", recruitmentTempInterfaceDTO.getRelationship());
            map.put("学历1", recruitmentTempInterfaceDTO.getFirstEducation());
            map.put("学历1毕业院校", recruitmentTempInterfaceDTO.getFirstSchool());
            map.put("学历1专业", recruitmentTempInterfaceDTO.getFirstSpecializedSubject());
            map.put("学历1开始时间", recruitmentTempInterfaceDTO.getFirstEnrollmentTime());
            map.put("学历1结束时间", recruitmentTempInterfaceDTO.getFirstGraduationTime());
            map.put("学历1性质", recruitmentTempInterfaceDTO.getFirstEnrollment());
            map.put("学历2", recruitmentTempInterfaceDTO.getSecondEducation());
            map.put("学历2毕业院校", recruitmentTempInterfaceDTO.getSecondSchool());
            map.put("学历2专业", recruitmentTempInterfaceDTO.getSecondSpecializedSubject());
            map.put("学历2开始时间", recruitmentTempInterfaceDTO.getSecondEnrollmentTime());
            map.put("学历2结束时间", recruitmentTempInterfaceDTO.getSecondGraduationTime());
            map.put("学历2性质", recruitmentTempInterfaceDTO.getSecondEnrollment());
            map.put("工作单位1", recruitmentTempInterfaceDTO.getFirstCompany());
            map.put("工作1开始时间", recruitmentTempInterfaceDTO.getFirstWorkStartTime());
            map.put("工作1结束时间", recruitmentTempInterfaceDTO.getFirstWorkEndTime());
            map.put("工作1部门", recruitmentTempInterfaceDTO.getFirstWorkDepart());
            map.put("工作1职务", recruitmentTempInterfaceDTO.getFirstWorkPost());
            map.put("工作1月薪（税前）", recruitmentTempInterfaceDTO.getFirstWorkSalaryOld());
            map.put("工作1离职原因", recruitmentTempInterfaceDTO.getFirstWorkReasonsLeaving());
            map.put("工作1证明人", recruitmentTempInterfaceDTO.getFirstWorkWitness());
            map.put("工作1证明人电话", recruitmentTempInterfaceDTO.getFirstWorkTele());
            map.put("工作单位2", recruitmentTempInterfaceDTO.getSecondCompany());
            map.put("工作2开始时间", recruitmentTempInterfaceDTO.getSecondWorkStartTime());
            map.put("工作2结束时间", recruitmentTempInterfaceDTO.getSecondWorkEndTime());
            map.put("工作2部门", recruitmentTempInterfaceDTO.getSecondWorkDepart());
            map.put("工作2职务", recruitmentTempInterfaceDTO.getSecondWorkPost());
            map.put("工作2月薪（税前）", recruitmentTempInterfaceDTO.getSecondWorkSalaryOld());
            map.put("工作2离职原因", recruitmentTempInterfaceDTO.getSecondWorkReasonsLeaving());
            map.put("工作2证明人", recruitmentTempInterfaceDTO.getSecondWorkWitness());
            map.put("工作2证明人电话", recruitmentTempInterfaceDTO.getSecondWorkTele());
            map.put("工作单位3", recruitmentTempInterfaceDTO.getThirdCompany());
            map.put("工作3开始时间", recruitmentTempInterfaceDTO.getThirdWorkStartTime());
            map.put("工作3结束时间", recruitmentTempInterfaceDTO.getThirdWorkEndTime());
            map.put("工作3部门", recruitmentTempInterfaceDTO.getThirdWorkDepart());
            map.put("工作3职务", recruitmentTempInterfaceDTO.getThirdWorkPost());
            map.put("工作3月薪（税前）", recruitmentTempInterfaceDTO.getThirdWorkSalaryOld());
            map.put("工作3离职原因", recruitmentTempInterfaceDTO.getThirdWorkReasonsLeaving());
            map.put("工作3证明人", recruitmentTempInterfaceDTO.getThirdWorkWitness());
            map.put("工作3证明人电话", recruitmentTempInterfaceDTO.getThirdWorkTele());
            map.put("工作单位4", recruitmentTempInterfaceDTO.getFourthCompany());
            map.put("工作4开始时间", recruitmentTempInterfaceDTO.getFourthWorkStartTime());
            map.put("工作4结束时间", recruitmentTempInterfaceDTO.getFourthWorkEndTime());
            map.put("工作4部门", recruitmentTempInterfaceDTO.getFourthWorkDepart());
            map.put("工作4职务", recruitmentTempInterfaceDTO.getFourthWorkPost());
            map.put("工作4月薪（税前）", recruitmentTempInterfaceDTO.getFourthWorkSalaryOld());
            map.put("工作4离职原因", recruitmentTempInterfaceDTO.getFourthWorkReasonsLeaving());
            map.put("工作4证明人", recruitmentTempInterfaceDTO.getFourthWorkWitness());
            map.put("工作4证明人电话", recruitmentTempInterfaceDTO.getFourthWorkTele());
            map.put("培训1", recruitmentTempInterfaceDTO.getFirstTrain());
            map.put("培训1开始时间", recruitmentTempInterfaceDTO.getFirstTrainStartTime());
            map.put("培训1结束时间", recruitmentTempInterfaceDTO.getFirstTrainEndTime());
            map.put("培训1时长", recruitmentTempInterfaceDTO.getFirstTrainTime());
            map.put("培训1地点", recruitmentTempInterfaceDTO.getFirstTrainAddress());
            map.put("培训1培训单位", recruitmentTempInterfaceDTO.getFirstTrainCompany());
            map.put("培训1所获证书", recruitmentTempInterfaceDTO.getFirstTrainCredential());
            map.put("培训2", recruitmentTempInterfaceDTO.getSecondTrain());
            map.put("培训2开始时间", recruitmentTempInterfaceDTO.getSecondTrainStartTime());
            map.put("培训2结束时间", recruitmentTempInterfaceDTO.getSecondTrainEndTime());
            map.put("培训2时长", recruitmentTempInterfaceDTO.getSecondTrainTime());
            map.put("培训2地点", recruitmentTempInterfaceDTO.getSecondTrainAddress());
            map.put("培训2培训单位", recruitmentTempInterfaceDTO.getSecondTrainCompany());
            map.put("培训2所获证书", recruitmentTempInterfaceDTO.getSecondTrainCredential());
            map.put("奖处1", recruitmentTempInterfaceDTO.getFirstAward());
            map.put("奖处1获得时间", recruitmentTempInterfaceDTO.getFirstAwardTime());
            map.put("奖处1执行部门", recruitmentTempInterfaceDTO.getFirstAwardDept());
            map.put("奖处1获得原因", recruitmentTempInterfaceDTO.getFirstAwardDescription());
            map.put("奖处2", recruitmentTempInterfaceDTO.getSecondAward());
            map.put("奖处2获得时间", recruitmentTempInterfaceDTO.getSecondAwardTime());
            map.put("奖处2执行部门", recruitmentTempInterfaceDTO.getSecondAwardDept());
            map.put("奖处2获得原因", recruitmentTempInterfaceDTO.getSecondAwardDescription());
            map.put("职称", recruitmentTempInterfaceDTO.getTitleName());
            map.put("职称级别", recruitmentTempInterfaceDTO.getTitleLevel());
            map.put("职称获得时间", recruitmentTempInterfaceDTO.getTitleEvaluationTime());
            map.put("英语掌握情况", recruitmentTempInterfaceDTO.getFirstLevelSelf());
            map.put("英语考级", recruitmentTempInterfaceDTO.getFirstLevelMechanism());
            map.put("其他语种", recruitmentTempInterfaceDTO.getSecondHobby());
            map.put("其他语种掌握情况", recruitmentTempInterfaceDTO.getSecondLevelSelf());
            map.put("其他语种考级", recruitmentTempInterfaceDTO.getSecondLevelMechanism());
            map.put("电脑水平考级", recruitmentTempInterfaceDTO.getThirdLevelSelf());
            map.put("熟练运用软件名", recruitmentTempInterfaceDTO.getFourthHobby());
            map.put("驾驶证级别", recruitmentTempInterfaceDTO.getFifthHobby());
            map.put("爱好特长", recruitmentTempInterfaceDTO.getSixthHobby());
            map.put("id", recruitmentTempInterfaceDTO.getId());
            map.put("创建时间", recruitmentTempInterfaceDTO.getCreateTime());
            map.put("创建人ID", recruitmentTempInterfaceDTO.getCreateBy());
            map.put("修改时间", recruitmentTempInterfaceDTO.getUpdateTime());
            map.put("修改人ID", recruitmentTempInterfaceDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<ReRecruitmentTempInterface> importExcelData(List<ReRecruitmentTempInterface> reRecruitmentTempInterfaces) {
        FndInterfaceOperationRecord fndInterfaceOperationRecord = new FndInterfaceOperationRecord();
        fndInterfaceOperationRecord.setOperationValue("insertReRecruitment");
        fndInterfaceOperationRecord.setOperationDescription("应聘信息导入");
        fndInterfaceOperationRecordService.insert(fndInterfaceOperationRecord);
        try {
            instance.insertMainAndSon(reRecruitmentTempInterfaces, fndInterfaceOperationRecord.getId());
        } catch (Exception e) {
            fndInterfaceOperationRecord.setDataProcessingDescription(ThrowableUtil.getStackTrace(e));
            fndInterfaceOperationRecord.setSuccessFlag(false);
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
            throw new InfoCheckWarningMessException("导入失败，请联系管理员");
        } finally {
            fndInterfaceOperationRecord.setDataProcessingDescription("导入正常");
            fndInterfaceOperationRecord.setSuccessFlag(true);
            fndInterfaceOperationRecordService.update(fndInterfaceOperationRecord);
        }
        // 查除结果集
        ReRecruitmentTempInterfaceQueryCriteria recruitmentTempInterfaceQueryCriteria = new ReRecruitmentTempInterfaceQueryCriteria();
        recruitmentTempInterfaceQueryCriteria.setGroupId(fndInterfaceOperationRecord.getId());
        return reRecruitmentTempInterfaceDao.listAllByCriteria(recruitmentTempInterfaceQueryCriteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void insertMainAndSon(List<ReRecruitmentTempInterface> reRecruitmentTempInterfaces, Long groupId) {
        for (ReRecruitmentTempInterface reRecruitmentTempInterface: reRecruitmentTempInterfaces
             ) {
            reRecruitmentTempInterface.setGroupId(groupId);
            reRecruitmentTempInterface.setDataStatus("T");
            reRecruitmentTempInterface.setOperationCode("C");
            reRecruitmentTempInterfaceDao.insertAllColumn(reRecruitmentTempInterface);
        }
        // 写入主表
        reRecruitmentService.insertByTempInterface(groupId);
        // 写入子
        ReRecruitmentTempInterfaceQueryCriteria reRecruitmentTempInterfaceQueryCriteria = new ReRecruitmentTempInterfaceQueryCriteria();
        reRecruitmentTempInterfaceQueryCriteria.setGroupId(groupId);
        reRecruitmentTempInterfaceQueryCriteria.setDataStatus("T");
        List<ReRecruitmentTempInterface> recruitmentTempInterfaces = reRecruitmentTempInterfaceDao.listAllByCriteria(reRecruitmentTempInterfaceQueryCriteria);
        for (ReRecruitmentTempInterface re : recruitmentTempInterfaces
             ) {
            if (null != re.getReId()) {
                tempInterfaceToFamily(re);
                tempInterfaceToEducation(re);
                tempInterfaceToWorkHistory(re);
                tempInterfaceToTrain(re);
                tempInterfaceToAward(re);
                tempInterfaceToHobby(re);
            }
        }
        // 写入职称
        reTitleService.insertByTempInterface(groupId);
    }

    private void tempInterfaceToHobby(ReRecruitmentTempInterface reRecruitmentTempInterface) {
        ReHobby reHobby;
        ReRecruitment reRecruitment = new ReRecruitment();
        reRecruitment.setId(reRecruitmentTempInterface.getReId());
        if (null != reRecruitmentTempInterface.getFirstLevelSelf()) {
            reHobby = new ReHobby();
            reHobby.setRecruitment(reRecruitment);
            reHobby.setHobby("英语");
            reHobby.setEnabledFlag(true);
            reHobby.setLevelMyself(reRecruitmentTempInterface.getFirstLevelSelf());
            reHobby.setLevelMechanism(reRecruitmentTempInterface.getFirstLevelMechanism());
            reHobby.setAttribute1("个人技能");
            reHobbyService.insert(reHobby);
        }
        if (null != reRecruitmentTempInterface.getSecondHobby()) {
            reHobby = new ReHobby();
            reHobby.setRecruitment(reRecruitment);
            reHobby.setHobby("其他语种");
            reHobby.setEnabledFlag(true);
            reHobby.setLevelMyself(reRecruitmentTempInterface.getSecondLevelSelf());
            reHobby.setLevelMechanism(reRecruitmentTempInterface.getSecondLevelMechanism());
            reHobby.setAttribute1("个人技能");
            reHobbyService.insert(reHobby);
        }
        if (null != reRecruitmentTempInterface.getThirdLevelSelf()) {
            reHobby = new ReHobby();
            reHobby.setRecruitment(reRecruitment);
            reHobby.setHobby("电脑水平");
            reHobby.setEnabledFlag(true);
//            reHobby.setLevelMyself(reRecruitmentTempInterface.getThirdLevelSelf());
            reHobby.setLevelMechanism(reRecruitmentTempInterface.getThirdLevelSelf());
            reHobby.setAttribute1("个人技能");
            reHobbyService.insert(reHobby);
        }
        if (null != reRecruitmentTempInterface.getFourthHobby()) {
            reHobby = new ReHobby();
            reHobby.setRecruitment(reRecruitment);
            reHobby.setHobby("常用软件");
            reHobby.setEnabledFlag(true);
            reHobby.setLevelMechanism(reRecruitmentTempInterface.getFourthHobby());
            reHobby.setAttribute1("个人技能");
            reHobbyService.insert(reHobby);
        }
        if (null != reRecruitmentTempInterface.getFifthHobby()) {
            reHobby = new ReHobby();
            reHobby.setRecruitment(reRecruitment);
            reHobby.setHobby("驾驶证");
            reHobby.setEnabledFlag(true);
            reHobby.setLevelMechanism(reRecruitmentTempInterface.getFifthHobby());
            reHobby.setAttribute1("个人技能");
            reHobbyService.insert(reHobby);
        }
        if (null != reRecruitmentTempInterface.getSixthHobby()) {
            reHobby = new ReHobby();
            reHobby.setRecruitment(reRecruitment);
            reHobby.setHobby("爱好");
            reHobby.setLevelMechanism(reRecruitmentTempInterface.getSixthHobby());
            reHobby.setEnabledFlag(true);
            reHobby.setAttribute1("爱好特长");
            reHobbyService.insert(reHobby);
        }
    }

    private void tempInterfaceToAward(ReRecruitmentTempInterface reRecruitmentTempInterface) {
        ReAward reAward;
        ReRecruitment reRecruitment = new ReRecruitment();
        reRecruitment.setId(reRecruitmentTempInterface.getReId());
        if (null != reRecruitmentTempInterface.getFirstAward()) {
            reAward = new ReAward();
            reAward.setRecruitment(reRecruitment);
            reAward.setAwardName(reRecruitmentTempInterface.getFirstAward());
            reAward.setEnabledFlag(true);
            if (null != reRecruitmentTempInterface.getFirstAwardTime()) {
                reAward.setAwardStarTime(reRecruitmentTempInterface.getFirstAwardTime());
            }
            reAward.setAwardCompany(null != reRecruitmentTempInterface.getFirstAwardDept() ? reRecruitmentTempInterface.getFirstAwardDept() : "");
            reAward.setAwardContent(null != reRecruitmentTempInterface.getFirstAwardDescription() ? reRecruitmentTempInterface.getFirstAwardDescription() : "");
            reAwardService.insert(reAward);
        }
        if (null != reRecruitmentTempInterface.getSecondAward()) {
            reAward = new ReAward();
            reAward.setRecruitment(reRecruitment);
            reAward.setAwardName(reRecruitmentTempInterface.getSecondAward());
            reAward.setEnabledFlag(true);
            if (null != reRecruitmentTempInterface.getSecondAwardTime()) {
                reAward.setAwardStarTime(reRecruitmentTempInterface.getSecondAwardTime());
            }
            reAward.setAwardCompany(null != reRecruitmentTempInterface.getSecondAwardDept() ? reRecruitmentTempInterface.getSecondAwardDept() : "");
            reAward.setAwardContent(null != reRecruitmentTempInterface.getSecondAwardDescription() ? reRecruitmentTempInterface.getSecondAwardDescription() : "");
            reAwardService.insert(reAward);
        }
    }

    private void tempInterfaceToTrain(ReRecruitmentTempInterface reRecruitmentTempInterface) {
        ReTrain reTrain;
        ReRecruitment reRecruitment = new ReRecruitment();
        reRecruitment.setId(reRecruitmentTempInterface.getReId());
        if (null != reRecruitmentTempInterface.getFirstTrain()) {
            reTrain = new ReTrain();
            reTrain.setRecruitment(reRecruitment);
//            if (null != reRecruitmentTempInterface.getFirstTrainStartTime()) {
//                reTrain.setStartTime(reRecruitmentTempInterface.getFirstTrainStartTime());
//            }
//            if (null != reRecruitmentTempInterface.getFirstTrainEndTime()) {
//                reTrain.setEndTime(reRecruitmentTempInterface.getFirstTrainEndTime());
//            }
            if (null != reRecruitmentTempInterface.getFirstTrainTimeNew()) {
                reTrain.setTrainTimeNew(reRecruitmentTempInterface.getFirstTrainTimeNew());
            }
            reTrain.setTrainTime(null != reRecruitmentTempInterface.getFirstTrainTime() ? reRecruitmentTempInterface.getFirstTrainTime() : "");
            reTrain.setTrainAddress(null != reRecruitmentTempInterface.getFirstTrainAddress() ? reRecruitmentTempInterface.getFirstTrainAddress() : "");
            reTrain.setTrainName(reRecruitmentTempInterface.getFirstTrain());
            reTrain.setTrainCompany(null != reRecruitmentTempInterface.getFirstTrainCompany() ? reRecruitmentTempInterface.getFirstTrainCompany() : "");
            reTrain.setCertificate(null != reRecruitmentTempInterface.getFirstTrainCredential() ? reRecruitmentTempInterface.getFirstTrainCredential() : "");
            reTrain.setEnabledFlag(true);
            reTrainService.insert(reTrain);
        }
        if (null != reRecruitmentTempInterface.getSecondTrain()) {
            reTrain = new ReTrain();
            reTrain.setRecruitment(reRecruitment);
//            if (null != reRecruitmentTempInterface.getSecondTrainStartTime()) {
//                reTrain.setStartTime(reRecruitmentTempInterface.getSecondTrainStartTime());
//            }
//            if (null != reRecruitmentTempInterface.getSecondTrainEndTime()) {
//                reTrain.setEndTime(reRecruitmentTempInterface.getSecondTrainEndTime());
//            }
            if (null != reRecruitmentTempInterface.getSecondTrainTimeNew()) {
                reTrain.setTrainTimeNew(reRecruitmentTempInterface.getSecondTrainTimeNew());
            }
            reTrain.setTrainTime(null != reRecruitmentTempInterface.getSecondTrainTime() ? reRecruitmentTempInterface.getSecondTrainTime() : "");
            reTrain.setTrainAddress(null != reRecruitmentTempInterface.getSecondTrainAddress() ? reRecruitmentTempInterface.getSecondTrainAddress() : "");
            reTrain.setTrainName(reRecruitmentTempInterface.getSecondTrain());
            reTrain.setTrainCompany(null != reRecruitmentTempInterface.getSecondTrainCompany() ? reRecruitmentTempInterface.getSecondTrainCompany() : "");
            reTrain.setCertificate(null != reRecruitmentTempInterface.getSecondTrainCredential() ? reRecruitmentTempInterface.getSecondTrainCredential() : "");
            reTrain.setEnabledFlag(true);
            reTrainService.insert(reTrain);
        }
    }

    private void tempInterfaceToWorkHistory(ReRecruitmentTempInterface reRecruitmentTempInterface) {
        ReWorkhistory workhistory;
        ReRecruitment reRecruitment = new ReRecruitment();
        reRecruitment.setId(reRecruitmentTempInterface.getReId());
        if (null != reRecruitmentTempInterface.getFirstCompany()) {
            workhistory = new ReWorkhistory();
            workhistory.setRecruitment(reRecruitment);
            workhistory.setCompany(reRecruitmentTempInterface.getFirstCompany());
//            if (null != reRecruitmentTempInterface.getFirstWorkStartTime()) {
//                workhistory.setStartTime(reRecruitmentTempInterface.getFirstWorkStartTime());
//            }
//            if (null != reRecruitmentTempInterface.getFirstWorkEndTime()) {
//                workhistory.setEndTime(reRecruitmentTempInterface.getFirstWorkEndTime());
//            }
            if (null != reRecruitmentTempInterface.getFirstWorkTimeNew()) {
                workhistory.setWorkTimeNew(reRecruitmentTempInterface.getFirstWorkTimeNew());
            }
            workhistory.setAttribute1(null != reRecruitmentTempInterface.getFirstWorkDepart() ? reRecruitmentTempInterface.getFirstWorkDepart() : "");
            workhistory.setPost(null != reRecruitmentTempInterface.getFirstWorkPost() ? reRecruitmentTempInterface.getFirstWorkPost() : "");
            if (null != reRecruitmentTempInterface.getFirstWorkSalaryOld()) {
                workhistory.setSalaryOld(reRecruitmentTempInterface.getFirstWorkSalaryOld().toString());
            }
            if (null != reRecruitmentTempInterface.getFirstWorkReasonsLeaving()) {
                workhistory.setReasonsLeaving(reRecruitmentTempInterface.getFirstWorkReasonsLeaving());
            }
            if (null != reRecruitmentTempInterface.getFirstWorkWitness()) {
                workhistory.setWitness(reRecruitmentTempInterface.getFirstWorkWitness());
            }
            if (null != reRecruitmentTempInterface.getFirstWorkTele()) {
                workhistory.setTele(reRecruitmentTempInterface.getFirstWorkTele());
            }
            workhistory.setEnabledFlag(true);
            reWorkhistoryService.insert(workhistory);
        }
        if (null != reRecruitmentTempInterface.getSecondCompany()) {
            workhistory = new ReWorkhistory();
            workhistory.setRecruitment(reRecruitment);
            workhistory.setCompany(reRecruitmentTempInterface.getSecondCompany());
//            if (null != reRecruitmentTempInterface.getSecondWorkStartTime()) {
//                workhistory.setStartTime(reRecruitmentTempInterface.getSecondWorkStartTime());
//            }
//            if (null != reRecruitmentTempInterface.getSecondWorkEndTime()) {
//                workhistory.setEndTime(reRecruitmentTempInterface.getSecondWorkEndTime());
//            }
            if (null != reRecruitmentTempInterface.getSecondWorkTimeNew()) {
                workhistory.setWorkTimeNew(reRecruitmentTempInterface.getSecondWorkTimeNew());
            }
            workhistory.setAttribute1(null != reRecruitmentTempInterface.getSecondWorkDepart() ? reRecruitmentTempInterface.getSecondWorkDepart() : "");
            workhistory.setPost(null != reRecruitmentTempInterface.getSecondWorkPost() ? reRecruitmentTempInterface.getSecondWorkPost() : "");
            if (null != reRecruitmentTempInterface.getSecondWorkSalaryOld()) {
                workhistory.setSalaryOld(reRecruitmentTempInterface.getSecondWorkSalaryOld().toString());
            }
            if (null != reRecruitmentTempInterface.getSecondWorkReasonsLeaving()) {
                workhistory.setReasonsLeaving(reRecruitmentTempInterface.getSecondWorkReasonsLeaving());
            }
            if (null != reRecruitmentTempInterface.getSecondWorkWitness()) {
                workhistory.setWitness(reRecruitmentTempInterface.getSecondWorkWitness());
            }
            if (null != reRecruitmentTempInterface.getSecondWorkTele()) {
                workhistory.setTele(reRecruitmentTempInterface.getSecondWorkTele());
            }
            workhistory.setEnabledFlag(true);
            reWorkhistoryService.insert(workhistory);
        }
        if (null != reRecruitmentTempInterface.getThirdCompany()) {
            workhistory = new ReWorkhistory();
            workhistory.setRecruitment(reRecruitment);
            workhistory.setCompany(reRecruitmentTempInterface.getThirdCompany());
//            if (null != reRecruitmentTempInterface.getThirdWorkStartTime()) {
//                workhistory.setStartTime(reRecruitmentTempInterface.getThirdWorkStartTime());
//            }
//            if (null != reRecruitmentTempInterface.getThirdWorkEndTime()) {
//                workhistory.setEndTime(reRecruitmentTempInterface.getThirdWorkEndTime());
//            }
            if (null != reRecruitmentTempInterface.getThirdWorkTimeNew()) {
                workhistory.setWorkTimeNew(reRecruitmentTempInterface.getThirdWorkTimeNew());
            }
            workhistory.setAttribute1(null != reRecruitmentTempInterface.getThirdWorkDepart() ? reRecruitmentTempInterface.getThirdWorkDepart() : "");
            workhistory.setPost(null != reRecruitmentTempInterface.getThirdWorkPost() ? reRecruitmentTempInterface.getThirdWorkPost() : "");
            if (null != reRecruitmentTempInterface.getThirdWorkSalaryOld()) {
                workhistory.setSalaryOld(reRecruitmentTempInterface.getThirdWorkSalaryOld().toString());
            }
            if (null != reRecruitmentTempInterface.getThirdWorkReasonsLeaving()) {
                workhistory.setReasonsLeaving(reRecruitmentTempInterface.getThirdWorkReasonsLeaving());
            }
            if (null != reRecruitmentTempInterface.getThirdWorkWitness()) {
                workhistory.setWitness(reRecruitmentTempInterface.getThirdWorkWitness());
            }
            if (null != reRecruitmentTempInterface.getThirdWorkTele()) {
                workhistory.setTele(reRecruitmentTempInterface.getThirdWorkTele());
            }
            workhistory.setEnabledFlag(true);
            reWorkhistoryService.insert(workhistory);
        }
        if (null != reRecruitmentTempInterface.getFourthCompany()) {
            workhistory = new ReWorkhistory();
            workhistory.setRecruitment(reRecruitment);
            workhistory.setCompany(reRecruitmentTempInterface.getFourthCompany());
//            if (null != reRecruitmentTempInterface.getFourthWorkStartTime()) {
//                workhistory.setStartTime(reRecruitmentTempInterface.getFourthWorkStartTime());
//            }
//            if (null != reRecruitmentTempInterface.getFourthWorkEndTime()) {
//                workhistory.setEndTime(reRecruitmentTempInterface.getFourthWorkEndTime());
//            }
            if (null != reRecruitmentTempInterface.getFourthWorkTimeNew()) {
                workhistory.setWorkTimeNew(reRecruitmentTempInterface.getFourthWorkTimeNew());
            }
            workhistory.setAttribute1(null != reRecruitmentTempInterface.getFourthWorkDepart() ? reRecruitmentTempInterface.getFourthWorkDepart() : "");
            workhistory.setPost(null != reRecruitmentTempInterface.getFourthWorkPost() ? reRecruitmentTempInterface.getFourthWorkPost() : "");
            if (null != reRecruitmentTempInterface.getFourthWorkSalaryOld()) {
                workhistory.setSalaryOld(reRecruitmentTempInterface.getFourthWorkSalaryOld().toString());
            }
            if (null != reRecruitmentTempInterface.getFourthWorkReasonsLeaving()) {
                workhistory.setReasonsLeaving(reRecruitmentTempInterface.getFourthWorkReasonsLeaving());
            }
            if (null != reRecruitmentTempInterface.getFourthWorkWitness()) {
                workhistory.setWitness(reRecruitmentTempInterface.getFourthWorkWitness());
            }
            if (null != reRecruitmentTempInterface.getFourthWorkTele()) {
                workhistory.setTele(reRecruitmentTempInterface.getFourthWorkTele());
            }
            workhistory.setEnabledFlag(true);
            reWorkhistoryService.insert(workhistory);
        }
    }

    private void tempInterfaceToEducation(ReRecruitmentTempInterface reRecruitmentTempInterface) {
        ReEducation reEducation;
        ReRecruitment reRecruitment = new ReRecruitment();
        reRecruitment.setId(reRecruitmentTempInterface.getReId());
        if (null != reRecruitmentTempInterface.getFirstEducation()) {
            reEducation = new ReEducation();
            reEducation.setEducation(reRecruitmentTempInterface.getFirstEducation());
            reEducation.setRecruitment(reRecruitment);
            reEducation.setSchool(null != reRecruitmentTempInterface.getFirstSchool() ? reRecruitmentTempInterface.getFirstSchool() : "");
            reEducation.setSpecializedSubject(null != reRecruitmentTempInterface.getFirstSpecializedSubject() ? reRecruitmentTempInterface.getFirstSpecializedSubject() : "");
            if (reRecruitmentTempInterface.getFirstEnrollmentTime() != null) {
                reEducation.setEnrollmentTime(reRecruitmentTempInterface.getFirstEnrollmentTime());
            }
            if (reRecruitmentTempInterface.getFirstGraduationTime() != null) {
                reEducation.setGraduationTime(reRecruitmentTempInterface.getFirstGraduationTime());
            }
            if (reRecruitmentTempInterface.getFirstEnrollment() != null) {
                reEducation.setEnrollment(reRecruitmentTempInterface.getFirstEnrollment());
            }
            if (reRecruitmentTempInterface.getFirstEnrollmentTimeNew() != null) {
                reEducation.setEnrollmentTimeNew(reRecruitmentTempInterface.getFirstEnrollmentTimeNew());
            }
            reEducation.setEnabledFlag(true);
            reEducation.setNewEducationFlag(true);
            reEducationService.insert(reEducation);
        }
        if (null != reRecruitmentTempInterface.getSecondEducation()) {
            reEducation = new ReEducation();
            reEducation.setEducation(reRecruitmentTempInterface.getSecondEducation());
            reEducation.setRecruitment(reRecruitment);
            reEducation.setSchool(null != reRecruitmentTempInterface.getSecondSchool() ? reRecruitmentTempInterface.getSecondSchool() : "");
            reEducation.setSpecializedSubject(null != reRecruitmentTempInterface.getSecondSpecializedSubject() ? reRecruitmentTempInterface.getSecondSpecializedSubject() : "");
            if (reRecruitmentTempInterface.getSecondEnrollmentTime() != null) {
                reEducation.setEnrollmentTime(reRecruitmentTempInterface.getSecondEnrollmentTime());
            }
            if (reRecruitmentTempInterface.getSecondGraduationTime() != null) {
                reEducation.setGraduationTime(reRecruitmentTempInterface.getSecondGraduationTime());
            }
            if (reRecruitmentTempInterface.getSecondEnrollment() != null) {
                reEducation.setEnrollment(reRecruitmentTempInterface.getSecondEnrollment());
            }
            if (reRecruitmentTempInterface.getSecondEnrollmentTimeNew() != null) {
                reEducation.setEnrollmentTimeNew(reRecruitmentTempInterface.getSecondEnrollmentTimeNew());
            }
            reEducation.setEnabledFlag(true);
            reEducation.setNewEducationFlag(false);
            reEducationService.insert(reEducation);
        }
    }

    private void tempInterfaceToFamily(ReRecruitmentTempInterface reRecruitmentTempInterface) {
        ReFamily reFamily;
        ReRecruitment reRecruitment = new ReRecruitment();
        reRecruitment.setId(reRecruitmentTempInterface.getReId());
        if (null != reRecruitmentTempInterface.getHalfName()) {
            reFamily = new ReFamily();
            reFamily.setRecruitment(reRecruitment);
            reFamily.setRelationship("配偶");
            reFamily.setEnabledFlag(true);
            reFamily.setName(reRecruitmentTempInterface.getHalfName());
            reFamily.setCompany(null != reRecruitmentTempInterface.getHalfCompany() ? reRecruitmentTempInterface.getHalfCompany() : "");
            reFamily.setPost(null != reRecruitmentTempInterface.getHalfPost() ? reRecruitmentTempInterface.getHalfPost() : "");
            reFamily.setTele(null != reRecruitmentTempInterface.getHalfTele() ? reRecruitmentTempInterface.getHalfTele() : "");
            reFamilyService.insert(reFamily);
        }
        if (null != reRecruitmentTempInterface.getFatherName()) {
            reFamily = new ReFamily();
            reFamily.setRecruitment(reRecruitment);
            reFamily.setRelationship("父亲");
            reFamily.setEnabledFlag(true);
            reFamily.setName(reRecruitmentTempInterface.getFatherName());
            reFamily.setCompany(null != reRecruitmentTempInterface.getFatherCompany() ? reRecruitmentTempInterface.getFatherCompany() : "");
            reFamily.setPost(null != reRecruitmentTempInterface.getFatherPost() ? reRecruitmentTempInterface.getFatherPost() : "");
            reFamily.setTele(null != reRecruitmentTempInterface.getFatherTele() ? reRecruitmentTempInterface.getFatherTele() : "");
            reFamilyService.insert(reFamily);
        }
        if (null != reRecruitmentTempInterface.getMotherName()) {
            reFamily = new ReFamily();
            reFamily.setRecruitment(reRecruitment);
            reFamily.setRelationship("母亲");
            reFamily.setEnabledFlag(true);
            reFamily.setName(reRecruitmentTempInterface.getMotherName());
            reFamily.setCompany(null != reRecruitmentTempInterface.getMotherCompany() ? reRecruitmentTempInterface.getMotherCompany() : "");
            reFamily.setPost(null != reRecruitmentTempInterface.getMotherPost() ? reRecruitmentTempInterface.getMotherPost() : "");
            reFamily.setTele(null != reRecruitmentTempInterface.getMotherTele() ? reRecruitmentTempInterface.getMotherTele() : "");
            reFamilyService.insert(reFamily);
        }
        if (null != reRecruitmentTempInterface.getFirstChildName()) {
            reFamily = new ReFamily();
            reFamily.setRecruitment(reRecruitment);
            if (null != reRecruitmentTempInterface.getFirstChildSex()) {
                reFamily.setGender(reRecruitmentTempInterface.getFirstChildSex());
                if (reRecruitmentTempInterface.getFirstChildSex().contains("男")){
                    reFamily.setRelationship("儿子");
                } else {
                    reFamily.setRelationship("女儿");
                }
            }
            reFamily.setEnabledFlag(true);
            reFamily.setName(reRecruitmentTempInterface.getFirstChildName());
            if (null != reRecruitmentTempInterface.getFirstChildBirthday()) {
                reFamily.setBirthday(reRecruitmentTempInterface.getFirstChildBirthday());
            }
            reFamily.setCompany(null != reRecruitmentTempInterface.getFirstChildCompany() ? reRecruitmentTempInterface.getFirstChildCompany() : "");
            reFamilyService.insert(reFamily);
        }
        if (null != reRecruitmentTempInterface.getSecondChildName()) {
            reFamily = new ReFamily();
            reFamily.setRecruitment(reRecruitment);
            if (null != reRecruitmentTempInterface.getSecondChildSex()) {
                reFamily.setGender(reRecruitmentTempInterface.getSecondChildSex());
                if (reRecruitmentTempInterface.getSecondChildSex().contains("男")){
                    reFamily.setRelationship("儿子");
                } else {
                    reFamily.setRelationship("女儿");
                }
            }
            reFamily.setEnabledFlag(true);
            reFamily.setName(reRecruitmentTempInterface.getSecondChildName());
            if (null != reRecruitmentTempInterface.getSecondChildBirthday()) {
                reFamily.setBirthday(reRecruitmentTempInterface.getSecondChildBirthday());
            }
            reFamily.setCompany(null != reRecruitmentTempInterface.getSecondChildCompany() ? reRecruitmentTempInterface.getSecondChildCompany() : "");
            reFamilyService.insert(reFamily);
        }
    }

}
