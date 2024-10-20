package com.mayflowertech.chilla.services;

import com.mayflowertech.chilla.entities.UserProfile;

public interface IUserProfileService {
  UserProfile getByUserId(String userid);
  UserProfile add(UserProfile userProfile);
}
