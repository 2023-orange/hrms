<#assign mainVar = entity?uncap_first?replace(package.ModuleName, "")?uncap_first>
<#assign DtoClass = entity + cfg.dtoSuffix>
<#assign QcClass = entity + cfg.qcSuffix>
<#assign dtoVar = DtoClass?uncap_first?replace(package.ModuleName, "")?uncap_first>
<#assign cuxKeyField=[]><#--表key字段 -->
<#list (table.fields + table.commonFields) as field>
    <#if field.keyFlag || field.customMap["KEY"] == "PRI" >
        <#assign cuxKeyField+=[field]>
    </#if>
</#list>
<#assign idFieldParam ><#list cuxKeyField as field>${field.propertyType} ${field.propertyName}<#sep>, </#list></#assign>
package ${package.Service};

import ${package.Entity}.${entity};
import ${cfg.sourceParentUrl}.${cfg.dtoUrl}.${entity}${cfg.dtoSuffix};
import ${cfg.sourceParentUrl}.${cfg.dtoUrl}.${entity}${cfg.qcSuffix};
import ${superServiceClassPackage};
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    ${DtoClass} insert(${entity} ${mainVar}New);

    void delete(${idFieldParam});

    void delete(${entity} ${mainVar});

    void update(${entity} ${mainVar}New);

    ${DtoClass} getByKey(${idFieldParam});

    List<${DtoClass}> listAll(${QcClass} criteria);

    ${"Map<String, Object>"} listAll(${QcClass} criteria, Pageable pageable);

    void download(List<${DtoClass}> ${dtoVar}S, HttpServletResponse response) throws IOException;
}
</#if>
