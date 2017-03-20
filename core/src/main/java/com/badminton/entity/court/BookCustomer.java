package com.badminton.entity.court;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by Luoqb on 2017/3/15.
 */
public class BookCustomer extends BaseEntity{
    @Column(name = "product_id")
    private String         productId;
    @Column(name = "type")
    private Double type;
    @Column(name = "member_num")
    private String        memberNum;
    @Column(name = "name")
    private String  name;
    @Column(name = "phone")
    private String        phone;
    @Column(name = "note")
    private String note;
    @Column(name = "created_dt")
    private Date      createdDt;
    @Column(name = "updated_dt")
    private Date updatedDt;
    @Column(name = "source")
    private Double           source;
    @Column(name = "pay_type")
    private Double   payType;
    @Column(name = "price")
    private Double          price;
    @Column(name = "state")
    private Integer   state;
    @Column(name = "refund_state")
     private Integer       refundState;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "person")
    private String person;
    @Transient
    private String nameInfo;
    @Transient
    private String area;
    @Transient
    private String createdDtQuery;
    @Transient
    private String areaQuery;

    public String getAreaQuery() {
        return areaQuery;
    }

    public void setAreaQuery(String areaQuery) {
        this.areaQuery = areaQuery;
    }

    public String getNameInfo() {
        return nameInfo;
    }

    public void setNameInfo(String nameInfo) {
        this.nameInfo = nameInfo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCreatedDtQuery() {
        return createdDtQuery;
    }

    public void setCreatedDtQuery(String createdDtQuery) {
        this.createdDtQuery = createdDtQuery;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getType() {
        return type;
    }

    public void setType(Double type) {
        this.type = type;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Double getSource() {
        return source;
    }

    public void setSource(Double source) {
        this.source = source;
    }

    public Double getPayType() {
        return payType;
    }

    public void setPayType(Double payType) {
        this.payType = payType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRefundState() {
        return refundState;
    }

    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
