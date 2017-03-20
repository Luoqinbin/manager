package com.badminton.interceptors.mySqlHelper.conditionHelper.dialect;

import org.springframework.stereotype.Component;

/** 
*
* @author ethan.yuan E-mail: windofdusk@gmail.com
* @createTime 2016年6月13日 下午4:05:07 
* MySql数据库方言 
*
*/
@Component("mysqlDialect")
public class MySQLDialect extends Dialect {

	@Override
	protected String buildPagedSql(String sql, int offset, int limit, int rowcount) {
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect.append(sql);
		pagingSelect.append(" limit " + offset + "," + limit);
		return pagingSelect.toString();
	}

}
