package com.sjy.imagechain.config;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.config.Config;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuomc
 * @Date 2025/5/21
 * @Description
 */
@Configuration
public class BCOSConfig {
    @Bean
    public BcosSDK bcosSDK() throws ConfigException {
        return new BcosSDK(Config.load("src/main/resources/config-example.toml"));
    }
}

