package com.badminton.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;


/**
 * 资源
 */
@Controller
@RequestMapping("language")
public class LanguageController extends BaseController {
    @Autowired
    CookieLocaleResolver resolver;
    private static Logger logger = LoggerFactory.getLogger(LanguageController.class);

    /**
     * 国际化语言切换
     */
    @RequestMapping("change")
    public ModelAndView language(HttpServletRequest request, HttpServletResponse response, String language, String callBackUrl) {
        logger.info("前台语言切换");
        try {
            language = language.toLowerCase();
            if (language == null || language.equals("")) {
                return new ModelAndView("redirect:/" + callBackUrl);
            } else {
                if (language.equals("zh_cn")) {
                    resolver.setLocale(request, response, Locale.CHINA);
                } else if (language.equals("en")) {
                    resolver.setLocale(request, response, Locale.ENGLISH);
                } else {
                    resolver.setLocale(request, response, Locale.CHINA);
                }
            }
        } catch (Exception e) {
            logger.info("前台语言切换失败");
            return new ModelAndView("redirect:/" + callBackUrl);
        }
        logger.info("前台语言切换结束");
        return new ModelAndView("redirect:/" + callBackUrl);
    }
}

