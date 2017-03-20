package com.badminton.admin.controller;


import com.badminton.entity.system.SysUser;
import com.badminton.entity.system.vo.ChangePassVo;
import com.badminton.result.BaseResult;
import com.badminton.security.Constant;
import com.badminton.security.mapper.SysUserDao;
import com.badminton.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * 密码
 */
@Controller
@RequestMapping("pass")
public class PassController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(PassController.class);
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private UserCache userCache;
    /**
     * 修收密码
     * @param changePassVo
     * @return
     */
    @RequestMapping(value = "doChangePassword", method = RequestMethod.POST)
    @ResponseBody
    public Object doChangePassword(@Valid ChangePassVo changePassVo, BindingResult bindingResult, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        logger.info("开始密码修改业务{}", changePassVo);
        try {
            //1、入参校验
            orderCheck(changePassVo, bindingResult);
            //2、验证用户是合法用户
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if(sysUser==null){
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            if (sysUser != null) {
                //检验旧密码是否一致
                PasswordUtil passwordUtil = new PasswordUtil(sysUser.getUsername(), "MD5");
                //确认新密码和旧密码的MD5值是否对得上号
                if (!passwordUtil.encode(changePassVo.getOldPassword()).equals(sysUser.getPassword())) {
                    logger.info("{}修改密码业务失败，旧密码验证不通过！", sysUser.getUsername());
                    result.setCode(BaseResult.CODE_FAIL);
                    result.setMessage("修改密码业务失败，旧密码验证不通过!");
                    return result;
                }
                //3、修改用户密码
                sysUser.setPassword(passwordUtil.encode(changePassVo.getNewPassword()));
                sysUserDao.updateUser(sysUser);
                //清除缓存
                userCache.removeUserFromCache(sysUser.getUsername());
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        logger.info("密码修改业务结束{}",result);
        return result;
    }



}
