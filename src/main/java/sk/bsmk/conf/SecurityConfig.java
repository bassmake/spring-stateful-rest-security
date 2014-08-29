package sk.bsmk.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import sk.bsmk.security.CsrfTokenGeneratorFilter;
import sk.bsmk.security.MyWebAuthenticationDetailsSource;

/**
 * Created by bsmk on 8/20/14.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String LOGOUT_PATH = "/rest/logout";

  @Autowired
  AuthenticationProvider authenticationProvider;

  @Autowired
  MyWebAuthenticationDetailsSource myWebAuthenticationDetailsSource;

  // csrfTokenRepository is inaccessible in CsrfConfigurer so we create our own instance
  // and we give it to the CsrfTokenGeneratorFilter
  private final CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authenticationProvider(authenticationProvider)
        .authorizeRequests().anyRequest().authenticated()
        .and().httpBasic().authenticationDetailsSource(myWebAuthenticationDetailsSource)
        .and()
        .csrf()
        .csrfTokenRepository(csrfTokenRepository)
        // everything except /rest/login
        .requireCsrfProtectionMatcher(new AndRequestMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/rest/login")), AnyRequestMatcher.INSTANCE))
        .and()
        .logout()
        .logoutUrl(LOGOUT_PATH)
        .and()
        .addFilterAfter(new CsrfTokenGeneratorFilter(csrfTokenRepository, new AntPathRequestMatcher("/rest/logout", "POST")), SessionManagementFilter.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.debug(true);
  }

}
