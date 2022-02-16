package com.kindminds.drs.persist.v1.model.mapping.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;





import com.kindminds.drs.UserInfo;
import org.springframework.security.core.GrantedAuthority;


public class UserInfoImpl implements UserInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7726030500857250919L;
	//@Id ////@Column(name="id")
	private int id;
	//@Column(name="user_name")
	private String userName;
	//@Column(name="locale")
	private String locale;
	//@Column(name="user_enabled")
	private boolean enabled;
	//@Column(name="user_display_name")
	private String displayName;
	//@Column(name="company_kcode")
	private String companyKcode;
	//@Column(name="is_drs_company")
	private Boolean isDrsCompany;
	//@Column(name="is_supplier")
	private Boolean isSupplier;
	

	private List<GrantedAuthority> authorities;

	public UserInfoImpl() {
	}

	public UserInfoImpl(int id, String userName, String locale,
						boolean enabled, String displayName, String companyKcode,
						Boolean isDrsCompany, Boolean isSupplier) {
		this.id = id;
		this.userName = userName;
		this.locale = locale;
		this.enabled = enabled;
		this.displayName = displayName;
		this.companyKcode = companyKcode;
		this.isDrsCompany = isDrsCompany;
		this.isSupplier = isSupplier;

	}


	@Override
	public int getUserId() {
		return this.id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void addAuthority(GrantedAuthority authority) {
		if (authorities == null)
			authorities = new ArrayList<GrantedAuthority>();
		authorities.add(authority);
	}

	@Override
	public String getUserDisplayName() {
		return displayName;
	}
	
	@Override
	public Locale getLocale() {		
		return Locale.forLanguageTag(this.locale);
	}

	@Override
	public String getCompanyKcode() {
		return this.companyKcode;
	}

	@Override
	public Boolean isDrsUser() {
		return this.isDrsCompany;
	}

	@Override
	public Boolean isSupplier() {
		return this.isSupplier;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setIsEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setCompanyKcode(String companyKcode) {
		this.companyKcode = companyKcode;
	}

	public void setIsDrsCompany(Boolean drsCompany) {
		isDrsCompany = drsCompany;
	}

	public void setIsSupplier(Boolean supplier) {
		isSupplier = supplier;
	}
}
