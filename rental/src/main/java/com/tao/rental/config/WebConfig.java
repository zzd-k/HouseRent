package com.tao.rental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//注册拦截器
//当需要对目标对象（target object）执行某种横切关注点（cross-cutting concern）操作，
//比如日志记录、事务处理等，开发者会将相关的拦截器注册到registry里。当匹配的条件触发时，
//registry会按照配置顺序查找并执行相应的拦截器，实现了对业务流程的动态增强。
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) { //registry是注册器，用来管理拦截器
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register");
    }
}