package io.avrios.controller;

import io.avrios.services.CurrencyService;
import io.avrios.services.entities.Cube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/currency", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController {


  private final CurrencyService currencyService;

  @Autowired
  public CurrencyController(CurrencyService currencyService) {
    this.currencyService = currencyService;
  }

  @GetMapping
  public List<Cube> listAllCubes() throws Exception {
    return this.currencyService.fetchCubes();
  }
}
