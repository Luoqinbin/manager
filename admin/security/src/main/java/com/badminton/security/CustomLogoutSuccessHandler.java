package com.badminton.security;

import com.badminton.entity.system.SysUser;
import com.badminton.security.mapper.SysUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

    @Resource
    private SysUserDao sysUserService;

    private String defaultTargetUrl;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        SysUser user = (SysUser)authentication.getPrincipal();
        String outLoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        SysUser u = sysUserService.queryBuId(user.getId()+"");;
        u.setOutLoginTime(outLoginTime);
        logger.info(outLoginTime +"=>退出成功");
        try {
            sysUserService.updateUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);
    }


    public void setDefaultTargetUrl(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }
}
