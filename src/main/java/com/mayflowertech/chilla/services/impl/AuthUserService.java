package com.mayflowertech.chilla.services.impl;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mayflowertech.chilla.config.AuthorizationConstants;
import com.mayflowertech.chilla.config.Constants;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.AuthUser;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.enums.UserStatus;
import com.mayflowertech.chilla.repositories.IAuthUserRepository;
import com.mayflowertech.chilla.services.IAuthUserService;
import com.mayflowertech.chilla.services.IRoleService;
import com.mayflowertech.chilla.utils.CommonUtils;
import com.mayflowertech.chilla.utils.PasswordUtils;

public class AuthUserService  implements UserDetailsService, IAuthUserService {
	private static final Logger logger = LoggerFactory.getLogger(AuthUserService.class);
	
	@Autowired
	private IRoleService roleService;

	@Autowired
	private IAuthUserRepository userRepository;
	
	@Override
	public boolean validateUser(AuthUser user) {
		String usrpwd = user.getPassword();
		user = userRepository.findByUsernameAndActive(user.getUsername(), true);
		if (user == null) {
			System.out.println("validate user is null");
		} else {

			System.out.println("validate user is not null");
			boolean passwordMatch = PasswordUtils.verifyUserPassword(usrpwd, user.getPassword(),
					Constants.PASSWORD_SALT);
			if (passwordMatch)
				return true;
			else {
				System.out.println("Password match failed for " + user.getUsername());
				return false;
			}

		}
		return false;
	}
	
	@Override
	public AuthUser createUser(AuthUser user) throws Throwable {
		AuthUser existingUser = getByEmail(user.getEmail());
		if (existingUser != null) {
			throw new CustomException("Email already in use");
		}
		if (isExist(user)) {
			return updateUser(user, false);
		}
		user.setPassword(PasswordUtils.generateSecurePassword(user.getPassword(), Constants.PASSWORD_SALT));

		user.setStatus(UserStatus.INACTIVE.getCode());
		user = userRepository.save(user);
		logger.info("user updated in db: " + user);
		return user;
	}

	@Override
	public AuthUser updateUser(AuthUser user, boolean changepassword) {
		logger.debug("updateUser  " + user);

		AuthUser ret = userRepository.findById(user.getId());
		if (ret != null) {
			if (user.getReasonForDeactivate() != null && !user.getReasonForDeactivate().isEmpty())
				ret.setReasonForDeactivate(user.getReasonForDeactivate());
			if (user.getReasonForDelete() != null && !user.getReasonForDelete().isEmpty())
				ret.setReasonForDelete(user.getReasonForDelete());
			if (user.getUsername() != null && !user.getUsername().isEmpty())
				ret.setUsername(user.getUsername());
			if (user.getEmail() != null && !user.getEmail().isEmpty())
				ret.setEmail(user.getEmail());
			if (user.getFirstName() != null && !user.getFirstName().isEmpty())
				ret.setFirstName(user.getFirstName());
			if (user.getLastName() != null && !user.getLastName().isEmpty())
				ret.setLastName(user.getLastName());
			if (user.getProvider() != null && !user.getProvider().isEmpty())
				ret.setProvider(user.getProvider());
			if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty())
				ret.setPhotoUrl(user.getPhotoUrl());

			if (changepassword)
				ret.setPassword(PasswordUtils.generateSecurePassword(user.getPassword(), Constants.PASSWORD_SALT));
			ret.setActive(user.isActive());
			return userRepository.save(ret);
		}
		return null;
	}

	@Override
	public AuthUser getUser(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public AuthUser getById(String id) {
		return userRepository.findById(UUID.fromString(id));
	}


	@Override
	public AuthUser getByEmail(String email) {
	    AuthUser user = userRepository.findByEmail(email);
	    return user;
	}


	@Override
	public AuthUser getByMobile(String mobile) {
		return userRepository.findByMobile(mobile);
	}

	@Override
	public boolean isExist(AuthUser user) {
		AuthUser ret = userRepository.findByUsername(user.getUsername());
		if (ret == null && user.getId() != null) {
			ret = userRepository.findById(user.getId());
		}
		if (ret == null)
			return false;
		else
			return true;
	}

	@Override
	@Transactional
	public AuthUser addRoletoUser(AuthUser user, Role role) {
		logger.info("addRoletoUser  " + user + "   role " + role);
		try {
			AuthUser ret = userRepository.findById(user.getId());
			if (role.getId() != null && role.getRolename() == null) {
				role = roleService.getById(role.getId().toString());
			}
			if ((ret != null) && (!this.hasRole(ret, role))) {
				ret.addRole(role);
				return userRepository.save(ret);
			}

			return user;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean hasRole(AuthUser user, Role role) {
		System.out.println("before  " + user + "   role " + role);

		user = userRepository.findByUsername(user.getUsername());
		logger.info("hasRole  " + user + "   role " + role);
		if (user != null) {
			if (user.getRoles() == null) {
				// System.out.println("user roles is null");
				return false;
			}
			if (user.getRoles().size() == 0) {
				// System.out.println("user has no roles");
				return false;
			}
			for (Role tmpRole : user.getRoles()) {
				if (tmpRole.getRolename().trim().equalsIgnoreCase(role.getRolename().trim()))
					return true;
			}
		} else {
			System.out.println("user is null");
		}
		return false;
	}


	@Override
	public AuthUser getCurrentLoggedInUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("Getting current logged in user");
		String username = "";
		AuthUser user = null;
		if (principal instanceof UserDetails) {

			username = ((UserDetails) principal).getUsername();
			System.out.println("Getting current logged in user : " + username);

		} else {

			username = principal.toString();
			System.out.println("else Getting current logged in user : " + username);

		}
		if (username.trim().length() > 0) {
			user = userRepository.findByUsername(username.trim());
		}
		return user;
	}


	@Override
	public AuthUser removeRolefromUser(AuthUser user, Role role) {
		logger.debug("removeRolefromUser  " + user + "   role " + role);
		AuthUser ret = userRepository.findById(user.getId());
		ret.removeRole(role);
		return userRepository.save(ret);
	}

	@Override
	public String generatePasswordResetLink(AuthUser user, String uri) {
		String ret = "";

		if (user == null)
			return ret;
		if (user.getUsername() == null)
			return ret;
		if (user.getUsername().trim().isEmpty())
			return ret;

		long ut1 = Instant.now().getEpochSecond();
		String data = uri + "##" + ut1 + "##" + user.getUsername();
		ret = CommonUtils.encrypt(data, AuthorizationConstants.AES_SIGNINGSALT, AuthorizationConstants.AES_SIGNINGSALT);

		return ret.replace("/", "**").replace("\n", "").replace("\r", "");
	}

	@Override
	public AuthUser getUserFromSignature(String data) {
		AuthUser user = null;

		if (data == null)
			return user;

		if (data.trim().isEmpty())
			return user;

		long ut1 = Instant.now().getEpochSecond();
		System.out.println("1 signature is \n" + data);

		String decryptdata = CommonUtils.decrypt(data, AuthorizationConstants.AES_SIGNINGSALT,
				AuthorizationConstants.AES_SIGNINGSALT);
		String[] tokens = decryptdata.split("##");

		System.out.println("decryptdata = " + decryptdata);
		System.out.println("Tokens : " + tokens.length);

		if (tokens.length == 3) {
			String username = tokens[1];
			System.out.println("Token user : " + username);
			String validstatus = tokens[2];
			if (validstatus.trim().equalsIgnoreCase("OK")) {
				user = this.getUser(username);
			}
		}

		return user;
	}

	@Override
	public AuthUser checkSocialUser(AuthUser user) throws CustomException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AuthUser user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthority(user));
	}
	
	private Set<SimpleGrantedAuthority> getAuthority(AuthUser user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRolecategory()));
		});
		return authorities;
	}


}
