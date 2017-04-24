package com.badminton.entity.system;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by Luoqb on 2017/4/24.
 */
public class SysHoliday extends BaseEntity{
    @Column(name = "date")
    private Date date;
    @Column(name = "is_holiday")
    private Integer isHoliday;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(Integer isHoliday) {
        this.isHoliday = isHoliday;
    }
}
