package com.badminton.utils;

import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ethan
 * @date 下午4:15:11
 * @email windofdusk@gmail.com
 * 类说明
 */
public class StringUtil {
	private StringUtil() {
		super();
	}

	/**
	 * 将byte单位的文件大小格式化为直观易懂的字符描述形式
	 * @return
	 * @author wenping
	 * @since  2011-3-24
	 */
	public static String getFormattedFileSize(Long filesize) {
		String size = "NULL";
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		if (filesize >= 1024d * 1024d * 0.8d) {
			size = df.format(filesize / (1024d * 1024d)) + "MB";
		} else if (filesize >= 1024d * 0.8d) {
			size = df.format(filesize / 1024d) + "KB";
		} else {
			size = filesize + "Bytes";
		}
		return size;
	}

	/**
	 * 将数字类型的字符串数组转换为Long型数组
	 * @param strs
	 * @return
	 * @author wenping
	 * @since  2011-3-24
	 */
	public static Long[] formatToLongs(String[] strs) {
		if (strs == null) {
			return null;
		} else {
			Long[] ret = new Long[strs.length];
			for (int i = 0; i < strs.length; i++) {
				ret[i] = Long.valueOf(strs[i]);
			}
			return ret;
		}
	}
	/**
	 * 对于字符串value中,将其prefix值除去,只取其后值
	 * @param prefix
	 * @param value
	 * @return
	 */
	public static String extractPrefix(String prefix, String value){
		return value.substring(prefix.length());
	}

	/**
	 * 属性名称转换为数据库表列名,并遵循一定的规范
	 * @param propertyName
	 * @return
	 */
	public static String propertyNameToColumnName(String propertyName){

		StringBuffer columnName = new StringBuffer();
		Pattern pattern = Pattern.compile("[A-Z]");
		Matcher matcher = pattern.matcher(propertyName);
		while(matcher.find()){
			matcher.appendReplacement(columnName, "_" + matcher.group());
		}
		matcher.appendTail(columnName);

		return columnName.toString().toUpperCase();
	}

	/**
	 * 实体名称转换为数据库表名,并遵循一定的规范
	 * @param entityName
	 * @return
	 */
	public static String entityNameToTableName(String entityName){

		StringBuffer columnName = new StringBuffer();
		Pattern pattern = Pattern.compile("[A-Z]");
		Matcher matcher = pattern.matcher(entityName);
		while(matcher.find()){
			matcher.appendReplacement(columnName, "_" + matcher.group());
		}
		matcher.appendTail(columnName);
		String tableName = columnName.toString().toUpperCase();
		if(tableName.startsWith("_")){
			return tableName.substring(1, tableName.length());
		}else{
			return entityName.toUpperCase();
		}
	}

	public static String escape(String paramString)
	{
		if (paramString == null) return null;

		StringBuffer localStringBuffer = new StringBuffer();

		for (int i = 0; i < paramString.length(); i++) {
			char c = paramString.charAt(i);
			if (((c >= '0') && (c <= '9')) || ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || (c > 'ÿ')) {
				localStringBuffer.append(c);
			} else {
				String str = Integer.toHexString(c);
				if (str.length() == 1) str = "0" + str;
				localStringBuffer.append("%" + str);
			}
		}
		return localStringBuffer.toString();
	}

	public static String replace(String paramString1, String paramString2, String paramString3)
	{
		if (paramString1 == null) return null;
		if ((paramString2 == null) || (paramString3 == null)) return paramString1;
		StringBuffer localStringBuffer = new StringBuffer();
		int i = paramString2.length();
		int j = 0;
		int k;
		while ((k = paramString1.indexOf(paramString2, j)) >= j) {
			localStringBuffer.append(paramString1.substring(j, k));
			localStringBuffer.append(paramString3);
			j = k + i;
		}
		if (j < paramString1.length()) localStringBuffer.append(paramString1.substring(j));
		return localStringBuffer.toString();
	}

	public static String left(String paramString, int paramInt)
	{
		if (paramString == null) return null;
		if (paramInt <= 0) return "";
		if (paramInt > paramString.length()) paramInt = paramString.length();
		return paramString.substring(0, paramInt);
	}

	public static String right(String paramString, int paramInt)
	{
		if (paramString == null) return null;
		if (paramInt <= 0) return "";
		if (paramInt > paramString.length()) paramInt = paramString.length();
		return paramString.substring(paramString.length() - paramInt, paramString.length());
	}

	public static String mid(String paramString, int paramInt1, int paramInt2)
	{
		if (paramString == null) return null;
		if (paramInt1 <= 0) { paramInt2 += paramInt1; paramInt1 = 0; }
		if (paramInt1 >= paramString.length()) return "";
		if (paramInt2 <= 0) return "";
		if (paramInt2 > paramString.length() - paramInt1) paramInt2 = paramString.length() - paramInt1;
		return paramString.substring(paramInt1, paramInt1 + paramInt2);
	}

	public static String makeString(String paramString, int paramInt)
	{
		if (paramString == null) return null;
		if (paramInt <= 0) return "";
		StringBuffer localStringBuffer = new StringBuffer(paramInt);
		int i = paramInt / paramString.length();
		int j = paramInt % paramString.length();

		for (int k = 0; k < i; k++) localStringBuffer = localStringBuffer.append(paramString);
		localStringBuffer.append(paramString.substring(0, j));
		return localStringBuffer.toString();
	}

	public static String lpad(String paramString1, int paramInt, String paramString2)
	{
		return makeString(paramString2, paramInt - paramString1.length()) + paramString1;
	}

	public static String rpad(String paramString1, int paramInt, String paramString2)
	{
		return paramString1 + makeString(paramString2, paramInt - paramString1.length());
	}

	public static String nullTo(String paramString1, String paramString2)
	{
		return paramString1 == null ? paramString2 : paramString1;
	}

	public static String nullTo(String paramString)
	{
		return paramString == null ? "" : paramString;
	}

	public static String emptyTo(String paramString1, String paramString2)
	{
		return (paramString1 == null) || (paramString1.equals("")) ? paramString2 : paramString1;
	}

	public static String emptyTo(String paramString)
	{
		return paramString == null ? "" : paramString;
	}

	public static String convert(String paramString1, String paramString2, String paramString3)
	{
		try {
			return new String(paramString1.getBytes(paramString2), paramString3);
		} catch (Exception localException) {
		}
		return null;
	}

	public static String decode(String paramString)
	{
		return convert(paramString, "UTF-8", "ISO8859-1");
	}

	public static String encode(String paramString)
	{
		return convert(paramString, "ISO8859-1", "UTF-8");
	}

	public static String text2html(String paramString)
	{
		paramString = replace(replace(paramString, "<", "&lt;"), ">", "&gt;");

		paramString = replace(replace(paramString, " ", "&nbsp;"), "\r\n", "<br>");

		paramString = replace(replace(paramString, "'", "&#39;"), "\"", "&quot;");
		return paramString;
	}

	public static String join(String[] paramArrayOfString, String paramString)
	{
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < paramArrayOfString.length; i++) {
			localStringBuffer.append(paramArrayOfString[i]);
			if ((i != paramArrayOfString.length - 1) && (paramString != null)) {
				localStringBuffer.append(paramString);
			}
		}
		return localStringBuffer.toString();
	}

	public static String join(String[] paramArrayOfString)
	{
		return join(paramArrayOfString, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] split(String paramString1, String paramString2)
	{
		Vector localVector = new Vector();
		if ((paramString1 == null) || (paramString1.equals("")) || (paramString2 == null) || (paramString2.equals(""))) return null;
		String str = new String(paramString1);
		int i = paramString2.length();
		int j = str.indexOf(paramString2);
		while (j != -1)
		{
			if (j > 0)
			{
				localVector.add(str.substring(0, j));
			}
			str = str.substring(j + i, str.length());
			j = str.indexOf(paramString2);
		}
		if (str.length() > 0) localVector.add(str);
		String[] arrayOfString = new String[localVector.size()];
		int k = 0;
		for (Enumeration localEnumeration = localVector.elements(); localEnumeration.hasMoreElements(); k++)
		{
			arrayOfString[k] = ((String)localEnumeration.nextElement());
		}
		return arrayOfString;
	}

	public static String getRandomString(int paramInt)
	{
		return getRandomString("abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ", paramInt);
	}

	public static String getRandomString(int paramInt, String paramString)
	{
		return getRandomString(paramString, paramInt);
	}

	public static String getRandomString(String paramString, int paramInt)
	{
		if (paramInt <= 0) return null;
		Date localDate = new Date();
		char[] arrayOfChar = new char[paramInt];
		String str = null;
		long l = (long)(Math.random() * localDate.getTime());
		Random localRandom = new Random(localDate.getTime() + l);
		for (int i = 0; i < paramInt; i++)
		{
			int j = (int)(localRandom.nextDouble() * paramString.length());
			arrayOfChar[i] = paramString.charAt(j);
		}
		str = new String(arrayOfChar);
		return str;
	}

	public static String webEncode(String paramString)
	{
		return paramString == null ? "" : convert(paramString, "ISO8859-1", "UTF-8");
	}

	public static String webEncode(String paramString1, String paramString2)
	{
		return paramString1 == null ? paramString2 : convert(paramString1, "ISO8859-1", "UTF-8");
	}

	public static String jspEncode(String paramString)
	{
		return paramString == null ? "" : paramString;
	}

	public static String jspEncode(String paramString1, String paramString2)
	{
		return paramString1 == null ? paramString2 : paramString1;
	}

	public static String jspDecode(String paramString)
	{
		return paramString == null ? "" : convert(paramString, "ISO8859-1", "UTF-8");
	}

	public static String jspDecode(String paramString1, String paramString2)
	{
		return paramString1 == null ? paramString2 : convert(paramString1, "ISO8859-1", "UTF-8");
	}
	/**
	 * 转换查询条件中用到的通配符为\_或\%
	 * @param source
	 * @return
	 */
	public static final String parseRegex(String source){
		if(NullUtil.isNull(source)){
			return null;
		}
		String dest = source.replace("_", "\\_");
		dest = dest.replace("%", "\\%");
		return dest;
	}

	/**
	 * 将为null的对象转为"",去空格
	 *@author 周明
	 *@DATE   2013-4-3 下午2:01:02
	 *@param objCs
	 *@return
	 */
	public static String formatTrimString(Object objCs) {
		if (objCs == null) {
			return "";
		}
		String strObj = objCs.toString();
		if (strObj.length() == 0) {
			return strObj;
		}
		return strObj.trim();
	}

	public static void main(String[] args){
		//    	System.out.println(extractPrefix("EQ_", "EQ_HELLO"));
		System.out.println(StringUtil.entityNameToTableName("ChinaNet"));
	}
}
