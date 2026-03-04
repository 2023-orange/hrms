package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.pm.domain.Dxnj;
import com.sunten.hrms.pm.dao.DxnjDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmUsedAnnualLeave;
import com.sunten.hrms.pm.service.DxnjService;
import com.sunten.hrms.pm.dto.DxnjDTO;
import com.sunten.hrms.pm.dto.DxnjQueryCriteria;
import com.sunten.hrms.pm.mapper.DxnjMapper;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2021-10-08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DxnjServiceImpl extends ServiceImpl<DxnjDao, Dxnj> implements DxnjService {
    private final DxnjDao dxnjDao;
    private final DxnjMapper dxnjMapper;
    private final PmEmployeeService pmEmployeeService;
    private final FndUserDao fndUserDao;

    public DxnjServiceImpl(DxnjDao dxnjDao, DxnjMapper dxnjMapper, PmEmployeeService pmEmployeeService, FndUserDao fndUserDao) {
        this.dxnjDao = dxnjDao;
        this.dxnjMapper = dxnjMapper;
        this.pmEmployeeService = pmEmployeeService;
        this.fndUserDao = fndUserDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DxnjDTO insert(Dxnj dxnjNew) {
        dxnjDao.insertAllColumn(dxnjNew);
        return dxnjMapper.toDto(dxnjNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Dxnj dxnj = new Dxnj();
        dxnj.setId(id);
        this.delete(dxnj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Dxnj dxnj) {
        // TODO    确认删除前是否需要做检查
        dxnjDao.deleteByEntityKey(dxnj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dxnj dxnjNew) {
        Dxnj dxnjInDb = Optional.ofNullable(dxnjDao.getByKey(dxnjNew.getId())).orElseGet(Dxnj::new);
        ValidationUtil.isNull(dxnjInDb.getId() ,"dxnj", "id", dxnjNew.getId());
        dxnjNew.setId(dxnjInDb.getId());
        updateTenYearDate(dxnjNew, dxnjInDb.getGph(), dxnjInDb.getXm());
        String name = fndUserDao.getNameByUserId(SecurityUtils.getUserId());
        if (null != name){
            dxnjNew.setZhxgr(name);
        }
        // 更新带薪年假
        dxnjDao.updateDurationAnnualLeave(dxnjNew);
    }

    @Override
    public DxnjDTO getByKey(Integer id) {
        Dxnj dxnj = Optional.ofNullable(dxnjDao.getByKey(id)).orElseGet(Dxnj::new);
        ValidationUtil.isNull(dxnj.getId() ,"dxnj", "id", id);
        return dxnjMapper.toDto(dxnj);
    }

    @Override
    public List<DxnjDTO> listAll(DxnjQueryCriteria criteria) {
        List<Dxnj> dxnjs = dxnjDao.listAllByCriteria(criteria);
        // 在OA获取已使用年假
        setDx(dxnjs);
        return dxnjMapper.toDto(dxnjDao.listAllByCriteria(criteria));
    }

    private void setDx(List<Dxnj> dxnjs) {
        Set<String> workCards = dxnjs.stream().map(Dxnj::getGph).collect(Collectors.toSet());
        // 根据工号合集获取
        List<PmUsedAnnualLeave> pmUsedAnnualLeaves = dxnjDao.getYXJTSFromOA(workCards);
        List<PmUsedAnnualLeave> temp;
        for (Dxnj dx : dxnjs
        ) {
            temp = pmUsedAnnualLeaves.stream().filter(x -> x.getWorkCard().equals(dx.getGph()) && x.getYear().equals(dx.getNd())).collect(Collectors.toList());
            if (LocalDate.now().getYear() == dx.getNd()) { // 当前年直接动态获取，往年的直接从sql查出来
                dx.setYxjts(temp.size() > 0 ? temp.get(0).getUsedAnnualLeave() : 0);
                dx.setSyxjts((null != dx.getNjts() ? dx.getNjts() : 0) + (null != dx.getDurationAnnualLeave() ? dx.getDurationAnnualLeave() : 0)
                        - (null != dx.getYxjts() ? dx.getYxjts() : 0)); // 剩余休假天数 = 年假天数 + （调整天数） - 已休假天数
                dx.setRealAnnualLeave((null != dx.getNjts() ? dx.getNjts() : 0) + (null != dx.getDurationAnnualLeave() ? dx.getDurationAnnualLeave() : 0)); // 实际年假天数 = 年假天数 + 调整天数
            }
        }
    }

    @Override
    public Map<String, Object> listAll(DxnjQueryCriteria criteria, Pageable pageable) {
        Page<Dxnj> page = PageUtil.startPage(pageable);
        List<Dxnj> dxnjs = dxnjDao.listAllByCriteriaPage(page, criteria);
        // 在OA获取已使用年假
        setDx(dxnjs);
        return PageUtil.toPage(dxnjMapper.toDto(dxnjs), page.getTotal());
    }

    @Override
    public void download(List<DxnjDTO> dxnjDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DxnjDTO dxnjDTO : dxnjDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("年度", null != dxnjDTO.getNd() ? dxnjDTO.getNd().toString() : "");
            map.put("工牌号", null != dxnjDTO.getGph() ? dxnjDTO.getGph() : "");
            map.put("姓名", null != dxnjDTO.getXm() ? dxnjDTO.getXm() : "");
            map.put("部门", null != dxnjDTO.getBmmc() ? dxnjDTO.getBmmc() : "");
            map.put("科室", null != dxnjDTO.getKsmc() ? dxnjDTO.getKsmc() : "");
            map.put("班组", null != dxnjDTO.getTeam() ? dxnjDTO.getTeam() : "");
            map.put("入职时间", null != dxnjDTO.getRzsj() ? DateUtil.localDateToStr(dxnjDTO.getRzsj().toLocalDate()) : "");
            map.put("公司工龄（月）", null != dxnjDTO.getGssl() ? dxnjDTO.getGssl().toString() : "");
            map.put("年假天数（天）", null != dxnjDTO.getNjts() ? dxnjDTO.getNjts().toString() : "");
            map.put("年假偏值（天）", null != dxnjDTO.getDurationAnnualLeave() ? dxnjDTO.getDurationAnnualLeave().toString() : "");
            map.put("实际年假天数（天）", null != dxnjDTO.getRealAnnualLeave() ? dxnjDTO.getRealAnnualLeave().toString() : "");
            map.put("已修假天数（天）", null != dxnjDTO.getYxjts() ? dxnjDTO.getYxjts().toString() : "");
            map.put("剩余休假天数（天）", null != dxnjDTO.getSyxjts() ? dxnjDTO.getSyxjts().toString() : "");
            map.put("满十年社保日期", null != dxnjDTO.getTenYearDate() ? DateUtil.localDateToStr(dxnjDTO.getTenYearDate()) : "");
            map.put("满二十年社保日期", null != dxnjDTO.getTwentyYearDate() ? DateUtil.localDateToStr(dxnjDTO.getTwentyYearDate()) : "");
            map.put("备注", null != dxnjDTO.getBz() ? dxnjDTO.getBz() : "");
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoInsertDXNJEveryDay() {
        dxnjDao.autoInsertDXNJEveryDay();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dxnjToFile() {
        dxnjDao.dxnjToFile();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<Dxnj> dxnjs) {
        String name = fndUserDao.getNameByUserId(SecurityUtils.getUserId());
        for (Dxnj dxnj : dxnjs
             ) {
            updateTenYearDate(dxnj, dxnj.getGph(), dxnj.getXm());
            dxnj.setZhxgr(name);
            // 更新带薪年假
            dxnjDao.updateDurationAnnualLeave(dxnj);
        }
    }

    private void updateTenYearDate(Dxnj dxnj, String gph, String xm) {
        PmEmployee pe = new PmEmployee();
        pe.setWorkCard(gph);
        pe.setName(xm);
        if (null != dxnj.getTenYearChangeFlag() || null != dxnj.getTwentyYearChangeFlag()) {
            if (null != dxnj.getTenYearChangeFlag() && dxnj.getTenYearChangeFlag()) {
                if (null != dxnj.getTenYearDate()) {
                    pe.setTwentyYearDate(dxnj.getTenYearDate().plusYears(10));
                    pe.setTenYearDate(dxnj.getTenYearDate());
                    pmEmployeeService.updateTenYearDate(pe);
                } else {
                    // 置空10 和20
                    pe.setTenYearDate(null);
                    pe.setTwentyYearDate(null);
                    pmEmployeeService.updateTenYearDateSetNull(pe);
                }
            }
            if (null != dxnj.getTwentyYearChangeFlag() && dxnj.getTwentyYearChangeFlag()) {
                if (null != dxnj.getTwentyYearDate()) {
                    pe.setTenYearDate(dxnj.getTwentyYearDate().minusYears(10));
                    pe.setTwentyYearDate(dxnj.getTwentyYearDate());
                    pmEmployeeService.updateTenYearDate(pe);
                } else {
                    // 置空10 和20
                    dxnj.setTenYearDate(null);
                    dxnj.setTwentyYearDate(null);
                    pmEmployeeService.updateTenYearDateSetNull(pe);
                }
            }
        }
        // 更新年假天数
        pmEmployeeService.updatePaidAnnualLeaveEveryDayAfterTenYearUpdate(gph);
        dxnj.setZhxgsj(LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTempEmployeeDXNJ() {
        dxnjDao.updateTempEmployeeDXNJ();
    }
}
