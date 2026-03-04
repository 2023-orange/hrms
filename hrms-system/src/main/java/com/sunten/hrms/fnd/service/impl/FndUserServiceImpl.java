package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.EntityExistException;
import com.sunten.hrms.exception.EntityNotFoundException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndUserAvatarDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.dao.FndUsersRolesDao;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.domain.FndUserAvatar;
import com.sunten.hrms.fnd.domain.FndUsersRoles;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndUserMapper;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.fnd.vo.DeptChargeEmail;
import com.sunten.hrms.fnd.vo.FndUserPassVo;
import com.sunten.hrms.fnd.vo.RsaKeyPairVo;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.security.service.RsaService;
import com.sunten.hrms.tool.domain.ToolVerificationCode;
import com.sunten.hrms.tool.service.ToolVerificationCodeService;
import com.sunten.hrms.utils.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Service
@CacheConfig(cacheNames = "user")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndUserServiceImpl extends ServiceImpl<FndUserDao, FndUser> implements FndUserService {
    private final FndUserDao fndUserDao;
    private final FndUserMapper fndUserMapper;
    private final RedisService redisService;
    private final FndUserAvatarDao fndUserAvatarDao;
    private final FndRoleService fndRoleService;
    private final FndUsersRolesDao fndUsersRolesDao;
    private final RsaService rsaService;
    private final ToolVerificationCodeService toolVerificationCodeService;
    @Autowired
    private FndUserService fndUserService;
    @Value("${file.avatar}")
    private String avatar;

    @Value("${sunten.default-password}")
    private String defaultPassword;

    @Value("${sunten.rsa.pass-public-key}")
    private String passPublicKey;

    public FndUserServiceImpl(FndUserDao fndUserDao, FndUserMapper fndUserMapper, RedisService redisService, FndUserAvatarDao fndUserAvatarDao, FndRoleService fndRoleService, FndUsersRolesDao fndUsersRolesDao, RsaService rsaService, ToolVerificationCodeService toolVerificationCodeService) {
        this.fndUserDao = fndUserDao;
        this.fndUserMapper = fndUserMapper;
        this.redisService = redisService;
        this.fndUserAvatarDao = fndUserAvatarDao;
        this.fndRoleService = fndRoleService;
        this.fndUsersRolesDao = fndUsersRolesDao;
        this.rsaService = rsaService;
        this.toolVerificationCodeService = toolVerificationCodeService;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndUserDTO insert(FndUser userNew) {
        if (fndUserDao.getByUsername(userNew.getUsername()) != null) {
            throw new EntityExistException(FndUser.class, "username", userNew.getUsername());
        }
//        if (fndUserDao.getByEmail(userNew.getEmail()) != null) {
//            throw new EntityExistException(FndUser.class, "email", userNew.getEmail());
//        }
        userNew.setSalt(RandomStringUtils.randomAlphanumeric(10));
        // 默认密码 123456，此密码是加密后的字符
//        userNew.setPassword("e10adc3949ba59abbe56e057f20f883e");
        String password = defaultPassword;
        if (userNew.getEmployee() != null) {
            String idNumber = userNew.getEmployee().getIdNumber();
            if (idNumber != null && !idNumber.equals("")) {
                password = idNumber.substring(idNumber.length() - 6);
            }
        }
        try {
            userNew.setPassword(rsaService.passEncrypt(userNew.getSalt() + password));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new InfoCheckWarningMessException(e.getMessage());
        }
        userNew.setLastPasswordResetTime(LocalDateTime.now());
        fndUserDao.insertAllColumn(userNew);
        //新增用户角色表
        Set<FndRole> roleNew = userNew.getRoles();
        List<Long> roleIdsNew = roleNew.stream().map(FndRole::getId).sorted().collect(Collectors.toList());
        FndUsersRoles usersRoles = new FndUsersRoles();
        usersRoles.setUserId(userNew.getId());
        roleIdsNew.stream().forEach((id) -> {
            usersRoles.setRoleId(id);
            fndUsersRolesDao.insertAllColumn(usersRoles);
        });
        return fndUserMapper.toDto(userNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fndUserDao.deleteByKey(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndUser userNew) {
        FndUser userInDb = Optional.ofNullable(fndUserDao.getByKey(userNew.getId())).orElseGet(FndUser::new);
        ValidationUtil.isNull(userInDb.getId(), "User", "id", userNew.getId());
        FndUser user1 = fndUserDao.getByUsername(userInDb.getUsername());
        if (user1 != null && !userInDb.getId().equals(user1.getId())) {
            throw new EntityExistException(FndUser.class, "username", userNew.getUsername());
        }
//        FndUser user2 = fndUserDao.getByEmail(userInDb.getEmail());
//        if (user2 != null && !userInDb.getId().equals(user2.getId())) {
//            throw new EntityExistException(FndUser.class, "email", userNew.getEmail());
//        }
        userNew.setId(userInDb.getId());
        userNew.setUserAvatar(userInDb.getUserAvatar());
        fndUserDao.updateAllColumnByKey(userNew);
        //处理用户角色关联roles
        List<FndRoleSmallDTO> roleOld = fndRoleService.listByUserId(userInDb.getId());
        Set<FndRole> roleNew = userNew.getRoles();
        List<Long> roleIdsOld = roleOld.stream().map(FndRoleSmallDTO::getId).sorted().collect(Collectors.toList());
        List<Long> roleIdsNew = roleNew.stream().map(FndRole::getId).sorted().collect(Collectors.toList());
        ListUtils.listComp(roleIdsOld, roleIdsNew);
        FndUsersRoles usersRoles = new FndUsersRoles();
        usersRoles.setUserId(userInDb.getId());
        roleIdsOld.stream().forEach((id) -> {
            usersRoles.setRoleId(id);
            fndUsersRolesDao.deleteByKey(userInDb.getId(), id);
        });
        roleIdsNew.stream().forEach((id) -> {
            usersRoles.setRoleId(id);
            fndUsersRolesDao.insertAllColumn(usersRoles);
        });
        // 如果用户的角色改变了，需要手动清理下缓存
        if (roleIdsOld.size() + roleIdsNew.size() > 0) {
            String key = "role::loadPermissionByUser:" + userInDb.getId();
            redisService.delete(key);
            key = "role::findByUserId:" + userInDb.getId();
            redisService.delete(key);
        }
    }

    @Override
    @Cacheable(key = "#p0")
    public FndUserDTO getByKey(Long id) {
        FndUser user = Optional.ofNullable(fndUserDao.getByKey(id)).orElseGet(FndUser::new);
        ValidationUtil.isNull(user.getId(), "User", "id", id);
        FndUserDTO userDTO = fndUserMapper.toDto(user);
        setRoles(userDTO);
        return userDTO;
    }

    @Override
    @Cacheable
    public List<FndUserDTO> listAll(FndUserQueryCriteria criteria) {
        List<FndUser> users = fndUserDao.listAllByCriteria(criteria);
        List<FndUserDTO> userDTOS = fndUserMapper.toDto(users);
        userDTOS.stream().forEach(this::setRoles);
        return userDTOS;
    }

    @Override
//    @Cacheable
    public Map<String, Object> listAll(FndUserQueryCriteria criteria, Pageable pageable) {
        Page<FndUser> page = PageUtil.startPage(pageable);
        List<FndUser> users = fndUserDao.listAllByCriteriaPage(page, criteria);
        List<FndUserDTO> userDTOS = fndUserMapper.toDto(users);
        userDTOS.stream().forEach(this::setRoles);
        return PageUtil.toPage(userDTOS, page.getTotal());
    }

    @Override
    public void download(List<FndUserDTO> userDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndUserDTO userDTO : userDTOS) {
            List roles = userDTO.getRoles().stream().map(FndRoleSmallDTO::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", userDTO.getUsername());
            map.put("描述", userDTO.getDescription());
            map.put("头像", userDTO.getAvatar());
            map.put("邮箱", userDTO.getEmail());
            map.put("状态", userDTO.getEnabledFlag() ? "启用" : "禁用");
            map.put("手机号码", userDTO.getPhone());
            map.put("角色", roles);
            map.put("最后修改密码的时间", userDTO.getLastPasswordResetTime());
            map.put("创建日期", userDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Cacheable(key = "'loadUserByUsername:'+#p0")
    public FndUserDTO getByName(String userName) {
        FndUser user;
        if (ValidationUtil.isEmail(userName)) {
            user = fndUserDao.getByEmail(userName);
        } else {
            user = fndUserDao.getByUsername(userName);
        }
        if (user == null) {
            throw new EntityNotFoundException(FndUser.class, "name", userName);
        } else {
            FndUserDTO userDTO = fndUserMapper.toDto(user);
            setRoles(userDTO);
            return userDTO;
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String encryptPassword) {
        fndUserDao.updatePass(username, encryptPassword, LocalDateTime.now(), SecurityUtils.getUserId());
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(FndUserPassVo user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        FndUserPassVo userE = new FndUserPassVo();
        userE.setOldPass(rsaService.passEncrypt(SecurityUtils.getSalt() + rsaService.transactionPrivateDecrypt(user.getOldPass())));
        userE.setNewPass(rsaService.passEncrypt(SecurityUtils.getSalt() + rsaService.transactionPrivateDecrypt(user.getNewPass())));
        if (!userDetails.getPassword().equals(userE.getOldPass())) {
            throw new InfoCheckWarningMessException("修改失败，旧密码错误");
        }
        if (userDetails.getPassword().equals(userE.getNewPass())) {
            throw new InfoCheckWarningMessException("新密码不能与旧密码相同");
        }
        fndUserDao.updatePass(userDetails.getUsername(), userE.getNewPass(), LocalDateTime.now(), SecurityUtils.getUserId());
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(MultipartFile multipartFile) {
        FndUser user = fndUserDao.getByUsername(SecurityUtils.getUsername());
        FndUserAvatar userAvatar = user.getUserAvatar();
        String oldPath = "";
        if (userAvatar != null) {
            oldPath = userAvatar.getPath();
        }
        File file = FileUtil.upload(multipartFile, avatar);
        assert file != null;
        FndUserAvatar userAvatar1 = new FndUserAvatar(userAvatar, file.getName(), file.getPath(), FileUtil.getSize(multipartFile.getSize()));
        fndUserAvatarDao.insertAllColumn(userAvatar1);
        userAvatar = userAvatar1;
        user.setUserAvatar(userAvatar);
        fndUserDao.updateAvatar(user);
        if (StringUtils.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        fndUserDao.updateEmail(username, email, LocalDateTime.now(), SecurityUtils.getUserId());
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateUserName(FndUser user) {
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy(SecurityUtils.getUserId());
        fndUserDao.updateUserName(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateDescription(FndUser fndUser) {
        fndUser.setUpdateTime(LocalDateTime.now());
        fndUser.setUpdateBy(SecurityUtils.getUserId());
        fndUserDao.updateDescription(fndUser);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String code, FndUser user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        UserDetails userDetails = SecurityUtils.getUserDetails();
        String password = rsaService.passEncrypt(SecurityUtils.getSalt() + rsaService.transactionPrivateDecrypt(user.getPassword()));
        if (!userDetails.getPassword().equals(password)) {
            throw new InfoCheckWarningMessException("密码错误");
        }
        ToolVerificationCode verificationCode = new ToolVerificationCode(code, GlobalConstant.RESET_MAIL, "email", user.getEmail());
        toolVerificationCodeService.validated(verificationCode);
        fndUserDao.updateEmail(userDetails.getUsername(), user.getEmail(), LocalDateTime.now(), SecurityUtils.getUserId());
    }

    private void setRoles(FndUserDTO userDTO) {
        userDTO.setRoles(new HashSet<>(fndRoleService.listByUserId(userDTO.getId())));
    }

    @Override
    public boolean isActiveUser(Long employeeId) {
        //验证是否本人
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (!user.getEmployee().getId().equals(employeeId)) {
            return false;
        }
        return true;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void resetPass(String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        FndUser user = fndUserDao.getByUsername(username);
        if (user == null) {
            throw new InfoCheckWarningMessException("用户" + username + "不存在！");
        }

        String newPassword = defaultPassword;
        if (password == null || password.equals("")) {
            if (user.getEmployee() != null) {
                String idNumber = user.getEmployee().getIdNumber();
                if (idNumber != null && !idNumber.equals("")) {
                    newPassword = idNumber.substring(idNumber.length() - 6);
                }
            }
        } else {
            newPassword = rsaService.transactionPrivateDecrypt(password);
        }
        String encryptPassword = rsaService.passEncrypt(user.getSalt() + newPassword);
        fndUserDao.updatePass(username, encryptPassword, LocalDateTime.now(), SecurityUtils.getUserId());
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void insertByEmployee(PmEmployee employee) {
        FndUser user = fndUserDao.getByUsername(employee.getWorkCard());
        if (user == null) {
            FndUser userNew = new FndUser();
            userNew.setUsername(employee.getWorkCard());
            userNew.setDescription(employee.getName());
            userNew.setEmployee(employee);
            String email = employee.getEmailInside();
            if (com.qiniu.util.StringUtils.isNullOrEmpty(email)) {
                email = "@in-sunten.com";
            }
            userNew.setEmail(email);
            userNew.setPhone("");
            userNew.setEnabledFlag(true);
            FndRole role = new FndRole();
            role.setId(2L);
            Set<FndRole> roles = new HashSet<>();
            roles.add(role);
            userNew.setRoles(roles);
            fndUserService.insert(userNew);
        } else {
            user.setEmployee(employee);
            fndUserService.update(user);
        }
    }

    @Override
    public String getDecryptPassword(Long id, String privateKey) {
        FndUser user = fndUserDao.getByKey(id);
        return passDecrypt(privateKey, user);
    }

    @Override
    public String getDecryptPassword(String userName, String privateKey) {
        FndUser user = fndUserDao.getByUsername(userName);
        return passDecrypt(privateKey, user);
    }

    private String passDecrypt(String privateKey, FndUser user) {
        String passDecrypt;
        if (user == null) {
            throw new InfoCheckWarningMessException("用户不存在");
        } else {
            try {
                passDecrypt = rsaService.passDecrypt(user.getPassword(), privateKey);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new InfoCheckWarningMessException(e.getMessage());
            }
            if (passDecrypt.startsWith(user.getSalt())) {
                passDecrypt = passDecrypt.replaceFirst(user.getSalt(), "");
            }
        }
        try {
            passDecrypt = EncryptUtils.desEncrypt(passDecrypt);
        } catch (Exception e) {
            throw new InfoCheckWarningMessException(e.getMessage());
        }
        return passDecrypt;
    }

    private void checkRsaKeyPair(RsaKeyPairVo keyPair) {
        String passTest = "Abc1234defG279/";
        String passEncrypt = "";
        String passDecrypt = "";
        log.debug("oldPrivateKey:" + keyPair.getOldPrivateKey());
        log.debug("oldPublicKey:" + keyPair.getOldPublicKey());
        log.debug("newPrivateKey:" + keyPair.getNewPrivateKey());
        log.debug("newPublicKey:" + keyPair.getNewPublicKey());
        if (StringUtils.isBlank(keyPair.getOldPublicKey())) {
            throw new InfoCheckWarningMessException("旧公钥为空！");
        }
        if (!keyPair.getOldPublicKey().equals(passPublicKey)) {
            throw new InfoCheckWarningMessException("旧公钥与在用系统公钥不一致！");
        }
        if (StringUtils.isBlank(keyPair.getOldPrivateKey())) {
            throw new InfoCheckWarningMessException("旧私钥为空！");
        }
        if (StringUtils.isBlank(keyPair.getNewPublicKey())) {
            throw new InfoCheckWarningMessException("新公钥为空！");
        }
        if (StringUtils.isBlank(keyPair.getNewPrivateKey())) {
            throw new InfoCheckWarningMessException("新私钥为空！");
        }
        try {
            passEncrypt = rsaService.passEncrypt(passTest);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new InfoCheckWarningMessException(e.getMessage());
        }
        try {
            passDecrypt = rsaService.passDecrypt(passEncrypt, keyPair.getOldPrivateKey());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new InfoCheckWarningMessException(e.getMessage());
        }
        if (!passTest.equals(passDecrypt)) {
            throw new InfoCheckWarningMessException("旧私钥与旧公钥不匹配！");
        }
        try {
            passEncrypt = rsaService.passEncrypt(passTest, keyPair.getNewPublicKey());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new InfoCheckWarningMessException(e.getMessage());
        }
        passDecrypt = "";
        try {
            passDecrypt = rsaService.passDecrypt(passEncrypt, keyPair.getNewPrivateKey());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new InfoCheckWarningMessException(e.getMessage());
        }
        if (!passTest.equals(passDecrypt)) {
            throw new InfoCheckWarningMessException("新私钥与新公钥不匹配！");
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updatePassWithRsaKeyPair(RsaKeyPairVo keyPair, FndUserQueryCriteria userQueryCriteria, Boolean updateDataBase) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.checkRsaKeyPair(keyPair);
        List<FndUser> users = fndUserDao.listAllByCriteria(userQueryCriteria);
        int i = 1;
        for (FndUser user : users) {
            String passDecryptOld = rsaService.passDecrypt(user.getPassword(), keyPair.getOldPrivateKey());
            String passEncryptNew = rsaService.passEncrypt(passDecryptOld, keyPair.getNewPublicKey());
            String passDecryptNew = rsaService.passDecrypt(passEncryptNew, keyPair.getNewPrivateKey());
            log.debug((i++) + ". ======================================================");
            log.debug("user:" + user.getUsername());
            log.debug("passEncryptOld:" + user.getPassword());
            log.debug("passDecryptOld:" + passDecryptOld);
            log.debug("passEncryptNew:" + passEncryptNew);
            log.debug("passDecryptNew:" + passDecryptNew);
            if (!passDecryptOld.equals(passDecryptNew)) {
                throw new InfoCheckWarningMessException("密码加密解密不一致！");
            }
            if (updateDataBase) {
                this.updatePass(user.getUsername(), passEncryptNew);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public void updateUserEmail(Long employeeId, String email) {
        fndUserDao.updateUserEmail(employeeId, email);
    }

    @Override
    public String getEmailByEmployeeId(Long employeeId) {
        return fndUserDao.getEmailByEmployeeId(employeeId);
    }

    @Override
    public List<FndUser> getuserNameAndAttribute() {
        return fndUserDao.getuserNameAndAttribute();
    }

    @Override
    public List<FndUser> getuserByNameOrWorkCard(String str) {
        return fndUserDao.getuserByNameOrWorkCard(str);
    }

    @Override
    public Boolean checkUserHaveRole(String permission, Long employeeId) {
        return fndUserDao.checkUserHaveRole(permission, employeeId);
    }

    @Override
    public String getDepartmentEmailByWorkCard(String workCard) {
        String finalSendTarget=null;
        //查询个人邮箱
        String sEmail=fndUserDao.getEmailByWorkCard(workCard);
        if (null == sEmail || "".equals(sEmail) || "@in-sunten.com".equals(sEmail)) {
            //根据workCard查询DeptId
            List<DeptChargeEmail> deptChargeEmails = fndUserDao.selectDeptChargeEmailList(fndUserDao.getDeptIdByWorkCard(workCard));
            List<DeptChargeEmail> departmentChargeEmails = fndUserDao.selectDeptChargeEmailList(fndUserDao.getDepartmentIdByWorkCard(workCard));
            List<DeptChargeEmail> superiorList = departmentChargeEmails.stream().filter(DeptChargeEmail -> DeptChargeEmail.getJobName().equals("主管")).collect(Collectors.toList());
            if (superiorList.size() > 0) {
                // 找到主管
                finalSendTarget=superiorList.get(0).getEmail();
//            System.out.println("主管 = " + finalSendTarget);
            } else {
                List<DeptChargeEmail> managerList = deptChargeEmails.stream().filter(DeptChargeEmail -> DeptChargeEmail.getJobName().equals("经理")).collect(Collectors.toList());
//            System.out.println("经理List="+managerList);
                if (managerList.size() > 0) {
                    // 找到经理
                    finalSendTarget=managerList.get(0).getEmail();
//                System.out.println("经理 = " + finalSendTarget);
                } else {
                    // 发给管理人员
                    finalSendTarget="luojb@in-sunten.com";
                    System.out.println("没找到 = " + finalSendTarget);
                }
            }
        }else{
            finalSendTarget=sEmail;
        }
        return finalSendTarget;
    }
}
