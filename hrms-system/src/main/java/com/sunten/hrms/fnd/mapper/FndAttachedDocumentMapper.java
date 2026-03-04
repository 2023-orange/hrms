package com.sunten.hrms.fnd.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentDTO;
import com.sunten.hrms.fnd.domain.FndAttachedDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author batan
 * @since 2020-09-25
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FndAttachedDocumentMapper extends BaseMapper<FndAttachedDocumentDTO, FndAttachedDocument> {

}
