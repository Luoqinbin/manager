package com.badminton.member.service.impl;

import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.query.MemberCardQuery;
import com.badminton.entity.member.vo.MemberCardVo;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import com.badminton.member.mapper.MemberCardMapper;
import com.badminton.member.service.IMemberCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
@Service("memberCardService")
public class MemberCardServiceImpl implements IMemberCardService {
    @Resource
    private MemberCardMapper memberCardMapper;

    @Override
    public List<MemberCard> query(MemberCard memberCard) {
        if(memberCard.getOrderDir().equals("asc")) {
            memberCard.setCondition(Condition.build().addOrder(memberCard.getOrderColumn(), OrderCondition.Direction.ASC));
        }else {
            memberCard.setCondition(Condition.build().addOrder(memberCard.getOrderColumn(), OrderCondition.Direction.DESC));
        }
        return memberCardMapper.select(memberCard);
    }


    @Override
    public void insert(MemberCard q) throws Exception {
        memberCardMapper.insert(q);
    }

    @Override
    public MemberCard queryId(Long id) {
        return memberCardMapper.selectByPrimaryKey(id);
    }

    @Override
    public MemberCard queryOne(MemberCard memberCard) {
        return memberCardMapper.selectOne(memberCard);
    }

    @Override
    public void update(MemberCard memberCard) throws Exception {
        memberCardMapper.updateByPrimaryKeySelective(memberCard);
    }

    @Override
    public void delete(Long id) throws Exception {
        memberCardMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<MemberCard> queryAll() {
        MemberCard memberCard=new MemberCard();
       // memberCard.setStatus("1");
        return memberCardMapper.select(memberCard);
    }
}
