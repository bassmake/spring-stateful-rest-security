package sk.bsmk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by bsmk on 8/22/14.
 */
@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Application {

  public static void main(String ... args) {
    SpringApplication.run(Application.class, args);
  }

}
