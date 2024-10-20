package com.mayflowertech.chilla.config.custom;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RuntimeRunner {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SystemData.class);
	      
		SystemData sd = context.getBean(SystemData.class);
		sd.test();
	}

}
