package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.domain.AcSetUp;
import com.sunten.hrms.ac.dao.AcSetUpDao;
import com.sunten.hrms.ac.service.AcSetUpService;
import com.sunten.hrms.ac.dto.AcSetUpDTO;
import com.sunten.hrms.ac.dto.AcSetUpQueryCriteria;
import com.sunten.hrms.ac.mapper.AcSetUpMapper;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.sunten.hrms.wta.dao.WtaQuartzJobDao;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.dto.WtaQuartzJobQueryCriteria;
import com.sunten.hrms.wta.service.WtaQuartzJobService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 考勤异常允许设置表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcSetUpServiceImpl extends ServiceImpl<AcSetUpDao, AcSetUp> implements AcSetUpService {
    private final AcSetUpDao acSetUpDao;
    private final AcSetUpMapper acSetUpMapper;
    private final WtaQuartzJobDao wtaQuartzJobDao;
    private final WtaQuartzJobService wtaQuartzJobService;

    public AcSetUpServiceImpl(AcSetUpDao acSetUpDao, AcSetUpMapper acSetUpMapper, WtaQuartzJobDao wtaQuartzJobDao,
                              WtaQuartzJobService wtaQuartzJobService) {
        this.acSetUpDao = acSetUpDao;
        this.acSetUpMapper = acSetUpMapper;
        this.wtaQuartzJobDao = wtaQuartzJobDao;
        this.wtaQuartzJobService = wtaQuartzJobService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcSetUpDTO insert(AcSetUp setUpNew) {
        acSetUpDao.insertAllColumn(setUpNew);
        return acSetUpMapper.toDto(setUpNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcSetUp setUp = new AcSetUp();
        setUp.setId(id);
        this.delete(setUp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcSetUp setUp) {
        // TODO    确认删除前是否需要做检查
        acSetUpDao.deleteByEntityKey(setUp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcSetUp setUpNew) {
        acSetUpDao.updateAllColumnByKey(setUpNew);
        // 获取定时任务的cron
        WtaQuartzJobQueryCriteria wtaQuartzJobQueryCriteria = new WtaQuartzJobQueryCriteria();
        wtaQuartzJobQueryCriteria.setJobName("考勤异常生成");
        List<WtaQuartzJob> wtaQuartzJobs = wtaQuartzJobDao.listAllByCriteria(wtaQuartzJobQueryCriteria);
        WtaQuartzJob wtaQuartzJob = new WtaQuartzJob();
        if (wtaQuartzJobs.size() > 0) {
            wtaQuartzJob = wtaQuartzJobs.get(0);
        }        // 更新定时任务的cron
        if (null != wtaQuartzJob) {// 0 0 0 1/10 * ?   从1号开始每10日0点更新一次
            String cron = setUpNew.getRunTime().getSecond() + " " +
                    setUpNew.getRunTime().getMinute() + " " +
                    setUpNew.getRunTime().getHour() + " " + "1/" +
                    setUpNew.getInterval().toString() + " * ?";
            wtaQuartzJob.setCronExpression(cron);
            wtaQuartzJobService.update(wtaQuartzJob);
        } else {
            throw new InfoCheckWarningMessException("没找到考勤异常的定时任务");
        }
    }

    @Override
    public AcSetUpDTO getByKey(Long id) {
        AcSetUp setUp = Optional.ofNullable(acSetUpDao.getByKey(id)).orElseGet(AcSetUp::new);
        ValidationUtil.isNull(setUp.getId() ,"SetUp", "id", id);
        return acSetUpMapper.toDto(setUp);
    }

    @Override
    public List<AcSetUpDTO> listAll(AcSetUpQueryCriteria criteria) {
        return acSetUpMapper.toDto(acSetUpDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcSetUpQueryCriteria criteria, Pageable pageable) {
        Page<AcSetUp> page = PageUtil.startPage(pageable);
        List<AcSetUp> setUps = acSetUpDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acSetUpMapper.toDto(setUps), page.getTotal());
    }

    @Override
    public void download(List<AcSetUpDTO> setUpDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcSetUpDTO setUpDTO : setUpDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id主键", setUpDTO.getId());
            map.put("异常间隔日期", setUpDTO.getInterval());
            map.put("允许时间", setUpDTO.getRunTime());
            map.put("是否0点阶段", setUpDTO.getStageFlag());
            map.put("创建时间", setUpDTO.getCreateTime());
            map.put("创建人ID", setUpDTO.getCreateBy());
            map.put("修改时间", setUpDTO.getUpdateTime());
            map.put("修改人ID", setUpDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcSetUpDTO getByTop() { // 获取最新一条
        AcSetUp acSetUp = acSetUpDao.getByNew();
        if (null == acSetUp) { // 手动生成一条acSetUp与定义任务
            AcSetUp newAc = new AcSetUp();
            newAc.setInterval(7);
            newAc.setStageFlag(true);
            newAc.setRunTime(LocalTime.of(0,0,0));
            newAc.setCalculation(BigDecimal.valueOf(7.5));
            // 获取定时任务是否存在
            WtaQuartzJobQueryCriteria wtaQuartzJobQueryCriteria = new WtaQuartzJobQueryCriteria();
            wtaQuartzJobQueryCriteria.setJobName("考勤异常生成");
            List<WtaQuartzJob> wtaQuartzJobs = wtaQuartzJobDao.listAllByCriteria(wtaQuartzJobQueryCriteria);
            if (wtaQuartzJobs.size() == 0) { // 创建任务
                WtaQuartzJob wtaQuartzJob = new WtaQuartzJob();
                wtaQuartzJob.setCronExpression("0 0 0 1/7 * ?");
                wtaQuartzJob.setPause(false);
                wtaQuartzJob.setBeanName("acAttendanceRecordTempServiceImpl");
                wtaQuartzJob.setJobName("考勤异常生成");
                wtaQuartzJob.setMethodName("generateAbnormalAttendance");
                wtaQuartzJobService.insert(wtaQuartzJob);
            }
            return this.insert(newAc);
        } else {
            return acSetUpMapper.toDto(acSetUpDao.getByNew());
        }
    }
}
