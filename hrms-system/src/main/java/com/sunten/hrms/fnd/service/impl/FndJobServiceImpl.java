package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.dao.FndJobDao;
import com.sunten.hrms.fnd.dao.FndJobDeptDao;
import com.sunten.hrms.fnd.domain.*;
import com.sunten.hrms.fnd.dto.FndJobDTO;
import com.sunten.hrms.fnd.dto.FndJobQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndJobMapper;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndJobService;
import com.sunten.hrms.fnd.service.FndUpdateHistoryService;
import com.sunten.hrms.pm.dao.PmEmployeeJobDao;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.PmEmployeeJobQueryCriteria;
import com.sunten.hrms.td.dao.TdJobGradingDao;
import com.sunten.hrms.td.domain.TdJobGrading;
import com.sunten.hrms.tool.dao.ToolEmailInterfaceDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.ListUtils;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Service
@CacheConfig(cacheNames = "job")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndJobServiceImpl extends ServiceImpl<FndJobDao, FndJob> implements FndJobService {
    private final FndJobDao fndJobDao;
    private final FndJobMapper fndJobMapper;
    private final FndDeptService fndDeptService;
    private final FndUpdateHistoryService fndUpdateHistoryService;
    private final PmEmployeeJobDao pmEmployeeJobDao;
    private final FndJobDeptDao fndJobDeptDao;
    private final FndDeptDao fndDeptDao;
    private final TdJobGradingDao tdJobGradingDao;
    private final ToolEmailInterfaceDao toolEmailInterfaceDao;

    @Value("${sunten.in-sunten-email-server-id}")
    private Long inSuntenEmailServerId;

    @Value("${email.yedongEmail}")
    private String yedongEmail;

    @Value("${email.lixiaoEmail}")
    private String lixiaoEmail;

    public FndJobServiceImpl(FndJobDao fndJobDao, FndJobMapper fndJobMapper, FndDeptService fndDeptService, FndUpdateHistoryService fndUpdateHistoryService, PmEmployeeJobDao pmEmployeeJobDao, FndJobDeptDao fndJobDeptDao, FndDeptDao fndDeptDao, TdJobGradingDao tdJobGradingDao, ToolEmailInterfaceDao toolEmailInterfaceDao) {
        this.fndJobDao = fndJobDao;
        this.fndJobMapper = fndJobMapper;
        this.fndDeptService = fndDeptService;
        this.fndUpdateHistoryService = fndUpdateHistoryService;
        this.pmEmployeeJobDao = pmEmployeeJobDao;
        this.fndJobDeptDao = fndJobDeptDao;
        this.fndDeptDao = fndDeptDao;
        this.tdJobGradingDao = tdJobGradingDao;
        this.toolEmailInterfaceDao = toolEmailInterfaceDao;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndJobDTO insert(FndJob jobNew) {
        TdJobGrading tdJobGrading = new TdJobGrading();
        if (jobNew.getJobCode() != null && "".equals(jobNew.getJobCode())) {
            FndDept deptParent = Optional.ofNullable(fndDeptDao.getByKey(jobNew.getDept().getId())).orElseGet(FndDept::new);
            jobNew.setJobCode(deptParent.getDeptCode() + '.' + jobNew.getJobName());
        }
        FndJobQueryCriteria criteria = new FndJobQueryCriteria();
        criteria.setJobCode(jobNew.getJobCode());
        criteria.setDeleted(false);
        List<FndJob> jobs = fndJobDao.listAllByCriteria(criteria);
        if (jobs != null && jobs.size() > 0) {
            throw new InfoCheckWarningMessException("岗位代码" + jobNew.getJobCode() + "已存在！");
        }
        // 判断岗位排序字段是否由系统生成，为0则视为系统自动，否则视为用户插入
        Long maxSequence = fndJobDao.getMaxJobSequence();
        maxSequence = (maxSequence == null ? 0L : maxSequence);
        if (jobNew.getJobSequence().equals(0L) || jobNew.getJobSequence() > maxSequence) {
            jobNew.setJobSequence(maxSequence + 1L);
        } else {
            fndJobDao.thanAutoIncrement(jobNew.getJobSequence());
        }
//        String deptName = fndJobDeptDao.listDeptNameById(jobNew.getDept().getId());
        fndJobDao.insertAllColumn(jobNew);
        //处理depts关联
        List<FndDept> deptNew = new ArrayList<>(jobNew.getDataScopeDepts());
        List<Long> deptIdsNew = deptNew.stream().map(FndDept::getId).sorted().collect(Collectors.toList());
        FndJobDept jobDept = new FndJobDept();
        jobDept.setJobId(jobNew.getId());
        deptIdsNew.stream().forEach((id) -> {
            jobDept.setDeptId(id);
            fndJobDeptDao.insertAllColumn(jobDept);
        });
        if (jobNew.getJobClass().equals("DVC")) {
            tdJobGrading.setDeptId(jobNew.getDept().getId());
            tdJobGrading.setJobId(jobNew.getId());
            tdJobGrading.setJobName(jobNew.getJobName());
            tdJobGrading.setDeptName(jobNew.getDeptName());
            tdJobGrading.setTeamName(jobNew.getTeamName());
            tdJobGrading.setEnabledFlag(true);
            tdJobGradingDao.insertAllColumn(tdJobGrading);
        }
        return fndJobMapper.toDto(jobNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        checkPerson(id);
        FndJob jobTemp = Optional.ofNullable(fndJobDao.getByKey(id)).orElseGet(FndJob::new);
        FndJob job = new FndJob();
        job.setId(id);
        job.setDeletedFlag(true);
        fndJobDao.updateDeletedFlag(job);
        fndJobDao.lessenJobSequence(jobTemp.getJobSequence());// 将排序字段大于此删除排序字段的都自减1
        FndUpdateHistory updateHistory = new FndUpdateHistory();
        updateHistory.setTableName("fnd_job");
        updateHistory.setColumnName("deletedFlag");
        updateHistory.setTableId(id);
        updateHistory.setNewValue("true");
        updateHistory.setOldValue("false");
        fndUpdateHistoryService.insert(updateHistory);
        fndJobDeptDao.deleteByJobId(id);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "user", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void update(FndJob jobNew) {
        FndJob jobTemp = Optional.ofNullable(fndJobDao.getByKey(jobNew.getId())).orElseGet(FndJob::new);
        ValidationUtil.isNull(jobTemp.getId(), "Job", "id", jobNew.getId());
        jobNew.setId(jobTemp.getId());
        if (!jobNew.getJobCode().equals(jobTemp.getJobCode())) {
            FndJobQueryCriteria criteria = new FndJobQueryCriteria();
            criteria.setJobCode(jobNew.getJobCode());
            criteria.setDeleted(false);
            List<FndJob> jobs = fndJobDao.listAllByCriteria(criteria);
            for (FndJob job : jobs) {
                if (job.getId() != jobNew.getId()) {
                    throw new InfoCheckWarningMessException("岗位代码" + jobNew.getJobCode() + "已存在！");
                }
            }
        }
        // 如果部门有改变，则需验证岗位是否存在任职
        if (!jobNew.getDept().getId().equals(jobTemp.getDept().getId())) {
            checkPerson(jobNew.getId());
        }
        // enabledFlag 旧值false 新值 true，进行数据检测
        if (jobTemp.getEnabledFlag() == true && jobNew.getEnabledFlag() == false) {
            checkPerson(jobNew.getId());
        }
        if (!jobNew.getJobName().equals(jobTemp.getJobName())) {
            PmEmployeeJob pmEmployeeJob = new PmEmployeeJob();
            pmEmployeeJob.setJob(jobNew);
            pmEmployeeJobDao.updateJobName(pmEmployeeJob);
        }
        // 当岗位的排序字段改变时，修改其他行的排序字段
        if (!jobNew.getJobSequence().equals(jobTemp.getJobSequence())) {
            Long maxJobSequence = fndJobDao.getMaxJobSequence();
            maxJobSequence = (maxJobSequence == null ? 1L : maxJobSequence);
            if (jobNew.getJobSequence().equals(0L) || jobNew.getJobSequence() > maxJobSequence){
                jobNew.setJobSequence(maxJobSequence + 1L);
            } else {
                if (jobNew.getJobSequence() > jobTemp.getJobSequence()) {
                    fndJobDao.inAutoIncrement(jobTemp.getJobSequence(),jobNew.getJobSequence());
                } else {
                    fndJobDao.inAutoIncrement(jobNew.getJobSequence(),jobTemp.getJobSequence());
                }
            }
        }

        fndJobDao.updateAllColumnByKey(jobNew);
        String columnS = "id,jobName,enabledFlag,sequence,authorizedStrength,jobCode,jobClass,jobDescribes,dataScope";
        fndUpdateHistoryService.insertDomainEqualsResultList("fnd_job", columnS, jobNew.getId(), jobNew, jobTemp);
        //处理depts关联
        List<FndDept> deptOld = fndDeptDao.listByJobId(jobTemp.getId());
        List<FndDept> deptNew = new ArrayList<>(jobNew.getDataScopeDepts());
        List<Long> deptIdsOld = deptOld.stream().map(FndDept::getId).sorted().collect(Collectors.toList());
        List<Long> deptIdsNew = deptNew.stream().map(FndDept::getId).sorted().collect(Collectors.toList());
        ListUtils.listComp(deptIdsOld, deptIdsNew);
        FndJobDept jobDept = new FndJobDept();
        jobDept.setJobId(jobTemp.getId());
        deptIdsOld.stream().forEach((id) -> {
            jobDept.setDeptId(id);
            fndJobDeptDao.deleteByEntityKey(jobDept);
        });
        deptIdsNew.stream().forEach((id) -> {
            jobDept.setDeptId(id);
            fndJobDeptDao.insertAllColumn(jobDept);
        });
    }

    private void checkPerson(Long id) {
        // TODO 检查此岗位是否存在在职人员信息
        PmEmployeeJobQueryCriteria jobQueryCriteria = new PmEmployeeJobQueryCriteria();
        jobQueryCriteria.setEnabled(true);
        List<Long> jobs = new ArrayList<Long>();
        jobs.add(id);
        jobQueryCriteria.setJobs(jobs);
        jobQueryCriteria.setLeaveFlag(false);
        List<PmEmployeeJob> pmJobList = pmEmployeeJobDao.listAllByCriteria(jobQueryCriteria);
        if (pmJobList.size() > 0) {
            throw new InfoCheckWarningMessException("此岗位存在在职人员，人员数：" + pmJobList.size());
        }
    }

    @Override
    @Cacheable(key = "#p0")
    public FndJobDTO getByKey(Long id) {
        FndJob job = Optional.ofNullable(fndJobDao.getByKey(id)).orElseGet(FndJob::new);
        ValidationUtil.isNull(job.getId(), "Job", "id", id);
        setDataScopeDepts(job);
        return fndJobMapper.toDto(job);
    }

    @Override
    public List<FndJobDTO> listAll(FndJobQueryCriteria criteria) {
        List<FndJob> jobs = fndJobDao.listAllByCriteria(criteria);
        setDataScopeDepts(jobs);
        return fndJobMapper.toDto(jobs);
    }

    @Override
    public Map<String, Object> listAll(FndJobQueryCriteria criteria, Pageable pageable) {
        Page<FndJob> page = PageUtil.startPage(pageable);
        List<FndJob> jobs = fndJobDao.listAllByCriteriaPage(page, criteria);
        setDataScopeDepts(jobs);
        List<FndJobDTO> jobDTOS = new ArrayList<>();
        for (FndJob job : jobs) {
            jobDTOS.add(fndJobMapper.toDto(job, fndDeptService.getNameById(job.getDept().getParentId())));
        }
        return PageUtil.toPage(jobDTOS, page.getTotal());
    }

    @Override
    public Map<String, Object> listAllByPage(FndJobQueryCriteria criteria, Pageable pageable) {
        Page<FndJob> page = PageUtil.startPage(pageable);
        List<FndJob> jobs = fndJobDao.listAllByCriteriaPage(page, criteria);
        List<FndJobDTO> jobDTOS = fndJobMapper.toDto(jobs);
        return PageUtil.toPage(jobDTOS, page.getTotal());
    }

    @Override
    public void download(List<FndJobDTO> jobDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndJobDTO jobDTO : jobDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", jobDTO.getJobName());
            map.put("部门", jobDTO.getDept().getExtDeptName() == null ? "" : jobDTO.getDept().getExtDeptName());
            map.put("科室", jobDTO.getDept().getExtDepartmentName() == null ? "" : jobDTO.getDept().getExtDepartmentName());
            map.put("班组", jobDTO.getDept().getExtTeamName() == null ? "" : jobDTO.getDept().getExtTeamName() );
            map.put("岗位编制", jobDTO.getAuthorizedStrength());
            map.put("岗位状态", jobDTO.getEnabledFlag() ? "启用" : "停用");
            map.put("数据权限", jobDTO.getDataScope());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchSortJob(List<FndJob> jobs) {

        if (jobs != null) {
            for (FndJob job : jobs) {

                fndJobDao.updateSortJob(job);
            }
        }
    }


    private void setDataScopeDepts(List<FndJob> jobs) {
        jobs.stream().forEach((job) -> {
            setDataScopeDepts(job);
        });
    }

    private void setDataScopeDepts(FndJob job) {
        Set<FndDept> deptSet = new HashSet<>(fndDeptDao.listByJobId(job.getId()));
        job.setDataScopeDepts(deptSet);
    }

    @Override
    public List<FndJobDTO> listAllByDeptAndLeader(Long deptId) {
        FndJobQueryCriteria criteria = new FndJobQueryCriteria();
        FndDept fndDept = fndDeptDao.getDeptByLeader();
        Set<Long> depts = new HashSet<>();
        depts.add(deptId);
        if (fndDept != null && !fndDept.getId().equals(deptId)) {
            depts.add(fndDept.getId());
        }
        criteria.setDeptIds(depts);
        List<FndJob> jobs = fndJobDao.listAllByCriteria(criteria);
        return fndJobMapper.toDto(jobs);
    }

    @Override
    public List<FndJobDTO> listByAdminJob(FndJobQueryCriteria criteria) {
        List<FndJob> jobs = fndJobDao.listByAdminJob(criteria);
        setDataScopeDepts(jobs);
        return fndJobMapper.toDto(jobs);
    }

    @Override
    public void sendAddJobEmail() {
        ToolEmailServer emailServer = new ToolEmailServer();
        emailServer.setId(inSuntenEmailServerId);
        String mailContent = null;
        mailContent ="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\"> <head>     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>  \n" +
                "   <style>         @page {             margin: 0;         }     </style> </head> <body style=\"margin: 0px;             padding: 0px;    font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;    \n" +
                "            color: #000;\"> <div style=\"height: auto;    width: 820px;    min-width: 820px;    margin: 0 auto;    margin-top: 20px;             border: 1px solid #eee;\">     <div style=\"padding: 10px;padding-bottom: 0px;\">       \n" +
                "\t\t\t  <p style=\"margin-bottom: 10px;padding-bottom: 0px;\">尊敬的用户，您好：</p> <p style=\"text-indent: 2em;\">人事专员新增了如下岗位，请及时补充岗位认证相关信息。谢谢！</p>" ;
        LocalDateTime date = LocalDateTime.now();
        ToolEmailInterface toolEmailInterface = new ToolEmailInterface();
        toolEmailInterface.setMailSubject("新增岗位通知 - SUNTEN人力资源管理系统");
        toolEmailInterface.setStatus("PLAN");
        toolEmailInterface.setPlannedDate(date);
        toolEmailInterface.setEmailServer(emailServer);
        List<FndJobAndDept> FndJobAndDepts = fndJobDao.listFndJobAndDept();
        int i = 0;
        for (FndJobAndDept fndJobAndDept: FndJobAndDepts)
        {
            if (fndJobAndDept.getJobClass().equals("DVC")) {
                i ++;
                mailContent = mailContent + "<p style=\"text-indent: 2em;\">" + fndJobAndDept.getDeptName() + "的" + fndJobAndDept.getJobName() + "岗位</p>";
            }
            else continue;
        }
        mailContent = mailContent +"<p style=\"text-align: center;    font-family: Times New Roman;    font-size: small;  \n" +
                "\t\t\t    color: #C60024;    padding: 20px 0px;    margin-bottom: 10px;    font-weight: bold;    background: #ebebeb;\">系统主页链接：<br>\n" +
                "\t\t\t\t点击链接进行跳转<a href='http://172.18.1.159:8016' target='_blank'>http://172.18.1.159:8016</a> </p>      \n" +
                "\t\t\t\t   <div class=\"foot-hr hr\" style=\"margin: 0 auto;    z-index: 111;    width: 800px;    margin-top: 30px;    border-top: 1px solid #DA251D;\">      \n" +
                "\t\t\t\t      </div>         <div style=\"text-align: center;    font-size: 12px;    padding: 20px 0px;    font-family: Microsoft YaHei;\">           \n" +
                "\t\t\t\t\t         Copyright &copy;2023 SUNTEN 人力资源管理系统 All Rights Reserved.         </div>      </div> </div> </body> </html> ";
        toolEmailInterface.setMailContent(mailContent);
        toolEmailInterface.setSendTo(yedongEmail);
        if (i > 0) {
            if (FndJobAndDepts.isEmpty() != true) {
                toolEmailInterfaceDao.insertAllColumn(toolEmailInterface);
            }
            toolEmailInterface.setSendTo(lixiaoEmail);
            if (FndJobAndDepts.isEmpty() != true) {
                toolEmailInterfaceDao.insertAllColumn(toolEmailInterface);
            }
        }
    }

    @Override
    public List<HashMap<String, Object>> loadAllCertificationJobList() {
        return fndJobDao.loadAllCertificationJobList();
    }


}
