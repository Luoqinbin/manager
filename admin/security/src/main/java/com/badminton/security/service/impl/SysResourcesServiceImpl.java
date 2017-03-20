package com.badminton.security.service.impl;

import com.badminton.entity.system.Authority;
import com.badminton.entity.system.SysResources;
import com.badminton.security.mapper.SysResourcesDao;
import com.badminton.security.service.SysResourcesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service("sysResourcesService")
public class SysResourcesServiceImpl implements SysResourcesService {

    @Resource
    private SysResourcesDao dao;

    @Override
    public List<Authority> queryAllByType(String type) {
        return dao.queryAllByType(type);
    }

    @Override
    public List<SysResources> queryAllByUserId(String userId) {
        return dao.queryAllByUserId(userId);
    }

    @Override
    public List<SysResources> queryAllResource(SysResources sysResources)throws Exception {
        List<SysResources> sysResourcesList =  dao.queryAllResource(sysResources);
        return sysResourcesList;
    }

    @Override
    public List<SysResources> queryResourceForName(SysResources sysResources) throws Exception {
        List<SysResources> sysResourcesList =  dao.queryResourceForName(sysResources);
        return sysResourcesList;
    }

    @Override
    public int addResource(SysResources sysResources)throws Exception {
        return dao.addResource(sysResources);
    }

    @Override
    public int delResource(String id) throws Exception {
        dao.deleteRoleResource(id);
        return dao.delResource(id);
    }

    @Override
    public int updateResource(SysResources sysResources)throws Exception {
        return dao.updateResource(sysResources);
    }

    @Override
    public String queryBtnByRole(String roleId, String menuId,int index) {
        List<SysResources> list = dao.queryBtnByRole(roleId,menuId,index);
        StringBuffer sb = new StringBuffer();
        for(SysResources sysResources:list){
            sb.append(sysResources.getBtn_style());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<SysResources> queryLeft(String userId) {
        List<SysResources> listMenu = new ArrayList<>();
        List<SysResources> list = queryAllByUserId(userId);
        //先找所以的一级菜单
        for(SysResources sysResources:list){
            if(sysResources.getResource_parent().equals("0")){
                listMenu.add(sysResources);
            }
        }

        // 为一级菜单设置子菜单，getChild是递归调用的
        for (SysResources menu : listMenu) {
            menu.setChildren(getChild(menu.getId().toString(), list));
        }
        return listMenu;
    }

    @Override
    public List<SysResources> queryTree(SysResources sys) {
        List<SysResources> listMenu = new ArrayList<>();
        List<SysResources> list = null;
        try {
            list = queryAllResource(sys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //先找所以的一级菜单
        for (SysResources sysResources : list) {
            if (sysResources.getResource_parent().equals("0")) {
                sysResources.setTitle(sysResources.getResource_name());
                listMenu.add(sysResources);
            }
        }

        // 为一级菜单设置子菜单，getChild是递归调用的
        for (SysResources menu : listMenu) {
            menu.setTitle(menu.getResource_name());
            menu.setExpanded(true);
            //判定是否有字节点
            for(SysResources s:listMenu){
                if(menu.getId().equals(s.getResource_parent())){
                    menu.setFolder(true);
                    break;
                }
            }
            menu.setChildren(getChild(menu.getId().toString(), list));
        }
        return listMenu;
    }

    /**
     * 递归查找子菜单
     *
     * @param id
     *            当前菜单id
     * @param rootMenu
     *            要查找的列表
     * @return
     */
    private List<SysResources> getChild(String id, List<SysResources> rootMenu) {
        // 子菜单
        List<SysResources> childList = new ArrayList<>();
        for (SysResources menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (StringUtils.isNotBlank(menu.getResource_parent())) {
                if (menu.getResource_parent().equals(id)) {
                    menu.setTitle(menu.getResource_name());
                    menu.setExpanded(true);
                    for(SysResources s:rootMenu) {
                        if (menu.getId().equals(s.getResource_parent())) {
                            menu.setFolder(true);
                            break;
                        }
                    }
                    childList.add(menu);
                    menu.setChildren(getChild(menu.getId().toString(), rootMenu));
                }
            }
        }
         // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

}
