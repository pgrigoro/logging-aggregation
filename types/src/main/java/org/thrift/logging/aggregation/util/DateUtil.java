package org.thrift.logging.aggregation.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class DateUtil {

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String SHORT_DATE_FORMAT = "yyyyMMdd";

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Parses a date/time string into a Date.
     * @param dateValue the formatted date value.
     * @param format the time format 
     * @param timeZone the given new time zone 
     *
     * @return the formatted date as string.
     */
    public static Date parse(String dateValue, String format, TimeZone timeZone) {
        if (StringUtils.isEmpty(dateValue) || timeZone == null || StringUtils.isEmpty(format)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);
        try {
            return sdf.parse(dateValue);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Parses a date/time string into a Date in UTC time zone.
     * @param dateValue the formatted date value.
     * @param format the time format 
     *
     * @return the formatted date as string.
     */    
    public static Date parseUTC(String dateValue, String format) {
        return parse(dateValue, format, TimeZone.getTimeZone("UTC"));
    }
    
    /**
     * Formats a Date into a date/time string.
     * @param date the time value to be formatted into a time string.
     * @param format the time format 
     * @param timeZone the given new time zone 
     *               
     * @return the formatted date as string.
     */	
	public static String format(Date date, String format, TimeZone timeZone) {
		if (date == null || timeZone == null || StringUtils.isEmpty(format)) {
		    return null;
        }
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}

    /**
     * Formats a Date into a date/time string in UTC time zone.
     * @param date the time value to be formatted into a time string.
     * @param format the time format 
     *
     * @return the formatted date as string.
     */
    public static String formatUTC(Date date, String format) {
        return format(date, format, TimeZone.getTimeZone("UTC"));
    }
    
}