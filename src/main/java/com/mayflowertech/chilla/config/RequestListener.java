package com.mayflowertech.chilla.config;

import java.util.UUID;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

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
