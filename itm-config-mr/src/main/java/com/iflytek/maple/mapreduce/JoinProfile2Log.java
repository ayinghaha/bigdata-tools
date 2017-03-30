package com.iflytek.maple.mapreduce;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.iflytek.avro.io.UnionData;
import com.iflytek.maple.model.UserClickLog;
import com.iflytek.maple.model.UserLogDetail;
import com.iflytek.maple.model.UserProfile;

/**
 * @author kim
 * @date 2012-11-11
 */
public class JoinProfile2Log {

	public static final Log LOG = LogFactory.getLog(JoinProfile2Log.class);

	public static class M extends Mapper<String, Object, String, UnionData> {

		@Override
		protected void map(String key, Object value, Context context)
				throws IOException, InterruptedException {
			context.write(key, new UnionData(value));
		}

	}

	public static class R extends
			Reducer<String, UnionData, String, UserLogDetail> {

		@Override
		protected void reduce(String key, Iterable<UnionData> values,
				Context context) throws IOException, InterruptedException {
			UserLogDetail uld = null;
			for (UnionData value : values) {
				if (uld == null)
					uld = new UserLogDetail();
				if (value.datum instanceof UserClickLog)
					uld.ucl = (UserClickLog) value.datum;
				else if (value.datum instanceof UserProfile)
					uld.upf = (UserProfile) value.datum;
			}
			context.write(key, uld);
		}
	}

}
