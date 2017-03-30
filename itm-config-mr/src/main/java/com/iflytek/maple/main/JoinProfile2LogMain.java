package com.iflytek.maple.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import com.iflytek.avro.io.UnionData;
import com.iflytek.avro.mapreduce.AvroJob;
import com.iflytek.avro.mapreduce.input.AvroPairInputFormat;
import com.iflytek.avro.mapreduce.output.AvroMapOutputFormat;
import com.iflytek.maple.mapreduce.JoinProfile2Log;
import com.iflytek.maple.model.UserClickLog;
import com.iflytek.maple.model.UserLogDetail;
import com.iflytek.maple.model.UserProfile;
import com.iflytek.oozie.main.OozieMain;
import com.iflytek.oozie.main.OozieToolRunner;

public class JoinProfile2LogMain extends OozieMain implements Tool {

	public static final Log LOG = LogFactory.getLog(JoinProfile2LogMain.class);

	@Override
	public int run(String[] args) throws Exception {
		if (args == null || args.length < 3) {
			System.out.println("Usage: <log> <profile> <output>");
			throw new IllegalArgumentException("this method need three args!");
		}
		mapreduceRun(args[0], args[1], args[2]);
		return 0;
	}

	public void mapreduceRun(String log, String profile, String output)
			throws Exception {
		AvroJob job = AvroJob.getAvroJob(getConf());
		job.setJobName(JoinProfile2LogMain.class.getName() + ":" + log + "-"
				+ profile + "-" + output);
		job.setInputFormatClass(AvroPairInputFormat.class);
		job.setMapperClass(JoinProfile2Log.M.class);
		job.setReducerClass(JoinProfile2Log.R.class);
		FileInputFormat.addInputPath(job, new Path(log));
		FileInputFormat.addInputPath(job, new Path(profile));

		FileOutputFormat.setOutputPath(job, new Path(output));
		UnionData.setParseClass(job, new Class[] { UserClickLog.class,
				UserProfile.class });
		job.setMapOutputKeyClass(String.class);
		job.setMapOutputValueClass(UnionData.class);
		job.setOutputKeyClass(String.class);
		job.setOutputValueClass(UserLogDetail.class);

		job.setOutputFormatClass(AvroMapOutputFormat.class);
		if (runJob(job)) {
		} else {
			throw new Exception("job faild, please look in for detail.");
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(org.apache.commons.lang.StringUtils.join(args));
		int res = OozieToolRunner.run(new Configuration(),
				new JoinProfile2LogMain(), args);
		System.exit(res);
	}
}