package com.badminton.entity.member;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by Luoqb on 2017/3/13.
 */
public class MemberInfo extends BaseEntity {
    @Column(name = "number")
    private String number;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private Long type;
    @Column(name = "phone")
    private String phone;
    @Column(name = "crated_dt")
    private Date cratedDt;
    @Column(name = "expire_dt")
    private Date expireDt;
    @Column(name = "empty_discount")
    private String emptyDiscount;
    @Column(name = "busy_discount")
    private String busyDiscount;
    @Column(name = "account")
    private Double account;
    @Column(name = "pay_price")
    private Double payPrice;
    @Column(name = "status")
    private Integer  status;
    @Column(name = "weichat")
    private String  weichat;
    @Column(name = "comments")
    private String comments;

    @Transient
    private String typeName;
    @Transient
    private String carPrice;
@Transient
    private String payWay;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCratedDt() {
        return cratedDt;
    }

    public void setCratedDt(Date cratedDt) {
        this.cratedDt = cratedDt;
    }

    public Date getExpireDt() {
        return expireDt;
    }

    public void setExpireDt(Date expireDt) {
        this.expireDt = expireDt;
    }

    public String getEmptyDiscount() {
        return emptyDiscount;
    }

    public void setEmptyDiscount(String emptyDiscount) {
        this.emptyDiscount = emptyDiscount;
    }

    public String getBusyDiscount() {
        return busyDiscount;
    }

    public void setBusyDiscount(String busyDiscount) {
        this.busyDiscount = busyDiscount;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWeichat() {
        return weichat;
    }

    public void setWeichat(String weichat) {
        this.weichat = weichat;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }


}
