package com.badminton.test.service.impl;

import com.badminton.entity.test.TestCrud;
import com.badminton.test.mapper.TestCrudMapper;
import com.badminton.test.service.TestCrudService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service("testCrudService")
public class TestCrudServiceImpl implements TestCrudService{

    @Resource
    private TestCrudMapper testCrudMapper;

    @Override
    public List<TestCrud> query(TestCrud query) {
        return testCrudMapper.select(query);
    }

    @Override
    public void insert(TestCrud q) throws Exception {
        testCrudMapper.insert(q);
    }

    @Override
    public TestCrud queryId(String id) {
        return testCrudMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TestCrud testCrud) {
        testCrudMapper.updateByPrimaryKeySelective(testCrud);
    }

    @Override
    public void delete(String id) throws Exception {
        testCrudMapper.deleteByPrimaryKey(id);
    }
}
