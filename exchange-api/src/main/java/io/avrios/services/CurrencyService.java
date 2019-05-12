package io.avrios.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
@Service
public class CurrencyService {

  private static Logger logger = LoggerFactory.getLogger(CurrencyService.class);

  @Value("${app.exchange.d90}")
  private String exchangeApi;

  private final RestTemplate restTemplate;

  @Autowired
  public CurrencyService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @PostConstruct
  public void afterInit() {
    logger.debug("Fetch xml document: {}", this.exchangeApi);
    String document = this.restTemplate.getForObject(this.exchangeApi, String.class);
    logger.debug("Document {}", document);
  }
}
