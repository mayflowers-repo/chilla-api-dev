package com.mayflowertech.chilla.config;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mayflowertech.chilla.entities.ResponseStatus;
import com.mayflowertech.chilla.services.impl.UserService;
import com.mayflowertech.chilla.utils.JsonUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtAuthorizationProvider jwtTokenUtil;


  @Autowired
  private UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    String requesturi = req.getRequestURI();
    // System.out.println("Request URI : " + requesturi);
    //System.out.println("====== requesturi  :"+requesturi);
    
    if (excludeRequestURIPattern(requesturi)) {
      //System.out.println("=====  AAD exclude");
      chain.doFilter(req, res);
      return;
    }
    //System.out.println("=====  AAD ask auth");
    String header = req.getHeader(Constants.AUTHORIZATION);
    String authToken = null;
    if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
      authToken = header.replace(Constants.TOKEN_PREFIX, "");
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
            ResponseStatus errorResponse = new ResponseStatus();
            errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatusmessage("Token Expired");
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.getWriter().write(JsonUtils.convertObjectToJson(errorResponse));
            res.sendError(errorResponse.getStatuscode(), errorResponse.getStatusmessage());
            return;
          }

        }

      } catch (IllegalArgumentException e) {
        ResponseStatus errorResponse = new ResponseStatus();
        errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setStatusmessage("Corrupted token");
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.getWriter().write(JsonUtils.convertObjectToJson(errorResponse));
        res.sendError(errorResponse.getStatuscode(), errorResponse.getStatusmessage());
        return;
      } catch (ExpiredJwtException e) {
        ResponseStatus errorResponse = new ResponseStatus();
        errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setStatusmessage("Token Expired");
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.getWriter().write(JsonUtils.convertObjectToJson(errorResponse));
        res.sendError(errorResponse.getStatuscode(), errorResponse.getStatusmessage());
        return;
      } catch (SignatureException e) {
        ResponseStatus errorResponse = new ResponseStatus();
        errorResponse.setStatuscode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setStatusmessage("Invalid Credentials");
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.getWriter().write(JsonUtils.convertObjectToJson(errorResponse));
        res.sendError(errorResponse.getStatuscode(), errorResponse.getStatusmessage());
        return;

      }
    }
    chain.doFilter(req, res);
  }

  private boolean excludeRequestURIPattern(String requesturi) {
    String pattern1 = "/*/api/v1/usermanagement/login";
    String pattern2 = "/*/swagger-ui.html";
    String pattern3 = "/*/api/v1/usermanagement/users/*/forgotpassword";
    String pattern4 = "/*/api/v1/usermanagement/users/resetpassword/*";
    String pattern5 = "/*/api/v1/usermanagement/users/changepassword";
    String pattern6 = "/*/ec/*";
    String pattern7 = "/*/api/v1/usermanagement/verifyGoogleIdToken";
    String pattern8 = "/*/api/v1/usermanagement/verifyGoogleIdTokenDart";
    String pattern9 = "/*/api/v1/usermanagement/signup";
    String pattern10 = "/*/api/v1/email/*";
    Pattern p = null;
    boolean patternmatched = false;


    List<Pattern> patterns = new ArrayList<Pattern>();
    // System.out.println("pattern1 = " + pattern1);
    p = Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);

    p = Pattern.compile(pattern2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    // System.out.println("pattern2 = " + pattern2);
    patterns.add(p);


    // System.out.println("pattern3 = " + pattern3);
    p = Pattern.compile(pattern3, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);
    // System.out.println("pattern4 = " + pattern4);
    p = Pattern.compile(pattern4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);
    // System.out.println("pattern5 = " + pattern5);
    p = Pattern.compile(pattern5, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);
    
    p = Pattern.compile(pattern6, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);

    p = Pattern.compile(pattern9, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);

   
    p = Pattern.compile(pattern7, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);

    p = Pattern.compile(pattern8, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);
    
    p = Pattern.compile(pattern10, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    patterns.add(p);
    
    for (Pattern pattern : patterns) {
      Matcher m = pattern.matcher(requesturi);
      if (m.find()) {
        patternmatched = true;
        return true;
      }
    }
    return patternmatched;
  }



}
