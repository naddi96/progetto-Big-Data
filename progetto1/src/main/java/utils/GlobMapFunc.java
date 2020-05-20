package utils;

import covidSerilizer.CovidGlob;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GlobMapFunc {

    public static Tuple2<String, CovidGlob> makeCountryKey(Tuple2<Double, CovidGlob> covid_in) {
        return new Tuple2<>(covid_in._2.getCountry(),covid_in._2 );
    }


    public static Tuple2<String,String> continetCountryToCountryContinent(Row line){
        if (line.getString(0).equals("South America") || line.getString(0).equals("North America")){
            return new Tuple2<>(line.getString(1),"America");
        }

        return new Tuple2<>(line.getString(1),line.getString(0));
    }



    public static  Tuple2<String, List<Integer>>
    makeContinentKey(Tuple2<String, Tuple2<CovidGlob, String>> input){
        List<String> countryList =new ArrayList<>();;
        //countryList.add( "//"+input._2._1.getCountry()+"-"+input._2._1.getState()+"//");
        return new Tuple2<>(input._2._2,input._2._1.getContagiati());

    }


    //somma la lista degli stati nello stesso continente
    public static List<Integer> sommaStati
            (List<Integer> a, List<Integer>  b){

        Iterator<Integer> iterA = a.iterator();
        Iterator<Integer> iterB = b.iterator();
        List<Integer> continente =new ArrayList<>();
        while (iterA.hasNext()){
            int somma=iterA.next() + iterB.next();
            continente.add(somma);

        }
        return continente;
    }


    public static Iterator<Row>  computeStatistics(Tuple2<String,List<Integer>> line){

        List<Integer> lista = line._2;
        int i=0;
        int min=Integer.MAX_VALUE;
        int max=Integer.MIN_VALUE;
        int tot=0;
        List<Integer> settimana =new ArrayList<>();
        List<Object> max_list=new ArrayList<>();
        List<Object> min_list=new ArrayList<>();
        List<Object> dev_list=new ArrayList<>();
        List<Object> avg_list=new ArrayList<>();

        //quando questi dati verrano caricati base in questo modo mi assicuro che vadano in region server
        // diffeernti mentre le righe che contengono la stessa funzione vadano negli stessi (scann più veloce)
        String continent=line._1;
        max_list.add("AAA"+continent);max_list.add(continent);max_list.add("MAX_WEEK");
        min_list.add("ZZZ"+continent);min_list.add(continent);min_list.add("MIN_WEEK");
        dev_list.add("III"+continent);dev_list.add(continent);dev_list.add("DEV_WEEK");
        avg_list.add("RRR"+continent);avg_list.add(continent);avg_list.add("AVG_WEEK");

        //max_list.add(states);min_list.add(states);dev_list.add(states)avg_list.add(states);

        for(Integer x:lista){
            if(x<min) min=x;
            if(x>max) max=x;

            tot=tot+x;
            settimana.add(x);
            if(i==6){
                double media = tot/7.0;
                double dev = deviation(settimana,media);
                min_list.add((float) min);
                max_list.add((float) max);
                avg_list.add((float) media);
                dev_list.add((float) dev);
                settimana.clear();
                i=0;
                tot=0;
                min=Integer.MAX_VALUE;
                max=Integer.MIN_VALUE;
            }else{
                i++;
            }
        }
        return Arrays.asList(
                RowFactory.create(max_list.toArray()),
                RowFactory.create(min_list.toArray()),
                RowFactory.create(dev_list.toArray()),
                RowFactory.create(avg_list.toArray())
        ).iterator();

    }

    public static Double deviation(List<Integer> giorni, Double media){
        Double diff=0.0;
        for (int day:giorni){
            diff=diff + Math.pow(day-media, 2);
        }
        return Math.pow(diff/7.0 , 0.5);

    }
}
