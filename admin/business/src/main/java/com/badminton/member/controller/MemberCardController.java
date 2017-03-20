package com.badminton.member.controller;

import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.query.MemberCardQuery;
import com.badminton.entity.system.SysUser;
import com.badminton.entity.test.TestCrud;
import com.badminton.entity.test.query.TestCrudQuery;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageInfo;
import com.badminton.member.service.IMemberCardService;
import com.badminton.member.service.IMemberInfoService;
import com.badminton.member.service.impl.MemberCardServiceImpl;
import com.badminton.result.BaseResult;
import com.badminton.result.PageResult;
import com.badminton.security.service.SysResourcesService;
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
import java.util.List;
import java.util.UUID;

/**
 * Created by Luoqb on 2017/3/13.
 */
@Controller
@RequestMapping("memberCard")
public class MemberCardController {
    @Autowired
    private SysResourcesService sysResourcesService;
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IMemberInfoService memberInfoService;

    @RequestMapping(value = "init")
    public String init(HttpServletRequest request) {
        String menuId = request.getParameter("menuId");
        request.setAttribute("menuId", menuId);
        SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String addBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 1);
        request.setAttribute("addBtn", addBtn);

        String listBtn = sysResourcesService.queryBtnByRole(userDetails.getRoleId(), menuId, 2);
        request.setAttribute("listBtn", listBtn);
        return "member/memberCard/list";
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<MemberCard> queryList(MemberCardQuery query, HttpServletRequest request) {
        PageUtils<MemberCardQuery> pageUtils = new PageUtils<MemberCardQuery>();

        query = pageUtils.sort(query, request, "name", "desc", null);
        List<MemberCard> list = memberCardService.query(query);
        PageResult<MemberCard> result = new PageResult<MemberCard>(new PageInfo<MemberCard>(list));
        return result;
    }

    private BaseResult update(MemberCardQuery query) {
        BaseResult baseResult = new BaseResult();
        MemberCard testCrud = this.memberCardService.queryId(Long.parseLong(query.getId()+""));
        if (testCrud != null) {
            try {
                BeanUtils.copyProperties(testCrud,query );
                SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                try {
                    this.memberCardService.update(testCrud);
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
                this.memberCardService.delete(id);
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
            MemberCard memberCard = this.memberCardService.queryId(id);
            Long max = this.memberInfoService.maxNumber(memberCard.getId()+"");
            if(max == null){
                max = Long.parseLong( memberCard.getRank()+"0001");
                memberCard.setMaxNumber(max);
            }else{
                memberCard.setMaxNumber(max+1);
            }
            baseResult.setData(memberCard);
        } else {
            baseResult.setCode(BaseResult.CODE_FAIL);
            baseResult.setMessage("查询数据失败");
        }

        return baseResult;
    }

    @RequestMapping(value = "addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addOrUpdate(MemberCardQuery query) {
        BaseResult baseResult = new BaseResult();
        query.setEmptyDiscount(String.format("%.2f",Double.parseDouble(query.getEmptyDiscount())/10));
        query.setBusyDiscount(String.format("%.2f",Double.parseDouble(query.getBusyDiscount())/10));
        if (StringUtils.isNotEmpty(query.getId().toString())) {
            //更新
            return this.update(query);
        } else {
            //添加
            MemberCard m = new MemberCard();
            m.setName(query.getName());
            MemberCard m1 = this.memberCardService.queryOne(m);
            if(m1!=null){
                baseResult.setCode(BaseResult.CODE_FAIL);
                baseResult.setMessage("此卡片已经存在");
                return baseResult;
            }
            SysUser userDetails = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            query.setId(new TimestampPkGenerator().next(this.getClass()));
            try {
                this.memberCardService.insert(query);
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
