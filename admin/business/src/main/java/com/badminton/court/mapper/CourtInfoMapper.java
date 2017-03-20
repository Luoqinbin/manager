package com.badminton.court.mapper;

import com.badminton.entity.court.CourtInfo;
import com.badminton.mapper.BaseMapper;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
public interface CourtInfoMapper extends BaseMapper<CourtInfo>{
    public List<String> queryArea();
}
