package com.badminton.entity.court;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Table(name = "court_product")
public class CourtProduct extends BaseEntity{
    @Column(name = "court_id")
      private Long      courtId;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "book_date")
    private Date bookDate;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date         endTime;
    @Column(name = "state")
    private Integer state;
    @Column(name = "type")
    private Integer       type;
    @Column(name = "price")
    private Double  price;
    @Column(name = "cash_price")
    private Double        cashPrice;
    @Transient
    private String area;
    @Transient
    private String name;
    @Transient
    private String addr;
    @Transient
    private String startTimeQuery;
    @Transient
    private String endTimeQuery;
    @Transient
    private String pid;

    public String getStartTimeQuery() {
        return startTimeQuery;
    }

    public void setStartTimeQuery(String startTimeQuery) {
        this.startTimeQuery = startTimeQuery;
    }

    public String getEndTimeQuery() {
        return endTimeQuery;
    }

    public void setEndTimeQuery(String endTimeQuery) {
        this.endTimeQuery = endTimeQuery;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCashPrice() {
        return cashPrice;
    }

    public void setCashPrice(Double cashPrice) {
        this.cashPrice = cashPrice;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
