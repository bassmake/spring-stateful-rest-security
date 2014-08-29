package sk.bsmk.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Created by bsmk on 8/23/14.
 */
@Component
public class ExternalAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    // send request to external service
    final String username = authentication.getPrincipal().toString();
    final String password = authentication.getCredentials().toString();

    MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();

    if (!"user".equals(username) || !"password".equals(password) || !"a".equals(details.getAdditionalInfo1()) || !"b".equals(details.getAdditionalInfo2()))
      throw new BadCredentialsException("authentication failed");

    // we have to store password
    return new UsernamePasswordAuthenticationToken(username, password, Collections.<GrantedAuthority>emptyList());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

}
