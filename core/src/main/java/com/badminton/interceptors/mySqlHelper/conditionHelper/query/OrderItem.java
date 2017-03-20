package com.badminton.interceptors.mySqlHelper.conditionHelper.query;

import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition.Direction;

/**
 * @author Ethan
 * @date 下午11:07:52
 * @email windofdusk@gmail.com
 * 类说明
 */
public class OrderItem {
	private String property;
	private Direction direction;
	
	public OrderItem(String property, Direction direction){
		super();
		this.property = property;
		this.direction = direction;
	}
	
	public String getProperty(){
		return property;
	}
	
	public void setProperty(String property){
		this.property = property;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
