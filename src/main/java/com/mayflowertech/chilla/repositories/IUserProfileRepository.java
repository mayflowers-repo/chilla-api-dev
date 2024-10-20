package com.mayflowertech.chilla.repositories;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.UserProfile;

public interface IUserProfileRepository  extends
JpaRepository<UserProfile, Serializable>{
    UserProfile findByUser(User user);
}
