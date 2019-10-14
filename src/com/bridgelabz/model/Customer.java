package com.bridgelabz.model;

public class Customer {

	private int customer_id;
	private String customer_name;
	private int customer_shares;
	private int customer_balance;
	

	public int getCustomer_id() 
	{
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getCustomer_balance() {
		return customer_balance;
	}

	public void setCustomer_balance(int customer_balance) {
		this.customer_balance = customer_balance;
	}

	 

	 

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public int getCustomer_shares() {
		return customer_shares;
	}

	public void setCustomer_shares(int customer_shares) {
		this.customer_shares = customer_shares;
	}

}
