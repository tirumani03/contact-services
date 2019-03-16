package com.ezetap.contacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.WebApplicationInitializer;

@EntityScan("com.ezetap.contacts.entity")
@ComponentScan( "com.ezetap.contacts")
@SpringBootApplication(scanBasePackages = "com.ezetap.contacts")
public class ContactsApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ContactsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ContactsApplication.class);
	}

}
