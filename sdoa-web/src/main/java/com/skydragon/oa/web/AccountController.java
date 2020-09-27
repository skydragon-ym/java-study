package com.skydragon.oa.web;

import com.skydragon.oa.service.contract.AccountService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    @DubboReference
    AccountService accountService;

    @GetMapping("/login")
    public String Login(){
        return "login";
    }

    @PostMapping("/login")
    public String DoLogin(){
        return accountService.login();
    }
}
