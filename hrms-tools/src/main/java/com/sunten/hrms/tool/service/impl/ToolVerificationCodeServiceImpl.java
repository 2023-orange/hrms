package com.sunten.hrms.tool.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.tool.dao.ToolVerificationCodeDao;
import com.sunten.hrms.tool.domain.ToolVerificationCode;
import com.sunten.hrms.tool.dto.ToolVerificationCodeDTO;
import com.sunten.hrms.tool.dto.ToolVerificationCodeQueryCriteria;
import com.sunten.hrms.tool.mapper.ToolVerificationCodeMapper;
import com.sunten.hrms.tool.service.ToolVerificationCodeService;
import com.sunten.hrms.tool.vo.ToolEmailVo;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-25
 */
@Service
@CacheConfig(cacheNames = "verificationCode")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ToolVerificationCodeServiceImpl extends ServiceImpl<ToolVerificationCodeDao, ToolVerificationCode> implements ToolVerificationCodeService {
    private final ToolVerificationCodeDao toolVerificationCodeDao;
    private final ToolVerificationCodeMapper toolVerificationCodeMapper;
    @Value("${code.expiration}")
    private Integer expiration;

    @Value("${sunten.system-name}")
    private String systemName;

    public ToolVerificationCodeServiceImpl(ToolVerificationCodeDao toolVerificationCodeDao, ToolVerificationCodeMapper toolVerificationCodeMapper) {
        this.toolVerificationCodeDao = toolVerificationCodeDao;
        this.toolVerificationCodeMapper = toolVerificationCodeMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ToolVerificationCodeDTO insert(ToolVerificationCode verificationCodeNew) {
        toolVerificationCodeDao.insertAllColumn(verificationCodeNew);
        return toolVerificationCodeMapper.toDto(verificationCodeNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ToolVerificationCode verificationCode = new ToolVerificationCode();
        verificationCode.setId(id);
        this.delete(verificationCode);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(ToolVerificationCode verificationCode) {
        // TODO    确认删除前是否需要做检查
        toolVerificationCodeDao.deleteByEntityKey(verificationCode);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ToolVerificationCode verificationCodeNew) {
        ToolVerificationCode verificationCodeInDb = Optional.ofNullable(toolVerificationCodeDao.getByKey(verificationCodeNew.getId())).orElseGet(ToolVerificationCode::new);
        ValidationUtil.isNull(verificationCodeInDb.getId(), "VerificationCode", "id", verificationCodeNew.getId());
        verificationCodeNew.setId(verificationCodeInDb.getId());
        toolVerificationCodeDao.updateAllColumnByKey(verificationCodeNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public ToolVerificationCodeDTO getByKey(Long id) {
        ToolVerificationCode verificationCode = Optional.ofNullable(toolVerificationCodeDao.getByKey(id)).orElseGet(ToolVerificationCode::new);
        ValidationUtil.isNull(verificationCode.getId(), "VerificationCode", "id", id);
        return toolVerificationCodeMapper.toDto(verificationCode);
    }

    @Override
    @Cacheable
    public List<ToolVerificationCodeDTO> listAll(ToolVerificationCodeQueryCriteria criteria) {
        return toolVerificationCodeMapper.toDto(toolVerificationCodeDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(ToolVerificationCodeQueryCriteria criteria, Pageable pageable) {
        Page<ToolVerificationCode> page = PageUtil.startPage(pageable);
        List<ToolVerificationCode> verificationCodes = toolVerificationCodeDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(toolVerificationCodeMapper.toDto(verificationCodes), page.getTotal());
    }

    @Override
    public void download(List<ToolVerificationCodeDTO> verificationCodeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ToolVerificationCodeDTO verificationCodeDTO : verificationCodeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("ID", verificationCodeDTO.getId());
            map.put("验证码", verificationCodeDTO.getCode());
            map.put("状态：1有效、0过期", verificationCodeDTO.getStatus());
            map.put("验证码类型：email或者短信", verificationCodeDTO.getType());
            map.put("接收邮箱或者手机号码", verificationCodeDTO.getValue());
            map.put("业务名称：如重置邮箱、重置密码等", verificationCodeDTO.getScenes());
            map.put("createTime", verificationCodeDTO.getCreateTime());
            map.put("createBy", verificationCodeDTO.getCreateBy());
            map.put("updateTime", verificationCodeDTO.getUpdateTime());
            map.put("updateBy", verificationCodeDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ToolEmailVo sendEmail(ToolVerificationCode code) {
        ToolEmailVo emailVo;
        String content;
        ToolVerificationCode verificationCode = toolVerificationCodeDao.getEnableCode(code.getScenes(), code.getType(), code.getValue());
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        if (verificationCode == null) {
            code.setCode(RandomUtil.randomNumbers(6));
            content = template.render(Dict.create().set("code", code.getCode()));
            emailVo = new ToolEmailVo(Collections.singletonList(code.getValue()), "验证码 - " + systemName, content);
            toolVerificationCodeDao.insertAllColumn(code);
            timedDestruction(code);
            // 存在就再次发送原来的验证码
        } else {
            content = template.render(Dict.create().set("code", verificationCode.getCode()));
            emailVo = new ToolEmailVo(Collections.singletonList(verificationCode.getValue()), "验证码 - " + systemName, content);
        }
        return emailVo;
    }

    @Override
    public void validated(ToolVerificationCode code) {
        ToolVerificationCode verificationCode = toolVerificationCodeDao.getEnableCode(code.getScenes(), code.getType(), code.getValue());
        if (verificationCode == null || !verificationCode.getCode().equals(code.getCode())) {
            throw new BadRequestException("无效验证码");
        } else {
            verificationCode.setStatus(false);
            toolVerificationCodeDao.updateAllColumnByKey(verificationCode);
        }
    }


    /**
     * 定时任务，指定分钟后改变验证码状态
     *
     * @param verifyCode 验证码
     */
    private void timedDestruction(ToolVerificationCode verifyCode) {
        //以下示例为程序调用结束继续运行
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            executorService.schedule(() -> {
                verifyCode.setStatus(false);
                toolVerificationCodeDao.updateAllColumnByKey(verifyCode);
            }, expiration * 60 * 1000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
