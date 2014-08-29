package sk.bsmk.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bsmk on 8/20/14.
 */
@RestController
@RequestMapping("/rest")
public class SecuredController {

  public static final String SECURED_MESSAGE = "hello";
  public static final String LOGIN_MESSAGE = "You logged in";

  @RequestMapping(method = RequestMethod.POST, value = "/login")
  ResponseEntity<String> login() {
    return new ResponseEntity<>(LOGIN_MESSAGE, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/secured")
  ResponseEntity<String> secured() {
    SecurityContextHolder.getContext().getAuthentication();
    return new ResponseEntity<>(SECURED_MESSAGE, HttpStatus.OK);
  }


}
