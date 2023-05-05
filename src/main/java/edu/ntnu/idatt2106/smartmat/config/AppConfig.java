package edu.ntnu.idatt2106.smartmat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for the application. This class is used to configure beans and other
 * configurations for the application.
 * @author Tobias O.
 * @version 1.0 18.04.2023
 */
@Configuration
public class AppConfig {

  /**
   * Creates a bean for the RestTemplate class.
   * @return A RestTemplate object.
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  // ... andre konfigurasjoner
}
