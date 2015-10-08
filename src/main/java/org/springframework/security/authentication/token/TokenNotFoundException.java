package org.springframework.security.authentication.token;

import org.springframework.security.core.AuthenticationException;

/**
 * Indicates that a token could not be found as expected.
 *
 * @author maamria
 *         <p>
 *         06/10/15.
 */
public class TokenNotFoundException extends AuthenticationException {

    public TokenNotFoundException() {
        super("Token not found");
    }
}
