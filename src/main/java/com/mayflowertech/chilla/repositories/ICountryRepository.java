package com.mayflowertech.chilla.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.Country;

public interface ICountryRepository extends JpaRepository<Country, Integer>{

}
