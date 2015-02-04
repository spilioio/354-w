package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author Fr��d��rique Bobier - 1952765
 * @version 1.0
 * 
 * To run after UserTest.java and TaskText.java to confirm that the user / task created
 * have both been deleted.
 */
public class DeleteUserTaskTest {

	@Test
	public void testDeleteUser() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = 'f_bobier';");

			assertEquals(0, rs.getRow());

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	@Test
	public void testDeleteTask() {
		// Check to see that both tasks has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs;
			
			//Make sure the task is deleted and that it is also deleted from the precedence table
			rs = stmt.executeQuery("SELECT * FROM tasks WHERE task_id = 3;");
			assertEquals(0, rs.getRow());
			rs = stmt.executeQuery("SELECT * FROM precedence WHERE task_id = 3;");
			assertEquals(0, rs.getRow());
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

}
