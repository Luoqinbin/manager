package com.badminton.court.service.impl;

import com.badminton.court.mapper.FlowRecordMapper;
import com.badminton.court.service.FlowRecordService;
import com.badminton.entity.court.FlowRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Luoqb on 2017/3/24.
 */
@Service("flowRecordService")
public class FlowRecordServiceImpl implements FlowRecordService {
    @Resource
    private FlowRecordMapper flowRecordMapper;

    @Override
    public int insert(FlowRecord flowRecord) throws Exception{
        return flowRecordMapper.insertSelective(flowRecord);
    }
}
