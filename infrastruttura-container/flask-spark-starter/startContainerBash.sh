docker rm spark-starter
docker run -it  --volume "$PWD":/myapp/ --network=mycluster --name spark-starter compute-starter /bin/bash
