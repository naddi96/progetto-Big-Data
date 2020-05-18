package utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CovidIta implements Serializable {

    private int positive;
    private String date;
    private int day;
    private int week;
    private int tampons;


    public CovidIta(String date, String positive, String tampons) {

        this.tampons =Integer.parseInt(tampons);
        this.positive = Integer.parseInt(positive);


        String input = date.substring(0,10);
        this.date =input;
        String format = "yyyy-MM-dd";

        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dat = null;
        try {
                dat = df.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar cal = Calendar.getInstance();

        cal.setTime(dat);
        this.week = Integer.parseInt(String.valueOf(cal.get(Calendar.YEAR)) +String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));
        this.day= cal.get(Calendar.DAY_OF_WEEK);

        if (!(this.day == 1 || this.day == 7)) {
            this.tampons=-1;
            this.positive=-1;
            this.week=-1;
            this.day=-1;

        }

    }



    public int getTampons() {
        return tampons;
    }

    public int getPositive() {
        return positive;
    }

    public String getDate() {
        return date;
    }
    public int getDay(){return day;}

    public int getWeek() {return week;}

    @Override
    public String toString() {
        return date.toString()+" "+day+" "+week;
    }
}