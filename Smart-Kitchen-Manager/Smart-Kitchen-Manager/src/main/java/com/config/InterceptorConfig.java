package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.interceptor.AuthorizationInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport{
	
	@Bean
    public AuthorizationInterceptor getAuthorizationInterceptor() {
        return new AuthorizationInterceptor();
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 只拦截API接口，不拦截静态资源
        registry.addInterceptor(getAuthorizationInterceptor())
            .addPathPatterns(
                "/yonghu/**",
                "/users/**",
                "/caipuxinxiController/**",
                "/caishileixing/**",
                "/config/**",
                "/dietStatistics/**",
                "/discusscaipuxinxi/**",
                "/messages/**",
                "/news/**",
                "/pingfenxinxi/**",
                "/storeup/**",
                "/systemintro/**",
                "/userShicai/**",
                "/aboutus/**",
                "/caipuxinxi/**",
                "/shopping/**",
                "/file/**",
                "/ocr/**"
            )
            .excludePathPatterns(
                "/yonghu/login",
                "/yonghu/register",
                "/yonghu/resetPass",
                "/users/login",
                "/users/register"
            );
        super.addInterceptors(registry);
	}
	
	/**
	 * springboot 2.0配置WebMvcConfigurationSupport之后，会导致默认配置被覆盖，要访问静态资源需要重写addResourceHandlers方法
	 */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 静态资源（优先级最高）
		registry.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/");
		
		// 上传文件
		registry.addResourceHandler("/upload/**")
			.addResourceLocations("classpath:/static/upload/")
			.addResourceLocations("file:upload/");
		
		// 前台页面资源（包含所有子路径）
		registry.addResourceHandler("/front/**")
			.addResourceLocations("classpath:/front/");
		
		// 后台页面资源（包含所有子路径）
		registry.addResourceHandler("/admin/**")
			.addResourceLocations("classpath:/admin/");
		
		// CSS、JS、图片等资源（全局）
		registry.addResourceHandler("/css/**")
			.addResourceLocations("classpath:/static/css/")
			.addResourceLocations("classpath:/front/front/dist/css/")
			.addResourceLocations("classpath:/admin/admin/dist/css/");
		
		registry.addResourceHandler("/js/**")
			.addResourceLocations("classpath:/static/js/")
			.addResourceLocations("classpath:/front/front/dist/js/")
			.addResourceLocations("classpath:/admin/admin/dist/js/");
		
		registry.addResourceHandler("/img/**")
			.addResourceLocations("classpath:/static/img/")
			.addResourceLocations("classpath:/front/front/dist/img/")
			.addResourceLocations("classpath:/admin/admin/dist/img/");
		
		registry.addResourceHandler("/fonts/**")
			.addResourceLocations("classpath:/static/fonts/")
			.addResourceLocations("classpath:/front/front/dist/fonts/")
			.addResourceLocations("classpath:/admin/admin/dist/fonts/");
		
		// 根路径映射到静态资源
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/")
			.addResourceLocations("classpath:/public/");
		
		super.addResourceHandlers(registry);
    }
}
