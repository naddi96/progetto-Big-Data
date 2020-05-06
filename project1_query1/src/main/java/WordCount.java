import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.sources.In;
import scala.Tuple2;
import scala.Tuple3;
//import scala.Tuple2;

import java.util.regex.Pattern;

public class WordCount {

    private static final Pattern SPACE = Pattern.compile(" ");

    private static String pathToFile = "data/dpc-covid19-ita-andamento-nazionale.csv";

    public static void main(String[] args) {

        String outputPath = "output";
        if (args.length > 0)
            outputPath = args[0];

        SparkConf conf = new SparkConf()
                .setMaster("local")
                .setAppName("Hello World");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile(pathToFile);
        String head = lines.first();
        JavaRDD<String> row_data = lines.filter(row -> !(row.equals(head)));


        JavaRDD<COVID> cov =
                row_data.map(
                        // line -> OutletParser.parseJson(line))         // JSON
                        line -> CSV.parseCSV(line)).filter(row -> row.getDay()!=-1 );
        JavaPairRDD<Integer,Tuple2> pairs =
                cov.mapToPair(co -> new Tuple2<>(co.getWeek(), new Tuple2<>(co.getPositive(),co.getTampons())   ));
        JavaPairRDD<Integer, Tuple2> counts =
                pairs.reduceByKey((x, y) ->mediaOutput(x,y)).sortByKey();


        counts.coalesce(1,false).saveAsTextFile("output");

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


}


