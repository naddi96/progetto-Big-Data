package parseParquet;

import org.apache.spark.sql.Row;
import covidSerilizer.CovidIta;
import scala.Tuple2;
import scala.Tuple3;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class ParqueIta {
    public static Iterator<Tuple2<Integer, Tuple3<Float,Float,Boolean>>> parseRow(Row line, int startingDay) {

        CovidIta cov = new CovidIta(
                line.getString(0), // date
                line.getString(1), // guariti
                line.getString(2),// tampons
                startingDay
        );


        if(cov.getTampons()==-1){
            return Collections.<Tuple2<Integer,Tuple3<Float,Float,Boolean>>> emptyList().iterator();
        }else{

            return Arrays.asList(
                new Tuple2<>(
                        Integer.valueOf(cov.toString()), //AnnoSettimana
                            new Tuple3<>((float) cov.getPositive(),//Positivi
                                        (float) cov.getTampons(),//tamponi
                                         false   )) //questo servira succesivamnete a distinguere le coppie di giorni con i giorni dingoli
            ).iterator();
        }
    }
}
