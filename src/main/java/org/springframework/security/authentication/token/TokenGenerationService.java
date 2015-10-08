package org.springframework.security.authentication.token;

import org.springframework.security.authentication.ApiAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author maamria
 *         <p>
 *         06/10/15.
 */
public interface TokenGenerationService {

    ApiAuthenticationToken generateAccessToken(UserDetails principal, int expiration);

}
