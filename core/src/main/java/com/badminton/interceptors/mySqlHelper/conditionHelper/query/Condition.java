package com.badminton.interceptors.mySqlHelper.conditionHelper.query;

import com.badminton.interceptors.mySqlHelper.conditionHelper.query.OrderCondition.Direction;
import com.google.common.base.Optional;

import java.util.*;


/**
 * @author Ethan
 * @date 下午1:17:13
 * @email windofdusk@gmail.com
 * 处理SQL的条件参数
 */
public class Condition {
	
	public static final String CONDITION_FILTER = "CONDITION_FILTER";
	
	public static final String ORDER_FILTER = "ORDER_FILTER";
	
	/**
	 * 查询条件
	 */
	protected Map<String, Object> filters = null;
	
	/**
	 * 排序字段
	 */
	protected List<OrderItem> orderList = new LinkedList<OrderItem>();
	
	/**
	 * 自定义查询条件字段
	 */
	protected List<WhereItem> whereList = new ArrayList<WhereItem>();
	
	public List<OrderItem> getOrderList(){
		return this.orderList;
	}
	
	public List<WhereItem> getWhereList(){
		return this.whereList;
	}
	
	/**
	 * 返回不带顺序的condition
	 * @return
	 */
	public static Condition build(){
		return build(false);
	}

	/**
	 * @param isOrderMap 参数是否排序
	 * @return
	 */
	public static Condition build(boolean isOrderMap){
		Map<String, Object> filters = isOrderMap ? (new LinkedHashMap<String, Object>()) : (new HashMap<String, Object>());
		return new Condition(filters);
	}
	
	private Condition(Map<String, Object> filters){
		this.filters = filters;
	}
	
	public Condition clear(){
		filters.clear();
		return this;
	}
	
	/**
	 * 将对象转换为condition的map
	 * @param obj
	 * @return
	 */
	public static final Object format(Object object){
		//obj不能为空
		if(null != object && object instanceof Condition){
			object = ((Condition)object).toMap();
		}
		return object;
	}
	
	public Condition add(String key, Object value){
		filters.put(key, value);
		return this;
	}
	
	/**
	 * 根据id查询
	 * @param value
	 * @return
	 */
	public Condition id(Long value){
		Optional<Long> id = Optional.of(value);
		add("id", id.get());
		return this;
	}
	
	/**
	 * 是否删除
	 * @param delete
	 * @return
	 */
	public Condition delete(boolean delete){
		if(delete){
			add("delete", Integer.valueOf(1));
		}else{
			add("delete", Integer.valueOf(0));
		}
		return this;
	}
	
	public Condition addOrder(String property, Direction direction){
		this.orderList.add(new OrderItem(property, direction));
		return this;
	}
	
	public Condition addWhere(String property, Object value){
		this.whereList.add(new WhereItem(property, value));
		return this;
	}
	
	public Map<String, Object> toMap(){
		//更新order条件和where条件
		this.filters.put(CONDITION_FILTER, this.whereList);
		this.filters.put(ORDER_FILTER, this.orderList);
		return this.filters;
	}
}

















