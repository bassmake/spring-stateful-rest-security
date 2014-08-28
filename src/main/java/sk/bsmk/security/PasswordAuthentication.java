package sk.bsmk.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by bsmk on 8/23/14.
 */
public class PasswordAuthentication implements Authentication {

  private final String loginAlias;
  private final String password;
  private final String brand;
  private final boolean authenticated;

  public PasswordAuthentication(String loginAlias, String password, String brand, boolean authenticated) {
    this.loginAlias = loginAlias;
    this.password = password;
    this.brand = brand;
    this.authenticated = authenticated;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getCredentials() {
    return password;
  }

  @Override
  public Object getDetails() {
    return brand;
  }

  @Override
  public Object getPrincipal() {
    return loginAlias;
  }

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getName() {
    throw new UnsupportedOperationException();
  }

}
