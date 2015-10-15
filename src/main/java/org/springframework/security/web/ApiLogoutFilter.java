package org.springframework.security.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.ApiSecurityConfig;
import org.springframework.security.authentication.ApiAuthenticationToken;
import org.springframework.security.authentication.token.TokenNotFoundException;
import org.springframework.security.authentication.token.TokenService;
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
public class ApiLogoutFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(ApiLogoutFilter.class);

    private ApiSecurityConfig apiSecurityConfig;

    private TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String actualUri =  servletRequest.getRequestURI().replace(servletRequest.getContextPath(), "");
        if(apiSecurityConfig.logoutEndpoint().equals(actualUri)){
            if(!"POST".equalsIgnoreCase(servletRequest.getMethod())){
                log.debug("{} HTTP method is not supported. Setting status to {}", servletRequest.getMethod(),
                        HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                servletResponse.setStatus (HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
            else {
                ApiAuthenticationToken accessToken = tokenService.extractAccessToken(servletRequest);
                if(accessToken != null){
                    log.debug("Token found: {}", accessToken.getAccessToken());
                    try{
                        log.debug("Trying to remove the token");
                        tokenService.removeToken(accessToken.getAccessToken());
                    } catch (TokenNotFoundException e){
                        servletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Token not found");
                    }
                } else {
                    log.debug("Token is missing. Sending a 400 Bad Request response");
                    servletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token header is missing");
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void setApiSecurityConfig(ApiSecurityConfig apiSecurityConfig) {
        this.apiSecurityConfig = apiSecurityConfig;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
