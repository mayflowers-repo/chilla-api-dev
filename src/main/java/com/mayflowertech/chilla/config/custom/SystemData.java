package com.mayflowertech.chilla.config.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mayflowertech.chilla.entities.AppConfig;
import com.mayflowertech.chilla.entities.AuthUser;
import com.mayflowertech.chilla.entities.Permission;
import com.mayflowertech.chilla.entities.Role;
import com.mayflowertech.chilla.enums.SystemConfigGroup;
import com.mayflowertech.chilla.enums.SystemRoles;
import com.mayflowertech.chilla.enums.ValueTypes;
import com.mayflowertech.chilla.services.IAppConfigService;
import com.mayflowertech.chilla.services.IAuthUserService;
import com.mayflowertech.chilla.services.IPermissionService;
import com.mayflowertech.chilla.services.IRoleService;

@Component
public class SystemData {
	@Autowired
	private IAuthUserService userService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IAppConfigService appConfigService;

	@Autowired
	ApplicationConfigParams applicationConfigParams;

	@Autowired
	private IPermissionService permissionService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger logger = LoggerFactory.getLogger(SystemData.class);

	@EventListener
	public void appReady(ApplicationReadyEvent event) throws Exception, Throwable {
		AuthUser user = null;
		if (applicationConfigParams.getEnvironment() != null
				&& applicationConfigParams.getEnvironment().equalsIgnoreCase("prod")) {
			user = new AuthUser("systemadmin", "admin@test.com", "systemadmin1");
		} else {
			user = new AuthUser("systemadmin", "admin@test.com", "systemadmin");
		}

		user.setFirstName("System");
		user.setLastName("Administrator");
		if (!userService.isExist(user))
			user = userService.createUser(user);
		else {
			System.out.println("Getting user " + user.getUsername());
			user = userService.getUser(user.getUsername());
			user.setFirstName("System");
			user.setLastName("Administrator");
			userService.updateUser(user, false);

		}
		Permission workflowListPerm = new Permission();
		workflowListPerm.setName("workflow listing");
		workflowListPerm.setLink("/workflows");
		workflowListPerm = permissionService.createPermission(workflowListPerm);

		Permission workflowPerm = new Permission();
		workflowPerm.setName("workflow edit");
		workflowPerm.setLink("/workflows/edit");
		workflowPerm = permissionService.createPermission(workflowPerm);

		Permission userPagePerm = new Permission();
		userPagePerm.setName("User edit");
		userPagePerm.setLink("/users");
		userPagePerm = permissionService.createPermission(userPagePerm);

		Permission contactGroupPerm = new Permission();
		contactGroupPerm.setName("Contact groups");
		contactGroupPerm.setLink("/users/contact-group");
		contactGroupPerm = permissionService.createPermission(contactGroupPerm);

		Permission configPerm = new Permission();
		configPerm.setName("configurations edit");
		configPerm.setLink("/workflows/config");
		configPerm = permissionService.createPermission(configPerm);

		Role role = new Role("SYSTEMADMIN", "SYSTEMADMIN");
		if (!roleService.isExists(role.getRolename())) {
			role.addPermission(workflowPerm);
			role.addPermission(workflowListPerm);
			role.addPermission(userPagePerm);
			role.addPermission(contactGroupPerm);
			role.addPermission(configPerm);
			System.out.println("Creating role " + role.getRolename());
			role = roleService.createRole(role);

		} else {
			System.out.println("Getting role " + role.getRolename());
			role = roleService.getRoleByName(role.getRolename());
		}

		if (!userService.hasRole(user, role)) {
			System.out.println("Role added");
			user.getRoles().add(role);
			user = userService.addRoletoUser(user, role);
		}

		role = new Role(SystemRoles.ADMIN.getRoleCode(), SystemRoles.ADMIN.name());
		if (!roleService.isExists(role.getRolename())) {
			System.out.println("Creating role " + role.getRolename());
			role.addPermission(workflowPerm);
			role.addPermission(userPagePerm);
			role.addPermission(contactGroupPerm);
			role.addPermission(configPerm);
			role.addPermission(workflowListPerm);
			role = roleService.createRole(role);
		}

		role = new Role(SystemRoles.GUEST.getRoleCode(), SystemRoles.GUEST.name());
		if (!roleService.isExists(role.getRolename())) {
			System.out.println("Creating role " + role.getRolename());
			role = roleService.createRole(role);
		}

		role = new Role(SystemRoles.CAREGIVER.getRoleCode(), SystemRoles.CAREGIVER.name());
		if (!roleService.isExists(role.getRolename())) {
			System.out.println("Creating role " + role.getRolename());
			role = roleService.createRole(role);
		}

		role = new Role(SystemRoles.CUSTOMER.getRoleCode(), SystemRoles.CUSTOMER.name());
		if (!roleService.isExists(role.getRolename())) {
			System.out.println("Creating role " + role.getRolename());
			role = roleService.createRole(role);
		}

		role = new Role(SystemRoles.MANAGER.getRoleCode(), SystemRoles.MANAGER.name());
		if (!roleService.isExists(role.getRolename())) {
			System.out.println("Creating role " + role.getRolename());
			// role.addPermission(userPagePerm);
			role = roleService.createRole(role);
		}

		role = new Role(SystemRoles.STUDENT.getRoleCode(), SystemRoles.STUDENT.name());
		if (!roleService.isExists(role.getRolename())) {
			System.out.println("Creating role " + role.getRolename());
			role = roleService.createRole(role);
		}

		AppConfig appconfig = null;

		appconfig = new AppConfig(SystemConfigGroup.APPLICATION.getCode(), "APPLICATION", "user.passwordreseturl",
				applicationConfigParams.getPasswordResetUrl(), true, 1, ValueTypes.STRING.getCode(),
				"Password Reset URL");
		if (!appConfigService.isExists(appconfig)) {
			appConfigService.add(appconfig);
		}

		// create a dummy user
		user = new AuthUser("render", "render@test.com", "redner");
		user.setFirstName("Render user");
		user.setLastName("dummy");

		if (!userService.isExist(user)) {
			user = userService.createUser(user);
		} else {
			user = userService.getUser("render");
		}

		role = roleService.getRoleByName(SystemRoles.GUEST.getRoleCode());
		userService.addRoletoUser(user, role);

		user = new AuthUser("caregiver", "caregiver@test.com", "care");
		user.setFirstName("Care");
		user.setLastName("Giver");

		if (!userService.isExist(user)) {
			user = userService.createUser(user);
		} else {
			user = userService.getUser("caregiver");
		}

		role = roleService.getRoleByName(SystemRoles.CAREGIVER.getRoleCode());
		userService.addRoletoUser(user, role);

		user = new AuthUser("customer1", "customer1@test.com", "cust");
		user.setFirstName("Customer");
		user.setLastName("First");

		if (!userService.isExist(user)) {
			user = userService.createUser(user);
		} else {
			user = userService.getUser("customer1");
		}

		role = roleService.getRoleByName(SystemRoles.CUSTOMER.getRoleCode());
		userService.addRoletoUser(user, role);

		user = new AuthUser("admin1", "admin1@test.com", "admin");
		user.setFirstName("Admin");
		user.setLastName("First");

		if (!userService.isExist(user)) {
			user = userService.createUser(user);
		} else {
			user = userService.getUser("admin1");
		}

		role = roleService.getRoleByName(SystemRoles.ADMIN.getRoleCode());
		userService.addRoletoUser(user, role);

	

	}

	public void test() {
		// System.out.println(featureService);
	}
}
