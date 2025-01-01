package com.mayflowertech.chilla.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Proxy;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Proxy(lazy = false)
@Entity
@Table(name = "roles", uniqueConstraints=
@UniqueConstraint(columnNames={"rolename","rolecategory"}))
public class Role extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", length = 50,updatable = false, nullable = false)
	@org.hibernate.annotations.Type(type = "pg-uuid")
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
	private Set<AuthUser> users = new HashSet<AuthUser>();
	
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


	public Set<AuthUser> getUsers() {
		return users;
	}


	public void setUsers(Set<AuthUser> users) {
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
