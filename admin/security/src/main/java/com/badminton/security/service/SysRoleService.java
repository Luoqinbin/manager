package com.badminton.security.service;

import com.badminton.entity.system.SysRole;
import com.badminton.entity.system.vo.ResourceTreeVo;
import com.badminton.result.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by Administrator on 2016/8/16.
 */
public interface SysRoleService {

	public long count(SysRole sysRole, String roleName);

    public List<SysRole> queryListByPage(SysRole sysRole);
    
    public BaseResult add(SysRole sysRole) throws Exception;

    public BaseResult update(SysRole sysRole) throws Exception;
    
    public List<SysRole> queryAll();

    public SysRole getById(String id);
    
    public BaseResult delete(String id);
    
    public List<ResourceTreeVo> getResource(String roleId);
    
    public BaseResult getResourceByPartentId(String partentId, String roleId);
    
    public BaseResult saveResource(HttpServletRequest request, String roleId, String[] resourceValue, String resourceId, String[] btnValue) throws Exception;
}
