package org.springframework.security.web

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.ApiAuthenticationToken
import org.springframework.security.authentication.token.TokenService
import spock.lang.Specification
/**
 * @author maamria
 *
 * 19/10/15.
 *
 */
class ApiAuthenticationSuccessHandlerSpec extends Specification {

    def handler = new ApiAuthenticationSuccessHandler()

     private TokenService tokenService

    void setup(){
        tokenService = Mock(TokenService)
        handler.setTokenService(tokenService)
    }

    void 'test response'(){
        setup :
        def request = new MockHttpServletRequest()
        def response = new MockHttpServletResponse()
        def token = new ApiAuthenticationToken('accesstoken', -1, new Object(), [])
        when :
        handler.onAuthenticationSuccess(request, response, token)
        then:
        1 * tokenService.renderToken(response, token)
    }


}
