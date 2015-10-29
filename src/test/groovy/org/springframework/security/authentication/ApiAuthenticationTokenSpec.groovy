package org.springframework.security.authentication

import spock.lang.Specification
/**
 * @author maamria
 *
 * 15/10/15.
 *
 */
class ApiAuthenticationTokenSpec extends Specification{

    void 'test unverified token'(){
        given :
        def token = new ApiAuthenticationToken('accesstoken', -1, new Object());
        expect:
        !token.isAuthenticated()
        token.getAccessToken() == 'accesstoken'
        token.expiration == -1
    }

    void 'test verified token'(){
        given:
        def token = new ApiAuthenticationToken('accesstoken', -1, new Object(), new ArrayList<>())
        expect :
        token.isAuthenticated()
    }

}
