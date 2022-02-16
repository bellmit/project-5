package com.kindminds.drs.api.usecase;

public interface UpdateUserProfileUco {
	public boolean changeUserPassword(String userName, String newPassword);
}
