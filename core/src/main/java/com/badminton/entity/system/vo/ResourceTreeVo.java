package com.badminton.entity.system.vo;

import java.io.Serializable;

/**
 * @desc 用于设置角色的菜单树结构
 * @author zhousg
 * @date 2016年8月22日下午5:42:36
 */
public class ResourceTreeVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String pId;
	
	private String name;
	
	private boolean checked;
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
