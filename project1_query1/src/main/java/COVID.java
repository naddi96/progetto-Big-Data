import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class COVID implements Serializable {

    private int positive;
    private Date date;
    private int day;
    private int week;
    private int tampons;



    public COVID(String date, String positive, String tampons)  {

        this.tampons =Integer.parseInt(tampons);
        this.positive = Integer.parseInt(positive);


        String input = date.substring(0,9);
        String format = "yyyy-MM-dd";

        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dat = null;

        this.date =dat;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        this.week = cal.get(Calendar.WEEK_OF_YEAR);
        this.day= cal.get(Calendar.DAY_OF_WEEK);


    }



    public int getTampons() {
        return tampons;
    }

    public int getPositive() {
        return positive;
    }

    public Date getDate() {
        return date;
    }
    public int getDay(){return day;}

    public int getWeek() {return week;}

    @Override
    public String toString() {
        return date.toString()+" "+day+" "+week;
    }
}