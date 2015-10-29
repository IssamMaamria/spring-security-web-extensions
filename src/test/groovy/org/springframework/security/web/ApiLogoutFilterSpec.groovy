package org.springframework.security.web

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.ApiAuthenticationToken
import org.springframework.security.authentication.token.TokenNotFoundException
import org.springframework.security.authentication.token.TokenService
import org.springframework.security.impl.DefaultApiSecurityConfig
import spock.lang.Specification

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse

/**
 * @author maamria
 *
 * 19/10/15.
 *
 */
class ApiLogoutFilterSpec extends Specification {

    private ApiLogoutFilter filter

    private TokenService tokenService

    private MockHttpServletRequest request
    private MockHttpServletResponse response

    void setup(){
        filter = new ApiLogoutFilter()
        filter.setApiSecurityConfig(new DefaultApiSecurityConfig())
        tokenService = Mock(TokenService)
        filter.setTokenService(tokenService)
        request = new MockHttpServletRequest()
        response = new MockHttpServletResponse()
    }

    void 'test not logout url'(){
        when:
        request.setContextPath("http://localhost")
        request.setRequestURI("/notlogout")
        filter.doFilter(request, response, Mock(FilterChain))
        then:
        0 * tokenService.extractAccessToken(_)
        0 * tokenService.removeToken(_)
    }

    void 'test logout url but not post'(){
        when:
        request.setContextPath("http://localhost")
        request.setRequestURI("/api/logout")
        filter.doFilter(request, response, Mock(FilterChain))
        then:
        0 * tokenService.extractAccessToken(_)
        0 * tokenService.removeToken(_)
        response.getStatus() == HttpServletResponse.SC_METHOD_NOT_ALLOWED
    }

    void 'test logout and token could not be extracted from request'(){
        setup:
        1 * tokenService.extractAccessToken(_) >> null
        when:
        request.setContextPath("http://localhost")
        request.setRequestURI("/api/logout")
        request.setMethod("POST")
        filter.doFilter(request, response, Mock(FilterChain))
        then:
        response.getStatus() == HttpServletResponse.SC_BAD_REQUEST
    }

    void 'test logout and token could not be removed'(){
        setup:
        1 * tokenService.extractAccessToken(request) >> new ApiAuthenticationToken('someaccesstoken', -1, new Object())
        1 * tokenService.removeToken('someaccesstoken') >> {
            throw new TokenNotFoundException()
        }
        when:
        request.setContextPath("http://localhost")
        request.setRequestURI("/api/logout")
        request.setMethod("POST")
        filter.doFilter(request, response, Mock(FilterChain))
        then:
        response.getStatus() == HttpServletResponse.SC_NOT_FOUND
    }

    void 'test logout'(){
        when:
        request.setContextPath("http://localhost")
        request.setRequestURI("/api/logout")
        request.setMethod("POST")
        filter.doFilter(request, response, Mock(FilterChain))
        then:
        1 * tokenService.extractAccessToken(request) >> new ApiAuthenticationToken('someaccesstoken', -1, new Object())
        1 * tokenService.removeToken('someaccesstoken')
    }

}
