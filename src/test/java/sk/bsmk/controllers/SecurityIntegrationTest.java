package sk.bsmk.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import sk.bsmk.Application;
import sk.bsmk.domain.LoginRequest;
import sk.bsmk.security.CsrfTokenGeneratorFilter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SecurityIntegrationTest {

  private static final String SET_COOKIE = "Set-Cookie";

  private static final RestTemplate anonymous = new TestRestTemplate();
  private static final RestTemplate wrong = new TestRestTemplate("user", "passwordd");
  private static final RestTemplate correct = new TestRestTemplate("user", "password");

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

  // https://jira.spring.io/browse/SPR-9367
  @Test(expected = ResourceAccessException.class)
  public void thatCredentialsAreChecked() {
    LoginRequest request = new LoginRequest("a", "b");
    wrong.postForEntity("http://localhost:8080/rest/login", request, String.class);
  }

  @Test
  public void thatCsrfTokenAndSessionIsReturned() {
    LoginRequest request = new LoginRequest("a", "b");
    ResponseEntity<String> response = correct.postForEntity("http://localhost:8080/rest/login", request, String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(SecuredController.LOGIN_MESSAGE));
    // csrf token
    assertThat(response.getHeaders().get(CsrfTokenGeneratorFilter.CSRF_TOKEN_HEADER).size(), is(1));
    assertNotNull(response.getHeaders().get(CsrfTokenGeneratorFilter.CSRF_TOKEN_HEADER).get(0));
    // session id
    assertThat(response.getHeaders().get(SET_COOKIE).size(), is(1));
    assertThat(response.getHeaders().get(SET_COOKIE).get(0), containsString("JSESSIONID"));
  }

  @Test
  public void thatSessionIsNeededToAccessSecuredResource() {
    LoginRequest request = new LoginRequest("a", "b");
    String token =  correct
        .postForEntity("http://localhost:8080/rest/login", request, String.class)
        .getHeaders()
        .get(CsrfTokenGeneratorFilter.CSRF_TOKEN_HEADER).get(0);

    HttpHeaders headers = new HttpHeaders();
    headers.add(CsrfTokenGeneratorFilter.CSRF_TOKEN_HEADER, token);

    HttpEntity<?> httpEntity = new HttpEntity<>(headers);

    ResponseEntity<String> response = anonymous.exchange(
        "http://localhost:8080/rest/secured",
        HttpMethod.GET,
        httpEntity,
        String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    assertThat(response.getBody(), containsString("Expected CSRF token not found. Has your session expired?"));
  }

  @Test
  public void thatTokenCanBeUsedToAccessSecuredResource() {
    LoginRequest request = new LoginRequest("a", "b");
    HttpHeaders loginHeaders = correct
        .postForEntity("http://localhost:8080/rest/login", request, String.class)
        .getHeaders();

    ResponseEntity<String> response = anonymous.exchange(
        "http://localhost:8080/rest/secured",
        HttpMethod.GET,
        createHttpEntity(loginHeaders),
        String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is(SecuredController.SECURED_MESSAGE));
  }

  @Test
  public void thatTokenCannotBeUsedTwoTimesToAccessSecuredResource() {
    LoginRequest request = new LoginRequest("a", "b");
    HttpHeaders loginHeaders = correct
        .postForEntity("http://localhost:8080/rest/login", request, String.class)
        .getHeaders();

    HttpEntity<?> httpEntity = createHttpEntity(loginHeaders);

    // first time everything is ok
    ResponseEntity<String> okResponse = anonymous.exchange(
        "http://localhost:8080/rest/secured",
        HttpMethod.GET,
        httpEntity,
        String.class);

    assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));
    assertThat(okResponse.getBody(), is(SecuredController.SECURED_MESSAGE));

    // second time csrf token is invalid
    ResponseEntity<String> response = anonymous.exchange(
        "http://localhost:8080/rest/secured",
        HttpMethod.GET,
        httpEntity,
        String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    assertThat(response.getBody(), containsString("Invalid CSRF Token"));

  }

  @Test
  public void thatNewTokenCanBeUsedToAccessSecuredResource() {
    LoginRequest request = new LoginRequest("a", "b");
    HttpHeaders loginHeaders = correct
        .postForEntity("http://localhost:8080/rest/login", request, String.class)
        .getHeaders();

    // first time everything is ok
    ResponseEntity<String> okResponse = anonymous.exchange(
        "http://localhost:8080/rest/secured",
        HttpMethod.GET,
        createHttpEntity(loginHeaders),
        String.class);

    assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));
    assertThat(okResponse.getBody(), is(SecuredController.SECURED_MESSAGE));

    // we can use token from new response
    ResponseEntity<String> newResponse = anonymous.exchange(
        "http://localhost:8080/rest/secured",
        HttpMethod.GET,
        createHttpEntity(extractJSessionId(loginHeaders), extractCsrfToken(okResponse.getHeaders())),
        String.class);

    assertThat(newResponse.getStatusCode(), is(HttpStatus.OK));
    assertThat(newResponse.getBody(), is(SecuredController.SECURED_MESSAGE));

  }


  @Test
  public void thatSecuredResourceIsInaccessibleAfterLogout() {
    LoginRequest request = new LoginRequest("a", "b");
    HttpHeaders loginHeaders = correct
        .postForEntity("http://localhost:8080/rest/login", request, String.class)
        .getHeaders();

    // logout
    ResponseEntity<String> logoutResponse = anonymous.exchange(
        "http://localhost:8080/rest/logout",
        HttpMethod.POST,
        createHttpEntity(loginHeaders),
        String.class);

    assertThat(logoutResponse.getStatusCode(), is(HttpStatus.FOUND));

    // no csrf token
    assertNull(logoutResponse.getHeaders().get(CsrfTokenGeneratorFilter.CSRF_TOKEN_HEADER));

    // second time csrf token is invalid
    ResponseEntity<String> response = anonymous.exchange(
        "http://localhost:8080/rest/secured",
        HttpMethod.GET,
        createHttpEntity(loginHeaders),
        String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    assertThat(response.getBody(), containsString("Expected CSRF token not found. Has your session expired?"));

  }

  private static HttpEntity<?> createHttpEntity(HttpHeaders headers) {
    String token =  extractCsrfToken(headers);
    String jSessionId = extractJSessionId(headers);
    return createHttpEntity(jSessionId, token);
  }

  private static HttpEntity<?> createHttpEntity(String jSessionId, String csrfToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(CsrfTokenGeneratorFilter.CSRF_TOKEN_HEADER, csrfToken);
    headers.add("Cookie", String.format("JSESSIONID=%s", jSessionId));

    return new HttpEntity<>(headers);
  }

  private static String extractJSessionId(HttpHeaders headers) {
    String setCookieHeader =  headers.get(SET_COOKIE).get(0);
    return setCookieHeader.substring(setCookieHeader.indexOf('=') + 1, setCookieHeader.indexOf(';'));
  }

  private static String extractCsrfToken(HttpHeaders headers) {
    return headers.get(CsrfTokenGeneratorFilter.CSRF_TOKEN_HEADER).get(0);
  }

}