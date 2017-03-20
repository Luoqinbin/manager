package com.badminton.security;

import com.badminton.entity.system.SysUser;
import com.badminton.entity.system.SysRole;
import com.badminton.security.mapper.SysUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 该类的主要作用是为Spring Security提供一个经过用户认证后的UserDetails。
 * 该UserDetails包括用户名、密码、是否可用、是否过期等信息。
 */
public class DefaultUserDetailsService implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(DefaultUserDetailsService.class);

    @Autowired
    private UserCache userCache;

    private boolean useCache = false;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Resource
    private SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = null;

        if (this.useCache) {
            user = (SysUser) this.userCache.getUserFromCache(username);
        }
        if (user == null) {
            user = sysUserDao.loadUserName(username);
            if (user == null) {
                throw new UsernameNotFoundException(messages.getMessage(
                        "User.notFound", new Object[] { username },
                        "UserName {0} not found"));
            }
            //查询权限
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            List<SysRole> list = sysUserDao.queryRoleByUserId(user.getId());
            for(SysRole sr:list){
                GrantedAuthority authority = new SimpleGrantedAuthority(sr.getRole_auth());
                authorities.add(authority);
            }
            user.setAuthorities(authorities);
        }
        this.userCache.putUserInCache(user);

        return user;
    }
}
