package parseCsv;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import scala.Tuple2;
import utils.CovidGlob;

import java.util.*;


public class CsvGlob {
    
    
    public static Iterator<Tuple2<Double, CovidGlob>> parseCSV(String csvLine, int startingDay) {

        String[] csvValues = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        if (csvValues[0].equals("Province/State")){
            return Collections.<Tuple2<Double, CovidGlob>> emptyList().iterator();
        }

        int lun = csvValues.length;
        List<Integer> lisat = new ArrayList<>();
        int i = startingDay+4;
        int offset=i;
        SimpleRegression regression = new SimpleRegression();
        
        
        int m=0;
        while (i < lun) {
            int app =Integer.valueOf(csvValues[i]);
            lisat.add(app-m);
            regression.addData(i-offset, app);
            m=app;
            i++;
        }
        
        CovidGlob cov =new CovidGlob(
                csvValues[0].replace("\"",""),
                csvValues[1].replace("\"",""),
                lisat
        );

        return Arrays.asList(
                new Tuple2<>(regression.getSlope(),cov )
        ).iterator();
        //csvValues[0]State;
        // csvValues[1]SCountry;
    }


}
