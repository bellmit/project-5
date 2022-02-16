package com.kindminds.drs;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Locale;

public interface UserInfo extends UserDetails {
	public abstract int getUserId();
	public abstract String getUserDisplayName();
	public abstract Locale getLocale();
	public abstract String getCompanyKcode();
	public abstract Boolean isDrsUser();
	public abstract Boolean isSupplier();
	public abstract void addAuthority(GrantedAuthority authority);
}
