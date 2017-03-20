package com.badminton.admin.controller;
import com.badminton.entity.system.SysUser;
import com.badminton.entity.system.vo.ChangePassVo;
import com.badminton.result.BaseResult;
import com.badminton.security.Constant;
import com.badminton.security.mapper.SysUserDao;
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
@RequestMapping("user")
public class UserController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private UserCache userCache;

    /**
     * 修收密码
     *
     * @param changePassVo
     * @return
     */
    @RequestMapping(value = "userInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object userInfo(@Valid ChangePassVo changePassVo, BindingResult bindingResult, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        logger.info("已登陆用户缓存信息查询{}", changePassVo);
        try {
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }else{
                result.setData(sysUser);
            }
        }catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        logger.info("已登陆用户缓存信息查询结束{}", result);
        return result;
    }
}

