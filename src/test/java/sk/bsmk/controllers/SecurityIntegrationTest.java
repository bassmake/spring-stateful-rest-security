package sk.bsmk.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import sk.bsmk.Application;
import sk.bsmk.domain.LoginRequest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SecurityIntegrationTest {

  private final RestTemplate anonymous = new TestRestTemplate();
  private final RestTemplate wrong = new TestRestTemplate("user", "passwordd");
  private final RestTemplate correct = new TestRestTemplate("user", "password");

  @Test
  public void thatSecuredIsNotAccessible() {
    ResponseEntity<String> response = anonymous.getForEntity("http://localhost:8080/rest/secured", String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    assertThat(response.getBody(), containsString("Expected CSRF token not found. Has your session expired?"));
  }


  // https://jira.spring.io/browse/SPR-9367
  @Test(expected = ResourceAccessException.class)
  public void thatLoginNeedsBasicAuthentication() {
    LoginRequest request = new LoginRequest("s", "s");
    wrong.postForEntity("http://localhost:8080/rest/login", request, String.class);
  }


  @Test
  public void thatCredentialsAreChecked() {
    LoginRequest request = new LoginRequest("a", "b");
    ResponseEntity<String> response = wrong.postForEntity("http://localhost:8080/login", request, String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    assertThat(response.getBody(), containsString("Expected CSRF token not found. Has your session expired?"));
  }


}