package io.avrios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
public class Response {

  private Date timestamp;
  private String message;
  private String details;
  private HttpStatus httpStatus;

  public Response(String message, String details) {
    this(message, details, HttpStatus.OK);
  }

  public Response(String message, String details, HttpStatus httpStatus) {
    this.timestamp = new Date();
    this.message = message;
    this.details = details;
    this.httpStatus = httpStatus;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDetails() {
    return details;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @JsonIgnore
  public ResponseEntity<Response> getResponseEnity() {
    return new ResponseEntity<>(this, httpStatus);
  }
}
