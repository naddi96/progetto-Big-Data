package utils;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;





public class CsvGlob {
    
    
    public static Tuple2<Double, CovidGlob> parseCSV(String csvLine) {

        csvLine=csvLine.replaceAll("(?!(([^\"]*\"){2})*[^\"]*$),",""); // rimuove la , tra le " " es: "korea, sud" -> "korea sud"
        String[] csvValues = csvLine.split(",");
        int lun = csvValues.length;
        List<Integer> lisat = new ArrayList<>();
        int i = 4;

        SimpleRegression regression = new SimpleRegression();
        
        
        int m=0;
        while (i < lun) {
            int app =Integer.valueOf(csvValues[i]);
            lisat.add(app-m);
            regression.addData(i-4, app);
            m=app;
            i++;
        }
        
        CovidGlob cov =new CovidGlob(
                csvValues[0],
                csvValues[1],
                lisat
        );
        
        return new Tuple2<>(regression.getSlope(),cov );

        //csvValues[0]State;
        // csvValues[1]SCountry;
    }

    public static CovidGlob CovidGlobTupleParse(Tuple2<Double, CovidGlob> covid_in) {
        covid_in._2.setContinet("continete");
        covid_in._2.setContagiati(covid_in._2.getContagiati());
        
    return covid_in._2;
    }
}
