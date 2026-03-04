package com.sunten.hrms.re.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.re.domain.ReDemand;
import com.sunten.hrms.re.domain.ReDemandJob;
import com.sunten.hrms.re.domain.ReDemandJobDescribes;
import com.sunten.hrms.re.dto.ReDemandDTO;
import com.sunten.hrms.re.dto.ReDemandJobDTO;
import com.sunten.hrms.re.dto.ReDemandQueryCriteria;
import com.sunten.hrms.re.service.ReDemandJobDescribesService;
import com.sunten.hrms.re.service.ReDemandJobService;
import com.sunten.hrms.re.service.ReDemandService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用人需求表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-04-22
 */
@RestController
@Api(tags = "用人需求表")
@RequestMapping("/api/re/demand")
public class ReDemandController {
    private static final String ENTITY_NAME = "demand";
    private final ReDemandService reDemandService;
    private final ReDemandJobService reDemandJobService;
    private final FndDeptService fndDeptService;
    private final ReDemandJobDescribesService reDemandJobDescribesService;
    private final FndUserService fndUserService;
    private final JwtPermissionService jwtPermissionService;
    private final FndDataScope fndDataScope;
    @Value("${role.authReCharge}")
    private String authReCharge;
    public ReDemandController(ReDemandService reDemandService,
                              ReDemandJobService reDemandJobService, FndDeptService fndDeptService,
                              ReDemandJobDescribesService reDemandJobDescribesService, FndUserService fndUserService,
                              JwtPermissionService jwtPermissionService, FndDataScope fndDataScope) {
        this.reDemandJobService = reDemandJobService;
        this.reDemandService = reDemandService;
        this.fndDeptService = fndDeptService;
        this.reDemandJobDescribesService = reDemandJobDescribesService;
        this.fndUserService = fndUserService;
        this.jwtPermissionService = jwtPermissionService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增用人需求表")
    @ApiOperation("新增用人需求表")
    @PostMapping
    @PreAuthorize("@el.check('demand:add')")
    public ResponseEntity create(@Validated @RequestBody ReDemand demand) {
        if (demand.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(reDemandService.insert(demand), HttpStatus.CREATED);
    }

    @Log("删除用人需求表")
    @ApiOperation("删除用人需求表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('demand:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reDemandService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该用人需求表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改用人需求表")
    @ApiOperation("修改用人需求表")
    @PutMapping
    @PreAuthorize("@el.check('demand:edit')")
    public ResponseEntity update(@Validated(ReDemand.Update.class) @RequestBody ReDemand demand) {
        reDemandService.update(demand);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个用人需求表")
    @ApiOperation("获取单个用人需求表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemand(@PathVariable Long id) {
        return new ResponseEntity<>(reDemandService.getByKey(id), HttpStatus.OK);
    }

    @Log("失效用人需求")
    @ApiOperation("失效用人需求")
    @PutMapping(value = "/disabled/{id}")
    @PreAuthorize("@el.check('demand:edit')")
    public ResponseEntity disabled(@PathVariable Long id) {
        reDemandService.disabledByEnabled(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("撤回用人需求")
    @ApiOperation("撤回用人需求")
    @PutMapping(value = "/repeal")
    @PreAuthorize("@el.check('demand:edit')")
    public ResponseEntity repeal(@RequestBody ReDemand demand) {
        reDemandService.repealDemand(demand.getId(),demand.getRepealReason(), demand.getOaOrder());
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("查询用人需求表（分页）")
    @ApiOperation("查询用人需求表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemandPage(ReDemandQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(getListByAuthAndPageable(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询用人需求表（不分页）")
    @ApiOperation("查询用人需求表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemandNoPaging(ReDemandQueryCriteria criteria) {
        return new ResponseEntity<>(getListByAuthAndPageable(criteria, null), HttpStatus.OK);
    }

    @ErrorLog("当前需求情况列表")
    @ApiOperation("当前需求情况列表")
    @GetMapping(value = "/getCurrentStatusList")
    public ResponseEntity getCurrentStatusList(Long deptId,String demandCode) {
        if(null == deptId ){
            throw new BadRequestException("该OA单号找不到对应部门ID");
        }else{
            return new ResponseEntity<>(reDemandService.getCurrentStatusList(deptId,demandCode), HttpStatus.OK);
        }
    }

    @ErrorLog("导出用人需求表数据")
    @ApiOperation("导出用人需求表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('demand:list')")
    public void download(HttpServletResponse response, ReDemandQueryCriteria criteria) throws IOException {
        reDemandService.download((List<ReDemandDTO>)getListByAuthAndPageable(criteria, null), response);
    }

    @ErrorLog("获取最新一条用人需求，并返回id")
    @ApiOperation("获取最新一条用人需求，并返回id")
    @GetMapping(value = "/getLast/{type}")
    @PreAuthorize("@el.check('demand:list', 'transferRequest:add')")
    public ResponseEntity getLast(@PathVariable String type) {
        ReDemandDTO reDemandDTO = reDemandService.getLastRemand(type);
        return new ResponseEntity<>(this.numberChange(null == reDemandDTO || null == reDemandDTO.getNumber() ? 0 : reDemandDTO.getNumber(), type), HttpStatus.OK);
    }

    @Log("新增用人需求,并新增岗位信息")
    @ApiOperation("新增用人需求,并新增岗位信息")
    @PostMapping(value = "/addDemand")
    @PreAuthorize("@el.check('demand:add')")
    public ResponseEntity addDemand(@Validated @RequestBody ReDemand demand) {
        if (demand.getId()  != -1L) {
            throw new BadRequestException("该用户需求新增属于非法新增");
        } else {
            // 检查编号是否存在, 虽然还是有可能重复,但毕竟使用人群少,重复概率几乎0
            ReDemandDTO reDemandCheck = reDemandService.getLastRemand(demand.getDemandClass());
            if (null != reDemandCheck) {
                if (demand.getDemandCode().startsWith("L")) {
                    if (Long.parseLong(reDemandCheck.getDemandCode().substring(1)) >= Long.parseLong(demand.getDemandCode().substring(1))) {
                        demand.setDemandCode("L" + (Long.parseLong(reDemandCheck.getDemandCode().substring(1)) + 1));
                    }
                } else {
                    if (Long.parseLong(reDemandCheck.getDemandCode()) >= Long.parseLong(demand.getDemandCode())) {
                        demand.setDemandCode(Long.parseLong(reDemandCheck.getDemandCode()) + 1 + "");
                    }
                }
                demand.setNumber(reDemandCheck.getNumber() + 1);
            } else {
                demand.setNumber(1L);
            }
            demand.setAfterCompleteEditFlag(false);

            //同意人数的初始值  等于   总需求人数之和
            if (null != demand.getDemandJobList() && demand.getDemandJobList().size() > 0){
                int agreeNum=0;
                for (ReDemandJob demandJob : demand.getDemandJobList()){
                    agreeNum= agreeNum + demandJob.getQuantity();
                }
                demand.setAgreeNum(agreeNum);
            }

            ReDemandDTO reDemandDTO = reDemandService.insert(demand);
            if (null != demand.getDemandJobList() && demand.getDemandJobList().size() > 0) {
                for (ReDemandJob demandJob : demand.getDemandJobList()
                     ) {
                    demandJob.setDemandId(reDemandDTO.getId());
                    ReDemandJobDTO reDemandJobDTO = reDemandJobService.insert(demandJob);
                    if (null != demandJob.getDemandJobDescribesList() && demandJob.getDemandJobDescribesList().size() > 0) {
                        for (ReDemandJobDescribes reDemandJobDes: demandJob.getDemandJobDescribesList()
                             ) {
                            reDemandJobDes.setDemandJobId(reDemandJobDTO.getId());
                            reDemandJobDescribesService.insert(reDemandJobDes);
                        }
                    }
                }
            }
            ReDemandQueryCriteria reDemandQueryCriteria = new ReDemandQueryCriteria();
            reDemandQueryCriteria.setId(reDemandDTO.getId());
            return new ResponseEntity<>(reDemandService.getDemandAndDemandJobWithOrder(reDemandQueryCriteria),HttpStatus.OK);
        }
    }

    @Log("更新OA单号及审批状态")
    @ApiOperation("更新OA单号")
    @PutMapping(value = "/updateOaReq")
    public ResponseEntity updateOaReq(@RequestBody ReDemand demand) {
        if (null == demand.getId() || demand.getId() < 0) {
            throw new BadRequestException("非法用人需求");
        } else {
            reDemandService.updateOaReqAndCurrentNode(demand);
            return new ResponseEntity<>(reDemandService.getByKey(demand.getId()),HttpStatus.OK);
        }
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/4/26 11:23
     *  @Description: 调用情况
     *  以OA单号作为更新依据
     *  0、不通过，currentNode 哪个节点不通过， 更新realDemandStatus为‘notPass’, 更新审批日期approvalDate
     *  1、招聘专员节点，填写往期人数（pastQuantity）及当前需求情况（recruitmentSource）、更新当前审批节点(currentNode)
     *  2、更新当前审批节点(currentNode)
     *  3、审批完成 更新realDemandStatus为‘complete’， 更新approvalDate 审批日期
     */
    @Log("更新往期人数及当前需求情况，OA使用")
    @ApiOperation("更新往期人数及当前需求情况，OA使用")
    @PostMapping(value = "/updateValue")
    public ResponseEntity updateValue(@RequestBody ReDemand demand) {
        if (null == demand.getOaOrder()) {
            throw new BadRequestException("请传输OA单号进行数据更新");
        } else {
            reDemandService.updateDemandByValue(demand);
            return new ResponseEntity<>("200",HttpStatus.OK);
        }
    }

    @ErrorLog("更新往期人数及当前需求情况，OA使用")
    @ApiOperation("更新往期人数及当前需求情况，OA使用")
    @GetMapping(value = "/getDemandAndDemandJobWithOrder")
    public ResponseEntity getDemandAndDemandJobWithOrder(String oaOrder, Long demandId) {
        if ((null == oaOrder || "".equals(oaOrder)) && null == demandId) {
            throw new BadRequestException("该OA单号找不到对应用人需求信息");
        } else {
            ReDemandQueryCriteria reDemandQueryCriteria = new ReDemandQueryCriteria();
            if (null != oaOrder) {
                reDemandQueryCriteria.setOaOrder(oaOrder);
                return new ResponseEntity<>(reDemandService.getDemandAndDemandJobWithOrder(reDemandQueryCriteria),HttpStatus.OK);
            }
            reDemandQueryCriteria.setId(demandId);
            return new ResponseEntity<>(reDemandService.getDemandAndDemandJobWithOrder(reDemandQueryCriteria),HttpStatus.OK);
        }
    }

    @ErrorLog("每次有绑定过用人需求单的审批申请审批通过时，更新用人需求状态描述")
    @ApiOperation("每次有绑定过用人需求单的审批申请审批通过时，更新用人需求状态描述")
    @PutMapping(value= "/checkDemandJobIsAllPassByDemandId/{id}")
    public void checkDemandJobIsAllPassByDemandId(@PathVariable Long id) {
        reDemandService.checkDemandJobIsAllPassByDemandId(id);
    }

    private Object getListByAuthAndPageable(ReDemandQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (null != criteria.getDeptId()) {
            Set<Long> deptIds = new HashSet<>(fndDeptService.listAllEnableChildrenIdByPid(criteria.getDeptId()));
            criteria.setDeptIds(deptIds);
            criteria.setDeptId(null);
        }
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        Collection<GrantedAuthority> grantedAuthorityCollection = jwtPermissionService.mapToGrantedAuthorities(user);
        for (GrantedAuthority auth: grantedAuthorityCollection
        ) {
            if (auth.getAuthority().equals(authReCharge)) {
                // 直接返回
                if (null == pageable) {
                    return reDemandService.listAll(criteria);
                } else {
                    return reDemandService.listAll(criteria, pageable);
                }
            }
        }
        if (null == user.getEmployee()) { // 已忽略admin
            throw new InfoCheckWarningMessException("检测到该用户未绑定员工，请联系管理员进行员工绑定");
        } else {
            criteria.setDemandBy(user.getEmployee().getId());
            if (null == pageable) {
                return reDemandService.listAll(criteria);
            } else {
                return reDemandService.listAll(criteria, pageable);
            }
        }
    }

    private String numberChange(long number, String type) {
        String numberChange;
        if (number == 0) {
            number = 1;
            numberChange = LocalDate.now().getYear() + String.format(" %03d", number).trim();
        } else {
            numberChange = LocalDate.now().getYear() + String.format(" %03d", number + 1).trim();
        }
        if (!type.equals("正式工")) {
            numberChange = "L" + numberChange;
        }
        return numberChange;
    }

    @ErrorLog("根据需求单号获取单个用人需求表")
    @ApiOperation("根据需求单号获取单个用人需求表")
    @GetMapping(value = "/getByDemandCode/{demandCode}")
    @PreAuthorize("@el.check('demand:list')")
    public ResponseEntity getDemandByDemandCode(@PathVariable String demandCode) {
        return new ResponseEntity<>(reDemandService.getByDemandCode(demandCode), HttpStatus.OK);
    }

    @ErrorLog("根据需求单号获取单个用人需求表")
    @ApiOperation("根据需求单号获取单个用人需求表")
    @GetMapping(value = "/getDemandByPass")
    @PreAuthorize("@el.check('demand:list', 'transferRequest:add')")
    public ResponseEntity getDemandByPass() {
        return new ResponseEntity<>(reDemandService.getDemandByPass(), HttpStatus.OK);
    }

}
