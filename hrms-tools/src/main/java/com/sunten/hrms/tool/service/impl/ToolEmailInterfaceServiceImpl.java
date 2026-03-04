package com.sunten.hrms.tool.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.util.StringUtils;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.tool.dao.ToolEmailInterfaceDao;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceDTO;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceQueryCriteria;
import com.sunten.hrms.tool.dto.ToolEmailServerDTO;
import com.sunten.hrms.tool.mapper.ToolEmailInterfaceMapper;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.tool.service.ToolEmailServerService;
import com.sunten.hrms.tool.vo.ToolEmailVo;
import com.sunten.hrms.utils.EncryptUtils;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-10-30
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ToolEmailInterfaceServiceImpl extends ServiceImpl<ToolEmailInterfaceDao, ToolEmailInterface> implements ToolEmailInterfaceService {
    private final ToolEmailInterfaceDao toolEmailInterfaceDao;
    private final ToolEmailInterfaceMapper toolEmailInterfaceMapper;
    private final ToolEmailServerService toolEmailServerService;
    @Autowired
    private ToolEmailInterfaceService instance;

    public static final String STATUS_IMMEDIATE = "IMMEDIATE";
    public static final String STATUS_PLAN = "PLAN";
    public static final String STATUS_SENDING = "SENDING";
    public static final String STATUS_FINISH = "FINISH";
    public static final String STATUS_ERROR = "ERROR";

    @Value("${sunten.in-sunten-email-server-id}")
    private Long inSuntenEmailServerId;

    @Value("${server.port}")
    private Long serverPort;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${sunten.email-server-address}")
    private String emailServerAddress;


    public ToolEmailInterfaceServiceImpl(ToolEmailInterfaceDao toolEmailInterfaceDao, ToolEmailInterfaceMapper toolEmailInterfaceMapper, ToolEmailServerService toolEmailServerService) {
        this.toolEmailInterfaceDao = toolEmailInterfaceDao;
        this.toolEmailInterfaceMapper = toolEmailInterfaceMapper;
        this.toolEmailServerService = toolEmailServerService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ToolEmailInterfaceDTO insert(ToolEmailInterface emailInterfaceNew) {
        toolEmailInterfaceDao.insertAllColumn(emailInterfaceNew);
        return toolEmailInterfaceMapper.toDto(emailInterfaceNew);
    }

    @Override
    public ToolEmailInterfaceDTO insert(ToolEmailVo emailVo) {
        ToolEmailInterface emailInterfaceNew = voToInterfaceDO(emailVo);
        toolEmailInterfaceDao.insertAllColumn(emailInterfaceNew);
        return toolEmailInterfaceMapper.toDto(emailInterfaceNew);
    }

    public ToolEmailInterface voToInterfaceDO(ToolEmailVo emailVo) {
        ToolEmailServer emailServer = new ToolEmailServer();
        emailServer.setId(inSuntenEmailServerId);
        ToolEmailInterface emailInterfaceNew = new ToolEmailInterface();
        emailInterfaceNew.setEmailServer(emailServer);
        String sendTo = "";
        List<String> tos = emailVo.getTos();
        for (int i = 0; i < tos.size(); ) {
            sendTo += tos.get(i);
            i++;
            if (i < tos.size()) {
                sendTo += ",";
            }
        }
        emailInterfaceNew.setSendTo(sendTo);
        emailInterfaceNew.setMailSubject(emailVo.getSubject());
        emailInterfaceNew.setMailContent(emailVo.getContent());
        emailInterfaceNew.setStatus(this.STATUS_IMMEDIATE);
        emailInterfaceNew.setPlannedDate(LocalDateTime.now());
        return emailInterfaceNew;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ToolEmailInterface emailInterface = new ToolEmailInterface();
        emailInterface.setId(id);
        this.delete(emailInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ToolEmailInterface emailInterface) {
        toolEmailInterfaceDao.deleteByEntityKey(emailInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ToolEmailInterface emailInterfaceNew) {
        ToolEmailInterface emailInterfaceInDb = Optional.ofNullable(toolEmailInterfaceDao.getByKey(emailInterfaceNew.getId())).orElseGet(ToolEmailInterface::new);
        ValidationUtil.isNull(emailInterfaceInDb.getId(), "EmailInterface", "id", emailInterfaceNew.getId());
        emailInterfaceNew.setId(emailInterfaceInDb.getId());
        toolEmailInterfaceDao.updateAllColumnByKey(emailInterfaceNew);
    }

    @Override
    public ToolEmailInterfaceDTO getByKey(Long id) {
        ToolEmailInterface emailInterface = Optional.ofNullable(toolEmailInterfaceDao.getByKey(id)).orElseGet(ToolEmailInterface::new);
        ValidationUtil.isNull(emailInterface.getId(), "EmailInterface", "id", id);
        return toolEmailInterfaceMapper.toDto(emailInterface);
    }

    @Override
    public List<ToolEmailInterfaceDTO> listAll(ToolEmailInterfaceQueryCriteria criteria) {
        return toolEmailInterfaceMapper.toDto(toolEmailInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ToolEmailInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ToolEmailInterface> page = PageUtil.startPage(pageable);
        List<ToolEmailInterface> emailInterfaces = toolEmailInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(toolEmailInterfaceMapper.toDto(emailInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ToolEmailInterfaceDTO> emailInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ToolEmailInterfaceDTO emailInterfaceDTO : emailInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("收件人", emailInterfaceDTO.getSendTo());
            map.put("主题", emailInterfaceDTO.getMailSubject());
            map.put("计划发送时间", emailInterfaceDTO.getPlannedDate());
            map.put("实际发送时间", emailInterfaceDTO.getSendDate());
            map.put("状态", emailInterfaceDTO.getStatus());
            map.put("弹性域1", emailInterfaceDTO.getAttribute1());
            map.put("弹性域2", emailInterfaceDTO.getAttribute2());
            map.put("弹性域3", emailInterfaceDTO.getAttribute3());
            map.put("弹性域4", emailInterfaceDTO.getAttribute4());
            map.put("弹性域5", emailInterfaceDTO.getAttribute5());
            map.put("serverId", emailInterfaceDTO.getEmailServer().getId());
            map.put("mailContent", emailInterfaceDTO.getMailContent());
            map.put("errorMessage", emailInterfaceDTO.getErrorMessage());
            map.put("id", emailInterfaceDTO.getId());
            map.put("updateBy", emailInterfaceDTO.getUpdateBy());
            map.put("createTime", emailInterfaceDTO.getCreateTime());
            map.put("createBy", emailInterfaceDTO.getCreateBy());
            map.put("updateTime", emailInterfaceDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    private void send(ToolEmailInterface emailInterface) {
        if (emailInterface.getEmailServer() == null || emailInterface.getEmailServer().getId() == null) {
            throw new BadRequestException("邮件服务为空，不能发送邮件");
        }
        ToolEmailServerDTO emailServerDTO = toolEmailServerService.getByKey(emailInterface.getEmailServer().getId());

        String[] temp = emailInterface.getSendTo().split(";");
        Set<String> set = new HashSet<>();
        for (int i = 0; i < temp.length; i++) {
            set.add(temp[i]);
        }
        set.remove(null);
        set.remove("");
        String[] tos = set.toArray(new String[set.size()]);
        if (tos.length == 0) {
            throw new BadRequestException("接收者为空，不能发送邮件");
        }
        // 封装
        MailAccount account = new MailAccount();
        account.setHost(emailServerDTO.getHost());
        account.setPort(Integer.parseInt(emailServerDTO.getPort()));
        account.setAuth(emailServerDTO.getNeedAuthFlag());
        // 设置用户名
        account.setUser(emailServerDTO.getUsername());
        try {
            // 对称解密
            account.setPass(EncryptUtils.desDecrypt(emailServerDTO.getPass()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        account.setFrom(emailServerDTO.getUsername() + "<" + emailServerDTO.getFromUser() + ">");
        // ssl方式发送
        account.setSslEnable(emailServerDTO.getSslEnableFlag());
        String content = emailInterface.getMailContent();
        // 发送
        try {
            Mail.create(account)
                    .setTos(tos)
                    .setTitle(emailInterface.getMailSubject())
                    .setContent(content)
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ToolEmailInterfaceDTO sendAndSave(ToolEmailInterface emailInterface, Boolean isNewEmail) {
        if (isNewEmail) {
            try {
                this.send(emailInterface);
                emailInterface.setSendDate(LocalDateTime.now());
                emailInterface.setStatus(this.STATUS_FINISH);
                emailInterface.setErrorMessage("");
            } catch (Exception ex) {
                emailInterface.setSendDate(null);
                emailInterface.setStatus(this.STATUS_ERROR);
                emailInterface.setErrorMessage(ex.getMessage());
            } finally {
                toolEmailInterfaceDao.insertAllColumn(emailInterface);
            }
        } else {
            emailInterface.setOldStatus(this.STATUS_PLAN);
            emailInterface.setStatus(this.STATUS_SENDING);
            emailInterface.setErrorMessage("");
            int updateCount = toolEmailInterfaceDao.updateStatus(emailInterface);
            if (updateCount == 1) {
                emailInterface.setOldStatus(this.STATUS_SENDING);
                try {
                    this.send(emailInterface);
                    emailInterface.setSendDate(LocalDateTime.now());
                    emailInterface.setStatus(this.STATUS_FINISH);
                    emailInterface.setErrorMessage("");
                } catch (Exception ex) {
                    emailInterface.setSendDate(null);
                    emailInterface.setStatus(this.STATUS_ERROR);
                    emailInterface.setErrorMessage("");
                } finally {
                    toolEmailInterfaceDao.updateStatus(emailInterface);
                }
            }
        }
        return toolEmailInterfaceMapper.toDto(emailInterface);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ToolEmailInterfaceDTO sendAndSaveWithThrow(ToolEmailInterface emailInterface, Boolean isNewEmail) {
        ToolEmailInterfaceDTO emailInterfaceDTO = instance.sendAndSave(emailInterface, isNewEmail);
        if (!StringUtils.isNullOrEmpty(emailInterfaceDTO.getErrorMessage())) {
            throw new InfoCheckWarningMessException(emailInterfaceDTO.getErrorMessage());
        } else {
            if (emailInterfaceDTO.getStatus().equals(this.STATUS_PLAN)) {
                throw new InfoCheckWarningMessException("邮件状态已被其他程序更新，请刷新状态再试！");
            }
        }
        return emailInterfaceDTO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ToolEmailInterfaceDTO sendAndSaveWithThrow(ToolEmailVo emailVo) {
        ToolEmailInterface emailInterfaceNew = voToInterfaceDO(emailVo);
        return this.sendAndSaveWithThrow(emailInterfaceNew, true);
    }


    @Override
    public void sendEmail() {
        if (this.emailServerAddress.equals(getUrl())) {
            log.debug("服务器地址匹配，允许批量发送邮件！当前后端服务地址 " + getUrl());
            ToolEmailInterfaceQueryCriteria criteria = new ToolEmailInterfaceQueryCriteria();
            criteria.setStatus(this.STATUS_PLAN);
            criteria.setPlanEndTime(LocalDateTime.now());
            List<ToolEmailInterface> emailInterfaces = toolEmailInterfaceDao.listAllByCriteria(criteria);
            for (ToolEmailInterface emailInterface : emailInterfaces) {
                instance.sendAndSave(emailInterface, false);
            }
        } else {
            log.error("后端服务器地址不匹配，不允许批量发送邮件！" +
                    "\n    当前后端服务地址 " + getUrl() +
                    "\n    邮件服务配置地址 " + this.emailServerAddress);
        }
    }

    @Override
    public void sendEmailImmediate(Long id) {
        ToolEmailInterfaceQueryCriteria criteria = new ToolEmailInterfaceQueryCriteria();
        criteria.setStatus(this.STATUS_IMMEDIATE);
        criteria.setId(id);
        List<ToolEmailInterface> emailInterfaces = toolEmailInterfaceDao.listAllByCriteria(criteria);
        for (ToolEmailInterface emailInterface : emailInterfaces) {
            instance.sendAndSave(emailInterface, true);
        }
    }

    @Override
    public Map<String, String> checkEmailSetting() {
        Map<String, String> resultMap = new HashMap<>();
        String emailServerAddressCheckResult;
        if (this.emailServerAddress.equals(getUrl())) {
            emailServerAddressCheckResult = "服务器邮箱地址匹配，允许批量发送邮件！<br>" +
                    "    当前后端服务地址 " + getUrl();
            resultMap.put("status", "pass");
        } else {
            emailServerAddressCheckResult = "服务器邮箱地址不匹配，不允许批量发送邮件！<br>" +
                    "    当前后端服务地址 " + getUrl() + "<br>" +
                    "    邮件服务配置地址 " + this.emailServerAddress;
            resultMap.put("status", "error");
        }
        resultMap.put("message", emailServerAddressCheckResult);
        return resultMap;
    }


    @Override
    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://" + address.getHostAddress() + ":" + this.serverPort + this.contextPath;
    }


}
