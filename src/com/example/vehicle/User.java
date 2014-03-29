package com.example.vehicle;

public class User {
	private long id;
	private String username;
	private String email;
	private String password;

	public User() {
		this.id = -1;
		this.username = "";
		this.email = "";
		this.password = "";
	}

	public User(long id, String username, String email, String password) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "{id:" + getId() + ", un:" + getUsername() + ", em:" + getEmail() + ", pw:"
				+ getPassword() + "}";
	}
}
