package com.kindminds.drs.api.data.access.usecase;

public interface UpdateUserProfileDao {
	public boolean changeUserPassword(String userName, String newPassword);
}
