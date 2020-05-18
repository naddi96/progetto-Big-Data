# Docker hadoop yarn cluster for spark 2.4.1
this docker container has been modified to suit my use from this repository
https://github.com/PierreKieffer/docker-spark-yarn-cluster

## docker-spark-yarn-cluster 
This application allows to deploy multi-nodes hadoop cluster with spark 2.4.1 on yarn. 

## Build image
- Clone the repo 
- cd inside ../spark 
- Run `./dowloadSpark.sh`

## Run  
- Run `/startHadoopCluster.sh`
- Access to master `docker exec -it mycluster-master bash`

### Run spark applications on cluster : 
- spark-shell : `spark-shell --master yarn --deploy-mode client`
- spark : `spark-submit --master yarn --deploy-mode client or cluster --num-executors 2 --executor-memory 4G --executor-cores 4 --class org.apache.spark.examples.SparkPi $SPARK_HOME/examples/jars/spark-examples_2.11-2.4.1.jar`

- Access to Hadoop cluster Web UI : <container ip>:8088 
- Access to spark Web UI : <container ip>:8080
- Access to hdfs Web UI : <container ip>:50070
  
### For upload the code to hdfs:
#### (its also uploaded when the container is started the the default jar is progetto1-1.0.jar ):

- `./uploadJarHdfs.sh`


## Stop 
- `./stopAndRemove.sh`


