package org.springframework.security.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author maamria
 *         <p>
 *         06/10/15.
 */
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler{

    private static final Logger log = LoggerFactory.getLogger(ApiAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        int statusCode = HttpServletResponse.SC_FORBIDDEN;
        log.debug("Setting status code to {}", statusCode);
        response.setStatus(statusCode);
    }
}
