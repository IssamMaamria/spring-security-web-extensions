package org.springframework.security.impl;

import org.springframework.security.ApiSecurityConfig;

/**
 * @author maamria
 *         <p>
 *         07/10/15.
 */
public class DefaultApiSecurityConfig implements ApiSecurityConfig {

    @Override
    public String loginEndpoint() {
        return "/api/login";
    }

    @Override
    public String validationEndpoint() {
        return "/api/validate";
    }

    @Override
    public String logoutEndpoint() {
        return "/api/logout";
    }
}
