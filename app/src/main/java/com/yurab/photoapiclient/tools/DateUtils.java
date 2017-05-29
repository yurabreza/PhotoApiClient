package com.yurab.photoapiclient.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {


    public static final String DATE_FORMAT_UI = "dd MMMM yyyy";
    public static final String DATE_FORMAT_ISO = "yyyy-MM-dd'T'hh:mm:ss";

    /**
     * Converts a Date object to a string representation.
     *
     * @param date
     * @return date as String
     */
    public static String dateToString(Date date, String dateFormat) {
        if (date == null) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
            return df.format(date);
        }
    }

    /**
     * Converts a string representation of a date to its Date object.
     *
     * @param dateAsString
     * @return Date
     */
    public static Date stringToDate(String dateAsString, String dateFormat) {
        try {
            DateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
            return df.parse(dateAsString);
        } catch (ParseException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
