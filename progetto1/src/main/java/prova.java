import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class prova {

    private static int startingDay=20;

    private static String firstDate="1/22/20";
    public static void main(String[] args) {


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
        c.add(Calendar.DATE, startingDay);
        int i=300;
        while(i!=0){

            c.add(Calendar.DATE, 7);
            int annoSet=Integer.valueOf(String.valueOf(c.get(Calendar.YEAR))+
                    String.valueOf(c.get(Calendar.WEEK_OF_YEAR)));

            i--;
        }


    }

}