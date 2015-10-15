package org.springframework.security.config.annotation.web.configurers;

import org.springframework.security.ApiSecurityConfig;
import org.springframework.security.authentication.ApiAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExtractor;
import org.springframework.security.authentication.token.TokenService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.ApiAuthenticationFailureHandler;
import org.springframework.security.web.ApiAuthenticationFilter;
import org.springframework.security.web.ApiAuthenticationSuccessHandler;
import org.springframework.security.web.ApiLogoutFilter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.http.HttpServletRequest;

/**
 * @author maamria
 *         <p>
 *         13/10/15.
 */
public class ApiSecurityConfigurer extends AbstractHttpConfigurer<ApiSecurityConfigurer, HttpSecurity> {

    private TokenService tokenService;
    private ApiSecurityConfig apiSecurityConfig;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private String apiUrlPattern;
    private CredentialsExtractor credentialsExtractor;

    private final ApiAuthenticationProvider apiAuthenticationProvider;

    private final ApiAuthenticationFilter apiAuthenticationFilter;

    private final ApiAuthenticationSuccessHandler apiAuthenticationSuccessHandler;

    private final ApiLogoutFilter apiLogoutFilter;

    public ApiSecurityConfigurer(){
        this.apiAuthenticationProvider = new ApiAuthenticationProvider();
        this.apiAuthenticationFilter = new ApiAuthenticationFilter();
        this.apiAuthenticationSuccessHandler = new ApiAuthenticationSuccessHandler();
        ApiAuthenticationFailureHandler apiAuthenticationFailureHandler = new ApiAuthenticationFailureHandler();
        this.apiAuthenticationFilter.setApiAuthenticationSuccessHandler(this.apiAuthenticationSuccessHandler);
        this.apiAuthenticationFilter.setApiAuthenticationFailureHandler(apiAuthenticationFailureHandler);
        this.apiLogoutFilter = new ApiLogoutFilter();
    }

    public ApiSecurityConfigurer antRequestsMatcher(String apiUrlPattern){
        this.apiUrlPattern = apiUrlPattern;
        return this;
    }

    public ApiSecurityConfigurer tokenService(TokenService tokenService){
        this.tokenService = tokenService;
        this.apiAuthenticationProvider.setTokenService(tokenService);
        this.apiAuthenticationFilter.setTokenService(tokenService);
        this.apiAuthenticationSuccessHandler.setTokenService(tokenService);
        this.apiLogoutFilter.setTokenService(tokenService);
        return this;
    }

    public ApiSecurityConfigurer withApiSecurityConfig(ApiSecurityConfig apiSecurityConfig){
        this.apiSecurityConfig = apiSecurityConfig;
        this.apiAuthenticationFilter.setApiSecurityConfig(apiSecurityConfig);
        this.apiLogoutFilter.setApiSecurityConfig(apiSecurityConfig);
        return this;
    }

    public ApiSecurityConfigurer authenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        this.apiAuthenticationFilter.setAuthenticationDetailsSource(authenticationDetailsSource);
        return this;
    }

    public ApiSecurityConfigurer credentialsExtractor(CredentialsExtractor credentialsExtractor){
        this.credentialsExtractor = credentialsExtractor;
        this.apiAuthenticationFilter.setCredentialsExtractor(credentialsExtractor);
        return this;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        apiAuthenticationFilter.setAuthenticationManager(authenticationManager);
        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(new Http403ForbiddenEntryPoint(),
                new NullRequestCache());
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage(null);
        exceptionTranslationFilter.setAccessDeniedHandler(accessDeniedHandler);
        if(apiUrlPattern != null){
            builder.antMatcher(apiUrlPattern);
        }
        builder.authenticationProvider(apiAuthenticationProvider)
                .addFilterAfter(apiAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(apiLogoutFilter, LogoutFilter.class)
                .addFilterAfter(exceptionTranslationFilter, ExceptionTranslationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin().disable()
                // csrf config
                .csrf().disable()
                // http basic disabled
                .httpBasic().disable()
                // jee disabled
                .jee().disable();
    }

    private boolean isConfigured(){
        return tokenService != null && apiSecurityConfig != null && authenticationDetailsSource != null && credentialsExtractor != null;
    }
}
