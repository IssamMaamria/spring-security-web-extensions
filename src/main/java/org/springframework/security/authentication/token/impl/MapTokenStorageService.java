package org.springframework.security.authentication.token.impl;

import org.springframework.security.authentication.token.TokenNotFoundException;
import org.springframework.security.authentication.token.TokenStorageService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.Assert.notNull;

/**
 * @author maamria
 *         <p>
 *         06/10/15.
 */
public class MapTokenStorageService implements TokenStorageService {

    private final Map<String, UserDetails> store;

    public MapTokenStorageService() {
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public UserDetails loadUserByToken(String tokenValue) throws TokenNotFoundException {
        UserDetails userDetails = store.get(tokenValue);
        if(userDetails == null){
            throw new TokenNotFoundException();
        }
        return userDetails;
    }

    @Override
    public void storeToken(String tokenValue, UserDetails principal) {
        notNull(tokenValue, "Token value cannot be null");
        notNull(principal, "Principal cannot be null");
        store.put(tokenValue, principal);
    }

    @Override
    public void removeToken(String tokenValue) throws TokenNotFoundException {
        notNull(tokenValue, "Token value cannot be null");
        UserDetails userDetails = store.remove(tokenValue);
        if(userDetails == null){
            throw new TokenNotFoundException();
        }
    }
}
