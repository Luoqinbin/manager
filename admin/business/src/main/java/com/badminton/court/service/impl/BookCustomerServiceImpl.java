package com.badminton.court.service.impl;

import com.badminton.court.mapper.BookCustomerMapper;
import com.badminton.court.service.BookCustomerService;
import com.badminton.entity.court.BookCustomer;
import com.badminton.entity.court.query.BookCustomerInfoQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Service("bookCustomerService")
public class BookCustomerServiceImpl implements BookCustomerService{
    @Resource
    private BookCustomerMapper mapper;

    @Override
    public List<BookCustomerInfoQuery> query(BookCustomer customer) {
        try{
            return mapper.query(customer);
        }catch (Exception e){
            e.printStackTrace();
        }
      return null;
    }

    @Override
    public BookCustomer queryOne(BookCustomer customer) {
        return mapper.selectOne(customer);
    }

    @Override
    public void insert(BookCustomer customer) {
        mapper.insert(customer);
    }
}
