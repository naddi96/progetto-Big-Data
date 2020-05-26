package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

public class HDFS {


    public static void saveStringTohdfs(String hdfs_master, String filename, String content, String folder) throws URISyntaxException, IOException {

        Configuration configuration = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(hdfs_master), configuration);
        Path file = new Path(hdfs_master+"/" + folder + "/" + filename);
        Path fold = new Path(hdfs_master +"/"+ folder);

        if (!hdfs.exists(fold)) {
            hdfs.mkdirs(fold);
        }

        if (hdfs.exists(file)) {
            hdfs.delete(file, true);
        }

        OutputStream os = hdfs.create(file);
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        br.write(content);
        br.close();
        hdfs.close();
    }
}
