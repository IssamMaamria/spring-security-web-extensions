package org.springframework.security.authentication.token.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.ApiAuthenticationToken;
import org.springframework.security.authentication.token.TokenGenerationService;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author maamria
 *         <p>
 *         07/10/15.
 */
public class SecureRandomTokenGenerationService implements TokenGenerationService {

    private final SecureRandom random;

    public SecureRandomTokenGenerationService() {
        this.random = new SecureRandom();
    }

    @Override
    public ApiAuthenticationToken generateAccessToken(UserDetails principal, int expiration) {
        String token = new BigInteger(160, this.random).toString(32);
        int tokenSize = token.length();
        if (tokenSize < 32) {
            token += RandomStringUtils.randomAlphanumeric(32 - tokenSize);
        }
        return new ApiAuthenticationToken(token, expiration, principal);
    }
}
