package com.badminton.security.mapper;

import com.badminton.entity.system.SysRole;
import com.badminton.entity.system.vo.ResourceBtnVo;
import com.badminton.entity.system.vo.ResourceTreeVo;
import com.badminton.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public interface SysRoleMapper extends BaseMapper<SysRole>{
	
	public long count(SysRole sysRole, @Param(value = "roleName") String roleName);
	
    public List<SysRole> queryListByPage(SysRole sysRole);

    public List<ResourceTreeVo> getResource();
    
    public List<String> getResourceByRoleId(@Param(value = "roleId") String roleId);
    
    public List<ResourceBtnVo> getBtnResourceByPartentId(@Param(value = "partentId") String partentId);
    
    public List<String> getBtnResourceByRoleId(@Param(value = "resourceId") String resourceId, @Param(value = "roleId") String roleId);
    
    public void delRoleResByRoleId(@Param(value = "roleId") String roleId);
    
    public void insertRoleResource(@Param(value = "roleId") String roleId, @Param(value = "resourceId") String resourceId);
    
    
    public void delRoleResBtnByRoleId(@Param(value = "roleId") String roleId, @Param(value = "resourceId") String resourceId);
    
    public void insertRoleResourceBtn(@Param(value = "menuId") String menuId, @Param(value = "btnId") String btnId, @Param(value = "roleId") String roleId);
}
