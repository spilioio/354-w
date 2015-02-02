package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Test;

import core.Task;
import core.User;

/**
 * 
 * @author Frédérique Bobier - 1952765
 * @version 1.0
 */
public class UserTest {
	private User myUser;

	@Test
	public void testUserConstructor() {

		// Create a user: userName (PK - unique), userPwd, fName, lName
		// The password shouldn't be stored
		myUser = new User("f_bobier", "qwerty", "Fred", "Bob");

		// Check to see that the user has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = f_bobier AND user_pwd = qwerty;");

			while (rs.next()) {
				assertEquals(rs.getString("user_id"), myUser.getName());
				assertEquals(rs.getString("user_pwd"), "qwerty");
				assertEquals(rs.getString("f_name"), myUser.getFirstName());
				assertEquals(rs.getString("l_name"), myUser.getLastName());
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
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = f_bobier;");

			while (rs.next()) {
				assertEquals(rs.getString("user_id"), myUser.getName());
				assertEquals(rs.getString("f_name"), myUser.getFirstName());
				assertEquals(rs.getString("l_name"), myUser.getLastName());
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
	public void testDeleteUser() {
		myUser.deleteUser();

		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = f_bobier;");

			assertEquals(rs.getRow(), 0);

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}
