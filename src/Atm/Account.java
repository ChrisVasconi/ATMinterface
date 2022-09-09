package Atm;

import java.util.ArrayList;

public class Account {

	//Name of account
	private String name;
	
	//Current Balance of the account
	private Double balance;
	
	//Account ID number
	private String uuid;
	
	//User object that owns this Account
	private User holder;
	
	//List of transactions for this account
	private ArrayList<Transaction> transactions;
	
	/**
	 * Create a New Account
	 * @param name
	 * @param holder
	 * @param theBank
	 */
	
	public Account(String name, User holder, Bank theBank) {
		
		
		//Set the account name and holder
		this.name = name;
		this.holder = holder;
		
		//Get the new account UUID
		this.uuid = theBank.getNewUserUUID();
				
		//init (intialize) transactions
		this.transactions = new ArrayList<Transaction>();
		
	}
	/**
	 * Get the account ID
	 * @return  the uuid
	 */
	
	public String getUUID() {
		return this.uuid;
	}
	
	
	/**
	 * Get summary line for account
	 * @return  the string summary
	 */
	
	public String getSummaryLine() {
		
		//get the accounts balance
		double balance = this.getBalance();
		
		//format the summary line, depending on whetehr the balance is negative
		if (balance >= 0) {
			return String.format("%s : $%.02f : %s",  this.uuid, balance, this.name);
		}else {
			return String.format("%s : $(%.02f) : %s",  this.uuid, balance, this.name);
		}
	}
	
	public double getBalance() {
		double balance = 0;
		for (Transaction t : this.transactions ) {
			balance += t.getAmount();
		}
		return balance;
	}
	
	/**
	 * Print the transaction history of the account
	 */
	public void printTransHistory() {
		System.out.printf("\nTransaction History for account %s\n", this.uuid);
		for(int t = this.transactions.size()-1; t >= 0; t--) {
			System.out.println(this.transactions.get(t).getSummaryLine());
		}
		System.out.println();
	}
	
	
	/**
	 * Add a new transaction in this account
	 * @param amount  the amount transacted
	 * @param memo  the transaction memo
	 */
	public void addTransaction(double amount, String memo) {
		
		//create new transaction object and add it to our list
		Transaction newTrans = new Transaction(amount, memo, this);
		this.transactions.add(newTrans);
	}
}
