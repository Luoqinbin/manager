package com.badminton.test.service;

import com.badminton.entity.test.TestCrud;

import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */
public interface TestCrudService {

    public List<TestCrud> query(TestCrud query);


    public void insert(TestCrud q)throws Exception;


    public TestCrud queryId(String id);


    public void update(TestCrud testCrud)throws Exception;


    public void delete(String id)throws Exception;

}


