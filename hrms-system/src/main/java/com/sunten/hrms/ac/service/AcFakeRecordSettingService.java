package com.sunten.hrms.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.domain.AcFakeRecordSetting;
import com.sunten.hrms.ac.dto.AcFakeRecordSettingDTO;
import com.sunten.hrms.ac.dto.AcFakeRecordSettingQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xukai
 * @since 2021-12-22
 */
public interface AcFakeRecordSettingService extends IService<AcFakeRecordSetting> {

    AcFakeRecordSettingDTO insert(AcFakeRecordSetting fakeRecordSettingNew);

    void delete(String username);

    void delete(AcFakeRecordSetting fakeRecordSetting);

    void update(AcFakeRecordSetting fakeRecordSettingNew);

    AcFakeRecordSettingDTO getByKey(String username);

    List<AcFakeRecordSettingDTO> listAll(AcFakeRecordSettingQueryCriteria criteria);

    Map<String, Object> listAll(AcFakeRecordSettingQueryCriteria criteria, Pageable pageable);

    void download(List<AcFakeRecordSettingDTO> fakeRecordSettingDTOS, HttpServletResponse response) throws IOException;
    // 修改开关
    void updateEnabled(AcFakeRecordSetting fakeRecordSettingNew);
}
