package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeRequests()
				.antMatchers("/posts").hasAnyRole("READ","ADMIN")
				.antMatchers("/authors").hasAnyRole("ADMIN")
				.antMatchers("/hello").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.and()
				.build();
	}
	
	@Bean
	public InMemoryUserDetailsManager users() {
		return new InMemoryUserDetailsManager(User
				.withUsername("banu")
				.password("{noop}secret")
				.authorities("ROLE_READ").build(),
				User
				.withUsername("prakash")
				.password("{noop}secret")
				.authorities("ROLE_READ", "ROLE_ADMIN").build());
				
	}
}
