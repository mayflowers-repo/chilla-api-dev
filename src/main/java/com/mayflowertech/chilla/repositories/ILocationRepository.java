package com.mayflowertech.chilla.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mayflowertech.chilla.entities.Location;

public interface ILocationRepository extends JpaRepository<Location, Integer> {
	List<Location> findByPincode(String pincode);
       
    // Get all states (unique)
    @Query("SELECT DISTINCT l.stateName FROM Location l")
    List<String> findAllStates();    
    
    @Query("SELECT DISTINCT l.districtName FROM Location l WHERE UPPER(l.stateName) = :stateName")
    List<String> findDistinctDistrictsByStateName(@Param("stateName") String stateName);
}