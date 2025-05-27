package com.sjy.imagechain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.sjy.imagechain.mapper")
public class ImageChainStarter {

    public static void main(String[] args) {
        SpringApplication.run(ImageChainStarter.class, args);
    }
}

