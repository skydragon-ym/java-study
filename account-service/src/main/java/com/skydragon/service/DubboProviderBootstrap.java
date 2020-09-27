package com.skydragon.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

interface EchoService{
    String echo(String message);
}

@DubboService(
        version = "${account.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
class EchoServiceImpl implements EchoService{

    public String echo(String message) {
        return "Echo: " + message;
    }
}

@EnableAutoConfiguration
public class DubboProviderBootstrap
{
    public static void main( String[] args ) throws InterruptedException {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(DubboProviderBootstrap.class)
                //.web(WebApplicationType.NONE)
                .run(args);
    }
}
