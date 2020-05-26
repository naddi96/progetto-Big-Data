package parseParquet;

import org.apache.spark.sql.Row;
import covidSerilizer.CovidIta;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class ParqueIta {
    public static Iterator<Tuple2<Integer,Tuple2<Float,Float>>> parseRow(Row line,int startingDay) {

        CovidIta cov = new CovidIta(
                line.getString(0), // date
                line.getString(1), // guariti
                line.getString(2),// tampons
                startingDay
        );


        if(cov.getTampons()==-1){
            return Collections.<Tuple2<Integer,Tuple2<Float,Float>>> emptyList().iterator();
        }else{

            return Arrays.asList(
                new Tuple2<>(//cambio il segno inquanto successivamente lo
                        // ricambierò alle sole  coppie di giorni nella stessa settimana dentro la map to pair in modo tale da distingurli                                 si questo commento da fastidio LMAO
                        Integer.valueOf(cov.toString()), //AnnoSettimana
                            new Tuple2<>(-(float) cov.getPositive(),//Positivi
                                        -(float) cov.getTampons()))//tamponi
            ).iterator();
        }
    }
}
