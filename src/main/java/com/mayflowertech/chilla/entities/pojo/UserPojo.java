package com.mayflowertech.chilla.entities.pojo;

import com.mayflowertech.chilla.entities.Address;
import com.mayflowertech.chilla.entities.User;

public class UserPojo {
	private String email;
	private String password;
	private String mobile;
	private String persona;
	private String firstName;
	private String lastName;
	private String id;
	private AddressPojo address;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPersona() {
		return persona;
	}
	public void setPersona(String persona) {
		this.persona = persona;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AddressPojo getAddress() {
		return address;
	}
	public void setAddress(AddressPojo address) {
		this.address = address;
	}
	
	
	 public static UserPojo copyToUserPojo(User user) {
	        if (user == null) {
	            return null;
	        }

	        UserPojo userPojo = new UserPojo();

	        // Copying simple fields
	        userPojo.setEmail(user.getEmail());
	        userPojo.setPassword(user.getPassword());
	        userPojo.setMobile(user.getMobile());
	        userPojo.setFirstName(user.getFirstName());
	        userPojo.setLastName(user.getLastName());
	        userPojo.setId(user.getId() != null ? user.getId().toString() : null);

	        // Selecting the first address from the set
	        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
	            Address firstAddress = user.getAddresses().iterator().next(); // Get the first address
	            AddressPojo addressPojo = new AddressPojo();
	            addressPojo.setFlatDetails(firstAddress.getFirst());
	            addressPojo.setApartmentDetails(firstAddress.getSecond());
	            addressPojo.setState(firstAddress.getState());
	            addressPojo.setPincode(firstAddress.getPincode());
	            userPojo.setAddress(addressPojo);
	        } else {
	            userPojo.setAddress(null); // No address present
	        }

	        return userPojo;
	    }
}
