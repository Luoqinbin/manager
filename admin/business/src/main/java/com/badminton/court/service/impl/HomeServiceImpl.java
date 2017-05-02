package com.badminton.court.service.impl;

import com.badminton.court.mapper.HomeMapper;
import com.badminton.court.service.HomeService;
import com.badminton.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 首页实现
 */
@Service("homeService")
public class HomeServiceImpl implements HomeService{
    @Resource
    private HomeMapper homeMapper;

    @Override
    public int countArea(String areaName) {
        return homeMapper.countArea(areaName, DateUtil.date2String(new Date()),DateUtil.date2String(new Date()));
    }

    @Override
    public int countOrderArea(String areaName) {
        return homeMapper.countOrderArea(areaName, DateUtil.date2String(new Date()),DateUtil.date2String(new Date()));
    }
}
