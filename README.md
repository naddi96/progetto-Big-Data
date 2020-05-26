# progetto-Big-Data

## Clone the repository

- `git clone git@github.com:naddi96/progetto-Big-Data.git`


## compile the code

Compile the java code inside "progetto1" folder with 

- `mvn deploy`

then copy the .jar file inside the target folder into "infrastruttura-container/spark"

- `cp target/progetto1-1.0.jar  infrastruttura-container/spark`

## start containers

for starting the container infrastructure go inside the "infrastruttura-container" and follow the READMEs files inside this folders:

   - spark
   - nifi
   - livy
   - hbase

and start the containers

## start processing infrastructure

afster starting all the containers go to the nifi web interface host_ip:9090 and import Query1.xml or Query2.xml (located at ./infrastruttura-container/nifi)
and start the processing from nifi

## Query the processed data inside the Hbase

the web interface for plotting the data from Hbase is Left as an exercise to the reader.

## DataSets used in this project
   
   https://github.com/dbouquin/IS_608/blob/master/NanosatDB_munging/Countries-Continents.csv
   
   https://github.com/pcm-dpc/COVID-19/blob/master/dati-andamento-nazionale/dpc-covid19-ita-andamento-nazionale.csv
   
   https://github.com/CSSEGISandData/COVID-19/blob/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv


