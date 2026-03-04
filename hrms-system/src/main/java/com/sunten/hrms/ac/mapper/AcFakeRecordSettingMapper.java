package com.sunten.hrms.ac.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.ac.dto.AcFakeRecordSettingDTO;
import com.sunten.hrms.ac.domain.AcFakeRecordSetting;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-12-22
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcFakeRecordSettingMapper extends BaseMapper<AcFakeRecordSettingDTO, AcFakeRecordSetting> {

}
