package io.avrios.configurations;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
@Configuration
public class Builders {

  @Bean
  public RestTemplate build(RestTemplateBuilder builder) {
    return builder.build();
  }
}
