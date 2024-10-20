package com.mayflowertech.chilla.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class PropertyReader {
  
  @Value("${server.port}")
  private String springBootPort;

  public String getSpringBootPort() {
    return springBootPort;
  }

  public void setSpringBootPort(String springBootPort) {
    this.springBootPort = springBootPort;
  }
    
  
  

}
