package sk.bsmk.security;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by bsmk on 8/24/14.
 */
public class MyAuthenticationFilterConfigurer<H extends HttpSecurityBuilder<H>>
    // TODO use custom filter here
  extends AbstractAuthenticationFilterConfigurer<H, MyAuthenticationFilterConfigurer<H>, UsernamePasswordAuthenticationFilter> {


  public  MyAuthenticationFilterConfigurer() {
    super(new UsernamePasswordAuthenticationFilter(), null);
  }

  @Override
  protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return new AntPathRequestMatcher("/rest/login", "POST");
  }


}
