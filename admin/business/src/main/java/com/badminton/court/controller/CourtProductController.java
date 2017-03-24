package com.badminton.court.controller;

import com.badminton.court.service.CourtInfoService;
import com.badminton.court.service.CourtProductService;
import com.badminton.entity.court.CourtInfo;
import com.badminton.entity.court.CourtProduct;
import com.badminton.entity.court.query.CourtProductQuery;
import com.badminton.entity.system.SysUser;
import com.badminton.entity.test.TestCrud;
import com.badminton.entity.test.query.TestCrudQuery;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.result.PageResult;
import com.badminton.security.service.SysResourcesService;
import com.badminton.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Controller
@RequestMapping("courtProduct")
public class CourtProductController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private CourtProductService courtProductService;
    @Autowired
    private CourtInfoService courtInfoService;

    @RequestMapping(value = "init")
    public String initList(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId", menuId);
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("addBtn", addBtn);

        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
        request.setAttribute("listBtn", listBtn);
        List<CourtInfo> list = courtInfoService.query(null);
        request.setAttribute("list",list);
        return "court/courtProudctList";
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CourtProduct> queryList(CourtProductQuery query, HttpServletRequest request) {
        PageUtils<CourtProductQuery> pageUtils = new PageUtils<CourtProductQuery>();

        query = pageUtils.sort(query, request, "end_time", "asc", null);
        List<CourtProduct> list = courtProductService.query(query);
        for(CourtProduct c:list){
            CourtInfo courtInfo = courtInfoService.queryId(c.getCourtId()+"");
            c.setArea(courtInfo.getArea());
            c.setAddr(courtInfo.getName()+"");
        }
        PageResult<CourtProduct> result = new PageResult<CourtProduct>(new PageInfo<CourtProduct>(list));
        return result;
    }

}
