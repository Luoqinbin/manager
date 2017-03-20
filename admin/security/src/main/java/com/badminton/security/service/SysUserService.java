package com.badminton.security.service;

import com.badminton.entity.system.SysUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public interface SysUserService {

    public SysUser loadUserName(String username);

    @Transactional
    public void updateUser(SysUser sysUser) throws Exception;

    public long count(SysUser sysUser);

    public List<SysUser> queryListByPage(SysUser sysUser);

    public SysUser queryById(String userid);
    @Transactional
    public void delete(String id) throws Exception;
    @Transactional
    public void insert(SysUser sysUser)throws  Exception;

    public int selectNext();
    
    public List<SysUser> queryAll();
}
