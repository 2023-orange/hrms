package com.sunten.hrms.wta.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import com.sunten.hrms.wta.dao.WtaQuartzLogDao;
import com.sunten.hrms.wta.domain.WtaQuartzLog;
import com.sunten.hrms.wta.dto.WtaQuartzLogDTO;
import com.sunten.hrms.wta.dto.WtaQuartzLogQueryCriteria;
import com.sunten.hrms.wta.mapper.WtaQuartzLogMapper;
import com.sunten.hrms.wta.service.WtaQuartzLogService;
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
@CacheConfig(cacheNames = "quartzLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WtaQuartzLogServiceImpl extends ServiceImpl<WtaQuartzLogDao, WtaQuartzLog> implements WtaQuartzLogService {
    private final WtaQuartzLogDao wtaQuartzLogDao;
    private final WtaQuartzLogMapper wtaQuartzLogMapper;

    public WtaQuartzLogServiceImpl(WtaQuartzLogDao wtaQuartzLogDao, WtaQuartzLogMapper wtaQuartzLogMapper) {
        this.wtaQuartzLogDao = wtaQuartzLogDao;
        this.wtaQuartzLogMapper = wtaQuartzLogMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public WtaQuartzLogDTO insert(WtaQuartzLog quartzLogNew) {
        wtaQuartzLogDao.insertAllColumn(quartzLogNew);
        return wtaQuartzLogMapper.toDto(quartzLogNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        WtaQuartzLog quartzLog = new WtaQuartzLog();
        quartzLog.setId(id);
        this.delete(quartzLog);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(WtaQuartzLog quartzLog) {
        // TODO    确认删除前是否需要做检查
        wtaQuartzLogDao.deleteByEntityKey(quartzLog);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(WtaQuartzLog quartzLogNew) {
        WtaQuartzLog quartzLogInDb = Optional.ofNullable(wtaQuartzLogDao.getByKey(quartzLogNew.getId())).orElseGet(WtaQuartzLog::new);
        ValidationUtil.isNull(quartzLogInDb.getId(), "QuartzLog", "id", quartzLogNew.getId());
        quartzLogNew.setId(quartzLogInDb.getId());
        wtaQuartzLogDao.updateAllColumnByKey(quartzLogNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public WtaQuartzLogDTO getByKey(Long id) {
        WtaQuartzLog quartzLog = Optional.ofNullable(wtaQuartzLogDao.getByKey(id)).orElseGet(WtaQuartzLog::new);
        ValidationUtil.isNull(quartzLog.getId(), "QuartzLog", "id", id);
        return wtaQuartzLogMapper.toDto(quartzLog);
    }

    @Override
    public List<WtaQuartzLogDTO> listAll(WtaQuartzLogQueryCriteria criteria) {
        return wtaQuartzLogMapper.toDto(wtaQuartzLogDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(WtaQuartzLogQueryCriteria criteria, Pageable pageable) {
        Page<WtaQuartzLog> page = PageUtil.startPage(pageable);
        List<WtaQuartzLog> quartzLogs = wtaQuartzLogDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(wtaQuartzLogMapper.toDto(quartzLogs), page.getTotal());
    }

    @Override
    public void download(List<WtaQuartzLogDTO> quartzLogDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WtaQuartzLogDTO quartzLogDTO : quartzLogDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzLogDTO.getJobName());
            map.put("Bean名称", quartzLogDTO.getBeanName());
            map.put("执行方法", quartzLogDTO.getMethodName());
            map.put("参数", quartzLogDTO.getParams());
            map.put("表达式", quartzLogDTO.getCronExpression());
            map.put("异常详情", quartzLogDTO.getExceptionDetail());
            map.put("耗时/毫秒", quartzLogDTO.getTime());
            map.put("状态", quartzLogDTO.getSuccess() ? "成功" : "失败");
            map.put("创建日期", quartzLogDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
