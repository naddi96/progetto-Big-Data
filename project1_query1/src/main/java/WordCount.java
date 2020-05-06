import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.*;
import scala.Tuple2;
import scala.Tuple3;
//import scala.Tuple2;

import java.util.Arrays;
import java.util.regex.Pattern;

public class WordCount {

    private static final Pattern SPACE = Pattern.compile(" ");

    private static String pathToFile = "data/dpc-covid19-ita-andamento-nazionale.csv";

    public static void main(String[] args){

        String outputPath = "output";
        if (args.length > 0)
            outputPath = args[0];

        SparkConf conf = new SparkConf()
                .setMaster("local")
                .setAppName("Hello World");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile(pathToFile);
        String head =lines.first();
        JavaRDD<String> row_data = lines.filter(row -> !(row.equals(head)));


        JavaRDD<COVID> cov =
                row_data.map(
                        // line -> OutletParser.parseJson(line))         // JSON
                        line -> CSV.parseCSV(line));

        for (COVID x: cov.collect()){
            System.out.println(x.getTampons());
        }

    }





}
