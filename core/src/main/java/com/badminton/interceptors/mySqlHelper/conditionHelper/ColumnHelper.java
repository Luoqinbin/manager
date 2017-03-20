package com.badminton.interceptors.mySqlHelper.conditionHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ethan
 * @date 下午9:20:00
 * @email windofdusk@gmail.com
 * 类说明
 */
public class ColumnHelper {
	private static final Pattern pattern = Pattern.compile("[A-Z]");
	private static final String UNDER_LINE = "_";
	
	/**
	 * 将属性名装换为数据表列名
	 * 规则为：大写字母前加下划线.myProperty -> my_property
	 * @param property
	 * @return
	 */
	public static String toColumn(String property){
		StringBuffer columnBuffer = new StringBuffer();
		Matcher matcher = pattern.matcher(property);
		while(matcher.find()){
			matcher.appendReplacement(columnBuffer, UNDER_LINE);
			columnBuffer.append(matcher.group().toLowerCase());
		}
		matcher.appendTail(columnBuffer);
		return columnBuffer.toString();
	}
	
	/**
	 * 类名转换为表名
	 * myClass -> My_Class
	 * @param clazz
	 * @return
	 */
	public static String toTableName(String clazz){
		StringBuffer tableBuffer = new StringBuffer();
		Matcher matcher = pattern.matcher(clazz);
		while(matcher.find()){
			matcher.appendReplacement(tableBuffer, UNDER_LINE);
			tableBuffer.append(matcher.group());
		}
		matcher.appendTail(tableBuffer);
		return tableBuffer.substring(1).toUpperCase();
	}

}
