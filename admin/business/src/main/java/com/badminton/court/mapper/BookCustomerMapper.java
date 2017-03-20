package com.badminton.court.mapper;

import com.badminton.entity.court.BookCustomer;
import com.badminton.entity.court.query.BookCustomerInfoQuery;
import com.badminton.mapper.BaseMapper;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
public interface BookCustomerMapper extends BaseMapper<BookCustomer>{

    public List<BookCustomerInfoQuery> query(BookCustomer customer);

}
