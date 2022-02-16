package com.kindminds.drs.persist.v1.model.mapping.user;

import java.io.Serializable;









import com.kindminds.drs.api.v1.model.user.UserCredential;

/*
@SqlResultSetMappings({
	 @SqlResultSetMapping(name="userCredential",
	   entities={
	    Result(entityClass=UserCredentialImpl.class)} ,
	    columns={//@ColumnResult(name="password")
	 })
})
 */
public class UserCredentialImpl implements UserCredential, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6829317277747654810L;
	
	//@Id
	//@Column(name = "user_password")
	private String password;

	public UserCredentialImpl(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

}
