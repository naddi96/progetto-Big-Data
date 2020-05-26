package covidSerilizer;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CovidIta implements Serializable {

    private int positive;
    private int year;
    private int week;
    private int tampons;


    public CovidIta(String date, String positive, String tampons, int startingDay) {

        this.tampons =Integer.parseInt(tampons);
        this.positive = Integer.parseInt(positive);


        String input = date.substring(0,10);

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
        cal.add(Calendar.DATE,startingDay);   //calcolo la settimana in base al giorno della settimana di partenza

        int day= cal.get(Calendar.DAY_OF_WEEK);
        this.year=cal.get(Calendar.YEAR);
        this.week=cal.get(Calendar.WEEK_OF_YEAR);
        if (!(day == 1 || day == 7)) {
            this.tampons=-1;
            this.positive=-1;
            this.week=-1;

        }

    }



    public int getTampons() {
        return tampons;
    }

    public int getPositive() {
        return positive;
    }

    public int getYear() {
        return year;
    }


    public int getWeek() {return week;}

    @Override
    public String toString() {
        return String.valueOf(year)+String.valueOf(week);
    }
}