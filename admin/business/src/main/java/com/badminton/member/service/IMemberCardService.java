package com.badminton.member.service;

import com.badminton.entity.member.MemberCard;

import java.util.List;

/**
 * Created by Luoqb on 2017/3/13.
 */
public interface IMemberCardService {

    public List<MemberCard> query(MemberCard memberCard);

    public void insert(MemberCard q)throws Exception;


    public MemberCard queryId(Long id);

    public MemberCard queryOne(MemberCard memberCard);


    public void update(MemberCard memberCard)throws Exception;


    public void delete(Long id)throws Exception;

    public List<MemberCard> queryAll();
}
