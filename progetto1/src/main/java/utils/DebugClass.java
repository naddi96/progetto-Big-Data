package utils;

import org.apache.log4j.helpers.ISO8601DateFormat;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DebugClass {

    private static int startingDay=0;

    private static String firstDate="2020-03-01T18:00:00".substring(0,10);;
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();


        String format = "yyyy-MM-dd";
        System.out.println(firstDate);
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dat = null;
        try {
            dat = df.parse(firstDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(dat);
        int i=0;
        while(i< 50) {

            System.out.println("settimana" + c.get(Calendar.WEEK_OF_YEAR));
            System.out.println("giorno set" + c.get(Calendar.DAY_OF_WEEK));
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("E",Locale.ITALY); // the day of the week abbreviated

            System.out.println(simpleDateformat.format(c.getTime()));
            simpleDateformat.getCalendar().get(Calendar.DAY_OF_WEEK);
            System.out.println(c.getTime());
            System.out.println("---------------------------------");
            c.add(Calendar.DATE, 1);

            i++;
        }
    }

}
