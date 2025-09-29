package com.github.edurbs.datsa.auth.core;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class AuthUser extends User {
    private String fullName;
    private String email;
    private Long userId;
    public AuthUser(com.github.edurbs.datsa.auth.domain.User myUser){
        super(myUser.getEmail(), myUser.getPassword(), Collections.emptyList());
        this.fullName = myUser.getName();
        this.userId = myUser.getId();
    }
}
