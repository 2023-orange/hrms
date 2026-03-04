<#assign mainVarCap = entity?uncap_first?replace(package.ModuleName, "")>
<#assign mainVar = mainVarCap?uncap_first>
<#assign DtoClass= entity + cfg.dtoSuffix>
<#assign QcClass = entity + cfg.qcSuffix>
<#assign mapstructClass = entity + cfg.mapstructSuffix>
<#assign dtoVar = DtoClass?uncap_first?replace(package.ModuleName, "")?uncap_first>
<#assign daoVar = table.mapperName?uncap_first>
<#assign mapstructVar = (entity+cfg.mapstructSuffix)?uncap_first>
<#assign isContainIdField = false>
<#assign cuxKeyField=[]><#--表key字段 -->
<#list (table.fields+table.commonFields) as field>
    <#if field.name == "id">
        <#assign isContainIdField = true>
    </#if>
    <#if field.keyFlag || field.customMap["KEY"] == "PRI" >
        <#assign cuxKeyField+=[field]>
    </#if>
</#list>
<#assign idFieldParam ><#list cuxKeyField as field>${field.propertyType} ${field.propertyName}<#sep>, </#list></#assign>
<#assign idFieldParamValue><#list cuxKeyField as field>${field.propertyName}<#sep>, </#list></#assign>
package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${cfg.sourceParentUrl}.${cfg.dtoUrl}.${entity}${cfg.dtoSuffix};
import ${cfg.sourceParentUrl}.${cfg.dtoUrl}.${entity}${cfg.qcSuffix};
import ${cfg.sourceParentUrl}.${cfg.mapstructUrl}.${entity}${cfg.mapstructSuffix};
import ${cfg.baseParentUrl}.utils.*;
import ${superServiceImplClassPackage};
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {
    private final ${table.mapperName} ${daoVar};
    private final ${mapstructClass} ${mapstructVar};

    public ${table.serviceImplName}(${table.mapperName} ${daoVar}, ${mapstructClass} ${mapstructVar}) {
        this.${daoVar} = ${daoVar};
        this.${mapstructVar} = ${mapstructVar};
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${DtoClass} insert(${entity} ${mainVar}New) {
        ${daoVar}.insertAllColumn(${mainVar}New);
        return ${mapstructVar}.toDto(${mainVar}New);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(${idFieldParam}) {
        ${entity} ${mainVar} = new ${entity}();
    <#list cuxKeyField as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
        ${mainVar}.set${field.capitalName}(${field.propertyName});
    </#list>
        this.delete(${mainVar});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(${entity} ${mainVar}) {
        // TODO    确认删除前是否需要做检查
        ${daoVar}.deleteByEntityKey(${mainVar});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(${entity} ${mainVar}New) {
        ${entity} ${mainVar}InDb = Optional.ofNullable(${daoVar}.getByKey(<#list cuxKeyField as field><#if field.propertyType == "boolean"><#assign getprefix="is"/><#else><#assign getprefix="get"/></#if>${mainVar}New.${getprefix}${field.capitalName}()<#sep>, </#list>)).orElseGet(${entity}::new);
    <#list cuxKeyField as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
        ValidationUtil.isNull(${mainVar}InDb.${getprefix}${field.capitalName}() ,"${mainVarCap}", "${field.propertyName}", ${mainVar}New.${getprefix}${field.capitalName}());
        ${mainVar}New.set${field.capitalName}(${mainVar}InDb.${getprefix}${field.capitalName}());
    </#list>
        ${daoVar}.updateAllColumnByKey(${mainVar}New);
    }

    @Override
    public ${DtoClass} getByKey(<#list cuxKeyField as field>${field.propertyType} ${field.propertyName}<#sep>, </#list>) {
        ${entity} ${mainVar} = Optional.ofNullable(${daoVar}.getByKey(${idFieldParamValue})).orElseGet(${entity}::new);
        <#list cuxKeyField as field>
            <#if field.propertyType == "boolean">
                <#assign getprefix="is"/>
            <#else>
                <#assign getprefix="get"/>
            </#if>
        ValidationUtil.isNull(${mainVar}.${getprefix}${field.capitalName}() ,"${mainVarCap}", "${field.propertyName}", ${field.propertyName});
        </#list>
        return ${mapstructVar}.toDto(${mainVar});
    }

    @Override
    public List<${DtoClass}> listAll(${QcClass} criteria) {
        return ${mapstructVar}.toDto(${daoVar}.listAllByCriteria(criteria));
    }

    @Override
    public ${"Map<String, Object>"} listAll(${QcClass} criteria, Pageable pageable) {
        Page<${entity}> page = PageUtil.startPage(pageable);
        List<${entity}> ${mainVar}s = ${daoVar}.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(${mapstructVar}.toDto(${mainVar}s), page.getTotal());
    }

    @Override
    public void download(List<${DtoClass}> ${dtoVar}S, HttpServletResponse response) throws IOException {
        ${"List<Map<String, Object>>"} list = new ArrayList<>();
        for (${DtoClass} ${dtoVar} : ${dtoVar}S) {
            ${"Map<String, Object>"} map = new LinkedHashMap<>();
            // TODO
    <#list (table.fields+table.commonFields) as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
            map.put("<#if field.comment?? && field.comment != "">${field.comment}<#else>${field.propertyName}</#if>", ${dtoVar}.${getprefix}${field.capitalName}());
    </#list>
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
</#if>
