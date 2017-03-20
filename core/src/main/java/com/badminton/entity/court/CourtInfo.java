package com.badminton.entity.court;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by Luoqb on 2017/3/15.
 */
@Table(name = "court_info")
public class CourtInfo extends BaseEntity{
    @Column(name = "serial")
     private String       serial;
    @Column(name = "Name")
    private String   name;
    @Column(name = "type")
      private Double      type;
    @Column(name = "note")
   private String note;
    @Column(name = "area")
   private String         area;
    @Column(name = "busy_base_price")
    private Double busyBasePrice;
    @Column(name = "empty_base_price")
    private Double         emptyBasePrice;
    @Column(name = "busy_cash_price")
    private Double busyCashPrice;
    @Column(name = "empty_cash_price")
    private Double      emptyCashPrice;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getType() {
        return type;
    }

    public void setType(Double type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Double getBusyBasePrice() {
        return busyBasePrice;
    }

    public void setBusyBasePrice(Double busyBasePrice) {
        this.busyBasePrice = busyBasePrice;
    }

    public Double getEmptyBasePrice() {
        return emptyBasePrice;
    }

    public void setEmptyBasePrice(Double emptyBasePrice) {
        this.emptyBasePrice = emptyBasePrice;
    }

    public Double getBusyCashPrice() {
        return busyCashPrice;
    }

    public void setBusyCashPrice(Double busyCashPrice) {
        this.busyCashPrice = busyCashPrice;
    }

    public Double getEmptyCashPrice() {
        return emptyCashPrice;
    }

    public void setEmptyCashPrice(Double emptyCashPrice) {
        this.emptyCashPrice = emptyCashPrice;
    }
}
