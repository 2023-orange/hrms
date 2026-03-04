package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.dao.PmEmployeeLeaveofficeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeLeaveoffice;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeLeaveofficeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeLeaveofficeQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeLeaveofficeMapper;
import com.sunten.hrms.pm.service.PmEmployeeLeaveofficeService;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 离职记录表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeLeaveofficeServiceImpl extends ServiceImpl<PmEmployeeLeaveofficeDao, PmEmployeeLeaveoffice> implements PmEmployeeLeaveofficeService {
    private final PmEmployeeLeaveofficeDao pmEmployeeLeaveofficeDao;
    private final PmEmployeeLeaveofficeMapper pmEmployeeLeaveofficeMapper;
    private final PmEmployeeService pmEmployeeService;
    private final PmEmployeeDao pmEmployeeDao;

    public PmEmployeeLeaveofficeServiceImpl(PmEmployeeLeaveofficeDao pmEmployeeLeaveofficeDao, PmEmployeeLeaveofficeMapper pmEmployeeLeaveofficeMapper, PmEmployeeService pmEmployeeService, PmEmployeeDao pmEmployeeDao) {
        this.pmEmployeeLeaveofficeDao = pmEmployeeLeaveofficeDao;
        this.pmEmployeeLeaveofficeMapper = pmEmployeeLeaveofficeMapper;
        this.pmEmployeeService = pmEmployeeService;
        this.pmEmployeeDao = pmEmployeeDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeLeaveofficeDTO insert(PmEmployeeLeaveoffice employeeLeaveofficeNew) {
        //添加离职记录时将员工离职标记设为true
        PmEmployee employee = employeeLeaveofficeNew.getEmployee();
        PmEmployee leaveEmp = pmEmployeeDao.getByKey(employee.getId(), null);
        PmEmployeeLeaveoffice oldleave = pmEmployeeLeaveofficeDao.getByEmployeeId(employee.getId());
        if (oldleave != null && oldleave.getId() != null) {
            throw new InfoCheckWarningMessException("该员工已存在离职记录，无法重复离职!");
        }
        if (leaveEmp.getLeaveFlag()) {
            throw new InfoCheckWarningMessException("该员工已离职，请刷新后重试!");
        }
        if (leaveEmp.getEmployeeEntry() != null && leaveEmp.getEmployeeEntry().getEntryTime() != null) {
            if (employeeLeaveofficeNew.getQuitTime().isBefore(leaveEmp.getEmployeeEntry().getEntryTime())) {
                throw new InfoCheckWarningMessException("离职时间不能早于入职时间!");
            }
        }
        employee.setLeaveFlag(true);
        pmEmployeeDao.updateLeaveFlag(employee);
        pmEmployeeLeaveofficeDao.insertAllColumn(employeeLeaveofficeNew);
        return pmEmployeeLeaveofficeMapper.toDto(employeeLeaveofficeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeLeaveoffice employeeLeaveoffice = new PmEmployeeLeaveoffice();
        employeeLeaveoffice.setId(id);
        employeeLeaveoffice.setEnabledFlag(false);
        this.delete(employeeLeaveoffice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeLeaveoffice employeeLeaveoffice) {
        //  确认删除前是否需要做检查,只失效，不删除
        pmEmployeeLeaveofficeDao.updateEnableFlag(employeeLeaveoffice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeLeaveoffice employeeLeaveofficeNew) {
        PmEmployeeLeaveoffice employeeLeaveofficeInDb = Optional.ofNullable(pmEmployeeLeaveofficeDao.getByKey(employeeLeaveofficeNew.getId())).orElseGet(PmEmployeeLeaveoffice::new);
        ValidationUtil.isNull(employeeLeaveofficeInDb.getId(), "EmployeeLeaveoffice", "id", employeeLeaveofficeNew.getId());
        employeeLeaveofficeNew.setId(employeeLeaveofficeInDb.getId());
        pmEmployeeLeaveofficeDao.updateAllColumnByKey(employeeLeaveofficeNew);
    }

    @Override
    public PmEmployeeLeaveofficeDTO getByKey(Long id) {
        PmEmployeeLeaveoffice employeeLeaveoffice = Optional.ofNullable(pmEmployeeLeaveofficeDao.getByKey(id)).orElseGet(PmEmployeeLeaveoffice::new);
        ValidationUtil.isNull(employeeLeaveoffice.getId(), "EmployeeLeaveoffice", "id", id);
        return pmEmployeeLeaveofficeMapper.toDto(employeeLeaveoffice);
    }

    @Override
    public List<PmEmployeeLeaveofficeDTO> listAll(PmEmployeeLeaveofficeQueryCriteria criteria) {
        //查看参数deptName是否存在，存在则根据该参数获取部门下的人员ID
        if (criteria != null && criteria.getDeptName() != null && !"".equals(criteria.getDeptName().trim())) {
            // TODO 获取人员集合后插入查询条件的employeeIds中
        }
        List<PmEmployeeLeaveofficeDTO> pmEmployeeLeaveofficeDTOS = pmEmployeeLeaveofficeMapper.toDto(pmEmployeeLeaveofficeDao.listAllByCriteria(criteria));
        for (PmEmployeeLeaveofficeDTO peld : pmEmployeeLeaveofficeDTOS) {
            setOtherEmployees(peld);
        }
        return pmEmployeeLeaveofficeDTOS;
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeLeaveofficeQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeLeaveoffice> page = PageUtil.startPage(pageable);
        // 新增查询参数，本表：离职时间、离职备注、岗位、是否办妥手续、部门、科室、班组、
        // 员工主表：工牌、姓名、性别、出生日期、身份证号、年龄、是否返聘
        List<PmEmployeeLeaveoffice> employeeLeaveoffices = pmEmployeeLeaveofficeDao.listAllByCriteriaPage(page, criteria);
        List<PmEmployeeLeaveofficeDTO> pmEmployeeLeaveofficeDTOS = pmEmployeeLeaveofficeMapper.toDto(employeeLeaveoffices);
        for (PmEmployeeLeaveofficeDTO peld : pmEmployeeLeaveofficeDTOS) {
            setOtherEmployees(peld);
        }
        return PageUtil.toPage(pmEmployeeLeaveofficeDTOS, page.getTotal());
    }

    @Override
    public void download(PmEmployeeLeaveofficeQueryCriteria criteria, Pageable pageable, HttpServletResponse response) throws IOException {
        Page<PmEmployeeLeaveoffice> page = PageUtil.startPage(pageable);
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<PmEmployeeLeaveofficeDTO> employeeLeaveofficeDTOS = pmEmployeeLeaveofficeMapper.toDto(pmEmployeeLeaveofficeDao.listAllByCriteria(criteria));
        for (PmEmployeeLeaveofficeDTO employeeLeaveofficeDTO : employeeLeaveofficeDTOS) {
            setOtherEmployees(employeeLeaveofficeDTO);
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", employeeLeaveofficeDTO.getEmployee().getWorkCard());//工牌号
            map.put("姓名", employeeLeaveofficeDTO.getEmployee().getName());//姓名
            map.put("离职时间", null == employeeLeaveofficeDTO.getQuitTime() ? "" : employeeLeaveofficeDTO.getQuitTime().format(fmt));//离职时间
            map.put("离职备注", employeeLeaveofficeDTO.getRemarks());//离职备注
            map.put("是否办妥手续", employeeLeaveofficeDTO.getProceduresFlag() ? "是" : "否");//是否办妥手续
            map.put("离职类型", employeeLeaveofficeDTO.getLeaveType());//离职类型
            map.put("返聘时间", null == employeeLeaveofficeDTO.getRehire() ? "" : employeeLeaveofficeDTO.getRehire().getStartTime().format(fmt));//返聘时间
            map.put("返聘备注", null == employeeLeaveofficeDTO.getRehire() ? "" : employeeLeaveofficeDTO.getRehire().getRemarks());//返聘备注
            map.put("部门", employeeLeaveofficeDTO.getEmployee().getDeptName());//部门
            map.put("科室", employeeLeaveofficeDTO.getEmployee().getDepartment());//科室
            map.put("班组", employeeLeaveofficeDTO.getEmployee().getTeam());//班组
            map.put("岗位", null == employeeLeaveofficeDTO.getEmployee().getMainJob() ? "" : employeeLeaveofficeDTO.getEmployee().getMainJob().getJob().getJobName());//岗位
            map.put("最高学历", employeeLeaveofficeDTO.getEmployee().getBetterEducation().getEducation());//最高学历
            map.put("最高职称", employeeLeaveofficeDTO.getEmployee().getBetterTitle().getTitleName());//最高职称
            map.put("性别", employeeLeaveofficeDTO.getEmployee().getGender());//性别
//            map.put("年龄", employeeLeaveofficeDTO.getEmployee().getAge());//年龄
            map.put("婚姻状态", employeeLeaveofficeDTO.getEmployee().getMaritalStatus());//婚姻状态
            map.put("出生日期", null == employeeLeaveofficeDTO.getEmployee().getBirthday() ? "" : employeeLeaveofficeDTO.getEmployee().getBirthday().format(fmt));//出生日期
            map.put("身份证号", employeeLeaveofficeDTO.getEmployee().getIdNumber());//身份证号
            map.put("公司工龄（月）", employeeLeaveofficeDTO.getEmployee().getCompanyAge());//工龄
            map.put("社保工龄（月）", employeeLeaveofficeDTO.getEmployee().getWorkedYears());//工龄
            map.put("入职时间", null == employeeLeaveofficeDTO.getEmployee().getEmployeeEntry().getEntryTime() ? "" : employeeLeaveofficeDTO.getEmployee().getEmployeeEntry().getEntryTime().format(fmt));//入职时间
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

//    /**
//     * 获取员工的基本属性
//     */
//    public void setContractEmployeeInfo1(PmEmployeeLeaveofficeDTO pelDTO) {
//        Long employeeId = pelDTO.getEmployee().getId();
//        PmEmployeeDTO pm = pmEmployeeService.getByKey(employeeId);
//        pelDTO.setEmployee(pm);
//    }

    /**
     * 获取员工的基本属性
     */
    public void setOtherEmployees(PmEmployeeLeaveofficeDTO pelDTO) {
        PmEmployeeDTO employeeDTO = pelDTO.getEmployee();
        employeeDTO.setOtherEmployees(pmEmployeeService.listOtherEmployees(employeeDTO.getIdNumber(), employeeDTO.getId()));
        pelDTO.setEmployee(employeeDTO);
    }
}
