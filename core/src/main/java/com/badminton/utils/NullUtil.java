/**
 * @author Ethan
 */
package com.badminton.utils;

/**
 * @author Ethan
 * @date 下午7:00:26
 * @email windofdusk@gmail.com
 * 类说明
 */
public class NullUtil {
	/**
	 * 判断是否为NULL
	 * 
	 * @param obj
	 * @return
	 */
	public static final boolean isNull(Object obj) {
		if (null == obj) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为Not NULL
	 * 
	 * @param obj
	 * @return
	 */
	public static final boolean isNotNull(Object obj) {
		if (null != obj) {
			return true;
		} else {
			return false;
		}
	}

	public static final boolean isEmpty(Object obj) {
		if (null == obj || "".equals(obj)) {
			return true;
		} else {
			return false;
		}
	}

	public static final boolean isNotEmpty(Object obj) {
		if (null != obj && !"".equals(obj)) {
			return true;
		} else {
			return false;
		}
	}
}
