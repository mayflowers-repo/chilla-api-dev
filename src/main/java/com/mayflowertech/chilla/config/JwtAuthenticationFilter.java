package com.mayflowertech.chilla.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mayflowertech.chilla.entities.ResponseStatus;
import com.mayflowertech.chilla.services.impl.UserService;
import com.mayflowertech.chilla.utils.JsonUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	 private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	 
	 
  @Lazy
  @Autowired
  private JwtAuthorizationProvider jwtTokenUtil;

  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
	  
	  FilterChainProxy filterChainProxy = (FilterChainProxy) req.getServletContext()
	            .getAttribute(FilterChainProxy.class.getName());
	    
    String requestUri = req.getRequestURI();    
    logger.info("====JwtAuthenticationFilter : "+requestUri);
    // Exclude specific endpoints from authentication
    if (excludeRequestURIPattern(requestUri)) {
      chain.doFilter(req, res);
      return;
    }
    logger.info("starting.. filter process");
    String header = req.getHeader(Constants.AUTHORIZATION);
    if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
      String authToken = header.replace(Constants.TOKEN_PREFIX, "");

      try {
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = userService.loadUserByUsername(username);

          if (jwtTokenUtil.validateToken(authToken, userDetails)) {
            UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(
                authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
          } else {
            sendErrorResponse(res, "Token Expired");
            return;
          }
        }
      } catch (IllegalArgumentException e) {
        sendErrorResponse(res, "Corrupted token");
        return;
      } catch (ExpiredJwtException e) {
        sendErrorResponse(res, "Token Expired");
        return;
      } catch (SignatureException e) {
        sendErrorResponse(res, "Invalid Credentials");
        return;
      }
    }
    chain.doFilter(req, res);
  }

  private void sendErrorResponse(HttpServletResponse res, String message) throws IOException {
    ResponseStatus errorResponse = new ResponseStatus();
    errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
    errorResponse.setStatusmessage(message);
    res.setStatus(HttpStatus.BAD_REQUEST.value());
    res.getWriter().write(JsonUtils.convertObjectToJson(errorResponse));
    res.sendError(errorResponse.getStatuscode(), errorResponse.getStatusmessage());
  }

  private boolean excludeRequestURIPattern(String requestUri) {
	  String pattern1 = "/*/api/v1/usermanagement/login";
		String pattern7 = "/*/api/v1/usermanagement/verifyGoogleIdToken";
		List<Pattern> patterns = new ArrayList<Pattern>();
		Pattern p = Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		patterns.add(p);
		
	    p = Pattern.compile(pattern7, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	    patterns.add(p);
	    
		for (Pattern pattern : patterns) {
			Matcher m = pattern.matcher(requestUri);
			if(m.find()){
				return true;
			}
		}
		return false;
	  
     }
}
