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
public class ProjectTest {
	
	public ProjectManager pm;
	public Project p1;
	public Task t1, t2, t3;
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	@BeforeClass
	public void init() {
		pm = new ProjectManager();
		p1 = new Project("b_jenkins", "project1");
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    }
	    catch(Exception e) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
	    }
	}
	
	@Test
	public void testCreateProject() {
		p1 = pm.addProject(p1);
		
		/* test to see if the projects were added to the DB */
		try {
			stmt = conn.createStatement();
			// ignore the project that's already in the DB (project_id 1)
			rs = stmt.executeQuery("SELECT * FROM projects WHERE project_name = 'project1' && owner_id = 'b_jenkins';");

			while (rs.next()) {
				assertEquals(p1.getId(), rs.getInt("project_id"));
				assertEquals(p1.getName(), rs.getString("project_name"));
				assertEquals(p1.getOwner(), rs.getString("owner_id"));
			}
			rs.close();
		    stmt.close();
		}
		catch(Exception e) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
	    }
	}
	
	@Test
	public void testEditProject() {
		p1.setName("p1");
		p1.setOwner("t_user");
		
		pm.setProj(p1.getId(), p1);
		
		Project p = pm.getProject(p1);
		
		/* test to see if the projects were added to the DB */
		try {
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery("SELECT * FROM projects WHERE project_name = 'p1' && owner_id = 't_user';");

			while (rs.next()) {
				assertEquals(p1.getId(), rs.getInt("project_id"));
				assertEquals(p1.getName(), rs.getString("project_name"));
				assertEquals(p1.getOwner(), rs.getString("owner_id"));
			}
			rs.close();
		    stmt.close();
		}
		catch(Exception e) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
	    }
		
		assertEquals(p1.getId(), p.getId());
		assertEquals(p1.getName(), p.getName());
		assertEquals(p1.getOwner(), p.getOwner());
		assertEquals(p.getName(), "p1");
		assertEquals(p.getOwner(), "t_user");
	}
	
	@Test
	public void testAddTask() {
		ArrayList<Integer> preReq = new ArrayList<Integer>();
		t1 = new Task("a", "desc a", 4, 1, "b_jenkins", preReq);

		p1.addTask(t1);
		
		/* test to see if the task was added to the DB */
		try {
			stmt = conn.createStatement();
			String query = "SELECT * FROM tasks WHERE task_name = 'a' " +
					"&& description = 'desc a' " +
					"&& user_id = 'b_jenkins' " +
					"&& project_id = " + 
					p1.getId();
			
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				assertEquals(t1.getId(), rs.getInt("task_id"));
				assertEquals(t1.getName(), rs.getString("task_name"));
				assertEquals(t1.getDesc(), rs.getString("description"));
				assertEquals(t1.getProjId(), rs.getString("project_id"));
				assertEquals(t1.getOwner(), rs.getString("user_id"));
			}
			rs.close();
		    stmt.close();
		}
		catch(Exception e) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
	    }
		
		ArrayList<Task> t = p1.getTasks();
		
		int i1 = t.indexOf(t1);
		assertTrue(t.contains(t1));
		assertEquals(t.get(i1).getName(), "t1");
		
		Task tk = p1.getTask(t1.getId());
		assertEquals("t1", t1.getName());
		
	}
	
	@Test
	public void testDeleteProject() {
		ArrayList<Project> allProjs = pm.getProjects();
		
		pm.delProj(p1); // delete by object

		ArrayList<Project> result = new ArrayList<Project>();
		
		/* test to see if the projects were deleted to the DB */
		try {
			stmt = conn.createStatement();
			// ignore the project that's already in the DB (project_id 1)
			rs = stmt.executeQuery("SELECT * FROM projects WHERE project_name = 'p1' && owner_id = 't_user';");

			while (rs.next()) {
				result.add(new Project(rs.getString("owner_id"), rs.getString("project_name")));
			}
			rs.close();
		    stmt.close();
		}
		catch(Exception e) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
	    }
		
		assertEquals(result.size(), 0);
		assertFalse(allProjs.contains(p1));
	}
	
	@AfterClass
	public void end() {
		try {
		    conn.close();
		}
		catch(Exception e) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
	    }
	}
}
