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
			
		task.setOptimisticEstimate(5);
		task.setLikelyEstimate(6);
		task.setPessimisticEstimate(7);
	}
	
	public void teardownTestEnvironment(){
		task.deleteTask();
	}
	
	@Test
	public void testGetPERTEstimate(){
		// getPERTEstimate must return a double
		
		this.setupTestEvironment();
		
		double expected = ((5+4*6+7) / 6);
		
		assertTrue(expected == task.getPERTEstimate());
		
		this.teardownTestEnvironment();
	}
	
	@Test
	public void testGetPERTVariance(){
		// getPERTVariance must return a double

		this.setupTestEvironment();
		
		double expected = (7 - 5)^2 / 36;

		assertTrue(expected == task.getPERTVariance());
		
		this.teardownTestEnvironment();
	}
	
	@Test
	public void testGetOptimisitcEstimate(){
		
		this.setupTestEvironment();
		assertEquals(5, task.getOptimisticEstimate());
		this.teardownTestEnvironment();
		
	}
	
	@Test
	public void testGetLikelyEstimate(){
		this.setupTestEvironment();
		assertEquals(6, task.getLikelyEstimate());
		this.teardownTestEnvironment();
	}
	
	@Test
	public void testGetPessimistic(){
		this.setupTestEvironment();
		assertEquals(7, task.getPessimisticEstimate());
		this.teardownTestEnvironment();
	}

}
