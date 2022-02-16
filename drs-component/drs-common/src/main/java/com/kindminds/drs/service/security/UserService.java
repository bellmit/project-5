package com.kindminds.drs.service.security;

import java.util.List;

import com.kindminds.drs.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kindminds.drs.security.SecurityUser;
import com.kindminds.drs.api.data.access.rdb.UserDao;

@Repository("userService")
public class UserService implements UserDetailsService {
	
	@Autowired
	@Qualifier("UserDao")
	private UserDao repo;

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	
    	UserInfo user = repo.findUserByUserID(username);
    	
    	if (user == null)
    		throw new UsernameNotFoundException("User " + username
                + " has no GrantedAuthority");
    	List<String> roles = repo.getUserRoles(username);
    	
    	//to create Authorities
    	UserBuilder builder = null;
		builder = org.springframework.security.core.userdetails.User.withUsername(username);
		builder.password(new BCryptPasswordEncoder().encode("123456"));
		builder.roles(roles.toArray(new String[0]));
		UserDetails springUser = builder.build();
		//====
    	
		SecurityUser securityUser = new SecurityUser(username , 
				springUser.getPassword() ,
				true,true,true,true , springUser.getAuthorities());

		securityUser.setUser(user);
		
		return securityUser;
    }
}