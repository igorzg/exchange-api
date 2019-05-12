package io.avrios.services.entities;

import java.time.LocalDate;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
public class Converted {
  private String from;
  private String into;
  private LocalDate date;
  private double result;

  public Converted(String from, String into, double result, LocalDate date) {
    this.from = from;
    this.into = into;
    this.date = date;
    this.result = result;
  }

  public String getFrom() {
    return from;
  }

  public String getInto() {
    return into;
  }

  public LocalDate getDate() {
    return date;
  }

  public double getResult() {
    return result;
  }
}
