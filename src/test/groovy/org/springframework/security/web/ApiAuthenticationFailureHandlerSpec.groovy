package org.springframework.security.web
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse
/**
 * @author maamria
 *
 * 19/10/15.
 *
 */
class ApiAuthenticationFailureHandlerSpec extends Specification {

    private handler = new ApiAuthenticationFailureHandler()

    void 'test response'(){
        setup:
        def request = new MockHttpServletRequest()
        def response = new MockHttpServletResponse()
        when:
        handler.onAuthenticationFailure(request, response, new AuthenticationServiceException("fail"))
        then:
        response.getStatus() == HttpServletResponse.SC_FORBIDDEN
    }


}
