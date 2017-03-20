package com.badminton.test.controller;

import com.badminton.entity.system.SysUser;
import com.badminton.entity.test.TestCrud;
import com.badminton.entity.test.query.TestCrudQuery;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.result.BaseResult;
import com.badminton.result.PageResult;
import com.badminton.security.service.SysResourcesService;
import com.badminton.test.service.TestCrudService;
import com.badminton.utils.PageUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
@Controller
@RequestMapping("testCrud")
public class TestCrudController {

    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private TestCrudService testCrudService;

    @RequestMapping(value = "init")
    public String initList(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId", menuId);
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("addBtn", addBtn);

        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
        request.setAttribute("listBtn", listBtn);
        return "test/testCrud/list";
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<TestCrud> queryList(TestCrudQuery query, HttpServletRequest request) {
        PageUtils<TestCrudQuery> pageUtils = new PageUtils<TestCrudQuery>();

        query = pageUtils.sort(query, request, "create_time", "desc", null);
        List<TestCrud> list = testCrudService.query(query);
        PageResult<TestCrud> result = new PageResult<TestCrud>(new PageInfo<TestCrud>(list));
        return result;
    }

    private BaseResult update(TestCrudQuery query) {
        BaseResult baseResult = new BaseResult();
        TestCrud testCrud = this.testCrudService.queryId(query.getId().toString());
        if (testCrud != null) {
            try {
                BeanUtils.copyProperties(testCrud,query );
                SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                /*testCrud.setUpdate_id(userDetails.getId());
                testCrud.setUpdate_time(new Date());
                testCrud.setUpdate_name(userDetails.getName());*/
                try {
                    this.testCrudService.update(testCrud);
                    baseResult.setCode(BaseResult.CODE_OK);
                    baseResult.setMessage("更新数据成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    baseResult.setCode(BaseResult.CODE_FAIL);
                    baseResult.setMessage("更新数据失败");
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("没有找到相应数据，请检查");
        }
        return baseResult;
    }


    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult delete(String id) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isNotEmpty(id)) {
            try {
                this.testCrudService.delete(id);
                baseResult.setCode(BaseResult.CODE_OK);
                baseResult.setMessage("删除数据成功");
            } catch (Exception e) {
                e.printStackTrace();
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setMessage("删除数据失败");
            }
        } else {
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("删除数据失败");
        }

        return baseResult;
    }

    @RequestMapping(value = "queryById", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult queryById(String id) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isNotEmpty(id)) {
            baseResult.setCode(BaseResult.CODE_OK);
            baseResult.setMessage("查询数据成功");
            TestCrud testCrud = this.testCrudService.queryId(id);
            baseResult.setData(testCrud);
        } else {
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("查询数据失败");
        }

        return baseResult;
    }

    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addOrUpdate(TestCrudQuery query) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isNotEmpty(query.getId().toString())) {
            //更新
            return this.update(query);
        } else {
            //添加
            SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
           /* query.setCreate_id(userDetails.getId());
            query.setCreate_time(new Date());
            query.setCreate_name(userDetails.getName());*/
            query.setId(UUID.randomUUID().toString().replace("-", ""));
            try {
                this.testCrudService.insert(query);
                baseResult.setCode(BaseResult.CODE_OK);
                baseResult.setMessage("添加数据成功");
            } catch (Exception e) {
                e.printStackTrace();
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setMessage("添加数据失败");
            }
        }
        return baseResult;
    }

}
