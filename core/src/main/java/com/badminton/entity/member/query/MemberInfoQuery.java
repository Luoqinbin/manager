package com.badminton.entity.member.query;

import com.badminton.entity.member.MemberInfo;

/**
 * Created by Luoqb on 2017/3/13.
 */
public class MemberInfoQuery extends MemberInfo {
    private String carPrice;//卡额
    private String payWay;//支付方式

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
}
