package com.skydragon.service;

import com.skydragon.oa.service.contract.AccountService;

public class AccountServiceImpl implements AccountService {

    @Override
    public String login() {
        return "token";
    }
}
