package com.mayflowertech.chilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class TeamWebApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(TeamWebApplication.class, args);
		System.out.println("===========  team web start up");
	}

	 @Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	  return application.sources(TeamWebApplication.class);
	 }
	 
}
