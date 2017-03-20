package com.badminton.security.entity;

import com.alibaba.fastjson.JSON;
import com.badminton.result.BaseResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *控制类
 */
public class ControllerTools {

    public static PrintWriter getWriter(HttpServletResponse response)
    {
        PrintWriter out = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");
        try
        {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void print(HttpServletResponse response, BaseResult msg)
    {
        String data = JSON.toJSONString(msg);
        print(response, data);
    }

    public static void print(HttpServletResponse response, Object data)
    {
        PrintWriter out = getWriter(response);
        out.print(data);
    }

    public static UserDetails getUserDetails(HttpServletRequest request)
    {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String requestType = request.getHeader("X-Requested-With");
        return requestType != null;
    }

    public static boolean hasAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);

        return auth != null;
    }

}
