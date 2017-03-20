package com.badminton.security;

import com.badminton.entity.system.Authority;
import com.badminton.security.mapper.SysResourcesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。 此类在初始化时，应该取到所有
 */
public class SecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource  {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static HashMap<String, Collection<ConfigAttribute>> resourceMap = null;
    private final static List<ConfigAttribute> NULL_CONFIG_ATTRIBUTE = Collections.emptyList();
    @Resource
    private SysResourcesDao sysResourcesDao;

    /**
     * 自定义方法，这个类放入到Spring容器后，
     * 指定init为初始化方法，从数据库中读取资源
     */
    @PostConstruct
    public void init() {
        loadResourceDefine();
    }

    /**
     * 程序启动的时候就加载所有资源信息
     * 初始化资源与权限的映射关系
     */
    public void loadResourceDefine() {

        // 在Web服务器启动时，提取系统中的所有权限
        List<Authority> urlList = sysResourcesDao.queryAllByType("url");
        List<Authority> btns = sysResourcesDao.queryAllBtn();
        //List<Authority> link = sysResourcesDao.queryAllLink();
        List<Authority> authorities = new ArrayList<>();
        authorities.addAll(urlList);
        authorities.addAll(btns);
        //authorities.addAll(link);
        //应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
        resourceMap = new HashMap<>();

        if (authorities != null && authorities.size() > 0)
            for (Authority auth : authorities) {
                String authName = auth.getRole_auth();  //获取权限的name 是以 ROLE_为前缀的代码值
                ConfigAttribute ca = new SecurityConfig(authName); //将ROLE_XXX 封装成spring的权限配置属性
                //根据权限名获取所有资源
                String url = auth.getResource_path();
                //判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中。
                if (resourceMap.containsKey(url)) { //如果已存在url 加入权限
                    Collection<ConfigAttribute> value = resourceMap.get(url);
                    value.add(ca);
                    resourceMap.put(url, value);
                } else {//如果不存存在url 加入url和权限
                    Collection<ConfigAttribute> atts = new ArrayList<>();
                    atts.add(ca);
                    resourceMap.put(url, atts);
                }
            }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //object是一个URL ,为用户请求URL
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        String url = request.getRequestURI();
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }
        Collection<ConfigAttribute> attrs = NULL_CONFIG_ATTRIBUTE;
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
            if (entry.getKey().equals(url)) {
                attrs =  entry.getValue();
                break;
            }
        }
        logger.info("URL资源："+request.getRequestURI()+ " -> " + attrs);
        return attrs;

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        for (Map.Entry<String, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
