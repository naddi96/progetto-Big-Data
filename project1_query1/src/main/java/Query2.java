import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;

import scala.Tuple2;
import utils.CovidContinent;
import utils.CovidGlob;
import utils.CsvGlob;
import java.util.Comparator;
import java.util.List;
import java.io.Serializable;

public class Query2 {

    private static String pathToFile = "data/time_series_covid19_confirmed_global.csv";
    private static String continentToContry = "data/Countries-Continents.csv";

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setMaster("local").setAppName("query");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile(pathToFile);
        String head = lines.first();
        JavaRDD<String> row_data = lines.filter(row -> !(row.equals(head)));

        JavaPairRDD<Double, CovidGlob> pairs = row_data.mapToPair(line -> CsvGlob.parseCSV(line));

        List<Tuple2<Double, CovidGlob>> top_100 = pairs.top(100,
                new ValueComparator<>(Comparator.<Double>naturalOrder()));
        for (Tuple2<Double, CovidGlob> x : top_100) {
            System.out.println(x._2.getCountry() + " " + x._2.getState() + " " + x._1);
        }

        JavaRDD<Tuple2<Double, CovidGlob>> top_rdd = sc.parallelize(top_100);
        JavaPairRDD<String, CovidGlob> covid_state_key = top_rdd.mapToPair(f -> CsvGlob.makeCountryKey(f));

        JavaRDD<String> line_continet = sc.textFile(continentToContry);
        String head2 = line_continet.first();
        JavaRDD<String> line_continet1 = line_continet.filter(f -> !(f.equals(head2)));
        JavaPairRDD<String, String> continent = line_continet1.mapToPair(line -> CsvGlob.continetCountry(line));
/*
        System.out.println();
        for (Tuple2<String, CovidGlob> nnn : covid_state_key.collect()) {
            System.out.println(nnn._1);
        }

        for (Tuple2<String, String> nnn : continent.collect()) {
            System.out.println(nnn._1);}
*/
        JavaPairRDD<String, Tuple2<CovidGlob, String>> 
        join_state_continent = covid_state_key.join(continent);
        
        JavaPairRDD<String, Tuple2<List<String>,List<Integer>> > ddd = join_state_continent.mapToPair(f -> CsvGlob.makeContinentKey(f));
        JavaPairRDD<String, Tuple2<List<String>, List<Integer>>> kkk = ddd.reduceByKey((x, y) -> CsvGlob.sommaStati(x, y));
        JavaRDD<CovidContinent> sss=kkk.map(f-> CsvGlob.computeStatistics(f));
        
    
        for (CovidContinent x : sss.collect()) {
            System.out.println(x);
        }
     


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
