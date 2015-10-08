package org.springframework.security;

/**
 * @author maamria
 *         <p>
 *         07/10/15.
 */
public interface ApiSecurityConfig {

    String authHeaderName();

    String usernamePropertyName();

    String passwordPropertyName();

    String loginEndpoint();

    String validationEndpoint();
}
