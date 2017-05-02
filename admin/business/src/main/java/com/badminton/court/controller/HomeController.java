package com.badminton.court.controller;

import com.badminton.court.mapper.HomeMapper;
import com.badminton.court.service.CourtInfoService;
import com.badminton.court.service.CourtProductService;
import com.badminton.court.service.HomeService;
import com.badminton.entity.court.CourtInfo;
import com.badminton.entity.court.CourtProduct;
import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.MemberInfo;
import com.badminton.entity.system.SysUser;
import com.badminton.member.service.IMemberCardService;
import com.badminton.member.service.IMemberInfoService;
import com.badminton.member.service.impl.MemberInfoServiceImpl;
import com.badminton.result.BaseResult;
import com.badminton.security.service.SysResourcesService;
import com.badminton.utils.DateUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 首页
 */
@Controller
@RequestMapping("home")
public class HomeController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private CourtInfoService courtInfoService;
    @Autowired
    private CourtProductService courtProductService;


    @RequestMapping("index")
    public String index(HttpServletRequest request){
        String menuId = request.getParameter("menuId");
        if(StringUtils.isEmpty(menuId)){
            menuId = "4"; //写死的
        }
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("listBtn", listBtn);
        //查询4F 订场情况
        int countArea4F = homeService.countArea("4F");
        int countOrderArea4F = homeService.countOrderArea("4F");
        request.setAttribute("countArea4F",countArea4F);
        request.setAttribute("countOrderArea4F",countOrderArea4F);


        int countArea5F = homeService.countArea("5F");
        int countOrderArea5F = homeService.countOrderArea("5F");
        request.setAttribute("countArea5F",countArea5F);
        request.setAttribute("countOrderArea5F",countOrderArea5F);

        List<MemberCard> list =  memberCardService.queryAll();
        request.setAttribute("list", list);
        //得到5天的日期
        List<String> dateList = new ArrayList<>();
        dateList.add(DateUtil.date2String(new Date()));
        dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 1)));
        dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 2)));
        dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 3)));
        dateList.add(DateUtil.date2String(DateUtil.getDateAfter(new Date(), 4)));
        request.setAttribute("dateList", dateList);

        return "home/index";
    }
    @ResponseBody
    @RequestMapping(value = "queryByHomeTable")
    public BaseResult queryByHomeTable(String area,String time){
        BaseResult baseResult = new BaseResult();
        Map<String,Object> map = new HashedMap();
        //获得楼层的所以场地
        CourtInfo courtInfo = new CourtInfo();
        courtInfo.setArea("4F");
        courtInfo.setOrderColumn("name");
        courtInfo.setOrderDir("asc");
        List<CourtInfo> list4FArea = courtInfoService.query(courtInfo);
        map.put("list4FArea",list4FArea);
        //查询时间
        List<CourtProduct> list4FProduct = courtProductService.queryTime("4F",StringUtils.isNotEmpty(time)?time:DateUtil.date2String(new Date()));
        map.put("list4FProduct",list4FProduct);

        baseResult.setCode(BaseResult.CODE_OK);
        baseResult.setData(map);
        return baseResult;
    }

    /**
     * 根据手机号或者卡号查询储值卡
     */
    @RequestMapping("queryInitRecharge")
    @ResponseBody
    public BaseResult queryInitRecharge(String number,String phone){
        BaseResult baseResult = new BaseResult();
        MemberInfo info = memberInfoService.queryByNumberOrPhone(number,phone);
        MemberCard memberCard = this.memberCardService.queryId(Long.parseLong( info.getType()+""));
        info.setCarPrice(memberCard.getTitleAccount());
        baseResult.setCode(BaseResult.CODE_OK);
        baseResult.setMessage("获取数据成功");
        baseResult.setData(info);
        return baseResult;
    }
}
