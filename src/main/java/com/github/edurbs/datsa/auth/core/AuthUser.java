package com.github.edurbs.datsa.auth.core;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.github.edurbs.datsa.auth.domain.MyUser;

import lombok.Getter;

@Getter
public class AuthUser extends User {
    private String fullName;
    private String email;
    private Long userId;
    public AuthUser(MyUser myUser, Collection<? extends GrantedAuthority> authorities){
        super(myUser.getEmail(), myUser.getPassword(), authorities);
        this.fullName = myUser.getName();
        this.userId = myUser.getId();
    }
}
