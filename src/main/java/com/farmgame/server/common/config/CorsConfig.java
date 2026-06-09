package com.farmgame.server.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 本地开发跨域配置。
 *
 * <p>Cocos Web 预览、静态 preview.html、Apifox/Postman 代理调试时会从不同来源访问后端接口，
 * 因此开发阶段允许 /api/** 接口接收跨域请求。</p>
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 放开业务接口跨域，方便 Cocos 客户端和接口测试工具在本地联调。
     *
     * @param registry Spring MVC 跨域映射注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("X-Player-Id")
                .maxAge(3600);
    }
}
