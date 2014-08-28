package sk.bsmk.controllers;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.MatcherAssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import sk.bsmk.Application;
import sk.bsmk.domain.LoginRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.util.MatcherAssertionErrors.*;

@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SecurityIntegrationTest {

  private final RestTemplate anonymous = new TestRestTemplate();
  private final RestTemplate correct = new TestRestTemplate("user", "password");
  private final RestTemplate wrong = new TestRestTemplate("user", "passwordd");

  @Test
  public void thatSecuredIsNotAccessible() {
    ResponseEntity<String> response = anonymous.getForEntity("http://localhost:8080/secured", String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    assertThat(response.getBody(), containsString("Expected CSRF token not found. Has your session expired?"));
  }


  @Test
  public void thatCredentialsAreChecked() {

    LoginRequest request = new LoginRequest("s", "s");

    ResponseEntity<String> response = anonymous.postForEntity("http://localhost:8080/login", request, String.class);
    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    assertThat(response.getBody(), containsString("Expected CSRF token not found. Has your session expired?"));

  }


}