package com.badminton.court.controller;

import com.badminton.court.service.BookCustomerService;
import com.badminton.court.service.CourtInfoService;
import com.badminton.court.service.CourtProductService;
import com.badminton.court.service.FlowRecordService;
import com.badminton.entity.court.BookCustomer;
import com.badminton.entity.court.CourtInfo;
import com.badminton.entity.court.CourtProduct;
import com.badminton.entity.court.FlowRecord;
import com.badminton.entity.court.query.BookCustomerInfoQuery;
import com.badminton.entity.court.query.BookCustomerQuery;
import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.MemberInfo;
import com.badminton.entity.system.SysUser;
import com.badminton.entity.test.TestCrud;
import com.badminton.entity.test.query.TestCrudQuery;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.interceptors.mySqlHelper.pagehelper.util.StringUtil;
import com.badminton.member.service.IMemberCardService;
import com.badminton.member.service.IMemberInfoService;
import com.badminton.member.service.impl.MemberCardServiceImpl;
import com.badminton.result.BaseResult;
import com.badminton.result.PageResult;
import com.badminton.security.service.SysResourcesService;
import com.badminton.test.service.TestCrudService;
import com.badminton.utils.DateUtil;
import com.badminton.utils.PageUtils;
import com.badminton.utils.TimestampPkGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Controller
@RequestMapping("bookCustomer")
public class BookCustomerController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private BookCustomerService bookCustomerService;
    @Autowired
    private CourtInfoService courtInfoService;
    @Autowired
    private CourtProductService courtProductService;
    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private FlowRecordService flowRecordService;

    @RequestMapping(value = "init")
    public String initList(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId", menuId);
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("addBtn", addBtn);

        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
        request.setAttribute("listBtn", listBtn);

        //根据日期查询区域场地
/*        //开始时间
        List<String> listH= new ArrayList<>();
        for(int i=0;i<24;i++){
            listH.add(i+"");
        }
        request.setAttribute("listH",listH);
        List<String> listM= new ArrayList<>();
        int num = 10;
        for(int i=0;i<6;i++){
            listM.add((num*i)+"");
        }
        request.setAttribute("listM",listM);
        //区域
        List<CourtInfo> courtInfoList = courtInfoService.query(null);
        request.setAttribute("courtInfoList",courtInfoList);
        //结束时间
        List<String> listOver = new ArrayList<>();
        for(int i=0;i<24;i++){
            listOver.add(i+":00");
        }
        request.setAttribute("listOver",listOver);*/
        List<CourtInfo>list= this.courtInfoService.query(null);
        request.setAttribute("list",list);
        return "court/bookCustomerList";
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<BookCustomerInfoQuery> queryList(BookCustomerQuery query, HttpServletRequest request) {
        PageUtils<BookCustomerQuery> pageUtils = new PageUtils<BookCustomerQuery>();
        if(StringUtil.isNotEmpty(query.getAreaQuery())){
            String[] str = query.getAreaQuery().split(",");
            query.setArea(str[0]);
            query.setNameInfo(str[1]);
        }
        query = pageUtils.sort(query, request, "created_dt", "desc", null);
        List<BookCustomerInfoQuery> list = bookCustomerService.query(query);
        PageResult<BookCustomerInfoQuery> result = new PageResult<BookCustomerInfoQuery>(new PageInfo<BookCustomerInfoQuery>(list));
        return result;
    }
    @RequestMapping(value = "selectByData", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult selectByData(String date,String id){
        BaseResult baseResult = new BaseResult();
        List<CourtProduct> list = this.courtProductService.queryByDate(date,id);
        baseResult.setData(list);
        baseResult.setMessage("获取数据成功");
        baseResult.setCode(BaseResult.CODE_OK);
        return baseResult;
    }

    //订场
    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addOrder(String areaId,String price,String payType,String phone,String date,String person,String startTime,String endTime) {
        try {

            return this.bookCustomerService.insertOrder(areaId,price,payType,phone,date,person,startTime,endTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new BaseResult("订场失败",BaseResult.CODE_FAIL);


    }

}
