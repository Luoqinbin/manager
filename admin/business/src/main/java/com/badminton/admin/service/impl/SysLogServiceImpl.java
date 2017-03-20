package com.badminton.admin.service.impl;

import com.badminton.admin.mapper.SysLogMapper;
import com.badminton.admin.service.SysLogService;
import com.badminton.entity.system.SysLog;
import com.badminton.entity.system.query.SysLogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
*
* @Author CodeGenerator
* @date 2016-12-08 14:34:55
*
**/
@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
    @Resource
    private SysLogMapper sysLogMapper;

    private Logger logger = LoggerFactory.getLogger(SysLogServiceImpl.class);

    @Override
    public List<SysLog> query(SysLogQuery query) {
        return sysLogMapper.select(query);
    }

    @Override
    public void insert(SysLog sysLog) {
        sysLogMapper.insert(sysLog);
    }

    @Override
    public void update(SysLog sysLog) {
        sysLogMapper.updateByPrimaryKeySelective(sysLog);
    }

    @Override
    public SysLog queryOne(String id) {
        SysLogQuery sysLogQuery = new SysLogQuery();
        sysLogQuery.setId(id);
        return sysLogMapper.selectOne(sysLogQuery);
    }

    @Override
    public void delete(String id) {
        sysLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SysLog queryById(String id) {
        return sysLogMapper.selectByPrimaryKey(id);
    }
}