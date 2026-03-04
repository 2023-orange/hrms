package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcExceptionDisposeDao;
import com.sunten.hrms.ac.domain.AcExceptionDispose;
import com.sunten.hrms.ac.domain.AcVacate;
import com.sunten.hrms.ac.dto.AcExceptionDisposeDTO;
import com.sunten.hrms.ac.dto.AcExceptionDisposeQueryCriteria;
import com.sunten.hrms.ac.dto.AcVacateDTO;
import com.sunten.hrms.ac.mapper.AcExceptionDisposeMapper;
import com.sunten.hrms.ac.service.AcExceptionDisposeService;
import com.sunten.hrms.ac.service.AcVacateService;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
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
import java.util.*;

/**
 * <p>
 * 考勤异常及处理表 服务实现类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcExceptionDisposeServiceImpl extends ServiceImpl<AcExceptionDisposeDao, AcExceptionDispose> implements AcExceptionDisposeService {
    private final AcExceptionDisposeDao acExceptionDisposeDao;
    private final AcExceptionDisposeMapper acExceptionDisposeMapper;
    private final PmEmployeeService pmEmployeeService;
    private final AcVacateService acVacateService;

    public AcExceptionDisposeServiceImpl(AcExceptionDisposeDao acExceptionDisposeDao, AcExceptionDisposeMapper acExceptionDisposeMapper, PmEmployeeService pmEmployeeService, AcVacateService acVacateService) {
        this.acExceptionDisposeDao = acExceptionDisposeDao;
        this.acExceptionDisposeMapper = acExceptionDisposeMapper;
        this.pmEmployeeService = pmEmployeeService;
        this.acVacateService = acVacateService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcExceptionDisposeDTO insert(AcExceptionDispose exceptionDisposeNew) {
        acExceptionDisposeDao.insertAllColumn(exceptionDisposeNew);
        return acExceptionDisposeMapper.toDto(exceptionDisposeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcExceptionDispose exceptionDispose = new AcExceptionDispose();
        exceptionDispose.setId(id);
        this.delete(exceptionDispose);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcExceptionDispose exceptionDispose) {
        // TODO    确认删除前是否需要做检查
        acExceptionDisposeDao.deleteByEntityKey(exceptionDispose);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcExceptionDispose exceptionDisposeNew) {
        AcExceptionDispose exceptionDisposeInDb = Optional.ofNullable(acExceptionDisposeDao.getByKey(exceptionDisposeNew.getId())).orElseGet(AcExceptionDispose::new);
        ValidationUtil.isNull(exceptionDisposeInDb.getId(), "ExceptionDispose", "id", exceptionDisposeNew.getId());
        exceptionDisposeNew.setId(exceptionDisposeInDb.getId());

        acExceptionDisposeDao.updateSomeColumnByKey(exceptionDisposeNew);


    }

    @Override
    public AcExceptionDisposeDTO getByKey(Long id) {
        AcExceptionDispose exceptionDispose = Optional.ofNullable(acExceptionDisposeDao.getByKey(id)).orElseGet(AcExceptionDispose::new);
        ValidationUtil.isNull(exceptionDispose.getId(), "ExceptionDispose", "id", id);
        return acExceptionDisposeMapper.toDto(exceptionDispose);
    }

    @Override
    public List<AcExceptionDisposeDTO> listAll(AcExceptionDisposeQueryCriteria criteria) {
        return acExceptionDisposeMapper.toDto(acExceptionDisposeDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcExceptionDisposeQueryCriteria criteria, Pageable pageable) {
        // TODO 差一个定时任务，处理包含有未通过OA单号的异常，如果该OA号已通过，则消除该异常
        Page<AcExceptionDispose> page = PageUtil.startPage(pageable);
        List<AcExceptionDispose> exceptionDisposes = acExceptionDisposeDao.listAllByCriteriaPage(page, criteria);
        List<AcExceptionDisposeDTO> acExceptionDisposeDTOS = acExceptionDisposeMapper.toDto(exceptionDisposes);
        acExceptionDisposeDTOS.forEach(exception -> {
            PmEmployeeDTO employee = pmEmployeeService.getByKey(exception.getEmployeeId());
            exception.setEmployee(employee);
            if (!exception.getDisposeFlag() && exception.getReqCode() !=null && !("").equals(exception.getReqCode().trim())) {
                // 2024-06-03 修改从OA读取请假单到HR读取
                AcVacateDTO acVacateDTO = acVacateService.getVacateByRequisitionCode(exception.getReqCode());
//                AcVacateDTO acVacateDTO = acLeaveApplicationDao.getAcVacateByRequisitionCode(exception.getReqCode());
                exception.setVacate(acVacateDTO);
            }
        });
        return PageUtil.toPage(acExceptionDisposeDTOS, page.getTotal());
    }

    @Override
    public void download(List<AcExceptionDisposeDTO> exceptionDisposeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcExceptionDisposeDTO exceptionDisposeDTO : exceptionDisposeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", exceptionDisposeDTO.getId());
            map.put("员工id", exceptionDisposeDTO.getEmployeeId());
            map.put("异常类型", exceptionDisposeDTO.getExceptionType());
            map.put("异常产生日期", exceptionDisposeDTO.getExceptionDate());
            map.put("异常详细说明", exceptionDisposeDTO.getExceptionDescribes());
            map.put("异常处理人id", exceptionDisposeDTO.getDisposeEmployeeId());
            map.put("异常处理日期", exceptionDisposeDTO.getDisposeDate());
            map.put("异常处理结果", exceptionDisposeDTO.getDisposeResult());
            map.put("异常处理完成标记", exceptionDisposeDTO.getDisposeFlag());
            map.put("相关OA审批单号", exceptionDisposeDTO.getReqCode());
            map.put("弹性域1", exceptionDisposeDTO.getAttribute1());
            map.put("弹性域2", exceptionDisposeDTO.getAttribute2());
            map.put("弹性域3", exceptionDisposeDTO.getAttribute3());
            map.put("有效标记", exceptionDisposeDTO.getEnabledFlag());
            map.put("创建人id", exceptionDisposeDTO.getCreateBy());
            map.put("创建时间", exceptionDisposeDTO.getCreateTime());
            map.put("修改人id", exceptionDisposeDTO.getUpdateBy());
            map.put("修改时间", exceptionDisposeDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
