package com.skydragon.service;

import com.skydragon.oa.service.contract.AccountService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(
        version = "${account.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class AccountServiceImpl implements AccountService {
    @Override
    public String login() {
        return "token";
    }
}
