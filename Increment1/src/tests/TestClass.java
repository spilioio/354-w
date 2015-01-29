package tests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * All tests assumes there is a user with id 1, and a user with id 2
 * in the database
 */
public class TestClass {

//	public ProjectManager pm;
//	public Project p1, p2, p3, p4;
//	public Task t1, t2;
//	
//	/*
//	 * TODO: add database conection
//	 */
//	@BeforeClass
//	public void init() {
//		pm = new ProjectManager();
//		
//		/* project name is not in the db, but should be added */
//		// user id, project name
//		p1 = new Project(1, "project1");
//		p2 = new Project(1, "project2");
//		p3 = new Project(2, "project3");
//		p4 = new Project(2, "project4");
//		
//		 // project id, user id, earliest start, duration, label
//		t1 = new Task(3, 1, 0, 1, "t1");
//		t2 = new Task(3, 2, 0, 1, "t2");
//	}
	
//	@Test
//	public void testCreateProjects() {
//		// return project object with a project id after db insertion
//		p1 = pm.addProject(p1);
//		p2 = pm.addProject(p2);
//		p3 = pm.addProject(p3);
//		p4 = pm.addProject(p4);
//		
//		ArrayList<Project> allProjs = pm.getProjects(); // get all projects
//		ArrayList<Project> proj1 = pm.getProjects(1); /* projects user id 1 is involved in
//														created by or has a task in project */
//		
//		/* test getProjects() */
//		int ind1 = allProjs.indexOf(p1);
//		int ind2 = allProjs.indexOf(p2);
//		int ind3 = allProjs.indexOf(p3);
//		int ind4 = allProjs.indexOf(p4);
//		
//		assertTrue(allProjs.contains(p1));
//		assertEquals(allProjs.get(ind1).getName(), "project1");
//		assertTrue(allProjs.contains(p2));
//		assertEquals(allProjs.get(ind2).getName(), "project2");
//		assertTrue(allProjs.contains(p3));
//		assertEquals(allProjs.get(ind3).getName(), "project3");
//		assertTrue(allProjs.contains(p4));
//		assertEquals(allProjs.get(ind4).getName(), "project1");
//		
//		/* test getProjects(int) - user id*/
//		int i1 = proj1.indexOf(p1);
//		int i2 = proj1.indexOf(p2);
//		
//		assertTrue(proj1.contains(p1));
//		assertEquals(allProjs.get(i1).getName(), "project1");
//		assertTrue(proj1.contains(p2));
//		assertEquals(allProjs.get(i2).getName(), "project1");
//		
//	}
//	
//	@Test
//	public void testEditProjects() {
//		p1.setName("p1"); // change name
//		pm.setProj(p1.getId(), p1); // project id, new project
//		
//		Project proj = pm.getProject(p.getId()); // get a single project by project id
//												 // don't confuse with getProjects()
//				
//		assertEquals(proj.getName(), "p1");
//	}
//	
//	@Test
//	public void testDeleteProjects() {
//		ArrayList<Project> allProjs = pm.getProjects();
//		
//		pm.delProj(p1); // delete by object
//		pm.delProj(p2.getId()); // delete by id
//		
//		assertFalse(allProjs.contains(p1));
//		assertFalse(allProjs.contains(p2));
//	}
//	
//	@Test
//	public void testCreateTasks() {
//		pm.addTask(p3.getId(), t1);
//		pm.addTask(p3, t2);
//
//		ArrayList<Task> t = pm.getProject(p3).getTasks(); // get all tasks
//														  // get project by project object
//		
//		/* test getTasks() */
//		int i1 = t.indexOf(t1);
//		int i2 = t.indexOf(t2);
//		
//		assertTrue(t.contains(t1));
//		assertEquals(t.get(i1).getName(), "t1");
//		assertTrue(t.contains(t2));
//		assertEquals(t.get(i2).getName(), "t2");
//		
//		/* test getTask(int) - task id */
//		Task task = pm.getProject(p3.getId()).getTask(t1.getId());
//		assertTrue(task.getName(), "t1");
//	}
}

