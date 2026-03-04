package com.sunten.hrms.ac.rest;

import com.sunten.hrms.ac.dao.AcOaLeaveDao;
import com.sunten.hrms.ac.domain.AcLeaveApplication;
import com.sunten.hrms.ac.domain.LeaveReqForm;
import com.sunten.hrms.ac.dto.AcLeaveApplicationQueryCriteria;
import com.sunten.hrms.ac.dto.PmLeaveApplicationDTO;
import com.sunten.hrms.ac.service.AcLeaveApplicationService;
import com.sunten.hrms.ac.service.PmLeaveApplicationService;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.security.utils.JwtTokenUtil;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zouyp
 * @since 2023-05-29
 */
@RestController
@Api(tags = "请假申请")
@RequestMapping("/api/ac/hrLeave")
public class AcLeaveApplicationController {
    private static final String ENTITY_NAME = "hrLeave";
    private final AcLeaveApplicationService acLeaveApplicationService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Value("${sunten.oaUri}")
    private String oaUri;
    @Value("${sunten.oauth.hrms.client_id}")
    private String hrmsClientId;
    @Value("${sunten.oauth.hrms.client_secret}")
    private String hrmsClientSecret;
//    @Autowired
//    private PmLeaveApplicationService pmLeaveApplicationService;

    @Autowired
    private AcOaLeaveDao acOaLeaveDao;

    public AcLeaveApplicationController(AcLeaveApplicationService acLeaveApplicationService, FndUserService fndUserService) {
        this.acLeaveApplicationService = acLeaveApplicationService;
        this.fndUserService = fndUserService;
    }

    private final FndUserService fndUserService;


    @GetMapping("/getUserInfo")
    @ErrorLog("获取当前登录人员信息")
    @ApiOperation("获取当前登录人员信息")
    public ResponseEntity getUserInfo() {
        //  获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        return new ResponseEntity<>(acLeaveApplicationService.getByEmployeeId(fndUserDTO.getEmployee().getId()), HttpStatus.OK);
    }

    @GetMapping("/getHrLeaderList")
    @ErrorLog("从HR获取当前登录人员的对应上级领导列表")
    @ApiOperation("从HR获取当前登录人员的对应上级领导列表")
    public ResponseEntity getHrLeaderList(@RequestParam("work_card") String workCard, @RequestParam("employee_type") Integer employee_type) {
        List<Map<String, Object>> hrLeaderList = acLeaveApplicationService.getHrLeaderList(workCard);
        if (employee_type == 0 || employee_type == 1) {
            // 使用stream进行迭代
//            List<Map<String, Object>> newHrLeaderList = hrLeaderList.stream()
//                    .map(map -> {
//                        // 使用computeIfAbsent方法进行team_authorize_work_card和username的值替换
//                        Map<String, Object> updatedMap = new HashMap<>(map);
//                        String leader_authorize_work_card = (String) updatedMap.get("leader_authorize_work_card");
//                        if (leader_authorize_work_card != null && !"".equals(leader_authorize_work_card)) {
//                            updatedMap.put("username", leader_authorize_work_card);
//                        }
//                        return updatedMap;
//                    })
//                    .collect(Collectors.toList());
//            System.out.println("newHrLeaderList: " + newHrLeaderList);
            return new ResponseEntity<>(hrLeaderList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(hrLeaderList, HttpStatus.OK);
        }
    }

    @GetMapping("/getHrMangerList")
    @ErrorLog("从HR获取当前请假人员的对应的经理列表")
    @ApiOperation("从HR获取当前请假人员的对应的经理列表")
    public ResponseEntity getHrMangerList(@RequestParam("work_card") String workCard, @RequestParam("employee_type") Integer employee_type) {
        List<Map<String, Object>> hrMangerList = acLeaveApplicationService.getHrMangerList(workCard);
        if (employee_type == 0 || employee_type == 1) {
            // 使用stream进行迭代
//            List<Map<String, Object>> newHrMangerList = hrMangerList.stream()
//                    .map(map -> {
//                        // 使用computeIfAbsent方法进行team_authorize_work_card和username的值替换
//                        Map<String, Object> updatedMap = new HashMap<>(map);
//                        String manger_authorize_work_card = (String) updatedMap.get("manger_authorize_work_card");
//                        if (manger_authorize_work_card != null && !"".equals(manger_authorize_work_card)) {
//                            updatedMap.put("username", manger_authorize_work_card);
//                        }
//                        return updatedMap;
//                    })
//                    .collect(Collectors.toList());
//            System.out.println("newHrMangerList: " + newHrMangerList);
            return new ResponseEntity<>(hrMangerList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(hrMangerList, HttpStatus.OK);
        }
    }
    @GetMapping("/getDepartmentLists")
    @ErrorLog("从HR获取当前请假人员对应的主管列表")
    @ApiOperation("从HR获取当前请假人员对应的主管列表")
    public ResponseEntity getDepartmentLists(@RequestParam("work_card") String workCard) {
        List<Map<String, Object>> departmentList = acLeaveApplicationService.getDepartmentLists(workCard);
        // 使用stream进行迭代
//        List<Map<String, Object>> newDepartmentList = departmentList.stream()
//                .map(map -> {
//                    // 使用computeIfAbsent方法进行team_authorize_work_card和username的值替换
//                    Map<String, Object> updatedMap = new HashMap<>(map);
//                    String department_authorize_work_card = (String) updatedMap.get("department_authorize_work_card");
//                    if (department_authorize_work_card != null && !"".equals(department_authorize_work_card)) {
//                        updatedMap.put("username", department_authorize_work_card);
//                    }
//                    return updatedMap;
//                })
//                .collect(Collectors.toList());
////        System.out.println("newDepartmentList: " + newDepartmentList);
//        return new ResponseEntity<>(departmentList, HttpStatus.OK);
        return new ResponseEntity<>(departmentList, HttpStatus.OK);
    }

    @GetMapping("/getTeamLeaderList")
    @ErrorLog("从HR获取当前请假人员的对应班长列表")
    @ApiOperation("从HR获取当前请假人员的对应班长列表")
    public ResponseEntity getTeamLeaderList(@RequestParam("work_card") String workCard) {
        List<Map<String, Object>> foremanList = acLeaveApplicationService.getTeamLeaderList(workCard);
        // 使用stream进行迭代
//        List<Map<String, Object>> updatedForemanList = foremanList.stream()
//                .map(map -> {
//                    // 使用computeIfAbsent方法进行team_authorize_work_card和username的值替换
//                    Map<String, Object> updatedMap = new HashMap<>(map);
//                    String teamAuthorizeWorkCard = (String) updatedMap.get("team_authorize_work_card");
//                    if (teamAuthorizeWorkCard != null && !"".equals(teamAuthorizeWorkCard)) {
//                        updatedMap.put("username", teamAuthorizeWorkCard);
//                    }
//                    return updatedMap;
//                })
//                .collect(Collectors.toList());
//        System.out.println("foremanList: " + foremanList);
        return new ResponseEntity<>(foremanList, HttpStatus.OK);
    }

    @PostMapping("/addApprovalDetail")
    @ErrorLog("新增审批管理行详情")
    @ApiOperation("新增审批管理行详情")
    public ResponseEntity addApprovalDetail(@RequestParam(value = "dept_id", required = false) Integer dept_id,
//            @RequestParam(value = "dept_name", required = false) String dept_name,
                                            @RequestParam(value = "team_work_card", required = false) String team_work_card,
                                            @RequestParam(value = "team_authorize_work_card", required = false) String team_authorize_work_card,
                                            @RequestParam(value = "department_work_card", required = false) String department_work_card,
                                            @RequestParam(value = "department_authorize_work_card", required = false) String department_authorize_work_card,
                                            @RequestParam(value = "manger_work_card", required = false) String manger_work_card,
                                            @RequestParam(value = "manger_authorize_work_card", required = false) String manger_authorize_work_card,
                                            @RequestParam(value = "leader_work_card", required = false) String leader_work_card,
                                            @RequestParam(value = "leader_authorize_work_card", required = false) String leader_authorize_work_card) {
        Integer res = acLeaveApplicationService.addApprovalDetail(dept_id, team_work_card, team_authorize_work_card,
                department_work_card, department_authorize_work_card, manger_work_card, manger_authorize_work_card,
                leader_work_card, leader_authorize_work_card);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/updateApproval")
    @ErrorLog("修改审批管理行详情")
    @ApiOperation("修改审批管理行详情")
    public ResponseEntity updateApproval(@RequestParam("id") Integer id,
                                         @RequestParam("dept_id") Integer dept_id,
                                         @RequestParam(value = "team_work_card", required = false) String team_work_card,
                                         @RequestParam(value = "department_work_card", required = false) String department_work_card,
                                         @RequestParam(value = "manger_work_card", required = false) String manger_work_card,
                                         @RequestParam(value = "leader_work_card", required = false) String leader_work_card,
                                         @RequestParam(value = "team_authorize_work_card", required = false) String team_authorize_work_card,
                                         @RequestParam(value = "department_authorize_work_card",required = false) String department_authorize_work_card,
                                         @RequestParam(value = "manger_authorize_work_card",required = false) String manger_authorize_work_card,
                                         @RequestParam(value = "leader_authorize_work_card",required = false) String leader_authorize_work_card) {
//        System.out.println("参数：");
//        System.out.println("team_work_card: " + team_work_card);
//        System.out.println("department_work_card: " + department_work_card);
//        System.out.println("manger_work_card: " + manger_work_card);
//        System.out.println("leader_work_card: " + leader_work_card);
//        System.out.println("team_authorize_work_card: " + team_authorize_work_card);
//        System.out.println("department_authorize_work_card: " + department_authorize_work_card);
//        System.out.println("manger_authorize_work_card: " + manger_authorize_work_card);
//        System.out.println("leader_authorize_work_card: " + leader_authorize_work_card);
        acLeaveApplicationService.updateApproval(id, dept_id,
                team_work_card,
                department_work_card,
                manger_work_card,
                leader_work_card,
                team_authorize_work_card,
                department_authorize_work_card,
                manger_authorize_work_card,
                leader_authorize_work_card);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/getApprovalDetail")
    @ErrorLog("获取审批管理行详情")
    @ApiOperation("获取审批管理行详情")
    public ResponseEntity getApprovalDetail(@RequestParam("id") Integer id, @RequestParam("dept_id") Integer dept_id) {
        List<HashMap<String,Object>> resList = acLeaveApplicationService.getApprovalDetail(id, dept_id);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @GetMapping("/getAcAuthorizationList")
    @ErrorLog("获取审批管理列表")
    @ApiOperation("获取审批管理列表")
    public ResponseEntity getAcAuthorizationList(@RequestParam(value = "deptId") Integer deptId) {
        List<HashMap<String,Object>> resList = acLeaveApplicationService.getAcAuthorizationList(deptId);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @PutMapping("/notifyHrManager")
    @ErrorLog("通知人资经理有人再次上传附件")
    @ApiOperation("通知人资经理有人再次上传附件")
    public ResponseEntity notifyHrManager(@RequestParam("oaOrder") String oaOrder) {
        acLeaveApplicationService.notifyHrManager(oaOrder);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping("/leaveApplicationNotifyHr")
    @ErrorLog("通知当前节点审批人，有人上传附件了")
    @ApiOperation("通知当前节点审批人，有人上传附件了")
    public ResponseEntity leaveApplicationNotifyHr(@RequestParam("oaOrder") String oaOrder, @RequestParam("mailUser") String mailUser) {
        acLeaveApplicationService.leaveApplicationNotifyHr(oaOrder, mailUser);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PutMapping("/notifyHr")
    @ErrorLog("通知HR有人上传附件了")
    @ApiOperation("通知人资经理有人再次上传附件")
    public ResponseEntity notifyHr(@RequestParam("oaOrder") String oaOrder) {
        acLeaveApplicationService.notifyHr(oaOrder);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/getHrManger")
    @ErrorLog("获取人资经理")
    @ApiOperation("获取人资经理")
    public ResponseEntity getHrManger() {
        HashMap<String, Object> res = acLeaveApplicationService.getHrManger();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getHrInfo")
    @ErrorLog("获取人事专员")
    @ApiOperation("获取人事专员")
    public ResponseEntity getHrInfo() {
        List<HashMap<String, Object>> res = acLeaveApplicationService.getHrInfo();
//        System.out.println("看这里： " + res);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping("/getDepartmentList")
    @ErrorLog("获取公司全部部门")
    @ApiOperation("获取公司全部部门")
    public ResponseEntity getDepartmentList() {
        List<HashMap<String, String>> res = acLeaveApplicationService.getDepartmentList();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getUserByDepartmentID")
    @ErrorLog("根据部门ID获取部门下的全部人员")
    @ApiOperation("根据部门ID获取对应的部门人员信息")
    public ResponseEntity getUserByDepartmentID(@RequestParam("departmentID") String departmentID) {
        List<HashMap<String, String>> userMap = acLeaveApplicationService.getUserByDepartmentID(departmentID);
        return new ResponseEntity<>(userMap, HttpStatus.OK);
    }

    @GetMapping("/getNurseDays")
    @ErrorLog("护理假逻辑判断")
    @ApiOperation(value = "根据用户提交的父母出生日期计算出，已经使用护理假次数、已休护理假天数，剩余天数")
    public ResponseEntity getNurseDays(@RequestParam("parentDate") String parentDate, @RequestParam("workCard") String workCard) {
        HashMap<String, Object> resMap;
        resMap = acLeaveApplicationService.getNurseDays(parentDate, workCard);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @GetMapping("/calculateChildrenDate")
    @ErrorLog("育儿假逻辑判断")
    @ApiOperation(value = "根据用户提交的子女出生日期计算出，已经使用育儿假次数、已休育儿假天数，剩余天数")
    public ResponseEntity calculateChildrenDate(@RequestParam("childrenDate") String childrenDate, @RequestParam("workCard") String workCard) {
        HashMap<String, Object> res = new HashMap<>(16);
        res = acLeaveApplicationService.logicalProcessing(childrenDate, workCard);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getLeaveInfo")
    @ErrorLog("根据oa_order")
    @ApiOperation("根据oa_order来查询对应的请假详情信息")
    public ResponseEntity getLeaveInfo(@RequestParam("checkOaOrder") String checkOaOrder) {
//        System.out.println("checkOaOrder: " + checkOaOrder);
        HashMap<String, List<HashMap<String, Object>>> res = acLeaveApplicationService.getLeaveInfo(checkOaOrder);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/getHrEditLeaveInfo")
    @ErrorLog("根据oa_order")
    @ApiOperation("根据oa_order来查询对应的请假详情信息")
    public ResponseEntity getHrEditLeaveInfo(@RequestParam("checkOaOrder") String checkOaOrder) {
//        System.out.println("checkOaOrder: " + checkOaOrder);
        HashMap<String, List<HashMap<String, Object>>> res = acLeaveApplicationService.getHrEditLeaveInfo(checkOaOrder);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }



    @PutMapping("/changeAcApprovalDepartmentStatus")
    @ErrorLog("更新请假审批部门审批人员是否生效")
    @ApiOperation("更新请假审批部门审批人员是否生效")
    public ResponseEntity changeAcApprovalDepartmentStatus(@RequestParam("id") Integer id,
                                                           @RequestParam("deptId") Integer deptId,
                                                           @RequestParam("enabled_flag") Boolean enabled_flag) {
        if (enabled_flag) {
            acLeaveApplicationService.changeAcApprovalDepartmentStatus(id, deptId, 1);
        } else {
            acLeaveApplicationService.changeAcApprovalDepartmentStatus(id, deptId, 0);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @ErrorLog("从OA端进入，根据OA号获取请假主表和子表")
    @ApiOperation("从OA端进入，根据OA号获取请假主表和子表")
    @GetMapping(value = "/getLeaveInfoByReqCode")
    public ResponseEntity getLeaveInfoByReqCode(String reqCode) {
        return new ResponseEntity<>(acLeaveApplicationService.getLeaveInfoByReqCode(reqCode), HttpStatus.OK);
    }

    @ErrorLog("从OA端进入，OA审批时修改请假主表")
    @ApiOperation("从OA端进入，OA审批时修改请假主表")
    @PostMapping(value = "/updateFromOA")
    public ResponseEntity updateFromOA(@RequestBody AcLeaveApplication acLeaveApplication) {
        acLeaveApplicationService.writeOaApprovalResult(acLeaveApplication);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/getFileList")
    @ErrorLog("从OA当中获取对应的附件列表")
    @ApiOperation("根据工牌号和OA号获取对应的附件列表")
    public ResponseEntity getFileList(@RequestParam("workCard") String workCard, @RequestParam("oaOrder") String oaOrder) {
        // 获取附件列表
        List<HashMap<String, Object>> fileList = acOaLeaveDao.getFileList(workCard, oaOrder);
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("fileList", fileList);
        // 根据申请单号去获取当前流程taskID和processID
        HashMap<String, String> flowMap = acOaLeaveDao.getFlowInfo(oaOrder);
        res.put("flowInfo", flowMap);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/submitForm")
    @ErrorLog("提交请假表单信息并且做校验")
    @ApiOperation("提交请假表单信息并且做校验")
    public ResponseEntity submitForm(@RequestBody LeaveReqForm leaveReqForm) {
//        System.out.println(leaveReqForm.toString());
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String oaOrderWordCard = fndUserDTO.getEmployee().getWorkCard();
        // 开始校验提交的数据是否符合
        HashMap<String, String> resMap = acLeaveApplicationService.checkSubmitData(leaveReqForm, oaOrderWordCard);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @PostMapping("/submitAttachment")
    @ErrorLog("接受请假单附件")
    @ApiOperation("接受请假单附件")
    public ResponseEntity<String> submitAttachment(HttpServletRequest request, @RequestParam("file") List<MultipartFile> newFiles, @RequestParam("oa_order") String oaOrder) throws UnsupportedEncodingException, URISyntaxException {
        // 处理接收到的文件列表 newFiles
//        System.out.println("newFiles: " + newFiles);
//        System.out.println(newFiles.size());
//        System.out.println(newFiles.get(0));
//        System.out.println(newFiles.get(0).getOriginalFilename());
        MultipartFile newFile = newFiles.get(0);
        String suffix = FileUtil.getExtensionName(newFile.getOriginalFilename());
        String name = FileUtil.getFileNameNoEx(newFile.getOriginalFilename());
        String type1 = FileUtil.getFileTypeByMimeType(suffix);
        String type = FileUtil.getFileType(suffix);
//        System.out.println(name);
//        System.out.println(suffix);
//        System.out.println(type);
//        System.out.println(type1);
        String authToken = jwtTokenUtil.getToken(request);
//        System.out.println("authToken: " + authToken);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        URI tagetUri = new URIBuilder(oaUri + "/fjsc/hrToOaFileUpload.do").build();
        HttpPost post = new HttpPost(tagetUri);
        // 构建请求体
//        MultipartEntity multipartEntity = new MultipartEntity();
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
    // 现在已经得到 MultipartFile 了
//        System.out.println("FILE[]: " + newFiles);
//        System.out.println("oa_order: " + oaOrder);
//        MultipartFile newFile = newFile[0];
//        System.out.println(newFile);
//        String suffix = FileUtil.getExtensionName(newFile.getOriginalFilename());
//        String name = FileUtil.getFileNameNoEx(newFile.getOriginalFilename());
//        可自行选择方式
//        String type = FileUtil.getFileTypeByMimeType(suffix);
//        String type = FileUtil.getFileType(suffix);
//        System.out.println(suffix);
//        System.out.println(name);
//        System.out.println(type);
//        System.out.println("newFile: " + newFile.getOriginalFilename());
//        String fileType = newFile.getContentType();
//        long fileSize = newFile.getSize();
//        System.out.println("fileType: " + fileType);
//        System.out.println("fileSize: " + fileSize);
//        System.out.println("-------------------------------------------------------------------");
//        return new ResponseEntity<>("ok", HttpStatus.OK);
//    }
    @GetMapping("/autoMatchNewLeaderUser")
    @ErrorLog("新的自动匹配上级领导")
    @ApiOperation("新的自动匹配上级领导")
    public ResponseEntity autoMatchNewLeaderUser(@RequestParam("work_card") String work_card, @RequestParam("employee_type") Integer employee_type) {
        HashMap<String, String> resMap = new HashMap<>(16);
        resMap = acLeaveApplicationService.autoMatchNewLeaderUser(work_card);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @GetMapping("/autoMatchLeaderUser")
    @ErrorLog("自动匹配上级领导")
    @ApiOperation("自动匹配上级领导")
    public ResponseEntity autoMatchLeaderUser(@RequestParam("department") String department, @RequestParam("administrative_office") String administrative_office) {
        HashMap<String, String> resMap = new HashMap<>(16);
        resMap = acLeaveApplicationService.autoMatchLeaderUser(department, administrative_office);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @GetMapping("/getAcLeaveLeaderUser")
    @ErrorLog("爬架构得到员工对应高级领导")
    @ApiOperation("爬架构得到员工对应高级领导")
    public ResponseEntity getAcLeaveLeaderUser(@RequestParam("deptName") String deptName) {
        HashMap<String, Object> resMap = new HashMap<>(16);
        resMap = acLeaveApplicationService.getAcLeaveLeaderUser(deptName);
//        System.out.println("---resMap---: " + resMap);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }
    @PutMapping("/updateOaReq")
    @ErrorLog("反写当前请假审批流程节点信息")
    @ApiOperation("反写当前请假审批流程节点信息")
    public ResponseEntity updateOaReq(@RequestParam("oaOrder") String oaOrder, @RequestParam("approval_node") String approval_node, @RequestParam("approvalEmployee") String approvalEmployee) {
        HashMap<String, String> resMap = new HashMap<>(16);
//        System.out.println("---oaOrder---: " + oaOrder);
//        System.out.println("---approval_node---: " + approval_node);
//        System.out.println("---approvalEmployee---: " + approvalEmployee);
        resMap = acLeaveApplicationService.updateOaReq(oaOrder, approval_node, approvalEmployee);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @GetMapping("/getLeaveOfAbsence")
    @ErrorLog("获取个人事假信息")
    @ApiOperation("获取个人事假信息")
    public ResponseEntity getLeaveOfAbsence(@RequestParam("workCard") String workCard) {
        if ("".equals(workCard)) {
            // 获取令牌中的人员信息
            FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
            if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
                throw new InfoCheckWarningMessException("找不到登录人员对应用户");
            }
            workCard = fndUserDTO.getEmployee().getWorkCard();
        }
        return new ResponseEntity<>(acLeaveApplicationService.getLeaveOfAbsence(workCard), HttpStatus.OK);
    }

    @GetMapping(value = "/getAnnualLeave")
    @ErrorLog("获取年假信息")
    @ApiOperation("获取年假信息和调休信息")
    public ResponseEntity getAnnualLeave(@RequestParam("workCard") String workCard) {
        if ("".equals(workCard)) {
            // 获取令牌中的人员信息
            FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
            if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
                throw new InfoCheckWarningMessException("找不到登录人员对应用户");
            }
            workCard = fndUserDTO.getEmployee().getWorkCard();
        }
        // 获取个人年假信息
        Double annualLeaveDays = acLeaveApplicationService.getAnnualLeave(workCard);
        /**
         * 获取个人调休信息
         * 月度可调休时数 = 月度休息日加班工时 - 月度已调休小时
         */
        // HR中查詢月度可調休時數
        Double monthlyAdjustableHours = acLeaveApplicationService.getMonthlyRestDayOvertimeHours(workCard);
        // 原來OA中查詢月度可調休信息
        //        Double monthlyAdjustableHours = acLeaveApplicationService.getCompensatoryLeaveHoursWithMonth(workCard);
        // 年度总年假天数
        Double teamHourAnnualYear =  acLeaveApplicationService.getTeamHourAnnualYear(workCard);
        // 已经休年假天数
        Double team_hour_used_annual_year = acLeaveApplicationService.getTeamUsedHourAnnualYear(workCard);
        Double[] doubles = {annualLeaveDays, monthlyAdjustableHours, teamHourAnnualYear, team_hour_used_annual_year};
        return new ResponseEntity<>(doubles, HttpStatus.OK);
    }

    @GetMapping(value = "/getUserViews")
    @ErrorLog("查询员工信息")
    @ApiOperation("查询员工信息")
    public ResponseEntity getUserViews(@RequestParam(value = "workCard") String workCard) {
        Map<String, String> userViews = acLeaveApplicationService.getUserViews(workCard);
        if (userViews == null || userViews.isEmpty()) {
            return new ResponseEntity<>("没有找到对应的员工信息", HttpStatus.OK);
        }
        return new ResponseEntity<>(userViews, HttpStatus.OK);
    }

//    @GetMapping(value = "/getHrTeamLeaderList")
//    @ErrorLog("查询车间员工班组长列表")
//    @ApiOperation("查询车间员工班组长列表")
//    public ResponseEntity getUserForemanList(@RequestParam("deptName") String deptName, @RequestParam("section") String section,
//                                             @RequestParam("group") String group) {
//
//    }

    @GetMapping(value = "/getUserForemanList")
    @ErrorLog("查询车间员工班组长列表")
    @ApiOperation("查询车间员工班组长列表")
    public ResponseEntity getUserForemanList(@RequestParam("deptName") String deptName, @RequestParam("section") String section,
                                             @RequestParam("group") String group) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("deptName", deptName);
        params.put("section", section);
        params.put("group", group);
        List<Map<String, String>> foremanList = acLeaveApplicationService.getUserForemanList(params)
                .stream()
                .filter(Objects::nonNull)
                .map(userMes -> {
                    Map<String, String> map = new HashMap<>(16);
                    map.put("position", userMes.getPosition());
                    map.put("nickname", userMes.getNickName());
                    map.put("usersection", userMes.getUserSection());
                    map.put("username", userMes.getUserName());
                    return map;
                })
                .collect(Collectors.toList());
//        System.out.println("List----: " + foremanList);
        if (foremanList.isEmpty() && foremanList == null) {
            return new ResponseEntity<>("没有找到对应的班组长,请直接选择主管", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foremanList, HttpStatus.OK);
    }

    @GetMapping(value = "/getDepartmentHeads")
    @ErrorLog("查询部门主管列表")
    @ApiOperation("查询部门主管列表")
    public ResponseEntity getDepartmentHeads(@RequestParam("deptName") String deptName, @RequestParam("section") String section) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("deptName", deptName);
        params.put("section", section);
        List<Map<String, String>> foremanList = acLeaveApplicationService.getDepartmentHeads(params)
                .stream()
                .filter(Objects::nonNull)
                .map(userMes -> {
                    Map<String, String> map = new HashMap<>(16);
                    map.put("position", userMes.getPosition());
                    map.put("nickname", userMes.getNickName());
                    map.put("usersection", userMes.getUserSection());
                    map.put("username", userMes.getUserName());
                    return map;
                })
                .collect(Collectors.toList());
//        System.out.println("List----: " + foremanList);
        if (foremanList.isEmpty() && foremanList == null) {
            return new ResponseEntity<>("没有找到对应的主管,请直接选择经理", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foremanList, HttpStatus.OK);
    }

    @GetMapping(value = "/getMangerList")
    @ErrorLog("查询部门经理列表")
    @ApiOperation("查询部门经理列表")
    public ResponseEntity getMangerList(@RequestParam("deptName") String deptName) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("deptName", deptName);
        List<Map<String, String>> foremanList = acLeaveApplicationService.getMangerList(params)
                .stream()
                .filter(Objects::nonNull)
                .map(userMes -> {
                    Map<String, String> map = new HashMap<>(16);
                    map.put("position", userMes.getPosition());
                    map.put("nickname", userMes.getNickName());
                    map.put("usersection", userMes.getUserSection());
                    map.put("username", userMes.getUserName());
                    return map;
                })
                .collect(Collectors.toList());
//        System.out.println("List----: " + foremanList);
        if (foremanList.isEmpty() && foremanList == null) {
            return new ResponseEntity<>("没有找到对应的经理,请直接选择上级领导", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foremanList, HttpStatus.OK);
    }

    @GetMapping(value = "/getDirectorList")
    @ErrorLog("查询公司上级领导列表")
    @ApiOperation("查询公司上级领导列表")
    public ResponseEntity getDirectorList(@RequestParam("keyWord") String keyWord) {
        Map<String, Object> directorMap = acLeaveApplicationService.getDirectorMap(keyWord);
//        System.out.println("map director: " + directorMap);
        if (directorMap.isEmpty() && directorMap == null) {
            return new ResponseEntity<>("出错了，请联系管理员", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(directorMap, HttpStatus.OK);
    }

    @GetMapping(value = "/checkLeaveSubDataContinuous")
    @ErrorLog("查询请假时间是否连续请假")
    @ApiOperation("查询请假时间是否连续请假")
    public ResponseEntity checkLeaveSubDataContinuous(@RequestParam("start_time") String start_time,
                                                      @RequestParam("end_time") String end_time,
                                                      @RequestParam("workCard") String workCard,
                                                      @RequestParam(value = "subTotalNumber", required = false) Float  subTotalNumber) {
        // 从HR数据库中查询-请假的开始日期或结束日期是否跟审批中的日期是否有重复 2024-05-26 按照人资新要求进行修改，经过阿敏和百安审核技术方案
//        HashMap<String, Object> res = acLeaveApplicationService.checkLeaveSubDataContinuous(start_time, end_time, workCard, subTotalNumber);
        HashMap<String, Object> res = acLeaveApplicationService.newCheckLeaveSubDataContinuous(start_time, end_time, workCard, subTotalNumber);
//        System.out.println("-----res----: " + res);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/getWorkDays")
    @ErrorLog("查询工作日天数，非排班使用")
    @ApiOperation(value = "查询工作日天数，非排班使用")
    public ResponseEntity getWorkDays(@RequestParam("start_time") String start_time,
                                      @RequestParam("end_time") String end_time) {
        Integer res = acLeaveApplicationService.getWorkDays(start_time, end_time);
//        System.out.println("--------------res-----------: " + res);
        HashMap<String, Integer> resMap = new HashMap(16);
        resMap.put("days", res);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @GetMapping(value = "/getWorkDaysSecond")
    @ErrorLog("查询工作日天数，非排班使用,事假和年假使用")
    @ApiOperation(value = "查询工作日天数，非排班使用,事假和年假使用")
    public ResponseEntity getWorkDaysSecond(@RequestParam("start_time") String start_time,
                                      @RequestParam("end_time") String end_time) {
        Integer res = acLeaveApplicationService.getWorkDaysSecond(start_time, end_time);
//        System.out.println("--------------res-----------: " + res);
        HashMap<String, Integer> resMap = new HashMap(16);
        resMap.put("days", res);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }


    @PutMapping("/updateResultToHrLeave")
    @ErrorLog("返写撤销申请单结果")
    @ApiOperation(value = "返写撤销申请单结果")
    public ResponseEntity updateResultToHrLeave(@RequestParam("oaOrder") String oaOrder, @RequestParam("approvalResult") String approvalResult) {
        acLeaveApplicationService.updateResultToHrLeave(oaOrder, approvalResult);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    @PutMapping("/cancelLeave")
    @ErrorLog("撤销请假单")
    @ApiOperation(value = "撤销请假单")
    public ResponseEntity cancelLeave(@RequestParam("oaOrder") String oaOrder) {
        Integer res = acLeaveApplicationService.cancelLeave(oaOrder);
        HashMap<String, Integer> resMap = new HashMap<String, Integer>(16);
        resMap.put("days", res);
        return new ResponseEntity<>(resMap, HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("@el.check('hrLeave:add')")
    public ResponseEntity create(@Validated @RequestBody AcLeaveApplication hrLeave) {
        return new ResponseEntity<>(acLeaveApplicationService.insert(hrLeave), HttpStatus.CREATED);
    }

    @Log("删除")
    @ApiOperation("删除")
    @DeleteMapping(value = "")
    @PreAuthorize("@el.check('hrLeave:del')")
    public ResponseEntity delete() {
        try {
            acLeaveApplicationService.delete();
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改")
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('hrLeave:edit')")
    public ResponseEntity update(@Validated(AcLeaveApplication.Update.class) @RequestBody AcLeaveApplication hrLeave) {
        acLeaveApplicationService.update(hrLeave);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    //    private void setCriteria(PmLeaveApplicationQueryCriteria criteria) {
//
//    }
//
//    @ErrorLog("查询（分页）")
//    @ApiOperation("查询（分页）")
//    @GetMapping
//    @PreAuthorize("@el.check('leaveApproval:list')")
//    public ResponseEntity getHrLeavePage(PmLeaveApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) {
//        System.out.println(criteria);
//        setCriteria(criteria);
//        // 获取令牌中的人员信息
//        FndUserDTO  fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
//        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
//            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
//        }
//        String workCard = fndUserDTO.getEmployee().getWorkCard();
//        criteria.setUser_name(workCard);
//        if ("全部申请单".equals(reqStatus)) {
//            reqStatus = "0";
//        } else if ("已结束申请单".equals(reqStatus)) {
//            reqStatus = "1";
//        } else if("审批中申请单".equals(reqStatus)) {
//            reqStatus = "2";
//        }
//        criteria.setReqStatus(reqStatus);
//        System.out.println("criteria: " + criteria);
//        return new ResponseEntity<>(pmLeaveApplicationService.listAll(criteria, pageable), HttpStatus.OK);
//    }

    private void setCriteria(AcLeaveApplicationQueryCriteria criteria) {

    }

    @ErrorLog("查询（分页）")
    @ApiOperation("查询（分页）")
    @GetMapping
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getHrLeavePage(AcLeaveApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) {
//        System.out.println(criteria);
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        }
        criteria.setReqStatus(reqStatus);
//        System.out.println("criteria: " + criteria);
        return new ResponseEntity<>(acLeaveApplicationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("请假统计查询（分页）")
    @ApiOperation("请假统计查询（分页）")
    @GetMapping("/leaveStatistics")
    public ResponseEntity getHrLeavePageLeaveStatistics(AcLeaveApplicationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("reqStatusValue") String reqStatus) {
//        System.out.println(criteria);
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        } else if("需上传附件".equals(reqStatus)) {
            reqStatus = "需上传附件";
        }
        criteria.setReqStatus(reqStatus);
//        System.out.println("criteria: " + criteria);
        return new ResponseEntity<>(acLeaveApplicationService.listAllLeaveStatistics(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）")
    @ApiOperation("查询（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('leaveApproval:list')")
    public ResponseEntity getHrLeaveNoPaging(AcLeaveApplicationQueryCriteria criteria) {
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
//        System.out.println("criteria: " + criteria);
        return new ResponseEntity<>(acLeaveApplicationService.listAll(criteria), HttpStatus.OK);
    }

    //    @ErrorLog("查询（不分页）")
//    @ApiOperation("查询（不分页）")
//    @GetMapping(value = "/noPaging")
//    @PreAuthorize("@el.check('leaveApproval:list')")
//    public ResponseEntity getHrLeaveNoPaging(PmLeaveApplicationQueryCriteria criteria) {
//        setCriteria(criteria);
//        // 获取令牌中的人员信息
//        FndUserDTO  fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
//        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
//            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
//        }
//        String workCard = fndUserDTO.getEmployee().getWorkCard();
//        criteria.setUser_name(workCard);
//        System.out.println("criteria: " + criteria);
//    return new ResponseEntity<>(pmLeaveApplicationService.listAll(criteria), HttpStatus.OK);
//    }


    @ErrorLog("查询")
    @ApiOperation("查询")
    @GetMapping("/getLeaveFormList")
    public ResponseEntity getLeaveFormList() {
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        List<PmLeaveApplicationDTO> res = acLeaveApplicationService.getLeaveFormList(workCard);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ErrorLog("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
//    @PreAuthorize("@el.check('hrLeave:list')")
    public void download(HttpServletResponse response, AcLeaveApplicationQueryCriteria criteria, @RequestParam("reqStatusValue") String reqStatus) throws IOException {
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        }
        criteria.setReqStatus(reqStatus);
        acLeaveApplicationService.download(acLeaveApplicationService.listAll(criteria), response);
    }

    @ErrorLog("人资用的导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download2")
//    @PreAuthorize("@el.check('hrLeave:list')")
    public void download2(HttpServletResponse response, AcLeaveApplicationQueryCriteria criteria, @RequestParam("reqStatusValue") String reqStatus) throws IOException {
        setCriteria(criteria);
        // 获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        String workCard = fndUserDTO.getEmployee().getWorkCard();
        criteria.setUser_name(workCard);
        if ("全部申请单".equals(reqStatus)) {
            reqStatus = "全部申请单";
        } else if ("已结束申请单".equals(reqStatus)) {
            reqStatus = "已结束申请单";
        } else if ("审批中申请单".equals(reqStatus)) {
            reqStatus = "审批中申请单";
        }
        criteria.setReqStatus(reqStatus);
        acLeaveApplicationService.download(acLeaveApplicationService.listAll2(criteria), response);
    }

    @ErrorLog("根据开始日期查询当前日期全部上班时间段")
    @ApiOperation("根据开始日期查询当前日期全部上班时间段")
    @GetMapping("/getStartTimeList")
    public ResponseEntity getStartTimeList(@RequestParam("work_card") String workCard, @RequestParam("start_time") String startTime) {
        HashMap<String, Object> startTimeRes = acLeaveApplicationService.getStartTimeList(workCard, startTime);
        return new ResponseEntity<>(startTimeRes, HttpStatus.OK);
    }

    @ErrorLog("根据开始日期查询当前日期全部上班时间段")
    @ApiOperation("根据开始日期查询当前日期全部上班时间段")
    @GetMapping("/getEndTimeList")
    public ResponseEntity getEndTimeList(@RequestParam("work_card") String workCard, @RequestParam("end_time") String endTime) {
        HashMap<String, Object> endTimeRes = acLeaveApplicationService.getEndTimeList(workCard, endTime);
        return new ResponseEntity<>(endTimeRes, HttpStatus.OK);
    }

    /**
     * 判断startTime和endTime之间是否存在未排班的日期，如果有则返回提示那一天是，如果都有排班，那就返回这个日期范围内有几天是休息日
     */
    @ErrorLog("判断startTime和endTime之间是否存在未排班的日期")
    @ApiOperation("判断startTime和endTime之间是否存在未排班的日期")
    @GetMapping("/checkLeaveRule")
    public ResponseEntity checkLeaveRule(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("workCard") String workCard) {
        HashMap<String, Object> endTimeRes = acLeaveApplicationService.checkLeaveRule(startTime, endTime, workCard);
        return new ResponseEntity<>(endTimeRes, HttpStatus.OK);
    }

    /**
     * 判断startTime和endTime之间是否存在未排班的日期，如果有则返回提示那一天是，如果都有排班，那就返回这个日期范围内有几天是休息日
     */
    @ErrorLog("判断startTime和endTime之间是否存在未排班的日期")
    @ApiOperation("判断startTime和endTime之间是否存在未排班的日期")
    @GetMapping("/shiftchecks")
    public ResponseEntity shiftChecks(@RequestParam("workCard") String workCard) {
        Integer res = acLeaveApplicationService.shiftChecks(workCard);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
