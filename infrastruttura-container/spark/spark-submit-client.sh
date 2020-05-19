docker exec mycluster-master hadoop fs -fs  hdfs://mycluster-master:9000 -put -f /myapp/progetto1-1.0.jar /
docker exec mycluster-master $SPARK_HOME/bin/spark-submit --class "Query2"  --master "spark://mycluster-master:7077" hdfs://mycluster-master:9000/progetto1-1.0.jar

