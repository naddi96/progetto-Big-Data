package hbase;

import scala.Tuple2;

import java.text.ParseException;
import java.util.Collections;
import java.util.Iterator;

public class DataLoaderQuery1 {

    public static void createTable(HBaseClient hbc){
        if (!hbc.exists("covid_ita")){

            System.out.println("Creating table...");
            hbc.createTable("covid_ita",
                    "covid");
        }else{
            System.out.println("Dropping table table...");
            hbc.dropTable("covid_ita");
            System.out.println("Creating table table...");
            hbc.createTable("covid_ita",
                    "covid");
        }
    }


    public static Iterator<Object> insertLineToHBase(HBaseClient hbc, Tuple2<Integer, Tuple2> line) throws ParseException {
        String[] cols={ line._2._1.toString(), line._2._2.toString()};
        hbc.put("covid_ita", line._1.toString(), cols);
        return Collections.emptyList().iterator();
    }


    }
