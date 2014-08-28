package sk.bsmk.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bsmk on 8/24/14.
 */
public class ModifiedFilter extends BasicAuthenticationFilter {


  public static final String CSRF_PARAMETER_NAME = "_csrf";
  //private HttpSessionCsrfTokenRepository.DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

  public static final String CSRF_HEADER_NAME = "X-CSRF-TOKEN";
  //private HttpSessionCsrfTokenRepositoryDEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";


  @Override
  protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
    request.getAttribute(CSRF_PARAMETER_NAME);
    super.onSuccessfulAuthentication(request, response, authResult);
  }
}
