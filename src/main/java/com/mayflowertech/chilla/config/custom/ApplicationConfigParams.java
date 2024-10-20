package com.mayflowertech.chilla.config.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mayflowertech.chilla.entities.AppConfig;
import com.mayflowertech.chilla.enums.SystemConfigGroup;
import com.mayflowertech.chilla.services.impl.AppConfigService;

@Configuration
@PropertySource(value = { "classpath:application.properties}" }, ignoreResourceNotFound = true)
public class ApplicationConfigParams {
    
    @Autowired
    AppConfigService appConfigService;
   
    /*
    @Value("${user.passwordreseturl:http://localhost/resetpassword}")
    private String passwordreseturl;

    public String getPasswordreseturl() {
        if(appConfigService != null){
            AppConfig appconfig = appConfigService.getAppConfig(SystemConfigGroup.APPLICATION.getCode(), "APPLICATION", "user.passwordreseturl");
            if(appconfig != null){
                this.setPasswordreseturl(appconfig.getConfigValue().trim());
            }
        }
        return passwordreseturl;
    }

    public void setPasswordreseturl(String passwordreseturl) {
        this.passwordreseturl = passwordreseturl;
    }
    */
    
    @Value("${user.passwordreseturl}")
    private String passwordResetUrl;

    
    @Value("${server.port}")
    private String springBootPort;

    public String getSpringBootPort() {
      return springBootPort;
    }

    public void setSpringBootPort(String springBootPort) {
      this.springBootPort = springBootPort;
    }

    @Value("${hostname}")
    private String hostname;


    @Value("${angular.port}")
    private String angularPort;

    public String getPasswordResetUrl() {
      return passwordResetUrl;
    }
    
    @Value("${server.env}")
    private String environment;
    

    public void setPasswordResetUrl(String passwordResetUrl) {
      this.passwordResetUrl = passwordResetUrl;
    }

    public String getHostname() {
      return hostname;
    }

    public void setHostname(String hostname) {
      this.hostname = hostname;
    }

    public String getAngularPort() {
      return angularPort;
    }

    public void setAngularPort(String angularPort) {
      this.angularPort = angularPort;
    }

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

    

}