package com.badminton.member.service.impl;

import com.badminton.entity.system.SysHoliday;
import com.badminton.member.mapper.SysHolidayMapper;
import com.badminton.member.service.SysHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Luoqb on 2017/4/24.
 */
@Service("sysHolidayService")
public class SysHolidayServiceImpl implements SysHolidayService {

    @Autowired
    private SysHolidayMapper sysHolidayMapper;

    @Override
    public SysHoliday queryByDate(Date date) {
        SysHoliday sysHoliday = new SysHoliday();
        sysHoliday.setDate(date);
        return sysHolidayMapper.selectOne(sysHoliday);
    }
}
