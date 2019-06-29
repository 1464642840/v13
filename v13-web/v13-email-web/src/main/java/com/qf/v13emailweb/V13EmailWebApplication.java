package com.qf.v13emailweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDubbo
@EnableScheduling
public class V13EmailWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(V13EmailWebApplication.class, args);
    }

}
