package com.badminton.member.service;

import com.badminton.entity.member.MemberOrder;
import com.badminton.entity.member.query.MemberOrderRechargeQuery;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
public interface IMemberOrderService {
    public void insert(MemberOrder memberOrder);

    public List<MemberOrderRechargeQuery> query(MemberOrder memberOrder);
}
