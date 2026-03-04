package com.sunten.hrms.wta.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import com.sunten.hrms.wta.dao.WtaQuartzJobDao;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.dto.WtaQuartzJobDTO;
import com.sunten.hrms.wta.dto.WtaQuartzJobQueryCriteria;
import com.sunten.hrms.wta.mapper.WtaQuartzJobMapper;
import com.sunten.hrms.wta.service.WtaQuartzJobService;
import com.sunten.hrms.wta.utils.QuartzManage;
import org.quartz.CronExpression;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-23
 */
@Service
@CacheConfig(cacheNames = "quartzJob")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WtaQuartzJobServiceImpl extends ServiceImpl<WtaQuartzJobDao, WtaQuartzJob> implements WtaQuartzJobService {
    private final WtaQuartzJobDao wtaQuartzJobDao;
    private final WtaQuartzJobMapper wtaQuartzJobMapper;
    private final QuartzManage quartzManage;

    public WtaQuartzJobServiceImpl(WtaQuartzJobDao wtaQuartzJobDao, WtaQuartzJobMapper wtaQuartzJobMapper, QuartzManage quartzManage) {
        this.wtaQuartzJobDao = wtaQuartzJobDao;
        this.wtaQuartzJobMapper = wtaQuartzJobMapper;
        this.quartzManage = quartzManage;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public WtaQuartzJobDTO insert(WtaQuartzJob quartzJobNew) {
        if (!CronExpression.isValidExpression(quartzJobNew.getCronExpression())) {
            throw new InfoCheckWarningMessException("cron表达式格式错误");
        }
        wtaQuartzJobDao.insertAllColumn(quartzJobNew);
        quartzManage.addJob(quartzJobNew);
        return wtaQuartzJobMapper.toDto(quartzJobNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        WtaQuartzJob quartzJob = new WtaQuartzJob();
        quartzJob.setId(id);
        this.delete(quartzJob);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(WtaQuartzJob quartzJob) {
        // TODO    确认删除前是否需要做检查
//        if (quartzJob.getId().equals(1L)) {
//            throw new InfoCheckWarningMessException("该任务不可操作");
//        }
        wtaQuartzJobDao.deleteByEntityKey(quartzJob);
        quartzManage.deleteJob(quartzJob);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(WtaQuartzJob quartzJobNew) {
        WtaQuartzJob quartzJobInDb = Optional.ofNullable(wtaQuartzJobDao.getByKey(quartzJobNew.getId())).orElseGet(WtaQuartzJob::new);
        ValidationUtil.isNull(quartzJobInDb.getId(), "QuartzJob", "id", quartzJobNew.getId());
        quartzJobNew.setId(quartzJobInDb.getId());
        wtaQuartzJobDao.updateAllColumnByKey(quartzJobNew);
        quartzManage.updateJobCron(quartzJobNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public WtaQuartzJobDTO getByKey(Long id) {
        WtaQuartzJob quartzJob = Optional.ofNullable(wtaQuartzJobDao.getByKey(id)).orElseGet(WtaQuartzJob::new);
        ValidationUtil.isNull(quartzJob.getId(), "QuartzJob", "id", id);
        return wtaQuartzJobMapper.toDto(quartzJob);
    }

    @Override
    public WtaQuartzJob getEntityByKey(Long id) {
        WtaQuartzJob quartzJob = Optional.ofNullable(wtaQuartzJobDao.getByKey(id)).orElseGet(WtaQuartzJob::new);
        ValidationUtil.isNull(quartzJob.getId(), "QuartzJob", "id", id);
        return quartzJob;
    }

    @Override
    @Cacheable
    public List<WtaQuartzJobDTO> listAll(WtaQuartzJobQueryCriteria criteria) {
        return wtaQuartzJobMapper.toDto(wtaQuartzJobDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(WtaQuartzJobQueryCriteria criteria, Pageable pageable) {
        Page<WtaQuartzJob> page = PageUtil.startPage(pageable);
        List<WtaQuartzJob> quartzJobs = wtaQuartzJobDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(wtaQuartzJobMapper.toDto(quartzJobs), page.getTotal());
    }

    @Override
    public void download(List<WtaQuartzJobDTO> quartzJobDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WtaQuartzJobDTO quartzJobDTO : quartzJobDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzJobDTO.getJobName());
            map.put("Bean名称", quartzJobDTO.getBeanName());
            map.put("执行方法", quartzJobDTO.getMethodName());
            map.put("参数", quartzJobDTO.getParams());
            map.put("表达式", quartzJobDTO.getCronExpression());
            map.put("状态", quartzJobDTO.getPause() ? "暂停中" : "运行中");
            map.put("描述", quartzJobDTO.getRemark());
            map.put("创建日期", quartzJobDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updatePause(WtaQuartzJob quartzJob) {
//        if (quartzJob.getId().equals(1L)) {
//            throw new InfoCheckWarningMessException("该任务不可操作");
//        }
        if (quartzJob.getPause()) {
            quartzManage.resumeJob(quartzJob);
            quartzJob.setPause(false);
        } else {
            quartzManage.pauseJob(quartzJob);
            quartzJob.setPause(true);
        }
        wtaQuartzJobDao.updatePause(quartzJob);
    }

    @Override
    public void execution(WtaQuartzJob quartzJob) {
//        if (quartzJob.getId().equals(1L)) {
//            throw new InfoCheckWarningMessException("该任务不可操作");
//        }
        quartzManage.runAJobNow(quartzJob);
    }
}
