package com.rahulshettyacademy.controller;

import java.util.concurrent.atomic.AtomicLong;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private final Greeting greeting;

	public GreetingController(Greeting greeting) {
		this.greeting = greeting;
	}

AtomicLong counter= new AtomicLong();

	
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value="name")String name)
	{
		greeting.setId(counter.incrementAndGet());
		greeting.setContent("Hey I am learning Spring Boot from "+name);
	return greeting;
		
		
	}
	
	
	
	
	
}
