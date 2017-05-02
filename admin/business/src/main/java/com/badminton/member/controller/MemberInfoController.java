package com.badminton.member.controller;

import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.MemberInfo;
import com.badminton.entity.member.MemberOrder;
import com.badminton.entity.member.query.MemberCardQuery;
import com.badminton.entity.member.query.MemberInfoQuery;
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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
@Controller
@RequestMapping("memberInfo")
public class MemberInfoController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private IMemberInfoService memberInfoService;
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IMemberOrderService memberOrderService;

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
        return "member/memberInfo/list";
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<MemberInfo> queryList(MemberInfoQuery query, HttpServletRequest request) {
        PageUtils<MemberInfoQuery> pageUtils = new PageUtils<MemberInfoQuery>();
        query = pageUtils.sort(query, request, "crated_dt", "desc", null);
        query.setStatus(1);
        List<MemberInfo> list = memberInfoService.query(query);
        for(MemberInfo memberInfo:list){
           MemberCard memberCard = memberCardService.queryId(Long.parseLong(memberInfo.getType()+""));
           if(memberCard!=null) {
               memberInfo.setTypeName(memberCard.getName());
               memberInfo.setCarPrice(memberCard.getTitleAccount());
           }
        }
        PageResult<MemberInfo> result = new PageResult<MemberInfo>(new PageInfo<MemberInfo>(list));
        return result;
    }

    private BaseResult update(MemberInfoQuery query) {
        BaseResult baseResult = new BaseResult();
        MemberInfo testCrud = this.memberInfoService.queryId(Long.parseLong(query.getId()+""));
        if (testCrud != null) {
            try {
                BeanUtils.copyProperties(testCrud,query );
                SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                try {
                    this.memberInfoService.update(testCrud);
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
    public BaseResult delete(Long id) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isNotEmpty(id+"")) {
            try {
                this.memberInfoService.delete(id);
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
    public BaseResult queryById(Long id) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isNotEmpty(id+"")) {
            baseResult.setCode(BaseResult.CODE_OK);
            baseResult.setMessage("查询数据成功");
            MemberInfo memberInfo = this.memberInfoService.queryId(id);
            MemberCard memberCard = this.memberCardService.queryId(Long.parseLong( memberInfo.getType()+""));
            memberInfo.setCarPrice(memberCard.getTitleAccount());
            baseResult.setData(memberInfo);
        } else {
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("查询数据失败");
        }

        return baseResult;
    }

    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addOrUpdate(MemberInfoQuery query) {
        BaseResult baseResult = new BaseResult();
        if (StringUtils.isNotEmpty(query.getId().toString())) {
            //更新
            return this.update(query);
        } else {
            //检查是否手机号添加
            MemberInfo m = new MemberInfo();
            m.setPhone(query.getPhone());
            MemberInfo m1 = this.memberInfoService.queryOne(m);
            if(m1!=null){
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setMessage("该手机号已经存在");
                return baseResult;
            }
            //添加
            SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long memberId= new TimestampPkGenerator().next(this.getClass());
            query.setId(memberId);
            query.setCratedDt(new Date());
            MemberCard memberCard = this.memberCardService.queryId(Long.parseLong(query.getType()+""));
            query.setStatus(1);
            query.setEmptyDiscount(memberCard.getEmptyDiscount());
            query.setBusyDiscount(memberCard.getBusyDiscount());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,Integer.parseInt( memberCard.getLast()));
            query.setExpireDt(calendar.getTime());
            query.setComments("后台新增卡");
            query.setCratedDt(new Date());
            query.setAccount(Double.parseDouble(query.getCarPrice()));
            //添加订单
            MemberOrder memberOrder = new MemberOrder();
            memberOrder.setId(new TimestampPkGenerator().next(this.getClass()));
            memberOrder.setMemberId(memberId);
            memberOrder.setPhone(query.getPhone());
            memberOrder.setCreatedDt(new Date());
            memberOrder.setBalance(Double.parseDouble(query.getCarPrice()+""));
            memberOrder.setSource(2);
            memberOrder.setPayType(Integer.parseInt(query.getPayWay()));
            memberOrder.setState(1);
            memberOrder.setOperateType(1);
            try {
                Long max = this.memberInfoService.maxNumber(query.getType()+"");
                if(max == null){
                    max = Long.parseLong( memberCard.getRank()+"0001");
                    query.setNumber(max+"");
                }else{
                    Long num = max+1;
                    String lastNum = num.toString().substring(num.toString().length()-1,num.toString().length());
                    if(lastNum.equals("4")){
                        num++;
                    }
                    query.setNumber(num+"");
                }
                this.memberInfoService.insert(query);
                this.memberOrderService.insert(memberOrder);
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
    @RequestMapping(value = "importXls", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult importXls(@RequestParam(value = "xlsFile", required = false) MultipartFile xlsFile){
        BaseResult baseResult = new BaseResult();
        CommonsMultipartFile cf= (CommonsMultipartFile)xlsFile;
        DiskFileItem fi = (DiskFileItem)cf.getFileItem();
        File f = fi.getStoreLocation();
        SysUser sysUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
           String index = memberInfoService.importXls(f,sysUser);
            baseResult.setData(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseResult.setCode(BaseResult.CODE_OK);
        baseResult.setMessage("数据导入成功!");

        return baseResult;
    }
}
