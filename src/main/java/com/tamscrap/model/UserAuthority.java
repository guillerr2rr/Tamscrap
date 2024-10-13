package com.tamscrap.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {
    ADMIN, USER, ANONYMOUS;

    @Override
    public String getAuthority() {
        return this.name();   
    }
}
