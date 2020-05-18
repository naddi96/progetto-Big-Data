#!/bin/bash

# VARIABLES
IMG_NAME="spark-hadoop-cluster"
HOST_PREFIX="mycluster"
NETWORK_NAME=$HOST_PREFIX


N=${1:-2}
# remove docker HADOOP SLAVES 
i=1
while [ $i -le $N ]
do
	HADOOP_SLAVE="$HOST_PREFIX"-slave-$i
	docker stop $HADOOP_SLAVE
	docker rm $HADOOP_SLAVE
	i=$(( $i + 1 ))
done

# remove START HADOOP MASTER

HADOOP_MASTER="$HOST_PREFIX"-master
docker stop $HADOOP_MASTER 
docker rm $HADOOP_MASTER 
