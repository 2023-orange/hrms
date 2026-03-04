package com.sunten.hrms.re.service.impl;

import com.sunten.hrms.re.domain.ReEmpMesMonthly;
import com.sunten.hrms.re.dao.ReEmpMesMonthlyDao;
import com.sunten.hrms.re.service.ReEmpMesMonthlyService;
import com.sunten.hrms.re.dto.ReEmpMesMonthlyDTO;
import com.sunten.hrms.re.dto.ReEmpMesMonthlyQueryCriteria;
import com.sunten.hrms.re.mapper.ReEmpMesMonthlyMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 每月人员情况存档 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-01-07
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReEmpMesMonthlyServiceImpl extends ServiceImpl<ReEmpMesMonthlyDao, ReEmpMesMonthly> implements ReEmpMesMonthlyService {
    private final ReEmpMesMonthlyDao reEmpMesMonthlyDao;
    private final ReEmpMesMonthlyMapper reEmpMesMonthlyMapper;

    public ReEmpMesMonthlyServiceImpl(ReEmpMesMonthlyDao reEmpMesMonthlyDao, ReEmpMesMonthlyMapper reEmpMesMonthlyMapper) {
        this.reEmpMesMonthlyDao = reEmpMesMonthlyDao;
        this.reEmpMesMonthlyMapper = reEmpMesMonthlyMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReEmpMesMonthlyDTO insert(ReEmpMesMonthly empMesMonthlyNew) {
        reEmpMesMonthlyDao.insertAllColumn(empMesMonthlyNew);
        return reEmpMesMonthlyMapper.toDto(empMesMonthlyNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReEmpMesMonthly empMesMonthly = new ReEmpMesMonthly();
        empMesMonthly.setId(id);
        this.delete(empMesMonthly);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReEmpMesMonthly empMesMonthly) {
        // TODO    确认删除前是否需要做检查
        reEmpMesMonthlyDao.deleteByEntityKey(empMesMonthly);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReEmpMesMonthly empMesMonthlyNew) {
        ReEmpMesMonthly empMesMonthlyInDb = Optional.ofNullable(reEmpMesMonthlyDao.getByKey(empMesMonthlyNew.getId())).orElseGet(ReEmpMesMonthly::new);
        ValidationUtil.isNull(empMesMonthlyInDb.getId() ,"EmpMesMonthly", "id", empMesMonthlyNew.getId());
        empMesMonthlyNew.setId(empMesMonthlyInDb.getId());
        reEmpMesMonthlyDao.updateAllColumnByKey(empMesMonthlyNew);
    }

    @Override
    public ReEmpMesMonthlyDTO getByKey(Long id) {
        ReEmpMesMonthly empMesMonthly = Optional.ofNullable(reEmpMesMonthlyDao.getByKey(id)).orElseGet(ReEmpMesMonthly::new);
        ValidationUtil.isNull(empMesMonthly.getId() ,"EmpMesMonthly", "id", id);
        return reEmpMesMonthlyMapper.toDto(empMesMonthly);
    }

    @Override
    public List<ReEmpMesMonthlyDTO> listAll(ReEmpMesMonthlyQueryCriteria criteria) {
        return reEmpMesMonthlyMapper.toDto(reEmpMesMonthlyDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReEmpMesMonthlyQueryCriteria criteria, Pageable pageable) {
        Page<ReEmpMesMonthly> page = PageUtil.startPage(pageable);
        List<ReEmpMesMonthly> empMesMonthlys = reEmpMesMonthlyDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reEmpMesMonthlyMapper.toDto(empMesMonthlys), page.getTotal());
    }

    @Override
    public void download(List<ReEmpMesMonthlyDTO> empMesMonthlyDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReEmpMesMonthlyDTO empMesMonthlyDTO : empMesMonthlyDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("年份", empMesMonthlyDTO.getYear());
            map.put("月份", empMesMonthlyDTO.getMonth());
            map.put("所属日期", empMesMonthlyDTO.getDate());
            map.put("人员id", empMesMonthlyDTO.getEmployeeId());
            map.put("岗位id", empMesMonthlyDTO.getJobId());
            map.put("部门id", empMesMonthlyDTO.getDeptId());
            map.put("id", empMesMonthlyDTO.getId());
            map.put("updateTime", empMesMonthlyDTO.getUpdateTime());
            map.put("updateBy", empMesMonthlyDTO.getUpdateBy());
            map.put("createTime", empMesMonthlyDTO.getCreateTime());
            map.put("createBy", empMesMonthlyDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     *  @author liangjw
     *  @since 2022/1/7 9:58
     *  每月自动插入人员信息记录,用于出近三年科室.岗位需求人数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoInsertEmpMesMonthlyForReDemand() {
        // 检验是否已存在
        if (reEmpMesMonthlyDao.checkBeforeAutoInsertEmpMesMonthly(LocalDate.now().getYear(), LocalDate.now().getMonthValue())) {
            reEmpMesMonthlyDao.autoInsertEmpMesMonthly();
        }
    }
}
