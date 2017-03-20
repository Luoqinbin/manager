package com.badminton.security.service;



import com.badminton.entity.system.SysUserRole;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public interface SysUserRoleService {

    public void delete(String userId);

    public void insert(SysUserRole sysUserRole);

    public List<SysUserRole> query(SysUserRole sysUserRole);
}
