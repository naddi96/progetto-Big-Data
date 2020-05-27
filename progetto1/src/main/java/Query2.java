import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import parseCsv.CsvGlob;
import scala.Tuple2;
import covidSerilizer.CovidGlob;

import utils.GlobMapFunc;
import utils.HDFS;

import java.io.*;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Query2 {

    private static String hdfs_master="hdfs://mycluster-master:9000";

    private static int lunedi=5;
    private static int martedi=6;
    private static int mercoledi=0;
    private static int giovedi=1;
    private static int venerdi=2;
    private static int sabato=3;
    private static int domenica=4;

    private static int startingDay=lunedi;

    private static String firstDate="1/22/20";



    private static String pathToFile =  hdfs_master+"/user/nifi/covidWor/covidWor.csv";
    private static String continentToContry = hdfs_master+"/user/nifi/covidWor/Countries-Continents.parquet";
    private static String pathToOutput = hdfs_master+"/user/nifi/covidWor/output/";

    public static void main(String[] args) throws IOException, URISyntaxException {

        /*
        pathToFile="/data/covidWor.csv"
        pathToOutput="/data/out/covidWor.out"
        String outputPath = "output";
        SparkConf conf = new SparkConf()
                .setMaster("local")
                .setAppName("query2");
        JavaSparkContext sc = new JavaSparkContext(conf);
        */
        long startTimeSparkConf = System.nanoTime();

        SparkConf conf = new SparkConf().setAppName("query2");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().config(sc.getConf()).getOrCreate();

        long endTimeSparkConf = System.nanoTime();

        long startQueryPro = System.nanoTime();


        JavaRDD<String> rdd = sc.textFile(pathToFile);


        //uso il csv inquanto nifi ha problemi a cambiare il formato in parquet in questo csv
        //uso la maptopair inquanto non considero l'header del csv
        JavaPairRDD<Double, CovidGlob> pairs = rdd.flatMapToPair(line -> CsvGlob.parseCSV(line, startingDay));

        List<Tuple2<Double, CovidGlob>> top_100 = pairs.top(100,
                new ValueComparator<>(Comparator.<Double>naturalOrder()));

        JavaRDD<Tuple2<Double, CovidGlob>> top_100_rdd = sc.parallelize(top_100);

        JavaPairRDD<String, CovidGlob> covid_state_key = top_100_rdd.mapToPair(f -> GlobMapFunc.makeCountryKey(f));

        JavaRDD<Row> line_continet=spark.read().parquet(continentToContry).javaRDD();


        JavaPairRDD<String, String> continentMapping = line_continet.mapToPair(line ->
                GlobMapFunc.continetCountryToCountryContinent(line));



        JavaPairRDD<String, Tuple2<CovidGlob, String>> 
        joined_state_continent = covid_state_key.join(continentMapping).cache();
        /*
        JavaPairRDD<String, Tuple2<List<String>,List<Integer>>>
                key_continent = joined_state_continent.mapToPair(f -> GlobMapFunc.makeContinentKey(f));

        JavaPairRDD<String, Tuple2<List<String>, List<Integer>>>
                sum_states_in_cont = key_continent.reduceByKey((x, y) -> GlobMapFunc.sommaStati(x, y));
*/


        JavaPairRDD<String, List<Integer>>
                key_continent = joined_state_continent.mapToPair(f -> GlobMapFunc.makeContinentKey(f));


        JavaPairRDD<String, List<Integer>>
                sum_states_in_cont = key_continent.reduceByKey((x, y) -> GlobMapFunc.sommaStati(x, y));

        JavaRDD<Row>
                rowRDD=sum_states_in_cont.flatMap(f-> GlobMapFunc.computeStatistics(f));

        Row row = rowRDD.take(1).get(0);
        int len =row.length()-3;

        StructType schema = createSchema(len);
        Dataset<Row> output = spark.createDataFrame(rowRDD, schema);
        output.write().mode("overwrite").parquet(pathToOutput);

        long endQueryPro = System.nanoTime();
        long sparkTimeNano= ( endTimeSparkConf - startTimeSparkConf ) ;
        float sparkTimeSec= (float) ( endTimeSparkConf - startTimeSparkConf )/1000000000 ;
        long queryTimeNano= ( endQueryPro - startQueryPro );
        float queryTimeSec= (float) ( endQueryPro - startQueryPro )/1000000000 ;


        String filename=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date())+"time.txt";
        String time="****************QUERY2***************\n" +
                    "loading contex: "+sparkTimeSec+" Sec. \n" +
                    "loading contex: "+sparkTimeNano+" NanoSec.\n" +
                    "*************************************\n " +
                    "query process: "+queryTimeSec+" Sec. \n" +
                    "query process: "+queryTimeNano+" NanoSec.\n" +
                    "*************************************\n ";

        HDFS.saveStringTohdfs(hdfs_master,filename,time,"time_query2");
    }

    private static StructType createSchema(int len){
        String format = "MM/dd/yy";
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dat = null;
        try {
            dat = df.parse(firstDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dat);
        c.add(Calendar.DATE, startingDay);

        List<StructField> fields = new ArrayList<>();
        fields.add(new StructField("Key", DataTypes.StringType, true, Metadata.empty()));
        fields.add(new StructField("Continente", DataTypes.StringType, true, Metadata.empty()));
        //fields.add(new StructField("StatoRegione", DataTypes.StringType, true, Metadata.empty()));
        fields.add(new StructField("Funzione", DataTypes.StringType, true, Metadata.empty()));
        int week=c.get(Calendar.WEEK_OF_YEAR);
        while(len!=0){


            String annoSet="a"+String.valueOf( c.get(Calendar.YEAR)) + "s"+
                    String.valueOf(week);
            fields.add(new StructField(annoSet, DataTypes.FloatType, true, Metadata.empty()));
            week=week+1;
            len--;
        }
        return  DataTypes.createStructType(fields);
    }


    private static class ValueComparator<K, V> implements Comparator<Tuple2<K, V>>, Serializable {

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
