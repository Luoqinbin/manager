package com.badminton.security;

import com.badminton.entity.system.SysUser;
import com.badminton.security.mapper.SysUserDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 在登录成功后把用户的登录时间及登录IP记录到数据库
 */
public class SimpleLoginSuccessHandler  implements AuthenticationSuccessHandler,InitializingBean {
    protected static final Logger logger = LoggerFactory.getLogger(SimpleLoginSuccessHandler.class);

    private String defaultTargetUrl;

    private boolean forwardToDestination = false;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Resource
    private SysUserDao sysUserService;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isEmpty(defaultTargetUrl)){
            logger.error("You must configure defaultTargetUrl");
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       logger.info("登录成功");
        SysUser user = (SysUser)authentication.getPrincipal();
        request.getSession().setAttribute(Constant.USER_SESSION,user);
        request.getSession().setMaxInactiveInterval(10000000);
        SysUser u = sysUserService.queryBuId(user.getId()+"");
        u.setLastLogin(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        u.setLoginIp(getIpAddress(request));
        u.setId(user.getId());
        try {
            sysUserService.updateUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(this.forwardToDestination){
            request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);
        }else{
            this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);
        }
    }

    /**
     * 获得客户机ip地址
     * @param request
     * @return
     */
    public String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getDefaultTargetUrl() {
        return defaultTargetUrl;
    }

    public void setDefaultTargetUrl(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
    }

    public boolean isForwardToDestination() {
        return forwardToDestination;
    }

    public void setForwardToDestination(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
