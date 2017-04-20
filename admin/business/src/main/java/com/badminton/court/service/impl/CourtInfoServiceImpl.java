package com.badminton.court.service.impl;

import com.badminton.court.mapper.CourtInfoMapper;
import com.badminton.court.service.CourtInfoService;
import com.badminton.entity.court.CourtInfo;
import com.badminton.entity.test.TestCrud;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;
import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Service("courtInfoService")
public class CourtInfoServiceImpl implements CourtInfoService {
    @Resource
    private CourtInfoMapper courtInfoMapper;

    @Override
    public List<CourtInfo> query(CourtInfo courtInfo) {
        if (courtInfo != null){
            if (courtInfo.getOrderDir().equals("asc")) {
                Condition condition = Condition.build() ;
                courtInfo.setCondition(condition.addOrder(courtInfo.getOrderColumn(), OrderCondition.Direction.ASC).addOrder("name", OrderCondition.Direction.ASC));
            } else if (courtInfo.getOrderDir().equals("desc")) {
                courtInfo.setCondition(Condition.build().addOrder(courtInfo.getOrderColumn(), OrderCondition.Direction.DESC).addOrder("name", OrderCondition.Direction.DESC));
            }
        }

        return courtInfoMapper.select(courtInfo);
    }

    @Override
    public List<String> queryArea() {
        return courtInfoMapper.queryArea();
    }

    @Override
    public void insert(CourtInfo q) throws Exception {
        courtInfoMapper.insert(q);
    }

    @Override
    public CourtInfo queryId(String id) {
        return courtInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(CourtInfo testCrud) {
        courtInfoMapper.updateByPrimaryKeySelective(testCrud);
    }

    @Override
    public void delete(String id) throws Exception {
        courtInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CourtInfo queryOne(CourtInfo courtInfo) {
        return courtInfoMapper.selectOne(courtInfo);
    }
}
