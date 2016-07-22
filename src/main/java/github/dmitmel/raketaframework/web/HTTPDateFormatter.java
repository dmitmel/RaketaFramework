package github.dmitmel.raketaframework.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HTTPDateFormatter {
    /**
     * Request summary date format string. Description for formatting pattern:
     *
     * <table>
     *     <tr><th>Tag</th>  <th>Description</th></tr>
     *     <tr><td>dd</td>   <td>number of day in month</td></tr>
     *     <tr><td>MMM</td>  <td>abbreviated name of month</td></tr>
     *     <tr><td>yyyy</td> <td>4-digit number of year</td></tr>
     *     <tr><td>HH</td>   <td>hour, 24-hour format</td></tr>
     *     <tr><td>mm</td>   <td>minute</td></tr>
     *     <tr><td>ss</td>   <td>second</td></tr>
     * </table>
     */
    public static final String REQUEST_SUMMARY_DATE_FORMAT = "dd/MMM/yyyy HH:mm:ss";
    /**
     * GTM date format string. Description for formatting pattern:
     *
     * <table>
     *     <tr><th>Tag</th>  <th>Description</th></tr>
     *     <tr><td>EEE</td>  <td>abbreviated name of weekday</td></tr>
     *     <tr><td>MMM</td>  <td>abbreviated name of month</td></tr>
     *     <tr><td>d</td>    <td>number of day in month</td></tr>
     *     <tr><td>yyyy</td> <td>4-digit number of year</td></tr>
     *     <tr><td>hh</td>   <td>hour, 12-hour format</td></tr>
     *     <tr><td>mm</td>   <td>minute</td></tr>
     *     <tr><td>ss</td>   <td>second</td></tr>
     *     <tr><td>a</td>    <td>PM or AM</td></tr>
     *     <tr><td>z</td>    <td>abbreviated name of time-zone.</td></tr>
     * </table>
     */
    public static final String GMT_DATE_FORMAT = "EEE, MMM d, yyyy hh:mm:ss a z";
    
    public static String currentDateInRequestSummaryFormat() {
        return dateInRequestSummaryFormat(new Date());
    }

    public static String dateInRequestSummaryFormat(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(REQUEST_SUMMARY_DATE_FORMAT);
        return dateFormatter.format(date);
    }

    public static String currentDateInGMTFormat() {
        return dateInGMTFormat(new Date());
    }

    public static String dateInGMTFormat(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(GMT_DATE_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }
}
