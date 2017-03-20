package com.badminton.interceptors.mySqlHelper.conditionHelper.query;

import com.badminton.interceptors.mySqlHelper.conditionHelper.ColumnHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Ethan.Yuan
 * @date 下午4:03:36
 * @email windofdusk@gmail.com
 * 类说明
 */
@Component
public class OrderCondition {
	private static final String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";
	private static final String SPACE = " ";
	private static final String COMMA = ",";
	private static final String ORDER_BY = "ORDER BY";
	/**顺序列表**/
//	private List<OrderItem> itemList = new ArrayList<OrderItem>();
	
//	public OrderCondition append(String property, Direction direction){
//		this.itemList.add(new OrderItem(property, direction));
//		return this;
//	}
	
	/**
	 * 组装order by SQL
	 * @param itemList
	 * @return
	 */
	public String toSQL(List<OrderItem> itemList){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < itemList.size(); i++){
			OrderItem orderItem = itemList.get(i);
			if(isSQLInjection(orderItem.getProperty())){
				throw new IllegalArgumentException("SQLInjection property: " + orderItem.getProperty());
			}
			builder.append(COMMA).append(SPACE).append(ColumnHelper.toColumn(orderItem.getProperty()))
								 .append(SPACE).append(orderItem.getDirection().name());
		}
		if(builder.length() == 0){//没有order
			return null;
		}
		builder.deleteCharAt(0);//删除逗号
		return new StringBuilder(" order by ").append(builder).toString();
	}
	
	public OrderCondition(){};
	
	/**
	 * 检查sql注入
	 * @param str
	 * @return
	 */
	public static boolean isSQLInjection(String str){
		return !Pattern.matches(INJECTION_REGEX, str);
	}
	
	public static enum Direction{
		ASC,DESC;
		public static Direction fromString(String value){
			try{
				return Direction.valueOf(value.toUpperCase(Locale.US));
			}catch(Exception e){
				return ASC;
			}
		}
	}
}




































