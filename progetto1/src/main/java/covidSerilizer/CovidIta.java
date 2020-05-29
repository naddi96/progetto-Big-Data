package covidSerilizer;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CovidIta implements Serializable {

    private int positive;
    private String year;
    private String week;
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


        this.year=String.valueOf(cal.get(Calendar.YEAR));
        this.week=String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E", Locale.ITALY); // the day of the week abbreviated
        String day = simpleDateformat.format(cal.getTime());
        this.week=String.valueOf(simpleDateformat.getCalendar().get(Calendar.WEEK_OF_YEAR));

        if (!(day.equals("lun")|| day.equals("dom"))) {
            this.tampons=-1;
            this.positive=-1;
            this.week="-1";

        }


    }



    public int getTampons() {
        return tampons;
    }

    public int getPositive() {
        return positive;
    }

    public String getYear() {
        return year;
    }


    public String getWeek() {return week;}

    @Override
    public String toString() {
        return year+week;
    }
}