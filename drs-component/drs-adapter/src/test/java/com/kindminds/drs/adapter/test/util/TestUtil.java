package com.kindminds.drs.adapter.test.util;

public class TestUtil {
	public static <T> boolean nullableEquals(T a, T b){
		if(a==null&&b==null) return true;
		return a.equals(b);
	}
}
