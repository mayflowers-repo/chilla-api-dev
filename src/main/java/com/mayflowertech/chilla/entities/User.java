package com.mayflowertech.chilla.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Proxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonFilter("UserFilter")
@Proxy(lazy = false)
@Entity
@Table(name = "users", uniqueConstraints=
@UniqueConstraint(columnNames={"username"}))
public class User extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", length = 50, updatable = false, nullable = false)
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @NaturalId(mutable = true)
    @Column(name = "username", nullable = false, length = 150)
    private String username;

    @Column(name = "firstname", nullable = true, length = 255)
    private String firstName;

    @Column(name = "lastname", nullable = true, length = 255)
    private String lastName;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", nullable = true)
    private String password;

    private String authtoken;
    
    @Column(name = "status", nullable = true, length = 25)
    private String status;
    
    @Column(name = "mobile", nullable = true, length = 15)
    private String mobile;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<Role>();

    @Column(name = "photo_url", nullable = true, length = 250)
    private String photoUrl;

    @Column(name = "provider", nullable = true, length = 250)
    private String provider;

    @Column(name = "social_id", nullable = true, length = 50)
    private String socialId;

    @Transient
    private List<String> assignedRoles;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = true)
    private Address address;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        roles.add(role);
        if (role != null) {
            role.getUsers().add(this);
        }
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public List<String> getAssignedRoles() {
        return assignedRoles;
    }

    public void setAssignedRoles(List<String> assignedRoles) {
        this.assignedRoles = assignedRoles;
    }

    @JsonIgnore
    @Override
    public List<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        // Build user's authorities
        if (this.getRoles().size() > 0) {
            for (Role userRole : this.getRoles()) {
                setAuths.add(new SimpleGrantedAuthority(userRole.getRolecategory()));
            }
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(setAuths);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public User() {
    }

    public User(String name, String email, String password) {
        this.username = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    @JsonIgnore
    public boolean hasPermission(String key) {
        if (key.indexOf("_") > 0) {
            String[] parts = key.split("_");
            key = parts[parts.length - 1];
        }

        if (this.getRoles().size() > 0) {
            for (Role userRole : this.getRoles()) {
                if (userRole.getPermissions().size() > 0) {
                    for (Permission perm : userRole.getPermissions()) {
                        if (perm.getName().endsWith(key)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    
    public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
    @Column(name = "city", nullable = true, length = 25)
    private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='"+status+  '\'' +
                '}';
    }
	
	@Transient
	private Long studentId;
	

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	@Transient
	private Long managerId;
	
	
	@Column(name = "waiting_otp", nullable = true)
	protected boolean otpWaiting = false;

	public boolean isOtpWaiting() {
		return otpWaiting;
	}

	public void setOtpWaiting(boolean otpWaiting) {
		this.otpWaiting = otpWaiting;
	}
	


	@Column(name = "registered")
	protected boolean registered = false;

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

}
