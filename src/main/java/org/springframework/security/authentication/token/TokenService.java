package org.springframework.security.authentication.token;

import org.springframework.security.authentication.ApiAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * A service managing access tokens {@link ApiAuthenticationToken}. It provides facilities
 * to generate, extract, store and remove access tokens.
 *
 * @author maamria
 *         <p>
 *         13/10/15.
 */
public interface TokenService {

    /**
     * Generates an {@link ApiAuthenticationToken} for the principal.
     * @param principal principal, not null
     * @param expiration expiry
     * @return token
     */
    ApiAuthenticationToken generateAccessToken(UserDetails principal, int expiration);

    /**
     * Extracts an {@link ApiAuthenticationToken} from the request.
     * @param request request, not null
     * @return access token
     * @throws TokenNotFoundException if token does not exist in request
     */
    ApiAuthenticationToken extractAccessToken(HttpServletRequest request) throws TokenNotFoundException;

    /**
     * Loads a {@link UserDetails} corresponding to the access token.
     * @param tokenValue access token, not null
     * @return corresponding {@link UserDetails}
     * @throws TokenNotFoundException if token not found
     */
    UserDetails loadUserByToken(String tokenValue) throws TokenNotFoundException;

    /**
     * Stores the token for the given principal.
     * @param tokenValue access token, not null
     * @param principal principal, not null
     */
    void storeToken(String tokenValue, UserDetails principal);

    /**
     * Removes the token.
     * @param tokenValue access token, not null
     * @throws TokenNotFoundException if the token cannot be found
     */
    void removeToken(String tokenValue) throws TokenNotFoundException;

    /**
     * Renders token, and writes it to the response. This should strive to adhere to content negotation.
     * @param response response
     * @param apiAuthenticationToken token
     */
    void renderToken(ServletResponse response, ApiAuthenticationToken apiAuthenticationToken) throws AuthenticationException;
}
