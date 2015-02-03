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
		    ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = 'b_jenkins';");

		    while(rs.next())
		    {
		    	//Assumes that the users table only has one entry and verifies
		    	//it has selected that statement
		    	assertEquals(rs.getString("user_id"), "b_jenkins");
		    	assertEquals(rs.getString("user_pwd"), "1234");
		    	assertEquals(rs.getString("f_name"), "Bob");
		    	assertEquals(rs.getString("l_name"), "Jenkins");
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
