package com.skydragon.oa.web.controller;

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
    //显示登录页面
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    //执行后台登录逻辑
    public String doLogin(){
        return accountService.login();
    }
}
