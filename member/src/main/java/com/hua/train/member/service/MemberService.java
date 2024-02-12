package com.hua.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.hua.train.common.exception.BusinessException;
import com.hua.train.common.exception.BusinessExceptionEnum;
import com.hua.train.member.domain.Member;
import com.hua.train.member.domain.MemberExample;
import com.hua.train.member.mapper.MemberMapper;
import com.hua.train.member.resp.MemberLoginResp;
import com.hua.train.req.MemberLoginReq;
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
    Member memberDB = selectByMobile(mobile);
    if (ObjectUtil.isNull(memberDB)) {
      throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
    }
    Member member = new Member();
    member.setId(IdUtil.getSnowflakeNextId());
    member.setMobile(mobile);
    memberMapper.insert(member);
    return member.getId();
  }

  public void sendCode(MemberSendCodeReq req) {
    String mobile = req.getMobile();
    Member memberDB = selectByMobile(mobile);
    //如果手机号不存在，则插入一条记录
    if (ObjectUtil.isNull(memberDB)) {
      Member member = new Member();
      member.setId(IdUtil.getSnowflake(1, 1).nextId());
      member.setMobile(mobile);
      memberMapper.insert(member);
    }
    //生成验证码
    String code = RandomUtil.randomString(4);
    LOG.info("生成短信验证码:{}", code);
    //保存短信记录表 手机号 短信验证码 有效期 是否已经使用 业务类型 发送时间 使用时间
    LOG.info("保存短信记录表");
    //对接短信通道，发送短信
    LOG.info("对接短信通道");
  }


  public MemberLoginResp login(MemberLoginReq req) {
    String mobile = req.getMobile();
    String code = req.getCode();
    Member memberDB = selectByMobile(mobile);
    //如果手机号不存在，则插入一条记录
    if (ObjectUtil.isNull(memberDB)) {
      throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
    }

    //校验短信验证码
    if("8888".equals(code)){
      throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
    }
    return BeanUtil.copyProperties(memberDB,MemberLoginResp.class);

  }

  private Member selectByMobile(String mobile) {
    MemberExample memberExample = new MemberExample();
    memberExample.createCriteria().andMobileEqualTo(mobile);
    List<Member> list = memberMapper.selectByExample(memberExample);
    if (CollUtil.isNotEmpty(list)) {
      return null;
    } else {
      return list.get(0);
    }
  }

}