package com.mayflowertech.chilla.services.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.UserProfile;
import com.mayflowertech.chilla.repositories.IUserProfileRepository;
import com.mayflowertech.chilla.repositories.IUserRepository;
import com.mayflowertech.chilla.services.IUserProfileService;

@Service
public class UserProfileService implements IUserProfileService{

    @Autowired
    IUserProfileRepository userProfileRepository;
    
    @Autowired
    IUserRepository userRepository;
    
    @Override
    public UserProfile getByUserId(String userid) {
        if(userid == null) return null;
        if(userid.trim().length() <= 0) return null;
        User user = userRepository.findById(UUID.fromString(userid));
        UserProfile userProfile = userProfileRepository.findByUser(user);
        if(userProfile == null) {
        	userProfile = new UserProfile();
        	userProfile.setUser(user);
        	userProfile = userProfileRepository.save(userProfile);
        }
        return userProfile;
    }

    @Override
    public UserProfile add(UserProfile userProfile) {
        if(userProfile == null) return null;
        if(userProfile.getUser() == null) return null;
        UserProfile tmpUserProfile = userProfileRepository.findByUser(userProfile.getUser());
        if(tmpUserProfile != null)
            userProfile.setId(tmpUserProfile.getId());
        return userProfileRepository.save(userProfile);
    }

}
