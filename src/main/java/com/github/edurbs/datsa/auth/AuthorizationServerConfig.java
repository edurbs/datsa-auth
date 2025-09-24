package com.github.edurbs.datsa.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@SuppressWarnings("deprecation")
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
        clients
            .inMemory()
                .withClient("datsa-web") // client identifier
                .secret(passwordEncoder.encode("123")) // password
                .authorizedGrantTypes("password", "refresh_token") // password with refresh token flow
                .scopes("write", "read")
                .accessTokenValiditySeconds(60*15) // 15 minutes to access token expiration
                .refreshTokenValiditySeconds(60*60*12) // 12 hours refresh token
            .and()
                .withClient("other-backend") 
                .secret(passwordEncoder.encode("123")) 
                .authorizedGrantTypes("client_credentials") // client without user flow
                .scopes("write", "read")
            .and()
                .withClient("datsa-analytics") // client identifier
                .secret(passwordEncoder.encode("")) // password
                .authorizedGrantTypes("authorization_code") // authorization code flow with PKCE support
                .scopes("write", "read")
                .redirectUris("http://localhost:8081") // in production must be https and use real client URL
            .and()
                .withClient("webadmin") // client identifier
                .authorizedGrantTypes("implicit") // implicit authorization flow
                .scopes("write", "read")
                .redirectUris("http://localhost") 
            .and()
                .withClient("checktoken")
                .secret(passwordEncoder.encode("web123"));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.checkTokenAccess("isAuthenticated()"); // allow only with basic authentication
        security.checkTokenAccess("permitAll()") // allow all without any authentication
            .allowFormAuthenticationForClients(); // allow client_id in body without basic auth.
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .authenticationManager(authenticationManager) // only the password flow needs this
            .userDetailsService(userDetailsService)
            .reuseRefreshTokens(false) // must not reuse refresh tokens
            .accessTokenConverter(jwtAccessTokenConverter()) // generate JWT tokens (instead of opaque tokens)
            .tokenGranter(tokenGranter(endpoints));
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("secretKey");
        return jwtAccessTokenConverter;
    }

	private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
				endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory());
		
		var granters = Arrays.asList(
				pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());
		
		return new CompositeTokenGranter(granters);
	}

}
