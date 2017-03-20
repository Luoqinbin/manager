package com.badminton.entity.system;


import com.badminton.entity.BaseEntity;
import lombok.Data;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

/**
 *
 * @Author mybatis-CodeGenerator
 * @date 2017-02-14 12:51:22
 *
 **/
@Table(name="sys_log")
@Data
public class  SysLog extends BaseEntity {
		/****/
	@Column(name="content")
	private String  content;
	/****/
	@Column(name="title")
	private String  title;

	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "create_id")
	private String createId;
	@Column(name = "create_name")
	private String createName;

   @Transient
    private String btnOptions;
}