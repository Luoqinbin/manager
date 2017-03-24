package com.badminton.court.service;

import com.badminton.entity.court.FixedOrder;
import com.badminton.result.BaseResult;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/16.
 */
public interface FixedOrderService {
    public List<FixedOrder> query(FixedOrder fixedOrder);

    public void delete(Long id);

    public void insert(FixedOrder fixedOrder);

    public FixedOrder queryOne(FixedOrder fixedOrder);

    public FixedOrder queryById(Object id);

    public void update(FixedOrder fixedOrder);

    public BaseResult addFixedOrder(FixedOrder fixedOrder) throws Exception;
}
