package com.example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DAO {
	
	public final Logger log= Logger.getLogger(DAO.class);
	
	private static final String URL = "jdbc:postgresql://joeydb.cpthsxeojuz1.us-east-2.rds.amazonaws.com:5432/project0db";
	private static final String username =  "project0user";
	private static final String password = "p4ssw0rd";
	
	public boolean isactive(int aid) { //return user type, ie customer, employee or admin
		List<bank> allbank=getAllbank();
		for (bank banks : allbank) {
			if (banks.getId()==aid) {
				return banks.isActive();
			}
		}
		return false;
	}
	
	public String usertype(String uname) { //return user type, ie customer, employee or admin
		List<user> alluser=getAlluser();
		String answer="";
		for (user users : alluser) {
			if (users.getUsername().equals(uname)) {
				return users.getType();
			}
		}
		return answer;
	}
	
	public boolean ownershipcheck(String uname, int aid) { //checks if user owns bank account
		List<bank> allbank=getAllbank();
		boolean answer=false;
		for (bank banks : allbank) {
			if (banks.getId()==aid) {
				if (banks.getOwner1().equals(uname)||banks.getOwner2().equals(uname)) {
					answer=true;
				}
			}
		}
		return answer;
	}
	
	
	public boolean passwordcheck(String uname, String passw) { //checks if username and password match
		List<user> alluser=getAlluser();
		boolean answer=false;
		for (user users : alluser) {
			if (users.getUsername().equals(uname)) {
				if (users.getPassword().equals(passw)) {
					answer=true;
				}
			}
		}
		return answer;
	}
	
	public boolean userexists(String uname) { //checks if user exists on table
		List<user> alluser=getAlluser();
		boolean answer=false;
		for (user users : alluser) {
			
			if (users.getUsername().equals(uname)) {
				answer=true;
			}
		}
		return answer;
	}
	
	public boolean bankexits(int idnum) { //checks if bank exists on table
		List<bank> allbank=getAllbank();
		boolean answer=false;
		for (bank banks : allbank) {
			
			if (banks.getId()==idnum) {
				answer=true;
			}
		}
		return answer;
	}
	
	public boolean canwithdraw(int idnum,double money ) { //withdraw action will go through if money < funds
		List<bank> allbank=getAllbank();
		for (bank bank : allbank) {
			if (bank.getId()==idnum) {
				if (bank.getFunds()>=money) {
					return true;
				}else {
					return false;
				}
			}
		}
		return false; 
	}
	

	public void withdraw(int idnum,double money ) { //will withdraw money
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){

			String sql= "update bankaccount set funds=funds-? where id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDouble(1, money);
			ps.setInt(2, idnum);
			ps.execute();
			log.info("successfully withdrew $"+money+ " from id=" +idnum);
			//System.out.println("successfully withdrew $"+money+ " from id=" +idnum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deposit(int idnum,double money ) { //will deposit money
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
		
			String sql= "update bankaccount set funds=funds+? where id=?";		
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDouble(1, money);
			ps.setInt(2, idnum);
			ps.execute();
			log.info("successfully deposited $"+money+" to id= " +idnum);
			//System.out.println("successfully deposited $"+money+" to id= " +idnum);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void joint(int idnum,String own2 ) { //will add a 2nd owner to account
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
		
			String sql= "update bankaccount set owner2=? where id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, own2);
			ps.setInt(2, idnum);
			ps.execute();
			log.info("successfully added "+own2+" to bank id= " +idnum);
			//System.out.println("successfully added "+own2+" to bank id= " +idnum);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	//update bankaccount set active=true where id=1;
	public void approve(int idnum) { //will deposit money
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
			String sql="update bankaccount set active=true where id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, idnum);
			ps.execute();
			log.info("successfully activated ID:"+idnum);
			//System.out.println("successfully activated ID:"+idnum);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void cancelbank(int idnum) { //will delete bank account
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
			String sql="delete from bankaccount where id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, idnum);
			ps.execute();
			log.info("successfully canceled ID:"+idnum);
			//System.out.println("successfully canceled ID:"+idnum);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void canceluser(String uname) { //will delete bank account
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
			String sql="delete from useraccount where username=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, uname);
			ps.execute();
			log.info("successfully canceled username:"+uname);
			//System.out.println("successfully canceled username:"+uname);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//delete from useraccount where username='bob';
	
	//THIS IS THE EXAMPLE FOR CALLABLE STATEMENTS, user for createuser and createbank
	public void createbank(String aname,String atype ) { //creates a new bank account
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
			String sql = "{? = call createbank(?,?)}";
			
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR); 
			cs.setString(2, aname);
			cs.setString(3, atype);
			cs.execute();
			System.out.println(cs.getString(1));
			log.info("successfully created bank for" +aname);
			//System.out.println("successfully created bank for" +aname);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void createuser(String aname,String apass,String atype ,String fname, String lname ) { //creates a new user account
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
			
			String sql = "{? = call createuser(?,?,?,?,?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR); 
			cs.setString(2, aname);
			cs.setString(3, apass);
			cs.setString(4, atype);
			cs.setString(5, fname);
			cs.setString(6, lname);
			cs.execute();
			System.out.println(cs.getString(1));
			log.info("successfully created user "+aname);
			//System.out.println("successfully created user "+aname);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	
	//user(String username, String password, String type, String firstname, String lastname)
	public List<user> getAlluser() { //gets entire user table
		
		List<user> userlist = new ArrayList<>();
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
			
			String sql = "select * from useraccount";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				userlist.add(new user(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userlist;
	}
	
	//bank(int id, String owner1, String owner2, String type, double funds, boolean active)
	public List<bank> getAllbank() { //gets entire bank table
		
		List<bank> banklist = new ArrayList<>();
		
		try(Connection con = DriverManager.getConnection(URL, username, password)){
			
			String sql = "select * from bankaccount";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				banklist.add(new bank(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getBoolean(6)));
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return banklist;
	}
	
}
