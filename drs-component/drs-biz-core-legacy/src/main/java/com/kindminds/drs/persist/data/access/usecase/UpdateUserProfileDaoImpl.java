package com.kindminds.drs.persist.data.access.usecase;

import com.kindminds.drs.api.data.access.usecase.UpdateUserProfileDao;
import org.springframework.transaction.annotation.Transactional;




public class UpdateUserProfileDaoImpl implements UpdateUserProfileDao {
	
	//@PersistenceContext(unitName="casApplication") private EntityManager entityManager;

	@Override @Transactional("transactionManagerCas")
	public boolean changeUserPassword(String userName, String newPassword) {
		/*
		String sql = "update user_info set password = :pswd where username = :userName ";
		Query q = this.entityManager.createNativeQuery(sql);
		q.setParameter("userName", userName);
		q.setParameter("pswd", newPassword);
		q.executeUpdate();
		*/
		return true;
	}

}
