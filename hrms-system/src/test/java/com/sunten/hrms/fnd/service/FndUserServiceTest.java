package com.sunten.hrms.fnd.service;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.dto.FndUserQueryCriteria;
import com.sunten.hrms.fnd.vo.RsaKeyPairVo;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.security.service.RsaService;
import com.sunten.hrms.utils.EncryptUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FndUserServiceTest {
    @Autowired
    FndUserService fndUserService;
    @Autowired
    FndUserDao fndUserDao;
    @Autowired
    RsaService rsaService;

    @Autowired
    PmEmployeeDao pmEmployeeDao;

    @Test
    public void resetPass() throws InvalidKeySpecException, NoSuchAlgorithmException {
        fndUserService.resetPass("acTest", null);
    }

    @Test
    public void batchInitUser() {
        PmEmployeeQueryCriteria criteria = new PmEmployeeQueryCriteria();
        criteria.setEnabledFlag(true);
//        criteria.setWorkCard("000001");
        List<PmEmployee> pmEmployees = pmEmployeeDao.listAllByCriteria(criteria);
        for (PmEmployee employee : pmEmployees) {
            fndUserService.insertByEmployee(employee);
        }

    }



    @Test
    public void batchUpdateUserPassword() {
        FndUserQueryCriteria criteria = new FndUserQueryCriteria();
        criteria.setEnabled(true);
//        criteria.setBlurry("000001");
        List<FndUser> fndUsers = fndUserDao.listAllByCriteria(criteria);
        for (FndUser user : fndUsers) {
            String newPassword = user.getAttribute1();
            if (!com.qiniu.util.StringUtils.isNullOrEmpty(newPassword)) {
                String encryptPassword = "";
                try {
                    encryptPassword = rsaService.passEncrypt(user.getSalt() + newPassword);
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    throw new InfoCheckWarningMessException(e.getMessage());
                }
                fndUserDao.updatePass(user.getUsername(), encryptPassword, LocalDateTime.now(), -1L);
            }
        }


    }

    @Test
    public void getDecryptPassword() throws Exception {
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALe4PF1MTBdu0RQA4JKce/J9hT8qdbOLtyrdtTkKOx6wodWqM70WpPIcUvlXC5z4FI/0cxGW69BvRdP/zfT7L9YdM9GgXhFY35V8oLAJ1L1vxslBFoOXXSJL0To5u/AEBIrbU+83QUb/3DHsLSXijphD28Lz9pRRCpgZ+D4OLKQfAgMBAAECgYArqJZ/vV/fua/pxwOXE6TIzVda2WY7EpqmjyU/ttSxyFvNALNm3fi4qE53fl3+IV4Rj4/AwFKtp6O6a3GwI/kLyt7qKc5Q3IGJfpmE88ME/pBKHvJsdUgtPpXpoXtgefFYy+XwN6tZ7nW6/g347AloVJuqLapx372/+K4ctD+7wQJBAPZgAYWb5Vq+rQmWlXtD3nSMxV/RVPUBamAN7j0bBcn5NCzBTIGkRPSDw5Jq0vVT3vSiacTh1YQafNEiZQYWFrECQQC+5Z1EeCiX0GV4zxEql7JX8l5dh6pyjBHBskOMPoUUopXySKoMDLEkK/4eLFSrPkSzVFN5gB+l683804YUzbvPAkBM9gCDtAcZKabz784SC0laLv8Yx1M6lY6dIrzg6agNR4M818UGWkIP/3kAK85qRCDJWlKf5cvE0GFdEtlr5UqBAkBuPEAd6tleGZyPL9v04Za+TJqLni0iappSZTO2h9/ns5+tQqLXxHiCr9jV6bmXDaU0fWyazA76jHnuFuPlnYxnAkEAvbmOWvuCIFGbr2aqx6LwTV0NWIjO4ONZxY7jz8PCxxfEYemUITjnKllbX3yvlzndSYgmgGiXXkoIPnRdf0O/nw==";
        String transactionPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANL378k3RiZHWx5AfJqdH9xRNBmD9wGD\n" +
                "2iRe41HdTNF8RUhNnHit5NpMNtGL0NPTSSpPjjI1kJfVorRvaQerUgkCAwEAAQ==";
        String transactionPrivateKey = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEA0vfvyTdGJkdbHkB8\n" +
                "mp0f3FE0GYP3AYPaJF7jUd1M0XxFSE2ceK3k2kw20YvQ09NJKk+OMjWQl9WitG9p\n" +
                "B6tSCQIDAQABAkA2SimBrWC2/wvauBuYqjCFwLvYiRYqZKThUS3MZlebXJiLB+Ue\n" +
                "/gUifAAKIg1avttUZsHBHrop4qfJCwAI0+YRAiEA+W3NK/RaXtnRqmoUUkb59zsZ\n" +
                "UBLpvZgQPfj1MhyHDz0CIQDYhsAhPJ3mgS64NbUZmGWuuNKp5coY2GIj/zYDMJp6\n" +
                "vQIgUueLFXv/eZ1ekgz2Oi67MNCk5jeTF2BurZqNLR3MSmUCIFT3Q6uHMtsB9Eha\n" +
                "4u7hS31tj1UWE+D+ADzp59MGnoftAiBeHT7gDMuqeJHPL4b+kC+gzV4FGTfhR9q3\n" +
                "tTbklZkD2A==";
        String pass = fndUserService.getDecryptPassword("000480",privateKey );
        System.out.println(pass);
        String passD =  EncryptUtils.desDecrypt(pass);
        System.out.println(passD);
//         String passDecrypt;
//        try {
//            passDecrypt = rsaService.passDecrypt("HBvwB2FVglH50AuLuC2sisgADNTgnJfhr9DUy5PBYAPICq_jgjTGy0KMFPLf911dysczUmUir_U1oU_jcRAoKZO47Zq33CkM2k4fZ2hWd8VIsEntLe10HegrrFKu_n2h4_CtQ8BcFidI30LMz7mwVqgR-qBkeQKj1Ghcw9rGXvo", privateKey);
//        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
//            throw new InfoCheckWarningMessException(e.getMessage());
//        } // 206130  0260
//        if (passDecrypt.startsWith("GSsXwHOaxy")) {
//            System.out.println(passDecrypt.replaceFirst("GSsXwHOaxy", ""));
//        }
//        String pass = fndUserService.getDecryptPassword("admin", privateKey, transactionPublicKey);
//        System.out.println(pass);
//        try {
//            System.out.println(rsaService.transactionPrivateDecrypt(pass, transactionPrivateKey));
//        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
//            throw new InfoCheckWarningMessException(e.getMessage());
//        }
    }


    @Test
    public void updateRsaKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        RsaKeyPairVo keyPair = new RsaKeyPairVo();
        keyPair.setOldPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3uDxdTEwXbtEUAOCSnHvyfYU/KnWzi7cq3bU5CjsesKHVqjO9FqTyHFL5Vwuc+BSP9HMRluvQb0XT/830+y/WHTPRoF4RWN+VfKCwCdS9b8bJQRaDl10iS9E6ObvwBASK21PvN0FG/9wx7C0l4o6YQ9vC8/aUUQqYGfg+DiykHwIDAQAB");
        keyPair.setOldPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALe4PF1MTBdu0RQA4JKce/J9hT8qdbOLtyrdtTkKOx6wodWqM70WpPIcUvlXC5z4FI/0cxGW69BvRdP/zfT7L9YdM9GgXhFY35V8oLAJ1L1vxslBFoOXXSJL0To5u/AEBIrbU+83QUb/3DHsLSXijphD28Lz9pRRCpgZ+D4OLKQfAgMBAAECgYArqJZ/vV/fua/pxwOXE6TIzVda2WY7EpqmjyU/ttSxyFvNALNm3fi4qE53fl3+IV4Rj4/AwFKtp6O6a3GwI/kLyt7qKc5Q3IGJfpmE88ME/pBKHvJsdUgtPpXpoXtgefFYy+XwN6tZ7nW6/g347AloVJuqLapx372/+K4ctD+7wQJBAPZgAYWb5Vq+rQmWlXtD3nSMxV/RVPUBamAN7j0bBcn5NCzBTIGkRPSDw5Jq0vVT3vSiacTh1YQafNEiZQYWFrECQQC+5Z1EeCiX0GV4zxEql7JX8l5dh6pyjBHBskOMPoUUopXySKoMDLEkK/4eLFSrPkSzVFN5gB+l683804YUzbvPAkBM9gCDtAcZKabz784SC0laLv8Yx1M6lY6dIrzg6agNR4M818UGWkIP/3kAK85qRCDJWlKf5cvE0GFdEtlr5UqBAkBuPEAd6tleGZyPL9v04Za+TJqLni0iappSZTO2h9/ns5+tQqLXxHiCr9jV6bmXDaU0fWyazA76jHnuFuPlnYxnAkEAvbmOWvuCIFGbr2aqx6LwTV0NWIjO4ONZxY7jz8PCxxfEYemUITjnKllbX3yvlzndSYgmgGiXXkoIPnRdf0O/nw==");
        keyPair.setNewPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5XatsfxEpJEDT5TOo8c9XZvK6\n" +
                "77Hum5E3abE241Kt2trrKbpkX35xQi9ywt04BCsUb8YlHF/4Cd3DNgnAlHoOOL39\n" +
                "TrZwosi7pF6e+LX4VexlaTMkosT6K12emJPG9loJdL/W2oKjTmjPH/8ttMbdEnI3\n" +
                "a4Ny3VwnKKmicgLWAwIDAQAB\n");
        keyPair.setNewPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALldq2x/ESkkQNPl\n" +
                "M6jxz1dm8rrvse6bkTdpsTbjUq3a2uspumRffnFCL3LC3TgEKxRvxiUcX/gJ3cM2\n" +
                "CcCUeg44vf1OtnCiyLukXp74tfhV7GVpMySixPorXZ6Yk8b2Wgl0v9bagqNOaM8f\n" +
                "/y20xt0Scjdrg3LdXCcoqaJyAtYDAgMBAAECgYBCdwa/eXRixyADmnyCiGnrEfvY\n" +
                "8ZdgUiGot9oWLz53HjE+/xBZazBAVhYDVUHTExOLWWS368uxeYAtvdOTXOnCUVf3\n" +
                "ybSimXPsg7fDtegmNcHEl13BKGW9M+aG7jNm72mObfG2G0dcBlp8YbdIhnjNbWgs\n" +
                "XnHzW3JvsT72LqbekQJBAOK/iNGoByTLVkVvXBMb8AI/hSeBm/ntaRAJ8qWcQ10c\n" +
                "bgRJ/DqJQ/oiJbQJrx23jqWdmh8KgRV6LxcttWceqhsCQQDRR3c5+hHLOSDqlTH+\n" +
                "K1u42U8lYEIeY+tsL4bmuqtMdC1nVGA9q/d1D8fCcJhQTJYuoTJjbDu0JRX/b7WC\n" +
                "PEI5AkEAk/69d1H7b6mD4of/ib1nVqBM8W8n9eVd+Ij1peArG5/NsuDzTuV+x8j3\n" +
                "4N2dPYEsCq6mBHKfs/vutmswBmE+VwJAYpfTkInCNkacvkc5fS+6D3S5N7eIjr6u\n" +
                "G5KscCjDKMqW1VZH3OYWN50OlAjtiO771c31aieEXRMXsPZUwZ1X6QJARtty53Ko\n" +
                "IqHeFh/gHYaPpEFRlJrhSANkmvvFRd6HzXtguwcUHaz0E1sV49hYgCESg7+chWmE\n" +
                "zH0UGfvN23Bg4w==\n");
        FndUserQueryCriteria userQueryCriteria = new FndUserQueryCriteria();
        userQueryCriteria.setBlurry("00267_");
        fndUserService.updatePassWithRsaKeyPair(keyPair,userQueryCriteria, false);
    }

    @Test
    public void testEmailPass() throws Exception {
        String pass = EncryptUtils.desDecrypt("68D9872E5EBB6715");
        System.out.println(pass);
    }
}
