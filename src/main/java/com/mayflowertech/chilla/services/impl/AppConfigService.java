package com.mayflowertech.chilla.services.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.entities.AppConfig;
import com.mayflowertech.chilla.repositories.IAppConfigRepository;
import com.mayflowertech.chilla.services.IAppConfigService;

@Service
public class AppConfigService implements IAppConfigService{

    @Autowired
    private IAppConfigRepository appConfigRepository;

    @Override
    public List<AppConfig> getAllAppConfig() {
        return appConfigRepository.findAll();
    }

    
    

    @Override
    public List<AppConfig> getAllAppConfig(String configGroup) {
        // TODO Auto-generated method stub
        System.out.println("getAllAppConfig configgroup  = " + configGroup);
        return appConfigRepository.findByConfigGroupOrderByOrderOfDisplayAsc(configGroup);
    }

    @Override
    public AppConfig getAppConfig(String configGroup, String configKey,
            String configParam) {
        if(!appConfigRepository.findByConfigKeyAndConfigParamAndDeletedOrderByOrderOfDisplayAsc(configKey, configParam, false).isEmpty())
            return appConfigRepository.findByConfigKeyAndConfigParamAndDeletedOrderByOrderOfDisplayAsc(configKey, configParam, false).get(0);
        return null;
    }

    @Override
    public boolean isExists(AppConfig appConfig) {
        AppConfig tmpAppConfig = appConfigRepository
                .findByConfigGroupAndConfigKeyAndConfigParam(appConfig.getConfigGroup(), 
                    appConfig.getConfigKey(), appConfig.getConfigParam());
        if(tmpAppConfig != null) return true;
        return false;
    }

    @Override
    public int getNextDisplayOrder(AppConfig appConfig) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public AppConfig add(AppConfig object) {        
        return appConfigRepository.save(object);
        
    }



    @Override
    public AppConfig update(AppConfig object) {
        object =  appConfigRepository.save(object);
//      InflowApiApplication.restart();
        return object;
    }



    @Override
    public AppConfig get(String id) {
        return appConfigRepository.findById(UUID.fromString(id));
    }




    @Override
    public List<AppConfig> getAllAppConfig(String configGroup, String configKey) {
//      System.out.println("getAllAppConfig configgroup  = " + configGroup);
        return appConfigRepository.findByConfigGroupAndConfigKeyOrderByOrderOfDisplayAsc(configGroup, configKey);
    }




	@Override
	public AppConfig findByConfigParam(String configParam) {
		return appConfigRepository.findByConfigParam(configParam);
	}
    
}
