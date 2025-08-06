package com.techyatra.blog_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BlogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
		// Create instance and call print
		// BlogApiApplication app = new BlogApiApplication();
		// app.print();

	}

	// public void print() {
	// 	String message = "Hello, World!";
	// 	System.out.println(Integer.MAX_VALUE);
	// }

}
