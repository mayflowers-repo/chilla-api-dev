package com.mayflowertech.chilla.services;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.AuthUser;
import com.mayflowertech.chilla.entities.Role;

public interface IAuthUserService {
	AuthUser createUser(AuthUser user) throws Exception, Throwable;
	AuthUser updateUser(AuthUser user, boolean changepassword);
	boolean validateUser(AuthUser user);
	AuthUser getUser(String username);
	AuthUser getById(String id);
	AuthUser getByEmail(String email);
	AuthUser getByMobile(String mobile);
	boolean isExist(AuthUser user);	
	public AuthUser addRoletoUser(AuthUser user, Role role);
	boolean hasRole(AuthUser user, Role role);
	public AuthUser getCurrentLoggedInUser();
	AuthUser removeRolefromUser(AuthUser user, Role role);
	String generatePasswordResetLink(AuthUser user, String uri);
	AuthUser getUserFromSignature(String signature);
    AuthUser checkSocialUser(AuthUser user) throws CustomException;
}
