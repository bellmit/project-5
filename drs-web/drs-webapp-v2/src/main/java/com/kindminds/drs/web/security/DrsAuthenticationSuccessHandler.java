package com.kindminds.drs.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.kindminds.drs.service.util.SpringAppCtx;

@Component
public class DrsAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	/**
	 * RequestCahe
	 */
    private RequestCache requestCache = new HttpSessionRequestCache();
    /**
     * Redirect Strategy
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override @SuppressWarnings("unused")
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
		UserDetailsService uService = (UserDetailsService) SpringAppCtx.get().getBean("userService");
		UserDetails user = uService.loadUserByUsername(authentication.getName());
		SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            return;
        }
        
       String defaultTarget = savedRequest.getRedirectUrl();
       redirectStrategy.sendRedirect(request, response,defaultTarget );
	}

}
