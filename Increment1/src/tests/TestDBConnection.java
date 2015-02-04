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
		    	assertEquals("b_jenkins", rs.getString("user_id"));
		    	assertEquals("1234", rs.getString("user_pwd"));
		    	assertEquals("Bob", rs.getString("f_name"));
		    	assertEquals("Jenkins", rs.getString("l_name"));
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
