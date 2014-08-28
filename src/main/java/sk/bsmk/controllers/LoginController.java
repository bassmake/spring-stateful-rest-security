package sk.bsmk.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sk.bsmk.domain.LoginRequest;

/**
 * Created by bsmk on 8/20/14.
 */
@RestController
@RequestMapping("/rest")
public class LoginController {

  private static final Logger log = LoggerFactory.getLogger(LoginController.class);

  @RequestMapping(method = RequestMethod.POST, value = "/login")
  ResponseEntity<String> login(@RequestBody LoginRequest request) {
    log.info("login for {}", request);
    return new ResponseEntity<>("You logged in", HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/secured")
  ResponseEntity<String> secured() {
    return new ResponseEntity<>("Hello", HttpStatus.OK);
  }


}
