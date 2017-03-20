package com.badminton.interceptors.mySqlHelper.conditionHelper.query;

/**
 * @author Ethan
 * @date 上午11:59:23
 * @email windofdusk@gmail.com
 * 类说明
 */
public class ConditionConstant {
	/**
	 * MAPPING文件中定义的key值
	 */
	public static final String WHERE_NATIVE_SQL_KEY = "WHERE_NATIVE_SQL_KEY";
	/**
	 * 原生态ORDER BY SQL
	 */
	public static final String ORDER_NATIVE_SQL_KEY = "ORDER_NATIVE_SQL_KEY";
	/**
	 * 仅用于存储between数据,分割出between xxx1 and xxx2, format=xxx1@VALUE_SPLIT_BETWEEN@xxx2
	 */
	public static final String VALUE_SPLIT_BETWEEN = "@VALUE_SPLIT_BETWEEN@";
	
	public static final String WHERE_PREPEND = " and ";
	
	public static final String ORDER_BY_PREPEND = " ORDER BY ";
	/**
	 * ==
	 */
	public static final String PREFIX_EQ = "@EQ_";
	/**
	 * !=
	 */
	public static final String PREFIX_NOT_EQ = "@NOT_EQ_";
	/**
	 * >
	 */
	public static final String PREFIX_GT = "@GT_";
	/**
	 * >=
	 */
	public static final String PREFIX_GE = "@GE_";
	/**
	 * <
	 */
	public static final String PREFIX_LT = "@LT_";
	/**
	 * <=
	 */
	public static final String PREFIX_LE = "@LE_";
	/**
	 * like %%
	 */
	public static final String PREFIX_LIKE = "@LIKE_";
	/**
	 * in (...)
	 */
	public static final String PREFIX_IN = "@IN_";
	/**
	 * not in (...)
	 */
	public static final String PREFIX_NOT_IN = "@NOT_IN_";
	/**
	 * between
	 */
	public static final String PREFIX_BETWEEN = "@BETWEEN_";
	/**
	 * ORDER BY
	 */
	public static final String PREFIX_ORDER = "@ORDER_BY_";
	/**
	 * ASCEENDING
	 */
	public static final String ORDER_ASC = "ASC";
	/**
	 * DESCENDING
	 */
	public static final String ORDER_DESC = "DESC";
	
	/**
	 * length(...)
	 */
	public static final String PREFIX_LENGTH = "@LENGTH_";
}
