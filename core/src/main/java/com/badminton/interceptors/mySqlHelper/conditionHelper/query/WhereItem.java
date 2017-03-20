package com.badminton.interceptors.mySqlHelper.conditionHelper.query;

/**
 * @author Ethan
 * @date 下午11:08:28
 * @email windofdusk@gmail.com
 * 类说明
 */
public class WhereItem {
	private String propertyName;
	private Object value;
	
	public WhereItem(String propertyName, Object value){
		this.propertyName = propertyName;
		this.value = value;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
}
