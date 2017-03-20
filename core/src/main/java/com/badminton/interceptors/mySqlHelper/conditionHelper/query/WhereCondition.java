package com.badminton.interceptors.mySqlHelper.conditionHelper.query;


import com.badminton.interceptors.mySqlHelper.conditionHelper.ColumnHelper;
import com.badminton.interceptors.mySqlHelper.conditionHelper.dialect.Dialect;
import com.badminton.utils.DateUtil;
import com.badminton.utils.NullUtil;
import com.badminton.utils.StringUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Ethan
 * @date 上午11:54:50
 * @email windofdusk@gmail.com
 * 类说明
 */
@Component
public class WhereCondition {
	@Resource(name="mysqlDialect")
	private Dialect dialect;
	//方言
	private final String dialectName = "mysql";
	
	/**
	 * 组装自定义where SQL
	 * @param itemList
	 * @return
	 */
	public String toSQL(List<WhereItem> itemList){
		StringBuilder whereBuilder = new StringBuilder();
		if(null == itemList || itemList.size() == 0){
			whereBuilder.append("");
		}else{
			for(WhereItem item : itemList){
				whereBuilder.append(" and ").append(this.whereParse(item.getPropertyName(), item.getValue()));
			}
		}
		return whereBuilder.toString();
	}
	
	public String whereParse(String key, Object val){
		String result = this.convertObjectToString(val);
		StringBuilder sb = new StringBuilder();
		if(NullUtil.isNull(result)){
			sb.append(" 1 = 1 ");
		}else if (key.startsWith(ConditionConstant.PREFIX_NOT_EQ)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_NOT_EQ, key);
			sb.append(ColumnHelper.toColumn(propertyName)).append(" <> ").append(result);
		} else if (key.startsWith(ConditionConstant.PREFIX_GT)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_GT, key);
			sb.append(ColumnHelper.toColumn(propertyName)).append(" > ").append(result);
		} else if (key.startsWith(ConditionConstant.PREFIX_GE)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_GE, key);
			sb.append(ColumnHelper.toColumn(propertyName)).append(" >= ").append(result);
		} else if (key.startsWith(ConditionConstant.PREFIX_LT)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_LT, key);
			sb.append(ColumnHelper.toColumn(propertyName)).append(" < ").append(result);
		} else if (key.startsWith(ConditionConstant.PREFIX_LE)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_LE, key);
			sb.append(ColumnHelper.toColumn(propertyName)).append(" <= ").append(result);
		}  else if (key.startsWith(ConditionConstant.PREFIX_LENGTH)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_LENGTH, key);
			sb.append("LENGTH(").append(ColumnHelper.toColumn(propertyName)).append(") = ").append(result);
		} else if (key.startsWith(ConditionConstant.PREFIX_LIKE)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_LIKE, key);
			StringBuilder builder = new StringBuilder(result);
			builder.deleteCharAt(0);
			builder.deleteCharAt(builder.length() - 1);
			//数据库方言判断
			if("oracle".equals(dialectName)){
				sb.append(ColumnHelper.toColumn(propertyName)).append(" like '%").append(StringUtil.parseRegex(builder.toString())).append("%' ESCAPE '\\'");//FOR ORACLE
			}else if("mysql".equals(dialectName)){
				sb.append(ColumnHelper.toColumn(propertyName)).append(" like '%").append(StringUtil.parseRegex(builder.toString())).append("%' ");//FOR MYSQL
			}else if("sqlserver".equals(dialectName)){
				sb.append(ColumnHelper.toColumn(propertyName)).append(" like '%").append(StringUtil.parseRegex(builder.toString())).append("%' ");//FOR SQLSERVER
			}else{
				sb.append(ColumnHelper.toColumn(propertyName)).append(" like '%").append(StringUtil.parseRegex(builder.toString())).append("%' ");//FOR OTHER
			}
			
		} else if (key.startsWith(ConditionConstant.PREFIX_IN)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_IN, key);
			sb.append(ColumnHelper.toColumn(propertyName)).append(" in ( ").append(result).append(" ) ");
		} else if (key.startsWith(ConditionConstant.PREFIX_NOT_IN)) {
			String propertyName = StringUtil.extractPrefix(ConditionConstant.PREFIX_NOT_IN, key);
			sb.append(ColumnHelper.toColumn(propertyName)).append(" not in ( ").append(result).append(" ) ");
		} else if (key.startsWith(ConditionConstant.PREFIX_BETWEEN)) {
			//todo:wait to be extend
			sb.append(" 1=1 ");
		}
		return sb.toString();
	}
	
	/**
	 * 将Object对象转换为String
	 * @param val
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String convertObjectToString(Object val){
		String resultString = "";
		if(NullUtil.isNull(val)){
			return null;
		}
		//字符串
		if(val instanceof String){
			resultString = "'" + val + "'";
		}else if(val instanceof Date){
			//日期类型
			resultString = "'" + this.convertDateToDateFunctionString((Date)val) + "'";
		}else if(val instanceof Collection){
			StringBuilder sb = new StringBuilder();
			Collection<Object> col = (Collection<Object>)val;
			for(Object object : col){
				sb.append(convertObjectToString(object)).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			resultString = sb.toString();
		}else if(val.getClass().isArray()){
			StringBuilder sb = new StringBuilder();
			Object[] array = (Object[])val;
			for(int i = 0; i < array.length; i++){
				sb.append(convertObjectToString(array[i])).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			resultString = sb.toString();
		}else if(val instanceof Number){
			resultString = String.valueOf(val);
		}else{
			resultString = "''";
		}
		return resultString;
	}
	
	
	public String convertDateToDateFunctionString(Date value){
		String dtValue = DateUtil.date2String(value, "yyyy-MM-dd HH:mm:ss");
		return dtValue;
//		String templateString = dialect.getDateFormatValue();
//		return templateString.replaceFirst("#DATE_FORMAT_VALUE#", dtValue);
	}
	
}




















