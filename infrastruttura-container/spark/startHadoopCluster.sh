#!/bin/bash

# VARIABLES
IMG_NAME="spark-hadoop-cluster"
HOST_PREFIX="mycluster"
NETWORK_NAME=$HOST_PREFIX


N=${1:-2}
NET_QUERY=$(docker network ls | grep -i $NETWORK_NAME)
if [ -z "$NET_QUERY" ]; then
	docker network create --driver=bridge $NETWORK_NAME
fi

# START HADOOP SLAVES 
i=1
while [ $i -le $N ]
do
	HADOOP_SLAVE="$HOST_PREFIX"-slave-$i
	docker run --volume "$PWD":/myapp/ --name $HADOOP_SLAVE -h $HADOOP_SLAVE --net=$NETWORK_NAME -itd "$IMG_NAME"
	i=$(( $i + 1 ))
done

# START HADOOP MASTER

HADOOP_MASTER="$HOST_PREFIX"-master
docker run  --volume "$PWD":/myapp/ --name $HADOOP_MASTER -h $HADOOP_MASTER --net=$NETWORK_NAME \
		-p 9001:9000 -p 7077:7077 -p  8088:8088  -p 50070:50070 -p 50090:50090 \
		-p  8081:8080 \
		-itd "$IMG_NAME"


# START MULTI-NODES CLUSTER
docker exec -it $HADOOP_MASTER "/usr/local/hadoop/spark-services.sh"
docker exec $HADOOP_MASTER hadoop fs -fs  hdfs://mycluster-master:9000 -put -f /myapp/progetto1-1.0.jar /






