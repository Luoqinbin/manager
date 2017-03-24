package com.badminton.court.service;

import com.badminton.entity.court.CourtProduct;
import com.badminton.entity.court.FixedOrder;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
public interface CourtProductService {
    public List<CourtProduct>  query(CourtProduct courtProduct);

    public boolean checkOrder(CourtProduct courtProduct);

    public List<CourtProduct> queryByDate(String date,String courtId);

    public CourtProduct queryById(Long id);

    public void update(CourtProduct courtProduct);

    public boolean updateProductFixeOrder(FixedOrder fixedOrder,String cycel, String start, String end, String startTime, String endTime, String courtInfoId) throws Exception;


    public List<CourtProduct> queryByType(CourtProduct courtProduct);
}
