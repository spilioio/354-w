package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import core.Project;
import core.ProjectManager;
import core.Task;

/*
 * Run this class after TaskTest passes
 */
public class ProjectTest
{
	
	public static ProjectManager pm;
	public static Project p1;
	public Task t1, t2, t3;
	private static Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	@BeforeClass
	public static void init()
	{
		pm = new ProjectManager();
		
		pm.clean();
		
		p1 = new Project("b_jenkins", "project1");
		
		conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in init()");
			System.exit(0);
		}
	}
	
	@Test
	public void testCreateProject()
	{
		
		System.out.println(p1.getName() + " " + p1.getOwner());
		
		/* test to see if the projects were added to the DB */
		try
		{
			stmt = conn.createStatement();
			// ignore the project that's already in the DB (project_id 1)
			rs = stmt
					.executeQuery("SELECT * FROM projects WHERE project_name = 'project1' AND owner_id = 'b_jenkins';");
			
			while (rs.next())
			{
				assertEquals(p1.getId(), rs.getInt("project_id"));
				assertEquals(p1.getName(), rs.getString("project_name"));
				assertEquals(p1.getOwner(), rs.getString("owner_id"));
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in testCreateProject()");
			System.exit(0);
		}
	}
	
	@Test
	public void testEditProject()
	{
		p1.setName("p1");
		p1.setOwner("t_user");
		
		Project p = pm.getProject(p1);
		
		/* test to see if the projects were added to the DB */
		try
		{
			stmt = conn.createStatement();
			
			rs = stmt
					.executeQuery("SELECT * FROM projects WHERE project_name = 'p1' AND owner_id = 't_user';");
			
			while (rs.next())
			{
				assertEquals(p1.getId(), rs.getInt("project_id"));
				assertEquals(p1.getName(), rs.getString("project_name"));
				assertEquals(p1.getOwner(), rs.getString("owner_id"));
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in testEditProject()");
			System.exit(0);
		}
		
		assertEquals(p1.getId(), p.getId());
		assertEquals(p1.getName(), p.getName());
		assertEquals(p1.getOwner(), p.getOwner());
		assertEquals(p.getName(), "p1");
		assertEquals(p.getOwner(), "t_user");
	}
	
	@Test
	public void testAddTask()
	{
		// ArrayList<Integer> preReq = new ArrayList<Integer>();
		t1 = new Task( "a", "desc a", 1, 4, p1.getId(), "b_jenkins");
		
		/* test to see if the task was added to the DB */
		try
		{
			stmt = conn.createStatement();
			String query = "SELECT * FROM tasks WHERE task_name = 'a' "
					+ "AND description = 'desc a' "
					+ "AND user_id = 'b_jenkins' " + "AND project_id = "
					+ p1.getId();
			
			rs = stmt.executeQuery(query);
			
			while (rs.next())
			{
				assertEquals(t1.getId(), rs.getInt("task_id"));
				assertEquals(t1.getName(), rs.getString("task_name"));
				assertEquals(t1.getDescription(), rs.getString("description"));
				assertEquals(t1.getProjectID(), rs.getInt("project_id"));
				assertEquals(t1.getOwnerID(), rs.getString("user_id"));
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in testAddTask()");
			System.exit(0);
		}
		
		ArrayList<Task> t = p1.getTasks();
		
		int i1 = t.indexOf(t1);
		assertTrue(t.contains(t1));
		assertEquals(t.get(i1).getName(), "a");
		
		assertEquals("a", t1.getName());
		
	}
	
	/*
	 * @Test public void testAddProjectProperties(){ //addProperty(String
	 * property) String testProperty_1, testProperty_2; testProperty_1 =
	 * "This is a first property!"; testProperty_2 =
	 * "This is a second property!";
	 * 
	 * //USE THE PROJECT_PROPERTY TABLE FROM THE DB
	 * p1.addProperty(testProperty_1); p1.addProperty(testProperty_2); // test
	 * to see if the projects were deleted to the DB try { stmt =
	 * conn.createStatement();
	 * 
	 * rs = stmt.executeQuery(
	 * "SELECT * FROM project_property WHERE project_property.project_name = 'p1' && project_property.project_prop = "
	 * +testProperty_1+";");
	 * 
	 * while (rs.next()) { assertEquals(p1.getId(), rs.getInt("project_id"));
	 * assertEquals(testProperty_1, rs.getString("project_prop")); }
	 * 
	 * rs = stmt.executeQuery(
	 * "SELECT * FROM project_property WHERE project_property.project_name = 'p1' && project_property.project_prop = "
	 * +testProperty_2+";");
	 * 
	 * while (rs.next()) { assertEquals(p1.getId(), rs.getInt("project_id"));
	 * assertEquals(testProperty_2, rs.getString("project_prop")); }
	 * 
	 * rs.close(); stmt.close(); } catch(Exception e) { System.err.println(
	 * e.getClass().getName() + ": " + e.getMessage() +
	 * " in testDeleteProject()" ); System.exit(0); } }
	 */
	
	@Test
	public void testDeleteProject()
	{
		
		p1.delProj();
		// pm.delProj(p1); // delete by object
		
		ArrayList<Project> result = new ArrayList<Project>();
		
		/* test to see if the projects were deleted to the DB */
		try
		{
			stmt = conn.createStatement();
			// ignore the project that's already in the DB (project_id 1)
			rs = stmt
					.executeQuery("SELECT * FROM projects WHERE project_name = 'p1' AND owner_id = 't_user';");
			
			while (rs.next())
			{
				result.add(new Project(rs.getString("owner_id"), rs
						.getString("project_name")));
			}
			rs.close();
			stmt.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in testDeleteProject()");
			System.exit(0);
		}
		
		assertEquals(result.size(), 0);
		assertFalse(result.contains(p1));
	}
	
	// - Create project
	// - Add tasks
	// - Gantt algorithm should yield ordered list of tasks
	// - test will predict correct order by doing assertEquals on each position
	// of the list.
	// - Can also predict start time and end time of project (see first start time
	// of ordered list, and last end time.)
	@Test
	public void testGANTTValues()
	{
		//task_id, name, description, start_time, end_time, project_id, owner_id
		Task task0, task1, task2;
		Project project0 = new Project("b_jenkins", "GANTT TEST PROJECT");
		
		task0 = new Task("Task 0", "Complete this before Task 1", 0, 3, project0.getId(), "b_jenkins");
		task1 = new Task("Task 1", "Complete this before Task 2", 4, 6, project0.getId(), "b_jenkins");
		task2 = new Task("Task 2", "Final task", 8, 10, project0.getId(), "b_jenkins");
		
		Task[] taskCollection =  project0.GANTTAnalysis();
		
		// Is size the same?
		assertEquals(taskCollection.length, 3);
		
		// Is the order correct?
		assertEquals(taskCollection[0], task0);
		assertEquals(taskCollection[1], task1);
		assertEquals(taskCollection[2], task2);
		
		// Is the total time correct?
		assertEquals(taskCollection[2].getStartTime() - 
				taskCollection[0].getStartTime(), 10);
		
		// Is the start time correct?
		assertEquals(taskCollection[0].getStartTime(), 0);
		
		// How about end time?
		assertEquals(taskCollection[0].getEndTime(), 10);
	}
	
	
//	
//	/*
//	 * -Create a project
//	 * -Add a series of interdependent Tasks to the project
//	 * -Organize the project with organize() which should set the proper dates
//	 * 
//	 * -perform a PERTAnalysis() on the project to set the expectedDate,standardDeviation, MostLikelyDuration, PessimisticDuration, OptimisticDuration
//	 *  of the each task(these are projected values for the END of each Task)
//	 *  
//	 * -test will predict expected start date and standard deviation of each task and the 
//	 * 	project's START and END times and assertEquals on each value returned by PERTAnalysis()
//	 */
//	@Test
//	public void testPERTAnalysis()
//	{
//		//This is a very simple project other tests will try more complicated ones
//		ProjectManager pm;
//		Project p1;
//		
//		pm = new ProjectManager();
//		
//		pm.delAllProjects();
//		
//		
//		//Create a Project
//		p1 = new Project("b_jenkins", "testPERTAnalysis");
//		
//		Task t1 = new Task("Task1", "must be completed before Task 2 ", 0, 5, p1.getId(), "b_jenkins");
//		Task t2 = new Task("Task2", "must be completed before Task 3", 0, 5, p1.getId(), "b_jenkins" );
//		Task t3 = new Task("Task3", "Last task in the project", 0, 5, p1.getId(), "b_jenkins" );
//		
//		
//		//add task prerequisite
//		t3.addPrereq(t2.getId());
//		t2.addPrereq(t1.getId());
//		//t1 should start first and has no prerequisites
//		
//		p1.organize();
//		
//		//perform PERT analysis on the project this should gives new assigned new variables to
//		//our tasks needed to perform the PERTAnalysis
//		p1.PERTAnalysis();
//		
//		
//		//Assertions regarding early finish/start dates because
//		//PERT requires these values to be right
//		assertEquals(t1.getEarlyStart(), 0);
//		assertEquals(t1.getEarlyFinish(), 5);
//		assertEquals(t1.getLateStart(), 0);
//		assertEquals(t1.getLateFinish(), 5);
//		assertEquals(t1.getFloat(), 0);
//		
//		assertEquals(t2.getEarlyStart(), 6);
//		assertEquals(t2.getEarlyFinish(), 10);
//		assertEquals(t2.getLateStart(), 6);
//		assertEquals(t2.getLateFinish(), 10);
//		assertEquals(t2.getFloat(), 0);
//		
//		assertEquals(t3.getEarlyStart(), 11);
//		assertEquals(t3.getEarlyFinish(), 15);
//		assertEquals(t3.getLateStart(), 11);
//		assertEquals(t3.getLateFinish(), 15);
//		assertEquals(t3.getFloat(), 0);
//		
//		//Assertions regarding PERT Values
//		
//		
//		
//		
//	}
	
	@AfterClass
	public static void end()
	{
		try
		{
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in end()");
			System.exit(0);
		}
	}
}
