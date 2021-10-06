package com.example;


import java.util.List;
import java.util.Scanner;

public class Project0Driver {

	public static void main(String[] args) {
		
		DAO dao1=new DAO();
		
		@SuppressWarnings("resource")
		Scanner scan=new Scanner(System.in);
		
		
		while (true) {
			System.out.println("-------------------------------------------------------------------");
			System.out.println("Welcome to Woori Bank!");
			System.out.println("Enter \"login\", \"signup\" or \"exit\".");
			System.out.println("Please use only one word alpha-numeric answers durring this session.");
			System.out.println("-------------------------------------------------------------------");
			String s=scan.nextLine();
			if(s.equals("login")) {
			//---------login------------------------------------------
				System.out.println("enter username");
				String un=scan.nextLine();
				System.out.println("enter password");
				String pw=scan.nextLine();
				if(dao1.passwordcheck(un, pw)) { //do username and password match and exist on table
					
					if(dao1.usertype(un).equals("customer")) {
						customersession(un);
					}else if (dao1.usertype(un).equals("employee")) {
						employeesession(un);
					}else if (dao1.usertype(un).equals("admin")) {
						adminsession(un);
					}else {
						System.out.println("ERROR invalid usertype");
					}
					
				} else {
					System.out.println("invalid username or password");
				}
		
			}else if (s.equals("signup")){
			//-----------signup-----------------------------------------
				System.out.println("create username");
				String un=scan.next();
				System.out.println("create password");
				String pw=scan.next();
				System.out.println("enter firstname");
				String fn=scan.next();
				System.out.println("enter lastname");
				String ln=scan.next();
				if (dao1.userexists(un)) {
					System.out.println("Sorry, username already exists");
				}else {
				dao1.createuser(un, pw,"customer", fn, ln);
				customersession(un);
				}
			} else if (s.equals("exit")) {
				System.exit(0);
			}
				
		}
	}
	
	
	//CUSTOMER SESSION---------------------------------------------------------------------------------------------------------------------
	public static void customersession(String un) {
		@SuppressWarnings("resource")
		Scanner scan=new Scanner(System.in);
		DAO dao1=new DAO();
		System.out.println("-------------------------------------------------------------------");
		System.out.println("WELCOME "+ un);
		System.out.println("Enter one of the following options");
		System.out.println("create = create a new checkings account");
		System.out.println("withdraw = withdraw funds");
		System.out.println("deposit = deposit funds");
		System.out.println("transfer = transfer funds");
		System.out.println("joint = give joint ownership to another user");
		System.out.println("view = view bank acounts and balances");
		System.out.println("logoff = end this current session");
		System.out.println("-------------------------------------------------------------------");
		String s=scan.nextLine();
		
		switch (s) {
		case "create":
			System.out.println("Enter type of account, example: checking, saving, college");
			s=scan.nextLine();
			dao1.createbank(un, s);
			break;
		
		case "withdraw":
			System.out.println("Enter ID number of account you are withdrawing from");
			int id=scan.nextInt();
			System.out.println("How much would you like to withdraw?");
			double money=scan.nextDouble();
			if (dao1.isactive(id)) {
				if (dao1.ownershipcheck(un, id)) {
					if (dao1.canwithdraw(id, money)) {
						dao1.withdraw(id, money);
					} else {
						System.out.println("Error: insufficient funds or account doesnt exist");
					}
				} else {
					System.out.println("Error: You do not own this account");
				}
			}else {
				System.out.println("bank account not activated, talk to employeee");
			}		
			break;
		
		case "deposit":
			System.out.println("Enter ID number of account you are depositing to");
			id=scan.nextInt();
			System.out.println("How much would you like to deposit?");
			money=scan.nextDouble();
			if (dao1.isactive(id)) {
				if (dao1.ownershipcheck(un, id)&&money>=0) {
					dao1.deposit(id, money);
				} else {
						System.out.println("Error: You do not own this account");
				}
			}else {
				System.out.println("bank account not activated, talk to employeee");
			}
			break;
		
		case "transfer":
			System.out.println("Enter ID number of account you are transfering from (withdrawing from)");
			int idw=scan.nextInt();
			System.out.println("Enter ID number of account you are transfering to (depositing to)");
			int idd=scan.nextInt();
			System.out.println("How much would you like to transfer?");
			money=scan.nextDouble();
			if (dao1.isactive(idw)) {
				if (dao1.ownershipcheck(un, idw)&&money>=0) {
					if (dao1.canwithdraw(idw, money)) {
						dao1.withdraw(idw, money);
						dao1.deposit(idd, money);
					} else {
						System.out.println("Error: insufficient funds or account doesnt exist");
					}
				} else {
					System.out.println("Error: You do not own this account");
				}
			}else {
				System.out.println("bank account not activated, talk to employeee");
			}
			break;
		
		case "joint":
			System.out.println("Enter the username that you want to grant joint ownership to");
			s=scan.nextLine();
			System.out.println("Enter the account number");
			id=scan.nextInt();
			if (dao1.userexists(s)&&!(un.equals(s))) {
				if (dao1.ownershipcheck(un, id)) {
					dao1.joint(id, s);
				}else {
					System.out.println("Error: you dont own this account");
				}
				
			} else {
				System.out.println("Error, user doesnt exist or you entered in your own username");
			}
			break;
		
		case "view":
			System.out.println("You own are owner or joint owner of the following accounts:");
			List<bank> banklist = dao1.getAllbank();
			for (bank banks : banklist) {
				if(banks.getOwner2()!=null){
					if (banks.getOwner2().equals(un)) {
						System.out.println(banks);
					}
				}
				if (banks.getOwner1().equals(un)) {
					System.out.println(banks);
				}
				
			}
			break;
		
		case "logoff":
			System.out.println("LOGGING OFF, GOODBYE");
			return;
		
		default:
			System.out.println("invalid input, try again");
			break;
		}
		
		customersession(un); //will allow more than one time, only log off exits session
		
	}
	
	
	//EMPLOYEE SESSION---------------------------------------------------------------------------------------------------------------------
	public static void employeesession(String un) {
		@SuppressWarnings("resource")
		Scanner scan=new Scanner(System.in);
		DAO dao1=new DAO();
		System.out.println("-------------------------------------------------------------------");
		System.out.println("WELCOME employee "+ un);
		System.out.println("Enter one of the following options");
		System.out.println("activate = activate accounts");
		System.out.println("users = view user accounts");
		System.out.println("bank = view bank accounts");
		System.out.println("logoff = end session");
		System.out.println("-------------------------------------------------------------------");
		String s=scan.nextLine();
		switch (s) {
		case "activate":
			System.out.println("Which acount do you want to activate?");
			int n=scan.nextInt();
			if(dao1.bankexits(n)) {
				dao1.approve(n);
			} else {
				System.out.println("no bank account exists with that number");
			}
			break;
			
		case "users":
			System.out.println("All user accounts:");
			List<user> userlist = dao1.getAlluser();
			for (user users : userlist) {
				if (users.getType().equals("customer")) {
					System.out.println(users);
				}	
			}
			break;
			
		case "bank":
			System.out.println("All bank accounts:");
			List<bank> banklist = dao1.getAllbank();
			for (bank banks : banklist) {
					System.out.println(banks);
			}
			break;
			
		case "logoff":
			System.out.println("LOGGING OFF, GOODBYE");
			return;
		
		default:
			System.out.println("invalid input, try again");
			break;
		}
		
		employeesession(un);
		
	}
	
	
	//ADMIN SESSION---------------------------------------------------------------------------------------------------------------------
	public static void adminsession(String un) {
		@SuppressWarnings("resource")
		Scanner scan=new Scanner(System.in);
		DAO dao1=new DAO();
		System.out.println("-------------------------------------------------------------------");
		System.out.println("WELCOME admin "+ un);
		System.out.println("Enter one of the following options");
		System.out.println("activate = activate accounts");
		System.out.println("users = view user accounts");
		System.out.println("bank = view bank accounts");
		System.out.println("withdraw = withdraw funds");
		System.out.println("deposit = deposit funds");
		System.out.println("transfer = tranfer funds from one account to another");
		System.out.println("cancelbank = delete bank account");
		System.out.println("canceluser = delete user account");
		System.out.println("createemployee = create an employee account");
		System.out.println("createadmin = create an admin account");
		System.out.println("logoff = end session");
		System.out.println("-------------------------------------------------------------------");
		String s=scan.nextLine();
		
		switch (s) {
		case "activate":
			System.out.println("Which acount do you want to activate?");
			int n=scan.nextInt();
			if(dao1.bankexits(n)) {
				dao1.approve(n);
			} else {
				System.out.println("no bank account exists with that number");
			}
			break;
			
		case "users":
			System.out.println("All user accounts:");
			List<user> userlist = dao1.getAlluser();
			for (user users : userlist) {
					System.out.println(users);
			}
			break;
			
		case "bank":
			System.out.println("All bank accounts:");
			List<bank> banklist = dao1.getAllbank();
			for (bank banks : banklist) {
					System.out.println(banks);
			}
			break;
			
		case "withdraw":
			System.out.println("Enter ID number of account you are withdrawing from");
			int id=scan.nextInt();
			System.out.println("How much would you like to withdraw?");
			double money=scan.nextDouble();
				if (dao1.canwithdraw(id, money)&&money>=0) {
					dao1.withdraw(id, money);
				} else {
					System.out.println("Error: insufficient funds or account doesnt exist");
				}
			break;
		
		case "deposit":
			System.out.println("Enter ID number of account you are depositing to");
			id=scan.nextInt();
			System.out.println("How much would you like to deposit?");
			money=scan.nextDouble();
			if (dao1.bankexits(id)&&money>=0) {
				dao1.deposit(id, money);
			} else {
				System.out.println("bank account doesnt exist");
			}
			break;
		
		case "transfer":
			System.out.println("Enter ID number of account you are transfering from (withdrawing from)");
			int idw=scan.nextInt();
			System.out.println("Enter ID number of account you are transfering to (depositing to)");
			int idd=scan.nextInt();
			System.out.println("How much would you like to transfer?");
			money=scan.nextDouble();
				if (dao1.canwithdraw(idw, money)&&money>=0) {
					dao1.withdraw(idw, money);
					dao1.deposit(idd, money);
				} else {
					System.out.println("Error: insufficient funds or account doesnt exist");
				}
			break;
			
		case "createemployee":
			System.out.println("create username");
			String una=scan.next();
			System.out.println("create password");
			String pw=scan.next();
			System.out.println("enter firstname");
			String fn=scan.next();
			System.out.println("enter lastname");
			String ln=scan.next();
			if (dao1.userexists(una)) {
				System.out.println("Sorry, username already exists");
			}else {
				dao1.createuser(una, pw,"employee", fn, ln);
			}
			break;
			
		case "createadmin":
			System.out.println("create username");
			una=scan.next();
			System.out.println("create password");
			pw=scan.next();
			System.out.println("enter firstname");
			fn=scan.next();
			System.out.println("enter lastname");
			ln=scan.next();
			if (dao1.userexists(una)) {
				System.out.println("Sorry, username already exists");
			}else {
				dao1.createuser(una, pw,"admin", fn, ln);
			}
			break;	
			
		case "cancelbank":
			System.out.println("Enter ID number of account wish to delete");
			id=scan.nextInt();
			if (dao1.bankexits(id)) {
				dao1.cancelbank(id);
			} else {
				System.out.println("account doesnt exist");
			}
			break;
			
		case "canceluser":
			System.out.println("Enter username of account wish to delete");
			s=scan.nextLine();
			if (dao1.userexists(s)) {
				dao1.canceluser(s);
			} else {
				System.out.println("account doesnt exist");
			}
			break;
			
		case "logoff":
			System.out.println("LOGGING OFF, GOODBYE");
			return;
		
		default:
			System.out.println("invalid input, try again");
			break;
		}
		
		adminsession(un);
	}
	
	
}

//TESTED CODE
//System.out.println(p1dao.bankexits(5));
//System.out.println(p1dao.bankexits(10));
//System.out.println(p1dao.userexists("monkey"));
//System.out.println(p1dao.userexists("blueyak100"));
//System.out.println(p1dao.canwithdraw(3, 100));
//System.out.println(p1dao.canwithdraw(3, 100000000));
//System.out.println(p1dao.passwordcheck("a", "a"));
//System.out.println(p1dao.passwordcheck("blueyak8", "password"));

//p1dao.createuser("monkey", "password", "don", "jenkins");
//p1dao.createbank("monkey", "checking");
//p1dao.withdraw(3, 1.00);
//p1dao.deposit(3, 10000.00);
//System.out.println(p1dao.getAllbank());
//System.out.println(p1dao.getAlluser());