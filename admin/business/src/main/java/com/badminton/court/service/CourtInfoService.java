package com.badminton.court.service;

import com.badminton.entity.court.CourtInfo;
import com.badminton.entity.test.TestCrud;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/15.
 */
public interface CourtInfoService {

    public List<CourtInfo> query(CourtInfo courtInfo);
    public List<String> queryArea();
    public void insert(CourtInfo q)throws Exception;


    public CourtInfo queryId(String id);


    public void update(CourtInfo testCrud)throws Exception;


    public void delete(String id)throws Exception;

    public CourtInfo queryOne(CourtInfo courtInfo);

}
