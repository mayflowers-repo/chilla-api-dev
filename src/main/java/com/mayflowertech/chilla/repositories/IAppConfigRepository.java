package com.mayflowertech.chilla.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mayflowertech.chilla.entities.AppConfig;

public interface IAppConfigRepository extends
JpaRepository<AppConfig, Serializable> {

List<AppConfig> findByDeletedOrderByConfigGroupAscConfigKeyAscConfigParamAscOrderOfDisplayAsc(
    boolean deleted);

List<AppConfig> findByDeleted(boolean deleted, Sort sort);

List<AppConfig> findByConfigKeyAndDeletedOrderByConfigParamAscOrderOfDisplayAsc(
    String configKey, boolean deleted);

List<AppConfig> findByConfigKeyAndConfigParamAndDeletedOrderByOrderOfDisplayAsc(
    String configKey, String configParam, boolean deleted);

Page<AppConfig> findByDeleted(boolean deleted, Pageable pageable);

List<AppConfig> findByConfigGroupOrderByOrderOfDisplayAsc(
    String configGroup);

List<AppConfig> findByConfigGroupAndConfigKeyOrderByOrderOfDisplayAsc(
    String configGroup,String configKey);

AppConfig findById(UUID id);

AppConfig findByConfigGroupAndConfigKeyAndConfigParam(String configGroup,
    String configKey, String configParam);

AppConfig findByConfigParam(String configParam);
}
