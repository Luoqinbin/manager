package com.badminton.entity.court;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Luoqb on 2017/3/24.
 */
@Table(name = "flow_record")
public class FlowRecord extends BaseEntity {

    @Column(name = "weichat")
    private String weichat;
    @Column(name = "created_dt")
    private Date createdDt;
    @Column(name = "updated_dt")
    private Date updatedDt;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "operate_type")
    private int operateType;
    @Column(name = "source_type")
    private int sourceType;
    @Column(name = "direction")
    private int direction;
    @Column(name = "object_id")
    private String objectId;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "note")
    private String note;

    public String getWeichat() {
        return weichat;
    }

    public void setWeichat(String weichat) {
        this.weichat = weichat;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }


    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
}
