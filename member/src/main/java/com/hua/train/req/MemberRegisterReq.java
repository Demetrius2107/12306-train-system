package com.hua.train.req;

public class MemberRegisterReq {
  /*private String*/

  private String mobile;

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  @Override
  public String toString() {
    return "MemberRegisterReq{" +
        "mobile='" + mobile + '\'' +
        '}';
  }
}
