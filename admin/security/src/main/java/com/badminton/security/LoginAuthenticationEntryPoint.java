package com.badminton.security;

import com.badminton.result.BaseResult;
import com.badminton.security.entity.ControllerTools;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/6/27.
 */
public class LoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
   private String loginFormUrl;

    public LoginAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
       this.loginFormUrl = loginFormUrl;
    }
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        boolean isAjax = ControllerTools.isAjaxRequest(request);
        boolean hasSession = ControllerTools.hasAuthentication();
        System.out.println("isAjax:"+isAjax);
        System.out.println("hasAuthentication："+hasSession);
        if(isAjax && !hasSession){
            this.transformAjaxRequest(request, response);
        }else{
            super.commence(request, response, authException);
        }
    }

    private void transformAjaxRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        BaseResult msg = new BaseResult();
        msg.setCode(300);
        msg.setMessage("Session超时，请重新登录");
        ControllerTools.print(response, msg);
    }
}
