package com.shopme.admin.security;

import java.io.IOException;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties.Request;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer ->
        	configurer
                .requestMatchers("/").permitAll()
                .requestMatchers("/admin/users").permitAll()
                .requestMatchers("/images/**","/js/**","/webjars/**").permitAll()
                .anyRequest().permitAll()
                );
//        	.formLogin(form ->
//                form
//                        .loginPage("/login")
//                        .usernameParameter("email")
//                        .loginProcessingUrl("/authenticateTheUser")
//                        .permitAll()
//                        .failureHandler(new AuthenticationFailureHandler() {
//							
//							@Override
//							public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//									AuthenticationException exception) throws IOException, ServletException {
//								//handle login failure
//								System.out.println("Đăng nhập thất bại");
//								response.sendRedirect("/login?error");
//							}
//						})
//        			)
//        	.logout(logout -> logout.permitAll()
//        			)
//        	.exceptionHandling(configurer ->
//                configurer.accessDeniedPage("/access-denied")
//        			);
        return http.build();
	}
	
	
}
