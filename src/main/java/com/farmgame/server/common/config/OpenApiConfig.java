package com.farmgame.server.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI farmGameOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("农场经营摆摊小游戏后端 API")
                        .description("MVP 阶段接口：用户、农场、仓库、摆摊、经济、排行榜、广告奖励。")
                        .version("0.1.0")
                        .contact(new Contact().name("Farm Game"))
                        .license(new License().name("Private")));
    }
}
