package com.badminton.security.mapper;

import com.badminton.entity.system.SysUser;
import com.badminton.entity.system.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public interface SysUserDao {
    /**
     * 查询用户
     * @param username
     * @return
     */
    public SysUser loadUserName(@Param(value = "username") String username);

    /**
     * 查询用户角色权限
     */
    public List<SysRole> queryRoleByUserId(@Param(value = "userId")String userId);

    /**
     * 登录成功之后更新
     * @param lastLoginTime
     * @param loginIp
     */
    public void updateUser(SysUser sysUser);

    /**
     * id查询用户信息
     * @param userId
     * @return
     */
   public SysUser queryUserByUserId(int userId);

    public long count(SysUser sysUser);

    public List<SysUser> queryListByPage(SysUser sysUser);
    public SysUser queryBuId(@Param(value = "id")String id);

    public void delete(@Param(value = "id")String id);

    public void insert(SysUser sysUser);

    public int selectNext();
    
    public List<SysUser> queryAll();

}
