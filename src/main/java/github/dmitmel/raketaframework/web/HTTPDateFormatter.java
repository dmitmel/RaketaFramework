package github.dmitmel.raketaframework.web;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HTTPDateFormatter {
    public static final String DATE_FORMAT = "dd/MMM/yyyy hh:mm:ss";

    public static String formatCurrentDate() {
        return formatDate(new Date());
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        return dateFormatter.format(date);
    }
}
