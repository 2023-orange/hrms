package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeAwardDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeAward;
import com.sunten.hrms.pm.dto.PmEmployeeAwardDTO;
import com.sunten.hrms.pm.dto.PmEmployeeAwardQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeAwardMapper;
import com.sunten.hrms.pm.service.PmEmployeeAwardService;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 奖罚情况表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeAwardServiceImpl extends ServiceImpl<PmEmployeeAwardDao, PmEmployeeAward> implements PmEmployeeAwardService {
    private final PmEmployeeAwardDao pmEmployeeAwardDao;
    private final PmEmployeeAwardMapper pmEmployeeAwardMapper;
    @Autowired
    private PmEmployeeService pmEmployeeService;

    public PmEmployeeAwardServiceImpl(PmEmployeeAwardDao pmEmployeeAwardDao, PmEmployeeAwardMapper pmEmployeeAwardMapper) {
        this.pmEmployeeAwardDao = pmEmployeeAwardDao;
        this.pmEmployeeAwardMapper = pmEmployeeAwardMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeAwardDTO insert(PmEmployeeAward employeeAwardNew) {
        pmEmployeeAwardDao.insertAllColumn(employeeAwardNew);
        return pmEmployeeAwardMapper.toDto(employeeAwardNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeAward employeeAward = new PmEmployeeAward();
        employeeAward.setId(id);
        employeeAward.setEnabledFlag(false);
        this.delete(employeeAward);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeAward employeeAward) {
        //  确认删除前是否需要做检查,只失效，不删除
        pmEmployeeAwardDao.updateEnableFlag(employeeAward);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeAward employeeAwardNew) {
        PmEmployeeAward employeeAwardInDb = Optional.ofNullable(pmEmployeeAwardDao.getByKey(employeeAwardNew.getId())).orElseGet(PmEmployeeAward::new);
        ValidationUtil.isNull(employeeAwardInDb.getId(), "EmployeeAward", "id", employeeAwardNew.getId());
        employeeAwardNew.setId(employeeAwardInDb.getId());
        pmEmployeeAwardDao.updateAllColumnByKey(employeeAwardNew);
    }

    @Override
    public PmEmployeeAwardDTO getByKey(Long id) {
        PmEmployeeAward employeeAward = Optional.ofNullable(pmEmployeeAwardDao.getByKey(id)).orElseGet(PmEmployeeAward::new);
        ValidationUtil.isNull(employeeAward.getId(), "EmployeeAward", "id", id);
        return pmEmployeeAwardMapper.toDto(employeeAward);
    }

    @Override
    public List<PmEmployeeAwardDTO> listAll(PmEmployeeAwardQueryCriteria criteria) {
        List<PmEmployeeAwardDTO> pmEmployeeAwardDTOSs = pmEmployeeAwardMapper.toDto(pmEmployeeAwardDao.listAllByCriteria(criteria));
//        for (PmEmployeeAwardDTO pea : pmEmployeeAwardDTOSs) {
//            pea.setEmployee(pmEmployeeService.getByKey(pea.getEmployee().getId()));
//        }
        return pmEmployeeAwardDTOSs;
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeAwardQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeAward> page = PageUtil.startPage(pageable);
        List<PmEmployeeAward> employeeAwards = pmEmployeeAwardDao.listAllByCriteriaPage(page, criteria);
        List<PmEmployeeAwardDTO> pmEmployeeAwardDTOSs = pmEmployeeAwardMapper.toDto(employeeAwards);
//        for (PmEmployeeAwardDTO pea : pmEmployeeAwardDTOSs) {
//            pea.setEmployee(pmEmployeeService.getByKey(pea.getEmployee().getId()));
//        }
        return PageUtil.toPage(pmEmployeeAwardDTOSs, page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeAwardDTO> employeeAwardDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (PmEmployeeAwardDTO employeeAwardDTO : employeeAwardDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("工牌号", employeeAwardDTO.getEmployee().getWorkCard());
            map.put("姓名", employeeAwardDTO.getEmployee().getName());
            map.put("奖励或扣罚", employeeAwardDTO.getType() == null ? "" : ("Reward".equals(employeeAwardDTO.getType()) ? "奖励" : "扣罚"));
            map.put("奖罚名称", employeeAwardDTO.getAwardName());
            map.put("奖罚处理开始时间", format.format(employeeAwardDTO.getAwardStarTime()));
            map.put("奖罚处理结束时间", format.format(employeeAwardDTO.getAwardEndTime()));
            map.put("奖罚单位", employeeAwardDTO.getAwardCompany());
            map.put("奖罚内容", employeeAwardDTO.getAwardContent());
            map.put("奖罚结果", employeeAwardDTO.getAwardResult());
            map.put("奖罚金额", employeeAwardDTO.getAwardMoney());
            map.put("是否有备查资料", employeeAwardDTO.getReferenceBackupFlag() == null ? "" : ( employeeAwardDTO.getReferenceBackupFlag() ? "有" : "没有"));
            map.put("备注", employeeAwardDTO.getRemarks());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PmEmployeeAwardDTO> batchInsert(List<PmEmployeeAward> employeeAwardNews, Long employeeId) {
        if (employeeAwardNews != null) {
            for (PmEmployeeAward pea : employeeAwardNews) {
                if (employeeId != null) {
                    if (pea.getEmployee() == null) {
                        pea.setEmployee(new PmEmployee());
                    }
                    pea.getEmployee().setId(employeeId);
                }
                if (pea.getEmployee() == null || pea.getEmployee().getId() == null) {
                    throw new InfoCheckWarningMessException("员工id不能为空");
                }
                if (pea.getId() != null) {
                    if (pea.getId().equals(-1L)) {
                        pmEmployeeAwardDao.insertAllColumn(pea);
                    } else {
                        pmEmployeeAwardDao.updateAllColumnByKey(pea);
                    }
                } else {
                    throw new InfoCheckWarningMessException("奖罚批量插入传入集合中 id 包含空值！");
                }
            }
        }

        return pmEmployeeAwardMapper.toDto(employeeAwardNews);
    }
}
