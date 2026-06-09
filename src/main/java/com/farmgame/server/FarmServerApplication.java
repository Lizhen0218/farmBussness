package com.farmgame.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.farmgame.server.modules")
@SpringBootApplication
public class FarmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmServerApplication.class, args);
    }
}
