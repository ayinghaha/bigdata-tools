package com.iflytek.maple.mapreduce;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.iflytek.maple.model.UserProfile;

/**
 * @author kim
 * @date 2012-11-11
 */
public class UserProfile2Avro {
  
  public static final Log LOG = LogFactory.getLog(UserProfile2Avro.class);
  
  public static class M extends Mapper<LongWritable,Text,String,UserProfile> {
    
    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
    	String[] logs = value.toString().split(",");
    	
    	UserProfile upf = new UserProfile();
    	upf.uid = logs[0];
    	upf.gender = logs[1];
    	upf.age = Integer.parseInt(logs[2]);
    	upf.address = logs[3];
    	context.write(upf.uid, upf);
    }
    
  }
  
  public static class R extends Reducer<String,UserProfile,String,UserProfile> {
    
    @Override
    protected void reduce(String key, Iterable<UserProfile> values,
        Context context) throws IOException, InterruptedException {
    	for(UserProfile upf :values)
    	{
    		context.write(key, upf);
    		break;
    	}
    }
  }
  
}
