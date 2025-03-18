package com.mayflowertech.chilla.test;

import java.util.Collections;
import java.util.Date;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationServiceForTest {
  static final long EXPIRATIONTIME = 864_000_000; // 10 days
     static final String SECRET = "systemadmin";
     static final String TOKEN_PREFIX = "Bearer";
     static final String HEADER_STRING = "Authorization";

     public static void addAuthentication(HttpServletResponse res, String username, String password) {

         String jwt = createToken(username, password);

         res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
     }

     public static Authentication getAuthentication(HttpServletRequest request) {
         String token = request.getHeader(HEADER_STRING);
         if (token != null) {
             // parse the token.
             String user = Jwts.parser()
                     .setSigningKey(SECRET)
                     .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                     .getBody()
                     .getSubject();

             return user != null ?
                     new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) :
                         null;
         }
         return null;
     }

     public static String createToken(String username, String password) {
         String jwt = Jwts.builder()
                 .setSubject(username)
                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                 .signWith(SignatureAlgorithm.HS512, password)
                 .compact();

         return jwt;
     }
}
