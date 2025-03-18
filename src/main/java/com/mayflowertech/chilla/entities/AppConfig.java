package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "app_config", uniqueConstraints=
@UniqueConstraint(columnNames={"config_group", "config_key", "config_param"}))
@DynamicInsert
@DynamicUpdate
public class AppConfig extends BaseEntity implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 64, updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    
    @Column(name = "config_group",length = 100, nullable = true)
    private String configGroup;
    
    @Column(name = "config_key",length = 100, nullable = true)
    private String configKey;
    
    @Column(name = "config_param",length = 100, nullable = true)
    private String configParam;
    
    @Column(name = "config_value",length = 2000)
    private String configValue;
    
    @Column(name = "display_text",length = 2000)
    private String displayText;

    @Column(name = "editable", nullable = true)
    private boolean editable;
    
    @Column(name = "order_of_display")
    private int orderOfDisplay;
    
    @Column(name = "value_type",length = 100, nullable = true)
    private String valueType;

    public AppConfig() {
        super();
    }


    public AppConfig(String configgroup, String configkey, String configparam,
            String configvalue, boolean editable, int orderofdisplay,
            String valuetype, String displaytext) {
        super();
        this.configGroup = configgroup;
        this.configKey = configkey;
        this.configParam = configparam;
        this.configValue = configvalue;
        this.editable = editable;
        this.orderOfDisplay = orderofdisplay;
        this.valueType = valuetype;
        this.displayText = displaytext;
    }


    public UUID getId() {
      return id;
    }


    public void setId(UUID id) {
      this.id = id;
    }


    public String getConfigGroup() {
      return configGroup;
    }


    public void setConfigGroup(String configGroup) {
      this.configGroup = configGroup;
    }


    public String getConfigKey() {
      return configKey;
    }


    public void setConfigKey(String configKey) {
      this.configKey = configKey;
    }


    public String getConfigParam() {
      return configParam;
    }


    public void setConfigParam(String configParam) {
      this.configParam = configParam;
    }


    public String getConfigValue() {
      return configValue;
    }


    public void setConfigValue(String configValue) {
      this.configValue = configValue;
    }


    public String getDisplayText() {
      return displayText;
    }


    public void setDisplayText(String displayText) {
      this.displayText = displayText;
    }


    public boolean isEditable() {
      return editable;
    }


    public void setEditable(boolean editable) {
      this.editable = editable;
    }


    public int getOrderOfDisplay() {
      return orderOfDisplay;
    }


    public void setOrderOfDisplay(int orderOfDisplay) {
      this.orderOfDisplay = orderOfDisplay;
    }


    public String getValueType() {
      return valueType;
    }


    public void setValueType(String valueType) {
      this.valueType = valueType;
    }
 
    
    
}