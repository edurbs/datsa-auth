package com.github.edurbs.datsa.auth;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Validated
@Component
@ConfigurationProperties("datsa.jwt.keystore")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class JwtKeyStoreProperties {

    @NotBlank
    String path;

    @NotBlank
    String password;

    @NotBlank
    String keypairAlias;

}
