package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.User;

/**
 * 
 * @author Fr��d��rique Bobier - 1952765
 * @version 1.0
 */

public class UserTest {
	private static User myUser;

	@Before
	public void Initialize(){
		myUser = new User("f_bobier", "qwerty", "Fred", "Bob");
	}
	
	@Test
	public void testUserConstructor() {
		// Create a user: userName (PK - unique), userPwd, fName, lName
		// The password shouldn't be stored
		
		// Check to see that the user has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = 'f_bobier' AND user_pwd = 'qwerty';");

			while (rs.next()) {
				assertEquals(myUser.getName(), rs.getString("user_id"));
				assertEquals("qwerty", rs.getString("user_pwd"));
				assertEquals(myUser.getFirstName(), rs.getString("f_name"));
				assertEquals(myUser.getLastName(), rs.getString("l_name"));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	@Test
	public void testEditUser() {
		myUser.modifyLastName("Bobier");

		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = 'f_bobier';");

			while (rs.next()) {
				assertEquals(myUser.getName(), rs.getString("user_id"));
				assertEquals(myUser.getFirstName(), rs.getString("f_name"));
				assertEquals(myUser.getLastName(), rs.getString("l_name"));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	@After
	public void testDeleteUser() {
		myUser.deleteUser();
	}
}
