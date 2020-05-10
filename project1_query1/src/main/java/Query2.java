import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import utils.CovidGlob;
import utils.CsvGlob;
import java.util.Comparator;
import java.util.List;
import java.io.Serializable;

public class Query2 {

    private static String pathToFile = "data/time_series_covid19_confirmed_global.csv";

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setMaster("local").setAppName("query");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile(pathToFile);
        String head = lines.first();
        JavaRDD<String> row_data = lines.filter(row -> !(row.equals(head)));

        JavaPairRDD<Double, CovidGlob> pairs = row_data.mapToPair(line -> CsvGlob.parseCSV(line));
        

        List<Tuple2<Double, CovidGlob>> top_100 = pairs.top(100, new ValueComparator<>(Comparator.<Double>naturalOrder()));
        for (Tuple2<Double, CovidGlob> x: top_100){
          System.out.println(x._2.getCountry()+" "+x._2.getState()+" "+x._1);  
        }

        JavaRDD<Tuple2<Double, CovidGlob>> top_rdd = sc.parallelize(top_100);
        top_rdd.map(f) 
            
    }

    public static class ValueComparator<K, V> implements Comparator<Tuple2<K, V>>, Serializable {

        private Comparator<K> comparator;

        public ValueComparator(Comparator<K> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
            return comparator.compare(o1._1(), o2._1());
        }

    }

}
