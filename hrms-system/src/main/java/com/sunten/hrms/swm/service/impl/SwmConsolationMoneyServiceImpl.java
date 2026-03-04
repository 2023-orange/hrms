package com.sunten.hrms.swm.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.sunten.hrms.ac.dao.AcEmpDeptsDao;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.ac.service.impl.AcEmployeeAttendanceServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndAttachedDocumentDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndAttachedDocument;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.swm.domain.SwmConsolationCheck;
import com.sunten.hrms.swm.domain.SwmConsolationMoney;
import com.sunten.hrms.swm.dao.SwmConsolationMoneyDao;
import com.sunten.hrms.swm.service.SwmConsolationMoneyService;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyDTO;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyQueryCriteria;
import com.sunten.hrms.swm.mapper.SwmConsolationMoneyMapper;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 慰问金表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SwmConsolationMoneyServiceImpl extends ServiceImpl<SwmConsolationMoneyDao, SwmConsolationMoney> implements SwmConsolationMoneyService {
    @Value("${role.authDocumenter}")
    private String authDocumenter;
    @Value("${role.consolationCharge}")
    private String consolationCharge;
    private final SwmConsolationMoneyDao swmConsolationMoneyDao;
    private final SwmConsolationMoneyMapper swmConsolationMoneyMapper;
    private final AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl;
    private final ToolEmailInterfaceService toolEmailInterfaceService;
    private final FndUserDao fndUserDao;
    private final FndAttachedDocumentDao fndAttachedDocumentDao;
    private final AcEmpDeptsDao acEmpDeptsDao;
    public SwmConsolationMoneyServiceImpl(SwmConsolationMoneyDao swmConsolationMoneyDao, SwmConsolationMoneyMapper swmConsolationMoneyMapper,
                                          AcEmployeeAttendanceServiceImpl acEmployeeAttendanceServiceImpl, FndUserDao fndUserDao,
                                          ToolEmailInterfaceService toolEmailInterfaceService, FndAttachedDocumentDao fndAttachedDocumentDao,
                                          AcEmpDeptsDao acEmpDeptsDao) {
        this.swmConsolationMoneyDao = swmConsolationMoneyDao;
        this.swmConsolationMoneyMapper = swmConsolationMoneyMapper;
        this.acEmployeeAttendanceServiceImpl = acEmployeeAttendanceServiceImpl;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
        this.fndUserDao = fndUserDao;
        this.fndAttachedDocumentDao = fndAttachedDocumentDao;
        this.acEmpDeptsDao = acEmpDeptsDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SwmConsolationMoneyDTO insert(SwmConsolationMoney consolationMoneyNew) {
        // 先判定是否允许增加
        SwmConsolationCheck swmConsolationCheck = swmConsolationMoneyDao.checkBornBeforeInsert(consolationMoneyNew.getEmployeeId(),
                consolationMoneyNew.getDate(),consolationMoneyNew.getChildName());
        if (((consolationMoneyNew.getDate().isAfter(LocalDate.now().minusYears(1)) && consolationMoneyNew.getDate().isBefore(LocalDate.now()))
        || consolationMoneyNew.getDate().equals(LocalDate.now().minusYears(1)) || consolationMoneyNew.getDate().equals(LocalDate.now())) && !consolationMoneyNew.getConsolationMoneyType().equals("子女幼托费")
        ) {
            switch (consolationMoneyNew.getConsolationMoneyType()) {
                case "子女诞辰":
                    if (swmConsolationCheck.getHaveBornFlag()) {
                        throw new InfoCheckWarningMessException("每个孩子只能申请一次子女诞辰慰问金，不允许重复申请");
                    } else {
                        if (swmConsolationCheck.getHaveHalfBornFlag()) {
                            throw new InfoCheckWarningMessException("每个孩子只能申请一次子女诞辰慰问金，不允许重复申请");
                        }
                    }
                    break;
                case "结婚":
                    if (swmConsolationCheck.getHaveMarriedFlag()) {
                        throw new InfoCheckWarningMessException("检测到员工曾提交过结婚慰问金领取申请，不允许重复申请");
                    } else {
                        if (swmConsolationCheck.getHaveHalfMarriedFlag()) {
                            throw new InfoCheckWarningMessException("检测到员工的伴侣曾提交过结婚慰问金领取申请，不允许重复申请");
                        }
                    }
                    break;
                case "丧事":
                    if (swmConsolationCheck.getHaveDeadFlag()) {
                        throw new InfoCheckWarningMessException("检测到员工曾提交过丧事慰问金领取申请，不需要重复申请");
                    }
                    break;
                default:
                    throw new InfoCheckWarningMessException("无效的费用类别");
            }

            // 都OK
            if (consolationMoneyNew.getConsolationMoneyType().equals("子女诞辰")
                    && null != consolationMoneyNew.getDate()
            ) {
                consolationMoneyNew.setPayStartDate(consolationMoneyNew.getDate().plusYears(2));
                consolationMoneyNew.setPayEndDate(consolationMoneyNew.getDate().plusYears(13));
            }
            swmConsolationMoneyDao.insertAllColumn(consolationMoneyNew);
            return swmConsolationMoneyMapper.toDto(consolationMoneyNew);
        } else {
            if (consolationMoneyNew.getConsolationMoneyType().equals("子女幼托费")) {
                if (swmConsolationCheck.getHaveBornFlag()) {
                    throw new InfoCheckWarningMessException("检测到系统中已存在子女诞辰，系统将每年自动生成子女幼托费，无需自行申请");
                } else {
                    if (swmConsolationCheck.getHaveHalfBornFlag()) {
                        throw new InfoCheckWarningMessException("检测到配偶曾完成子女诞辰的申请，系统将每年自动生成子女幼托费，无需自行申请");
                    } else {
                        if (swmConsolationCheck.getHaveChildFlag()) {
                            throw new InfoCheckWarningMessException("检测到系统中已存在子女幼托费，不需要重复申请");
                        } else {
                            if (swmConsolationCheck.getHaveHalfChildFlag()) {
                                throw new InfoCheckWarningMessException("检测到员工伴侣已办理子女幼托费，不需要重复申请");
                            }
                        }
                    }
                }
                // 都通过后
                swmConsolationMoneyDao.insertAllColumn(consolationMoneyNew);
                return swmConsolationMoneyMapper.toDto(consolationMoneyNew);
            } else {
                if (consolationMoneyNew.getDate().isAfter(LocalDate.now())) {
                    switch (consolationMoneyNew.getConsolationMoneyType()) {
                        case "子女诞辰": {
                            throw new InfoCheckWarningMessException("出生日期不能晚于今日");
                        }
                        case "结婚": {
                            throw new InfoCheckWarningMessException("结婚日期不能晚于今日");
                        }
                        case "丧事": {
                            throw new InfoCheckWarningMessException("死亡日期不符合要求");
                        }
                        default:
                            throw new InfoCheckWarningMessException("无效的费用类别");
                    }
                } else {
                    throw new InfoCheckWarningMessException("仅在事发一年内申请有效");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SwmConsolationMoney consolationMoney = new SwmConsolationMoney();
        consolationMoney.setId(id);
        this.delete(consolationMoney);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SwmConsolationMoney consolationMoney) {
        // TODO    确认删除前是否需要做检查
        // 删除时，检查是否有附件，如果有，则同时删除
        FndAttachedDocumentQueryCriteria criteria = new FndAttachedDocumentQueryCriteria();
        criteria.setSource("swm");
        criteria.setEnabledFlag(true);
        criteria.setSourceId(consolationMoney.getId());
        criteria.setType("consolationMoney");
        List<FndAttachedDocument> files = fndAttachedDocumentDao.listAllByCriteria(criteria);
        if (files.size() > 0) {
            files.forEach(file -> {
                fndAttachedDocumentDao.deleteByKey(file.getId());
            });
        }
        swmConsolationMoneyDao.deleteByEntityKey(consolationMoney);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SwmConsolationMoney consolationMoneyNew) {
        SwmConsolationMoney consolationMoneyInDb = Optional.ofNullable(swmConsolationMoneyDao.getByKey(consolationMoneyNew.getId())).orElseGet(SwmConsolationMoney::new);
        ValidationUtil.isNull(consolationMoneyInDb.getId() ,"ConsolationMoney", "id", consolationMoneyNew.getId());
        consolationMoneyNew.setId(consolationMoneyInDb.getId());
        swmConsolationMoneyDao.updateAllColumnByKey(consolationMoneyNew);
    }

    @Override
    public SwmConsolationMoneyDTO getByKey(Long id) {
        SwmConsolationMoney consolationMoney = Optional.ofNullable(swmConsolationMoneyDao.getByKey(id)).orElseGet(SwmConsolationMoney::new);
        ValidationUtil.isNull(consolationMoney.getId() ,"ConsolationMoney", "id", id);
        return swmConsolationMoneyMapper.toDto(consolationMoney);
    }

    @Override
    public List<SwmConsolationMoneyDTO> listAll(SwmConsolationMoneyQueryCriteria criteria) {
        return swmConsolationMoneyMapper.toDto(swmConsolationMoneyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(SwmConsolationMoneyQueryCriteria criteria, Pageable pageable) {
        Page<SwmConsolationMoney> page = PageUtil.startPage(pageable);
        List<SwmConsolationMoney> consolationMoneys = swmConsolationMoneyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(swmConsolationMoneyMapper.toDto(consolationMoneys), page.getTotal());
    }

    @Override
    public void download(List<SwmConsolationMoneyDTO> consolationMoneyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (SwmConsolationMoneyDTO consolationMoneyDTO : consolationMoneyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("状态", consolationMoneyDTO.getStatus());
            map.put("申请人", consolationMoneyDTO.getName());
            map.put("工号", consolationMoneyDTO.getWorkCard());
            map.put("部门", null != consolationMoneyDTO.getDeptName() ? consolationMoneyDTO.getDeptName() : "");
            map.put("科室", null != consolationMoneyDTO.getDepartmentName() ? consolationMoneyDTO.getDepartmentName() : "");
            map.put("班组", null != consolationMoneyDTO.getTeamName() ? consolationMoneyDTO.getTeamName() : "");
            map.put("申请类型", null != consolationMoneyDTO.getConsolationMoneyType() ? consolationMoneyDTO.getConsolationMoneyType() : "");
            map.put("申请金额", null != consolationMoneyDTO.getApplicationMoney() ? consolationMoneyDTO.getApplicationMoney() : "");
            map.put("发放日期", null != consolationMoneyDTO.getReleasedTime() ? consolationMoneyDTO.getReleasedTime().format(timeFormatter) : "");
            map.put("发放金额", null != consolationMoneyDTO.getReleasedMoney() ? consolationMoneyDTO.getReleasedMoney() : "");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeConsolationMoney(Long id) {
        swmConsolationMoneyDao.deleteByEnabled(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportForApproval(List<SwmConsolationMoneyDTO> consolationMoneyDTOS, HttpServletResponse response, Long userId, SwmConsolationMoneyQueryCriteria swmConsolationMoneyQueryCriteria
    ) throws IOException {
        // 更新所有被导出的慰问金的导出标记
        if (null != swmConsolationMoneyQueryCriteria.getIds() && swmConsolationMoneyQueryCriteria.getIds().size() > 0) {
            swmConsolationMoneyDao.updateExportFlagByCriteria(userId, swmConsolationMoneyQueryCriteria);
        }
        // 导出
        List<Map<String, Object>> list = new ArrayList<>();
        String releaseTime = swmConsolationMoneyQueryCriteria.getReleasedTime().getMonthValue() + "月";
        for (SwmConsolationMoneyDTO consolationMoneyDTO : consolationMoneyDTOS
             ) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("姓名", consolationMoneyDTO.getName());
            map.put("工号", consolationMoneyDTO.getWorkCard());
            map.put("部门", consolationMoneyDTO.getDeptName());
            map.put("科室", consolationMoneyDTO.getDepartmentName());
            map.put("班组", consolationMoneyDTO.getTeamName());
            map.put("申请原因", consolationMoneyDTO.getConsolationMoneyType());
            map.put("发放时间", (null != swmConsolationMoneyQueryCriteria.getIds() && swmConsolationMoneyQueryCriteria.getIds().size() > 0) ? releaseTime : consolationMoneyDTO.getReleasedTime().getMonthValue() + "月");
            map.put("金额", consolationMoneyDTO.getApplicationMoney());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importInterfaceToMain(Long groupId) {
        swmConsolationMoneyDao.oldestInterfaceToMain(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releasedInterfaceToMain(Long groupId) {
        swmConsolationMoneyDao.releasedInterfaceToMain(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releasedMoney(SwmConsolationMoney swmConsolationMoney) {
        swmConsolationMoneyDao.releasedMoney(swmConsolationMoney);
        swmConsolationMoney.setReleasedTimeStr(DateUtil.localDateToStr(swmConsolationMoney.getReleasedTime()));
//        if (null != swmConsolationMoney.getConsolationMoneyType() && swmConsolationMoney.getConsolationMoneyType().equals("子女幼托费")) {
//            this.insertBornAfterChildPass(swmConsolationMoney);
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendReleasedEmail(List<SwmConsolationMoney> swmConsolationMoneys) {
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        acEmployeeAttendanceServiceImpl.getServer(toolEmailInterface);
        // 批量获取慰问金
        List<SwmConsolationMoney> swmConsolationMonies = swmConsolationMoneyDao.getListForSendEmail(swmConsolationMoneys.stream().map(SwmConsolationMoney::getId).collect(Collectors.toSet()));
        // 批量获取资料员
        AcEmpDeptsQueryCriteria acEmpDeptsQueryCriteria = new AcEmpDeptsQueryCriteria();
        acEmpDeptsQueryCriteria.setEnabledFlag(true);
        acEmpDeptsQueryCriteria.setRoleId(17L);
        acEmpDeptsQueryCriteria.setDataType(authDocumenter);
        List<AcEmpDepts> acEmpDeptsList = acEmpDeptsDao.listAllByGroup(acEmpDeptsQueryCriteria);
        List<SwmConsolationMoney> noDocList = new ArrayList<>();
        boolean flag = false;
        for (SwmConsolationMoney swm : swmConsolationMonies
             ) {
            swm.setReleasedTimeStr(DateUtil.localDateToStr(swm.getReleasedTime()));
            for (AcEmpDepts ac : acEmpDeptsList
            ) {
                if (ac.getDeptList().contains(swm.getDeptId())) {
                    if (null == ac.getSwmConsolationMonies()) {
                        List<SwmConsolationMoney> swmConsolationMoneyList = new ArrayList<>();
                        swmConsolationMoneyList.add(swm);
                        ac.setSwmConsolationMonies(swmConsolationMoneyList);
                    } else {
                        ac.getSwmConsolationMonies().add(swm);
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                noDocList.add(swm);
            }
            flag = false;
        }
        List<SwmConsolationMoney> noEmailList = new ArrayList<>();

        for (AcEmpDepts ac : acEmpDeptsList
             ) {
            if (null != ac.getSwmConsolationMonies() && ac.getSwmConsolationMonies().size() > 0) {
                if (null != ac.getEmail() && !ac.getEmail().equals("@in-sunten.com")) {
                    toolEmailInterface.setStatus("PLAN");
                    toolEmailInterface.setSendTo(ac.getEmail());
                    toolEmailInterface.setPlannedDate(LocalDateTime.now());
                    toolEmailInterface.setMailSubject("慰问金发放通知,请资料员知照相关员工");
                    Dict dict = Dict.create();
                    dict.set("swmConsolationMoneyList", ac.getSwmConsolationMonies());
                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
                    Template template = engine.getTemplate("email/swm/swmConsolationMoney.ftl");
                    toolEmailInterface.setMailContent(template.render(dict));
                    toolEmailInterfaceService.insert(toolEmailInterface);
                } else {
                    for (SwmConsolationMoney swmConsolationMoney : ac.getSwmConsolationMonies()
                         ) {
                        if (null != swmConsolationMoney.getEmail() && !swmConsolationMoney.getEmail().equals("@in-sunten.com")) {
                            sendPersonEmail(toolEmailInterface, swmConsolationMoney);
                        } else {
                            noEmailList.add(swmConsolationMoney);
                        }
                    }
                }
            }
        }
        for (SwmConsolationMoney swmConsolationMoney : noDocList
             ) {
            if (null != swmConsolationMoney.getEmail() && !swmConsolationMoney.getEmail().equals("@in-sunten.com")) {
                sendPersonEmail(toolEmailInterface, swmConsolationMoney);
            } else {
                noEmailList.add(swmConsolationMoney);
            }
        }
        List<String> consolationCharges = fndUserDao.selectEmailListByRole(consolationCharge);
        if (consolationCharges.size() > 0 && noEmailList.size() > 0) {
            for (String email : consolationCharges
            ) {
                toolEmailInterface.setStatus("PLAN");
                toolEmailInterface.setSendTo(email);
                toolEmailInterface.setPlannedDate(LocalDateTime.now());
                toolEmailInterface.setMailSubject("慰问金发放通知,请知照以下员工（系统无法通知到位）");
                Dict dict = Dict.create();
                dict.set("swmConsolationMoneyList", noEmailList);
                TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
                Template template = engine.getTemplate("email/swm/swmConsolationMoney.ftl");
                toolEmailInterface.setMailContent(template.render(dict));
                toolEmailInterfaceService.insert(toolEmailInterface);
            }
        }
    }

    public void sendPersonEmail(ToolEmailInterface toolEmailInterface, SwmConsolationMoney swmConsolationMoney) {
        toolEmailInterface.setStatus("PLAN");
        toolEmailInterface.setSendTo(swmConsolationMoney.getEmail());
        toolEmailInterface.setPlannedDate(LocalDateTime.now());
        toolEmailInterface.setMailSubject("慰问金发放通知");
        Dict dict = Dict.create();
        List<SwmConsolationMoney> swmConsolationMonies = new ArrayList<>();
        swmConsolationMonies.add(swmConsolationMoney);
        dict.set("swmConsolationMoneyList", swmConsolationMonies);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/swm/swmConsolationMoney.ftl");
        toolEmailInterface.setMailContent(template.render(dict));
        toolEmailInterfaceService.insert(toolEmailInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void BatchReleasedMoney(List<SwmConsolationMoney> swmConsolationMoney, Long updateBy) {
        for (SwmConsolationMoney swm : swmConsolationMoney
        ) {
            swm.setUpdateBy(updateBy);
            releasedMoney(swm);
        }

        sendReleasedEmail(swmConsolationMoney);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notReleasedMoney(SwmConsolationMoney swmConsolationMoney) {
        swmConsolationMoneyDao.notReleasedMoney(swmConsolationMoney);
    }

//    @Override
//    public Boolean checkHaveBornAfterChildPass(Long id) {
//        return swmConsolationMoneyDao.checkHaveBornAfterChildPass(id);
//    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void insertBornAfterChildPass(SwmConsolationMoney swmConsolationMoney){
//        if (swmConsolationMoneyDao.checkHaveBornAfterChildPass(swmConsolationMoney.getId())) {
//            // 插入一条子女诞辰
//            SwmConsolationMoney swmConsolationMoneyBorn = new SwmConsolationMoney();
//            swmConsolationMoneyBorn.setUpdateBy(-1L);
//            swmConsolationMoneyBorn.setCreateBy(-1L);
//            swmConsolationMoneyBorn.setApplicationDate(LocalDate.now());
//            swmConsolationMoneyBorn.setApplicationMoney(new BigDecimal(300));
//            swmConsolationMoneyBorn.setConsolationMoneyType("子女诞辰");
//            swmConsolationMoneyBorn.setStatus("等待导出");
//            swmConsolationMoneyBorn.setExportFlag(false);
//            swmConsolationMoneyBorn.setReleasedFlag(false);
//            swmConsolationMoneyBorn.setEnabledFlag(true);
//            if (null != swmConsolationMoney.getDate()) {
//                swmConsolationMoneyBorn.setDate(swmConsolationMoney.getDate());
//            }
//            swmConsolationMoneyDao.insertAllColumn(swmConsolationMoneyBorn);
//        }
//    }

    @Override
    public SwmConsolationMoneyDTO getSwmConsolationMoneyByOaOrder(String oaOrder) {
        return swmConsolationMoneyMapper.toDto(swmConsolationMoneyDao.getSwmConsolationMoneyByOaOrder(oaOrder));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reBackExport(Long updateBy, Long id) {
        swmConsolationMoneyDao.reBackExport(updateBy, id);
    }

    @Override
    public void batchReBackExport(List<SwmConsolationMoney> swmConsolationMoneys, Long updateBy) {
        if (swmConsolationMoneys.size() > 0) {
            List<Long> ids = new ArrayList<>();
            for(SwmConsolationMoney swmConsolationMoney : swmConsolationMoneys) {
                ids.add(swmConsolationMoney.getId());
            }
            swmConsolationMoneyDao.batchReBackExport(updateBy,ids);
        } else {
            throw new InfoCheckWarningMessException("没有需要取消导出的行");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDateByChild(Long groupId) {
        swmConsolationMoneyDao.updateDateByChild(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoCreateChildConsolation() {
        // 通过子女幼托生成子女幼托
        swmConsolationMoneyDao.autoCreateChildByChild();
        // 根据诞辰生成第一条幼托， 已存在幼托则不生成
        swmConsolationMoneyDao.autoCreateFirstChildByBorn();
    }

    @Override
    public List<SwmConsolationMoneyDTO> listForExportApproval(List<Long> ids) {
        return swmConsolationMoneyMapper.toDto(swmConsolationMoneyDao.listForExportApproval(ids));
    }
}
