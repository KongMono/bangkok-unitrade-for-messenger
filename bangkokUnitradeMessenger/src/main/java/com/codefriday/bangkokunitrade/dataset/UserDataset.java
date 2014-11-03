package com.codefriday.bangkokunitrade.dataset;

public class UserDataset {

	/**
	 * 
	 1 = IT System (System Admin) 2 = Administrator 3 = Sale 4 = Product 5 =
	 * Messenger
	 * 
	 */
	public static final int Type_System = 1;
	public static final int Type_Administrator = 2;
	public static final int Type_Sale = 3;
	public static final int Type_Product = 4;
	public static final int Type_Messenger = 5;
	public static final int Type_Store = 6;

	private String user_id;
	private String user_type;
	private String username;
	private String password;

	private static UserDataset instance;

	private UserDataset() {
		// Private constructor so nobody can create an instance of your class.
	}

	public static UserDataset getInstance() {
		if (instance == null) {
			instance = new UserDataset();
		}
		return instance;
	}

	public UserDataset(String user_id, String user_type, String username,
			String password) {
		super();
		this.user_id = user_id;
		this.user_type = user_type;
		this.username = username;
		this.password = password;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
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

	public static int getTypeSystem() {
		return Type_System;
	}

	public static int getTypeAdministrator() {
		return Type_Administrator;
	}

	public static int getTypeSale() {
		return Type_Sale;
	}

	public static int getTypeProduct() {
		return Type_Product;
	}

	public static int getTypeMessenger() {
		return Type_Messenger;
	}

	public static void setInstance(UserDataset instance) {
		UserDataset.instance = instance;
	}
	
	
	


}
