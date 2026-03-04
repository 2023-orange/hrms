package com.sunten.hrms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.SqlServerTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.SqlServerQuery;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.Test;

import java.util.*;


public class MybatisPlusAutoGeneratorTest {
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    @Test
    public void testGenerator() {
        String author = scanner("作者");
        String moduleName = scanner("模块名");
        String[] tableNames = scanner("表名，多个用英文逗号分割").split(",");

        String projectPath = System.getProperty("user.dir");
        String sourcePath = "/src/main/java";
        String resourcesPath = "/src/main/resources";
        String jsPath = "/src/test/java/js";
        String baseParentUrl = "com.sunten.hrms";
        String sourceParentUrl = baseParentUrl;//+ ".generator";
        String sourceParentPath = sourceParentUrl.replace(".", "/");
        String qcSuffix = "QueryCriteria";  //QueryCriteria后缀      如FndUserQueryCriteria
        String dtoSuffix = "DTO";   //dto后缀     如FndUserDTO
        String dtoUrl = moduleName + ".dto";
        String dtoPath = dtoUrl.replace(".", "/");
        String mapstructSuffix = "Mapper";
        String mapstructUrl = moduleName + ".mapper";
        String mapstructPath = mapstructUrl.replace(".", "/");
        String mapperUrl = "dao";
        String xmlMapperPath = (moduleName + "." + mapperUrl).replace(".", "/");
        String apiPath = "api/" + moduleName;

        String controllerUrl = "rest";
        String serviceUrl = "service";
        String serviceImplUrl = "service.impl";
        String entityUrl = "domain";

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + sourcePath)
                .setAuthor(author)
                .setOpen(false)
                .setIdType(IdType.NONE) // 主键策略
                .setFileOverride(false)  // 文件覆盖
                .setEnableCache(false) //是否开启二级缓存
                .setActiveRecord(false) // 是否支持AR模式
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setMapperName("%sDao")
                .setXmlName("%sDaoMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController");
        //gc.setDateType(DateType.SQL_PACK);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:sqlserver://172.18.1.131:1433;DatabaseName=HRMS2020");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dsc.setUsername("sa");
        dsc.setPassword("serntcrm");
        dsc.setDbQuery(new SqlServerQuery() {
            /**
             * 重写父类预留查询自定义字段<br>
             * 这里查询的 SQL 对应父类 tableFieldsSql 的查询字段，默认不能满足你的需求请重写它<br>
             * 模板中调用：  table.fields 获取所有字段信息，
             * 然后循环字段获取 field.customMap 从 MAP 中获取注入字段如下  KEY 或者 isNullable
             */
            @Override
            public String tableFieldsSql() {
                return "SELECT  cast(a.name AS VARCHAR(500)) AS TABLE_NAME,cast(b.name AS VARCHAR(500)) AS COLUMN_NAME, cast(c.VALUE AS NVARCHAR(500)) AS COMMENTS,cast(sys.types.name AS VARCHAR (500)) AS DATA_TYPE,( SELECT CASE count(1) WHEN 1 then 'PRI' ELSE '' END FROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes  WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id (a.name) AND sysobjects.xtype = 'PK' AND sysobjects.parent_obj = syscolumns.id  AND sysindexes.id = syscolumns.id  AND sysobjects.name = sysindexes.name AND sysindexkeys.id = syscolumns.id  AND sysindexkeys.indid = sysindexes.indid  AND syscolumns.colid = sysindexkeys.colid AND syscolumns.name = b.name) as 'KEY',  b.is_identity isIdentity, b.is_nullable isNullable  FROM ( select name,object_id from sys.tables UNION all select name,object_id from sys.views ) a  INNER JOIN sys.columns b ON b.object_id = a.object_id  LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id    LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id  WHERE a.name = '%s' and sys.types.name !='sysname' ORDER BY b.column_id";
            }

            @Override
            public String[] fieldCustom() {
                return new String[]{"KEY", "isNullable"};
            }
        });
        dsc.setTypeConvert(new SqlServerTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//                System.out.println("转换类型：" + fieldType);
                //将数据库中datetime转换成LOCAL_DATE_TIME
                if (fieldType.toLowerCase().equals("datetime")) {
                    return DbColumnType.LOCAL_DATE_TIME;
                }
                //将数据库中date转换成LOCAL_DATE
                if (fieldType.toLowerCase().equals("date")) {
                    return DbColumnType.LOCAL_DATE;
                }
                //将数据库中time转换成LOCAL_TIME
                if (fieldType.toLowerCase().equals("time")) {
                    return DbColumnType.LOCAL_TIME;
                }
                //将数据库中decimal转换成BIG_DECIMAL
                if (fieldType.toLowerCase().equals("decimal")) {
                    return DbColumnType.BIG_DECIMAL;
                }
                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        });
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        String parentUrlEnter = scanner("父包路径（空即默认为：" + sourceParentUrl + ")", true);
//        if (!StringUtils.isEmpty(parentUrlEnter)) {
//            sourceParentUrl = parentUrlEnter;
//        }
        pc.setParent(sourceParentUrl);
        pc.setModuleName(moduleName);
        pc.setController(controllerUrl);
        pc.setService(serviceUrl);
        pc.setServiceImpl(serviceImplUrl);
        pc.setEntity(entityUrl);
        pc.setMapper(mapperUrl);
        mpg.setPackageInfo(pc);


        //xmlMapperPath = xmlMapperPath.replace(".","/");

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("dtoUrl", dtoUrl);
                map.put("sourceParentUrl", sourceParentUrl);
                map.put("mapstructUrl", mapstructUrl);
                map.put("qcSuffix", qcSuffix);
                map.put("dtoSuffix", dtoSuffix);
                map.put("mapstructSuffix", mapstructSuffix);
                map.put("baseParentUrl", baseParentUrl);
                this.setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/template/generator/server/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + resourcesPath + "/" + sourceParentPath + "/" + xmlMapperPath + "/mapper/" + tableInfo.getEntityName() + "DaoMapper" + StringPool.DOT_XML;
            }
        });
        focList.add(new FileOutConfig("template/generator/server/dto.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + sourcePath + "/" + sourceParentPath + "/" + dtoPath + "/" + tableInfo.getEntityName() + dtoSuffix + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("template/generator/server/qc.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + sourcePath + "/" + sourceParentPath + "/" + dtoPath + "/" + tableInfo.getEntityName() + qcSuffix + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("template/generator/server/mapstruct.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + sourcePath + "/" + sourceParentPath + "/" + mapstructPath + "/" + tableInfo.getEntityName() + mapstructSuffix + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("template/generator/server/api.js.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + jsPath + "/" + apiPath + "/" + tableInfo.getEntityName() + ".js";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null).setEntity("/template/generator/server/entity.java")
                .setMapper("/template/generator/server/mapper.java")
                .setService("/template/generator/server/service.java")
                .setServiceImpl("/template/generator/server/serviceImpl.java")
                .setController("/template/generator/server/controller.java");
        mpg.setTemplate(tc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("com.sunten.hrms.base.BaseEntity");
        strategy.setSuperEntityColumns(new String[]{"create_time", "create_by", "update_time", "update_by"});
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableNames);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();


        System.out.println("作者：" + author);
        System.out.println("模块：" + moduleName);

        System.out.println("本次自动生成代码的数据表如下：");
        for (String tableName : tableNames) {
            System.out.println(tableName);
        }
        System.out.println("");
        System.out.println("文件路径如下：");
        System.out.println("entity文件路径：         " + sourcePath + "/" + sourceParentPath + "/" + moduleName + "/" + controllerUrl.replace(".", "/"));
        System.out.println("DTO文件路径：            " + sourcePath + "/" + sourceParentPath + "/" + dtoPath);
        System.out.println("QueryCriteria文件路径：  " + sourcePath + "/" + sourceParentPath + "/" + dtoPath);
        System.out.println("mapstructMapper文件路径：" + sourcePath + "/" + sourceParentPath + "/" + mapstructPath);
        System.out.println("dao文件路径：            " + sourcePath + "/" + sourceParentPath + "/" + xmlMapperPath);
        System.out.println("daoXml文件路径：         " + resourcesPath + "/" + sourceParentPath + "/" + xmlMapperPath + "/mapper");
        System.out.println("service文件路径：        " + sourcePath + "/" + sourceParentPath + "/" + moduleName + "/" + serviceUrl.replace(".", "/"));
        System.out.println("serviceImpl文件路径：    " + sourcePath + "/" + sourceParentPath + "/" + moduleName + "/" + serviceImplUrl.replace(".", "/"));
        System.out.println("controller文件路径：     " + sourcePath + "/" + sourceParentPath + "/" + moduleName + "/" + controllerUrl.replace(".", "/"));
        System.out.println("api文件路径：            " + jsPath + "/" + apiPath);

    }
}
