package tests;
import core.*;

import org.junit.Test;

import static org.junit.Assert.*;


public class TestTaskPERT {
	
	String database = "jdbc:sqlite:COMP354";
	Task task;
	// (ShortestTime + 4(LikelyTime) + LongestTime) / 6
	
	public void setupTestEvironment(){
		task = new Task("test_task", "taskTester", 10, 15, 1, "b_jenkins");
			
		task.setOptimisticEstimate(5*1.15);
		task.setPessimisticEstimate(5*0.85);
		task.setLikelyEstimate((task.getOptimisticEstimate() + task.getPessimisticEstimate())/2);
	}
	
	public void teardownTestEnvironment(){
		task.deleteTask();
	}
	
	@Test
	public void testGetPERTEstimate(){
		// getPERTEstimate must return a double
		
		this.setupTestEvironment();
		
		double expected = (task.getOptimisticEstimate() + 4 * task.getLikelyEstimate() + task.getPessimisticEstimate()) / 6;
		
		assertTrue(expected == task.getPERTEstimate());
		
		this.teardownTestEnvironment();
	}
	
	@Test
	public void testGetPERTVariance(){
		// getPERTVariance must return a double

		this.setupTestEvironment();
		
		double expected = Math.pow(task.getPessimisticEstimate() - task.getOptimisticEstimate(), 2) / 36;

		assertTrue(expected == task.getPERTVariance());
		
		this.teardownTestEnvironment();
	}
	
	@Test
	public void testGetOptimisitcEstimate(){
		
		this.setupTestEvironment();
		//System.out.println(task.getOptimisticEstimate());
		assertTrue(5*1.15 == task.getOptimisticEstimate());
		this.teardownTestEnvironment();
		
	}
	
	@Test
	public void testGetLikelyEstimate(){
		this.setupTestEvironment();
		//System.out.println(task.getLikelyEstimate());
		assertTrue((task.getOptimisticEstimate() + task.getPessimisticEstimate())/2 == task.getLikelyEstimate());
		this.teardownTestEnvironment();
	}
	
	@Test
	public void testGetPessimistic(){
		this.setupTestEvironment();
		//System.out.println(task.getPessimisticEstimate());
		assertTrue(5*0.85 == task.getPessimisticEstimate());
		this.teardownTestEnvironment();
	}

}
