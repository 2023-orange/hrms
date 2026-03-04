package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.pm.dao.PmEmployeeJobDao;
import com.sunten.hrms.pm.dao.PmEmployeeJobTransferDao;
import com.sunten.hrms.pm.dao.PmTransferRequestDao;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.domain.PmEmployeeJobTransfer;
import com.sunten.hrms.pm.dto.PmEmployeeJobTransferDTO;
import com.sunten.hrms.pm.dto.PmEmployeeJobTransferQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeJobTransferMapper;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
import com.sunten.hrms.pm.service.PmEmployeeJobTransferService;
import com.sunten.hrms.pm.service.PmTransferRequestService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 岗位调动表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeJobTransferServiceImpl extends ServiceImpl<PmEmployeeJobTransferDao, PmEmployeeJobTransfer> implements PmEmployeeJobTransferService {
    private final String PERMANENT_TRANSFER = "永久调动";
    private final String TEMPORARILY_TRANSFER = "期限调动";
    private final String ADD_JOB = "新增岗位";
    private final String DISABLE_JOB = "失效岗位";
    private final String TRANSFER_BACK = "期限调动返回";
    private final String ORG_ADJ = "机构调整";

    private final String STATE_PLAN = "PLAN";
    private final String STATE_FINISHED = "FINISHED";
    private final String STATE_TRANSFERRING = "TRANSFERRING";
    private final String STATE_CLOSED = "CLOSED";
    private final String STATE_ERROR = "ERROR";

    private final PmEmployeeJobTransferDao pmEmployeeJobTransferDao;
    private final PmEmployeeJobTransferMapper pmEmployeeJobTransferMapper;
    private final PmEmployeeJobDao pmEmployeeJobDao;
    private final PmEmployeeJobService pmEmployeeJobService;
    private final FndDeptService fndDeptService;
    private final PmTransferRequestDao pmTransferRequestDao;

    public PmEmployeeJobTransferServiceImpl(PmEmployeeJobTransferDao pmEmployeeJobTransferDao, PmEmployeeJobTransferMapper pmEmployeeJobTransferMapper, PmEmployeeJobDao pmEmployeeJobDao, PmEmployeeJobService pmEmployeeJobService, FndDeptService fndDeptService,PmTransferRequestDao pmTransferRequestDao) {
        this.pmEmployeeJobTransferDao = pmEmployeeJobTransferDao;
        this.pmTransferRequestDao = pmTransferRequestDao;
        this.pmEmployeeJobTransferMapper = pmEmployeeJobTransferMapper;
        this.pmEmployeeJobDao = pmEmployeeJobDao;
        this.pmEmployeeJobService = pmEmployeeJobService;
        this.fndDeptService = fndDeptService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeJobTransferDTO insert(PmEmployeeJobTransfer employeeJobTransferNew) {
        if (employeeJobTransferNew.getOldJob() != null || employeeJobTransferNew.getNewJob() != null) {
            Long oldJobId = -1L;
            Long newJobId = -1L;
            if (employeeJobTransferNew.getOldJob() != null && employeeJobTransferNew.getOldJob().getId() != null) {
                oldJobId = employeeJobTransferNew.getOldJob().getId();
            }
            if (employeeJobTransferNew.getNewJob() != null && employeeJobTransferNew.getNewJob().getId() != null) {
                newJobId = employeeJobTransferNew.getNewJob().getId();
            }
            switch (employeeJobTransferNew.getTransferType()) {
                case ADD_JOB:
                    if (!oldJobId.equals(-1L)) {
                        //数据有问题
                        throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferType() + "的原岗位不能为非空，请检查！");
                    }
                    if (newJobId.equals(-1L)) {
                        //数据有问题
                        throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferType() + "的新岗位不能为空，请检查！");
                    }
                    break;
                case DISABLE_JOB:
                    if (oldJobId.equals(-1L)) {
                        //数据有问题
                        throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferType() + "的原岗位不能为空，请检查！");
                    }
                    if (!newJobId.equals(-1L)) {
                        //数据有问题
                        throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferType() + "的新岗位不能为非空，请检查！");
                    }
                    break;
                case TEMPORARILY_TRANSFER:
                    if (employeeJobTransferNew.getEndTime() == null) {
                        //数据有问题
                        throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferType() + "的终止时间不能为空，请检查！");
                    }
                case PERMANENT_TRANSFER:
                    if (oldJobId.equals(-1L)) {
                        //数据有问题
                        throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferType() + "的原岗位不能为空，请检查！");
                    }
                    if (newJobId.equals(-1L)) {
                        //数据有问题
                        throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferType() + "的新岗位不能为空，请检查！");
                    }
                    break;
                default:
                    throw new InfoCheckWarningMessException("不支持的调动类型" + employeeJobTransferNew.getTransferType() + "，请检查！");
            }
        } else {
            throw new InfoCheckWarningMessException("检测不到新旧岗位信息，请检查！");
        }

        // 检查是否存在未生效的岗位调动或正在执行的借调记录
        if (!employeeJobTransferNew.getTransferType().equals(ADD_JOB)
                && isPlanOrTransferring(employeeJobTransferNew.getEmployee().getId(), employeeJobTransferNew.getGroupId())) {
            throw new InfoCheckWarningMessException("该员工存在未生效的岗位调动或正在执行的期限调动记录！");
        }

        employeeJobTransferNew.setTransferState(STATE_PLAN);
        // 根据岗位调动分类进行信息检查
        checkAndTransfer(employeeJobTransferNew, LocalDate.now());

        if (employeeJobTransferNew.getTransferState().equals(STATE_ERROR)) {
            throw new InfoCheckWarningMessException(employeeJobTransferNew.getTransferError());
        }
        // 插入调岗记录
        employeeJobTransferNew.setEnabledFlag(true);
        pmEmployeeJobTransferDao.insertAllColumn(employeeJobTransferNew);
        if (employeeJobTransferNew.getTransferType().equals(this.TEMPORARILY_TRANSFER)
                && employeeJobTransferNew.getEndTime() != null
                && employeeJobTransferNew.getEndTime().isBefore(LocalDate.now())) {
            transferCheckAndBack(employeeJobTransferNew);
        }
        pmTransferRequestDao.updateAttribute5(employeeJobTransferNew);
        return pmEmployeeJobTransferMapper.toDto(employeeJobTransferNew);
    }

    private void checkAndTransfer(PmEmployeeJobTransfer employeeJobTransfer, LocalDate localDate) {
        PmEmployeeJob oldJob = new PmEmployeeJob();
        PmEmployeeJob jobInDB = new PmEmployeeJob();
        jobInDB.setEmployee(employeeJobTransfer.getEmployee());
        jobInDB.setGroupId(employeeJobTransfer.getGroupId());

        switch (employeeJobTransfer.getTransferType()) {
            case ADD_JOB:
                jobInDB.setJob(employeeJobTransfer.getNewJob());
                jobInDB = pmEmployeeJobDao.getByEmployeeJob(jobInDB);
                if (jobInDB != null) {
                    employeeJobTransfer.setTransferState(STATE_ERROR);
                    employeeJobTransfer.setTransferError("该员工已在岗位【" + employeeJobTransfer.getNewJob().getJobName() + "】任职，无法重复任职！");
                }
                break;
            case DISABLE_JOB:
                jobInDB.setJob(employeeJobTransfer.getOldJob());
                oldJob = pmEmployeeJobDao.getByEmployeeJob(jobInDB);//根据员工ID和岗位ID获取 岗位关系表，从而获取岗位关系ID
                if (oldJob == null) {
                    employeeJobTransfer.setTransferState(STATE_ERROR);
                    employeeJobTransfer.setTransferError("该员工未在岗位【" + employeeJobTransfer.getOldJob().getJobName() + "】任职！");
                } else if (oldJob.getJobMainFlag()) {
                    employeeJobTransfer.setTransferState(STATE_ERROR);
                    employeeJobTransfer.setTransferError("该员工的岗位【" + employeeJobTransfer.getOldJob().getJobName() + "】是主岗位，无法直接失效，请先设立新的主岗位");
                }
                break;
            case PERMANENT_TRANSFER:
            case TEMPORARILY_TRANSFER:
                jobInDB.setJob(employeeJobTransfer.getNewJob());
                jobInDB = pmEmployeeJobDao.getByEmployeeJob(jobInDB);
                if (jobInDB != null) {
                    employeeJobTransfer.setTransferState(STATE_ERROR);
                    employeeJobTransfer.setTransferError("该员工已在岗位【" + employeeJobTransfer.getNewJob().getJobName() + "】任职，无法重复任职！");
                }
                jobInDB = new PmEmployeeJob();
                jobInDB.setEmployee(employeeJobTransfer.getEmployee());
                jobInDB.setJob(employeeJobTransfer.getOldJob());
                oldJob = pmEmployeeJobDao.getByEmployeeJob(jobInDB);
                if (oldJob == null) {
                    employeeJobTransfer.setTransferState(STATE_ERROR);
                    employeeJobTransfer.setTransferError("该员工未在岗位【" + employeeJobTransfer.getOldJob().getJobName() + "】任职！");
                }
                break;
            default:
                employeeJobTransfer.setTransferState(STATE_ERROR);
                employeeJobTransfer.setTransferError("该调动类型不在程序支持范围！");
                break;
        }

        if (employeeJobTransfer.getTransferState().equals(STATE_PLAN)) {
            if (employeeJobTransfer.getId() == null || employeeJobTransfer.getId().equals(-1L)) {
                if (this.ADD_JOB.equals(employeeJobTransfer.getTransferType())) {
                    employeeJobTransfer.setGroupId(-1L);//存放旧岗位id
                } else {
                    employeeJobTransfer.setGroupId(oldJob.getGroupId());//存放旧岗位id
                }
            }

            PmEmployeeJob newJob = new PmEmployeeJob();
            newJob.setEmployee(employeeJobTransfer.getEmployee());
            // 若岗位调动起始时间不晚于当前时间，直接修改岗位信息
            if (!employeeJobTransfer.getStartTime().isAfter(localDate)) {
                employeeJobTransfer.setTransferState(STATE_FINISHED);
                switch (employeeJobTransfer.getTransferType()) {
                    case ADD_JOB:
                        newJob.setDept(employeeJobTransfer.getNewDept());
                        newJob.setJob(employeeJobTransfer.getNewJob());
                        Long groupId = pmEmployeeJobService.getMaxGroupId();
                        newJob.setGroupId(groupId);
                        newJob.setJobMainFlag(false);
                        newJob.setEnabledFlag(true);
                        employeeJobTransfer.setGroupId(groupId);
                        if (employeeJobTransfer.getEndTime() != null) {
                            employeeJobTransfer.setTransferState(STATE_TRANSFERRING);
                        }
                        pmEmployeeJobDao.insertAllColumn(newJob);
                        break;
                    case DISABLE_JOB:
                        oldJob.setEnabledFlag(false);
                        pmEmployeeJobDao.updateEnableFlagByKey(oldJob);
                        break;
                    case TEMPORARILY_TRANSFER:
                        employeeJobTransfer.setTransferState(STATE_TRANSFERRING);
                    case PERMANENT_TRANSFER:
                        newJob.setJob(employeeJobTransfer.getNewJob());
                        newJob.setDept(employeeJobTransfer.getNewDept());
                        newJob.setGroupId(oldJob.getGroupId());
                        newJob.setJobMainFlag(oldJob.getJobMainFlag());
                        newJob.setEnabledFlag(true);
                        oldJob.setEnabledFlag(false);
                        pmEmployeeJobDao.updateEnableFlagByKey(oldJob);// 失效原岗位关系
                        pmEmployeeJobDao.insertAllColumn(newJob);// 新增主岗位关系
                        break;
                    default:
                        employeeJobTransfer.setTransferError("该调动类型不在程序支持范围！");
                        break;
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeJobTransfer employeeJobTransferInDb = pmEmployeeJobTransferDao.getByKey(id);
        ValidationUtil.isNull(employeeJobTransferInDb.getId(), "EmployeeJobTransfer", "id", id);
        employeeJobTransferInDb.setId(id);
        employeeJobTransferInDb.setEnabledFlag(false);
        this.delete(employeeJobTransferInDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeJobTransfer employeeJobTransfer) {
        //  确认删除前是否需要做检查，只失效，不删除
        if (employeeJobTransfer.getTransferState().equals(STATE_PLAN)) {
            pmEmployeeJobTransferDao.updateEnableFlag(employeeJobTransfer);
        } else {
            throw new InfoCheckWarningMessException("该岗位调动状态已变更，无法失效该岗位调动！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeJobTransfer employeeJobTransferNew) {
        PmEmployeeJobTransfer employeeJobTransferInDb = Optional.ofNullable(pmEmployeeJobTransferDao.getByKey(employeeJobTransferNew.getId())).orElseGet(PmEmployeeJobTransfer::new);
        ValidationUtil.isNull(employeeJobTransferInDb.getId(), "EmployeeJobTransfer", "id", employeeJobTransferNew.getId());
        employeeJobTransferNew.setId(employeeJobTransferInDb.getId());
        pmEmployeeJobTransferDao.updateAllColumnByKey(employeeJobTransferNew);
    }

    @Override
    public PmEmployeeJobTransferDTO getByKey(Long id) {
        PmEmployeeJobTransfer employeeJobTransfer = Optional.ofNullable(pmEmployeeJobTransferDao.getByKey(id)).orElseGet(PmEmployeeJobTransfer::new);
        ValidationUtil.isNull(employeeJobTransfer.getId(), "EmployeeJobTransfer", "id", id);
        return pmEmployeeJobTransferMapper.toDto(employeeJobTransfer);
    }

    @Override
    public List<PmEmployeeJobTransferDTO> listAll(PmEmployeeJobTransferQueryCriteria criteria) {
        List<PmEmployeeJobTransfer> employeeJobTransfers = pmEmployeeJobTransferDao.listAllByCriteria(criteria);
        return pmEmployeeJobTransferMapper.toDto(employeeJobTransfers);
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeJobTransferQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeJobTransfer> page = PageUtil.startPage(pageable);
        List<PmEmployeeJobTransfer> employeeJobTransfers = pmEmployeeJobTransferDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeJobTransferMapper.toDto(employeeJobTransfers), page.getTotal());
    }

    @Override
    public void download(PmEmployeeJobTransferQueryCriteria criteria, Pageable pageable, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Page<PmEmployeeJobTransfer> page = PageUtil.startPage(pageable);
        List<PmEmployeeJobTransfer> employeeJobTransfers = pmEmployeeJobTransferDao.listAllByCriteriaPage(page, criteria);

        for (PmEmployeeJobTransfer employeeJobTransfer : employeeJobTransfers) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("工牌号", employeeJobTransfer.getEmployee().getWorkCard());
            map.put("姓名", employeeJobTransfer.getEmployee().getName());
            map.put("原部门", null == employeeJobTransfer.getOldDept() ? "" : employeeJobTransfer.getOldDept().getExtDeptName());
            map.put("原科室", null == employeeJobTransfer.getOldDept() ? "" : employeeJobTransfer.getOldDept().getExtDepartmentName());
            map.put("原班组", null == employeeJobTransfer.getOldDept() ? "" : employeeJobTransfer.getOldDept().getExtTeamName());
            map.put("原岗位", employeeJobTransfer.getOldJob() == null ? "" : employeeJobTransfer.getOldJob().getJobName());
            map.put("新部门", employeeJobTransfer.getNewDept() == null ? "" : employeeJobTransfer.getNewDept().getExtDeptName());
            map.put("新科室", employeeJobTransfer.getNewDept() == null ? "" : employeeJobTransfer.getNewDept().getExtDepartmentName());
            map.put("新班组", employeeJobTransfer.getNewDept() == null ? "" : employeeJobTransfer.getNewDept().getExtTeamName());
            map.put("新岗位", employeeJobTransfer.getNewJob() == null ? "" : employeeJobTransfer.getNewJob().getJobName());
            map.put("调动类别", employeeJobTransfer.getTransferType());
            map.put("调动类型", employeeJobTransfer.getTransferForm());
            map.put("调动开始时间", null == employeeJobTransfer.getStartTime() ? "" : employeeJobTransfer.getStartTime().format(fmt));
            map.put("调动结束时间", null == employeeJobTransfer.getEndTime() ? "" : employeeJobTransfer.getEndTime().format(fmt));
            map.put("调动原因", employeeJobTransfer.getTransferReason());
            map.put("备注", employeeJobTransfer.getRemarks());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public boolean isPlanOrTransferring(Long employeeId, Long groupId) {
        List<PmEmployeeJobTransfer> planOrTransferringTransferList = pmEmployeeJobTransferDao.
                listPlanOrTransferringTransferByEmployeeId(employeeId, groupId);
        return (planOrTransferringTransferList != null && planOrTransferringTransferList.size() > 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(PmEmployeeJobTransfer employeeJobTransferNew) {
        PmEmployeeJobTransfer employeeJobTransferInDb = Optional.ofNullable(pmEmployeeJobTransferDao.getByKey(employeeJobTransferNew.getId())).orElseGet(PmEmployeeJobTransfer::new);
        ValidationUtil.isNull(employeeJobTransferInDb.getId(), "EmployeeJobTransfer", "id", employeeJobTransferNew.getId());
        String statue = employeeJobTransferInDb.getTransferState();
        if (employeeJobTransferInDb.getEndTime() != null && (statue.equals(STATE_TRANSFERRING) || statue.equals(STATE_PLAN)) && employeeJobTransferInDb.getEnabledFlag()) {
            if (employeeJobTransferInDb.getEndTime().isEqual(employeeJobTransferNew.getEndTime())) {
                throw new InfoCheckWarningMessException("结束日期没变化，无需保存！");
            }
            if (employeeJobTransferNew.getEndTime().isBefore(employeeJobTransferInDb.getStartTime())) {
                throw new InfoCheckWarningMessException("结束日期不能早于起始日期！");
            }
            employeeJobTransferInDb.setTransferState(STATE_CLOSED);
            employeeJobTransferInDb.setEnabledFlag(false);
            pmEmployeeJobTransferDao.updateAllColumnByKey(employeeJobTransferInDb);
            employeeJobTransferInDb.setTransferState(statue);
            employeeJobTransferInDb.setEndTime(employeeJobTransferNew.getEndTime());
            employeeJobTransferInDb.setEnabledFlag(true);
            employeeJobTransferInDb.setAttribute1("变更结束日期，原调动ID:" + employeeJobTransferInDb.getId());
            pmEmployeeJobTransferDao.insertAllColumn(employeeJobTransferInDb);

            if (statue.equals(STATE_TRANSFERRING) && employeeJobTransferInDb.getEndTime().isBefore(LocalDate.now())) {
                transferCheckAndBack(employeeJobTransferInDb);
            }
        } else {
            throw new InfoCheckWarningMessException("调动状态有误，修改失败，请刷新页面再试！");
        }
        if (employeeJobTransferInDb.getTransferState().equals(STATE_ERROR)) {
            throw new InfoCheckWarningMessException(employeeJobTransferInDb.getTransferError());
        }
    }

    private void transferCheckAndBack(PmEmployeeJobTransfer employeeJobTransfer) {
        PmEmployeeJob jobInDB = new PmEmployeeJob();
        jobInDB.setEmployee(employeeJobTransfer.getEmployee());
        jobInDB.setJob(employeeJobTransfer.getNewJob());
        PmEmployeeJob oldJob = pmEmployeeJobDao.getByEmployeeJob(jobInDB);
        if (oldJob == null) {
            employeeJobTransfer.setTransferState(STATE_ERROR);
            employeeJobTransfer.setTransferError("该员工当前不在【" + employeeJobTransfer.getNewJob().getJobName() + "】任职！");
            pmEmployeeJobTransferDao.updateAllColumnByKey(employeeJobTransfer);
        } else {
            // 标记原调动记录状态为 完成“FINISHED”
            employeeJobTransfer.setTransferState(STATE_FINISHED);
            pmEmployeeJobTransferDao.updateAllColumnByKey(employeeJobTransfer);

            // 失效原岗位关系
            oldJob.setEnabledFlag(false);
            pmEmployeeJobDao.updateEnableFlagByKey(oldJob);

            if (employeeJobTransfer.getTransferType().equals(TEMPORARILY_TRANSFER)) {
                // 新增相应期限调动结束返回原岗位的岗位调动记录
                FndDeptDTO primaryNewDeptDTO = fndDeptService.getByKey(employeeJobTransfer.getNewDept().getId());
                FndDept primaryNewDept = new FndDept()
                        .setId(primaryNewDeptDTO.getId())
                        .setDeptName(primaryNewDeptDTO.getDeptName())
                        .setExtDeptName(primaryNewDeptDTO.getExtDeptName())
                        .setExtDepartmentName(primaryNewDeptDTO.getExtDepartmentName())
                        .setExtTeamName(primaryNewDeptDTO.getExtTeamName());
                FndJob primaryNewJob = employeeJobTransfer.getNewJob();
                FndDeptDTO primaryOldDeptDTO = fndDeptService.getByKey(employeeJobTransfer.getOldDept().getId());
                FndDept primaryOldDept = new FndDept()
                        .setId(primaryOldDeptDTO.getId())
                        .setDeptName(primaryOldDeptDTO.getDeptName())
                        .setExtDeptName(primaryOldDeptDTO.getExtDeptName())
                        .setExtDepartmentName(primaryOldDeptDTO.getExtDepartmentName())
                        .setExtTeamName(primaryOldDeptDTO.getExtTeamName());
                FndJob primaryOldJob = employeeJobTransfer.getOldJob();

                employeeJobTransfer.setNewDept(primaryOldDept);
                employeeJobTransfer.setNewJob(primaryOldJob);
                employeeJobTransfer.setOldDept(primaryNewDept);
                employeeJobTransfer.setOldJob(primaryNewJob);
                employeeJobTransfer.setStartTime(employeeJobTransfer.getEndTime().plusDays(1));
                employeeJobTransfer.setEndTime(null);
                employeeJobTransfer.setAttribute1("期限调动结束返回原部门，期限调动ID:" + employeeJobTransfer.getId());
                employeeJobTransfer.setTransferType(this.TRANSFER_BACK);
                String extOldDeptStr = "{" + primaryOldDept.getExtDeptName() + "," + primaryOldDept.getExtDepartmentName() + "," + primaryOldDept.getExtTeamName() + "}";
                String primaryOldDeptStr = "{" + employeeJobTransfer.getOldDept().getExtDeptName() + "," + employeeJobTransfer.getOldDept().getExtDepartmentName() + "," + employeeJobTransfer.getOldDept().getExtTeamName() + "}";
                if (!extOldDeptStr.equals(primaryOldDeptStr)) {
                    employeeJobTransfer.setRemarks(employeeJobTransfer.getRemarks() + "\n原部门机构变化：" + primaryOldDeptStr + " => " + extOldDeptStr);
                }
                pmEmployeeJobTransferDao.insertAllColumn(employeeJobTransfer);

                // 新增岗位关系
                PmEmployeeJob newJob = new PmEmployeeJob();
                newJob.setEmployee(employeeJobTransfer.getEmployee());
                newJob.setJob(employeeJobTransfer.getNewJob());
                newJob.setDept(employeeJobTransfer.getNewDept());
                newJob.setGroupId(oldJob.getGroupId());
                newJob.setJobMainFlag(oldJob.getJobMainFlag());
                newJob.setEnabledFlag(true);
                pmEmployeeJobDao.insertAllColumn(newJob);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishTransfer(LocalDate date) {
        // 查询结束日期早于date的期限调动
        PmEmployeeJobTransferQueryCriteria criteria = new PmEmployeeJobTransferQueryCriteria();
        criteria.setTaskTransferEndTime(date);
        criteria.setEnabled(true);
        List<PmEmployeeJobTransfer> transfers = pmEmployeeJobTransferDao.listAllByCriteria(criteria);
        transfers.forEach(this::transferCheckAndBack);

        // 查询起始时间早于今天的调动
        criteria = new PmEmployeeJobTransferQueryCriteria();
        criteria.setTaskTransferStartTime(date);
        criteria.setEnabled(true);
        transfers = pmEmployeeJobTransferDao.listAllByCriteria(criteria);
        transfers.forEach((transfer) -> {
            this.checkAndTransfer(transfer, date);
            pmEmployeeJobTransferDao.updateAllColumnByKey(transfer);
        });
    }
}
