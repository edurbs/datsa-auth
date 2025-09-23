package com.github.edurbs.datsa.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("datsa-web") // client identifier
                .secret(passwordEncoder.encode("123")) // password
                .authorizedGrantTypes("password", "refresh_token") // authentication flow
                .scopes("write", "read")
                .accessTokenValiditySeconds(5) // 15 minutes to access token expiration
                .refreshTokenValiditySeconds(10) // 12 hours refresh token
	.and()
		.withClient("checktoken")
		.secret(passwordEncoder.encode("web123"));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.checkTokenAccess("isAuthenticated()"); // allow only with basic authentication
        security.checkTokenAccess("permitAll()"); // allow all without any authentication
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .authenticationManager(authenticationManager) // only the password flow needs this
            .userDetailsService(userDetailsService)
            .reuseRefreshTokens(false); // must not reuse refresh tokens
    }

}
