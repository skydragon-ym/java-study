package com.skydragon.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class DubboProviderBootstrap
{
    public static void main( String[] args ) throws InterruptedException {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(DubboProviderBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
