package com.badminton.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录
 */
@Controller
@RequestMapping("login")
public class LoginController {

    @RequestMapping(value = "doLogin")
    public String doLogin(){
        return "login";
    }

}
