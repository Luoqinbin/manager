package com.badminton.entity.member;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * Created by Luoqb on 2017/3/13.
 */
public class MemberCard extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "rank")
    private Integer rank;
    @Column(name = "empty_discount")
    private String emptyDiscount;
    @Column(name = "busy_discount")
    private String busyDiscount;
    @Column(name = "title_account")
    private String titleAccount;
    @Column(name = "price")
    private String price;
    @Column(name = "payDate")
    private Integer payDate;
    @Column(name = "last")
    private String last;
    @Column(name = "status")
    private String status;
    @Transient
    private Long maxNumber;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public String getTitleAccount() {
        return titleAccount;
    }

    public void setTitleAccount(String titleAccount) {
        this.titleAccount = titleAccount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPayDate() {
        return payDate;
    }

    public void setPayDate(Integer payDate) {
        this.payDate = payDate;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Long maxNumber) {
        this.maxNumber = maxNumber;
    }
}
