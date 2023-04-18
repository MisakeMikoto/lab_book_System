package com.yiling.config;

import com.yiling.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * @Author MisakiMikoto
 * @Date 2023/3/31 22:22
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/img/**").addResourceLocations("File:"+ UPLOAD_FOLDER);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

//    /**
//     * 解决跨域请求
//     * @param registry
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedHeaders("*")
//                .allowedMethods("*")
////                .allowedOrigins("*")
//                .allowedOriginPatterns("*")
//                .allowCredentials(true);
//    }
    /**
     * 异步请求配置
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
        configurer.setDefaultTimeout(30000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<Object> excludePath = new ArrayList<>();
//        排除拦截，除了注册登录(此时还没Token),其他都拦截
        excludePath.add("/user/login");
        excludePath.add("/user/register");
        excludePath.add("/static/**");
        excludePath.add("/assets/**");
//        System.out.println(excludePath.toString());
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/checkRegisterCode")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/assets/**")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/getRegisterCode");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
