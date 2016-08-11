package org.willthisfly.raketaframework;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HTTPDateFormatter {
    private static final String ABBREVIATED_DAY_OF_WEEK = "EEE";
    private static final String ABBREVIATED_MONTH = "MMM";
    private static final String TWO_DIGIT_DAY_IN_MONTH = "dd";
    private static final String DAY_IN_MONTH = "d";
    private static final String FOUR_DIGIT_YEAR = "yyyy";
    private static final String HOUR_24 = "HH";
    private static final String HOUR_12 = "hh";
    private static final String MINUTE = "mm";
    private static final String SECOND = "ss";
    private static final String AM_OR_PM = "a";
    private static final String ABBREVIATED_TIMEZONE = "z";
    
    private static final String SERVER_LOG_DATE_FORMAT = String.format("%s/%s/%s %s:%s:%s",
            TWO_DIGIT_DAY_IN_MONTH, ABBREVIATED_MONTH, FOUR_DIGIT_YEAR,
            HOUR_24, MINUTE, SECOND);
    
    private static final String GMT_DATE_FORMAT = String.format("%s, %s %s, %s %s:%s:%s %s %s",
            ABBREVIATED_DAY_OF_WEEK, ABBREVIATED_MONTH, DAY_IN_MONTH, FOUR_DIGIT_YEAR,
            HOUR_12, MINUTE, SECOND,
            AM_OR_PM, ABBREVIATED_TIMEZONE);
    
    
    private HTTPDateFormatter() {
        throw new UnsupportedOperationException("Can\'t create instance of HTTPDateFormatter");
    }
    
    
    public static String currentDateInServerLogFormat() {
        return dateInServerLogFormat(new Date());
    }

    public static String dateInServerLogFormat(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(SERVER_LOG_DATE_FORMAT);
        return dateFormatter.format(date);
    }

    public static String currentDateInGMTFormat() {
        return dateInGMTFormat(new Date());
    }

    public static String dateInGMTFormat(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(GMT_DATE_FORMAT);
        return df.format(date);
    }
}
