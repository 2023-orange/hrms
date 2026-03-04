package com.sunten.hrms.cm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.cm.domain.CmDetail;
import com.sunten.hrms.cm.dto.CmDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author liangjw
 * @since 2022-03-24
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CmDetailMapper extends BaseMapper<CmDetailDTO, CmDetail> {

}
