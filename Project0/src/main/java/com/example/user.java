package com.example;

public class user {
	String username;
	String password;
	String type;
	String firstname;
	String lastname;
	
	public user() {
	}
	
	public user(String username, String password, String type, String firstname, String lastname) {
		super();
		this.username = username;
		this.password = password;
		this.type = type;
		this.firstname = firstname;
		this.lastname = lastname;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	@Override
	public String toString() {
		return "user [username=" + username +  ", type=" + type + ", firstname=" + firstname
				+ ", lastname=" + lastname + "]";
	}

	
	
	
}
