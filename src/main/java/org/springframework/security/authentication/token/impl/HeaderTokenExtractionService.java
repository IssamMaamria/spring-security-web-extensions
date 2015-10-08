package org.springframework.security.authentication.token.impl;

import org.springframework.security.ApiSecurityConfig;
import org.springframework.security.authentication.ApiAuthenticationToken;
import org.springframework.security.authentication.token.TokenExtractionService;
import org.springframework.security.impl.DefaultApiSecurityConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * @author maamria
 *         <p>
 *         08/10/15.
 */
public class HeaderTokenExtractionService implements TokenExtractionService {

    private ApiSecurityConfig apiSecurityConfig = new DefaultApiSecurityConfig();

    @Override
    public ApiAuthenticationToken extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(apiSecurityConfig.authHeaderName());
        return new ApiAuthenticationToken(header, -1, null);
    }

    public void setApiSecurityConfig(ApiSecurityConfig apiSecurityConfig) {
        this.apiSecurityConfig = apiSecurityConfig;
    }
}
