package com.badminton.member.service;

import com.badminton.entity.system.SysHoliday;

import java.util.Date;

/**
 * Created by Luoqb on 2017/4/24.
 */
public interface SysHolidayService {

    public SysHoliday queryByDate(Date date);

}
