package com.badminton.member.service.impl;

import com.badminton.entity.member.MemberOrder;
import com.badminton.entity.member.query.MemberOrderRechargeQuery;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import com.badminton.member.mapper.MemberOrderMapper;
import com.badminton.member.service.IMemberOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
@Service("memberOrderService")
public class MemberOrderServiceImpl implements IMemberOrderService{
    @Autowired
    private MemberOrderMapper memberOrderMapper;

    @Override
    public void insert(MemberOrder memberOrder) {
        memberOrderMapper.insert(memberOrder);
    }

    @Override
    public List<MemberOrderRechargeQuery> query(MemberOrder memberOrder) {
        if(memberOrder.getOrderDir().equals("asc")) {
            memberOrder.setCondition(Condition.build().addOrder(memberOrder.getOrderColumn(), OrderCondition.Direction.ASC));
        }else{
            memberOrder.setCondition(Condition.build().addOrder(memberOrder.getOrderColumn(), OrderCondition.Direction.DESC));
        }
        try{
            return memberOrderMapper.query( memberOrder);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
