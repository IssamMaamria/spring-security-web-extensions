package org.springframework.security.authentication.token;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author maamria
 *         <p>
 *         05/10/15.
 */
public interface TokenStorageService {

    UserDetails loadUserByToken(String tokenValue) throws TokenNotFoundException;

    void storeToken(String tokenValue, UserDetails principal);

    void removeToken(String tokenValue) throws TokenNotFoundException;
}
