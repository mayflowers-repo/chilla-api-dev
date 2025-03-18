package com.mayflowertech.chilla.config;

import org.apache.catalina.security.SecurityConfig;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

@Configuration
@Order(2)
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { Config.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

	
	@Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        super.registerDispatcherServlet(servletContext);
        servletContext.addListener(new ApplicationContextListener());
    }
	
	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    
	    factory.setMaxFileSize(DataSize.ofMegabytes(10)); // Convert String to DataSize
	    factory.setMaxRequestSize(DataSize.ofMegabytes(1));
	    factory.setFileSizeThreshold(DataSize.ofMegabytes(5));

	    registration.setMultipartConfig(factory.createMultipartConfig());
	}
	
	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
        
    }

}
