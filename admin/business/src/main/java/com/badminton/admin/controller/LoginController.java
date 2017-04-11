package com.badminton.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 */
@Controller
@RequestMapping("login")
public class LoginController {

    @RequestMapping(value = "doLogin")
    public String doLogin(HttpServletRequest request){
        String path =request.getContextPath();//项目名称
        String basePath =request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        request.getSession().setAttribute("basePath",basePath);
        return "login";
    }

}
