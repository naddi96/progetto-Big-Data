FROM ubuntu:latest



COPY requirements.txt ./
RUN  apt-get -y update
RUN apt-get install -y openssh-server openjdk-8-jdk wget scala

RUN  apt-get -y install zip 
RUN  apt-get -y install vim
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64


RUN apt-get update \
  && apt-get install -y python3-pip python3-dev \
  && cd /usr/local/bin \
  && ln -s /usr/bin/python3 python \
  && pip3 install --upgrade pip


RUN pip3 install --no-cache-dir -r requirements.txt

WORKDIR /spark

RUN wget -O spark.tar.gz -q https://archive.apache.org/dist/spark/spark-2.4.1/spark-2.4.1-bin-hadoop2.7.tgz
RUN tar xfz spark.tar.gz

ENV SPARK_HOME=/spark/spark-2.4.1-bin-hadoop2.7

RUN wget https://repo1.maven.org/maven2/org/apache/hbase/hbase-client/1.4.2/hbase-client-1.4.2.jar
RUN mv  hbase-client-1.4.2.jar $SPARK_HOME/jars/


WORKDIR /myapp

CMD [ "python3", "./flasck_app.py" ]

