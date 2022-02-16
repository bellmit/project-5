package com.kindminds.drs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.MONTH;

public class RTDateUtil {

    public static Date conformDateTime(int marketplaceId, String dateStr) {
        if (marketplaceId == 8) {
            int index = dateStr.lastIndexOf(".");
            return convertToDateTime(8, dateStr.substring(0,index) + ":" + dateStr.substring(index+1));
        } else if (marketplaceId == 7) {
            return convertToDateTime(7, dateStr.replaceAll("UTC", "GMT"));
        } else {
            return convertToDateTime(marketplaceId, dateStr);
        }
    }

    private static final String DATETIME_FORMAT_US = "MMM d, yyyy h:mm:ss a z";
    private static final String DATETIME_FORMAT_UK = "d MMM yyyy HH:mm:ss zzz";
    private static final String DATETIME_FORMAT_CA = "yyyy-MM-dd h:mm:ss a z";
    private static final String DATETIME_FORMAT_DE = "dd.MM.yyyy HH:mm:ss zzz";
    private static final String DATETIME_FORMAT_FR = "d MMMM yyyy HH:mm:ss zzz";
    private static final String DATETIME_FORMAT_IT = "dd/MMM/yyyy HH.mm.ss zzz";
    private static final String DATETIME_FORMAT_ES = "dd/MM/yyyy HH:mm:ss zzz";

    public static Date convertToDateTime(int marketplaceId, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_US, Locale.US);
        Date date = null;
        try {
            if (marketplaceId == 4) {
                sdf = new SimpleDateFormat(DATETIME_FORMAT_UK, Locale.UK);
            } else if (marketplaceId == 5) {
                sdf = new SimpleDateFormat(DATETIME_FORMAT_CA, Locale.CANADA);
            } else if (marketplaceId == 6) {
                sdf = new SimpleDateFormat(DATETIME_FORMAT_DE, Locale.GERMANY);
            } else if (marketplaceId == 7) {
                sdf = new SimpleDateFormat(DATETIME_FORMAT_FR, Locale.FRANCE);
            } else if (marketplaceId == 8) {
                sdf = new SimpleDateFormat(DATETIME_FORMAT_IT, Locale.ITALIAN);
            } else if (marketplaceId == 9) {
                sdf = new SimpleDateFormat(DATETIME_FORMAT_ES, new Locale("es", "ES"));
            }
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            System.out.println("exception: " + e);
        }

        return date;
    }

    private static final String DATE_FORMAT_US = "MMM d, yyyy";
    private static final String DATE_FORMAT_UK = "d MMM yyyy";
    private static final String DATE_FORMAT_CA = "yyyy-MM-dd";
    private static final String DATE_FORMAT_CA_REVIEWS = "MMMM d, yyyy";
    private static final String DATE_FORMAT_DE = "dd.MM.yyyy";
    private static final String DATE_FORMAT_DE_REVIEWS = "d. MMMM yyyy";
    private static final String DATE_FORMAT_FR = "d MMMM yyyy";
    private static final String DATE_FORMAT_IT = "dd/MMM/yyyy";
    private static final String DATE_FORMAT_IT_REVIEWS = "d MMMM yyyy";
    private static final String DATE_FORMAT_ES = "dd/MM/yyyy";
    private static final String DATE_FORMAT_ES_REVIEWS = "d 'de' MMMM 'de' yyyy";

    public static Date conformDate(int marketplaceId, String dateStr) {
        if (marketplaceId == 8) {
            int index = dateStr.lastIndexOf(".");
            return convertToDate(8, dateStr.substring(0,index) + ":" + dateStr.substring(index+1));
        } else if (marketplaceId == 7) {
            return convertToDate(7, dateStr.replaceAll("UTC", "GMT"));
        } else {
            return convertToDate(marketplaceId, dateStr);
        }
    }

    private static Date convertToDate(int marketplaceId, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_US, Locale.US);
        Date date = null;
        try {
            if (marketplaceId == 4) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
            } else if (marketplaceId == 1) {
                sdf = new SimpleDateFormat("MMM dd yyyy", Locale.US);
            } else if (marketplaceId == 5) {
                sdf = new SimpleDateFormat("MMM dd yyyy", Locale.CANADA);
            } else if (marketplaceId == 6) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.GERMANY);
            } else if (marketplaceId == 7) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE);
            } else if (marketplaceId == 8) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN);
            } else if (marketplaceId == 9) {
                sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
            }
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            System.out.println("exception: " + e);
        }

        return date;
    }

    public static Date conformDateReviews(int marketplaceId, String dateStr) {
        SimpleDateFormat sdf;
        try {

           String [] ary = dateStr.split(" ");

           String m = ary[ary.length -2].contains(",") == true ?
                   ary[ary.length -2].replace(",","")  :  ary[ary.length -2];
           String d = ary[ary.length -3].contains(".") == true ?
                   ary[ary.length -3].replace(".","")  :  ary[ary.length -3];

           String newDateStr = d + " "  +  m + " " + ary[ary.length -1];

           /*
            if (marketplaceId == 5) {
                sdf = new SimpleDateFormat(DATE_FORMAT_CA_REVIEWS, Locale.CANADA);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 6) {
                sdf = new SimpleDateFormat(DATE_FORMAT_DE_REVIEWS, Locale.GERMANY);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 8) {
                sdf = new SimpleDateFormat(DATE_FORMAT_IT_REVIEWS, Locale.ITALIAN);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 9) {
                sdf = new SimpleDateFormat(DATE_FORMAT_ES_REVIEWS, new Locale("es", "ES"));
                return sdf.parse(newDateStr);
            }
            else {
                return conformDate(marketplaceId, newDateStr);
            }*/

            if (marketplaceId == 4) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 1) {
                sdf = new SimpleDateFormat("MMM dd yyyy", Locale.US);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 5) {
                sdf = new SimpleDateFormat("MMM dd yyyy", Locale.CANADA);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 6) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.GERMANY);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 7) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.FRANCE);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 8) {
                sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN);
                return sdf.parse(newDateStr);
            } else if (marketplaceId == 9) {
                sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
                return sdf.parse(newDateStr);
            } else {
                return conformDate(marketplaceId, newDateStr);
            }



        } catch (ParseException e) {
            System.out.println("exception: " + e);
        }
        return null;
    }

    public  static boolean isBeforeMonths(int months, Date aDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(MONTH, months);
        return aDate.compareTo(calendar.getTime()) < 0;
    }
}