package org.springframework.security.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.ApiSecurityConfig;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.token.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
 *         05/10/15.
 */
public class ApiAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthenticationFilter.class);

    private ApiSecurityConfig apiSecurityConfig;

    private TokenService tokenService;

    private CredentialsExtractor credentialsExtractor;

    private ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler;

    private ApiAuthenticationFailureHandler apiAuthenticationFailureHandler;

    private AuthenticationManager authenticationManager;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String actualURI = httpServletRequest.getRequestURI().replace(httpServletRequest.getContextPath(), "");
        if(apiSecurityConfig.loginEndpoint().equals(actualURI)){
            if(!"POST".equalsIgnoreCase(httpServletRequest.getMethod())){
                log.debug("{} HTTP method is not supported. Setting status to {}", httpServletRequest.getMethod(),
                        HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                httpServletResponse.setStatus (HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
            else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Authentication authenticationResult = null;
                UsernamePasswordAuthenticationToken authenticationRequest = credentialsExtractor.extractCredentials(httpServletRequest);
                if(authenticationRequest != null && authenticationRequest.getPrincipal() != null && authenticationRequest.getCredentials() != null){
                    authenticationRequest.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));
                    try {
                        authenticationResult = authenticationManager.authenticate(authenticationRequest);
                        if(authenticationResult.isAuthenticated()) {
                            log.debug("Request authenticated. Storing the authentication result in the security context");
                            log.debug("Authentication result: {}", authenticationResult);
                            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                        }
                    }
                    catch (AuthenticationException ex){
                        log.debug("Authentication failed: {}", ex.getMessage());
                        apiAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, ex);
                    }
                }
                else {
                    log.debug("Username and/or password parameters are missing.");
                    if(authentication == null){
                        log.debug("Setting status to {}", HttpServletResponse.SC_BAD_REQUEST);
                        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    else {
                        log.debug("Using authentication already in security context.");
                        authenticationResult = authentication;
                    }
                }
                if(authenticationResult != null && authenticationResult.isAuthenticated()){
                    UserDetails principal = (UserDetails) authenticationResult.getPrincipal();
                    ApiAuthenticationToken apiToken = tokenService.generateAccessToken(principal, -1);
                    log.debug("Generated token: {}", apiToken);
                    tokenService.storeToken(apiToken.getAccessToken(), principal);
                    apiAuthenticationSuccessHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, apiToken);
                }
                else {
                    log.debug("Not authenticated. API authentication token not generated.");
                }
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }

    public void setApiSecurityConfig(ApiSecurityConfig apiSecurityConfig) {
        this.apiSecurityConfig = apiSecurityConfig;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setApiAuthenticationSuccessHandler(ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler) {
        this.apiAuthenticationSuccessHandler = apiAuthenticationSuccessHandler;
    }

    public void setApiAuthenticationFailureHandler(ApiAuthenticationFailureHandler apiAuthenticationFailureHandler) {
        this.apiAuthenticationFailureHandler = apiAuthenticationFailureHandler;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setCredentialsExtractor(CredentialsExtractor credentialsExtractor) {
        this.credentialsExtractor = credentialsExtractor;
    }
}
