package com.badminton.interceptors.mySqlHelper.conditionHelper.dialect;

/** 
*
* @author ethan.yuan E-mail: windofdusk@gmail.com
* @createTime 2016年6月13日 下午3:44:03 
* class description 
*
*/

public abstract class Dialect {
	public static final String RS_COLUMN = "totalCount";
	
	protected static final String SQL_END_DELIMITER = ";";
	
	//数据库日期格式，默认MySql格式
	private String DateFormatValue = "'#DATE_FORMAT_VALUE#','%Y-%m-%d %H:%i:%s'";
	
	/**
	 * 将基础的sql组装为分页的sql
	 * @param sql 原始SQL
	 * @param offset 分页查询的记录偏移量
	 * @param limit 每页限定的记录数量
	 * @param rowcount 记录总行数
	 * @return 拼装好的SQL
	 */
	public String getPagedSql(String sql, int offset, int limit, int rowcount){
		sql = sql.trim();
		boolean isForUpdate = false;
		//处理修改锁定语句
		if(sql.toLowerCase().endsWith(" for update")){
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}
		if(offset < 0){
			offset = 0;
		}
		
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect.append(buildPagedSql(sql, offset, limit, rowcount));
		if(isForUpdate){
			pagingSelect.append(" for update");
		}
		return pagingSelect.toString();
	}
	
	/**
	 * 具体的的拼装语句由子类实现
	 */
	protected abstract String buildPagedSql(String sql, int offset, int limit, int rowcount);
	
	/**
	 * 以传入sql为基础拼装总记录数查询的sql语句
	 * @param sql
	 * @return
	 */
	public String buildCountSql(String sql){
		sql = trimEndDelimiter(sql);
		StringBuffer sb = new StringBuffer(sql.length() + 10);
		sb.append("SELECT COUNT(1) AS ").append(RS_COLUMN).append(" FROM ( ")
		  .append(sql)
		  .append(" ) totalQuery");
		return sb.toString();
	}
	
	public boolean supportsLimit(){
		return true;
	}
	
	protected String trimEndDelimiter(String sql){
		sql = sql.trim();
		if(sql.endsWith(SQL_END_DELIMITER)){
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}
	
	public String getDateFormatValue(){
		return this.DateFormatValue;
	}

}
































