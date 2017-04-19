package com.badminton.court.controller;

import com.badminton.court.service.CourtInfoService;
import com.badminton.entity.court.CourtInfo;
import com.badminton.entity.court.query.CourtInfoQuery;
import com.badminton.entity.system.SysUser;
import com.badminton.entity.test.TestCrud;
import com.badminton.entity.test.query.TestCrudQuery;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.result.BaseResult;
import com.badminton.result.PageResult;
import com.badminton.security.service.SysResourcesService;
import com.badminton.utils.DateUtil;
import com.badminton.utils.PageUtils;
import com.badminton.utils.TimestampPkGenerator;
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
import java.util.*;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Controller
@RequestMapping("courtInfo")
public class CourtInfoController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private CourtInfoService courtInfoService;

    @RequestMapping(value = "init")
    public String initList(HttpServletRequest request) {
        try {
            String menuId = request.getParameter("menuId");
            request.setAttribute("menuId", menuId);
            SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
            request.setAttribute("addBtn", addBtn);

            String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
            request.setAttribute("listBtn", listBtn);
            List<String> areaList = this.courtInfoService.queryArea();
            request.setAttribute("areaList", areaList);
            //得到5天的日期
            List<String> dateList = new ArrayList<>();
            dateList.add(DateUtil.date2String(new Date()));
            dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 1)));
            dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 2)));
            dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 3)));
            dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 4)));
            request.setAttribute("dateList", dateList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "court/courtInfoList";
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CourtInfo> queryList(CourtInfoQuery query, HttpServletRequest request) {
        PageUtils<CourtInfoQuery> pageUtils = new PageUtils<CourtInfoQuery>();

        query = pageUtils.sort(query, request, "area", "asc", null);
        List<CourtInfo> list = courtInfoService.query(query);
        list.sort(new Comparator<CourtInfo>() {
            @Override
            public int compare(CourtInfo o1, CourtInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        PageResult<CourtInfo> result = new PageResult<CourtInfo>(new PageInfo<CourtInfo>(list));
        return result;
    }

    private BaseResult update(CourtInfoQuery query) {
        BaseResult baseResult = new BaseResult();
        CourtInfo courtInfo = this.courtInfoService.queryId(query.getId().toString());
        if (courtInfo != null) {
            try {
                BeanUtils.copyProperties(courtInfo,query );
                SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                try {
                    this.courtInfoService.update(courtInfo);
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
                this.courtInfoService.delete(id);
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
            CourtInfo courtInfo = this.courtInfoService.queryId(id);
            baseResult.setData(courtInfo);
        } else {
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("查询数据失败");
        }

        return baseResult;
    }

    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addOrUpdate(CourtInfoQuery query) {
        BaseResult baseResult = new BaseResult();
        int areaIndex = query.getArea().indexOf("F".toUpperCase());
        if(areaIndex==-1){
            query.setArea(query.getArea()+"F");
        }
        query.setSerial(query.getName()+"");
        query.setName(query.getName());
        if (StringUtils.isNotEmpty(query.getId().toString())) {
            //更新
            return this.update(query);
        } else {
            //检查是否重复
            CourtInfo c = new CourtInfo();
            c.setName(query.getName());
            c.setArea(query.getArea());
            CourtInfo courtInfo = courtInfoService.queryOne(c);
            if(courtInfo!=null){
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setMessage("已经添加了相同的场地 ");
                return baseResult;
            }
            //添加
            SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            query.setId(new TimestampPkGenerator().next(getClass()));
            try {
                this.courtInfoService.insert(query);
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
