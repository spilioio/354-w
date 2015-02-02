package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class User {
	private String userName, userPwd, fname, lname;
	
	public User(String userName, String userPwd, String fname, String lname){
		this.userName = userName;
		this.userPwd = userPwd;
		this.fname = fname;
		this.lname = lname;
		//Add the user to the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("INSERT INTO users VALUES ('"+userName+ "', '"+userPwd+"', '"+fname+"', '"+lname+"');");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record created successfully");
	}
	
	public void deleteUser(){
		//delete from database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("DELETE FROM users WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record deleted successfully");
	}
	
	public String getName(){
		return userName;
	}
	
	public String getFirstName(){
		return fname;
	}
	
	public String getLastName(){
		return lname;
	}
	
	public void modifyLastName(String lname){
		this.lname = lname;
		//modify the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE users SET l_name = '"+lname+"' WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record last name updated successfully");
	}
	
	public void modifyFirstName(String fname){
		this.fname = fname;
		//modify the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE users SET f_name = '"+fname+"' WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record first name updated successfully");
	}
	
	public void modifyPwd(String userPwd){
		this.userPwd = userPwd;
		//modify the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE users SET user_pwd = '"+userPwd+"' WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record password updated successfully");
	}
}