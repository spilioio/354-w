package testPERTAnalysis;

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
		project = new Project("b_jenkins", "testProject", 999);
		task = new Task("test_task", "taskTester", 10, 15, 1, "b_jenkins");
		
		task.setOptimisticEstimate(5);
		task.setLikelyEstimate(6);
		task.setPessimisticEstimate(7);
		
		task2 = new Task("test_task", "taskTester", 15, 20, 1, "b_jenkins");
					
		task2.setOptimisticEstimate(5);
		task2.setLikelyEstimate(6);
		task2.setPessimisticEstimate(7);
		
		task2 = new Task("test_task", "taskTester", 14, 19, 1, "b_jenkins");
						
		task3.setOptimisticEstimate(5);
		task3.setLikelyEstimate(6);
		task3.setPessimisticEstimate(7);
		
		project.addTask(task);
		project.addTask(task2);
		project.addTask(task3);
	}
	
	public void teardownTestEnvironment(){
		task.deleteTask();
		task2.deleteTask();
		task3.deleteTask();
		project.delProj();
	}
	
	public void testPERTAnalysis(){
		// PERTAnalysis should return the sum of the project's task PERT Estimates
		this.setupTestEvironment();
		double expected;
		for(int i = 0; i < project.getTasks().size(); i++){
			expected += project.getTasks().get(i).getPERTEstimate();
		}
		assertEquals(expected, project.PERTAnalysis());
		this.teardownTestEnvironment();
	}
	
	public void testPERTVariance(){
		// PERTVariance should return the sum of the variances of the project's critical path tasks
		this.setupTestEvironment();
		double expected;
		for(int i = 0; i < project.getCriticalPath().size(); i++){
			expected += project.getCriticalPath().get(i).getPERTVariance();
		}
		assertEquals(expected, project.PERTVariance());
		this.teardownTestEnvironment();
	}
	
	public void testPERTStandardDeviation(){
		// PERTStandardDeviation should return the square root of the project's variance (calculated by PERTVariance)
		this.setupTestEvironment();
		double expected = Math.sqrt(project.PERTVariance());
		assertEquals(expected, project.PERTStandardDeviation());
		this.teardownTestEnvironment();
	}
	
}
