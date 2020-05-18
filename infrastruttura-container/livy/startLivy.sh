docker run -d --name=livy --network=mycluster -p 8998:8998 -e DEPLOY_MODE=cluster -e SPARK_MASTER_ENDPOINT=mycluster-master -e SPARK_MASTER_PORT=7077 -v /tmp:/tmp cloudiator/livy-server:latest

