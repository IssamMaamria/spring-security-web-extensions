package org.springframework.security.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ApiAuthenticationToken;
import org.springframework.security.authentication.token.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author maamria
 *         <p>
 *         06/10/15.
 */
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    private static final Logger log = LoggerFactory.getLogger(ApiAuthenticationSuccessHandler.class);

    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("Authentication succeeded, sending response with api token");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Pragma", "no-cache");
        tokenService.renderToken(response, (ApiAuthenticationToken) authentication);

    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
