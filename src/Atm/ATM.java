package Atm;

import java.util.Scanner;


public class ATM {

	public static void main(String[] args) {
		//init Scanner
		Scanner sc = new Scanner(System.in);
		
		//init Bank
		Bank theBank = new Bank("Bank of Vasconi");
		
		//add a user, which also creates a savings account
		User aUser = theBank.addUser("John", "Doe", "1234");
		
		//add a checking account for our user
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		
		User curUser;
		while(true) {
			
			//Stay in the login prompt until succesful login
			curUser = ATM.mainMenuPrompt(theBank, sc);
			//Stay in main menu until user quits
			ATM.printUserMenu(curUser, sc);
			
		}
		
	}
	
	/**
	 * Print the ATM's login menu
	 * @param theBank  the bank object whose accounts to use
	 * @param sc    the Scanner object to use for user input
	 * @return		the authenticated User object
	 */
	
	
		public static User mainMenuPrompt(Bank theBank, Scanner sc) {
			
			//inits
			String userID;
			String pin;
			User authUser;
			
			//Prompt the user for the user ID/pin combo until the correct one is successful
			do {
				
				System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
				System.out.print("Enter user ID:");
				userID = sc.nextLine();
				System.out.print("Enter pin: ");
				pin = sc.nextLine();
				
				//Try to get the user object corresponding to the ID and pin combo
				authUser = theBank.userLogin(userID, pin);
				if (authUser == null) {
					System.out.println("Incorrect user ID/pin combo. " + "Please try again.");
				}
				
			}while(authUser == null); //Continue looping until successful login
			
			return authUser;
		}
		
		public static void printUserMenu(User theUser, Scanner sc) {
			
			//print a summary of the users accounts
			theUser.printAccountSummary();
			
			//init
			int choice;
			
			//user menu
			do {
				System.out.printf("Welcome %s, what may we do for you?\n", theUser.getFirstName());
				System.out.println(" 1) Show account transaction History");
				System.out.println(" 2) Withdrawal");
				System.out.println(" 3) Deposit");
				System.out.println(" 4) Transfer funds");
				System.out.println(" 5) Quit");
				System.out.println();
				System.out.print("Enter your choice: ");
				choice = sc.nextInt();
				
				if(choice < 1 || choice > 5) {
					System.out.println("Invalid choice. Please choose 1-5");	
				}				
			}while(choice < 1 || choice > 5);
			
			//process the choice
			switch (choice) {
			
			case 1:
				ATM.showTransHistory(theUser, sc);
				break;
			case 2:
				ATM.withdrawalFunds(theUser, sc);
				break;
			case 3:
				ATM.depositFunds(theUser, sc);
				break;
			case 4:
				ATM.transferFunds(theUser, sc);
				break;
			case 5:
				//gooble up rest of previous input
				sc.nextLine();
				break;
			}
			//Redisplay this menu unless user wants to quit
			if (choice != 5) {
				ATM.printUserMenu(theUser, sc);
			}
			
		}
	
		/**
		 * Show the transaction history for an account
		 * @param theUser  the logged in user object
		 * @param sc  the scanner object used for user input
		 */
		public static void showTransHistory(User theUser, Scanner sc) {

		
			
			int theAcct;
			//Get account whose trans history to look at
			do {
				System.out.printf("Enter the number(1-%d) of the account\n" + "Whose transactions you want to see: ", theUser.numAccounts() );
			theAcct = sc.nextInt()-1;
			if(theAcct < 0 || theAcct >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again. ");
			}
			}while (theAcct < 0 || theAcct >= theUser.numAccounts());
			//Print the trans history
			theUser.printAcctTransHistory(theAcct);
		}
		
		/**
		 * Process transfering funds from one account to another
		 * @param theUser  the logged in User object
		 * @param sc  the scanner object used for the user input
		 */
		public static void transferFunds(User theUser, Scanner sc) {
			
			//inits
			int fromAcct;
			int toAcct;
			double amount;
			double acctBal;
			
			//get the account to transfer from
			do {
				System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from: ", theUser.numAccounts());
				fromAcct = sc.nextInt()-1;
				if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
					System.out.println("Invalid account. Please try again. ");
				}
			}while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
			acctBal = theUser.getAcctBalance(fromAcct);
		
			//get the account to transfer to
			do {
				System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from: " , theUser.numAccounts());
				toAcct = sc.nextInt()-1;
				if(toAcct < 0 || toAcct >= theUser.numAccounts()) {
					System.out.println("Invalid account. Please try again. ");
				}
			}while (toAcct < 0 || toAcct >= theUser.numAccounts());
			
			//get the amount to transfer
			do {
				System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
				amount = sc.nextDouble();
				if (amount < 0) {
					System.out.println("Amount must be greater then zero.");
				}else if (amount > acctBal) {
					System.out.printf("Amount must not be greater than \n" + "balance of $%.02f.\n", acctBal);
				}
			}while (amount < 0 ||amount > acctBal);
		
			
			//finally, do the transfer
			theUser.addAcctTransaction(fromAcct, -1*amount, String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
			theUser.addAcctTransaction(toAcct, amount, String.format("Transfer to account %s", theUser.getAcctUUID(fromAcct)));
		
}	
		/**
		 * Process a fund withdrawal from an account
		 * @param theUser  the logged in User object
		 * @param sc   the Scanner object user for the user input
		 */
		public static void withdrawalFunds(User theUser, Scanner sc) {
			//inits
			int fromAcct;
			double amount;
			double acctBal;
			String memo;
			//get the account to withdraw from
			do {
				System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from: " , theUser.numAccounts());
				fromAcct = sc.nextInt()-1;
				if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
					System.out.println("Invalid account. Please try again. ");
				}
			}while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
			acctBal = theUser.getAcctBalance(fromAcct);
			
			//Get the amount to transfer
			do {
				System.out.printf("Enter the amount to Withdraw (Max $%.02f): $", acctBal);
				amount = sc.nextDouble();
				if (amount < 0 ) {
					System.out.println("Amount must be greater than zero");
				}else if (amount > acctBal) {
					System.out.printf("Amount must not be greater than\n" + "balance of $%.02f. \n", acctBal);
					
				}
			}while (amount < 0 || amount > acctBal);
			
			//gooble up rest of previous input
			sc.nextLine();
			
			//get a memo
			System.out.print("Enter a memo:");
			memo = sc.nextLine();
			
			//do the withdrawal
			theUser.addAcctTransaction(fromAcct, -1*amount, memo);
		}
		
		
		/**
		 * Process a fund deposit to an account
		 * @param theUser  the logged in User object
		 * @param sc   the Scanner object used for user input
		 */
		
		public static void depositFunds(User theUser, Scanner sc) {
		
			int toAcct;
			double amount;
			double acctBal;
			String memo;
			//get the account to transfer from
			do {
				System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit in: ", theUser.numAccounts());
				toAcct = sc.nextInt()-1;
				if(toAcct < 0 || toAcct >= theUser.numAccounts()) {
					System.out.println("Invalid account. Please try again. ");
				}
			}while (toAcct < 0 || toAcct >= theUser.numAccounts());
			acctBal = theUser.getAcctBalance(toAcct);
			
	
			//Get the amount to transfer
			do {
				System.out.printf("Enter the amount to transfer (Max $%.02f): $", acctBal);
				amount = sc.nextDouble();
				if (amount < 0 ) {
					System.out.println("Amount must be greater than zero");
				}
			}while (amount < 0);
			
			//gooble up rest of previous input
			sc.nextLine();
			
			//get a memo
			System.out.print("Enter a memo:");
			memo = sc.nextLine();
			
			//do the withdrawal
			theUser.addAcctTransaction(toAcct, +1*amount, memo);
		} 
			
}

