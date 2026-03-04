<#assign varName = entity?uncap_first?replace(package.ModuleName, "")?uncap_first>
<#assign cuxKeyField=[]><#--表key字段 -->
<#list (table.fields + table.commonFields) as field>
    <#if field.keyFlag || field.customMap["KEY"] == "PRI" >
        <#assign cuxKeyField+=[field]>
    </#if>
</#list>
<#assign idFieldParam><#list cuxKeyField as field>@Param(value = "${field.propertyName}") ${field.propertyType} ${field.propertyName}<#sep>, </#list></#assign>
package ${package.Mapper};

import ${package.Entity}.${entity};
import ${cfg.sourceParentUrl}.${cfg.dtoUrl}.${entity}${cfg.qcSuffix};
import ${superMapperClassPackage};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
@Mapper
@Repository
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

    int insertAllColumn(${entity} ${varName});

    int deleteByKey(${idFieldParam});

    int deleteByEntityKey(${entity} ${varName});

    int updateAllColumnByKey(${entity} ${varName});

    ${entity} getByKey(${idFieldParam});

    List<${entity}> listAllByCriteria(@Param(value = "criteria") ${entity}${cfg.qcSuffix} criteria);

    List<${entity}> listAllByCriteriaPage(@Param(value = "page") Page<${entity}> page, @Param(value = "criteria") ${entity}${cfg.qcSuffix} criteria);
}
</#if>