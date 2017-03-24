package com.badminton.member.service.impl;

import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.MemberInfo;
import com.badminton.entity.system.SysUser;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import com.badminton.member.mapper.MemberInfoMapper;
import com.badminton.member.service.IMemberCardService;
import com.badminton.member.service.IMemberInfoService;
import com.badminton.utils.JxlExcelUtils;
import com.badminton.utils.TimestampPkGenerator;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
@Service("memberInfoService")
public class MemberInfoServiceImpl implements IMemberInfoService {
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Autowired
    private IMemberCardService memberCardService;

    @Override
    public List<MemberInfo> query(MemberInfo memberInfo) {
        if(memberInfo.getOrderDir().equals("asc")) {
            memberInfo.setCondition(Condition.build().addOrder(memberInfo.getOrderColumn(), OrderCondition.Direction.ASC));
        }else{
            memberInfo.setCondition(Condition.build().addOrder(memberInfo.getOrderColumn(), OrderCondition.Direction.DESC));
        }
        return memberInfoMapper.select(memberInfo);
    }


    @Override
    public void insert(MemberInfo q) throws Exception {
        memberInfoMapper.insert(q);
    }

    @Override
    public MemberInfo queryId(Long id) {
        return memberInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(MemberInfo memberCard) throws Exception {
        memberInfoMapper.updateByPrimaryKeySelective(memberCard);
    }

    @Override
    public void delete(Long id) throws Exception {
        memberInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public String importXls(File file, SysUser sysUser) throws Exception{
        String index = "";
        int num = 1;
        JxlExcelUtils jxlExcelUtils = new JxlExcelUtils();
            List<List<List<String>>> list = jxlExcelUtils.readExcel(file);
            List<List<String>> data = list.get(0);
            for (int i = 1; i < data.size(); i++) {
                List<String> detail = data.get(i);
                MemberInfo q = new MemberInfo();
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setNumber(detail.get(0));
                memberInfo = memberInfoMapper.selectOne(memberInfo);
                if(memberInfo==null){
                    memberInfo = new MemberInfo();
                    memberInfo.setNumber(detail.get(0));
                    MemberCard memberCard = new MemberCard();
                    memberCard.setName(detail.get(1));
                    memberCard = memberCardService.queryOne(memberCard);
                    if(memberCard!=null){
                        memberInfo.setId( new TimestampPkGenerator().next(this.getClass()));
                        memberInfo.setType(Long.parseLong( memberCard.getId()+""));
                        memberInfo.setPhone(detail.get(2));
                        memberInfo.setName(detail.get(3));
                        memberInfo.setAccount(Double.parseDouble(detail.get(4)));
                        Date blsj = new SimpleDateFormat("yyyy.MM.dd").parse(detail.get(5));
                        memberInfo.setCratedDt(blsj);
                        memberInfo.setComments(detail.get(6));
                        memberInfo.setStatus(1);
                        memberInfo.setEmptyDiscount(memberCard.getEmptyDiscount());
                        memberInfo.setBusyDiscount(memberCard.getBusyDiscount());
                        //设置到期时间
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(blsj);
                        calendar.add(Calendar.MONTH,Integer.parseInt( memberCard.getLast()));
                        memberInfo.setExpireDt(calendar.getTime());
                        memberInfoMapper.insert(memberInfo);
                    }
                }else{
                    index += (num+i)+",";
                }
            }
        return index;
    }

    @Override
    public Long maxNumber(String typeId) {
        synchronized (this){
           return memberInfoMapper.maxNumber(typeId);
        }
    }

    @Override
    public MemberInfo queryOne(MemberInfo memberInfo) {
        return memberInfoMapper.selectOne(memberInfo);
    }
}
