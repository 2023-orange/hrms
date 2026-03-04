package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmEmployeePhotoDTO;
import com.sunten.hrms.pm.domain.PmEmployeePhoto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2020-09-09
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmEmployeePhotoMapper extends BaseMapper<PmEmployeePhotoDTO, PmEmployeePhoto> {

}
