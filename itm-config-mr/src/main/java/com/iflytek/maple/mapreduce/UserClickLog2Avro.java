package com.iflytek.maple.mapreduce;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.iflytek.maple.model.UserClickLog;

/**
 * @author kim
 * @date 2012-11-11
 */
public class UserClickLog2Avro {
  
  public static final Log LOG = LogFactory.getLog(UserClickLog2Avro.class);
  
  public static class M extends Mapper<LongWritable,Text,String,UserClickLog> {
    
    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
    	String[] logs = value.toString().split(",");
    	
    	UserClickLog ucl = new UserClickLog();
    	ucl.uid = logs[0];
    	ucl.channel = logs[1];
    	ucl.adid = logs[2];
    	
    	context.write(ucl.uid, ucl);
    }
    
  }
  
  public static class R extends Reducer<String,UserClickLog,String,UserClickLog> {
    
    @Override
    protected void reduce(String key, Iterable<UserClickLog> values,
        Context context) throws IOException, InterruptedException {
    	for(UserClickLog ucl :values)
    	{
    		context.write(key, ucl);
    		break;
    	}
    }
  }
  
}
