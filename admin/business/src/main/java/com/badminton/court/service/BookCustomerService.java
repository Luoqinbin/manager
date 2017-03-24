package com.badminton.court.service;

import com.badminton.entity.court.BookCustomer;
import com.badminton.entity.court.query.BookCustomerInfoQuery;
import com.badminton.result.BaseResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
public interface BookCustomerService {
    public List<BookCustomerInfoQuery> query(BookCustomer customer);

    public BookCustomer queryOne(BookCustomer customer);

    public void insert(BookCustomer customer)  throws Exception;


    public void deleteById(BookCustomer bookCustomer);

    @Transactional
    public BaseResult insertOrder(String areaId,String price,String payType,String phone,String date,String person,String startTime,String endTime) throws Exception;
        ;
}
