package com.badminton.court.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Luoqb on 2017/5/2.
 */
public interface HomeMapper {

    public int countArea(@Param(value = "areaName") String areaName, @Param(value = "startTime")String startTime, @Param(value = "endTime")String endTime);

    public int countOrderArea(@Param(value = "areaName")String areaName,@Param(value = "startTime")String startTime,@Param(value = "endTime")String endTime);
}
