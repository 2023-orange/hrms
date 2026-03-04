package ${cfg.sourceParentUrl}.${cfg.mapstructUrl};

import ${cfg.baseParentUrl}.base.BaseMapper;
import ${cfg.sourceParentUrl}.${cfg.dtoUrl}.${entity}${cfg.dtoSuffix};
import ${package.Entity}.${entity};
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author ${author}
 * @since ${date}
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${entity}${cfg.mapstructSuffix} extends BaseMapper<${entity}${cfg.dtoSuffix}, ${entity}> {

}
