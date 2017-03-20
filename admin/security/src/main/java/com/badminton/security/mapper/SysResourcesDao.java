package com.badminton.security.mapper;

import com.badminton.entity.system.Authority;
import com.badminton.entity.system.SysResources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Sys resources dao.
 */
public interface SysResourcesDao {
    /**
     * Query all by type list.
     *
     * @param type the type
     * @return the list
     */
    public List<Authority> queryAllByType(@Param(value = "type") String type);

    /**
     * Query all by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    public List<SysResources> queryAllByUserId(@Param(value = "userId")String userId);

    /**
     * Query all resource list.
     *查询所有资源信息
     * @param sysResources the sys resources
     * @return the list
     */
    public List<SysResources>  queryAllResource(SysResources sysResources);

    /**
     * Query all resource list.
     *不分页校验用
     * @param sysResources the sys resources
     * @return the list
     */
    public List<SysResources>  queryResourceForName(SysResources sysResources);

    /**
     * Add resource int.
     *资源增加
     * @param sysResources the sys resources
     * @return the int
     * @throws Exception the exception
     */
   public int addResource(SysResources sysResources)throws Exception;

    /**
     * Del resource int.
     *资源删除
     * @param id the id
     * @return the int
     * @throws Exception the exception
     */
    public int delResource(String id)throws Exception;

    /**
     * Update resource int.
     *资源修改
     * @param sysResources the sys resources
     * @return the int
     * @throws Exception the exception
     */
    public int updateResource(SysResources sysResources)throws Exception;

    /**
     * Query all btn list.
     *
     * @return the list
     */
    public List<Authority> queryAllBtn();

    /**
     * Query btn by role list.
     *
     * @param roleId the role id
     * @param menuId the menu id
     * @param index  the index
     * @return the list
     */
    public List<SysResources> queryBtnByRole(@Param(value = "roleId")String roleId, @Param(value = "menuId")String menuId,@Param(value = "index")int index);


    public void deleteRoleResource(String resourceId);

}
