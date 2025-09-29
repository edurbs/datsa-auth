package com.github.edurbs.datsa.auth.core;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.edurbs.datsa.auth.domain.MyUser;
import com.github.edurbs.datsa.auth.domain.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new AuthUser(myUser, getAuthorities(myUser));
    }

    private Collection<GrantedAuthority> getAuthorities(MyUser myUser){
        return myUser.getGroups().stream()
            .flatMap(group -> group.getPermissions().stream())
            .map(permission -> new SimpleGrantedAuthority(permission.getName().toUpperCase()))
            .collect(Collectors.toSet());
    }
    
}
