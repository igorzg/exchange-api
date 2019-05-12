package io.avrios.controller;

import io.avrios.services.CurrencyService;
import io.avrios.services.entities.Converted;
import io.avrios.services.entities.Cube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

  @GetMapping("/dates")
  public List<String> listAllDates() throws Exception {
    return this.currencyService.fetchCubes().stream().map(i -> i.getDate().toString()).distinct().collect(Collectors.toList());
  }

  @GetMapping("/{from}/{into}/{value}")
  public Converted convert(@PathVariable String from, @PathVariable String into, @PathVariable double value) {
    return convert(LocalDate.now().toString(), from, into, value);
  }

  @GetMapping("/{date}/{from}/{into}/{value}")
  public Converted convert(@PathVariable String date, @PathVariable String from, @PathVariable String into, @PathVariable double value) {
    LocalDate localDate = this.currencyService.parseDate(date);
    return this.currencyService.convert(from, into, value, Cube.dateToEpoch(localDate));
  }
}
