package sk.bsmk.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.util.matcher.*;
import sk.bsmk.security.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * Created by bsmk on 8/20/14.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  RequestMatcher csrfProtectedMatcher;

  @Autowired
  AuthenticationProvider authenticationProvider;

  @Autowired
  MyWebAuthenticationDetailsSource myWebAuthenticationDetailsSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authenticationProvider(authenticationProvider)
        .authorizeRequests().anyRequest().authenticated()
        .and().httpBasic().authenticationDetailsSource(myWebAuthenticationDetailsSource)
        /*
        .apply(new MyAuthenticationFilterConfigurer<HttpSecurity>())
        .loginProcessingUrl("/rest/login")

        .successHandler(new AuthenticationSuccessHandler() {
          @Override
          public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            //response.setHeader(ModifiedFilter.CSRF_HEADER_NAME, token.getToken());
          }
        })
        .failureHandler(new AuthenticationFailureHandler() {
          @Override
          public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            try (PrintWriter writer = response.getWriter()) {
              writer.print("{\"message\":\"My message\"}");
              writer.flush();
            }
          }
        })
/*        .and()
        .httpBasic()
        .authenticationEntryPoint(authenticationEntryPoint)
        */
        .and()
        .csrf()
        .requireCsrfProtectionMatcher(csrfProtectedMatcher)
    .and()
    .addFilterAfter(new CsrfTokenGeneratorFilter(), CsrfFilter.class)
        //TODO .and().logout()
        ;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.debug(true);
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
      try (PrintWriter writer = response.getWriter()) {
        writer.print("{\"message\":\"My message\"}");
        writer.flush();
      }
    };
  }

  @Bean
  RequestMatcher csrfProtectedMatcher() {
    // all instead of login should be protected
    return new AndRequestMatcher(
        new NegatedRequestMatcher(new AntPathRequestMatcher("/rest/login")),
        AnyRequestMatcher.INSTANCE
    );
  }

  @Bean
  AuthenticationProvider authenticationProvider() {
    return new AuthenticationProvider() {
      @Override
      public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO
        // send request to external service
        final String principal = authentication.getPrincipal().toString();
        final String password = authentication.getCredentials().toString();
        final String brand = authentication.getDetails().toString();

        authentication.getDetails();

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(principal, null, Collections.<GrantedAuthority>emptyList());

        return newAuthentication;
        //return new TokenAuthentication();
      }

      @Override
      public boolean supports(Class<?> authentication) {
        // TODO here has to be ours Authentication
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
      }

    };
  }

}
