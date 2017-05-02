package com.badminton.member.mapper;

import com.badminton.entity.member.MemberInfo;
import com.badminton.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
public interface MemberInfoMapper extends BaseMapper<MemberInfo>{

    public Long maxNumber(String typeId);

    public List<MemberInfo> queryByNumberOrPhone(@Param(value = "number") String number, @Param(value = "phone")String phone);
}
