package com.badminton.security.service.impl;

import com.badminton.entity.system.SysUser;
import com.badminton.security.mapper.SysUserDao;
import com.badminton.security.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Override
    public SysUser loadUserName(String username) {
        return sysUserDao.loadUserName(username);
    }

    @Override
    public void updateUser(SysUser sysUser) throws Exception{
        sysUserDao.updateUser(sysUser);
    }

    public long count(SysUser sysUser){
        return sysUserDao.count(sysUser);
    }

    public List<SysUser> queryListByPage(SysUser sysUser){
        return sysUserDao.queryListByPage(sysUser);
    }

    @Override
    public SysUser queryById(String userid) {
        return sysUserDao.queryBuId(userid);
    }

    @Override
    public void delete(String id) throws Exception {
        sysUserDao.delete(id);
    }

    @Override
    public void insert(SysUser sysUser) throws Exception {
        sysUserDao.insert(sysUser);
    }

    @Override
    public int selectNext() {
        return sysUserDao.selectNext();
    }
    
    public List<SysUser> queryAll(){
    	return sysUserDao.queryAll();
    }
}
