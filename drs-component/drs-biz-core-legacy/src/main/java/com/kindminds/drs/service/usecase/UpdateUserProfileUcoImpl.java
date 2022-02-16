package com.kindminds.drs.service.usecase;

import com.kindminds.drs.api.usecase.UpdateUserProfileUco;
import com.kindminds.drs.service.util.Encrypter;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Service("updateUserProfileUco")
public class UpdateUserProfileUcoImpl implements UpdateUserProfileUco {
	
	//@Autowired private UpdateUserProfileDao dao;

	@Override
	public boolean changeUserPassword(String userName, String newPassword) {
		try {
			String encPassword = Encrypter.enrypt(newPassword);
			//return this.dao.changeUserPassword(userName, encPassword);
			return false;
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

}
