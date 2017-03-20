package com.badminton.entity.system.vo;

import java.io.Serializable;

/**
 * @desc 用于设置角色的按钮权限
 * @author zhousg
 * @date 2016年8月22日下午5:42:15
 */
public class ResourceBtnVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	//是否已经选中，1  是   0  否
	private int isCheck;

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

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
}
