package sk.bsmk.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by bsmk on 8/23/14.
 */
public class ExternalAuthenticationManager implements AuthenticationManager {

  //autowire rest client here

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    // send request to external service
    final String principal = authentication.getPrincipal().toString();
    final String password = authentication.getCredentials().toString();
    final String brand = authentication.getDetails().toString();

    //TODO
    return new TokenAuthentication();
  }

}
