package com.mayflowertech.chilla.config;


public class AuthorizationConstants {
  public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
  public static final String SIGNING_KEY = "1nGiN1M";
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String AUTHORITIES_KEY = "scopes";
  public static final String AES_SIGNINGSALT = "1nGiN1M1nGiN1M1n";
  public static final String PASSWORD_SALT = "1nGiN1M1nGiN1M1nGiN1M";
  
  public enum AuthorizationLevel {
       SELF(10,"self"), GROUP_LOC(20,"localGroup"), GROUP_GBL(30,"globalGroup"), ALL(40,"all");
  
      private int code;
      private String name;
      
      private AuthorizationLevel(int code, String name) { this.code = code; this.name = name;}
      
      public int getAuthorizationCode() { return code; }
      public String getAuthorizationName() { return name; }
      
      public AuthorizationLevel getAuthorizationLevel(String name) {
          switch (name) {
          case "self":
              return SELF;
          case "localGroup":
              return GROUP_LOC;
          case "globalGroup":
              return GROUP_GBL;
          case "all":
              return ALL;

          default:
              return null;
          }
      }
  }

}
