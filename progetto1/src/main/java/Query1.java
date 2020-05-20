import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import scala.Tuple2;


import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import covidSerilizer.CovidIta;
import parseParquet.ParqueIta;
import utils.HDFS;
//import scala.Tuple2;

public class Query1 {


    private static int lunedi=0;
    private static int martedi=1;
    private static int mercoledi=2;
    private static int giovedi=3;
    private static int venerdi=4;
    private static int sabato=5;
    private static int domenica=6;

    private static int startingDay=lunedi;




    private static String hdfs_master="hdfs://mycluster-master:9000";
    private static String pathToFile =hdfs_master+"/user/nifi/covidIta/covidIta.parquet";
    private static String pathToOutput = hdfs_master+"/user/nifi/covidIta/output/";



    public static void main(String[] args) throws IOException, URISyntaxException {
/*
        pathToFile="/data/covidIta.csv"
        pathToOutput="/data/out/covidIta.out"
        String outputPath = "output";

        SparkConf conf = new SparkConf()
                .setMaster("local")
                .setAppName("query1");
        JavaSparkContext sc = new JavaSparkContext(conf);

*/
        long startTimeSparkConf = System.nanoTime();

        SparkConf conf = new SparkConf().setAppName("query1");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().config(sc.getConf()).getOrCreate();

        long endTimeSparkConf = System.nanoTime();

        long startQueryPro = System.nanoTime();


        JavaRDD<Row> parquetFileDF=spark.
                read().parquet(pathToFile).javaRDD();



        //considero solo i giorni di inizio e fine settimana
                    //week ,    positive,tampons
        JavaPairRDD<Integer,Tuple2<Float,Float>> pairs =
                parquetFileDF.flatMapToPair(line -> ParqueIta.parseRow(line,startingDay));

                        //week ,    positive,tampons
        JavaPairRDD<Integer, Tuple2<Float,Float>> avg_week =
                pairs.reduceByKey((x, y) ->mediaOutput(x,y)).filter(f-> f._2._1 > 0 );


        //coversione a formato parquet
        JavaRDD<Row> rowRDD = avg_week.map(tuple -> RowFactory.create(tuple._1,tuple._2._1,tuple._2._2 ));

        //creazione dello schema
        StructField[] structFields = new StructField[]{
                new StructField("AnnoSettimana", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("guariti", DataTypes.FloatType, true, Metadata.empty()),
                new StructField("tamponi", DataTypes.FloatType, true, Metadata.empty())

        };
        StructType structType = DataTypes
                .createStructType(structFields);

        Dataset<Row> output = spark.createDataFrame(rowRDD, structType);
        output.write().mode("overwrite").parquet(pathToOutput);
        long endQueryPro = System.nanoTime();

        long sparkTimeNano= ( endTimeSparkConf - startTimeSparkConf ) ;
        float sparkTimeSec= (float) ( endTimeSparkConf - startTimeSparkConf )/1000000000 ;
        long queryTimeNano= ( endQueryPro - startQueryPro );
        float queryTimeSec= (float) ( endQueryPro - startQueryPro )/1000000000 ;


        String filename=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date())+"time.txt";
        String time="****************QUERY1***************\n" +
                "loading contex: "+sparkTimeSec+" Sec. \n" +
                "loading contex: "+sparkTimeNano+" NanoSec.\n" +
                "*************************************\n " +
                "query process: "+queryTimeSec+" Sec. \n" +
                "query process: "+queryTimeNano+" NanoSec.\n" +
                "*************************************\n ";
        HDFS.saveStringTohdfs(hdfs_master,filename,time,"time_query1");

        }




    public static Tuple2<Float, Float> mediaOutput(Tuple2<Float,Float> a, Tuple2<Float,Float> b){
        //inizialmente ho memorizzato i segni in negativo adesso li ricambio solo
        //per i giorni che sono nella stessa settimana -> gli eventuali giorni singoli resteranno negativi

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


