package org.springframework.security;

/**
 * @author maamria
 *         <p>
 *         07/10/15.
 */
public interface ApiSecurityConfig {

    String loginEndpoint();

    String validationEndpoint();

    String logoutEndpoint();
}
