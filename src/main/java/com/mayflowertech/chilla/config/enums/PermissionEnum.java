package com.mayflowertech.chilla.config.enums;

public enum PermissionEnum {
  MANAGE_WORKFLOW_DESIGN("Add or update workflows"), 
  MANAGE_USER("Activate, deactivate, add/modify roles for a user"), 
  MANAGE_CAMPAIGN("Start a campaign"), 
  VIEW_CAMPAIGN_RESULTS("View the results of a campaign");

  private final String name;

  private PermissionEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }


}
