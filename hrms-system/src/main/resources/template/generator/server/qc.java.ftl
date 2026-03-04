package ${cfg.sourceParentUrl}.${cfg.dtoUrl};

<#if entityLombokModel>
import lombok.Data;
</#if>
import java.io.Serializable;

/**
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
</#if>
public class ${entity}${cfg.qcSuffix} implements Serializable {

}
