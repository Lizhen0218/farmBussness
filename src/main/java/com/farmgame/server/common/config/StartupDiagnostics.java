package com.farmgame.server.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 开发期启动诊断信息。
 *
 * <p>只输出非敏感连接目标，帮助快速判断本地启动时实际连接的是本机数据库还是远端数据库。</p>
 */
@Component
public class StartupDiagnostics implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupDiagnostics.class);

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private String redisPort;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Local startup datasource url: {}", maskJdbcUrl(datasourceUrl));
        log.info("Local startup datasource username: {}", datasourceUsername);
        log.info("Local startup redis endpoint: {}:{}", redisHost, redisPort);
    }

    private String maskJdbcUrl(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }
        return url.replaceAll("(?i)(password=)[^&]*", "$1******");
    }
}
