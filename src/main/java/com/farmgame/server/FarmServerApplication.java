package com.farmgame.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;

@MapperScan("com.farmgame.server.modules")
@SpringBootApplication
public class FarmServerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FarmServerApplication.class);
        application.addListeners(event -> {
            if (event instanceof ApplicationEnvironmentPreparedEvent preparedEvent) {
                String datasourceUrl = preparedEvent.getEnvironment().getProperty("spring.datasource.url", "");
                String datasourceUsername = preparedEvent.getEnvironment().getProperty("spring.datasource.username", "");
                String redisHost = preparedEvent.getEnvironment().getProperty("spring.data.redis.host", "");
                String redisPort = preparedEvent.getEnvironment().getProperty("spring.data.redis.port", "");
                System.out.println("[startup-diagnostics] datasource.url=" + maskJdbcUrl(datasourceUrl));
                System.out.println("[startup-diagnostics] datasource.username=" + datasourceUsername);
                System.out.println("[startup-diagnostics] redis.endpoint=" + redisHost + ":" + redisPort);
            }
        });
        application.run(args);
    }

    private static String maskJdbcUrl(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }
        return url.replaceAll("(?i)(password=)[^&]*", "$1******");
    }
}
