package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.ac.util.AcEmpDeptUtil;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyDTO;
import com.sunten.hrms.utils.DateUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmConsolationMoney;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyQueryCriteria;
import com.sunten.hrms.swm.service.SwmConsolationMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 慰问金表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-08-04
 */
@RestController
@Api(tags = "慰问金表")
@RequestMapping("/api/swm/consolationMoney")
public class SwmConsolationMoneyController {
    private static final String ENTITY_NAME = "consolationMoney";
    private final SwmConsolationMoneyService swmConsolationMoneyService;
    private final FndDataScope fndDataScope;
    private final FndDeptService fndDeptService;
    private final FndUserService fndUserService;
    private final AcEmpDeptUtil acEmpDeptUtil;
    private final PmEmployeeService pmEmployeeService;
    @Value("${role.consolationCharge}")
    private String consolationCharge;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    public SwmConsolationMoneyController(SwmConsolationMoneyService swmConsolationMoneyService,
                                         FndDataScope fndDataScope, FndDeptService fndDeptService, FndUserService fndUserService,
                                         AcEmpDeptUtil acEmpDeptUtil, PmEmployeeService pmEmployeeService) {
        this.swmConsolationMoneyService = swmConsolationMoneyService;
        this.fndDataScope = fndDataScope;
        this.fndDeptService = fndDeptService;
        this.fndUserService = fndUserService;
        this.acEmpDeptUtil = acEmpDeptUtil;
        this.pmEmployeeService = pmEmployeeService;
    }

    @Log("新增慰问金表")
    @ApiOperation("新增慰问金表")
    @PostMapping
    @PreAuthorize("@el.check('consolationMoney:add')")
    public ResponseEntity create(@Validated @RequestBody SwmConsolationMoney consolationMoney) {
        if (consolationMoney.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmConsolationMoneyService.insert(consolationMoney), HttpStatus.CREATED);
    }

    @Log("删除慰问金表")
    @ApiOperation("删除慰问金表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('consolationMoney:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmConsolationMoneyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该慰问金表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改慰问金表")
    @ApiOperation("修改慰问金表")
    @PutMapping
    @PreAuthorize("@el.check('consolationMoney:edit')")
    public ResponseEntity update(@Validated(SwmConsolationMoney.Update.class) @RequestBody SwmConsolationMoney consolationMoney) {
        swmConsolationMoneyService.update(consolationMoney);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个慰问金表")
    @ApiOperation("获取单个慰问金表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('consolationMoney:list')")
    public ResponseEntity getConsolationMoney(@PathVariable Long id) {
        return new ResponseEntity<>(swmConsolationMoneyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询慰问金表（分页）")
    @ApiOperation("查询慰问金表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('consolationMoney:list')")
    public ResponseEntity getConsolationMoneyPage(SwmConsolationMoneyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) throws NoSuchFieldException, IllegalAccessException {
        setReleasedTimeStartAndEnd(criteria);
        return new ResponseEntity<>(getListByAuthAndPageable(criteria, pageable), HttpStatus.OK);
    }

    public void setReleasedTimeStartAndEnd(SwmConsolationMoneyQueryCriteria criteria) {
        if (criteria.getColName() != null && criteria.getColName().equals("released_time")
        && criteria.getSymbol() != null && criteria.getValue() != null) {
            //自动赋值起始时间结束时间
            LocalDate temp = DateUtil.strToLocalDate(criteria.getValue());
            criteria.setReleasedTimeStart(temp.with(TemporalAdjusters.firstDayOfMonth()));
            criteria.setReleasedTimeEnd(temp.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @ErrorLog("查询慰问金表（不分页）")
    @ApiOperation("查询慰问金表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('consolationMoney:list')")
    public ResponseEntity getConsolationMoneyNoPaging(SwmConsolationMoneyQueryCriteria criteria) throws NoSuchFieldException, IllegalAccessException {
        setReleasedTimeStartAndEnd(criteria);
        return new ResponseEntity<>(getListByAuthAndPageable(criteria, null), HttpStatus.OK);
    }

    @ErrorLog("导出慰问金表数据")
    @ApiOperation("导出慰问金表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('consolationMoney:list')")
    public void download(HttpServletResponse response, SwmConsolationMoneyQueryCriteria criteria) throws IOException, NoSuchFieldException, IllegalAccessException {
        swmConsolationMoneyService.download((List<SwmConsolationMoneyDTO>)getListByAuthAndPageable(criteria, null), response);
    }

    @Log("失效慰问金")
    @ApiOperation("失效慰问金")
    @PutMapping(value = "/disabled/{id}")
    @PreAuthorize("@el.check('consolationMoney:del')")
    public ResponseEntity disabled(@PathVariable Long id) {
        swmConsolationMoneyService.removeConsolationMoney(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("导出慰问金去线下发放")
    @ApiOperation("导出慰问金去线下发放")
    @GetMapping(value = "/exportForApproval")
    @PreAuthorize("@el.check('consolationCharge')")
    public void exportForApproval(HttpServletResponse response, SwmConsolationMoneyQueryCriteria criteria) throws Exception {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == criteria.getIds() || criteria.getIds().size() < 1) {
            throw new InfoCheckWarningMessException("条件内没有需要导出发放的数据");
        } else {
            swmConsolationMoneyService.exportForApproval(swmConsolationMoneyService.listForExportApproval(criteria.getIds()), response, user.getId(), criteria);
        }
    }

    @Log("慰问金单个发放")
    @ApiOperation("导出慰问金去线下发放")
    @PutMapping(value = "/releasedSingleMoney")
    @PreAuthorize("@el.check('consolationCharge')")
    public void releasedSingleMoney(@RequestBody SwmConsolationMoney consolationMoney) {
        if (null == consolationMoney.getReleasedMoney()) {
            throw new InfoCheckWarningMessException("发放金额不能为空");
        }
        if (null == consolationMoney.getReleasedTime()) {
            throw new InfoCheckWarningMessException("发放时间不能为空");
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        consolationMoney.setUpdateBy(user.getId());
        swmConsolationMoneyService.releasedMoney(consolationMoney);
        List<SwmConsolationMoney> swmConsolationMonies = new ArrayList<>();
        swmConsolationMonies.add(consolationMoney);
        swmConsolationMoneyService.sendReleasedEmail(swmConsolationMonies);
    }

    @Log("慰问金单个不发放")
    @ApiOperation("慰问金单个不发放")
    @PutMapping(value = "/notReleasedMoney")
    @PreAuthorize("@el.check('consolationCharge')")
    public void notReleasedMoney(@RequestBody SwmConsolationMoney consolationMoney) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        consolationMoney.setUpdateBy(user.getId());
        swmConsolationMoneyService.notReleasedMoney(consolationMoney);
    }

    @Log("慰问金成批发放")
    @ApiOperation("慰问金成批发放")
    @PutMapping(value = "/batchReleasedMoney")
    @PreAuthorize("@el.check('consolationCharge')")
    public void batchReleasedMoney(@RequestBody List<SwmConsolationMoney> consolationMoneys) {
        if (null == consolationMoneys || consolationMoneys.size() == 0) {
            throw new InfoCheckWarningMessException("请勾选至少一条数据");
        }
        List<BigDecimal> moneyList = consolationMoneys.stream().filter(a -> null == a.getReleasedMoney()).collect(Collectors.toList())
                .stream().map(SwmConsolationMoney::getReleasedMoney).collect(Collectors.toList());
        if (moneyList.size() > 0) {
            throw new InfoCheckWarningMessException("存在发放金额未填写");
        }
        List<LocalDate> dateList = consolationMoneys.stream().filter(a -> null == a.getReleasedTime()).collect(Collectors.toList())
                .stream().map(SwmConsolationMoney::getReleasedTime).collect(Collectors.toList());
        if (dateList.size() > 0) {
            throw new InfoCheckWarningMessException("存在发放时间未填写");
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());

        swmConsolationMoneyService.BatchReleasedMoney(consolationMoneys, user.getId());
    }

    @Log("慰问金成批不发放")
    @ApiOperation("慰问金成批不发放")
    @PutMapping(value = "/batchNotReleasedMoney")
    @PreAuthorize("@el.check('consolationCharge')")
    public void batchNotReleasedMoney(@RequestBody List<SwmConsolationMoney> consolationMoneys) {
        if (null == consolationMoneys || consolationMoneys.size() == 0) {
            throw new InfoCheckWarningMessException("请勾选至少一条数据");
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        for (SwmConsolationMoney swm : consolationMoneys
             ) {
            swm.setUpdateBy(user.getId());
            swmConsolationMoneyService.notReleasedMoney(swm);
        }

    }

    @ErrorLog("根据角色权限获取人员列表")
    @ApiOperation("根据角色权限获取人员列表")
    @GetMapping(value = "/listEmployeeByRole")
    @PreAuthorize("@el.check('consolationMoney:list')")
    public ResponseEntity listEmployeeByRole(PmEmployeeQueryCriteria criteria) {
        Map<String, Object> map = fndDataScope.checkRolePermission(consolationCharge);
        criteria.setLeaveFlag(false);
        criteria.setEnabledFlag(true);
        if ((Boolean)map.get("bool")) {
            return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
        } else {
            Set<Long> docDeptIds = acEmpDeptUtil.getDocAuth((Long)map.get("employeeId"));
            if (docDeptIds.size() > 0) {
                criteria.setDeptIds(docDeptIds);
                return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
            } else {
                criteria.setEmployeeId((Long)map.get("employeeId"));
                return new ResponseEntity<>(pmEmployeeService.listAll(criteria), HttpStatus.OK);
            }
        }
    }

    @ErrorLog("根据资料员管理员以及人事管理范围生成部门查询树")
    @ApiOperation("根据资料员管理员以及人事管理范围生成部门查询树")
    @GetMapping(value = "/buildConsolationTree")
    @PreAuthorize("@el.check('consolationMoney:list')")
    public ResponseEntity buildConsolationTree() {
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        fndDeptQueryCriteria.setEnabled(true);
        fndDeptQueryCriteria.setDeleted(false);
        Map<String, Object> map = fndDataScope.checkRolePermission(consolationCharge);
        if ((Boolean) map.get("bool")) {
            // 慰问金负责人以及管理员返回全部节点
            return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
        } else {
            Set<Long> docDeptIds = acEmpDeptUtil.getDocAuth((Long)map.get("employeeId"));
            Set<Long> targets = new HashSet<>(docDeptIds);
            // 获取管理范围
            FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, null);
            if (!fndDataScope.isNoDataPermission(dataScopeVo)) {
                logger.debug("dataScopeVo" + dataScopeVo.getDeptIds());
                if (dataScopeVo.getDeptIds().size() > 0) {
                    targets.addAll(dataScopeVo.getDeptIds());
                    fndDeptQueryCriteria.setIds(targets);
                    return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
                } else {
                    if (null != map.get("deptId")) {
                        targets.add((Long)map.get("deptId"));
                        fndDeptQueryCriteria.setIds(targets);
                        return new ResponseEntity<>(fndDeptService.buildAcTree(fndDeptService.listAll(fndDeptQueryCriteria)), HttpStatus.OK);
                    } else {
                        throw new InfoCheckWarningMessException("部门树检测不到搜索权限");
                    }
                }
            } else {
                throw new InfoCheckWarningMessException("部门树检测不到搜索权限");
            }
        }
    }

//    @ErrorLog("生成子女幼托通过后，没有子女诞辰的，自动补一条子女诞辰")
//    @ApiOperation("生成子女幼托通过后，没有子女诞辰的，自动补一条子女诞辰")
//    @PostMapping(value = "/autoCreateBornAfterChild")
//    public ResponseEntity autoCreateBornAfterChild(@RequestBody SwmConsolationMoney swmConsolationMoney) {
//        if (null != swmConsolationMoney && null != swmConsolationMoney.getId()) {
//            swmConsolationMoneyService.insertBornAfterChildPass(swmConsolationMoney);
//            return new ResponseEntity(HttpStatus.OK);
//        } else {
//            throw new InfoCheckWarningMessException("自动生成子女诞辰的条件缺失");
//        }
//    }

    @ErrorLog("根据OA单获取慰问金信息")
    @ApiOperation("根据OA单获取慰问金信息")
    @GetMapping(value = "/getSwmConsolationMoneyByOaOrder")
    public ResponseEntity getSwmConsolationMoneyByOaOrder(@RequestParam(value = "oaOrder") String oaOrder) {
        return new ResponseEntity<>(swmConsolationMoneyService.getSwmConsolationMoneyByOaOrder(oaOrder), HttpStatus.OK);
    }

    @Log("撤销慰问金导出状态")
    @ApiOperation("撤销慰问金导出状态")
    @PutMapping(value = "/reBackExport/{id}")
    public ResponseEntity reBackExport(@PathVariable Long id){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        swmConsolationMoneyService.reBackExport(user.getId(), id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("批量撤销慰问金导出状态")
    @ApiOperation("批量撤销慰问金导出状态")
    @PutMapping(value = "/batchReBackExport")
    public ResponseEntity batchReBackExport(@RequestBody List<SwmConsolationMoney> consolationMoneys){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        swmConsolationMoneyService.batchReBackExport(consolationMoneys,user.getId());
        return new ResponseEntity(HttpStatus.OK);
    }


    private Object getListByAuthAndPageable(SwmConsolationMoneyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) throws NoSuchFieldException, IllegalAccessException {
        if (null != criteria.getAdvancedQuerysStr() && !"".equals(criteria.getAdvancedQuerysStr())) {
            JSONArray jsonArray = new JSONArray(criteria.getAdvancedQuerysStr());
            criteria.setAdvancedQuerys(jsonArray.toList(AdvancedQuery.class));
        }
        Map<String, Object> map = fndDataScope.checkRolePermission(consolationCharge);
        criteria.setUserId((Long)map.get("userId"));
        // 管理员
        if ((Boolean)map.get("bool")) {
            criteria.setAdminFlag(true);
            if (null != criteria.getDeptAllId()) {
                criteria.setAdminDeptIds(new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptAllId())));
            }
            if (null == pageable) {
                return swmConsolationMoneyService.listAll(criteria);
            } else {
                return swmConsolationMoneyService.listAll(criteria, pageable);
            }
        } else {
            // 存储页面的搜索条件
            Set<Long> deptIds = new HashSet<>();
            if (null != criteria.getDeptAllId()) {
                deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptAllId()));
            }
            // 资料员范围
            Set<Long> docDeptIds = acEmpDeptUtil.getDocAuth((Long)map.get("employeeId"));
            FndDataScopeVo dataScopeVo = new FndDataScopeVo(null, null, false, false, null, null);
            logger.debug("输出资料员权限范围:" + docDeptIds.toString());
            if (fndDataScope.isNoDataPermission(dataScopeVo)) { // 无权限, 不会进到这里面
                if (docDeptIds.size() > 0) {
                    if (deptIds.size() > 0) {
                        // 取交集
                        criteria.setDeptIds(docDeptIds.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                    } else {
                        criteria.setDeptIds(docDeptIds);
                    }
                    criteria.setEmployeeId(dataScopeVo.getEmployeeId());
                    if (null == pageable) {
                        return swmConsolationMoneyService.listAll(criteria);
                    } else {
                        return swmConsolationMoneyService.listAll(criteria, pageable);
                    }
                } else {
                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
                }
            } else {
                criteria.setEmployeeId(fndUserService.getByName(SecurityUtils.getUsername()).getEmployee().getId());
                if (null != dataScopeVo.getDeptIds() && dataScopeVo.getDeptIds().size() > 0) {
                    if (docDeptIds.size() > 0) {
                        dataScopeVo.getDeptIds().addAll(docDeptIds);
                    }
                    if (deptIds.size() > 0) {
                        //取交集
                        criteria.setDeptIds(dataScopeVo.getDeptIds().stream().filter(deptIds::contains).collect(Collectors.toSet()));
                    } else {
                        criteria.setDeptIds(dataScopeVo.getDeptIds());
                    }
                } else {
                    // 个人判定
                    if (docDeptIds.size() > 0) {
                        if (deptIds.size() > 0) {
                            //取交集
                            criteria.setDeptIds(docDeptIds.stream().filter(deptIds::contains).collect(Collectors.toSet()));
                        } else {
                            criteria.setDeptIds(docDeptIds);
                        }
                    }
                }
                if (null == pageable) {
                    return swmConsolationMoneyService.listAll(criteria);
                } else {
                    return swmConsolationMoneyService.listAll(criteria, pageable);
                }
            }
        }
    }
}
