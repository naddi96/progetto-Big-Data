docker run -d --network=mycluster -p 16000:16000 -p 2181:2181 -p 16010:16010 --name=hbase-docker -h hbase-docker harisekhon/hbase:1.4
sleep 30
sudo docker exec hbase-docker /bin/bash -c "echo \"create 'covidIta', '19Ita'\" | /hbase/bin/hbase shell -n"
sudo docker exec hbase-docker /bin/bash -c "echo \"create 'covidWor', '19Wor'\" | /hbase/bin/hbase shell -n"

