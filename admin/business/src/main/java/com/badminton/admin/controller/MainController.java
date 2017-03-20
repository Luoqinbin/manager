package com.badminton.admin.controller;

import com.badminton.entity.system.SysResources;
import com.badminton.entity.system.SysUser;
import com.badminton.security.Constant;
import com.badminton.security.service.SysResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 主页controller
 */
@Controller
@RequestMapping("index")
public class MainController {
    @Autowired
    private SysResourcesService sysResourcesService;

    @RequestMapping(value = "main" )
    public String gotoMain(HttpServletRequest request){
        SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
        if(sysUser==null){
            return "login";
        }
        List<SysResources> list = sysResourcesService.queryLeft(sysUser.getId());
        request.setAttribute("menuList",list);
        return "main/index";
    }

    @RequestMapping(value = "welcome" )
    public String welcome(){
        return "main/welcome";
    }
    
 /*   @RequestMapping("queryResource")
    @ResponseBody
    public List<SysResources> queryResource(HttpServletRequest request){
        SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
        List<SysResources> list = sysResourcesService.queryLeft(sysUser.getId());
        //List<SysResources> list = sysResourcesService.queryAllByUserId(sysUser.getId());
        return list;
    }*/
}
