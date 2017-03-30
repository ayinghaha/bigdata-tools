package com.iflytek.maple.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import com.iflytek.avro.mapreduce.AvroJob;
import com.iflytek.avro.mapreduce.output.AvroMapOutputFormat;
import com.iflytek.maple.mapreduce.UserProfile2Avro;
import com.iflytek.maple.model.UserProfile;
import com.iflytek.oozie.main.OozieMain;
import com.iflytek.oozie.main.OozieToolRunner;

public class UserProfile2AvroMain extends OozieMain implements Tool {

	public static final Log LOG = LogFactory
			.getLog(UserProfile2AvroMain.class);

	@Override
	public int run(String[] args) throws Exception {
		if (args == null || args.length < 2) {
			System.out.println("Usage: <input> <output>");
			throw new IllegalArgumentException("this method need two args!");
		}
		mapreduceRun(args[0], args[1]);
		return 0;
	}

	public void mapreduceRun(String input, String output) throws Exception {
		AvroJob job = AvroJob.getAvroJob(getConf());
		job.setJobName(UserProfile2AvroMain.class.getName() + ":" + input
				+ "-" + output);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(UserProfile2Avro.M.class);
		job.setReducerClass(UserProfile2Avro.R.class);
		FileInputFormat.addInputPath(job, new Path(input));

		FileOutputFormat.setOutputPath(job, new Path(output));

		job.setOutputKeyClass(String.class);
		job.setOutputValueClass(UserProfile.class);

		job.setOutputFormatClass(AvroMapOutputFormat.class);
		if (runJob(job)) {
		} else {
			throw new Exception("job faild, please look in for detail.");
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(org.apache.commons.lang.StringUtils.join(args));
		int res = OozieToolRunner.run(new Configuration(),
				new UserProfile2AvroMain(), args);
		System.exit(res);
	}
}