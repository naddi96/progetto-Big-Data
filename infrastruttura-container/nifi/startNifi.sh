docker run --name nifi --network=mycluster -p 9090:9090 -d -e NIFI_WEB_HTTP_PORT='9090' apache/nifi:latest
docker exec nifi mkdir /home/nifi/hdfs_conf
docker cp ../spark/config/core-site.xml nifi:/home/nifi/hdfs_conf/core-site.xml
docker cp ../spark/config/core-site.xml nifi:/home/nifi/hdfs_conf/hdfs-site.xml
docker exec -u 0 nifi  chown nifi /home/nifi/hdfs_conf/core-site.xml
docker exec -u 0 nifi  chown nifi /home/nifi/hdfs_conf/hdfs-site.xml





