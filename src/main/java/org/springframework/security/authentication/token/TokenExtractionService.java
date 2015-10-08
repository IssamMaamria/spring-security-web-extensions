package org.springframework.security.authentication.token;

import org.springframework.security.authentication.ApiAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

/**
 * @author maamria
 *         <p>
 *         08/10/15.
 */
public interface TokenExtractionService {

    ApiAuthenticationToken extractAccessToken(HttpServletRequest request);

}
