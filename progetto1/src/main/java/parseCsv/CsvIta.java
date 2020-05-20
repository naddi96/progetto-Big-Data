package utils;

import covidSerilizer.CovidIta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class CsvIta {

    public static Iterator<CovidIta> parseCSV(String csvLine,int startingDay) {

        CovidIta cov = null;
        String[] csvValues = csvLine.split(",");

        //   if (csvValues.length != 7)
        //     return null;

//            1464894,1377987280,3.216,0,1,0,3
        if (csvValues[0].equals("data")){
            return Collections.<CovidIta> emptyList().iterator();
        }

        cov = new CovidIta(
                csvValues[0], // date
                csvValues[1], // guariti
                csvValues[2], // tampons
                startingDay
        );

        if(cov.getTampons()==-1){
            return Collections.<CovidIta> emptyList().iterator();
        }else{
            return Arrays.asList(cov).iterator();
        }
    }
}
