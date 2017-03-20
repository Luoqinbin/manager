package com.badminton.security.service.impl;

import com.badminton.entity.system.SysRole;
import com.badminton.entity.system.vo.ResourceBtnVo;
import com.badminton.entity.system.vo.ResourceTreeVo;
import com.badminton.result.BaseResult;
import com.badminton.security.Constant;
import com.badminton.security.SecurityMetadataSourceService;
import com.badminton.security.mapper.SysRoleMapper;
import com.badminton.security.service.SysRoleService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc 角色Service
 * @author zhousg
 * @date 2016年8月19日上午10:30:36
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;


    public long count(SysRole sysRole, String roleName){
        return sysRoleMapper.count(sysRole,roleName);
    }
    @Override
    public List<SysRole> queryListByPage(SysRole sysRole){
        return sysRoleMapper.queryListByPage(sysRole);
    }
    
    /**
     * 新增
     */
    @Override
    public BaseResult add(SysRole sysRole) throws Exception{
    	//判断角色名是否存在
    	SysRole check = new SysRole();
    	check.setRole_name(sysRole.getRole_name());
    	if(sysRoleMapper.selectCount(check)>0){
    		return new BaseResult("该角色名已存在", BaseResult.CODE_FAIL);
    	}
    	//判断角色代码是否存在
    	check = new SysRole();
    	check.setRole_auth(sysRole.getRole_auth());
    	if(sysRoleMapper.selectCount(check)>0){
    		return new BaseResult("该角色代码已存在", BaseResult.CODE_FAIL);
    	}
		sysRoleMapper.insert(sysRole);
		return new BaseResult();
    }
    /**
     * 修改
     */
    @Override
    public BaseResult update(SysRole sysRole) throws Exception{
    	
    	//判断角色名称是否存在
    	List<SysRole> checkList = new ArrayList<>();
    	SysRole check = new SysRole();
    	check.setRole_name(sysRole.getRole_name());
    	checkList = sysRoleMapper.select(check);
    	if(null != checkList && checkList.size() > 0){
    		for (SysRole sysRole2 : checkList) {
				if(!sysRole.getId() .equals( sysRole2.getId())){
					return new BaseResult("该角色名称已存在", BaseResult.CODE_FAIL);
				}
			}
    	}
    	//判断角色代码是否存在
    	check = new SysRole();
    	check.setRole_auth(sysRole.getRole_auth());
    	checkList = sysRoleMapper.select(check);
    	if(null != checkList && checkList.size() > 0){
    		for (SysRole sysRole2 : checkList) {
				if(!sysRole.getId().equals( sysRole2.getId())){
					return new BaseResult("该角色代码已存在", BaseResult.CODE_FAIL);
				}
			}
    	}
    	sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    	return new BaseResult();
    }
    /**
     * 根据Id获取
     */
    public SysRole getById(String id){
    	SysRole sysRole = new SysRole();
    	sysRole = sysRoleMapper.selectByPrimaryKey(id);
    	return sysRole;
    }
    
    public List<SysRole> queryAll(){
        SysRole sysRole = new SysRole();
        sysRole.setEnable(Constant.TRUE);
        return sysRoleMapper.select(sysRole);
    }
    /**
     * 逻辑删除
     */
    public BaseResult delete(String id){
    	sysRoleMapper.deleteByPrimaryKey(id);
    	return new BaseResult();
    }
    
    /**
     * 获取角色对应的资源树
     */
    public List<ResourceTreeVo> getResource(String roleId){
    	List<ResourceTreeVo> returnList = new ArrayList<>();
    	List<ResourceTreeVo> allList = sysRoleMapper.getResource();
    	List<String> roleList = sysRoleMapper.getResourceByRoleId(roleId);
    	for (ResourceTreeVo rt : allList) {
			if(roleList.contains(rt.getId())){
				rt.setChecked(true);
			}
			returnList.add(rt);
		}
    	return returnList;
    }
    /**
     * 根据父节点获取到按钮资源
     */
    public BaseResult getResourceByPartentId(String partentId,String roleId){
    	List<ResourceBtnVo> returnList = new ArrayList<>();
    	List<ResourceBtnVo> list = new ArrayList<>();
    	list = sysRoleMapper.getBtnResourceByPartentId(partentId);
    	List<String> btnList = sysRoleMapper.getBtnResourceByRoleId(partentId, roleId);
    	for (ResourceBtnVo rb : list) {
			if(btnList.contains(rb.getId())){
				rb.setIsCheck(Constant.TRUE);
			}
			returnList.add(rb);
		}
    	return new BaseResult("success",returnList);
    }
    /**
     * 保存角色的权限信息
     */
    public BaseResult saveResource(HttpServletRequest request, String roleId, String[] resourceValue, String resourceId, String[] btnValue) throws Exception{
    	//if(null != roleId && roleId > 0){
    		//1.先根据roleId删除现有的资源权限
        	sysRoleMapper.delRoleResByRoleId(roleId);
        	//2.添加现在选择的权限
        	if(null != resourceValue && resourceValue.length > 0){
        		for (String resId : resourceValue) {
        			sysRoleMapper.insertRoleResource(roleId, resId);
    			}
        	}
    	//}
    	//if(null != resourceId && resourceId > 0){
    		//3.先根据 roleId resourceId 删除现有的按钮权限
        	sysRoleMapper.delRoleResBtnByRoleId(roleId, resourceId);
        	//4.添加现在选择的按钮权限
        	if(null != btnValue && btnValue.length > 0){
        		for (String btnId : btnValue) {
        			sysRoleMapper.insertRoleResourceBtn(resourceId, btnId, roleId);
    			}
        	}
    	//}

		//刷新缓存
		ApplicationContext ctx =  WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		SecurityMetadataSourceService cs = ctx.getBean("securityMetadataSource",SecurityMetadataSourceService.class);
		cs.loadResourceDefine();
		return new BaseResult();
    }
}
