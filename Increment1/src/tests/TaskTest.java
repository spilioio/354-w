package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import core.Task;

public class TaskTest {

	private Task t1, t2;

	@Test
	public void testInit() {
		// project id, task id, user id, earliest start, duration, label
		t1 = new Task(3, 1, 2, 0, 1, "t1");
		t2 = new Task(3, 2, 2, 0, 4, "t2");
	}
	
	@Test
	public void testgetProjectId() {
		assertEquals(t1.getProjectId(), "3");
		assertEquals(t2.getProjectId(), "3");
	}
	
	@Test
	public void testgetTaskId() {
		assertEquals(t1.getTaskId(), "1");
		assertEquals(t2.getTaskId(), "2");
	}
	
	@Test
	public void testgetUserId() {
		assertEquals(t1.getUserId(), "1");
		assertEquals(t2.getUserId(), "2");
	}
	
	@Test
	public void testgetEarliestStart() {
		assertEquals(t1.getEarliestStart(), "0");
		assertEquals(t2.getEarliestStart(), "0");
	}
	
	@Test
	public void testgetDuration() {
		assertEquals(t1.getDuration(), "1");
		assertEquals(t2.getDuration(), "4");
	}
	
	@Test
	public void testgetName() {
		assertEquals(t1.getName(), "t1");
		assertEquals(t2.getName(), "t2");
	}
}
