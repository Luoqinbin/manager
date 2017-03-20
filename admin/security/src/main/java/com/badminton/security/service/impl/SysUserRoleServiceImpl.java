package com.badminton.security.service.impl;


import com.badminton.entity.system.SysUserRole;
import com.badminton.security.mapper.SysUserRoleMapper;
import com.badminton.security.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void delete(String userId) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUser_id(userId);
        List<SysUserRole> list = sysUserRoleMapper.select(sysUserRole);
        for(SysUserRole sur:list){
            sysUserRoleMapper.deleteByPrimaryKey(sur.getId());
        }
    }

    @Override
    public void insert(SysUserRole sysUserRole) {
        sysUserRoleMapper.insert(sysUserRole);
    }

    @Override
    public List<SysUserRole> query(SysUserRole sysUserRole) {
        return sysUserRoleMapper.select(sysUserRole);
    }
}
