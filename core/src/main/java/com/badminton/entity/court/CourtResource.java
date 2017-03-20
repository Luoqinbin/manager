package com.badminton.entity.court;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Table(name = "court_resource")
public class CourtResource extends BaseEntity{
    @Column(name = "court_id")
     private String       courtId;
    @Column(name = "product_id")
    private String  productId;
    @Column(name = "book_date")
    private Date bookDate;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date        endTime;
    @Column(name = "state")
    private Double state;

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Double getState() {
        return state;
    }

    public void setState(Double state) {
        this.state = state;
    }
}
