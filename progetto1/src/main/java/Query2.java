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
import utils.CovidGlob;

import utils.GlobMapFunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;

public class Query2 {

    private static int lunedi=5;
    private static int martedi=6;
    private static int mercoledi=0;
    private static int giovedi=1;
    private static int venerdi=2;
    private static int sabato=3;
    private static int domenica=4;

    private static int startingDay=lunedi;

    private static String firstDate="1/22/20";


    private static String pathToFile = "hdfs://mycluster-master:9000/user/nifi/covidWor/covidWor.csv";
    private static String continentToContry = "hdfs://mycluster-master:9000/user/nifi/covidWor/Countries-Continents.parquet";
    private static String pathToOutput="hdfs://mycluster-master:9000/user/nifi/covidWor/output/";

    public static void main(String[] args) {


        SparkConf conf = new SparkConf().setAppName("query2");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().config(sc.getConf()).getOrCreate();

        JavaRDD<String> rdd = sc.textFile(pathToFile);
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
        System.out.println(rowRDD.take(1).get(0));

        Row row = rowRDD.take(1).get(0);

        int len =row.length()-3;
        StructType schema = createSchema(len);
        Dataset<Row> output = spark.createDataFrame(rowRDD, schema);
        output.javaRDD().collect();

        output.write().mode("overwrite").parquet(pathToOutput);

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

        while(len!=0){


            String annoSet="a"+String.valueOf( c.get(Calendar.YEAR)) + "s"+
                    String.valueOf(c.get(Calendar.WEEK_OF_YEAR));
            fields.add(new StructField(annoSet, DataTypes.FloatType, true, Metadata.empty()));
            c.add(Calendar.DATE, 7);
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
