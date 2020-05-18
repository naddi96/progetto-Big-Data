import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import scala.Tuple2;


import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import utils.CovidIta;
import utils.ParqueIta;
//import scala.Tuple2;

public class Query1 {



    //private static String pathToFile = "data/dpc-covid19-ita-andamento-nazionale.csv";
    private static String pathToFile ="hdfs://mycluster-master:9000/user/nifi/covidIta/covidIta.parquet";
    private static String pathToOutput = "hdfs://mycluster-master:9000/user/nifi/covidIta/output/";

    public static void main(String[] args) {
/*
        String outputPath = "output";
       if (args.length > 0)
            outputPath = args[0];


        SparkConf conf = new SparkConf()
                .setMaster("local")

                .setAppName("query");
        JavaSparkContext sc = new JavaSparkContext(conf);

*/
        JavaSparkContext sc = new JavaSparkContext(new SparkConf());


        SparkSession spark = SparkSession.builder().config(sc.getConf()).getOrCreate();
        JavaRDD<Row> parquetFileDF=spark.
                read().parquet("pathToFile").javaRDD();




        JavaRDD<CovidIta> cov =
                parquetFileDF.flatMap(line -> ParqueIta.parseRow(line));
                        // line -> OutletParser.parseJson(line))         // JSON

        JavaPairRDD<Integer,Tuple2<Float,Float>> pairs =
                cov.mapToPair(co -> new Tuple2<Integer,
                        Tuple2<Float,Float>>(co.getWeek(), new Tuple2<Float, Float>(- (float) co.getPositive(),- (float) co.getTampons())));


        JavaPairRDD<Integer, Tuple2<Float,Float>> avg_week =
                pairs.reduceByKey((x, y) ->mediaOutput(x,y)).filter(f-> f._2._1 > 0 );
        JavaRDD<Row> rowRDD = avg_week.map(tuple -> RowFactory.create(tuple._1,tuple._2._1,tuple._2._2 ));

        StructField[] structFields = new StructField[]{
                new StructField("AnnoSettimana", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("guariti", DataTypes.FloatType, true, Metadata.empty()),
                new StructField("tamponi", DataTypes.FloatType, true, Metadata.empty())
        };
        StructType structType = DataTypes
                .createStructType(structFields);

        Dataset<Row> output = spark.createDataFrame(rowRDD, structType);
        output.write().mode("overwrite").parquet(pathToOutput);
//       output.write().parquet("hdfs://mycluster-master:9000/user/nifi/covidIta/output/");

        }

    public static Tuple2<Float, Float> mediaOutput(Tuple2<Float,Float> a, Tuple2<Float,Float> b){
        float max_gua= Math.max(-a._1,-b._1);
        float max_tam= Math.max(-a._2,-b._2);
        float min_gua =Math.min(-a._1,-b._1);
        float min_tam= Math.min(-a._2,-b._2);
        float gua_med= new Float((max_gua-min_gua) / 7.0) ;
        float tamp_med=new Float((max_tam-min_tam) /  7.0);


        return new Tuple2<>(gua_med ,tamp_med);
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


