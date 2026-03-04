package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.domain.ReDemandAgreeNum;
import com.sunten.hrms.re.dto.ReDemandAgreeNumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;



/**
 * @author zhoujy
 * @since 2022-11-22
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReDemandAgreeNumMapper extends BaseMapper<ReDemandAgreeNumDTO, ReDemandAgreeNum> {

}
