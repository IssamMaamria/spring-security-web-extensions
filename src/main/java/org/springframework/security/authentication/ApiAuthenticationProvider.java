package org.springframework.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.token.TokenNotFoundException;
import org.springframework.security.authentication.token.TokenStorageService;
import org.springframework.security.authentication.token.impl.MapTokenStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * @author maamria
 *         <p>
 *         05/10/15.
 */
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(ApiAuthenticationProvider.class);

    private TokenStorageService tokenStorageService = new MapTokenStorageService();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(ApiAuthenticationToken.class, authentication);
        ApiAuthenticationToken authenticationToken = (ApiAuthenticationToken) authentication;
        String accessToken = authenticationToken.getAccessToken();
        if(accessToken != null){
            logger.debug("Attempting to validate token {}", accessToken);
            UserDetails userDetails = tokenStorageService.loadUserByToken(accessToken);
            if(userDetails != null){
                return new ApiAuthenticationToken(accessToken, authenticationToken.getExpiration(), userDetails);
            }
        }
        throw new TokenNotFoundException();
    }

    public void setTokenStorageService(TokenStorageService tokenStorageService) {
        this.tokenStorageService = tokenStorageService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthenticationToken.class.equals(authentication);
    }
}
