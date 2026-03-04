package com.sunten.hrms.fnd.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.dao.FndLogDao;
import com.sunten.hrms.fnd.domain.FndLog;
import com.sunten.hrms.fnd.dto.FndLogDTO;
import com.sunten.hrms.fnd.dto.FndLogQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndLogErrorMapper;
import com.sunten.hrms.fnd.mapper.FndLogMapper;
import com.sunten.hrms.fnd.mapper.FndLogSmallMapper;
import com.sunten.hrms.fnd.service.FndLogService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.StringUtils;
import com.sunten.hrms.utils.ValidationUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
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
@CacheConfig(cacheNames = "log")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndLogServiceImpl extends ServiceImpl<FndLogDao, FndLog> implements FndLogService {
    private final FndLogDao fndLogDao;
    private final FndLogMapper fndLogMapper;
    private final FndLogErrorMapper fndLogErrorMapper;
    private final FndLogSmallMapper fndLogSmallMapper;

    public FndLogServiceImpl(FndLogDao fndLogDao, FndLogMapper fndLogMapper, FndLogErrorMapper fndLogErrorMapper, FndLogSmallMapper fndLogSmallMapper) {
        this.fndLogDao = fndLogDao;
        this.fndLogMapper = fndLogMapper;
        this.fndLogErrorMapper = fndLogErrorMapper;
        this.fndLogSmallMapper = fndLogSmallMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndLogDTO insert(FndLog logNew) {
        fndLogDao.insertAllColumn(logNew);
        return fndLogMapper.toDto(logNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndLog log = new FndLog();
        log.setId(id);
        this.delete(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndLog log) {
        // TODO    确认删除前是否需要做检查
        fndLogDao.deleteByEntityKey(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndLog logNew) {
        FndLog logInDb = Optional.ofNullable(fndLogDao.getByKey(logNew.getId())).orElseGet(FndLog::new);
        ValidationUtil.isNull(logInDb.getId(), "Log", "id", logNew.getId());
        logNew.setId(logInDb.getId());
        fndLogDao.updateAllColumnByKey(logNew);
    }

    @Override
    public FndLogDTO getByKey(Long id) {
        FndLog log = Optional.ofNullable(fndLogDao.getByKey(id)).orElseGet(FndLog::new);
        ValidationUtil.isNull(log.getId(), "Log", "id", id);
        return fndLogMapper.toDto(log);
    }

    @Override
    public List<FndLogDTO> listAll(FndLogQueryCriteria criteria) {
        return fndLogMapper.toDto(fndLogDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndLogQueryCriteria criteria, Pageable pageable) {
        Page<FndLog> page = PageUtil.startPage(pageable);
        List<FndLog> logs = fndLogDao.listAllByCriteriaPage(page, criteria);
        if ("ERROR".equals(criteria.getLogType())) {
            return PageUtil.toPage(fndLogErrorMapper.toDto(logs), page.getTotal());
        }
        return PageUtil.toPage(fndLogMapper.toDto(logs), page.getTotal());
    }

    @Override
    public Object listAllByUser(FndLogQueryCriteria criteria, Pageable pageable) {
        Page<FndLog> page = PageUtil.startPage(pageable);
        List<FndLog> logs = fndLogDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndLogSmallMapper.toDto(logs), page.getTotal());
    }

    @Override
    public void download(List<FndLogDTO> logDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndLogDTO logDTO : logDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", logDTO.getUsername());
            map.put("IP", logDTO.getRequestIp());
            map.put("IP来源", logDTO.getAddress());
            map.put("描述", logDTO.getDescription());
            map.put("浏览器", logDTO.getBrowser());
            map.put("请求耗时/毫秒", logDTO.getTime());
            map.put("异常详情", ObjectUtil.isNotNull(logDTO.getExceptionDetail()) ? logDTO.getExceptionDetail() : "");
            map.put("创建日期", logDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, FndLog log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);
        saveLog(username, browser, ip, joinPoint, log, signature, aopLog.value());
    }

    private void saveLog(String username, String browser, String ip, ProceedingJoinPoint joinPoint, FndLog log, MethodSignature signature, String value) {
        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        // 描述
        if (log != null) {
            log.setDescription(value);
        }
        assert log != null;
        log.setRequestIp(ip);

        String LOGINPATH = "login";
        if (LOGINPATH.equals(signature.getName())) {
            try {
                assert argValues != null;
                username = new JSONObject(argValues[0]).get("username").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params.toString() + " }");
        log.setBrowser(browser);
        fndLogDao.insertAllColumn(log);
    }

    @Override
    public void saveError(String username, String browser, String ip, ProceedingJoinPoint joinPoint, FndLog log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ErrorLog aopLog = method.getAnnotation(ErrorLog.class);
        saveLog(username, browser, ip, joinPoint, log, signature, aopLog.value());
    }

    @Override
    public Object listByErrDetail(Long id) {
        String details = fndLogDao.getByKey(id).getExceptionDetail();
        return Dict.create().set("exception", ObjectUtil.isNotNull(details) ? details : "");
    }
}
