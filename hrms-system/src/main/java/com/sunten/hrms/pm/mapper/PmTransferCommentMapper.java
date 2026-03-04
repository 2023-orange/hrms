package com.sunten.hrms.pm.mapper;

import com.sunten.hrms.base.BaseMapper;
import com.sunten.hrms.pm.dto.PmTransferCommentDTO;
import com.sunten.hrms.pm.domain.PmTransferComment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xukai
 * @since 2021-05-24
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmTransferCommentMapper extends BaseMapper<PmTransferCommentDTO, PmTransferComment> {

}
