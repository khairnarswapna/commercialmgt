package com.bridgelabz.controller;

import java.io.File;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.bridgelabz.model.Company;
import com.bridgelabz.model.CompanyModel;
import com.bridgelabz.model.Customer;
import com.bridgelabz.model.CustomerModel;
import com.bridgelabz.model.TransactionModel;
import com.bridgelabz.model.Transactions;
import com.bridgelabz.util.JsonUtil;
import com.bridgelabz.util.OOPsUtility;

public class AccountStock {

	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		boolean isExit = false;
		int choice;
		int indexOfCustomer = 0;
		int index = 0;
		

		int customerId, companySymbol; 
		int numOfCompanyShareToBuy;// taking from  share from user
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("HHmmss");

		CompanyModel compModel = new CompanyModel();
		CustomerModel custModel = new CustomerModel();
		TransactionModel transModel = new TransactionModel();

		// paths of files
		String pathOfCompanyShares = "/home/admin-1/eclipse-workspace/commercial/src/com/bridgelabz/json/company_shares.json";
		String pathOfCustomerInfo = "/home/admin-1/eclipse-workspace/commercial/src/com/bridgelabz/json/customer_info.json";
		//String pathOfCompanyShares2 = "/home/admin-1/eclipse-workspace/commercial/src/com/bridgelabz/json/company_shares2.json";
		String pathOfCompanyShares2="/home/admin-1/eclipse-workspace/commercial/src/com/bridgelabz/json/company_shares3.json";
		String pathOfCustomerInfo2 = "/home/admin-1/eclipse-workspace/commercial/src/com/bridgelabz/json/customer_info2.json";
		String pathOfTransaction = "/home/admin-1/eclipse-workspace/commercial/src/com/bridgelabz/json/transactions.json";
		
		// code for fetching json data and put it into models

		// code for fetching the company 
		compModel = (CompanyModel) JsonUtil.readMapper(pathOfCompanyShares, compModel);

		// code for fetching the customer info
		
		custModel = (CustomerModel) JsonUtil.readMapper(pathOfCustomerInfo, custModel);

		File file = new File(pathOfTransaction);
		if (file.length() != 0) {
			transModel = (TransactionModel) JsonUtil.readMapper(pathOfTransaction, transModel);
		}

		List<Customer> custList = new ArrayList<>();
		List<Company> compList = new ArrayList<>();
		List<Transactions> transList = new ArrayList<>();

		custList.addAll(custModel.getCustomerinfo());
		compList.addAll(compModel.getCompanyshares());

		if (!transModel.getTransactions().isEmpty()) 
		{
			transList.addAll(transModel.getTransactions());
		}

		 
		System.out.println("Please enter customer id");
		customerId = OOPsUtility.integerScanner();
		boolean isCustomerFound = false;
		for (int i = 0; i < custList.size(); i++) 
		{
			if (customerId == custList.get(i).getCustomer_id()) 
			{
				isCustomerFound = true;
				indexOfCustomer = i;
				break;
			}
		}
		if (isCustomerFound) 
		{
			while (!isExit) 
			{
				 
				System.out.println("1.buy shares\n" + "2. sell shares\n"+ "3. print report\n" + "4. exit");
				System.out.println("Enter your choice:");
				choice = OOPsUtility.integerScanner();
				switch (choice) 
				{
				case 1:	// buy();
					
					System.out.println("Please enter company symbol: ");
					companySymbol = OOPsUtility.integerScanner();

				//	System.out.println("Validating...");
					boolean  isfound= false;
					for (int i = 0; i < compList.size(); i++) 
					{
						if (companySymbol == compList.get(i).getCompany_symbol()) 
						{
							isfound= true;
							index = i; //getting the value index
							
							break;
						}
					}
					System.out.println("The company you selected is: " + compList.get(index).getCompany_name());
					System.out.println("Company shares: " + compList.get(index).getCompany_shares());
					System.out.println("Company share price: " + compList.get(index).getCompany_share_price());
					
					int customerBalance = custList.get(indexOfCustomer).getCustomer_balance();
					System.out.println("You have Balance: " + customerBalance);
					
					if (isfound) 
					{
						System.out.println();
						
						
						System.out.println("Please enter how much shares to buy: ");
						numOfCompanyShareToBuy = OOPsUtility.integerScanner();

						// checking whether amount to buy whether user has that much amount or not
						if (numOfCompanyShareToBuy < compList.get(index).getCompany_shares()) 
						{
							// checking user has that much amount or not
							if (customerBalance > (numOfCompanyShareToBuy * compList.get(index).getCompany_share_price())) 
							{
								
								//deducting customer balance
								int newCustomerBalance = customerBalance - numOfCompanyShareToBuy * compList.get(index).getCompany_share_price();
                                custList.get(indexOfCustomer).setCustomer_balance(newCustomerBalance);
								
                                // adding customer shares
								custList.get(indexOfCustomer).setCustomer_shares(custList.get(indexOfCustomer).getCustomer_shares() + numOfCompanyShareToBuy);
								// deducting the company share
								compList.get(index).setCompany_shares(compList.get(index).getCompany_shares() - numOfCompanyShareToBuy);

								// calculating new total value of company
								compList.get(index).setCompany_total_value(compList.get(index).getCompany_share_price()
												* compList.get(index).getCompany_shares());

								// code reflecting transaction
								Transactions trans = new Transactions();
						        LocalDateTime now = LocalDateTime.now();

								 
								String transId = dateTimeFormatter2.format(now)
										+ custList.get(indexOfCustomer).getCustomer_id()
										+ compList.get(index).getCompany_symbol();
								
								
								///
								trans.setTransaction_id(transId);
								trans.setBuyer(custList.get(indexOfCustomer).getCustomer_name());
								trans.setSeller(compList.get(index).getCompany_name());
								trans.setTransaction_amount(numOfCompanyShareToBuy * compList.get(index).getCompany_share_price());
								//trans.setDatetime(dateTimeFormatter.format(now));	
								transList.add(trans);

			                      //sav					
								int saveOrNot;
								System.out.println("Press\t" + "1. for save \n2. for not save");
								saveOrNot = OOPsUtility.integerScanner();
								if (saveOrNot == 1) 
								{
									JsonUtil.writeMapper(pathOfCompanyShares2, compModel.getCompanyshares());
									JsonUtil.writeMapper(pathOfCustomerInfo2, custModel.getCustomerinfo());
									transModel.setTransactions(transList);
									transModel.setTransaction("Transactions");
									JsonUtil.writeMapper(pathOfTransaction, transModel);
									System.out.println("Transaction has saved..!!");
								}else if (saveOrNot == 2) 
								{
									System.out.println("Transaction not saved");
								} else
									System.out.println("Transaction get void");

							} else
								System.out.println("You dont have enough balance");
						} else {
							System.out.println("Company don't have that much shares");
						}

					} else {
						System.out.println("Company not identified please try again...");
					}
					 
					break;
					
					
					

				case 2:
					// sell
			 

					System.out.println("Enter number of share you want to sell");
					int share = OOPsUtility.integerScanner();

					System.out.println("Please enter company symbol to who you want to sell: ");
					companySymbol = OOPsUtility.integerScanner();
 
					//company found based of companySymbol
					boolean isFound = false;
					for (int i = 0; i < compList.size(); i++) 
					{
						if (companySymbol == compList.get(i).getCompany_symbol()) 
						{
							isFound = true;
							index = i;
							break;
						}
					}

					// if company is valid
					if (isFound) 
					{
						 //printing the validate company info
						System.out.println("The company you selected is: " + compList.get(index).getCompany_name());
						System.out.println("Company shares: " + compList.get(index).getCompany_shares());
						System.out.println("Company share price: " + compList.get(index).getCompany_share_price());
						
						int amountToGet = share * compList.get(index).getCompany_share_price();
						System.out.println("Amount you will get: " + amountToGet);
						// company share increase
						if (share <= custList.get(indexOfCustomer).getCustomer_shares()) 
						{
							compList.get(index).setCompany_shares(compList.get(index).getCompany_shares() + share);
							// company value increase
							compList.get(index).setCompany_total_value(compList.get(index).getCompany_share_price()* compList.get(index).getCompany_shares());

							// customer share decrease
							custList.get(indexOfCustomer).setCustomer_shares(custList.get(indexOfCustomer).getCustomer_shares() + share);

							// customer balance increase

							custList.get(indexOfCustomer).setCustomer_balance(
									custList.get(indexOfCustomer).getCustomer_balance() + amountToGet);

							// code reflecting transaction
							Transactions trans = new Transactions();
							LocalDateTime now = LocalDateTime.now();

						   //System.out.println(dateTimeFormatter2.format(now));  
							
							String transId = dateTimeFormatter2.format(now)
									+ custList.get(indexOfCustomer).getCustomer_id()
									+ compList.get(index).getCompany_symbol();
							
							trans.setTransaction_id(transId);//set transaction id
							
							trans.setBuyer(compList.get(index).getCompany_name());
							trans.setSeller(custList.get(indexOfCustomer).getCustomer_name());
							trans.setTransaction_amount(share * compList.get(index).getCompany_share_price());
							trans.setDatetime(dateTimeFormatter.format(now));

							transList.add(trans);

							int saveOrNot;
							System.out.println("Press\t" + "1. for save \n2. for not save");
							saveOrNot = OOPsUtility.integerScanner();
							if (saveOrNot == 1) 
							{
								JsonUtil.writeMapper(pathOfCompanyShares2,compModel.getCompanyshares());
								JsonUtil.writeMapper(pathOfCustomerInfo2, custModel.getCustomerinfo());
								
								transModel.setTransactions(transList);
								transModel.setTransaction("Transactions");
								
								JsonUtil.writeMapper(pathOfTransaction, transModel);
								System.out.println("Transaction has saved..!!!");
							} else if (saveOrNot == 2) {
								System.out.println("Transaction not saved");
							} else
								System.out.println("Transaction get void");

						} else {
							System.out.println("You dont have that much shares to sell");
						}

					} else
						System.out.println("Company not found please try again");

				 
					break;
//			case 3:
//				// save
//				 
//				break;
				case 3:
					// print report
					System.out.println("----------------------------------");
				 
					boolean hasValue = false;
					for (int i = 0; i < transList.size(); i++) 
					{
						if (Character.getNumericValue(transList.get(i).getTransaction_id().charAt(6)) == customerId) {
							hasValue = true;
							break;
						}
					}
					if (hasValue) 
					{
						for (int i = 0; i < transList.size(); i++) 
						{
							if (i == 0) {
								System.out.print("Transaction_ID\t");
								System.out.print("Buyer\t\t");
								System.out.print("Seller\t\t\t");
								System.out.print("Trans_Amt\t");
								System.out.println("DateTime\t");
							}
							if (Character.getNumericValue(transList.get(i).getTransaction_id().charAt(6)) == customerId) {
								System.out.print(transList.get(i).getTransaction_id() + "\t");
								System.out.print(transList.get(i).getBuyer() + "\t\t");
								System.out.print(transList.get(i).getSeller() + "\t\t");
								System.out.print(transList.get(i).getTransaction_amount() + "\t");
								System.out.println(transList.get(i).getDatetime() + "\t");

							}

						}
					} else
						System.out.println("You dont have any transactions!!!");

					System.out.println();
				 
					break;
				case 4:
					isExit = true;
					System.out.println("Thank you for using service");
					break;
				default:
					System.out.println("Please select valid option");
				}// end of switch loop

			} // end of while loop

		} else {
			System.out.println("Invalid customer id");
		}
	}
}
