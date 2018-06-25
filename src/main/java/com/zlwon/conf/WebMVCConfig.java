package com.zlwon.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zlwon.interceptors.AuthLoginInterceptor;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
    
    @Autowired
    private  AuthLoginInterceptor  authLoginInterceptor;
    /**
     * 配置自定义拦截器，拦截所有请求
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(authLoginInterceptor).addPathPatterns("/**");
    }
}
