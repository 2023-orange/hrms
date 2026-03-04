package com.sunten.hrms.fnd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.vo.FndUserPassVo;
import com.sunten.hrms.fnd.vo.RsaKeyPairVo;
import com.sunten.hrms.pm.domain.PmEmployee;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
public interface FndUserService extends IService<FndUser> {

    FndUserDTO insert(FndUser userNew);

    void delete(Long id);

    void update(FndUser userNew);

    void updateUserName(FndUser user);

    FndUserDTO getByKey(Long id);

    List<FndUserDTO> listAll(FndUserQueryCriteria criteria);

    Map<String, Object> listAll(FndUserQueryCriteria criteria, Pageable pageable);

    void download(List<FndUserDTO> userDTOS, HttpServletResponse response) throws IOException;

    FndUserDTO getByName(String userName);

    void updatePass(String username, String encryptPassword);

    void updatePass(FndUserPassVo user) throws InvalidKeySpecException, NoSuchAlgorithmException;

    void updateAvatar(MultipartFile file);

    void updateEmail(String username, String email);

    void updateEmail(String code, FndUser user) throws InvalidKeySpecException, NoSuchAlgorithmException;

    boolean isActiveUser(Long employeeId);

    void resetPass(String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException;

    void insertByEmployee(PmEmployee employee);

    String getDecryptPassword(Long id, String privateKey);

    String getDecryptPassword(String userName, String privateKey);

    void updatePassWithRsaKeyPair(RsaKeyPairVo keyPair, FndUserQueryCriteria userQueryCriteria, Boolean updateDataBase) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String getEmailByEmployeeId(Long employeeId);

    void updateDescription(FndUser fndUser);

    List<FndUser> getuserNameAndAttribute();

    List<FndUser> getuserByNameOrWorkCard(String str);

    void updateUserEmail(Long employeeId, String email);

    Boolean checkUserHaveRole(String permission, Long employeeId);

    String getDepartmentEmailByWorkCard(String workCard);
}
