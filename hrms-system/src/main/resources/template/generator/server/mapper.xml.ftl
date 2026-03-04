<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#assign maxLength=100>
<#assign tableAlias=""><#--表简称（首字母） -->
<#list table.name?split("_") as x>
    <#assign tableAlias+=x[0..<1]>
</#list>
<#assign cuxKeyNoIdentityField=[]><#--表普通key字段 -->
<#assign cuxKeyIdentityField=[]><#--表自增长key字段 -->
<#assign cuxResultField=[]><#--表普通字段 -->
<#assign cuxUpdateField=[]><#--表更新字段 -->
<#list (table.fields + table.commonFields) as field>
    <#if field.keyFlag || field.customMap["KEY"] == "PRI" >
        <#if field.keyIdentityFlag>
            <#assign cuxKeyIdentityField+=[field]>
        <#else>
            <#assign cuxKeyNoIdentityField+=[field]>
        </#if>
    <#else>
        <#assign cuxResultField+=[field]>
        <#if (field.name !=  "create_time" && field.name !=  "create_by")>
            <#assign cuxUpdateField+=[field]>
        </#if>
    </#if>
</#list>
<#assign  cuxKeyField= cuxKeyNoIdentityField + cuxKeyIdentityField>
<#assign  cuxNoIdentityField = cuxKeyNoIdentityField + cuxResultField>
<#assign cuxSqlServerJdbcTypeMap = {"bigint":"BIGINT", "timestamp":"BINARY", "binary":"BINARY", "bit":"BIT",
"char":"CHAR", "decimal":"DECIMAL", "money":"DECIMAL", "smallmoney":"DECIMAL", "float":"DOUBLE",
"int":"INTEGER", "image":"LONGVARBINARY", "varbinary(max)":"LONGVARBINARY", "varchar(max)":"LONGVARCHAR",
"text":"LONGVARCHAR", "nchar":"NCHAR", "nvarchar":"NVARCHAR", "nvarchar(max)":"LONGVARCHAR", "ntext":"LONGNVARCHAR",
"numeric":"NUMERIC", "real":"REAL", "smallint":"SMALLINT", "datetime":"TIMESTAMP", "smalldatetime":"TIMESTAMP",
"varbinary":"VARBINARY", "udt":"VARBINARY", "varchar":"VARCHAR", "tinyint":"TINYINT", "uniqueidentifier":"CHAR",
"xml":"SQLXML", "time":"TIME", "date":"DATE", "datetime2":"TIMESTAMP", "datetimeoffset":"microsoft.sql.Types.DATETIMEOFFSET"}>
<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list (table.fields + table.commonFields) as field>
<#if field.keyFlag || field.customMap["KEY"] == "PRI"><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list (table.fields + table.commonFields) as field>
<#if !field.keyFlag && field.customMap["KEY"] != "PRI"><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#--<#list table.commonFields as field>&lt;#&ndash;生成公共字段 &ndash;&gt;-->
<#--        <result column="${field.name}" property="${field.propertyName}" />-->
<#--</#list>-->
    </resultMap>

    <resultMap id="${entity}ResultMap" type="${package.Entity}.${entity}" autoMapping="true">
<#list (table.fields + table.commonFields) as field>
<#if field.keyFlag || field.customMap["KEY"] == "PRI"><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        <![CDATA[
    <#list (table.fields + table.commonFields)>
        <#assign columListString = "">
        <#items as field>
            <#assign cuxSqlQ=" ">
            <#if field?has_next>
                <#assign cuxSqlQ=", ">
            </#if>
            <#assign columListStringTemp = tableAlias + "." + field.name + cuxSqlQ>
            <#if (columListString?length+columListStringTemp?length > maxLength) >
            ${columListString}
                <#assign columListString = columListStringTemp>
            <#else>
                <#assign columListString += columListStringTemp>
            </#if>
            <#if !field?has_next>
            ${columListString}
            </#if>
        </#items>
    </#list>
        ]]>
    </sql>

    <!-- 带参数通用查询结果列 -->
    <sql id="Base_Column_With_Param_List">
        <![CDATA[
    <#list (table.fields + table.commonFields)>
        <#assign columListString = "">
        <#items as field>
            <#assign cuxSqlQ=" ">
            <#if field?has_next>
                <#assign cuxSqlQ=", ">
            </#if>
            <#assign columListStringTemp = r"${tAlias}." + field.name + r" as ${tAlias}_"  + field.name + cuxSqlQ>
            <#if (columListString?length+columListStringTemp?length > maxLength) >
            ${columListString}
                <#assign columListString = columListStringTemp>
            <#else>
                <#assign columListString += columListStringTemp>
            </#if>
            <#if !field?has_next>
            ${columListString}
            </#if>
        </#items>
    </#list>
        ]]>
    </sql>

    <sql id="Base_Column_Query_Sql">
        <![CDATA[
            select
        ]]>
        <include refid="Base_Column_List"/>
        <![CDATA[
              from ${table.name} ${tableAlias}
        ]]>
    </sql>

    <sql id="Select_All_Criteria_Sql">
        <include refid="Base_Column_Query_Sql"/>
        <![CDATA[
             where 1 = 1
        ]]>
${r'<!--        <if test="criteria != null">-->'}
${r'<!--        <if test="criteria.xxxx != null">-->'}
${r"<!--            <![CDATA[-->"}
${r"<!--                and "}${tableAlias}${r".xxxxx like #{criteria.xxxx,jdbcType=xxxx}-->"}
${r"<!--            ]]>-->"}
${r"<!--        </if>-->"}
${r"<!--        </if>-->"}
    </sql>

    <#assign cuxSqls1=[]>
    <#assign cuxSqls2=[]>
    <#list cuxNoIdentityField>
        <#assign cuxSql1="insert into ${table.name} (">
        <#assign cuxSql2="values (">
        <#items as field>
            <#assign cuxSqlP="">
            <#assign cuxSqlQ="">
            <#if !field?is_first>
                <#assign cuxSqlP=" ">
            </#if>
            <#if field?has_next>
                <#assign cuxSqlQ=",">
            </#if>
            <#assign cuxSqlTmp = cuxSqlP + r"#{"+field.propertyName+",jdbcType="+cuxSqlServerJdbcTypeMap[field.type]+"}"+cuxSqlQ>
            <#if (cuxSql2?length+cuxSqlTmp?length > maxLength) >
                <#assign cuxSqls1+=[cuxSql1]>
                <#assign cuxSqls2+=[cuxSql2]>
                <#assign cuxSql1=""?left_pad(13+table.name?length)+ cuxSqlP+field.name+cuxSqlQ>
                <#assign cuxSql2=""?left_pad(7)+cuxSqlTmp>
            <#else>
                <#assign cuxSql1+=cuxSqlP+field.name+cuxSqlQ>
                <#assign cuxSql2+=cuxSqlTmp>
            </#if>
        </#items>
        <#assign cuxSqls1+=[cuxSql1+")"]>
        <#assign cuxSqls2+=[cuxSql2+")"]>
    </#list>
    <#assign cuxSqls1+=cuxSqls2>
    <insert id="insertAllColumn" parameterType="${package.Entity}.${entity}"<#if cuxKeyIdentityField?? && (cuxKeyIdentityField?size > 0)> useGeneratedKeys="true" keyProperty="<#list cuxKeyIdentityField as field><#if field.keyFlag>${field.name}</#if></#list>"</#if>>
        <![CDATA[
        <#list cuxSqls1 as sql>
            ${sql}
        </#list>
        ]]>
    </insert>

    <sql id="Delete_Sql">
        <![CDATA[
            delete
              from ${table.name}
        <#list cuxKeyField>
            <#items as field>
                <#assign cuxSqlP="and "?left_pad(7)>
                <#if field?is_first>
                    <#assign cuxSqlP=" where ">
                </#if>
            ${cuxSqlP + field.name + r" = #{"+field.propertyName+",jdbcType="+cuxSqlServerJdbcTypeMap[field.type]+"}"}
            </#items>
        </#list>
        ]]>
    </sql>

    <delete id="deleteByKey">
        <include refid="Delete_Sql"/>
    </delete>

    <delete id="deleteByEntityKey" parameterType="${package.Entity}.${entity}">
        <include refid="Delete_Sql"/>
    </delete>

    <#assign cloumnMaxLength = 0>
    <#list cuxUpdateField as field>
        <#if (field.name?length > cloumnMaxLength)>
            <#assign cloumnMaxLength = field.name?length>
        </#if>
    </#list>
    <#assign cuxSqls1=["update ${table.name}"]>
    <#list cuxUpdateField>
        <#items as field>
            <#assign cuxSqlP=""?left_pad(7)>
            <#assign cuxSqlQ="">
            <#if field?is_first>
                <#assign cuxSqlP="   set ">
            </#if>
            <#if field?has_next>
                <#assign cuxSqlQ=",">
            </#if>
            <#assign cuxSqlTmp = cuxSqlP + field.name?right_pad(cloumnMaxLength) + r" = #{"+field.propertyName+",jdbcType="+cuxSqlServerJdbcTypeMap[field.type]+"}"+cuxSqlQ>
            <#assign cuxSqls1+=[cuxSqlTmp]>
        </#items>
    </#list>
    <#list cuxKeyField>
        <#items as field>
            <#assign cuxSqlP="and "?left_pad(7)>
            <#if field?is_first>
                <#assign cuxSqlP=" where ">
            </#if>
            <#assign cuxSqlTmp = cuxSqlP + field.name + r" = #{"+field.propertyName+",jdbcType="+cuxSqlServerJdbcTypeMap[field.type]+"}">
            <#assign cuxSqls1+=[cuxSqlTmp]>
        </#items>
    </#list>
    <update id="updateAllColumnByKey" parameterType="${package.Entity}.${entity}">
        <![CDATA[
        <#list cuxSqls1 as sql>
            ${sql}
        </#list>
        ]]>
    </update>

    <select id="getByKey" resultMap="${entity}ResultMap">
        <include refid="Base_Column_Query_Sql"/>
        <![CDATA[
        <#list cuxKeyField>
            <#items as field>
                <#assign cuxSqlP="and "?left_pad(7)>
                <#if field?is_first>
                    <#assign cuxSqlP=" where ">
                </#if>
            ${cuxSqlP + tableAlias + "." + field.name + r" = #{"+field.propertyName+",jdbcType="+cuxSqlServerJdbcTypeMap[field.type]+"}"}
            </#items>
        </#list>
        ]]>
    </select>

    <select id="listAllByCriteria" resultMap="${entity}ResultMap">
        <include refid="Select_All_Criteria_Sql"/>
        <#if cuxKeyField?? && (cuxKeyField?size > 0)>
        <![CDATA[
            order by <#list cuxKeyField as field>${field.name}<#sep>, </#list>
        ]]>
        </#if>
    </select>

    <select id="listAllByCriteriaPage" resultMap="${entity}ResultMap">
        <include refid="Select_All_Criteria_Sql"/>
    </select>
</#if>

</mapper>
