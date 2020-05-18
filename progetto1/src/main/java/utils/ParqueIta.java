package utils;

import org.apache.spark.sql.Row;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class ParqueIta {
    public static Iterator<CovidIta> parseRow(Row line) {

        CovidIta cov = null;
        //   if (csvValues.length != 7)
        //     return null;

//            1464894,1377987280,3.216,0,1,0,3

        cov = new CovidIta(
                line.getString(0), // date
                line.getString(1), // guariti
                line.getString(2)// tampons
        );

        if(cov.getTampons()==-1){
            return Collections.<CovidIta> emptyList().iterator();
        }else{
            return Arrays.asList(cov).iterator();
        }
    }
}
