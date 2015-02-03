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
 * @author Fr��d��rique Bobier - 1952765
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
	
	@Test
	public void testTaskConstructorOne() {
		// Check to see that both tasks has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs;

			rs = stmt.executeQuery("SELECT * FROM tasks WHERE tasks.task_id = 3;");
			while (rs.next()) {
				assertEquals(rs.getInt("task_id"), t1.getId());
				assertEquals(rs.getString("task_name"), t1.getName());
				assertEquals(rs.getString("description"), t1.getDescription());
				assertEquals(rs.getInt("duration"), t1.getDuration());
				assertEquals(rs.getInt("project_id"), t1.getProjectID());
				assertEquals(rs.getString("user_id"), t1.getOwnerID());
			}
			
			rs = stmt.executeQuery("SELECT precedence.pre_req FROM tasks JOIN precedence ON tasks.task_id = precedence.task_id WHERE tasks.task_id = 3;");
			int i = 0;
			while (rs.next()) {
				assertEquals(rs.getInt("pre_req"), (int)t1.getPrereq().get(i));
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
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT * FROM tasks WHERE tasks.task_id = 3;");
			while (rs.next()) {
				assertEquals(rs.getInt("task_id"), t1.getId());
				assertEquals(rs.getString("description"), t1.getDescription());
				assertEquals(rs.getInt("is_done"), t1.isDone());
			}
			
			rs = stmt.executeQuery("SELECT precedence.pre_req FROM tasks JOIN precedence ON tasks.task_id = precedence.task_id WHERE tasks.task_id = 3;");
			int i = 0;
			while (rs.next()) {
				assertEquals(rs.getInt("pre_req"), (int)t1.getPrereq().get(i));
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
	
	@After
	public void testDeleteTask() {
		t1.deleteTask();
	}
}