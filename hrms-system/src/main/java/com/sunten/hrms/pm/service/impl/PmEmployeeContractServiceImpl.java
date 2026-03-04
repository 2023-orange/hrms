package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeContractDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeContract;
import com.sunten.hrms.pm.dto.PmEmployeeContractDTO;
import com.sunten.hrms.pm.dto.PmEmployeeContractQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.mapper.PmEmployeeContractMapper;
import com.sunten.hrms.pm.service.PmEmployeeContractService;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.utils.DateUtil;
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
 * 合同情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeContractServiceImpl extends ServiceImpl<PmEmployeeContractDao, PmEmployeeContract> implements PmEmployeeContractService {
    private final PmEmployeeContractDao pmEmployeeContractDao;
    private final PmEmployeeContractMapper pmEmployeeContractMapper;
    private final PmEmployeeService pmEmployeeService;

    public PmEmployeeContractServiceImpl(PmEmployeeContractDao pmEmployeeContractDao, PmEmployeeContractMapper pmEmployeeContractMapper,
                                         PmEmployeeService pmEmployeeService) {
        this.pmEmployeeContractDao = pmEmployeeContractDao;
        this.pmEmployeeContractMapper = pmEmployeeContractMapper;
        this.pmEmployeeService = pmEmployeeService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeContractDTO insert(PmEmployeeContract employeeContractNew) {
        // 插入时是否需要做最新合同判断: 如果是最新合同，则先将其它合同得最新标记更改
        if (employeeContractNew.getNewContractFlag()) {
            PmEmployeeContract newContract = pmEmployeeContractDao.getNewContractByEmployee(employeeContractNew.getEmployee().getId());
            if (newContract != null) {
                pmEmployeeContractDao.updateNewContractFlag(newContract);
            }
        }
        pmEmployeeContractDao.insertAllColumn(employeeContractNew);

        return pmEmployeeContractMapper.toDto(employeeContractNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeContract employeeContract = new PmEmployeeContract();
        employeeContract.setId(id);
        employeeContract.setEnabledFlag(false);
        this.delete(employeeContract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeContract employeeContract) {
        //确认删除前是否需要做检查，目前只失效，不删除
        pmEmployeeContractDao.updateEnableFlag(employeeContract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeContract employeeContractNew) {
        PmEmployeeContract employeeContractInDb = Optional.ofNullable(pmEmployeeContractDao.getByKey(employeeContractNew.getId())).orElseGet(PmEmployeeContract::new);
        ValidationUtil.isNull(employeeContractInDb.getId(), "EmployeeContract", "id", employeeContractNew.getId());
        employeeContractNew.setId(employeeContractInDb.getId());
        pmEmployeeContractDao.updateAllColumnByKey(employeeContractNew);
    }

    @Override
    public PmEmployeeContractDTO getByKey(Long id) {
        PmEmployeeContract employeeContract = Optional.ofNullable(pmEmployeeContractDao.getByKey(id)).orElseGet(PmEmployeeContract::new);
        ValidationUtil.isNull(employeeContract.getId(), "EmployeeContract", "id", id);
        PmEmployeeContractDTO pmEmployeeContractDTO = pmEmployeeContractMapper.toDto(employeeContract);
//        setContractEmployeeInfo(pmEmployeeContractDTO);
        return pmEmployeeContractDTO;
    }

    @Override
    public List<PmEmployeeContractDTO> getAllByemployeeId(Long employeeId) {
        List<PmEmployeeContract> employeeContracts = pmEmployeeContractDao.getAllByemployeeId(employeeId);
        List<PmEmployeeContractDTO> employeeContractDTOS = pmEmployeeContractMapper.toDto(employeeContracts);
        return employeeContractDTOS;
    }

    @Override
    public List<PmEmployeeContractDTO> listAll(PmEmployeeContractQueryCriteria criteria) {
        List<PmEmployeeContract> employeeContracts = pmEmployeeContractDao.listAllByCriteria(criteria);
        List<PmEmployeeContractDTO> employeeContractDTOS = pmEmployeeContractMapper.toDto(employeeContracts);
//        for (PmEmployeeContractDTO pecd : employeeContractDTOS) {
//            setContractEmployeeInfo(pecd);
//        }
        return employeeContractDTOS;
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeContractQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeContract> page = PageUtil.startPage(pageable);
        List<PmEmployeeContract> employeeContracts = pmEmployeeContractDao.listAllByCriteriaPage(page, criteria);
        List<PmEmployeeContractDTO> employeeContractDTOS = pmEmployeeContractMapper.toDto(employeeContracts);
//        for (PmEmployeeContractDTO pecd : employeeContractDTOS) {
//            setContractEmployeeInfo(pecd);
//        }
        return PageUtil.toPage(employeeContractDTOS, page.getTotal());
    }

    @Override
    public void download(PmEmployeeContractQueryCriteria criteria, HttpServletResponse response) throws IOException {
        List<PmEmployeeContractDTO> employeeContractDTOS = pmEmployeeContractMapper.toDto(pmEmployeeContractDao.listAllAndSignNumByCriteria(criteria));
        List<Map<String, Object>> list = new ArrayList<>();
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (PmEmployeeContractDTO employeeContractDTO : employeeContractDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            if (null != employeeContractDTO.getEmployee()) {
                map.put("工牌号", employeeContractDTO.getEmployee().getWorkCard());//工牌号
                map.put("姓名", employeeContractDTO.getEmployee().getName());//姓名
                map.put("部门", employeeContractDTO.getEmployee().getDeptName());//部门
                map.put("科室", employeeContractDTO.getEmployee().getDepartment());//科室
                map.put("班组", employeeContractDTO.getEmployee().getTeam());//班组
                map.put("岗位", null != employeeContractDTO.getEmployee().getMainJob() && null != employeeContractDTO.getEmployee().getMainJob().getJob() && null != employeeContractDTO.getEmployee().getMainJob().getJob().getJobName() ?
                                employeeContractDTO.getEmployee().getMainJob().getJob().getJobName() : ""
                        );
//            map.put("岗位", null == employeeContractDTO.getEmployee().getMainJob() ? "" : employeeContractDTO.getEmployee().getMainJob().getJob().getJobName());//岗位
                map.put("入职时间", null != employeeContractDTO.getEmployee() && null != employeeContractDTO.getEmployee().getEmployeeEntry() && null != employeeContractDTO.getEmployee().getEmployeeEntry().getEntryTime()
                        ?  DateUtil.localDateToStr( employeeContractDTO.getEmployee().getEmployeeEntry().getEntryTime()) :  "");//入职时间
                map.put("合同次序", employeeContractDTO.getQuantity());
                map.put("合同开始时间", null != employeeContractDTO.getStartTime() ? DateUtil.localDateToStr(employeeContractDTO.getStartTime().toLocalDate()) : "" );//合同开始时间
                map.put("合同结束时间", null != employeeContractDTO.getEndTime() ? DateUtil.localDateToStr(employeeContractDTO.getEndTime().toLocalDate()) : "");//合同结束时间
//            map.put("合同结束时间", null == employeeContractDTO.getEndTime() ? "" : employeeContractDTO.getStartTime().format(fmt));//合同结束时间
                map.put("合同期限", employeeContractDTO.getContractValidity());//合同期限
                list.add(map);
            }
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PmEmployeeContractDTO> batchInsert(List<PmEmployeeContract> pmEmployeeContracts, Long employeeId) {
        for (PmEmployeeContract pec : pmEmployeeContracts) {
            if (employeeId != null) {
                if (pec.getEmployee() == null) {
                    pec.setEmployee(new PmEmployee());
                }
                pec.getEmployee().setId(employeeId);
            }
            if (pec.getEmployee() == null || pec.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pec.getId() != null) {
                if (pec.getId().equals(-1L)) {
                    pmEmployeeContractDao.insertAllColumn(pec);
                } else {
                    pmEmployeeContractDao.updateAllColumnByKey(pec);
                }
            } else {
                throw new InfoCheckWarningMessException("合同批量插入时ID不能为空");
            }
        }
        return pmEmployeeContractMapper.toDto(pmEmployeeContracts);
    }

    /**
     *  @author：liangjw
     *  @Date: 2021/3/11 15:06
     *  @Description: 合同分页查询
     */
    @Override
    public Map<String, Object> listAllAndSignNumByPage(PmEmployeeContractQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeContract> page = PageUtil.startPage(pageable);
        List<PmEmployeeContract> employeeContracts = pmEmployeeContractDao.listAllAndSignNumByCriteriaPage(page, criteria);
        List<PmEmployeeContractDTO> employeeContractDTOS = pmEmployeeContractMapper.toDto(employeeContracts);
        return PageUtil.toPage(employeeContractDTOS, page.getTotal());
    }

    @Override
    public List<PmEmployeeContractDTO> listAllAndSignNum(PmEmployeeContractQueryCriteria criteria) {
        return pmEmployeeContractMapper.toDto(pmEmployeeContractDao.listAllAndSignNumByCriteria(criteria));
    }

    @Override
    public PmEmployeeContractDTO getNewestContractByEmployeeId(Long employeeId) {
        return pmEmployeeContractMapper.toDto(pmEmployeeContractDao.getNewestContractByEmployeeId(employeeId));
    }
}
