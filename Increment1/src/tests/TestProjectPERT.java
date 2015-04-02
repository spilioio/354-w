package tests;

import java.util.ArrayList;

import junit.*;
import core.*;

import org.junit.*;

import static org.junit.Assert.*;
public class TestProjectPERT {

	String database = "jdbc:sqlite:COMP354";
	Project project;
	Task task;
	Task task2;
	Task task3;
	// (ShortestTime + 4(LikelyTime) + LongestTime) / 6
	
	public void setupTestEvironment(){
		ProjectManager pm = new ProjectManager();
		
		pm.delAllProjects();
		
		project = new Project("b_jenkins", "testProject", 999);
		task = new Task("test_task", "taskTester", 10, 15, 999, "b_jenkins");
		
		task.setOptimisticEstimate(5);
		task.setLikelyEstimate(6);
		task.setPessimisticEstimate(7);
		
		task2 = new Task("test_task", "taskTester", 15, 20, 999, "b_jenkins");
					
		task2.setOptimisticEstimate(5);
		task2.setLikelyEstimate(6);
		task2.setPessimisticEstimate(7);
		
		task2 = new Task("test_task", "taskTester", 14, 19, 999, "b_jenkins");
						
//		task3.setOptimisticEstimate(5);
//		task3.setLikelyEstimate(6);
//		task3.setPessimisticEstimate(7);
		task2.addPrereq(task.getId());
		project.addTask(task);
		project.addTask(task2);
//		project.addTask(task3);
	}
	
	public void teardownTestEnvironment(){
		task.deleteTask();
		task2.deleteTask();
//		task3.deleteTask();
		project.delProj();
	}
	
	@Test
	public void testPERTAnalysis(){
		// PERTAnalysis should return the sum of the project's task PERT Estimates
		this.setupTestEvironment();
		double expected = 0, pertAnalysis;
		
		ArrayList<Task> tasks = project.organize();
		
		for(Task t : tasks){
			expected += t.getPERTEstimate();
		}
		pertAnalysis = project.PERTAnalysis();
		System.out.println(expected+" "+pertAnalysis);
		assertTrue(expected == pertAnalysis);

		this.teardownTestEnvironment();
	}
	
	@Test
	public void testPERTVariance(){
		// PERTVariance should return the sum of the variances of the project's critical path tasks
		this.setupTestEvironment();
		double expected = 0, pertVariance;
		ArrayList<Task> criticalPath = project.getCriticalPath();
		
		
		for(Task t : criticalPath){
			expected += t.getPERTVariance();
		}
		
		pertVariance = project.PERTVariance();
		assertTrue(expected == pertVariance);
		this.teardownTestEnvironment();
	}
	
	@Test
	public void testPERTStandardDeviation(){
		// PERTStandardDeviation should return the square root of the project's variance (calculated by PERTVariance)
		this.setupTestEvironment();
		double expected = Math.sqrt(project.PERTVariance()), pertSd;
		pertSd = project.PERTStandardDeviation();
		assertTrue(expected == pertSd);
		this.teardownTestEnvironment();
	}
	
}
