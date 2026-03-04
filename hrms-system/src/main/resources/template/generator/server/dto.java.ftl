package ${cfg.sourceParentUrl}.${cfg.dtoUrl};

<#list table.importPackages as pkg>
    import ${pkg};
</#list>
import ${cfg.baseParentUrl}.base.BaseDTO;
<#if entityLombokModel>
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
</#if>

/**
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Getter
@Setter
@ToString(callSuper = true)
</#if>
public class ${entity}${cfg.dtoSuffix} extends BaseDTO {
<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;

</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.comment!?length gt 0>
    // ${field.comment}
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
<#------------  END 字段循环遍历  ---------->

}
