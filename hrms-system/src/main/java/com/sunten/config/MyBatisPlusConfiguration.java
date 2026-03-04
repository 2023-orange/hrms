package com.sunten.config;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

@Configuration
@MapperScan(basePackages = {MyBatisPlusConfiguration.BASE_PACKAGES, "com.baomidou.mybatisplus.samples.quickstart.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MyBatisPlusConfiguration {

    static final String BASE_PACKAGES = "com.sunten.*.*.dao";

    private static final String MAPPER_LOCATION = "classpath*:com/sunten/**/dao/mapper/*DaoMapper.xml";

    @Bean
    @Primary    //多数据源时需要加上该注解，加一个即可
    @ConfigurationProperties(prefix = "spring.datasource.druid.hrms")
    public AtomikosNonXADataSourceBean hrmsDs() {
        //也可以直接return new AtomikosNonXADataSourceBean();
        return DataSourceBuilder.create().type(AtomikosNonXADataSourceBean.class).build();
    }
//    public AtomikosDataSourceBean hrmsDs() {
//        return new AtomikosDataSourceBean();
//    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.erp")
    public AtomikosDataSourceBean erpDs() {
        return new AtomikosDataSourceBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.oa")
    public AtomikosDataSourceBean oaDs() {
        return new AtomikosDataSourceBean();
    }

    @Bean(name = "sqlSessionTemplate")
    public MySqlSessionTemplate customSqlSessionTemplate() throws Exception {
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>() {{
            put(DataSourceKeyEnum.HRMS.getValue(), createSqlSessionFactoryForHrms(hrmsDs()));
            put(DataSourceKeyEnum.ERP.getValue(), createSqlSessionFactory(erpDs()));
            put(DataSourceKeyEnum.OA.getValue(), createSqlSessionFactory(oaDs()));
        }};
        MySqlSessionTemplate sqlSessionTemplate = new MySqlSessionTemplate(sqlSessionFactoryMap.get(DataSourceKeyEnum.HRMS.getValue()));
        sqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryMap);
        return sqlSessionTemplate;
    }


    /**
     * 创建数据源
     *
     * @param dataSource
     * @return
     */
    private SqlSessionFactory createSqlSessionFactoryForHrms(AtomikosNonXADataSourceBean dataSource) throws Exception {
        dataSource.init();//项目启动则初始化连接
        MybatisSqlSessionFactoryBean sqlSessionFactory = createMybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(sqlServer2012PaginationInnerInterceptor());
        sqlSessionFactory.setPlugins(mybatisPlusInterceptor);
        //重写了GlobalConfig的MyGlobalConfig注入到sqlSessionFactory使其生效
        MyGlobalConfig globalConfig = new MyGlobalConfig();
        globalConfig.setMetaObjectHandler(metaObjectHandlerConfig());
        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.afterPropertiesSet();
        return sqlSessionFactory.getObject();
    }

    /**
     * 创建数据源
     *
     * @param dataSource
     * @return
     */
    private SqlSessionFactory createSqlSessionFactory(AtomikosDataSourceBean dataSource) throws Exception {
//        dataSource.setMaintenanceInterval(60);//定时维护线程周期 单位秒
        //以上配置可提取到.yml内通过ConfigurationProperties注解注入
        dataSource.init();//项目启动则初始化连接
        MybatisSqlSessionFactoryBean sqlSessionFactory = createMybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor());
        sqlSessionFactory.setPlugins(mybatisPlusInterceptor);
        //重写了GlobalConfig的MyGlobalConfig注入到sqlSessionFactory使其生效
        MyGlobalConfig globalConfig = new MyGlobalConfig();
        sqlSessionFactory.setGlobalConfig(globalConfig);
        sqlSessionFactory.afterPropertiesSet();
        return sqlSessionFactory.getObject();
    }

    private MybatisSqlSessionFactoryBean createMybatisSqlSessionFactoryBean() throws IOException {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        sqlSessionFactory.setVfs(SpringBootVFS.class);
        MybatisConfiguration configuration = new MybatisConfiguration();
        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        return sqlSessionFactory;
    }


//    /*
//     * 自定义的分页插件，自动识别数据库类型
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }
//
//
//    @Bean
//    public PaginationInterceptor sqlServer2012PaginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        paginationInterceptor.setDialectType(DbType.SQL_SERVER.getDb());
//        return paginationInterceptor;
//    }

    /*
     * 自定义的分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor();
    }

    @Bean
    public PaginationInnerInterceptor sqlServer2012PaginationInnerInterceptor() {
        PaginationInnerInterceptor myPaginationInnerInterceptor = new PaginationInnerInterceptor();
        myPaginationInnerInterceptor.setDbType(DbType.SQL_SERVER);
        myPaginationInnerInterceptor.setDialect(new MySQLServerDialect());
        return myPaginationInnerInterceptor;
    }

    @Bean
    public MetaObjectHandlerConfig metaObjectHandlerConfig() {
        return new MetaObjectHandlerConfig();
    }
}
