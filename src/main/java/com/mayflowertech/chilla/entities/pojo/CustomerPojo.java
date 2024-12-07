package com.mayflowertech.chilla.entities.pojo;

import java.util.Iterator;
import java.util.Set;

import com.mayflowertech.chilla.entities.Address;
import com.mayflowertech.chilla.entities.Customer;

public class CustomerPojo {
	
	private Long customerId;
	private String job;
	private String city;
	private String country;
	private String email;
	private String username;
	private String password;
    private String mobile;
    private String registeredUserId;
    
	private AddressPojo address;
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getRegisteredUserId() {
		return registeredUserId;
	}
	public void setRegisteredUserId(String registeredUserId) {
		this.registeredUserId = registeredUserId;
	}
	public AddressPojo getAddress() {
		return address;
	}
	public void setAddress(AddressPojo address) {
		this.address = address;
	}
	
	private String firstName;
	private String lastName;

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

	
	public static CustomerPojo copyCustomerToPojo(Customer customer) {
	    if (customer == null) {
	        return null;
	    }

	    CustomerPojo customerPojo = new CustomerPojo();
	    customerPojo.setCustomerId(customer.getCustomerId());
	    customerPojo.setJob(customer.getJob());
	    customerPojo.setCity(customer.getCity());
	    customerPojo.setCountry(customer.getCountry() != null ? customer.getCountry().getName() : null);

	    if(customer.getRegisteredUser() != null) {
		    customerPojo.setRegisteredUserId(customer.getRegisteredUser().getId().toString());
		    customerPojo.setEmail(customer.getRegisteredUser().getEmail());
		    customerPojo.setUsername(customer.getRegisteredUser().getUsername());
		    customerPojo.setPassword(customer.getRegisteredUser().getPassword());
		    customerPojo.setMobile(customer.getRegisteredUser().getMobile());
		    customerPojo.setFirstName(customer.getRegisteredUser().getFirstName());
		    customerPojo.setLastName(customer.getRegisteredUser().getLastName());
		    Set<Address> addresses = customer.getRegisteredUser().getAddresses();
	        if (addresses != null && !addresses.isEmpty()) {
	            Iterator<Address> iterator = addresses.iterator();
	            if (iterator.hasNext()) {
	                Address firstAddress = iterator.next();
	                AddressPojo addressPojo = new AddressPojo();
	                addressPojo.setFlatDetails(firstAddress.getFirst());
	                addressPojo.setApartmentDetails(firstAddress.getSecond());
	                addressPojo.setPincode(firstAddress.getPincode());
	                addressPojo.setLandmark(firstAddress.getLandmark());
	                addressPojo.setDistrict(firstAddress.getDistrict());
	                addressPojo.setState(firstAddress.getState());
	                customerPojo.setAddress(addressPojo);
	            }
	        }

	    }

	    return customerPojo;
	}

	
	@Override
	public String toString() {
	    return "CustomerPojo{" +
	            "customerId=" + customerId +
	            ", job='" + job + '\'' +
	            ", city='" + city + '\'' +
	            ", country='" + country + '\'' +
	            ", email='" + email + '\'' +
	            ", username='" + username + '\'' +
	            ", password='" + password + '\'' +
	            ", mobile='" + mobile + '\'' +
	            ", registeredUserId='" + registeredUserId + '\'' +
	            ", address=" + address +
	            ", firstName='" + firstName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            '}';
	}

}
