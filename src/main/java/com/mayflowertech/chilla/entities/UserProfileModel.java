package com.mayflowertech.chilla.entities;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserProfileModel {
  private String dateofbirth;
  private String location;
  private String mobileno;
  
  private MultipartFile attachment;
  public String getDateofbirth() {
      return dateofbirth;
  }
  public void setDateofbirth(String dateofbirth) {
      this.dateofbirth = dateofbirth;
  }
  public String getLocation() {
      return location;
  }
  public void setLocation(String location) {
      this.location = location;
  }
  public String getMobileno() {
      return mobileno;
  }
  public void setMobileno(String mobileno) {
      this.mobileno = mobileno;
  }
  public MultipartFile getAttachment() {
      return attachment;
  }
  public void setAttachment(MultipartFile attachment) {
      this.attachment = attachment;
  }   
  
  
}
