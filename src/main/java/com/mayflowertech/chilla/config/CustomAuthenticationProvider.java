package com.mayflowertech.chilla.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mayflowertech.chilla.entities.AuthUser;
import com.mayflowertech.chilla.services.impl.AuthUserService;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AuthUserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("CustomAuthenticationProvider authe");
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		AuthUser user = new AuthUser(name, password);
		boolean valid =  userService.validateUser(user);
		if(valid){
			UserDetails userdetails = userService.loadUserByUsername(name);
			if(userdetails == null || userdetails.getAuthorities() == null){
				System.out.println("userdetails/authorities  null ");
			}
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					name, password, userdetails.getAuthorities());
			System.out.println("CustomAuthenticationProvider valid user auth="+auth);
			return auth;
		}
		else{
			System.out.println("CustomAuthenticationProvider invalid user");
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		System.out.println("CustomAuthenticationProvider supports");
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
