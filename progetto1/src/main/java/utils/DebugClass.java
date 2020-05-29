package utils;

import org.apache.log4j.helpers.ISO8601DateFormat;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DebugClass {

    private static int startingDay=0;

    private static String firstDate="2020-02-24";
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();


        String format = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dat = null;
        try {
            dat = df.parse(firstDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("AAAAAAAAAAAAAAAAAa");

        Calendar c = Calendar.getInstance();
        c.setTime(dat);
        int i=0;
        while(i< 50) {

            System.out.println("settimana" + c.get(Calendar.WEEK_OF_YEAR));
            System.out.println("giorno set" + c.get(Calendar.DAY_OF_WEEK));
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("E",Locale.US); // the day of the week abbreviated

            System.out.println(simpleDateformat.format(c.getTime()));
            System.out.println("---------------------------------");
            c.add(Calendar.DATE, 1);
            i++;
        }
    }

}
