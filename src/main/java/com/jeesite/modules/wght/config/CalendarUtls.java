package com.jeesite.modules.wght.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtls {
	
	
	/**
	 * 获取当前日期的前一天
	 */
	public static Date getBeforeFirstDay(Date date) {
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		cldr.add(Calendar.DATE, -1);
		return cldr.getTime();
	}

	/**
	 * 获取当前日期的前N天
	 */
	public static Date getBeforeNDay(Date date,int n){
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		cldr.add(Calendar.DATE, -n);
		return cldr.getTime();
	}
	
	/**
	 * 获取当前日期的下个月当天
	 */
	public static Date getNextMonthDay(Date date) {
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		cldr.add(Calendar.MONTH, 1);
		return cldr.getTime();
	}
	
	/**
	 * 获取当前日期的n个月后对应当天
	 */
	public static Date getNextMonthDay(Date date,int n) {
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		cldr.add(Calendar.MONTH, n);
		return cldr.getTime();
	}
	
	/**
	 * 获取当前日期(不含时间的格式)
	 */
	public static Date getDateNoTime(Date date){
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		Date date2 = new Date();
		try {
			date2 = dft.parse(dft.format(cldr.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}
	
	/**
	 * 获取当前日期的月初日期
	 */
	public static Date getFirstMonthDay(Date date) {

		Date date1=getNextMonthDay(date, -1);
		Date date2=getAfterFirstDay(getMaxMonthDay(date1));
		return date2;
	}
	
	/**
	 * 获取当前日期的月末日期
	 */
	public static Date getMaxMonthDay(Date date) {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		cldr.set(Calendar.DAY_OF_MONTH, cldr.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date date2 = new Date();
		try {
			date2 = dft.parse(dft.format(cldr.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}
	
	/**
	 * 获取当前日期的下个月1号
	 */
	public static Date getNextMonthFirstDay(Date date) {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		cldr.add(Calendar.MONTH, 1);
		cldr.set(Calendar.DAY_OF_MONTH, cldr.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date date2 = new Date();
		try {
			date2 = dft.parse(dft.format(cldr.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}
	
	/**
	 * 获取当前日期的后一天
	 */
	public static Date getAfterFirstDay(Date date) {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		int day=cldr.get(Calendar.DATE);
		cldr.set(Calendar.DATE, day+1);
		Date date2 = new Date();
		try {
			date2 = dft.parse(dft.format(cldr.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}
	
	/**
	 * 获取当前日期的后N天
	 */
	public static Date getAfterNDay(Date date,int n){
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(date);
		cldr.add(Calendar.DATE, n);
		return cldr.getTime();
	}

	/**
	 * 判断当前是否是该月的第一天
	 */
	public static boolean isFirstDayOfMonth(Date date) {
		boolean flag = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int today=calendar.get(Calendar.DAY_OF_MONTH);
		if (1 == today) {
			flag = true;
		}
		return flag;
	}
	/**
	 * 获取两个日期之间相距多少个月
	 */
    public static int getBetweenMonths(Date date,Date date2){
    	Integer month=0;
    	Date date1=date;
    	date1=getNextMonthDay(getBeforeFirstDay(date), 1);//获取前一天N个月后当天
    	if(isFirstDayOfMonth(date)){
    		date1=getBeforeFirstDay(getNextMonthDay(date, 1));//获取N个月后当天的前一天
    	}
    	while(date1.compareTo(date2)<=0){
    		date1=getAfterFirstDay(date1);//替换下次开始日期
    		date1=getNextMonthDay(getBeforeFirstDay(date1), 1);//获取前一天N个月后当天
    		if(isFirstDayOfMonth(date1)){
        		date1=getBeforeFirstDay(getNextMonthDay(date1, 1));//获取N个月后当天的前一天
        	}
    		month++;//在结束日期之内说明含一个月
    	}
    	return month;
    }
    
    /**
     * 获取两个日期之间相差多少天（包含结束日期）
     * @param begin_date
     * @param end_date
     */
    public static int getBetweenTwoDays(Date begin_date, Date end_date) {
        long day = 0;
        try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			if(begin_date != null){
			    String begin = sdf.format(begin_date);
			    begin_date  = sdf.parse(begin);
			}
			if(end_date!= null){
			    String end= sdf.format(end_date);
			    end_date= sdf.parse(end);
			}
			day = (end_date.getTime()-begin_date.getTime())/(24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return (int) day+1;
    }
    
    /**
     * 获取两个日期之间相差多少天
     * @param begin_date
     * @param end_date
     */
    public static int getBetweenTwoDays2(Date begin_date, Date end_date) {
        long day = 0;
        try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			if(begin_date != null){
			    String begin = sdf.format(begin_date);
			    begin_date  = sdf.parse(begin);
			}
			if(end_date!= null){
			    String end= sdf.format(end_date);
			    end_date= sdf.parse(end);
			}
			day = (end_date.getTime()-begin_date.getTime())/(24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return (int) day;
    }
    
    /**
     * 获取不满整月时多余的天数
     * @param date
     * @param date2
     * @return
     */
    public static Integer getLeftDays(Date date,Date date2){
    	Integer month=getBetweenMonths(date, date2);
    	Date date11=getNextMonthDay(getBeforeFirstDay(date), month);
    	if(isFirstDayOfMonth(date)){
    		date11=getBeforeFirstDay(getNextMonthDay(date, month));
    	}
    	Integer days=CalendarUtls.getBetweenTwoDays2(date11, date2);
    	return days;
    }
   
}
