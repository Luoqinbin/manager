package com.badminton.interceptors.mySqlHelper.conditionHelper.query;

/**
 * @author Ethan
 * @date 下午11:00:34
 * @email windofdusk@gmail.com
 * 类说明
 */
public class SQLKey {
	/**JAVA自定义条件KEY*/
	public static final String CONDITION_FILTER = "CONDITION_FILTER";
	/**自定义order_filter*/
	public static final String ORDER_FILTER = "ORDER_FILTER";
	/**基类命名空间*/
	public static final String NAMESPACE_BASE = "Base.";
	
	/**mybatis内置的SQL KEYs*/
	public static final String INSERT_SQL = "_insertSql_";
	public static final String UPDATE_SQL = "_updateSql_";
	public static final String DELETE_SQL = "_deleteSql_";
	public static final String SELECT_SQL = "_selectSql_";
	
	/** DB操作SQL_MAP_ID常量定义 */
	public static final String PRE_INSERT = "insert";
	public static final String PRE_UPDATE_SELECTIVE_BY_PK = "updateByPrimaryKeySelective";
	public static final String PRE_UPDATE_BY_PK = "updateByPrimaryKey";
	public static final String PRE_DELETE_BY_PK = "deleteByPrimaryKey";
	public static final String PRE_FIND_BY_PARAMS = "findByParams";
	public static final String PRE_DELETE_BY_PARAMS = "deleteByParams";
}
