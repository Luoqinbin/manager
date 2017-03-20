package com.badminton.court.service.impl;

import com.badminton.court.mapper.FixedOrderMapper;
import com.badminton.court.service.FixedOrderService;
import com.badminton.entity.court.FixedOrder;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/16.
 */
@Service("fixedOrderService")
public class FixedOrderServiceImpl implements FixedOrderService {
    @Resource
    private FixedOrderMapper fixedOrderMapper;

    @Override
    public List<FixedOrder> query(FixedOrder fixedOrder) {
        if (fixedOrder != null){
            if (fixedOrder.getOrderDir().equals("asc")) {
                fixedOrder.setCondition(Condition.build().addOrder(fixedOrder.getOrderColumn(), OrderCondition.Direction.ASC));
            } else if (fixedOrder.getOrderDir().equals("desc")) {
                fixedOrder.setCondition(Condition.build().addOrder(fixedOrder.getOrderColumn(), OrderCondition.Direction.DESC));
            }
        }

        return fixedOrderMapper.select(fixedOrder);
    }

    @Override
    public void delete(Long id) {
        fixedOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insert(FixedOrder fixedOrder) {
        fixedOrderMapper.insert(fixedOrder);
    }

    @Override
    public FixedOrder queryOne(FixedOrder fixedOrder) {
        return fixedOrderMapper.selectOne(fixedOrder);
    }
}
