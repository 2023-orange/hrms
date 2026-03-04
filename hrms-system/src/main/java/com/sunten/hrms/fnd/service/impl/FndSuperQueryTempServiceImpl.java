package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndSuperQueryTempDao;
import com.sunten.hrms.fnd.domain.FndSuperQueryGroup;
import com.sunten.hrms.fnd.domain.FndSuperQueryTemp;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupQueryCriteria;
import com.sunten.hrms.fnd.dto.FndSuperQueryTempDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryTempQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.mapper.FndSuperQueryTempMapper;
import com.sunten.hrms.fnd.service.FndSuperQueryGroupService;
import com.sunten.hrms.fnd.service.FndSuperQueryTempService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dao.*;
import com.sunten.hrms.pm.domain.*;
import com.sunten.hrms.td.dao.TdPlanResultDao;
import com.sunten.hrms.td.vo.PlanChildResultVo;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 超级查询数据临时表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndSuperQueryTempServiceImpl extends ServiceImpl<FndSuperQueryTempDao, FndSuperQueryTemp> implements FndSuperQueryTempService {
    private final FndSuperQueryTempDao fndSuperQueryTempDao;
    private final FndSuperQueryTempMapper fndSuperQueryTempMapper;
    private final PmEmployeeDao pmEmployeeDao;
    private final PmEmployeeAwardDao pmEmployeeAwardDao;
    private final PmEmployeeContractDao pmEmployeeContractDao;
    private final PmEmployeeEducationDao pmEmployeeEducationDao;
    private final PmEmployeeEmergencyDao pmEmployeeEmergencyDao;
    private final PmEmployeeEntryDao pmEmployeeEntryDao;
    private final PmEmployeeFamilyDao pmEmployeeFamilyDao;
    private final PmEmployeePoliticalDao pmEmployeePoliticalDao;
    private final PmEmployeePostotherDao pmEmployeePostotherDao;
    private final PmEmployeeHobbyDao pmEmployeeHobbyDao;
    private final PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao;
    private final PmEmployeeTeleDao pmEmployeeTeleDao;
    private final PmEmployeeTitleDao pmEmployeeTitleDao;
    private final PmEmployeeVocationalDao pmEmployeeVocationalDao;
    private final PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao;
    private final TdPlanResultDao tdPlanResultDao;
    private final FndUserService fndUserService;
    private final PmEmployeeJobDao pmEmployeeJobDao;
    private final PmEmployeeJobTransferDao pmEmployeeJobTransferDao;
    private final PmEmployeeLeaveofficeDao pmEmployeeLeaveofficeDao;
    private final FndSuperQueryGroupService fndSuperQueryGroupService;
    @Autowired
    private FndSuperQueryTempService instance;
    // 用于计算条数
    private int order = 0;

    public FndSuperQueryTempServiceImpl(FndSuperQueryTempDao fndSuperQueryTempDao, FndSuperQueryTempMapper fndSuperQueryTempMapper,
                                        PmEmployeeDao pmEmployeeDao, PmEmployeeAwardDao pmEmployeeAwardDao, PmEmployeeContractDao pmEmployeeContractDao,
                                        PmEmployeeEducationDao pmEmployeeEducationDao, PmEmployeeEmergencyDao pmEmployeeEmergencyDao,
                                        PmEmployeeEntryDao pmEmployeeEntryDao, PmEmployeeFamilyDao pmEmployeeFamilyDao,
                                        PmEmployeePoliticalDao pmEmployeePoliticalDao, PmEmployeePostotherDao pmEmployeePostotherDao,
                                        PmEmployeeHobbyDao pmEmployeeHobbyDao, PmEmployeeSocialrelationsDao pmEmployeeSocialrelationsDao,
                                        PmEmployeeTeleDao pmEmployeeTeleDao, PmEmployeeTitleDao pmEmployeeTitleDao,
                                        PmEmployeeVocationalDao pmEmployeeVocationalDao, PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao,
                                        TdPlanResultDao tdPlanResultDao, FndUserService fndUserService, PmEmployeeJobDao pmEmployeeJobDao,
                                        PmEmployeeJobTransferDao pmEmployeeJobTransferDao, PmEmployeeLeaveofficeDao pmEmployeeLeaveofficeDao, FndSuperQueryGroupService fndSuperQueryGroupService) {
        this.fndSuperQueryTempDao = fndSuperQueryTempDao;
        this.fndSuperQueryTempMapper = fndSuperQueryTempMapper;
        this.pmEmployeeDao = pmEmployeeDao;
        this.pmEmployeeAwardDao = pmEmployeeAwardDao;
        this.pmEmployeeContractDao = pmEmployeeContractDao;
        this.pmEmployeeEducationDao = pmEmployeeEducationDao;
        this.pmEmployeeEmergencyDao = pmEmployeeEmergencyDao;
        this.pmEmployeeEntryDao = pmEmployeeEntryDao;
        this.pmEmployeeFamilyDao = pmEmployeeFamilyDao;
        this.pmEmployeePoliticalDao = pmEmployeePoliticalDao;
        this.pmEmployeePostotherDao = pmEmployeePostotherDao;
        this.pmEmployeeHobbyDao = pmEmployeeHobbyDao;
        this.pmEmployeeSocialrelationsDao = pmEmployeeSocialrelationsDao;
        this.pmEmployeeTeleDao = pmEmployeeTeleDao;
        this.pmEmployeeTitleDao = pmEmployeeTitleDao;
        this.pmEmployeeVocationalDao = pmEmployeeVocationalDao;
        this.pmEmployeeWorkhistoryDao = pmEmployeeWorkhistoryDao;
        this.tdPlanResultDao = tdPlanResultDao;
        this.fndUserService = fndUserService;
        this.pmEmployeeJobDao = pmEmployeeJobDao;
        this.pmEmployeeJobTransferDao = pmEmployeeJobTransferDao;
        this.pmEmployeeLeaveofficeDao = pmEmployeeLeaveofficeDao;
        this.fndSuperQueryGroupService = fndSuperQueryGroupService;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public FndSuperQueryTempDTO insert(FndSuperQueryTemp superQueryTempNew) {
//        fndSuperQueryTempDao.insertAllColumn(superQueryTempNew);
//        return fndSuperQueryTempMapper.toDto(superQueryTempNew);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void delete(Long id) {
//        FndSuperQueryTemp superQueryTemp = new FndSuperQueryTemp();
//        superQueryTemp.setId(id);
//        this.delete(superQueryTemp);
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void delete(FndSuperQueryTemp superQueryTemp) {
//        // TODO    确认删除前是否需要做检查
//        fndSuperQueryTempDao.deleteByEntityKey(superQueryTemp);
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void update(FndSuperQueryTemp superQueryTempNew) {
//        FndSuperQueryTemp superQueryTempInDb = Optional.ofNullable(fndSuperQueryTempDao.getByKey(superQueryTempNew.getId())).orElseGet(FndSuperQueryTemp::new);
//        ValidationUtil.isNull(superQueryTempInDb.getId() ,"SuperQueryTemp", "id", superQueryTempNew.getId());
//        superQueryTempNew.setId(superQueryTempInDb.getId());
//        fndSuperQueryTempDao.updateAllColumnByKey(superQueryTempNew);
//    }

//    @Override
//    public FndSuperQueryTempDTO getByKey(Long id) {
//        FndSuperQueryTemp superQueryTemp = Optional.ofNullable(fndSuperQueryTempDao.getByKey(id)).orElseGet(FndSuperQueryTemp::new);
//        ValidationUtil.isNull(superQueryTemp.getId() ,"SuperQueryTemp", "id", id);
//        return fndSuperQueryTempMapper.toDto(superQueryTemp);
//    }

    @Override
    public List<FndSuperQueryTempDTO> listAll(FndSuperQueryTempQueryCriteria criteria) {
        if (null == criteria.getSearchUserId()) {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            criteria.setSearchUserId(user.getId());
        }
        insertSuperQueryTemp(criteria);
        return fndSuperQueryTempMapper.toDto(fndSuperQueryTempDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndSuperQueryTempQueryCriteria criteria, Pageable pageable) {
        if (null == criteria.getSearchUserId()) {
            FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
            criteria.setSearchUserId(user.getId());
        }
        insertSuperQueryTemp(criteria);
        Page<FndSuperQueryTemp> page = PageUtil.startPage(pageable);
        List<FndSuperQueryTemp> superQueryTemps = fndSuperQueryTempDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndSuperQueryTempMapper.toDto(superQueryTemps), page.getTotal());
    }

    private void insertSuperQueryTemp(FndSuperQueryTempQueryCriteria criteria) {
        if (criteria.getCreateFlag()) {
            switch (criteria.getQueryType()) {
                case "ORIGINAL":
                    this.instance.insertSuperQueryOriginal(criteria);
                    break;
                case "UNPIVOT":
                    this.instance.insertSuperQueryUnpivot(criteria);
                    break;
                case "CROSS":
                default:
                    this.instance.insertSuperQueryCross(criteria);
                    break;
            }
        }
    }

    @Override
    public void download(List<FndSuperQueryTempDTO> superQueryTempDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndSuperQueryTempDTO superQueryTempDTO : superQueryTempDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("姓名", superQueryTempDTO.getName());
            map.put("工牌号", superQueryTempDTO.getWorkCard());
            map.put("信息类别", superQueryTempDTO.getType());
            map.put("所属字段", superQueryTempDTO.getColName());
            map.put("具体值", superQueryTempDTO.getColNameChn());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSuperQueryOriginal(FndSuperQueryTempQueryCriteria criteria) {
        String queryValue = criteria.getQueryValue();
        Long userId = criteria.getSearchUserId();
        // 清空表
        fndSuperQueryTempDao.deleteTemp(criteria.getSearchUserId());
        // 查询
        List<FndSuperQueryTemp> tempList = new ArrayList<>();

        List<PmEmployee> pmEmployeeList = pmEmployeeDao.superQuery(queryValue);
        List<PmEmployeeAward> pmEmployeeAwardList = pmEmployeeAwardDao.superQuery(queryValue);
        List<PmEmployeeContract> pmEmployeeContractList = pmEmployeeContractDao.superQuery(queryValue);
        List<PmEmployeeEducation> pmEmployeeEducationList = pmEmployeeEducationDao.superQuery(queryValue);
        List<PmEmployeeEmergency> pmEmployeeEmergencyList = pmEmployeeEmergencyDao.superQuery(queryValue);
        List<PmEmployeeEntry> pmEmployeeEntryList = pmEmployeeEntryDao.superQuery(queryValue);
        List<PmEmployeeFamily> pmEmployeeFamilyList = pmEmployeeFamilyDao.superQuery(queryValue);
        List<PmEmployeePolitical> pmEmployeePoliticalList = pmEmployeePoliticalDao.superQuery(queryValue);
        List<PmEmployeePostother> pmEmployeePostotherList = pmEmployeePostotherDao.superQuery(queryValue);
        List<PmEmployeeHobby> pmEmployeeHobbyList = pmEmployeeHobbyDao.superQuery(queryValue);
        List<PmEmployeeSocialrelations> pmEmployeeSocialrelationsList = pmEmployeeSocialrelationsDao.superQuery(queryValue);
        List<PmEmployeeTele> pmEmployeeTeleList = pmEmployeeTeleDao.superQuery(queryValue);
        List<PmEmployeeTitle> pmEmployeeTitleList = pmEmployeeTitleDao.superQuery(queryValue);
        List<PmEmployeeVocational> pmEmployeeVocationalList = pmEmployeeVocationalDao.superQuery(queryValue);
        List<PmEmployeeWorkhistory> pmEmployeeWorkhistoryList = pmEmployeeWorkhistoryDao.superQuery(queryValue);
        List<PlanChildResultVo> planChildResultVos = tdPlanResultDao.superQuery(queryValue);
        List<PmEmployeeJob> pmEmployeeJobList = pmEmployeeJobDao.superQuery(queryValue);
        List<PmEmployeeJobTransfer> pmEmployeeJobTransferList = pmEmployeeJobTransferDao.superQuery(queryValue);
        List<PmEmployeeLeaveoffice> pmEmployeeLeaveoffices = pmEmployeeLeaveofficeDao.superQuery(queryValue);


        tempList.addAll(pmEmployeeChangeToQueryTemp(pmEmployeeList, userId));
        tempList.addAll(pmEmployeeAwardChangeToQueryTemp(pmEmployeeAwardList, userId));
        tempList.addAll(pmEmployeeContractChangeToQueryTemp(pmEmployeeContractList, userId));
        tempList.addAll(pmEmployeeEducationChangeToQueryTemp(pmEmployeeEducationList, userId));
        tempList.addAll(pmEmployeeEmergencyChangeToQueryTemp(pmEmployeeEmergencyList, userId));
        tempList.addAll(pmEmployeeEntryChangeToQueryTemp(pmEmployeeEntryList, userId));
        tempList.addAll(pmEmployeeFamilyChangeToQueryTemp(pmEmployeeFamilyList, userId));
        tempList.addAll(pmEmployeePoliticalChangeToQueryTemp(pmEmployeePoliticalList, userId));
        tempList.addAll(pmEmployeePostotherChangeToQueryTemp(pmEmployeePostotherList, userId));
        tempList.addAll(pmEmployeeHobbyChangeToQueryTemp(pmEmployeeHobbyList, userId));
        tempList.addAll(pmEmployeeSocialrelationChangeToQueryTemp(pmEmployeeSocialrelationsList, userId));
        tempList.addAll(pmEmployeeTeleChangeToQueryTemp(pmEmployeeTeleList, userId));
        tempList.addAll(pmEmployeeTitleChangeToQueryTemp(pmEmployeeTitleList, userId));
        tempList.addAll(pmEmployeeVocationalChangeToQueryTemp(pmEmployeeVocationalList, userId));
        tempList.addAll(pmEmployeeWorkhistoryChangeToQueryTemp(pmEmployeeWorkhistoryList, userId));
        tempList.addAll(planChildResultVosChangeToQueryTemp(planChildResultVos, userId));
        tempList.addAll(pmEmployeeJobsChangeToQueryTemp(pmEmployeeJobList, userId));
        tempList.addAll(pmEmployeeJobTransfersChangeToQueryTemp(pmEmployeeJobTransferList, userId));
        tempList.addAll(pmEmployeeLeaveChangeToQueryTemp(pmEmployeeLeaveoffices, userId));

        // 分批插入临时表
        if (tempList.size() != 0) {
            int count = tempList.size() / 200;
            int mod = tempList.size() % 200;
            if (count == 0) { // 不足200条
                // 写入搜索结果
                fndSuperQueryTempDao.insertCollection(tempList);
            } else { // 超过200条
                // 循环写入搜索结果
                for (int i = 0; i < count; i++) {
                    int finalI = i;
                    fndSuperQueryTempDao.insertCollection(tempList.stream().filter(
                            FndSuperQueryTemp -> (FndSuperQueryTemp.getOrder() >= finalI * 200 && FndSuperQueryTemp.getOrder() < (finalI + 1) * 200)
                    ).collect(Collectors.toList()));
                }
                // 余数写入搜索结果
                fndSuperQueryTempDao.insertCollection(tempList.stream().filter(
                        FndSuperQueryTemp -> (FndSuperQueryTemp.getOrder() >= count * 200 && FndSuperQueryTemp.getOrder() <= count * 200 + mod)
                ).collect(Collectors.toList()));
            }
        }
        order = 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSuperQueryUnpivot(FndSuperQueryTempQueryCriteria criteria) {
        FndSuperQueryGroup group = getSuperQueryGroup(criteria.getGroupName(), true);
        fndSuperQueryTempDao.deleteTemp(criteria.getSearchUserId());
        fndSuperQueryTempDao.insertTempByUnpivot(criteria, group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSuperQueryCross(FndSuperQueryTempQueryCriteria criteria) {
        FndSuperQueryGroup group = getSuperQueryGroup(criteria.getGroupName(), true);
        fndSuperQueryTempDao.deleteTemp(criteria.getSearchUserId());
        fndSuperQueryTempDao.insertTempByCross(criteria, group);

    }

    private FndSuperQueryGroup getSuperQueryGroup(String groupName, Boolean enabledFlag) {
        FndSuperQueryGroupQueryCriteria groupQueryCriteria = new FndSuperQueryGroupQueryCriteria();
        groupQueryCriteria.setGroupName(groupName);
        groupQueryCriteria.setEnabledFlag(enabledFlag);
        List<FndSuperQueryGroup> groups = fndSuperQueryGroupService.listAllExpand(groupQueryCriteria);
        if (null == groups || groups.size() == 0) {
            throw new InfoCheckWarningMessException("找不到超级查询组[" + groupName + "]。");
        } else {
            if (groups.size() > 1) {
                throw new InfoCheckWarningMessException("找到多个超级查询组[" + groupName + "]。");
            }
        }
        return groups.get(0);
    }

    private List<FndSuperQueryTemp> pmEmployeeLeaveChangeToQueryTemp(List<PmEmployeeLeaveoffice> pmEmployeeLeaveoffices, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeLeaveoffice pel : pmEmployeeLeaveoffices
        ) {
            if (null != pel.getDeptName()) {
                tempList.add(new FndSuperQueryTemp(pel.getDeptName(), "部门名称", "离职情况", pel.getEmployee().getId(), order++, userId));
            }
            if (null != pel.getJobName()) {
                tempList.add(new FndSuperQueryTemp(pel.getJobName(), "岗位名称", "离职情况", pel.getEmployee().getId(), order++, userId));
            }
            if (null != pel.getLeaveType()) {
                tempList.add(new FndSuperQueryTemp(pel.getLeaveType(), "离职类别", "离职情况", pel.getEmployee().getId(), order++, userId));
            }
            if (null != pel.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pel.getRemarks(), "离职备注", "离职情况", pel.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeJobTransfersChangeToQueryTemp(List<PmEmployeeJobTransfer> pmEmployeeJobTransfers, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeJobTransfer pejt : pmEmployeeJobTransfers
        ) {
            if (null != pejt.getOldJob() && null != pejt.getOldJob().getJobName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getOldJob().getJobName(), "原岗位名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getNewJob() && null != pejt.getNewJob().getJobName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getNewJob().getJobName(), "现岗位名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getTransferReason()) {
                tempList.add(new FndSuperQueryTemp(pejt.getTransferReason(), "调动原因", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pejt.getRemarks(), "调动备注", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getTransferType()) {
                tempList.add(new FndSuperQueryTemp(pejt.getTransferType(), "调动类别", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getOldDept() && null != pejt.getOldDept().getExtDeptName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getOldDept().getExtDeptName(), "原部门名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getOldDept() && null != pejt.getOldDept().getExtDepartmentName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getOldDept().getExtDepartmentName(), "原科室名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getOldDept() && null != pejt.getOldDept().getExtTeamName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getOldDept().getExtTeamName(), "原班组名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getNewDept() && null != pejt.getNewDept().getExtDeptName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getNewDept().getExtDeptName(), "现部门名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getNewDept() && null != pejt.getNewDept().getExtDepartmentName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getNewDept().getExtDepartmentName(), "现科室名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getNewDept() && null != pejt.getNewDept().getExtTeamName()) {
                tempList.add(new FndSuperQueryTemp(pejt.getNewDept().getExtTeamName(), "现班组名称", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
            if (null != pejt.getTransferForm()) {
                tempList.add(new FndSuperQueryTemp(pejt.getTransferForm(), "调岗类型", "岗位调动", pejt.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeJobsChangeToQueryTemp(List<PmEmployeeJob> pmEmployeeJobList, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeJob pej : pmEmployeeJobList
        ) {
            if (null != pej.getJob() && null != pej.getJob().getJobName()) {
                tempList.add(new FndSuperQueryTemp(pej.getJob().getJobName(), "岗位名称", "岗位关系", pej.getEmployee().getId(), order++, userId));
            }
            if (null != pej.getDept() && null != pej.getDept().getExtDeptName()) {
                tempList.add(new FndSuperQueryTemp(pej.getDept().getExtDeptName(), "部门名称", "岗位关系", pej.getEmployee().getId(), order++, userId));
            }
            if (null != pej.getDept() && null != pej.getDept().getExtDepartmentName()) {
                tempList.add(new FndSuperQueryTemp(pej.getDept().getExtDepartmentName(), "科室名称", "岗位关系", pej.getEmployee().getId(), order++, userId));
            }
            if (null != pej.getDept() && null != pej.getDept().getExtTeamName()) {
                tempList.add(new FndSuperQueryTemp(pej.getDept().getExtTeamName(), "班组名称", "岗位关系", pej.getEmployee().getId(), order++, userId));
            }
            if (null != pej.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pej.getRemarks(), "备注", "岗位关系", pej.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> planChildResultVosChangeToQueryTemp(List<PlanChildResultVo> planChildResultVos, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PlanChildResultVo pcr : planChildResultVos
        ) {
            if (null != pcr.getTrainingName()) {
                tempList.add(new FndSuperQueryTemp(pcr.getTrainingName(), "培训名称", "培训记录", pcr.getEmployeeId(), order++, userId));
            }
            if (null != pcr.getTrainingContent()) {
                tempList.add(new FndSuperQueryTemp(pcr.getTrainingContent(), "培训内容", "培训记录", pcr.getEmployeeId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeChangeToQueryTemp(List<PmEmployee> pmEmployees, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployee pe : pmEmployees
        ) {
            if (null != pe.getWorkCard()) {
                tempList.add(new FndSuperQueryTemp(pe.getWorkCard(), "工牌号", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getName()) {
                tempList.add(new FndSuperQueryTemp(pe.getName(), "姓名", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getNameAbbreviation()) {
                tempList.add(new FndSuperQueryTemp(pe.getNameAbbreviation(), "姓名拼音简称", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getGender()) {
                tempList.add(new FndSuperQueryTemp(pe.getGender(), "性别", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getIdNumber()) {
                tempList.add(new FndSuperQueryTemp(pe.getIdNumber(), "身份证号", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getCalendar()) {
                tempList.add(new FndSuperQueryTemp(pe.getCalendar(), "农历公历", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getNation()) {
                tempList.add(new FndSuperQueryTemp(pe.getNation(), "民族", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getMaritalStatus()) {
                tempList.add(new FndSuperQueryTemp(pe.getMaritalStatus(), "婚姻状态", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getHouseholdCharacter()) {
                tempList.add(new FndSuperQueryTemp(pe.getHouseholdCharacter(), "户口性质", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getAddress()) {
                tempList.add(new FndSuperQueryTemp(pe.getAddress(), "地址", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getZipcode()) {
                tempList.add(new FndSuperQueryTemp(pe.getZipcode(), "现住邮编", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getHouseholdAddress()) {
                tempList.add(new FndSuperQueryTemp(pe.getHouseholdAddress(), "户口地址", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getHouseholdZipcode()) {
                tempList.add(new FndSuperQueryTemp(pe.getHouseholdZipcode(), "户口邮编", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getCollectiveAddress()) {
                tempList.add(new FndSuperQueryTemp(pe.getCollectiveAddress(), "集体户口所在地", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getNativePlace()) {
                tempList.add(new FndSuperQueryTemp(pe.getNativePlace(), "籍贯", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getRegisteredResidence()) {
                tempList.add(new FndSuperQueryTemp(pe.getRegisteredResidence(), "户口所在地", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getEmployeeType()) {
                tempList.add(new FndSuperQueryTemp(pe.getEmployeeType(), "员工性质", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getOccupationCategory()) {
                tempList.add(new FndSuperQueryTemp(pe.getOccupationCategory(), "职种", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getEmailInside()) {
                tempList.add(new FndSuperQueryTemp(pe.getEmailInside(), "内部邮箱", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getAdministrationCategory()) {
                tempList.add(new FndSuperQueryTemp(pe.getAdministrationCategory(), "类别区分", "人员主要信息", pe.getId(), order++, userId));
            }
            if (null != pe.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pe.getRemarks(), "备注", "人员主要信息", pe.getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeAwardChangeToQueryTemp(List<PmEmployeeAward> pmEmployeeAwards, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeAward pea : pmEmployeeAwards
        ) {
            if (null != pea.getAwardCompany()) {
                tempList.add(new FndSuperQueryTemp(pea.getAwardCompany(), "奖罚单位", "奖罚记录", pea.getEmployee().getId(), order++, userId));
            }
            if (null != pea.getAwardContent()) {
                tempList.add(new FndSuperQueryTemp(pea.getAwardContent(), "奖罚内容", "奖罚记录", pea.getEmployee().getId(), order++, userId));
            }
            if (null != pea.getAwardName()) {
                tempList.add(new FndSuperQueryTemp(pea.getAwardName(), "奖罚名称", "奖罚记录", pea.getEmployee().getId(), order++, userId));
            }
            if (null != pea.getAwardResult()) {
                tempList.add(new FndSuperQueryTemp(pea.getAwardResult(), "奖罚结果", "奖罚记录", pea.getEmployee().getId(), order++, userId));
            }
            if (null != pea.getType()) {
                tempList.add(new FndSuperQueryTemp(pea.getType(), "奖励或惩罚", "奖罚记录", pea.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeContractChangeToQueryTemp(List<PmEmployeeContract> pmEmployeeContracts, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeContract pec : pmEmployeeContracts
        ) {
            if (null != pec.getContractType()) {
                tempList.add(new FndSuperQueryTemp(pec.getContractType(), "期限类别", "合同情况", pec.getEmployee().getId(), order++, userId));
            }
            if (null != pec.getContractValidity()) {
                tempList.add(new FndSuperQueryTemp(pec.getContractValidity(), "合同期限", "合同情况", pec.getEmployee().getId(), order++, userId));
            }
            if (null != pec.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pec.getRemarks(), "备注", "合同情况", pec.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeEducationChangeToQueryTemp(List<PmEmployeeEducation> pmEmployeeEducations, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeEducation pee : pmEmployeeEducations
        ) {
            if (null != pee.getEducation()) {
                tempList.add(new FndSuperQueryTemp(pee.getEducation(), "学历", "学历", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getEnrollment()) {
                tempList.add(new FndSuperQueryTemp(pee.getEnrollment(), "入学性质", "学历", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getSchool()) {
                tempList.add(new FndSuperQueryTemp(pee.getSchool(), "毕业学校", "学历", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getSpecializedSubject()) {
                tempList.add(new FndSuperQueryTemp(pee.getSpecializedSubject(), "专业", "学历", pee.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeEmergencyChangeToQueryTemp(List<PmEmployeeEmergency> pmEmployeeEmergencies, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeEmergency pee : pmEmployeeEmergencies
        ) {
            if (null != pee.getEmergencyContact()) {
                tempList.add(new FndSuperQueryTemp(pee.getEmergencyContact(), "紧急联系人", "紧急联系电话", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getEmergencyTele()) {
                tempList.add(new FndSuperQueryTemp(pee.getEmergencyTele(), "紧急电话", "紧急联系电话", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pee.getRemarks(), "备注", "紧急联系电话", pee.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeEntryChangeToQueryTemp(List<PmEmployeeEntry> pmEmployeeEntries, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeEntry pee : pmEmployeeEntries
        ) {
            if (null != pee.getArchivesAddress()) {
                tempList.add(new FndSuperQueryTemp(pee.getArchivesAddress(), "档案所在地", "入职情况", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getArchivesUnknown()) {
                tempList.add(new FndSuperQueryTemp(pee.getArchivesUnknown(), "档案不详", "入职情况", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getEntryMode()) {
                tempList.add(new FndSuperQueryTemp(pee.getEntryMode(), "入职录用方式", "入职情况", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getIntroductionSituation()) {
                tempList.add(new FndSuperQueryTemp(pee.getIntroductionSituation(), "介绍信情况", "入职情况", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getIntroductionWages()) {
                tempList.add(new FndSuperQueryTemp(pee.getIntroductionWages(), "介绍信工资", "入职情况", pee.getEmployee().getId(), order++, userId));
            }
            if (null != pee.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pee.getRemarks(), "备注", "入职情况", pee.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeFamilyChangeToQueryTemp(List<PmEmployeeFamily> pmEmployeeFamilies, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeFamily pef : pmEmployeeFamilies
        ) {
            if (null != pef.getCompany()) {
                tempList.add(new FndSuperQueryTemp(pef.getCompany(), "单位", "家庭情况", pef.getEmployee().getId(), order++, userId));
            }
            if (null != pef.getGender()) {
                tempList.add(new FndSuperQueryTemp(pef.getGender(), "性别", "家庭情况", pef.getEmployee().getId(), order++, userId));
            }
            if (null != pef.getMobilePhone()) {
                tempList.add(new FndSuperQueryTemp(pef.getMobilePhone(), "电话号码", "家庭情况", pef.getEmployee().getId(), order++, userId));
            }
            if (null != pef.getName()) {
                tempList.add(new FndSuperQueryTemp(pef.getName(), "姓名", "家庭情况", pef.getEmployee().getId(), order++, userId));
            }
            if (null != pef.getPost()) {
                tempList.add(new FndSuperQueryTemp(pef.getPost(), "职务", "家庭情况", pef.getEmployee().getId(), order++, userId));
            }
            if (null != pef.getRelationship()) {
                tempList.add(new FndSuperQueryTemp(pef.getRelationship(), "与员工关系", "家庭情况", pef.getEmployee().getId(), order++, userId));
            }
            if (null != pef.getTele()) {
                tempList.add(new FndSuperQueryTemp(pef.getTele(), "电话号码", "家庭情况", pef.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeePoliticalChangeToQueryTemp(List<PmEmployeePolitical> pmEmployeePoliticals, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeePolitical pep : pmEmployeePoliticals
        ) {
            if (null != pep.getNature()) {
                tempList.add(new FndSuperQueryTemp(pep.getNature(), "性质", "政治面貌", pep.getEmployee().getId(), order++, userId));
            }
            if (null != pep.getPolitical()) {
                tempList.add(new FndSuperQueryTemp(pep.getPolitical(), "政治面貌", "政治面貌", pep.getEmployee().getId(), order++, userId));
            }
            if (null != pep.getPolitical()) {
                tempList.add(new FndSuperQueryTemp(pep.getPolitical(), "职务", "政治面貌", pep.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeePostotherChangeToQueryTemp(List<PmEmployeePostother> pmEmployeePostothers, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeePostother pep : pmEmployeePostothers
        ) {
            if (null != pep.getCompany()) {
                tempList.add(new FndSuperQueryTemp(pep.getCompany(), "公司名称", "工作外职务", pep.getEmployee().getId(), order++, userId));
            }
            if (null != pep.getPost()) {
                tempList.add(new FndSuperQueryTemp(pep.getPost(), "职务名称", "工作外职务", pep.getEmployee().getId(), order++, userId));
            }
            if (null != pep.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pep.getRemarks(), "备注", "工作外职务", pep.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeHobbyChangeToQueryTemp(List<PmEmployeeHobby> pmEmployeeHobbies, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeHobby peh : pmEmployeeHobbies
        ) {
            if (null != peh.getHobby()) {
                tempList.add(new FndSuperQueryTemp(peh.getHobby(), "技术爱好", "技术爱好", peh.getEmployee().getId(), order++, userId));
            }
            if (null != peh.getLevelMechanism()) {
                tempList.add(new FndSuperQueryTemp(peh.getLevelMechanism(), "认证等级", "技术爱好", peh.getEmployee().getId(), order++, userId));
            }
            if (null != peh.getLevelMyself()) {
                tempList.add(new FndSuperQueryTemp(peh.getLevelMyself(), "自我评价级别", "技术爱好", peh.getEmployee().getId(), order++, userId));
            }
            if (null != peh.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(peh.getRemarks(), "备注", "技术爱好", peh.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeSocialrelationChangeToQueryTemp(List<PmEmployeeSocialrelations> pmEmployeeSocialrelations, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeSocialrelations pes : pmEmployeeSocialrelations
        ) {
            if (null != pes.getCompany()) {
                tempList.add(new FndSuperQueryTemp(pes.getCompany(), "工作单位", "其它关系", pes.getEmployee().getId(), order++, userId));
            }
            if (null != pes.getName()) {
                tempList.add(new FndSuperQueryTemp(pes.getName(), "姓名", "其它关系", pes.getEmployee().getId(), order++, userId));
            }
            if (null != pes.getPost()) {
                tempList.add(new FndSuperQueryTemp(pes.getPost(), "职务", "其它关系", pes.getEmployee().getId(), order++, userId));
            }
            if (null != pes.getRelationship()) {
                tempList.add(new FndSuperQueryTemp(pes.getRelationship(), "与本人关系", "其它关系", pes.getEmployee().getId(), order++, userId));
            }
            if (null != pes.getTele()) {
                tempList.add(new FndSuperQueryTemp(pes.getTele(), "电话", "其它关系", pes.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeTeleChangeToQueryTemp(List<PmEmployeeTele> pmEmployeeTeles, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeTele pet : pmEmployeeTeles
        ) {
            if (null != pet.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pet.getRemarks(), "备注", "个人联系电话", pet.getEmployee().getId(), order++, userId));
            }
            if (null != pet.getTele()) {
                tempList.add(new FndSuperQueryTemp(pet.getTele(), "电话号码", "个人联系电话", pet.getEmployee().getId(), order++, userId));
            }
            if (null != pet.getTeleType()) {
                tempList.add(new FndSuperQueryTemp(pet.getTeleType(), "电话类别", "个人联系电话", pet.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeTitleChangeToQueryTemp(List<PmEmployeeTitle> pmEmployeeTitles, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeTitle pet : pmEmployeeTitles
        ) {
            if (null != pet.getTitleLevel()) {
                tempList.add(new FndSuperQueryTemp(pet.getTitleLevel(), "职称级别", "职称级别", pet.getEmployee().getId(), order++, userId));
            }
            if (null != pet.getTitleName()) {
                tempList.add(new FndSuperQueryTemp(pet.getTitleName(), "职称名称", "职称名称", pet.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeVocationalChangeToQueryTemp(List<PmEmployeeVocational> pmEmployeeVocationals, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeVocational pev : pmEmployeeVocationals
        ) {
            if (null != pev.getEvaluationMechanism()) {
                tempList.add(new FndSuperQueryTemp(pev.getEvaluationMechanism(), "发证机构", "职业资格", pev.getEmployee().getId(), order++, userId));
            }
            if (null != pev.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pev.getRemarks(), "备注", "职业资格", pev.getEmployee().getId(), order++, userId));
            }
            if (null != pev.getVocationalLevel()) {
                tempList.add(new FndSuperQueryTemp(pev.getVocationalLevel(), "资格级别", "职业资格", pev.getEmployee().getId(), order++, userId));
            }
            if (null != pev.getVocationalName()) {
                tempList.add(new FndSuperQueryTemp(pev.getVocationalName(), "资格名称", "职业资格", pev.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

    private List<FndSuperQueryTemp> pmEmployeeWorkhistoryChangeToQueryTemp(List<PmEmployeeWorkhistory> pmEmployeeWorkhistories, Long userId) {
        List<FndSuperQueryTemp> tempList = new ArrayList<>();
        for (PmEmployeeWorkhistory pew : pmEmployeeWorkhistories
        ) {
            if (null != pew.getCompany()) {
                tempList.add(new FndSuperQueryTemp(pew.getCompany(), "公司名称", "工作经历", pew.getEmployee().getId(), order++, userId));
            }
            if (null != pew.getPost()) {
                tempList.add(new FndSuperQueryTemp(pew.getPost(), "岗位职务", "工作经历", pew.getEmployee().getId(), order++, userId));
            }
            if (null != pew.getRemarks()) {
                tempList.add(new FndSuperQueryTemp(pew.getRemarks(), "备注", "工作经历", pew.getEmployee().getId(), order++, userId));
            }
            if (null != pew.getTele()) {
                tempList.add(new FndSuperQueryTemp(pew.getTele(), "联系电话", "工作经历", pew.getEmployee().getId(), order++, userId));
            }
        }
        return tempList;
    }

}
