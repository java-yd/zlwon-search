package com.zlwon.conf.db;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 配置druid需要的配置类，引入application.properties文件中以spring.datasource开头的信息
 * 因此需要在application.properties文件中配置相关信息。
 * 只是将DataSource对象的实现类变为了DruidDataSource对象
 * @author Administrator
 *
 */
@Configuration
public class DruidConfig {
	//配置初始化是为了解决如果访问/druid/login.html数据源那报无数据源的错，因为项目启动，没有执行sql，才会出现
    @Bean(initMethod="init") 
    @ConfigurationProperties(prefix = "spring.datasource")  
    public DataSource druidDataSource() {  
        DruidDataSource druidDataSource = new DruidDataSource();  
        return druidDataSource;  
    } 
}