package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.*;

import core.Task;

/**
 * 
 * @author Frédérique Bobier - 1952765
 * @version 1.0
 */
public class TaskTest {

	private static Task t1;
	private static ArrayList<Integer> myTasks = new ArrayList<Integer>();

	@Before 
	public void Initialize(){
		myTasks.add(1);
		//task_id, name, description, duration, project_id, owner_id, pre-reqs
		t1 = new Task(3, "3rd Task", "Complete this after T1", 4, 1, "b_jenkins", myTasks);
	}
	
	/*@Test
	public void testTaskConstructorOne() {
		// Check to see that both tasks has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT tasks.task_id, precedence.pre_req FROM tasks JOIN precedence ON tasks.task_id = precedence.task_id;");

			int i = 0;
			while (rs.next()) {
				assertEquals(rs.getString("tasks.task_id"), t1.getTaskId());
				assertEquals(rs.getString("precedence.pre_req"), t1.getPrereq().get(i));
				i++;
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
	public void testEditTask() {
		t1.setDescription("Complete this after T1 and T2.");
		t1.addPrereq(2);
		t1.finishedTask();
		
		// Check to see that both tasks has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT tasks.task_id tasks.is_done, precedence.pre_req FROM tasks JOIN precedence ON tasks.task_id = precedence.task_id;");

			int i = 0;
			while (rs.next()) {
				assertEquals(rs.getString("tasks.task_id"), t1.getTaskId());
				assertEquals(rs.getString("tasks.is_done"), t1.isDone());
				assertEquals(rs.getString("precedence.pre_req"), t1.getPrereq().get(i));
				i++;
			}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}*/
	
	@Test
	public void testDeleteTask() {
		t1.deleteTask();
		
		// Check to see that both tasks has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM tasks WHERE task_name = '3rd Task';");
			

			//The task will not be able to be deleted if it's a pre-requisit for another task.
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