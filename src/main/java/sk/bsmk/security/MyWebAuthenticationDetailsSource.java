package sk.bsmk.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bsmk on 8/28/14.
 */
@Component
public class MyWebAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

  @Override
  public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    WebAuthenticationDetails details = super.buildDetails(context);
    return details;
  }

}
