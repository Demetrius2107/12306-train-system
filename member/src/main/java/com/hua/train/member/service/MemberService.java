package com.hua.train.member.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.hua.train.common.exception.BusinessException;
import com.hua.train.common.exception.BusinessExceptionEnum;
import com.hua.train.member.domain.Member;
import com.hua.train.member.domain.MemberExample;
import com.hua.train.member.mapper.MemberMapper;
import com.hua.train.req.MemberRegisterReq;
import com.hua.train.req.MemberSendCodeReq;
import jakarta.annotation.Resource;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);


  @Resource
  private MemberMapper memberMapper;

  public int count() {
    return Math.toIntExact(memberMapper.countByExample(null));
  }

  public long register(MemberRegisterReq req) {
    String mobile = req.getMobile();
    MemberExample memberExample = new MemberExample();
    memberExample.createCriteria().andMobileEqualTo(mobile);
    List<Member> list = memberMapper.selectByExample(memberExample);

    if (CollUtil.isNotEmpty(list)) {
      //return list.get(0).getId();
      throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
    }

    Member member = new Member();
    //member.setId(System.currentTimeMillis());
    member.setId(IdUtil.getSnowflake(1, 1).nextId());
    member.setMobile(mobile);

    memberMapper.insert(member);
    return member.getId();
  }

  public void sendCode(MemberSendCodeReq req) {
    String mobile = req.getMobile();
    MemberExample memberExample = new MemberExample();
    memberExample.createCriteria().andMobileEqualTo(mobile);
    List<Member> list = memberMapper.selectByExample(memberExample);
    //如果手机号不存在，则插入一条记录
    if (ObjectUtil.isNull(list)) {
      Member member = new Member();
      member.setId(IdUtil.getSnowflake(1, 1).nextId());
      member.setMobile(mobile);
      memberMapper.insert(member);
    }
    //生成验证码
    String code = RandomUtil.randomString(4);
    LOG.info("生成短信验证码:{}",code);
    //保存短信记录表 手机号 短信验证码 有效期 是否已经使用 业务类型 发送时间 使用时间
    LOG.info("保存短信记录表");
    //对接短信通道，发送短信
    LOG.info("对接短信通道");
  }


}
