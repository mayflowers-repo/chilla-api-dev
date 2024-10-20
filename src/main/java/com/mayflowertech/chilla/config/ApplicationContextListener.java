package com.mayflowertech.chilla.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;

@Component
public class ApplicationContextListener  implements ServletContextListener{
	
	
	 @Override
	    public void contextInitialized(ServletContextEvent servletContextEvent) {
	        System.out.println("ApplicationContextListener Starting up!");
	    }

	    @Override
	    public void contextDestroyed(ServletContextEvent servletContextEvent) {
	        System.out.println("ApplicationContextListener Shutting down!");
	    }
}
