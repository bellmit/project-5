package com.kindminds.drs.service.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.kindminds.drs.service.util.SpringAppCtx;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.kindminds.drs.api.v1.model.user.UserCredential;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.service.util.Encrypter;

@Component("drsAuthenticationProvider")
public class DrsAuthenticationProvider  implements AuthenticationProvider {
	
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
        	String encPassword = Encrypter.enrypt(password);
			System.out.println("name is " + name + ", password is " + password);
			UserDao repo = (UserDao) SpringAppCtx.get().getBean("UserDao");
			UserCredential uc = repo.getUserCredential(name);
			if (uc == null)
				throw new BadCredentialsException("Incorrect UserName or Password");
			if (!uc.getPassword().equals(encPassword))
				throw new BadCredentialsException("Incorrect UserName or Password");
			UserDetailsService uService = (UserDetailsService) SpringAppCtx.get().getBean("userService");
			UserDetails user = uService.loadUserByUsername(name);
			
			return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new BadCredentialsException("Incorrect UserName or Password");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new BadCredentialsException("Incorrect UserName or Password");
		}
	}

	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
