package tests;

import static org.junit.Assert.*;
import java.sql.*;


import org.junit.Test;

/**
 * 
 * @author Frédérique Bobier - 1952765
 * @version 1.0
 */
public class TestDBConnection {

	@Test
	public void testConnection() {
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    
	    	//Create a select statement from the users table and execute it
		    Statement stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT * FROM users;");

		    while(rs.next())
		    {
		    	//Assumes that the users table only has one entry and verifies
		    	//it has selected that statement
		    	assertEquals(rs.getInt("user_id"), 1);
		    	assertEquals(rs.getString("first_name"), "Bob");
		    	assertEquals(rs.getString("last_name"), "Jenkins");
		    	assertEquals(rs.getInt("is_owner"), 0);
		    }
		      rs.close();
		      stmt.close();
		      conn.close();
	    }
	    catch(Exception e) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
	    }
	}	
}
