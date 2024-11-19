package com.mayflowertech.chilla.config;

public interface Constants {
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String AUTHORIZATION = "Authorization";

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final String SIGNING_KEY = "1nGiN1M";
    public static final String AUTHORITIES_KEY = "scopes";
    public static final String AES_SIGNINGSALT = "1nGiN1M1nGiN1M1n";
    public static final String PASSWORD_SALT = "1nGiN1M1nGiN1M1nGiN1M";
    public static final String INITIAL_PASSWORD = "abcd";
    public static final String TEST_OTP = "123456";
    
    public static final String GCP_BUCKET_NAME = "karuthal-docubucket1";
    
    
}
