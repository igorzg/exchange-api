package io.avrios.services.entities;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
public class Cube {

  private String currency;

  private double rate;

  private LocalDate date;

  public Cube(String currency, double rate, LocalDate date) {
    this.currency = currency;
    this.rate = rate;
    this.date = date;
  }

  public String getCurrency() {
    return currency;
  }

  public double getRate() {
    return rate;
  }

  public LocalDate getDate() {
    return date;
  }

  @JsonGetter(value = "timestamp")
  public Long getTimestamp() {
    return dateToEpoch(getDate());
  }

  public static Long dateToEpoch(LocalDateTime date) {
    return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  public static Long dateToEpoch(LocalDate date) {
    return date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
}
