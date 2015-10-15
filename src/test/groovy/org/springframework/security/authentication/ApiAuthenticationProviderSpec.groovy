package org.springframework.security.authentication
import org.springframework.security.authentication.token.TokenNotFoundException
import org.springframework.security.authentication.token.TokenService
import org.springframework.security.core.userdetails.User
import spock.lang.Specification
/**
 * @author maamria
 *
 * 15/10/15.
 *
 */
class ApiAuthenticationProviderSpec extends Specification{

    private ApiAuthenticationProvider provider

    private TokenService tokenService

    void setup(){
        tokenService = Mock(TokenService)
        provider = new ApiAuthenticationProvider(tokenService: tokenService)
    }

    void "test supports"(){
        expect:
        provider.supports(ApiAuthenticationToken)
        and:
        !provider.supports(UsernamePasswordAuthenticationToken)
    }

    void "test authenticate with no access token"(){
        setup:
        def token = new ApiAuthenticationToken(null, -1, new Object())
        when :
        provider.authenticate(token)
        then :
        thrown(TokenNotFoundException)
    }

    void "test authenticate with unrecognised access token"(){
        setup:
        tokenService.loadUserByToken("accesstoken") >> null
        def token = new ApiAuthenticationToken("accesstoken", -1, new Object())
        when :
        provider.authenticate(token)
        then :
        thrown(TokenNotFoundException)
    }

    void "test authenticate"(){
        def user = new User("someuser", 'somepass', new ArrayList())
        setup:
        tokenService.loadUserByToken("accesstoken") >> user
        def token = new ApiAuthenticationToken("accesstoken", -1, new Object())
        when:
        def authentication = provider.authenticate(token)
        then :
        authentication.principal == user
        authentication.isAuthenticated()
    }

}
