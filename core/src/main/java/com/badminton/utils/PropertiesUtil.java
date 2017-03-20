package com.badminton.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * 读取properties文件的工具类
 *
 * @author s.song
 */
public class PropertiesUtil {
	
	private static String site_id = "site_id";
	
	private static Properties p = new Properties();

	/**
	 * 读取properties配置文件信息
	 */
	static{
		try {
			p.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	/**
	 * 根据key得到value的值
	 */
	public static String getValue(String key)
	{
		return p.getProperty(key);
	}
	/**
	 * @desc 获取站点Id
	 * @author zhousg
	 * @date 2016年9月18日下午3:01:10
	 * @return
	 */
	public static int getSiteId(){
		int siteId = Integer.parseInt(getValue(site_id));
		return siteId;
	}
}
