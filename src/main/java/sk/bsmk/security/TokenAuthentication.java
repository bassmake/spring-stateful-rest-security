package sk.bsmk.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by bsmk on 8/23/14.
 */
public class TokenAuthentication implements Authentication {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public boolean isAuthenticated() {
    return false;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

  }

  @Override
  public boolean equals(Object another) {
    return false;
  }

  @Override
  public String toString() {
    return null;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String getName() {
    return null;
  }
}
