package com.mayflowertech.chilla.config;

import java.util.UUID;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

@Component
@WebListener
public class RequestListener implements ServletRequestListener{
	@Override
    public void requestInitialized(ServletRequestEvent event) {		
		//System.out.println("======  tracking request");
        ThreadContext.put("request.trackid", UUID.randomUUID().toString());  
        ThreadContext.put("request.sourceip", event.getServletRequest().getRemoteAddr()); 
    }

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        ThreadContext.clearMap();
    }
}
