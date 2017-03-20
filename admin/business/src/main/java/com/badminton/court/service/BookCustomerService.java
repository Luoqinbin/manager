package com.badminton.court.service;

import com.badminton.entity.court.BookCustomer;
import com.badminton.entity.court.query.BookCustomerInfoQuery;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
public interface BookCustomerService {
    public List<BookCustomerInfoQuery> query(BookCustomer customer);

    public BookCustomer queryOne(BookCustomer customer);

    public void insert(BookCustomer customer);
}
