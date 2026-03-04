<#assign mainVarCap = entity?uncap_first?replace(package.ModuleName, "")>
<#assign mainVar = mainVarCap?uncap_first>
<#assign DTOClass = entity + cfg.dtoSuffix>
<#assign QCClass = entity + cfg.qcSuffix>
<#assign dtoVar = DTOClass?uncap_first?replace(package.ModuleName, "")?uncap_first>
<#assign daoVar = table.mapperName?uncap_first>
<#assign serVar = table.serviceName?uncap_first>
<#assign mapstructVar = (entity+cfg.mapstructSuffix)?uncap_first>
<#if table.comment??>
    <#assign mainCN = table.comment>
<#else >
    <#assign mainCN = mainVarCap>
</#if>
<#assign cuxKeyField=[]><#--表key字段 -->
<#assign cuxKeyIdentityField=[]><#--表自增长key字段 -->
<#list (table.fields + table.commonFields) as field>
    <#if field.keyFlag || field.customMap["KEY"] == "PRI" >
        <#assign cuxKeyField+=[field]>
        <#if field.keyIdentityFlag>
            <#assign cuxKeyIdentityField+=[field]>
        </#if>
    </#if>
</#list>
package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${cfg.baseParentUrl}.fnd.aop.Log;
import ${cfg.baseParentUrl}.fnd.aop.ErrorLog;
import ${cfg.baseParentUrl}.exception.BadRequestException;
import ${cfg.baseParentUrl}.utils.ThrowableUtil;
import ${package.Entity}.${entity};
import ${cfg.sourceParentUrl}.${cfg.dtoUrl}.${QCClass};
import ${package.Service}.${table.serviceName};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@Api(tags = "${table.comment!}")
@RequestMapping("/api<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${mainVar}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private static final String ENTITY_NAME = "${mainVar}";
    private final ${table.serviceName} ${serVar};

    public ${table.controllerName}(${table.serviceName} ${serVar}) {
        this.${serVar} = ${serVar};
    }

    @Log("新增${mainCN}")
    @ApiOperation("新增${mainCN}")
    @PostMapping
    @PreAuthorize("@el.check('${mainVar}:add')")
    public ResponseEntity create(@Validated @RequestBody ${entity} ${mainVar}) {
    <#if cuxKeyIdentityField?? && (cuxKeyIdentityField?size >0)>
        <#assign ifTemp = "">
        <#list cuxKeyIdentityField as field>
            <#if field.propertyType == "boolean">
                <#assign getprefix="is"/>
            <#else>
                <#assign getprefix="get"/>
            </#if>
            <#if !field?is_first>
                <#assign ifTemp += " and ">
            </#if>
            <#assign ifTemp += mainVar + "." + getprefix + field.capitalName + "()  != null">
        </#list>
        if (${ifTemp}) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
    </#if>
        return new ResponseEntity<>(${serVar}.insert(${mainVar}), HttpStatus.CREATED);
    }

    @Log("删除${mainCN}")
    @ApiOperation("删除${mainCN}")
    @DeleteMapping(value = "<#list cuxKeyField as field>/{${field.propertyName}}</#list>")
    @PreAuthorize("@el.check('${mainVar}:del')")
    public ResponseEntity delete(<#list cuxKeyField as field>@PathVariable ${field.propertyType} ${field.propertyName}<#sep>, </#list>) {
        try {
            ${serVar}.delete(<#list cuxKeyField as field>${field.propertyName}<#sep>, </#list>);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该${mainCN}存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改${mainCN}")
    @ApiOperation("修改${mainCN}")
    @PutMapping
    @PreAuthorize("@el.check('${mainVar}:edit')")
    public ResponseEntity update(@Validated(${entity}.Update.class) @RequestBody ${entity} ${mainVar}) {
        ${serVar}.update(${mainVar});
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个${mainCN}")
    @ApiOperation("获取单个${mainCN}")
    @GetMapping(value = "<#list cuxKeyField as field>/{${field.propertyName}}</#list>")
    @PreAuthorize("@el.check('${mainVar}:list')")
    public ResponseEntity get${mainVarCap}(<#list cuxKeyField as field>@PathVariable ${field.propertyType} ${field.propertyName}<#sep>, </#list>) {
        return new ResponseEntity<>(${serVar}.getByKey(<#list cuxKeyField as field>${field.propertyName}<#sep>, </#list>), HttpStatus.OK);
    }

    @ErrorLog("查询${mainCN}（分页）")
    @ApiOperation("查询${mainCN}（分页）")
    @GetMapping
    @PreAuthorize("@el.check('${mainVar}:list')")
    public ResponseEntity get${mainVarCap}Page(${QCClass} criteria, @PageableDefault(value = 9999, sort = {<#list cuxKeyField as field>"${field.name}"<#sep>, </#list>}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(${serVar}.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询${mainCN}（不分页）")
    @ApiOperation("查询${mainCN}（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('${mainVar}:list')")
    public ResponseEntity get${mainVarCap}NoPaging(${QCClass} criteria) {
    return new ResponseEntity<>(${serVar}.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出${mainCN}数据")
    @ApiOperation("导出${mainCN}数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('${mainVar}:list')")
    public void download(HttpServletResponse response, ${QCClass} criteria) throws IOException {
        ${serVar}.download(${serVar}.listAll(criteria), response);
    }
}
</#if>
