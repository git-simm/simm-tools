//package com.simm.service.autoconfig;
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.support.TransactionTemplate;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
//import javax.sql.DataSource;
//
///**
// * @author miscr
// */
//@Configuration
//@MapperScan({"com.simm.service.mapper"})
//@EnableScheduling
//@EnableTransactionManagement(proxyTargetClass = true)
//public class MybatisConfig {
//    /**
//     * 数据源
//     *
//     * @return 获取数据源
//     */
//    @Bean(name = "dataSource")
//    public DataSource druidDataSource() {
//        return DruidDataSourceBuilder.create().build();
//    }
//
//    /**
//     * 事务驱动
//     *
//     * @return 获取事务驱动
//     */
//    @Bean(name = "transactionManager")
//    public DataSourceTransactionManager dataSourceTransactionManager() {
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
//        dataSourceTransactionManager.setDataSource(druidDataSource());
//        return dataSourceTransactionManager;
//    }
//
//    /**
//     * 获取事物模版
//     * @return
//     */
//    public TransactionTemplate transactionTemplate(){
//        TransactionTemplate transactionTemplate = new TransactionTemplate();
//        transactionTemplate.setTransactionManager(dataSourceTransactionManager());
//        return transactionTemplate;
//    }
//
//    @Bean(name = "globalValidator")
//    public LocalValidatorFactoryBean localValidatorFactoryBean() {
//        return new LocalValidatorFactoryBean();
//    }
//}
