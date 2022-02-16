package com.kindminds.drs.api.data.access.rdb;

import java.util.List;

import com.kindminds.drs.UserInfo;
import com.kindminds.drs.api.v1.model.user.UserCredential;

public interface UserDao {
	public abstract UserCredential getUserCredential(String name);
	public abstract UserInfo findUserByUserID(String userName);

	public abstract List<String> getUserRoles(String userName);
	public abstract boolean isSupplier(int userId);
	public abstract boolean isDrsUser(int userId);
	public abstract String queryUserMail(int userId);

	List<UserInfo> findUserByRole(String role);
}
