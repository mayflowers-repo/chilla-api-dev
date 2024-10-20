package com.mayflowertech.chilla.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("deprecation")
public class SecurityConfigurerAdapter  extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		System.out.println("========== SecurityConfig ====");
	      http.cors().and().csrf().disable().authorizeRequests()
	      .antMatchers("/static/**").permitAll()
          .antMatchers("/images/**").permitAll()
          .antMatchers("/**/api/v1/usermanagement/login").permitAll()
          .antMatchers("/**/api/v1/usermanagement/create").permitAll()
          .antMatchers("/**/api/v1/**/**/signup").permitAll()
          .antMatchers("/**/api/v1/email/**").permitAll()
          .antMatchers("/**/api/v1/usermanagement/verifyGoogleIdToken").permitAll()
          .antMatchers("/**/api/v1/usermanagement/verifyGoogleIdTokenDart").permitAll()
          .antMatchers("/**/api/v1/usermanagement/users/*/forgotpassword").permitAll()
          .antMatchers("/**/api/v1/usermanagement/users/resetpassword/**").permitAll()
          .antMatchers("/**/api/v1/usermanagement/users/changepassword").permitAll()
          .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
                  "/swagger-ui.html", "/ec/**", "/webjars/**")
          .permitAll()
          .anyRequest().authenticated().and().exceptionHandling()
          .authenticationEntryPoint(unauthorizedHandler).and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		
		
		http.addFilterBefore(authenticationTokenFilterBean(),
				UsernamePasswordAuthenticationFilter.class);
		
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
		
	}
	
	@Bean
	public JwtAuthenticationFilter authenticationTokenFilterBean() {
		System.out.println("in JwtAuthenticationFilter");
		return new JwtAuthenticationFilter();
	}

	
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		System.out.println("in authenticationManagerBean");
		return super.authenticationManagerBean();
	}

	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth)
			throws Exception {
		System.out.println("in globalUserDetails");
	}

}
