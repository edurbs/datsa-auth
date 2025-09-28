package com.github.edurbs.datsa.auth.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.edurbs.datsa.auth.domain.User;
import com.github.edurbs.datsa.auth.domain.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new AuthUser(myUser);
    }
    
}
