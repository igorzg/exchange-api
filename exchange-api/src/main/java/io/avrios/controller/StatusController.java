package io.avrios.controller;

import io.avrios.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
@RestController
@ControllerAdvice
public class StatusController extends ResponseEntityExceptionHandler implements ErrorController {

  @Value("${app.message}")
  private String globalMessage;

  @Value("${app.name}")
  private String globalApplicationName;

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> status() {
    return new Response(globalMessage, globalApplicationName).getResponseEnity();
  }

  @GetMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Response> error(Exception exception, WebRequest request, HttpServletResponse servletResponse) {
    return new Response(
      servletResponse.getStatus() == 404 ? "Service mapping not found" : "Unknown error",
      request.getDescription(false),
      HttpStatus.NOT_FOUND
    ).getResponseEnity();
  }

  @Override
  public String getErrorPath() {
    return "error";
  }
}
