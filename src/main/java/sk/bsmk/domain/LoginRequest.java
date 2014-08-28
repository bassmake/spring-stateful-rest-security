package sk.bsmk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bsmk on 8/20/14.
 */
public class LoginRequest {

  private final String additionalInfo1;

  private final String additionalInfo2;

  @JsonCreator
  public LoginRequest(
      @JsonProperty("additionalInfo1") String additionalInfo1,
      @JsonProperty("additionalInfo2") String additionalInfo2
  ) {
    this.additionalInfo1 = additionalInfo1;
    this.additionalInfo2 = additionalInfo2;
  }

  public String getAdditionalInfo1() {
    return additionalInfo1;
  }

  public String getAdditionalInfo2() {
    return additionalInfo2;
  }

}
