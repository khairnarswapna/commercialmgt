/**
 * purspose:object customerModel
 */
package com.bridgelabz.model;

import java.util.List;

public class CustomerModel {
	private List<Customer> Customerinfo;

	public List<Customer> getCustomerinfo() 
	{
		return Customerinfo;
	}

	public void setCustomerinfo(List<Customer> customerinfo) {
		Customerinfo = customerinfo;
	}
}
