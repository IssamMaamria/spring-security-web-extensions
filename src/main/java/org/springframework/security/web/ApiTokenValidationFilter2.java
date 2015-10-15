package org.springframework.security.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.ApiSecurityConfig;
import org.springframework.security.authentication.ApiAuthenticationToken;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.token.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.impl.DefaultApiSecurityConfig;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author maamria
 *         <p>
 *         07/10/15.
 */
public class ApiTokenValidationFilter2 extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(ApiTokenValidationFilter2.class);

    private ApiSecurityConfig apiSecurityConfig = new DefaultApiSecurityConfig();

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    private AuthenticationSuccessHandler authenticationSuccessHandler;

    private AuthenticationFailureHandler authenticationFailureHandler;

    private AuthenticationEventPublisher authenticationEventPublisher;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ApiAuthenticationToken accessToken = null;
        try {
            accessToken = tokenService.extractAccessToken(httpRequest);
            if(accessToken != null){
                log.debug("Access token found: {}", accessToken.getAccessToken());
                Authentication authenticatedToken = authenticationManager.authenticate(accessToken);
                if(authenticatedToken.isAuthenticated()){
                    log.debug("Token is authenticated");
                    SecurityContextHolder.getContext().setAuthentication(accessToken);
                    authenticationEventPublisher.publishAuthenticationSuccess(authenticatedToken);
                    processFilterChain(httpRequest, httpResponse, chain, (ApiAuthenticationToken) authenticatedToken);
                }
            }
            else {
                processFilterChain(httpRequest, httpResponse, chain, null);
            }
        } catch (AuthenticationException e){
            authenticationEventPublisher.publishAuthenticationFailure(e, accessToken);
            authenticationFailureHandler.onAuthenticationFailure(httpRequest, httpResponse, e);
        }
    }

    public void setApiSecurityConfig(ApiSecurityConfig apiSecurityConfig) {
        this.apiSecurityConfig = apiSecurityConfig;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public void setAuthenticationEventPublisher(AuthenticationEventPublisher authenticationEventPublisher) {
        this.authenticationEventPublisher = authenticationEventPublisher;
    }

    private void processFilterChain(HttpServletRequest request, HttpServletResponse response, FilterChain chain, ApiAuthenticationToken authenticationResult) throws IOException, ServletException {
        String actualUri = request.getRequestURI().replace(request.getContextPath(), "");
        if(authenticationResult != null &&  authenticationResult.getAccessToken() != null){
            if(apiSecurityConfig.validationEndpoint().equals(actualUri)){
                log.debug("Validation endpoint called. Generating response.");
                authenticationSuccessHandler.onAuthenticationSuccess(request, response, authenticationResult);
            }
            else {
                log.debug("Continuing the filter chain");
                chain.doFilter(request, response);
            }
        } else {
            log.debug("Request does not contain any token. Letting it continue through the filter chain");
            chain.doFilter(request, response);
        }
    }
}
