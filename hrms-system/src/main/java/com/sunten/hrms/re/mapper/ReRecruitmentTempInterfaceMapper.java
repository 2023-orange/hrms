package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReRecruitmentTempInterfaceDTO;
import com.sunten.hrms.re.domain.ReRecruitmentTempInterface;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2021-09-08
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReRecruitmentTempInterfaceMapper extends BaseMapper<ReRecruitmentTempInterfaceDTO, ReRecruitmentTempInterface> {

}
