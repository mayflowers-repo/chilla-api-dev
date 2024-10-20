package com.mayflowertech.chilla.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mayflowertech.chilla.entities.AppConfig;
import com.mayflowertech.chilla.entities.CustomStatusMessage;
import com.mayflowertech.chilla.services.IAppConfigService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/disinv/api/v1")
public class AppConfigController {
  @Autowired
  private IAppConfigService appConfigService;



  
  @ApiOperation(value = "View a list of System Configurations")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved list"),
      @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
      @ApiResponse(code = 403,
          message = "Accessing the resource you were trying to reach is forbidden"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/systemmanagement/list", method = {RequestMethod.GET},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AppConfig>> getAppConfig() {

    
    /*
    EndUserContact contact = endUserContactService.findByUserMobile("887678");
    System.out.println("found user "+contact);
    WorkflowResult res = new WorkflowResult();
    res.setContact(contact);
    
    WorkflowResultScreen screen1 = new WorkflowResultScreen();
    screen1.setChoiceCode(1);
    screen1.setSessionId("223");
    screen1.setWorkflowResult(res);
    
    Set<WorkflowResultScreen> resultScreens = new HashSet<WorkflowResultScreen>();
    resultScreens.add(screen1);
    res.setResultScreens(resultScreens );

    //testing result
    workflowResultService.createWorkflowResult(res);
      */  

    
    List<AppConfig> configlist = appConfigService.getAllAppConfig();
    return new ResponseEntity<List<AppConfig>>(configlist, HttpStatus.OK);
  }

  @ApiOperation(value = "Create/Update System Configuration", response = AppConfig.class)
  @ApiResponses(value = {@ApiResponse(code = 201, message = "AppConfig added successfully"),
      @ApiResponse(code = 409, message = "AppConfig already exists"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/systemmanagement/create", method = {RequestMethod.POST},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AppConfig> addEndUserContact(@RequestBody AppConfig appConfig) {
    AppConfig ret = null;
    if(appConfig.getId() != null) {
      ret = appConfigService.get(appConfig.getId().toString());      
      if(ret == null) {
        return new ResponseEntity(new CustomStatusMessage("ID is invalid"),
            HttpStatus.NOT_FOUND);
      }
      if(appConfig.getConfigGroup() != null) {
        ret.setConfigGroup(ret.getConfigGroup());
      }

      if(appConfig.getConfigValue() != null) {
        ret.setConfigValue(ret.getConfigValue());
      }
      if(appConfig.getDisplayText() != null) {
        ret.setDisplayText(ret.getDisplayText());
      }

    }
    appConfig = appConfigService.add(appConfig);
    return new ResponseEntity<AppConfig>(appConfig, HttpStatus.ACCEPTED);
  }

  @ApiOperation(value = "List System Configurations for a config group")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Listed System Configurations"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/systemmanagement/{configGroup}/appconfigs", method = {RequestMethod.GET},
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AppConfig>> listAppConfigs(
      @PathVariable("configGroup") String configGroup) {
    
    List<AppConfig> appConfig = appConfigService.getAllAppConfig(configGroup);
    return new ResponseEntity<List<AppConfig>>(appConfig, HttpStatus.OK);
  }

  @ApiOperation(value = "List System Configurations for a config group and config key")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Listed System Configurations"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/systemmanagement/{configGroup}/{configKey}/appconfigs",
      method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AppConfig>> listAppConfigs(
      @PathVariable("configGroup") String configGroup,
      @PathVariable("configKey") String configKey) {
    List<AppConfig> appConfig = appConfigService.getAllAppConfig(configGroup, configKey);
    return new ResponseEntity<List<AppConfig>>(appConfig, HttpStatus.OK);
  }

  @ApiOperation(value = "List System Configurations for a config group,config key and config param")
  @ApiResponses(value = {@ApiResponse(code = 202, message = "Listed System Configurations"),
      @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
  @RequestMapping(value = "/systemmanagement/{configGroup}/{configKey}/{configParam}/appconfigs",
      method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AppConfig> listAppConfigs(@PathVariable("configGroup") String configGroup,
      @PathVariable("configKey") String configKey,
      @PathVariable("configParam") String configParam) {
    AppConfig appConfig = appConfigService.getAppConfig(configGroup, configKey, configParam);
    return new ResponseEntity<AppConfig>(appConfig, HttpStatus.OK);
  }
}

