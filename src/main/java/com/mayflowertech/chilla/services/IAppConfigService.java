package com.mayflowertech.chilla.services;

import java.util.List;

import com.mayflowertech.chilla.entities.AppConfig;

public interface IAppConfigService {
  AppConfig add(AppConfig object);
  AppConfig get(String id);
  AppConfig update(AppConfig object);
  List<AppConfig> getAllAppConfig();
  List<AppConfig> getAllAppConfig(String configGroup);
  List<AppConfig> getAllAppConfig(String configGroup, String configKey);
  AppConfig getAppConfig(String configGroup, String configKey,String configParam);
  boolean isExists(AppConfig appConfig);
  int getNextDisplayOrder(AppConfig appConfig);
  AppConfig findByConfigParam(String configParam);
}
