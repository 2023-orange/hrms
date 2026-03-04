package com.sunten.hrms.re.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.re.dto.ReVocationalDTO;
import com.sunten.hrms.re.domain.ReVocational;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2020-08-28
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReVocationalMapper extends BaseMapper<ReVocationalDTO, ReVocational> {

}
