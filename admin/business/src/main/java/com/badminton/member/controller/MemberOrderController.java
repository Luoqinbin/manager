package com.badminton.member.controller;

import com.badminton.court.service.FlowRecordService;
import com.badminton.entity.court.FlowRecord;
import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.MemberInfo;
import com.badminton.entity.member.MemberOrder;
import com.badminton.entity.member.query.MemberInfoQuery;
import com.badminton.entity.member.query.MemberOrderQuery;
import com.badminton.entity.member.query.MemberOrderRechargeQuery;
import com.badminton.entity.system.SysUser;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.member.service.IMemberCardService;
import com.badminton.member.service.IMemberInfoService;
import com.badminton.member.service.IMemberOrderService;
import com.badminton.result.BaseResult;
import com.badminton.result.PageResult;
import com.badminton.security.service.SysResourcesService;
import com.badminton.utils.PageUtils;
import com.badminton.utils.TimestampPkGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
@Controller
@RequestMapping("memberOrder")
public class MemberOrderController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IMemberOrderService memberOrderService;
    @Autowired
    private FlowRecordService flowRecordService;

    @RequestMapping(value = "init")
    public String init(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId", menuId);
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("addBtn", addBtn);

        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
        request.setAttribute("listBtn", listBtn);

        List<MemberCard> list =  memberCardService.queryAll();
        request.setAttribute("list", list);
        return "member/memberOrder/list";
    }

    @RequestMapping(value = "rechargeInit")
    public String rechargeInit(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId", menuId);
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("addBtn", addBtn);

        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
        request.setAttribute("listBtn", listBtn);

        List<MemberCard> list =  memberCardService.queryAll();
        request.setAttribute("list", list);
        return "member/memberOrder/rechargeList";
    }

    @RequestMapping(value = "recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult recharge(MemberInfoQuery query,HttpServletRequest request) {
        BaseResult baseResult = new BaseResult();
        MemberInfo memberInfo = memberInfoService.queryId(Long.parseLong( query.getId().toString()));
        Double oldAccount = memberInfo.getAccount();
        Double nowAccount = Double.parseDouble( request.getParameter("rechargePrice"));
        Double newAccount = oldAccount+nowAccount;
        memberInfo.setAccount(newAccount);
        try {
            this.memberInfoService.update(memberInfo);
            //添加订单
            MemberOrder memberOrder = new MemberOrder();
            memberOrder.setId(new TimestampPkGenerator().next(this.getClass()));
            memberOrder.setMemberId(Long.parseLong( memberInfo.getId()+""));
            memberOrder.setPhone(query.getPhone());
            memberOrder.setCreatedDt(new Date());
            memberOrder.setSource(2);
            memberOrder.setPayType(Integer.parseInt(query.getPayWay()));
            memberOrder.setState(1);
            memberOrder.setOperateType(2);
            memberOrder.setBalance(nowAccount);
            String comment = request.getParameter("comment");
            memberOrder.setComments(comment);
            this.memberOrderService.insert(memberOrder);
            //添加记录
            //添加记录
            FlowRecord flowRecord = new FlowRecord();
            flowRecord.setId(new TimestampPkGenerator().next(getClass()));
            flowRecord.setCreatedDt(new Date());
            flowRecord.setOperateType(11);
            flowRecord.setAmount(nowAccount);
            flowRecord.setDirection(1);
            this.flowRecordService.insert(flowRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  baseResult;
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<MemberOrderRechargeQuery> queryList(MemberOrderQuery query, HttpServletRequest request) {
        PageUtils<MemberOrderQuery> pageUtils = new PageUtils<MemberOrderQuery>();
        query = pageUtils.sort(query, request, "crated_dt", "desc", null);
        List<MemberOrderRechargeQuery> list = memberOrderService.query(query);
        PageResult<MemberOrderRechargeQuery> result = new PageResult<MemberOrderRechargeQuery>(new PageInfo<MemberOrderRechargeQuery>(list));
        return result;
    }

}
