package com.iflytek.maple.model;

import org.apache.avro.reflect.Nullable;

public class UserProfile {
	@Nullable
	public String uid;
	@Nullable
	public String gender;
	public int age;
	@Nullable
	public String address;
}
