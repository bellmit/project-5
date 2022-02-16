package com.kindminds.drs.security;


import com.kindminds.drs.UserInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SecurityUser extends org.springframework.security.core.userdetails.User {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private UserInfo userInfo;

    public UserInfo getDRSUser() {
        return userInfo;
    }

    public void setUser(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public SecurityUser(String username, String password, boolean enabled,
                        boolean accountNonExpired, boolean credentialsNonExpired,
                        boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities)
            throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
    }


}