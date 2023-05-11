package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.demo.service.AppService;

@SpringBootApplication
public class SpringdemoApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringdemoApplication.class, args);
		String[] beans = ctx.getBeanDefinitionNames();
		for (String bean : beans) {
			System.out.println(bean);
		}
		
		AppService service = ctx.getBean("appService", AppService.class);
		service.doTask();
	}

}
