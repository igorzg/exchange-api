package io.avrios.services.entities;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
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
    return getDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
}
