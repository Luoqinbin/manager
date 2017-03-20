package com.badminton.entity;

import com.badminton.interceptors.mySqlHelper.conditionHelper.query.Condition;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * base entity
 */
@JsonIgnoreProperties(value = {"orderDir", "orderColumn", "start", "length", "draw"})
public class BaseEntity implements Serializable {
    @Transient
    private String orderDir;//ace desc
    @Transient
    private String orderColumn;
    @Transient
    private int start;
    @Transient
    private int length;
    @Transient
    private int draw;
    @Transient
    private Condition condition;
/**********************数据库公用的字段*************************/
    @Id
    private Object id ;
/*    @Column(name = "create_time")
    private Date create_time;
    @Column(name = "create_id")
    private String create_id;
    @Column(name = "create_name")
    private String create_name;
    @Column(name = "update_time")
    private Date update_time;
    @Column(name = "update_name")
    private String update_name;
    @Column(name = "update_id")
    private String update_id;
    @Column(name = "status")
    private Integer status;//1 可以，2 禁用， -1删除*/

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    /*public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getCreate_id() {
        return create_id;
    }

    public void setCreate_id(String create_id) {
        this.create_id = create_id;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_name() {
        return update_name;
    }

    public void setUpdate_name(String update_name) {
        this.update_name = update_name;
    }

    public String getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(String update_id) {
        this.update_id = update_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }*/
}
