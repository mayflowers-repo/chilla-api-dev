package com.mayflowertech.chilla.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Proxy(lazy = false)
@Entity
@Table(name = "locations")
public class Location {
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "pincode", nullable = false, length = 10)
    private String pincode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "district_name", nullable = false, length = 255)
    private String districtName;

    @Column(name = "state_name", nullable = false, length = 255)
    private String stateName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
    
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id == location.id &&
               Objects.equals(pincode, location.pincode) &&
               Objects.equals(name, location.name) &&
               Objects.equals(districtName, location.districtName) &&
               Objects.equals(stateName, location.stateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pincode, name, districtName, stateName);
    }

    // ToString for debugging
    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", pincode='" + pincode + '\'' +
                ", name='" + name + '\'' +
                ", districtName='" + districtName + '\'' +
                ", stateName='" + stateName + '\'' +
                '}';
    }
}
