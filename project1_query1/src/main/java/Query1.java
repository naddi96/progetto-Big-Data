import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import utils.CovidIta;
import utils.CsvIta;

import java.io.Serializable;
import java.util.Comparator;

//import scala.Tuple2;

public class Query1 {



    private static String pathToFile = "data/dpc-covid19-ita-andamento-nazionale.csv";

    public static void main(String[] args) {

        String outputPath = "output";
        if (args.length > 0)
            outputPath = args[0];
        
        SparkConf conf = new SparkConf()
                .setMaster("local")
                .setAppName("query");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile(pathToFile);



        JavaRDD<CovidIta> cov =
                lines.flatMap(line -> CsvIta.parseCSV(line));
                        // line -> OutletParser.parseJson(line))         // JSON


        JavaPairRDD<Integer,Tuple2> pairs =
                cov.mapToPair(co -> new Tuple2<>(co.getWeek(), new Tuple2<>(co.getPositive(),co.getTampons())   ));
        JavaPairRDD<Integer, Tuple2> counts =
                pairs.reduceByKey((x, y) ->mediaOutput(x,y)).sortByKey();


//        counts.coalesce(1,false).saveAsTextFile("output");
        System.out.println(counts.top(5, new ValueComparator<>(Comparator.<Integer>naturalOrder())));

        }

    public static Tuple2<Double, Double> mediaOutput(Tuple2<Integer,Integer> a, Tuple2<Integer,Integer> b){
        int max_gua= Math.max(a._1,b._1);
        int max_tam= Math.max(a._2,b._2);
        int min_gua =Math.min(a._1,b._1);
        int min_tam= Math.min(a._2,b._2);
        double gua_med= new Double(max_gua-min_gua) / 7 ;
        double tamp_med=new Double (max_tam-min_tam) /  7;
        return new Tuple2<>(gua_med,tamp_med);
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


