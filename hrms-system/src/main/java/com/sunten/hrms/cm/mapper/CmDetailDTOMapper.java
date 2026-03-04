package com.sunten.hrms.cm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.cm.dto.CmDetailDTO;
import com.sunten.hrms.cm.vo.CmExcelDataVo;
import com.sunten.hrms.cm.vo.CmExcelVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author tanba
 * @since 2023-02-20
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CmDetailDTOMapper extends BaseMapper<CmDetailDTO, CmExcelDataVo> {
}
