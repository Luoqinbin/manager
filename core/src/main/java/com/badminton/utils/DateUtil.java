package com.badminton.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间格式转换
 * 
 * @author HUSL
 * @date 2011-12-8
 * 
 * @note 时间帮助类
 */
public class DateUtil {

    /**
     * @description 获得当前的时间，yyyy-MM-dd
     * @return string类型的当前时间，
     *         pattern yyyy-MM-dd
     */
    public static String getCurrentStringDate() {
        return date2String(new Date());
    }

    /**
     * @description 将date转换成 yyyy-MM-dd时间
     * @param date
     * @return 将date转化为yyyy-MM-dd形式的时间
     */
    public static String date2String(Date date) {
        return date2String(date, "yyyy-MM-dd");
    }

    /**
     * @description 将Date格式的日期按照 format格式进行转换
     * @param date
     * @param format
     * @return
     */
    public static String date2String(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * @description 将Date格式的日期按照 format格式进行转换
     * @param date
     * @param format
     * @return
     */
    public static String date2String(int time, String format) {
        long l = Long.valueOf(time) * 1000;
        Date date = new Date(l);

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 将yyyyMMddhhmmss类型时间转换成 10位时间（距1970-1-1的秒级数据）
     * 
     * @param dateString
     *        默认pattern yyyyMMddhhmmss
     * @return
     * @throws ParseException
     */
    public static int stringDate2Int(String dateString) throws ParseException {
        return stringDate2Int(dateString, "yyyyMMddHHmmss");
    }

    /**
     * 将String类型时间转换成 10位时间（距1970-1-1的秒级数据）
     * 
     * @param dateString
     * @param pattern - 时间字符串格式
     * @return 10位时间，单位秒
     * @throws ParseException
     */
    public static int stringDate2Int(String dateString, String datePattern) throws ParseException {
        if (dateString == null || dateString.equals("")) {
            return 0;
        }

        Date date = string2Date(dateString, datePattern);
        return getTime(date);
    }

    /**
     * 将date形的转换成long型,即距离1970-1-1的时间数，精确至秒
     * 
     * @param date
     * @return
     */
    public static int getTime(Date date) {
        if (date == null) {
            return 0;
        }

        long time = date.getTime();
        //return (int) Arith.round(time / 1000, 0);
        return (int) (time / 1000);
    }

    /**
     * 将long型时间转换成int型时间，取消毫秒级
     * 
     * @param date
     * @return
     */
    public static int getTime(long date) {
        long time = date;
        //return (int) Arith.round(time / 1000, 0);
        return (int) (time / 1000);
    }

    /**
     * 获得当前10位的时间
     * 
     * @description 获取系统时间距1970-1-1秒级数据
     */
    public static int getTimeMillis() {
        long time = System.currentTimeMillis();
        //return (int) Arith.round(time / 1000, 0);
        return (int) (time / 1000);
    }

    /**
     * 获得当前系统毫秒时间
     * 
     * @description 获取系统时间距1970-1-1秒级数据
     */
    public static long getTimemillisecond() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * @description 将long形的转换成date型
     * @param dateTime
     * @return Date
     */
    public static Date long2Date(long dateTime) {
        if (Long.valueOf(dateTime).toString().length() <= 10) {
            dateTime *= 1000;
        }
        return new Date(dateTime);
    }

    /**
     * @description 将date形的转换成long型
     * @param date 时间
     * @param length 返回long型的长度
     * @return long
     */
    public static long date2Long(Date date, int length) {
        long dateTime = date.getTime();
        if (length == 10) {
            //dateTime = (long) Arith.round(dateTime / 1000, 0);
            dateTime = dateTime / 1000;
        }
        return dateTime;
    }

    /**
     * @description 将date形的转换成int型
     * @param date 时间
     * @param length 返回long型的长度
     * @return long
     */
    public static int date2Int(Date date) {
        long dateTime = date.getTime();
        //dateTime = (long) Arith.round(dateTime / 1000, 0);
        //return (int) dateTime;
        return (int) (dateTime / 1000);
    }

    /**
     * @description 将字符串时间格式的时间转换成Date类型 转换失败返回 null
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(date);
    }

    /**
     * @description 将字符串格式的日期按照 yyyy-MM-dd 格式进行转换，成为Date类型
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String date) throws ParseException {
        return string2Date(date, "yyyy-MM-dd");
    }

    /**
     * 将yyyy-MM-dd日期转换为yyyy年MM月dd日
     * 
     * @param date
     * @return
     */
    public static String date2CStr(String dateStr) {
        return dateStr.substring(0, 4) + "年" + dateStr.substring(5, 7) + "月" + dateStr.substring(8, 10) + "日";
    }

    /**
     * 通过日期判断是星期几
     * 
     * @param data
     * @author 李军
     * @throws ParseException
     */
    public static String weekDay(Date date) throws ParseException {
        SimpleDateFormat formatD = new SimpleDateFormat("E");
        // "E"表示"day in week"
        String week = formatD.format(date);
        // 将日期中的day of week打印
        return week;
    }

    /**
     * @param 13位毫秒数据
     *        将时间转换成 刚刚、..秒前 等<br/>
     *        5秒内：刚刚
     *        5-60秒：秒前
     *        60-3600秒：分钟前
     *        3600-86400秒：小时前
     *        <431999秒：天前
     *        >431999秒：yyyy-MM-dd
     */
    public static String getCreateTime(int time) {
        int diff = getTimeMillis() - time;
        if (diff <= 5) {
            return "刚刚";
        } else if (diff > 5 && diff < 60) {
            return diff + "秒前";
        } else if (diff >= 60 && diff < 3600) {
            return (diff / 60) + "分钟前";
        } else if (diff >= 3600 && diff < 86400) {
            return (diff / 3600) + "小时前";
        } else if (diff < 431999) {
            return (diff / 86400) + "天前";
        } else {
            long l = Long.valueOf(time) * 1000;
            return date2String(new Date(l));
        }
    }

    /**
     * 将一定格式的时间字符串转换成毫米数
     * 
     * @param time 需要转换的时间字符串
     * @param formatStr 时间格式字符串
     * @return 毫秒数
     */
    public static long convertTimeToLong(String time, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        Date dt = null;
        try {
            dt = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dt.getTime();
    }

    /**
     * 将毫秒时间格式转换成字符串格式
     * 
     * @param date long型毫秒数
     * @param formatStr 欲转换成的时间格式
     * @return 返回一定格式的时间字符串
     */
    public static String convertLongToTime(long date, String formatStr) {
        Date dt = new Date(date);

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String str = sdf.format(dt);
        return str;
    }

    /**
     * 
     * @creater SSJ
     * @date 2013-8-24
     * @desc 把时间（单位毫秒）转换为 hh:mm:ss的形式，例如 61000毫秒 转换结果： 00:01:01
     * @param time
     * @return
     */
    public static String formateTime(long time) {
        if (time <= 0)
            return "N/A";

        int tm = (int) (time / 1000L);
        int s = tm % 60;
        int m = (tm - s) / 60;
        int h = m / 60;
        m = m - h * 60;

        return addZero(h) + ":" + addZero(m) + ":" + addZero(s);
    }

    private static String addZero(int number) {
        if (number < 10)
            return "0" + number;

        return number + "";
    }

    /********************* 月、年帮助 **********************************/

    /**
     * Date转为Calendar格式
     * 
     * @param date
     * @return
     */
    public static Calendar date2Calendar(Date date) {
        String dateString = date2String(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(dateString.substring(0, 4)), Integer.valueOf(dateString.substring(5, 7)) - 1, Integer
            .valueOf(dateString.substring(8, 10)));
        return calendar;
    }

    /**
     * 功能：得到当前月份月初 格式为：xxxx-yy-01 (eg: 2012-02-01)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     */
    public static String firstDayCurMouth(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH) + 1;
        String strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        return x + "-" + strY + "-01";
    }

    /**
     * 功能：得到当前月份月底 格式为：xxxx-yy-31 xxxx-yy-30 xxxx-02-28 xxxx-02-29 (eg:
     * 2007-12-31)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     **/
    public static String lastDayCurMouth(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH) + 1;

        String strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        String strZ = String.valueOf(dayInMonth(x, y));

        return x + "-" + strY + "-" + strZ;
    }

    /**
     * 功能：得到上个月份月初 格式为：xxxx-yy-01 (eg: 2012-02-01)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     */
    public static String firstDayPrevMouth(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH);
        if (y == 0) {
            y = 12;
            x--;
        }
        //y的取值0-11，如果要用y显示月份需要+1
        String strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        return x + "-" + strY + "-01";
    }

    /**
     * 功能：得到上个月份月底 格式为：xxxx-yy-31 xxxx-yy-30 xxxx-02-28 xxxx-02-29 (eg:
     * 2007-12-31)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     **/
    public static String lastDayPrevMouth(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH);
        if (y == 0) {
            y = 12;
            x--;
        }

        String strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        String strZ = String.valueOf(dayInMonth(x, y));

        return x + "-" + strY + "-" + strZ;
    }

    /**
     * 功能：得到下月月份月初 格式为：xxxx-yy-01 (eg: 2012-02-01)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     */
    public static String firstDayNextMouth(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH);
        y += 2;
        if (y == 13) {
            y = 1;
            x++;
        }

        String strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        return x + "-" + strY + "-01";
    }

    /**
     * 功能：得到下月月份月底 格式为：xxxx-yy-31 xxxx-yy-30 xxxx-02-28 xxxx-02-29 (eg:
     * 2007-12-31)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     **/
    public static String lastDayNextMouth(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH);
        y += 2;
        if (y == 13) {
            y = 1;
            x++;
        }

        String strY = y >= 10 ? String.valueOf(y) : ("0" + y);
        String strZ = String.valueOf(dayInMonth(x, y));

        return x + "-" + strY + "-" + strZ;
    }

    /**
     * 功能：得到当前季度季初 格式为：xxxx-01-01, xxxx-04-01, xxxx-07-01, xxxx-10-01 (eg:
     * 2007-10-01)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     */
    public static String firstDayCurSeasion(Date date) {
        Calendar calendar = date2Calendar(date);
        String dateString = "";
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH) + 1;
        if (y >= 1 && y <= 3) {
            dateString = x + "-" + "01" + "-" + "01";
        }
        if (y >= 4 && y <= 6) {
            dateString = x + "-" + "04" + "-" + "01";
        }
        if (y >= 7 && y <= 9) {
            dateString = x + "-" + "07" + "-" + "01";
        }
        if (y >= 10 && y <= 12) {
            dateString = x + "-" + "10" + "-" + "01";
        }
        return dateString;
    }

    /**
     * 功能：得到当前季度季末 格式为：xxxx-03-31, xxxx-06-30, xxxx-09-30, xxxx-12-31 (eg:
     * 2007-12-31)
     * 
     * @param date Date形式
     * @return String
     * @author pure
     */
    public static String lastDayCurSeasion(Date date) {
        Calendar calendar = date2Calendar(date);
        String dateString = "";
        int x = calendar.get(Calendar.YEAR);
        int y = calendar.get(Calendar.MONTH) + 1;
        if (y >= 1 && y <= 3) {
            dateString = x + "-" + "03" + "-" + "31";
        }
        if (y >= 4 && y <= 6) {
            dateString = x + "-" + "06" + "-" + "30";
        }
        if (y >= 7 && y <= 9) {
            dateString = x + "-" + "09" + "-" + "30";
        }
        if (y >= 10 && y <= 12) {
            dateString = x + "-" + "12" + "-" + "31";
        }
        return dateString;
    }

    /**
     * 功能：得到当前年份年初 格式为：xxxx-01-01 (eg: 2007-01-01)
     * 
     * @param date String形式
     * @return String
     */
    public static String firstDayCurYear(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        return x + "-01" + "-01";
    }

    /**
     * 功能：得到当前年份年底 格式为：xxxx-12-31 (eg: 2007-12-31)<br>
     * 
     * @param date Date形式
     * @return String
     */
    public static String lastDayCurYear(Date date) {
        Calendar calendar = date2Calendar(date);
        int x = calendar.get(Calendar.YEAR);
        return x + "-12" + "-31";
    }

    /**
     * 得到某年某月的天数
     * 
     * @param year 年数
     * @param mouth 月数
     * @return maxDate 天数
     * @author 李军
     */
    public static int dayInMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        //必须加上号数。并且号数不能为31,30,29。否在获得上月或者下月月末号数的时候会出现问题。
        cal.set(Calendar.DATE, 15);
        int maxDate = cal.getActualMaximum(Calendar.DATE);
        return maxDate;
    }

    /**
     * 将int型转换成date型
     * 
     * @param iDateTime 整型时间格式
     * @return date型时间格式
     * 
     * */
    public static Date int2Date(int iDateTime) {
        long lDateTime = Long.valueOf(iDateTime);
        return DateUtil.long2Date(lDateTime);
    }

    /**
     * @description 将long形的转换成date型
     * @param dateTime
     * @return Date
     */
    public static Date long2Date(Long dateTime) {

        if (dateTime.toString().length() <= 10) {

            dateTime = dateTime * 1000;
        }
        return new Date(dateTime);
    }

    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 获取年月.
     * 
     * @author XUJUN
     * @date 2013-2-22
     * @return 获取年月
     */
    public static String getYearMonth() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    /**
     * @Description 取得指定小时数后的时间
     * @param date
     *            基准时间
     * @param hourAmount
     *            指定小时数，允许为负数
     * @return 指定小时数后的时间
     */
    public static Date addHour(Date date, int hourAmount) {
        if (date == null) {
            return null;
        }
        return add(date,Calendar.HOUR, hourAmount);
    }

    /**
     * 获取下月：返回格式 yyyy-MM
     * 
     * @author XUJUN
     * @date 2013-2-26
     * @param ym :参数格式为yyyy-MM
     * @return
     */
    public static String getNextMonthFirstDay(String ym) {
        String[] strs = ym.split("-");
        int year = Integer.valueOf(strs[0]);
        int month = Integer.valueOf(strs[1]);
        if (month == 12) {
            year = year + 1;
            month = 1;
        } else {
            month = month + 1;
        }
        String strm = month >= 10 ? String.valueOf(month) : ("0" + month);
        String reStr = year + "-" + strm;
        return reStr;
    }

    /**
     * 根据周获取日期.
     * 
     * @author HC
     * @date Apr 22, 2013
     * @param num 0：本周 1：下周 以此类推
     * @param week 周几，对应Calendar中的常量，如周一-Calendar.MONDAY
     * @return 日期
     */
    public static Date getWeek(int num, int week) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, num * 7);
        //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTime();
    }

    /**
     * 数字转周.
     * 
     * @author HC
     * @date Apr 22, 2013
     * @param value 数字，如1对应周一
     * @return 周数对应数值
     */
    public static Integer parse2CalWeek(int value) {
        switch (value + 1) {
        case Calendar.MONDAY:
            return Calendar.MONDAY;
        case Calendar.TUESDAY:
            return Calendar.TUESDAY;
        case Calendar.WEDNESDAY:
            return Calendar.WEDNESDAY;
        case Calendar.THURSDAY:
            return Calendar.THURSDAY;
        case Calendar.FRIDAY:
            return Calendar.FRIDAY;
        case Calendar.SATURDAY:
            return Calendar.MONDAY;
        case 8:
            return Calendar.SUNDAY;
        default:
            return null;
        }
    }

    /**
     * 计算某个年月，隔几个月后是多少年月，Date“日期 ,是从下个月开始加的 .
     * 如：2014-05 加 3月 就是 2014-08月.
     * 
     * @author XUJUN
     * @date 2014-8-13
     * @param date
     * @param addmonth 累加几月或减少几月
     * @param formate 返回年月格式：yyyy/MM yyyyMM yyyy-MM
     * @return
     */
    public static String findAddMothAfteryearMonth(Date date, int addmonth, String formate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addmonth);
        return DateUtil.date2String(calendar.getTime(), formate);
    }

    /**
     * 计算某个年月，隔几个月后是多少年月日，返回int时间
     * 
     * @param addmonth 添加月或减少月
     * @return int
     */
    public static int findAddMothAfteryearMonthLastDay(Date date, int addmonth) {
        Calendar cale = Calendar.getInstance();
        if (date != null) {
            cale.setTime(date);
        }
        cale.add(Calendar.MONTH, +addmonth);
        cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
        return DateUtil.date2Int(cale.getTime());
    }

    /**
     * @desc 获取某天的开始和结束时间.
     * @author HC
     * @date Apr 8, 2014
     * @param date
     * @return
     */
    public static int[] getTimeByDay(Date date) {
        int[] time = new int[2];
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        time[0] = DateUtil.date2Int(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        time[1] = DateUtil.date2Int(cal.getTime());
        return time;
    }
    
    /**
     * @desc 获取某月的开始和结束时间.
     * @author HC
     * @date Apr 8, 2014
     * @param date
     * @return
     */
    public static int[] getMinuteByNowMonth(Date date) {
        int[] time = new int[2];
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        time[0] = DateUtil.date2Int(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        time[1] = DateUtil.date2Int(cal.getTime());
        return time;
    }
    /**
     * @desc 获得2个时间 相差的 秒数
     * @author zhousg
     * @date 2016年7月18日下午4:47:50
     * @parameter 
     * @param minDate
     * @param maxDate
     * @return
     */
    public static int getDifferSecond(Date minDate,Date maxDate){
    	return getTime(maxDate)-getTime(minDate);
    }
    /**
     * @desc  获取2个时间相差的天数
     * @author zhousg
     * @date 2016年7月26日下午2:57:41
     * @parameter 
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 时间取整
     * @param time
     * @return
     */
    public static String getInitialTime(String time){
        String hour="00";//小时
        String minutes="00";//分钟
        String outTime="00:00";
        StringTokenizer st = new StringTokenizer(time, ":");
        List<String> inTime = new ArrayList<String>();
        while (st.hasMoreElements()) {
            inTime.add(st.nextToken());
        }
        hour=inTime.get(0).toString();
        minutes=inTime.get(1).toString();
        /*if(Integer.parseInt(minutes)>30){
            hour=(Integer.parseInt(hour)+1)+"";
        }*/
        outTime=hour+":00";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            outTime=sdf.format(sdf.parse(outTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outTime;
    }
}
