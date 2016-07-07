package github.dmitmel.raketaframework.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HTTPDateFormatter {
    public static final String DATE_FORMAT = "dd/MMM/yyyy hh:mm:ss";

    public static String formatCurrentDate() {
        return formatDate(new Date());
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        return dateFormatter.format(date);
    }

    public static String currentDateInGMT() {
        return dateInGMT(new Date());
    }

    public static String dateInGMT(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }
}
