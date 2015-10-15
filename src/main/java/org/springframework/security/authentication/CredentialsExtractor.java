package org.springframework.security.authentication;

import javax.servlet.ServletRequest;

/**
 * @author maamria
 *         <p>
 *         15/10/15.
 */
public interface CredentialsExtractor {

    /**
     * Extracts a {@link UsernamePasswordAuthenticationToken} from the request.
     * @param servletRequest request
     * @return {@link UsernamePasswordAuthenticationToken}
     * @throws BadCredentialsException, if token cannot be extracted
     */
    UsernamePasswordAuthenticationToken extractCredentials(ServletRequest servletRequest) throws BadCredentialsException;
}
