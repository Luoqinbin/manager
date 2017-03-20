package com.badminton.admin.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.badminton.entity.system.SysUser;
import com.badminton.entity.system.vo.JsTreeState;
import com.badminton.entity.system.vo.JsTreeVo;
import com.badminton.security.service.SysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.entity.system.SysRole;
import com.badminton.entity.system.query.SysRoleQuery;
import com.badminton.entity.system.vo.ResourceTreeVo;
import com.badminton.result.BaseResult;
import com.badminton.result.PageResult;
import com.badminton.security.Constant;
import com.badminton.utils.PageUtils;
/**
 * @desc 系统角色
 * @author zhousg
 * @date 2016年8月18日上午11:48:40
 */
@Controller
@RequestMapping("sysRole")
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String listGroup(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId",menuId);
        return "system/sysRole/list";
    }
    
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public Object queryList(HttpServletRequest request,SysRoleQuery sysRole) {
        String[] cols = new String[]{"role_name","role_desc","role_auth"};//数据库的列名 ,前提是对象和数据库字段名称一致
        PageUtils<SysRole> pageUtils = new PageUtils<>();
        SysRole role = pageUtils.sort(sysRole,request,"role_name","desc",new String[]{});
        List<SysRole> list = null;
        try{

           list = sysRoleService.queryListByPage(role);
        }catch (Exception e){
            e.printStackTrace();
        }
        PageResult result = new PageResult<>(new PageInfo<>(list));

        return result;
    }
    
    /**
     * @desc 新增角色
     * @author zhousg
     * @date 2016年8月19日上午10:24:39
     * @param request
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "addOrUpdate")
    @ResponseBody
    public Object addOrUpdate(HttpServletRequest request,@ModelAttribute SysRole sysRole,String id) throws Exception{
    	SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
        if(StringUtils.isNotEmpty(id)){
            return this.update(request,sysRole);
        }else {
            sysRole.setRole_auth("ROLE_" + sysRole.getRole_auth());
            sysRole.setCreate_id(sysUser.getId());
            sysRole.setCreate_name(sysUser.getUsername());
            sysRole.setCreate_time(new Date());
            sysRole.setEnable(Constant.TRUE);
            sysRole.setId(UUID.randomUUID().toString().replace("-", ""));
            return sysRoleService.add(sysRole);
        }
    }
    
    /**
     * @desc 根据id获取详情
     * @author zhousg
     * @date 2016年8月19日下午2:41:08
     * @param id
     * @return
     */
    @RequestMapping(value="getById")
    @ResponseBody
    public Object getById(String id){
    	SysRole sysRole = new SysRole();
    	sysRole = sysRoleService.getById(id);
    	sysRole.setRole_auth(sysRole.getRole_auth().replace("ROLE_", ""));
    	BaseResult result = new BaseResult("success", sysRole);
    	return result;
    }
    /**
     * @desc 修改角色
     * @author zhousg
     * @date 2016年8月19日上午11:36:21
     * @param request
     * @param sysRole
     * @return
     * @throws Exception
     */
    public Object update(HttpServletRequest request,@ModelAttribute SysRole sysRole) throws Exception{
    	SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
    	sysRole.setRole_auth("ROLE_"+sysRole.getRole_auth());
    	sysRole.setUpdate_id(sysUser.getId());
    	sysRole.setUpdate_name(sysUser.getUsername());
        sysRole.setUpdate_time(new Date());
    	return sysRoleService.update(sysRole);
    }
    /**
     * @desc 逻辑删除
     * @author zhousg
     * @date 2016年8月19日下午3:23:13
     * @param id
     * @return
     */
    @RequestMapping(value="delete")
    @ResponseBody
    public Object delete(String id){
    	return sysRoleService.delete(id);
    }
    /**
     * @desc 根据角色Id获取资源树
     * @author zhousg
     * @date 2016年8月22日下午4:37:09
     * @param roleId
     * @return
     */
    @RequestMapping(value="getResource")
    @ResponseBody
    public Object getResource(String roleId){
    	List<ResourceTreeVo> list= new ArrayList<>();
    	list = sysRoleService.getResource(roleId);
    	return new BaseResult("success",list);
    }

    @RequestMapping(value="getTree")
    @ResponseBody
    public JsTreeVo getTree(String roleId){
        JsTreeVo root = new JsTreeVo();
        List<JsTreeVo>  jsTreeVoList = new ArrayList<>();
       // tree.put("children",new ArrayList<>());
        List<ResourceTreeVo> list = sysRoleService.getResource(roleId);
        List<ResourceTreeVo> listRoot = new ArrayList<>();
        for(ResourceTreeVo sysResources:list){
            if(sysResources.getpId().equals("0")){
                listRoot.add(sysResources);
            }
        }
        for (ResourceTreeVo menu : listRoot) {
            JsTreeVo jsTreeVo = new JsTreeVo();
            jsTreeVo.setId(menu.getId());
            jsTreeVo.setText(menu.getName());
            jsTreeVo.setIcon("fa fa-folder icon-lg");
            jsTreeVo.setCheckbox(menu.isChecked());
            JsTreeState jsTreeState = new JsTreeState();
            jsTreeState.setOpened(true);
            jsTreeState.setSelected(menu.isChecked());
            jsTreeVo.setState(jsTreeState);
            //查找children
             jsTreeVo.setChildren(getChild(menu.getId(),list));
            jsTreeVoList.add(jsTreeVo);
        }
        root.setId("-1");
        root.setText("后台管理系统");
        root.setIcon("fa fa-folder icon-lg");
        JsTreeState jsTreeState = new JsTreeState();
        jsTreeState.setOpened(true);
        root.setState(jsTreeState);
        root.setChildren(jsTreeVoList);
        return root;
    }
    private List<JsTreeVo> getChild(String id, List<ResourceTreeVo> rootMenu) {
        // 子菜单
        List<JsTreeVo> childList = new ArrayList<>();
        for (ResourceTreeVo menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (StringUtils.isNotEmpty(menu.getpId())) {
                JsTreeVo jsTreeVo = new JsTreeVo();
                if (menu.getpId().equals(id)) {
                    jsTreeVo.setId(menu.getId());
                    jsTreeVo.setText(menu.getName());
                    if(isChild(menu.getId(),rootMenu)){
                        jsTreeVo.setIcon("fa fa-folder icon-lg");
                    }else {
                        jsTreeVo.setIcon("fa fa-file fa-large icon-state-default");
                    }
                    jsTreeVo.setCheckbox(menu.isChecked());
                    JsTreeState jsTreeState = new JsTreeState();
                    jsTreeState.setOpened(true);
                    jsTreeState.setSelected(menu.isChecked());
                    jsTreeVo.setState(jsTreeState);
                    jsTreeVo.setChildren(getChild(menu.getId(),rootMenu));
                    childList.add(jsTreeVo);
                }
            }
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
    /**
     * 判断是否有子节点
     */
    private boolean isChild(String id,List<ResourceTreeVo> rootMenu){
        int index = 0;
        for(ResourceTreeVo resourceTreeVo:rootMenu){
            if (StringUtils.isNotEmpty(resourceTreeVo.getpId())) {
                if (resourceTreeVo.getpId().equals(id)) {
                    index++;
                }
            }
        }
        if(index>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * @desc 根据角色Id获取资源树
     * @author zhousg
     * @date 2016年8月22日下午4:37:09
     * @param roleId
     * @return
     */
    @RequestMapping(value="getResourceBtn")
    @ResponseBody
    public Object getResourceBtn(String partentId,String roleId){
    	return sysRoleService.getResourceByPartentId(partentId,roleId);
    }
    /**
     * @desc 保存权限
     * @author zhousg
     * @date 2016年8月23日下午3:58:03
     * @param roleId
     * @param resourceValue
     * @param resourceId
     * @param btnValue
     * @return
     */
    @RequestMapping(value="saveResource")
    @ResponseBody
    public Object saveResource(HttpServletRequest request,String roleId,String[] resourceValue,String resourceId,String[] btnValue) throws Exception{
    	return sysRoleService.saveResource(request,roleId, resourceValue, resourceId, btnValue);
    }
}
