package tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

import core.Project;
import core.ProjectManager;
import core.Task;

public class CriticalPathAnalysisTest {
	

	
	public void OrganizeProjectTest1()
	{
		ProjectManager pm;
		Project p1;
		
		pm = new ProjectManager();
		
		pm.delAllProjects();
		
		
		//Create a Project
		p1 = new Project("b_jenkins", "Critical Path Test1");
		/*
		 *All these tasks have a duration of 5 days.(0 startTime 5 endTime)
		 *the start time and end Time should be rearranged once we have assigned dependencies
		 *and called forward pass
		 */
		Task t1 = new Task("Task1", "must be completed before Task 2 and Task 3", 0, 5, p1.getId(), "b_jenkins");
		Task t2 = new Task("Task2", "must be completed before Task 4 and Task 5", 0, 5, p1.getId(), "b_jenkins" );
		Task t3 = new Task("Task3", "must be completed before Task 7", 0, 5, p1.getId(), "b_jenkins" );
		Task t4 = new Task("Task4", "must be completed before Task 6", 0, 5, p1.getId(), "b_jenkins" );
		Task t5 = new Task("Task5", "must be completed before Task 6", 0, 5, p1.getId(), "b_jenkins" );
		Task t6 = new Task("Task6", "must be completed before Task 8", 0, 5, p1.getId(), "b_jenkins" );
		Task t7 = new Task("Task7", "must be completed before Task 8", 0, 5, p1.getId(), "b_jenkins" );
		Task t8 = new Task("Task8", "Last Task to be completed", 0, 5, p1.getId(), "b_jenkins" );
		
		//adding interdependencies to the tasks we created
		t8.addPrereq(t6.getId());
		t8.addPrereq(t7.getId());
		
		t7.addPrereq(t3.getId());
		
		t6.addPrereq(t5.getId());
		t6.addPrereq(t4.getId());
		
		t5.addPrereq(t2.getId());
		
		t4.addPrereq(t2.getId());
		
		t3.addPrereq(t1.getId());
		
		t2.addPrereq(t1.getId());
		
		//Task 1 has no prerequisites and should be the first to start
		
		
		//The forward pass should set the earliest start dates for all of the projects task
		//A project depending on a previous project should start the DAY AFTER the previous project
		//has finished 
		//ex: if task 1 starts on day 1 and ends Day 5 Task 2 should start on Day 6
		//if it depends on task 1.
		p1.organize();
		
		
		//Check Early start and Finish this should be set by the forward pass
		assertEquals(t1.getEarlyStart(), 0);
		assertEquals(t1.getEarlyFinish(), 5);
		
		assertEquals(t2.getEarlyStart(), 6);
		assertEquals(t2.getEarlyFinish(), 10);
		
		assertEquals(t3.getEarlyStart(), 6);
		assertEquals(t3.getEarlyFinish(), 10);
		
		assertEquals(t4.getEarlyStart(), 11);
		assertEquals(t4.getEarlyFinish(), 15);
		
		assertEquals(t5.getEarlyStart(), 11);
		assertEquals(t5.getEarlyFinish(), 15);
		
		assertEquals(t6.getEarlyStart(), 16);
		assertEquals(t6.getEarlyFinish(), 20);
		
		assertEquals(t7.getEarlyStart(), 11);
		assertEquals(t7.getEarlyFinish(), 15);
		
		assertEquals(t8.getEarlyStart(), 21);
		assertEquals(t8.getEarlyFinish(), 25);
		
		
		//Check Late start, Late Finish and Float values for all Tasks in the Project
		//these value should be set during the backwards pass
		assertEquals(t1.getLateStart(), 1);
		assertEquals(t1.getLateFinish(), 5);
		assertEquals(t1.getFloat(), 0);
		
		assertEquals(t2.getLateStart(), 6);
		assertEquals(t2.getLateFinish(), 10);
		assertEquals(t2.getFloat(), 0);
		
		assertEquals(t3.getLateStart(), 11);
		assertEquals(t3.getLateFinish(), 15);
		assertEquals(t3.getFloat(), 5);
		
		assertEquals(t4.getLateStart(), 11);
		assertEquals(t4.getLateFinish(), 15);
		assertEquals(t4.getFloat(), 0);
		
		assertEquals(t5.getLateStart(), 11);
		assertEquals(t5.getLateFinish(), 15);
		assertEquals(t5.getFloat(), 0);
		
		assertEquals(t6.getLateStart(), 16);
		assertEquals(t6.getLateFinish(), 20);
		assertEquals(t6.getFloat(), 0);
		
		assertEquals(t7.getLateStart(), 16);
		assertEquals(t7.getLateFinish(), 20);
		assertEquals(t7.getFloat(), 5);
		
		assertEquals(t8.getLateStart(), 21);
		assertEquals(t8.getLateFinish(), 25);
		assertEquals(t8.getFloat(), 0);
		
		//delete the project from database
		p1.delProj();
	}

	
	//This Scenario Tests a Linear Dependency between 3 tasks
	@Test
	public void organizeProjectTest2()
	{
		ProjectManager pm;
		Project p1;
		
		pm = new ProjectManager();
		
		pm.delAllProjects();
		
		
		//Create a Project
		p1 = new Project("b_jenkins", "Critical Path Test2");
		
		Task t1 = new Task("Task1", "must be completed before Task 2 ", 0, 5, p1.getId(), "b_jenkins");
		Task t2 = new Task("Task2", "must be completed before Task 3", 0, 5, p1.getId(), "b_jenkins" );
		Task t3 = new Task("Task3", "Last task in the project", 0, 5, p1.getId(), "b_jenkins" );
		
		t3.addPrereq(t2.getId());
		t2.addPrereq(t1.getId());
		//t1 should start first and has no prerequisites
		
		p1.organize();
		
		assertEquals(t1.getEarlyStart(), 0);
		assertEquals(t1.getEarlyFinish(), 5);
		assertEquals(t1.getLateStart(), 0);
		assertEquals(t1.getLateFinish(), 5);
		assertEquals(t1.getFloat(), 0);
		
		assertEquals(t2.getEarlyStart(), 6);
		assertEquals(t2.getEarlyFinish(), 10);
		assertEquals(t2.getLateStart(), 6);
		assertEquals(t2.getLateFinish(), 10);
		assertEquals(t2.getFloat(), 0);
		
		assertEquals(t3.getEarlyStart(), 11);
		assertEquals(t3.getEarlyFinish(), 15);
		assertEquals(t3.getLateStart(), 11);
		assertEquals(t3.getLateFinish(), 15);
		assertEquals(t3.getFloat(), 0);
		
		p1.delProj();
	}
	
	//This tests the scenario where 3 tasks have no dependencies between each other but task 4 depends on all the other 3
	//The critical path in this case should be set to the task with the longest duration
	@Test
	public void organizeProjectTest3()
	{
		ProjectManager pm;
		Project p1;
		
		pm = new ProjectManager();
		
		pm.delAllProjects();
		
		
		//Create a Project
		p1 = new Project("b_jenkins", "Critical Path Test3");
		
		Task t1 = new Task("Task1", "must be completed before Task 4", 0, 5, p1.getId(), "b_jenkins");
		Task t2 = new Task("Task2", "must be completed before Task 4", 0, 10, p1.getId(), "b_jenkins" );
		Task t3 = new Task("Task3", "must be completed before Task 4", 0, 7, p1.getId(), "b_jenkins" );
		Task t4 = new Task("Task4", "Last Task to be completed", 0, 5, p1.getId(), "b_jenkins");
		
		t4.addPrereq(t1.getId());
		t4.addPrereq(t2.getId());
		t4.addPrereq(t3.getId());
		
		p1.organize();
		
		assertEquals(t1.getEarlyStart(), 0);
		assertEquals(t1.getEarlyFinish(), 5);
		assertEquals(t1.getLateStart(), 6);
		assertEquals(t1.getLateFinish(), 10);
		assertEquals(t1.getFloat(), 5);
		
		assertEquals(t2.getEarlyStart(), 0);
		assertEquals(t2.getEarlyFinish(), 10);
		assertEquals(t2.getLateStart(), 0);
		assertEquals(t2.getLateFinish(), 10);
		assertEquals(t2.getFloat(), 0);
		
		assertEquals(t2.getEarlyStart(), 11);
		assertEquals(t2.getEarlyFinish(), 15);
		assertEquals(t2.getLateStart(), 11);
		assertEquals(t2.getLateFinish(), 15);
		assertEquals(t2.getFloat(), 0);
		
		p1.delProj();
	}
	
	
	
	//This test should check if the Tasks within the project have circular dependencies
	//In this case an exception should be thrown to indicate that the project's task prereqs are incorrect
	@Test(expected = CircularityException.class)
	public void CircularPathTest()
	{
		
		ProjectManager pm2;
		Project p2;
		
		pm2 = new ProjectManager();
		
		pm2.delAllProjects();
		
		p2 = new Project("b_jenkins", "CircularPathTest");
		
		Task t1 = new Task("Task1", "must be completed before Task 2 and Task 3", 0, 5, p2.getId(), "b_jenkins");
		Task t2 = new Task("Task2", "must be completed before Task 4 and Task 5", 0, 5, p2.getId(), "b_jenkins" );
		Task t3 = new Task("Task3", "must be completed before Task 7", 0, 5, p2.getId(), "b_jenkins" );
		Task t4 = new Task("Task4", "must be completed before Task 6", 0, 5, p2.getId(), "b_jenkins" );
		
		t2.addPrereq(t1.getId());
		t3.addPrereq(t2.getId());
		t4.addPrereq(t3.getId());
		
		//this prereq makes the project circular which should not be allowed
		t1.addPrereq(t4.getId());
		
		//the organize() method should throw a custom exception to indicate the project has a circular dependency
		
		p2.delProj();
	}
	
	public void CriticalPathTest(){
		
		ProjectManager pm;
		Project p1;
		
		pm = new ProjectManager();
		
		pm.delAllProjects();
		
		
		//Create a Project
		p1 = new Project("b_jenkins", "Critical Path Test");
		/*
		 *All these tasks have a duration of 5 days.(0 startTime 5 endTime)
		 *the start time and end Time should be rearranged once we have assigned dependencies
		 *and called forward pass
		 */
		Task t1 = new Task("Task1", "must be completed before Task 2 and Task 3", 0, 5, p1.getId(), "b_jenkins");
		Task t2 = new Task("Task2", "must be completed before Task 4 and Task 5", 0, 5, p1.getId(), "b_jenkins" );
		Task t3 = new Task("Task3", "must be completed before Task 7", 0, 5, p1.getId(), "b_jenkins" );
		Task t4 = new Task("Task4", "must be completed before Task 6", 0, 5, p1.getId(), "b_jenkins" );
		Task t5 = new Task("Task5", "must be completed before Task 6", 0, 5, p1.getId(), "b_jenkins" );
		Task t6 = new Task("Task6", "must be completed before Task 8", 0, 5, p1.getId(), "b_jenkins" );
		Task t7 = new Task("Task7", "must be completed before Task 8", 0, 5, p1.getId(), "b_jenkins" );
		Task t8 = new Task("Task8", "Last Task to be completed", 0, 5, p1.getId(), "b_jenkins" );
		
		//adding interdependencies to the tasks we created
		t8.addPrereq(t6.getId());
		t8.addPrereq(t7.getId());
		
		t7.addPrereq(t3.getId());
		
		t6.addPrereq(t5.getId());
		t6.addPrereq(t4.getId());
		
		t5.addPrereq(t2.getId());
		
		t4.addPrereq(t2.getId());
		
		t3.addPrereq(t1.getId());
		
		t2.addPrereq(t1.getId());
		
		//Task 1 has no prerequisites and should be the first to start
		
		
		//Get the name of the tasks in critical Path of the project
		ArrayList<String> criticalPath = p1.criticalPath();
		
		
		//Check if the returned List of task names are in order and 
		//represent the projects critical path (all tasks with a float of 0 are in the critical path)
		assertEquals(criticalPath.get(0), "Task1");
		assertEquals(criticalPath.get(1), "Task2");
		assertEquals(criticalPath.get(2), "Task4");
		assertEquals(criticalPath.get(3), "Task5");
		assertEquals(criticalPath.get(4), "Task6");
		assertEquals(criticalPath.get(5), "Task8");
		
		p1.delProj();
	}
	
	
	

}
