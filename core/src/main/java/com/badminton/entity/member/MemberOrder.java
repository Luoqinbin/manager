package com.badminton.entity.member;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by Luoqb on 2017/3/13.
 */
public class MemberOrder extends BaseEntity {

    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "created_dt")
    private Date createdDt;
    @Column(name = "updated_dt")
    private Date  updatedDt;
    @Column(name = "balance")
    private Double  balance;
    @Column(name = "source")
    private Integer source;
    @Column(name = "pay_type")
    private Integer payType;
    @Column(name = "state")
    private Integer state;
    @Column(name = "operate_type")
    private Integer operateType;
    @Column(name = "book_id")
    private Long  bookId;
    @Column(name = "comments")
    private String comments;
    @Transient
    private String cardNo;
    @Transient
    private String startTimeQuery;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getUpdatedDt() {
        return updatedDt;
    }

    public void setUpdatedDt(Date updatedDt) {
        this.updatedDt = updatedDt;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getComments() {
        return comments;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStartTimeQuery() {
        return startTimeQuery;
    }

    public void setStartTimeQuery(String startTimeQuery) {
        this.startTimeQuery = startTimeQuery;
    }
}
