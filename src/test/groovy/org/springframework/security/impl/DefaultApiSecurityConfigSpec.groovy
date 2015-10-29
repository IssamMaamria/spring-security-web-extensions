package org.springframework.security.impl

import spock.lang.Specification

/**
 * @author maamria
 *
 * 19/10/15.
 *
 */
class DefaultApiSecurityConfigSpec extends Specification {

    private config = new DefaultApiSecurityConfig()

    void 'check default value'(){
        expect :
        config.loginEndpoint() == '/api/login'
        config.logoutEndpoint() == '/api/logout'
        config.validationEndpoint() == '/api/validate'
    }

}
