package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Proxy;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Proxy(lazy = false)
@Entity
@Table(name = "roles", uniqueConstraints=
@UniqueConstraint(columnNames={"rolename","rolecategory"}))
public class Role extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", length = 50,updatable = false, nullable = false)
	@JdbcTypeCode(SqlTypes.UUID)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "rolename", nullable = false, length = 80)
	private String rolename;
	
	@Column(name = "rolecategory", nullable = false,length = 80)
	String rolecategory;
	
	@Column(name = "roledescription", length = 255)
	String roledescription;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	private Set<User> users = new HashSet<User>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_permissions", 
	joinColumns = @JoinColumn(name = "role_id"),
	inverseJoinColumns = @JoinColumn(name = "permission_id"))	
	private Set<Permission> permissions = new HashSet<Permission>();


	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public String getRolename() {
		return rolename;
	}


	public void setRolename(String rolename) {
		this.rolename = rolename;
	}


	public String getRolecategory() {
		return rolecategory;
	}


	public void setRolecategory(String rolecategory) {
		this.rolecategory = rolecategory;
	}


	public String getRoledescription() {
		return roledescription;
	}


	public void setRoledescription(String roledescription) {
		this.roledescription = roledescription;
	}


	public Set<User> getUsers() {
		return users;
	}


	public void setUsers(Set<User> users) {
		this.users = users;
	}


	public Set<Permission> getPermissions() {
		return permissions;
	}


	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	
	public Role() {
	}
	

	public Role(String rolename, String rolecategory) {
		super();
		this.rolename = rolename;
		this.rolecategory = rolecategory;
	}

	
	   public void addPermission(Permission permission) {
	        permissions.add(permission);
	        permission.getRoles().add(this);
	    }
	 
	    public void removePermission(Permission permission) {
	        permissions.remove(permission);
	        permission.getRoles().remove(this);
	    }
	    
	    
	    @Override
	    public boolean equals(Object o) {
	      if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        Role role = (Role) o;
	        if (id != role.id) return false;

	        return true;
	    }
	    
	    @Override
	    public String toString() {
	      return id+" "+rolename;
	    }
	    
	    @Override
	    public int hashCode() {
	      int result = id != null ? id.hashCode() : 0;
	        result = 31 * result;
	        return result;
	    }
	    
	    
 
}
