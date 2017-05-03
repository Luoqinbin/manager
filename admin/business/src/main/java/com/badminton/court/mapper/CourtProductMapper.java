package com.badminton.court.mapper;

import com.badminton.entity.court.CourtProduct;
import com.badminton.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
public interface CourtProductMapper extends BaseMapper<CourtProduct>{

    public List<CourtProduct> queryByTime(@Param(value = "start") String start, @Param(value = "end")String end , @Param(value = "courtInfoId")String courtInfoId);

    public List<CourtProduct> queryTime(@Param(value = "area") String area,@Param(value = "time") String time);

    public List<CourtProduct> queryTime1(@Param(value = "area") String area,@Param(value = "time") String time);
}
