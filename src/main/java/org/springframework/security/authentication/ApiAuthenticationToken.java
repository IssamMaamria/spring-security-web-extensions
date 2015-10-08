package org.springframework.security.authentication;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author maamria
 *         <p>
 *         04/10/15.
 */
public class ApiAuthenticationToken extends AbstractAuthenticationToken {

    private final String accessToken;

    private final int expiration;

    private final Object principal;

    public ApiAuthenticationToken(String accessToken, int expiration, Object principal){
        super(null);
        this.accessToken = accessToken;
        this.expiration = expiration;
        this.principal = principal;
        this.setAuthenticated(false);
    }

    public ApiAuthenticationToken(String accessToken, int expiration, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accessToken = accessToken;
        this.expiration = expiration;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiration() {
        return expiration;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
}
