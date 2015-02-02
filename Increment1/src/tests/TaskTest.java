package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Test;

import core.Task;

public class TaskTest {

	private Task t1;
	private ArrayList<Integer> myTasks = new ArrayList<Integer>();

	@Test
	public void testTaskConstructor() {
		myTasks.add(1);
		//name, description, duration, project_id, owner_id, pre-reqs
		t1 = new Task("3rd Task", "Complete this after T1", 4, 1, "b_jenkins", myTasks);
		
		// Check to see that both tasks has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT tasks.task_id, precedence.pre_req FROM tasks JOIN precedence ON tasks.task_id = precedence.task_id;");

			while (rs.next()) {
				assertEquals(rs.getString("tasks.task_id"), t1.getTaskId());
				assertEquals(rs.getString("precedence.pre_req"), t1.getPrereq().get(0));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
}
