package parseParquet;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.spark.sql.Row;
import scala.Tuple2;
import utils.CovidGlob;

import java.util.ArrayList;
import java.util.List;

public class parquetGlob {
    public static Tuple2<Double, CovidGlob> parseParquet(Row row, int startingDay) {

        //csvLine=csvLine.replaceAll("(?!(([^\"]*\"){2})*[^\"]*$),",""); // rimuove la , tra le " " es: "korea, sud" -> "korea sud"
       // String[] csvValues = csvLine.split(",");
        int lung = row.length();
        List<Integer> lisat = new ArrayList<>();
        int i = startingDay+4;
        int offset=i;

        SimpleRegression regression = new SimpleRegression();


        int m=0;
        while (i < lung) {
            int app =Integer.valueOf(row.getString(i));
            lisat.add(app-m);
            regression.addData(i-offset, app);
            m=app;
            i++;
        }

        CovidGlob cov =new CovidGlob(
                row.getString(0),
                row.getString(1),
                lisat
        );

        return new Tuple2<>(regression.getSlope(),cov );


    }

}
