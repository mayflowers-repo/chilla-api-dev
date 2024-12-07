package com.mayflowertech.chilla.entities.pojo;

import com.mayflowertech.chilla.entities.Address;

public class AddressPojo {
	
	private String flatDetails;
	private String apartmentDetails;
	private String pincode;
	private String landmark;
	private String district;
	private String state;
	
	public String getFlatDetails() {
		return flatDetails;
	}
	public void setFlatDetails(String flatDetails) {
		this.flatDetails = flatDetails;
	}
	public String getApartmentDetails() {
		return apartmentDetails;
	}
	public void setApartmentDetails(String apartmentDetails) {
		this.apartmentDetails = apartmentDetails;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	private String map;
	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public static Address convertToAddress(AddressPojo pojo) {
        if (pojo == null) {
            return null;
        }

        Address address = new Address();
        address.setFirst(pojo.getFlatDetails());
        address.setSecond(pojo.getApartmentDetails());
        address.setPincode(pojo.getPincode());
        address.setLandmark(pojo.getLandmark());
        address.setDistrict(pojo.getDistrict());
        address.setState(pojo.getState());
        return address;
    }
	
}
