from flask import Flask
import os

#$SPARK_HOME/bin/spark-submit --deploy-mode cluster --class "Query1"  --master "spark://mycluster-master:7077" progetto1-1.0.jar
start_computation="$SPARK_HOME/bin/spark-submit --deploy-mode cluster --class \"Query1\"  --master \"spark://mycluster-master:7077\" progetto1-1.0.jar"
app = Flask(__name__)


@app.route('/query1')
def query1():
    os.system(start_computation)
    return "computation complete"

if __name__ == '__main__':
    from waitress import serve
    serve(app, host="0.0.0.0", port=5000)    