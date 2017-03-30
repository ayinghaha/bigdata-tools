package com.iflytek.maple.assist;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class GenerateMRData {
  public static void main(String[] args) throws IOException {
    FileSystem fs = FileSystem.get(new Configuration());
    String ulogPath = "mapleDemoAutoData/ulog.txt";
    String uprofilePath = "mapleDemoAutoData/uprofile.txt";
    
    FSDataOutputStream ulog = fs.create(new Path(ulogPath));
    for (int i = 0; i < 100000; i++) {
      ulog.writeBytes(String.format("u%06d,runbox%06d,madhouse%06d\n", i, i, i));
    }
    ulog.close();
    
    FSDataOutputStream uprofile = fs.create(new Path(uprofilePath));
    for (int i = 0; i < 100000; i++) {
      uprofile
          .writeBytes(String.format("u%06d,%s,%d,city%04d\n", i,
              Math.random() * 2 > 1 ? "male" : "female",
              ((int) (Math.random() * 80 + 1)),
              ((int) (Math.random() * 4000 + 1))));
    }
    uprofile.close();
    
    System.out.println("\nGenerate Over! \nulog Path: " + ulogPath
        + "\nuprofile Path: " + uprofilePath + "\n");
  }
}
