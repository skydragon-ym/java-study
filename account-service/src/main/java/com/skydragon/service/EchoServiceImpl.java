package com.skydragon.service;

import com.skydragon.oa.service.contract.EchoService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(
        version = "${account.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
class EchoServiceImpl implements EchoService {
    public String echo(String message) {
        return "Echo: " + message;
    }
}

