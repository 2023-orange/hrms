package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndLogDao;
import com.sunten.hrms.fnd.dao.FndVisitDao;
import com.sunten.hrms.fnd.domain.FndVisit;
import com.sunten.hrms.fnd.service.FndVisitService;
import com.sunten.hrms.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-20
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndVisitServiceImpl extends ServiceImpl<FndVisitDao, FndVisit> implements FndVisitService {
    private final FndVisitDao fndVisitDao;
    private final FndLogDao fndLogDao;

    public FndVisitServiceImpl(FndVisitDao fndVisitDao, FndLogDao fndLogDao) {
        this.fndVisitDao = fndVisitDao;
        this.fndLogDao = fndLogDao;
    }

    @Override
    public void save() {
        LocalDate localDate = LocalDate.now();
        FndVisit visits = fndVisitDao.getByDate(localDate.toString());
        if (visits == null) {
            visits = new FndVisit();
            visits.setWeekDay(StringUtils.getWeekDay());
            visits.setPvCounts(1L);
            visits.setIpCounts(1L);
            visits.setDate(localDate.toString());
            fndVisitDao.insertAllColumn(visits);
        }
    }

    @Override
    public void count(HttpServletRequest request) {
        LocalDate localDate = LocalDate.now();
//        FndVisit visits = fndVisitDao.getByDate(localDate.toString());
//        visits.setPvCounts(visits.getPvCounts() + 1);
//        long ipCounts = fndLogDao.findIp(localDate.toString(), localDate.plusDays(1).toString());
//        visits.setIpCounts(ipCounts);
//        fndVisitDao.updateAllColumnByKey(visits);
    }

    @Override
    public Object get() {
        Map<String, Object> map = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        FndVisit visits = fndVisitDao.getByDate(localDate.toString());
        List<FndVisit> list = fndVisitDao.listVisitBetween(localDate.minusDays(6).toString(), localDate.plusDays(1).toString());

        long recentVisits = 0, recentIp = 0;
        for (FndVisit data : list) {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits", visits.getPvCounts());
        map.put("newIp", visits.getIpCounts());
        map.put("recentVisits", recentVisits);
        map.put("recentIp", recentIp);
        return map;
    }

    @Override
    public Object getChartData() {
        Map<String, Object> map = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        List<FndVisit> list = fndVisitDao.listVisitBetween(localDate.minusDays(6).toString(), localDate.plusDays(1).toString());
        map.put("weekDays", list.stream().map(FndVisit::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData", list.stream().map(FndVisit::getPvCounts).collect(Collectors.toList()));
        map.put("ipData", list.stream().map(FndVisit::getIpCounts).collect(Collectors.toList()));
        return map;
    }
}
