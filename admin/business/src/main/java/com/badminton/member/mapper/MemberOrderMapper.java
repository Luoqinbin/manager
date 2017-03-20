package com.badminton.member.mapper;

import com.badminton.entity.member.MemberOrder;
import com.badminton.entity.member.query.MemberOrderRechargeQuery;
import com.badminton.mapper.BaseMapper;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
public interface MemberOrderMapper  extends BaseMapper<MemberOrder> {
    public List<MemberOrderRechargeQuery> query(MemberOrder memberOrder);
}
