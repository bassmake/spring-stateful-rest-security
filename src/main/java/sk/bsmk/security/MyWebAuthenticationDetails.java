package sk.bsmk.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import sk.bsmk.domain.LoginRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bsmk on 8/28/14.
 */
public class MyWebAuthenticationDetails extends WebAuthenticationDetails {

  private final String additionalInfo1;
  private final String additionalInfo2;


  public MyWebAuthenticationDetails(HttpServletRequest request) {
    super(request);

    try {

      ObjectMapper mapper = new ObjectMapper();
      LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);

      additionalInfo1 = loginRequest.getAdditionalInfo1();
      additionalInfo2 = loginRequest.getAdditionalInfo2();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public String getAdditionalInfo1() {
    return additionalInfo1;
  }

  public String getAdditionalInfo2() {
    return additionalInfo2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MyWebAuthenticationDetails)) return false;
    if (!super.equals(o)) return false;

    MyWebAuthenticationDetails that = (MyWebAuthenticationDetails) o;

    if (!additionalInfo1.equals(that.additionalInfo1)) return false;
    if (!additionalInfo2.equals(that.additionalInfo2)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + additionalInfo1.hashCode();
    result = 31 * result + additionalInfo2.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "MyWebAuthenticationDetails{" +
        "additionalInfo2='" + additionalInfo2 + '\'' +
        '}';
  }
}
