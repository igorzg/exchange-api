package io.avrios.configurations;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author igorzg on 2019-05-12.
 * @since 1.0
 */
@Configuration
public class Builders {

  @Bean
  public HttpClient buildClient() {
    return HttpClients.createDefault();
  }

  @Bean
  public DocumentBuilder buildDocumentParser() throws Exception {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder();
  }
}
