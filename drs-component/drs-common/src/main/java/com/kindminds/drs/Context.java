package com.kindminds.drs;

import com.kindminds.drs.security.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;



public class Context {

	/**
	 *
	 * @return User界面
	 */
	public static UserInfo getCurrentUser(){

		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null){
			if( !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){

				UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				return ((SecurityUser)userDetails).getDRSUser();
			}
		}



		return null;
	}

}