package utils;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DebugClass {

    private static int startingDay=0;

    private static String firstDate="1/22/20";
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();


        String format = "MM/dd/yy";
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dat = null;
        try {
            dat = df.parse(firstDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dat);
        System.out.println(c.get(Calendar.DAY_OF_YEAR));
        c.add(Calendar.DATE, -startingDay);
        System.out.println(c.get(Calendar.DAY_OF_YEAR));
        int i=300;
        while(i!=0){

            c.add(Calendar.DATE, 7);
            int annoSet=Integer.valueOf(String.valueOf(c.get(Calendar.YEAR))+
                    String.valueOf(c.get(Calendar.WEEK_OF_YEAR)));

            i--;
        }


        long endTime = System.nanoTime();
        long durationSec = ((endTime - startTime)/ 1000000000);
        long durationNanoSec=(endTime - startTime);


        System.out.println("Seconds:"+durationSec );
        System.out.println("NanoSeconds:"+durationNanoSec );


    }

}
