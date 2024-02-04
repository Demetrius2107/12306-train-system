package com.hua.train.member.service;

import com.hua.train.member.mapper.MemberMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  @Resource
  private MemberMapper memberMapper;

  public int count(){
    return Math.toIntExact(memberMapper.countByExample(null));
  }
}
