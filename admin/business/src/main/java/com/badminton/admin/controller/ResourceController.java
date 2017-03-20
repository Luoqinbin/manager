package com.badminton.admin.controller;

import com.badminton.entity.system.SysResources;
import com.badminton.entity.system.SysUser;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.ConditionConstant;
import com.badminton.entity.system.vo.SysResourceVo;

import com.badminton.result.BaseResult;
import com.badminton.security.Constant;


import com.badminton.security.SecurityMetadataSourceService;
import com.badminton.security.service.SysResourcesService;
import com.badminton.utils.PageUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 资源
 */
@Controller
@RequestMapping("resource")
public class ResourceController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private SysResourcesService sysResourcesService;

    /**
     * Resource string.
     *
     * @param request the request
     * @return the string
     */
    @RequestMapping(value = "resource", method = RequestMethod.GET)
    public String resource(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId",menuId);
        return "system/sysResource/list";
    }


    /**
     * 菜单用户所拥有的资源
     *
     * @param sysResourceVo the sys resource vo
     * @param request       the request
     * @return object
     */
    @RequestMapping(value = "queryResources")
    @ResponseBody
    public Object queryResources(SysResourceVo sysResourceVo, HttpServletRequest request) {
        logger.info("菜单查询业务开始");
        try {
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                return null;
            }
            //菜询菜单资源
            SysResources sysResource = new SysResources();
            BeanUtils.copyProperties(sysResource, sysResourceVo);
            PageUtils<SysResources> pageUtils = new PageUtils<>();
           List<SysResources> sysResourcesList = sysResourcesService.queryTree(sysResource);
            return  sysResourcesList;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 不分页查菜单名称和ID和菜单级别
     *
     * @param request the request
     * @return object
     */
    @RequestMapping(value = "queryResourceParent", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> queryResourceParent(String ffData,HttpServletRequest request) {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            //空的查所有
            SysResources sysResource = new SysResources();
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(ffData)) {
                sysResource.setCondition(Condition.build().addWhere(ConditionConstant.PREFIX_LIKE + "resource_name", ffData ));

            }

            List<SysResources> sysResourcesList = sysResourcesService.queryAllResource(sysResource);
            for(SysResources sysRole:sysResourcesList){
                Map<String,Object> map = new HashedMap();
                map.put("id",sysRole.getId());
                map.put("text",sysRole.getResource_name());
                list.add(map);
            }
            Map<String,Object> map = new HashedMap();
            map.put("id","0");
            map.put("text","根节点");
            list.add(0,map);
            return list;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过ID不分页查资源
     *
     * @param id      the id
     * @param request the request
     * @return object
     */
    @RequestMapping(value = "queryResourceForId", method = RequestMethod.POST)
    @ResponseBody
    public Object queryResourceForId(String id, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        logger.info("不分页查菜单名称和ID和菜单级别");
        try {
            if (id == null || id.isEmpty() || id.equals("")) {
                throw new IllegalArgumentException();
            }
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            //空的查所有
            SysResources sysResource = new SysResources();
            sysResource.setId(id);
            sysResource.setOrderColumn("create_time");
            sysResource.setOrderDir("desc");
            List<SysResources> sysResourcesList = sysResourcesService.queryAllResource(sysResource);

            if (sysResourcesList == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("查询菜单失败!");
                return result;
            }
            SysResources resources = new SysResources();
            if (sysResourcesList.size() == 1) {
                resources = sysResourcesList.get(0);
                sysResource = new SysResources();
                sysResource.setId(resources.getResource_parent());
                List<SysResources> list = sysResourcesService.queryAllResource(sysResource);
                if(list!=null&&list.size()>0) {
                    SysResources sys = list.get(0);
                    resources.setParentName(sys.getResource_name());
                }
            }
            result.setData(resources);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        logger.info("不分页查菜单名称和ID和菜单级别{}", result.toString());
        return result;
    }

    /**
     * 增加资源
     *
     * @param sysResourceVo the sys resource vo
     * @param bindingResult the binding result
     * @param request       the request
     * @return object
     */
    @RequestMapping(value = "addOrUpdateResource", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdateResource(SysResourceVo sysResourceVo, BindingResult bindingResult, HttpServletRequest request) {
        if(StringUtils.isEmpty(sysResourceVo.getId())) {
            BaseResult result = new BaseResult();
            try {
                //1、入参校验

                //校验是否登陆用户，登陆用户才有权查菜单
                SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
                if (sysUser == null) {
                    result.setCode(BaseResult.CODE_FAIL);
                    result.setMessage("用户未登陆!");
                    return result;
                }
                //2、表单转换
                SysResources sysResource = new SysResources();
                BeanUtils.copyProperties(sysResource, sysResourceVo);
                sysResource.setId(UUID.randomUUID().toString().replace("-", ""));
                sysResource.setEnable(1);
//                sysResource.setCreate_id(sysUser.getId());
//                sysResource.setCreate_name(sysUser.getUsername());
//                sysResource.setCreate_time(new Date());
                //3、服务调用
                sysResourcesService.addResource(sysResource);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                result.setCode(BaseResult.CODE_PARAM_ERROR);
                result.setMessage(BaseResult.MSG_PARAM_ERROR);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("发生未知异常!");
                return result;
            }
            referCache(request);
            return result;
        }else{
            return this.updateResource(sysResourceVo,bindingResult,request);
        }
    }

    /**
     * 删除资源
     *
     * @param id      the id
     * @param request the request
     * @return object
     */
    @RequestMapping(value = "delResource", method = RequestMethod.POST)
    @ResponseBody
    public Object delResource(String id, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        try {
            if (id == null || id.equals("")) {
                throw new IllegalArgumentException();
            }
            sysResourcesService.delResource(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        referCache(request);
        return result;
}

    /**
     * 修改资源
     *
     * @param
     * @param bindingResult       the binding result
     * @param request             the request
     * @return object
     */
    @RequestMapping(value = "updateResource", method = RequestMethod.POST)
    @ResponseBody
    public Object updateResource(SysResourceVo sysResourceVo, BindingResult bindingResult, HttpServletRequest request) {
        BaseResult result = new BaseResult();
        try {
            //1、入参校验

            if (sysResourceVo.getId() == null) {
                throw new IllegalArgumentException();
            }
            //校验是否登陆用户，登陆用户才有权查菜单
            SysUser sysUser = (SysUser) request.getSession().getAttribute(Constant.USER_SESSION);
            if (sysUser == null) {
                result.setCode(BaseResult.CODE_FAIL);
                result.setMessage("用户未登陆!");
                return result;
            }
            //2、表单转换
            SysResources sysResource = new SysResources();
//            sysResourceVo.setUpdate_id(sysUser.getId());
//            sysResourceVo.setUpdate_name(sysUser.getName());
            sysResourceVo.setResource_parent(sysResourceVo.getResourceParentUpdate());
            BeanUtils.copyProperties(sysResource,sysResourceVo);

            //3、服务调用
            sysResourcesService.updateResource(sysResource);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_PARAM_ERROR);
            result.setMessage(BaseResult.MSG_PARAM_ERROR);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(BaseResult.CODE_FAIL);
            result.setMessage("发生未知异常!");
            return result;
        }
        referCache(request);
        return result;
    }

    /**
     * Refer cache.
     *
     * @param request the request
     */
    public static void referCache(HttpServletRequest request) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        SecurityMetadataSourceService cs = ctx.getBean("securityMetadataSource", SecurityMetadataSourceService.class);
        cs.loadResourceDefine();
    }
}

