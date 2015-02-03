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

public class ProjectManagerTest {
	
	public ProjectManager pm;
	public Project p1, p2, p3, p4;
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	@BeforeClass
	public void init() {
		pm = new ProjectManager();
		p1 = new Project("b_jenkins", "project1");
		p2 = new Project("b_jenkins", "project2");
		p3 = new Project("t_user", "project3");
		p4 = new Project("t_user", "project4");
		
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
	public void testCreateProjects() {
		// return project object with a project id after db insertion
		p1 = pm.addProject(p1);
		p2 = pm.addProject(p2);
		p3 = pm.addProject(p3);
		p4 = pm.addProject(p4);
		
		ArrayList<Project> result = new ArrayList<Project>();
		
		/* test to see if the projects were added to the DB */
		try {
			stmt = conn.createStatement();
			// ignore the project that's already in the DB (project_id 1)
			rs = stmt.executeQuery("SELECT * FROM projects WHERE project_id <> 1;");

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
		
		for ( Project p : result ) {
			switch(p.getName()) {
			case "project1":
				assertEquals(p1.getId(), p.getId());
				assertEquals(p1.getName(), p.getName());
				assertEquals(p1.getOwner(), p.getOwner());
				break;
			case "project2":
				assertEquals(p2.getId(), p.getId());
				assertEquals(p2.getName(), p.getName());
				assertEquals(p2.getOwner(), p.getOwner());
				break;
			case "project3":
				assertEquals(p3.getId(), p.getId());
				assertEquals(p3.getName(), p.getName());
				assertEquals(p3.getOwner(), p.getOwner());
				break;
			case "project4":
				assertEquals(p4.getId(), p.getId());
				assertEquals(p4.getName(), p.getName());
				assertEquals(p4.getOwner(), p.getOwner());
				break;
			default:
				fail("projects not added in the DB");
			}
		}
		
		ArrayList<Project> allProjs = pm.getProjects(); // get all projects
		ArrayList<Project> proj1 = pm.getProjects("t_user"); /*
													 * projects user_id t_user is
													 * involved in created by or
													 * has a task in project
													 */
		
		/* test getProjects() */
		int ind1 = allProjs.indexOf(p1);
		int ind2 = allProjs.indexOf(p2);
		int ind3 = allProjs.indexOf(p3);
		int ind4 = allProjs.indexOf(p4);
		
		assertTrue(allProjs.contains(p1));
		assertEquals(allProjs.get(ind1).getName(), "project1");
		assertTrue(allProjs.contains(p2));
		assertEquals(allProjs.get(ind2).getName(), "project2");
		assertTrue(allProjs.contains(p3));
		assertEquals(allProjs.get(ind3).getName(), "project3");
		assertTrue(allProjs.contains(p4));
		assertEquals(allProjs.get(ind4).getName(), "project1");
		
		/* test getProjects(int) - user id */
		int i1 = proj1.indexOf(p1);
		int i2 = proj1.indexOf(p2);
		
		assertTrue(proj1.contains(p1));
		assertEquals(allProjs.get(i1).getName(), "project1");
		assertTrue(proj1.contains(p2));
		assertEquals(allProjs.get(i2).getName(), "project1");
	}
	
	@Test
	public void testDeleteProjects()
	{
		ArrayList<Project> allProjs = pm.getProjects();
		
		pm.delProj(p1); // delete by object
		pm.delProj(p2.getId()); // delete by id
		pm.delProj(p3);
		pm.delProj(p4);
		
		ArrayList<Project> result = new ArrayList<Project>();
		
		/* test to see if the projects were deleted to the DB */
		try {
			stmt = conn.createStatement();
			// ignore the project that's already in the DB (project_id 1)
			rs = stmt.executeQuery("SELECT * FROM projects WHERE project_id <> 1;");

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
		assertFalse(allProjs.contains(p2));
		
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
