package Atm;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	//First name of user
	private String firstName;
	
	//Last name of user
	private String lastName;
	
	//ID number of user
	private String uuid;
	
	//The MD5 hash of the users pin number
	private byte pinHash[];
	
	//List of accounts for said user
	private ArrayList<Account> accounts;

	/**
	 * Create a new User
	 * @param firstName
	 * @param lastName
	 * @param pin
	 * @param theBank
	 */
	
	
	public User(String firstName, String lastName, String pin, Bank theBank) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		
		//store the pins MD5 hash, rather then the original value, for security purposes
		
	try {
		MessageDigest md = MessageDigest.getInstance("MD5");
		this.pinHash = md.digest(pin.getBytes());
	} catch (NoSuchAlgorithmException e) {
		System.err.println("error, caught NoSuchAlgoExceptions");
		e.printStackTrace();
		System.exit(1);
	}
		
	//get a new, unique universal ID for the user
	this.uuid = theBank.getNewUserUUID();
	
	//create empty list of ACCOUNTS
	this.accounts = new ArrayList<Account>();
	
	//Print log Message
	System.out.printf("New user %s, %s with ID %s created. \n", lastName, firstName, this.uuid);
	
	
	
	}
	
	/**
	 * Add an account for the user
	 * @param anAcct  the account to add
	 */
	
	
	public void addAccount(Account anAcct) {
		this.accounts.add(anAcct);
	}
	
	/**
	 * Return the Users UUID
	 * @return    the uuid
	 */

	public String getUUID() {
		return this.uuid;
	}

	
	/**
	 * Check whether a given pin matches the true User pin
	 * @param aPin   the pin to check
	 * @return   whether the pin is valid or not
	 */
	
	public boolean validatePin(String aPin) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(aPin.getBytes()),
					this.pinHash);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgoExceptions");
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}
	
	/**
	 * Return the users first name
	 * @return   the first name
 	 */
	
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * Print summaries for the accounts of this user.
	 */
	public void printAccountSummary() {
		
		System.out.printf("\n\n%s's accounts summary\n", this.firstName);
		for (int a = 0; a < this.accounts.size(); a++) {
			System.out.printf("  %d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
		}
		System.out.println();
	}
	

	/**
	 * Get the number of accounts from the user
	 * @return  the number of accounts
	 */
	public int numAccounts() {
		return this.accounts.size();
		
	}
	/**
	 * Prints trans history for a particular account
	 * @param acctIdx  the index of the account to use
	 */
	public void printAcctTransHistory(int acctIdx) {
		this.accounts.get(acctIdx).printTransHistory();
	}
	
	/**
	 * Get the balance of a particular account
	 * @param acctIdx  the index of the account to use
	 * @return  the balance of the account
	 */
	public double getAcctBalance(int acctIdx) {
		return this.accounts.get(acctIdx).getBalance();
	}
	
	/**
	 * Get the UUID of a particular account
	 * @param acctIdx  the index of the account to use
	 * @return  the UUID of the account
	 */
	public String getAcctUUID(int acctIdx) {
		return this.accounts.get(acctIdx).getUUID();
	}
	
	public void addAcctTransaction(int acctIdx, double amount, String memo) {
		this.accounts.get(acctIdx).addTransaction(amount, memo);
	}
}	