package org.springframework.security.impl;

import org.springframework.security.ApiSecurityConfig;

/**
 * @author maamria
 *         <p>
 *         07/10/15.
 */
public class DefaultApiSecurityConfig implements ApiSecurityConfig {

    @Override
    public String authHeaderName() {
        return "X-Auth-Token";
    }

    @Override
    public String usernamePropertyName() {
        return "username";
    }

    @Override
    public String passwordPropertyName() {
        return "password";
    }

    @Override
    public String loginEndpoint() {
        return "login";
    }

    @Override
    public String validationEndpoint() {
        return "validate";
    }
}
