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

	private static Task t1, t2, t3, t4;
	private static ArrayList<Integer> myTasks = new ArrayList<Integer>();

	@Before
	public void Initialize(){
		//task_id, name, description, start_time, end_time, project_id, owner_id
		t1 = new Task(1, "1st Task", "Complete this before t3", 0, 2, 0, "b_jenkins");
		t2 = new Task(2, "2nd Task", "Complete this before t3", 0, 3, 0, "b_jenkins");
		t3 = new Task(3, "3rd Task", "Complete this after T1 and T2", 3, 5, 0, "b_jenkins");
		t4 = new Task(4, "4th Task", "Illegal pre-req task of t3", 1, 4, 0, "b_jenkins");
		t3.addPrereq(1);
		t3.addPrereq(2);
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

			rs = stmt.executeQuery("SELECT * FROM tasks WHERE tasks.task_id = "+t3.getId()+";");
			while (rs.next()) {
				assertEquals(t3.getId(), rs.getInt("task_id"));
				assertEquals(t3.getName(), rs.getString("task_name"));
				assertEquals(t3.getDescription(), rs.getString("description"));
				assertEquals(t3.getStartTime(), rs.getInt("start_time"));
				assertEquals(t3.getEndTime(), rs.getInt("end_time"));
				assertEquals(t3.getProjectID(), rs.getInt("project_id"));
				assertEquals(t3.getOwnerID(), rs.getString("user_id"));
			}
			
			//rs = stmt.executeQuery("SELECT precedence.pre_req FROM tasks JOIN precedence ON tasks.task_id = precedence.task_id WHERE tasks.task_id = 3;");
			rs = stmt.executeQuery("SELECT * FROM precedence WHERE task_id = "+t3.getId()+" AND project_id = "+t3.getProjectID()+";");
			int i = 1;
			while (rs.next()) {
				assertEquals(i, rs.getInt("pre_req"));
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
		t3.setDescription("T2 and T1 have to be done first.");
		t3.setName("AMAZING TASK 3!");
		//t3.addPrereq(2);
		t1.finishedTask();
		t2.finishedTask();
		t3.finishedTask();
		
		// Check to see that both tasks has been added to the database
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT * FROM tasks WHERE tasks.task_id = "+t3.getId()+";");
			while (rs.next()) {
				assertEquals(t3.getId(), rs.getInt("task_id"));
				assertEquals(t3.getDescription(), rs.getString("description"));
				assertEquals(t3.getName(), rs.getString("task_name"));
				assertEquals(t3.isDone(), rs.getInt("is_done"));
			}
			
			rs = stmt.executeQuery("SELECT precedence.pre_req FROM tasks JOIN precedence ON tasks.task_id = precedence.task_id WHERE tasks.task_id = 3;");
			int i = 0;
			while (rs.next()) {
				assertEquals((int)t3.getPrereq().get(i), rs.getInt("pre_req"));
				i++;
			}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage() + " in testEditTask()");
			System.exit(0);
		}
	}
	
	@Test
	public void testChangePreReqTime(){
		//Try to modify the end time of a pre-req task of t3
		//This shouldn't be allowed for the test to pass
		//Note that 6 > startTime of t3 that is illegal
		int testTime = 6;
		t1.setEndTime(testTime);
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT * FROM tasks WHERE tasks.task_id = "+t1.getId()+" AND project_id = "+t1.getId()+";");
			while (rs.next()) {
				assertNotEquals(testTime, rs.getInt("end_time"));
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
	public void testChangeTaskTimeWithPreReqs(){
		//Try to modify the start time of task t3 that has 
		//This shouldn't be allowed for the test to pass
		//Note that 1 < end time of both t1 and t2 that is illegal
		int testTime = 1;
		t3.setStartTime(testTime);
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT * FROM tasks WHERE tasks.task_id = "+t3.getId()+" AND project_id = "+t3.getId()+";");
			while (rs.next()) {
				assertNotEquals(testTime, rs.getInt("start_time"));
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
	public void addIllegalPreRequisite(){
		//The task t4 should not be added as a pre-requisite of Task 3 because it has illegal start_time and end_time (overlaps with start and end time of t3)
		t3.addPrereq(t4.getId());
		
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT * FROM precedence WHERE task_id = "+t3.getId()+" AND pre_req = "+t4.getId()+" AND project_id = "+t3.getProjectID()+";");
			
			int i = 0;
			while(rs.next()){
				i++;
			}
			assertEquals(0, i);
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	@Test
	public void testProjectMemberAssignment(){
		//void setMember(String username)
		//You should first check if the user exists and then assign it the task
		//**USE THE USER_TASK TABLE FROM THE DB
		String testUser = "hello";
		t3.setMember(testUser);
		
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");

			Statement stmt = conn.createStatement();
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT * FROM user_task WHERE task_id = "+t3.getId()+" AND project_id = "+t3.getProjectID()+";");
			while (rs.next()) {
				assertEquals(testUser, rs.getString("user_id"));
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
		t2.deleteTask();
		t3.deleteTask();
		t4.deleteTask();
	}
}