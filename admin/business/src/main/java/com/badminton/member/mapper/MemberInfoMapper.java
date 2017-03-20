package com.badminton.member.mapper;

import com.badminton.entity.member.MemberInfo;
import com.badminton.mapper.BaseMapper;

/**
 * Created by Luoqb on 2017/3/13.
 */
public interface MemberInfoMapper extends BaseMapper<MemberInfo>{

    public Long maxNumber(String typeId);

}
