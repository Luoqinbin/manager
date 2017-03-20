package com.badminton.member.service;

import com.badminton.entity.member.MemberCard;
import com.badminton.entity.member.MemberInfo;
import com.badminton.entity.system.SysUser;

import java.io.File;
import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
public interface IMemberInfoService {
    public List<MemberInfo> query(MemberInfo memberInfo);

    public void insert(MemberInfo q)throws Exception;


    public MemberInfo queryId(Long id);


    public void update(MemberInfo memberInfo)throws Exception;


    public void delete(Long id)throws Exception;

    public String importXls(File file, SysUser sysUser) throws Exception;

    public Long  maxNumber(String typeId);

    public MemberInfo queryOne(MemberInfo memberInfo);

}

