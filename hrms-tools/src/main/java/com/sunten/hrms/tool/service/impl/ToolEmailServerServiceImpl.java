package com.sunten.hrms.tool.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.tool.dao.ToolEmailServerDao;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerDTO;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import com.sunten.hrms.tool.mapper.ToolEmailServerMapper;
import com.sunten.hrms.tool.service.ToolEmailServerService;
import com.sunten.hrms.utils.EncryptUtils;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
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
 * @since 2020-11-02
 */
@Service
@CacheConfig(cacheNames = "emailServer")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ToolEmailServerServiceImpl extends ServiceImpl<ToolEmailServerDao, ToolEmailServer> implements ToolEmailServerService {
    private final ToolEmailServerDao toolEmailServerDao;
    private final ToolEmailServerMapper toolEmailServerMapper;

    public ToolEmailServerServiceImpl(ToolEmailServerDao toolEmailServerDao, ToolEmailServerMapper toolEmailServerMapper) {
        this.toolEmailServerDao = toolEmailServerDao;
        this.toolEmailServerMapper = toolEmailServerMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ToolEmailServerDTO insert(ToolEmailServer emailServerNew) {
        try {
            emailServerNew.setPass(EncryptUtils.desEncrypt(emailServerNew.getPass()));
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        toolEmailServerDao.insertAllColumn(emailServerNew);
        return toolEmailServerMapper.toDto(emailServerNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ToolEmailServer emailServer = new ToolEmailServer();
        emailServer.setId(id);
        this.delete(emailServer);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(ToolEmailServer emailServer) {
        toolEmailServerDao.deleteByEntityKey(emailServer);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ToolEmailServer emailServerNew) {
        ToolEmailServer emailServerInDb = Optional.ofNullable(toolEmailServerDao.getByKey(emailServerNew.getId())).orElseGet(ToolEmailServer::new);
        ValidationUtil.isNull(emailServerInDb.getId(), "EmailServer", "id", emailServerNew.getId());
        emailServerNew.setId(emailServerInDb.getId());
        try {
            if (!emailServerInDb.getPass().equals(emailServerNew.getPass())) {
                emailServerNew.setPass(EncryptUtils.desEncrypt(emailServerNew.getPass()));
            }
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        toolEmailServerDao.updateAllColumnByKey(emailServerNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public ToolEmailServerDTO getByKey(Long id) {
        ToolEmailServer emailServer = Optional.ofNullable(toolEmailServerDao.getByKey(id)).orElseGet(ToolEmailServer::new);
        ValidationUtil.isNull(emailServer.getId(), "EmailServer", "id", id);
        return toolEmailServerMapper.toDto(emailServer);
    }

    @Override
    public List<ToolEmailServerDTO> listAll(ToolEmailServerQueryCriteria criteria) {
        return toolEmailServerMapper.toDto(toolEmailServerDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ToolEmailServerQueryCriteria criteria, Pageable pageable) {
        Page<ToolEmailServer> page = PageUtil.startPage(pageable);
        List<ToolEmailServer> emailServers = toolEmailServerDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(toolEmailServerMapper.toDto(emailServers), page.getTotal());
    }

    @Override
    public void download(List<ToolEmailServerDTO> emailServerDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ToolEmailServerDTO emailServerDTO : emailServerDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("ID", emailServerDTO.getId());
            map.put("发件人", emailServerDTO.getFromUser());
            map.put("邮件服务器SMTP地址", emailServerDTO.getHost());
            map.put("密码", emailServerDTO.getPass());
            map.put("端口", emailServerDTO.getPort());
            map.put("发件者用户名", emailServerDTO.getUsername());
            map.put("是否需要权限验证", emailServerDTO.getNeedAuthFlag());
            map.put("通过ssl发送", emailServerDTO.getSslEnableFlag());
            map.put("优先级", emailServerDTO.getPriorityLevel());
            map.put("有效标记", emailServerDTO.getEnabledFlag());
            map.put("弹性域1", emailServerDTO.getAttribute1());
            map.put("弹性域2", emailServerDTO.getAttribute2());
            map.put("弹性域3", emailServerDTO.getAttribute3());
            map.put("弹性域4", emailServerDTO.getAttribute4());
            map.put("弹性域5", emailServerDTO.getAttribute5());
            map.put("serverName", emailServerDTO.getServerName());
            map.put("description", emailServerDTO.getDescription());
            map.put("createTime", emailServerDTO.getCreateTime());
            map.put("updateBy", emailServerDTO.getUpdateBy());
            map.put("createBy", emailServerDTO.getCreateBy());
            map.put("updateTime", emailServerDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
