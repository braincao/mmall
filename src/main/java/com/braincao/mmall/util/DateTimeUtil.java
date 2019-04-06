package com.braincao.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * createTime、updateTime在数据库中是毫秒数，为了便于阅读，创建一个工具类DateTimeUtil
 * 利用joda-time来转换: str->Date;  Date->str
 */
public class DateTimeUtil {

    //通常用这个标准格式
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //str->Date
    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(DateTimeUtil.STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormat.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    //Date->str
    public static String dateToStr(Date date){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DateTimeUtil.STANDARD_FORMAT);
    }

//    //测试
//    public static void main(String[] args) {
//        System.out.println(DateTimeUtil.strToDate("2019-04-06 19:49:07"));
//        System.out.println(DateTimeUtil.dateToStr(new Date()));
//    }

}
