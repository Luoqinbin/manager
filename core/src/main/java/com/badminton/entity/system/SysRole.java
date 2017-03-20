package com.badminton.entity.system;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/16.
 */
@Table(name="sys_roles")
public class SysRole extends BaseEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="role_name")
    private String role_name;
	
	@Column(name="role_desc")
    private String role_desc;
	
	@Column(name="enable")
    private Integer enable;

	@Column(name="role_auth")
    private String role_auth;

	@Column(name = "create_time")
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
	private Integer status;//1 可以，2 禁用， -1删除

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public String getRole_auth() {
		return role_auth;
	}

	public void setRole_auth(String role_auth) {
		this.role_auth = role_auth;
	}

	public Date getCreate_time() {
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
	}
}
