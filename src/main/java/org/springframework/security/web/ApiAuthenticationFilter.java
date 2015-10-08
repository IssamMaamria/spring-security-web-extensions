package org.springframework.security.web;

import org.springframework.security.ApiSecurityConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.token.TokenGenerationService;
import org.springframework.security.authentication.token.TokenStorageService;
import org.springframework.security.authentication.token.impl.SecureRandomTokenGenerationService;
import org.springframework.security.impl.DefaultApiSecurityConfig;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author maamria
 *         <p>
 *         05/10/15.
 */
public class ApiAuthenticationFilter extends GenericFilterBean {

    private ApiSecurityConfig apiSecurityConfig = new DefaultApiSecurityConfig();

    private TokenGenerationService tokenGenerationService = new SecureRandomTokenGenerationService();

    private TokenStorageService tokenStorageService;

    private ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler;

    private ApiAuthenticationFailureHandler apiAuthenticationFailureHandler;

    private AuthenticationManager authenticationManager;

    private WebAuthenticationDetailsSource authenticationDetailsSource;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    public void setApiSecurityConfig(ApiSecurityConfig apiSecurityConfig) {
        this.apiSecurityConfig = apiSecurityConfig;
    }

    public void setTokenGenerationService(TokenGenerationService tokenGenerationService) {
        this.tokenGenerationService = tokenGenerationService;
    }

    public void setTokenStorageService(TokenStorageService tokenStorageService) {
        this.tokenStorageService = tokenStorageService;
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

    public void setAuthenticationDetailsSource(WebAuthenticationDetailsSource authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
    }
}
