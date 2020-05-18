package utils;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;





public class CsvGlob {
    
    
    public static Tuple2<Double, CovidGlob> parseCSV(String csvLine) {

        csvLine=csvLine.replaceAll("(?!(([^\"]*\"){2})*[^\"]*$),",""); // rimuove la , tra le " " es: "korea, sud" -> "korea sud"
        String[] csvValues = csvLine.split(",");
        int lun = csvValues.length;
        List<Integer> lisat = new ArrayList<>();
        int i = 4;

        SimpleRegression regression = new SimpleRegression();
        
        
        int m=0;
        while (i < lun) {
            int app =Integer.valueOf(csvValues[i]);
            lisat.add(app-m);
            regression.addData(i-4, app);
            m=app;
            i++;
        }
        
        CovidGlob cov =new CovidGlob(
                csvValues[0],
                csvValues[1],
                lisat
        );
        
        return new Tuple2<>(regression.getSlope(),cov );

        //csvValues[0]State;
        // csvValues[1]SCountry;
    }

    public static Tuple2<String, CovidGlob> makeCountryKey(Tuple2<Double, CovidGlob> covid_in) {
/*
        //compute statistics
        
        List<Integer> lista = covid_in._2.getContagiati();
        int i=0;
        int min=99999999;
        int max=0;
        int tot=0;
        List<Integer> settimana =new ArrayList<>();;
        List<Integer> m
        for(Integer x:lista){
            if(x<min) min=x;
            if(x>max) max=x;

            tot=tot+x;
            settimana.add(x);
            if(i==6){

                double media = tot/7.0;
                double dev=deviation(settimana, tot, media);
                


                i=0;
                tot=0;
                min=99999999;
                max=0;
            }else{
                i++;
            }
           

        }

        covid_in._2.setLineCoefficent(covid_in._1); */
    return new Tuple2<>(covid_in._2.getCountry(),covid_in._2 );
    }


    public static Tuple2<String,String> continetCountry(String csvLine){
        csvLine=csvLine.replaceAll("(?!(([^\"]*\"){2})*[^\"]*$),",""); // rimuove la , tra le " " es: "korea, sud" -> "korea sud"
        String[] csvValues = csvLine.split(",");
        if (csvValues[0].equals("South America") || csvValues[0].equals("North America")){
            return new Tuple2<>(csvValues[1],"America");
        }

        return new Tuple2<>(csvValues[1],csvValues[0]);
    }

    public static Double deviation(List<Integer> giorni,Integer tot,Double media){
        Double diff=0.0;
        for (Integer day:giorni){
            diff=diff + Math.pow(day-media, 2);
        }
        return Math.pow(diff/7.0 , 0.5);

    }

    public static  Tuple2<String, Tuple2<List<String>,List<Integer>>> 
        makeContinentKey(Tuple2<String, Tuple2<CovidGlob, String>> input){
            List<String> countryList =new ArrayList<>();;
            countryList.add( "//"+input._2._1.getCountry()+","+input._2._1.getState()+"//");
            return new Tuple2<>(input._2._2,  new Tuple2<>(countryList,input._2._1.getContagiati()));

    }

    public static Tuple2 sommaStati
    (Tuple2<List<String>,List<Integer>> a, Tuple2<List<String>,List<Integer>>  b){
        
        Iterator<Integer> iterA = a._2.iterator();
        Iterator<Integer> iterB = b._2.iterator();
        List<Integer> continente =new ArrayList<>();
        while (iterA.hasNext() && iterB.hasNext()){
            int somma=iterA.next() + iterB.next();
            continente.add(somma);

        }
        List<String> countryList =new ArrayList<>();
        countryList.add( a._1.get(0));
        countryList.add(b._1.get(0));
     //   iterA.
        return new Tuple2<>(countryList,continente);


    }


    public static CovidContinent computeStatistics(Tuple2<String, Tuple2<List<String>, List<Integer>>> line){
           
        List<Integer> lista = line._2._2;
        int i=0;
        int min=Integer.MAX_VALUE;
        int max=0;
        int tot=0;
        List<Integer> settimana =new ArrayList<>();
      
        List<Integer> max_list=new ArrayList<>();
        List<Integer> min_list=new ArrayList<>();
        List<Double> dev_list=new ArrayList<>();
        List<Double> media_list=new ArrayList<>();


        for(Integer x:lista){
            if(x<min) min=x;
            if(x>max) max=x;

            tot=tot+x;
            settimana.add(x);
            if(i==6){

                double media = tot/7.0;
                double dev = deviation(settimana, tot, media);
                
                min_list.add(min);
                max_list.add(max);
                media_list.add(media);
                dev_list.add(dev);  


                settimana.clear();
                i=0;
                tot=0;
                min=Integer.MAX_VALUE;
                max=0;
            }else{
                i++;
            }
        }
        return new CovidContinent(line._1,line._2._1(), max_list, min_list, dev_list, media_list);

    }
}
