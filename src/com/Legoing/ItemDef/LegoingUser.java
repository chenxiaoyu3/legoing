package com.Legoing.ItemDef;

public class LegoingUser {

	String userName;
	String userPass;
	int userIndex;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public int getUserIndex() {
		return userIndex;
	}
	public void setUserIndex(int userIndex) {
		this.userIndex = userIndex;
	}
	
	UserItems items;
	public UserItems getItems() {
		return items;
	}
	public void setItems(UserItems items) {
		this.items = items;
	}

}
