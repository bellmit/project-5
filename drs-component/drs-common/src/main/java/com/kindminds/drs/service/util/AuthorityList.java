package com.kindminds.drs.service.util;

import java.util.Properties;

public class AuthorityList {
	private static AuthorityList singleton;
	
	public static String auth(String type) {
		if (singleton == null)
			singleton = new AuthorityList();
		String val = singleton.getAuthList(type);
		return val;
	}
	
	private Properties auth = null;
	
	private AuthorityList() {
		auth = (Properties) SpringAppCtx.get().getBean("authProperties");
	}
	
	public String getAuthList(String type) {
		return auth.get(type).toString();
	}

}
