package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import GANTTChart.*;

/**
 * @author Laurence Werner 6063640
 * */
public class Project
{
	private String owner_id, project_name;
	private int project_id;
	private GanttChart ganttChart;
	private ArrayList<Task> allTasks;
	
	public Project(String owner_id, String project_name)
	{
		/*
		 * this.owner_id = owner_id; this.project_name = project_name;
		 * System.out.println("PROJECT CREATED IN JAVA");
		 */
		
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(project_id) FROM projects;");
			
			//if there is a maximum task_id meaning this is not the first task we are adding,
	    	//we increment the task_id
	    	if(rs.next()){
	    		int id = rs.getInt(1);
	    		id++;
	    		this.project_id = id;
	    	 	stmt.close();
	    	 	
	    	}
	    	else{
	    		//this only happens when there are no Projects in the database
	    		this.project_id = 1;
	    	 	stmt.close(); 
	    	 	
	    	}
			
			this.project_name = project_name;
			this.owner_id = owner_id;
			this.ganttChart = new GanttChart();
			
			stmt.executeUpdate("INSERT INTO projects (project_id, project_name, owner_id) VALUES ("
					+ project_id
					+ ", '"
					+ this.project_name
					+ "', '"
					+ this.owner_id + "');");
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Project created successfully");
	}
	
	/** Use this to instantiate a project from the DB */
	public Project(String owner_id, String project_name, int project_id)
	{
		this.owner_id = owner_id;
		this.project_name = project_name;
		this.project_id = project_id;
	}
	
	/**
	 * Use this method to add a brand new, Java-created, project.
	 * 
	 * @param new_project
	 *            should be create using Project(String, String) constructor
	 */
	public Project(Project new_project)
	{
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM projects;");
			
			int id = 0;
			while (rs.next())
				id++;
			
			project_id = id;
			project_name = new_project.getName();
			owner_id = new_project.getOwner();
			
			stmt.executeUpdate("INSERT INTO projects (project_id, project_name, owner_id) VALUES ("
					+ project_id
					+ ", '"
					+ project_name
					+ "', '" + owner_id + "');");
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Project created successfully");
		
	}
	
	/*public void setId(int id)
	{
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("UPDATE projects SET project_id = '"
					+ id + "' WHERE project_id = " + Integer.toString(project_id) + ";");
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Successfully modified Project with id# ="
				+ Integer.toString(getId()));
		
		this.project_id = id;
	}*/
	
	public int getId()
	{
		return project_id;
	}
	
	public String getName()
	{
		return project_name;
	}
	
	public String getOwner()
	{
		return owner_id;
	}
	
	public void setName(String new_name)
	{
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("UPDATE projects SET project_name = '"
					+ new_name + "' WHERE project_id = " + Integer.toString(project_id)
					+ ";");
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage() + "B");
			System.exit(0);
		}
		System.out.println("Successfully modified Project with id# ="
				+ Integer.toString(getId()));
		
		project_name = new_name;
	}
	
	public void setOwner(String new_owner)
	{
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("UPDATE projects SET owner_id = '" + new_owner
					+ "' WHERE project_id = " + Integer.toString(project_id)
					+ ";");
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage() + "A");
			System.exit(0);
		}
		System.out.println("Successfully modified Project with id# ="
				+ Integer.toString(getId()));
		
		owner_id = new_owner;
	}
	
	public boolean equals(Object otherProject)
	{
		Project p = (Project) otherProject;
		if (p.getId() == project_id)
			return true;
		return false;
	}
	
	public ArrayList<Task> getTasks()
	{
		ArrayList<Task> a = new ArrayList<Task>();
		
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM tasks WHERE project_id = "
							+ Integer.toString(project_id) + ";");
			
			while (rs.next())
			{
				//System.out.println("task_id:"+rs.getInt("task_id")+" "+rs.getString("task_name"));
				a.add(new Task(rs.getInt("task_id"), rs.getString("task_name"),
						rs.getString("description"), rs.getInt("start_time"), rs.getInt("end_time"), rs
								.getInt("project_id"), rs.getString("user_id"), rs.getInt("is_done")));
			}
			
			a.trimToSize();
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in getTasks()");
			System.exit(0);
		}
		System.out.println("All tasks successfully fetched");
		
		this.allTasks = a;
		return a;
	}
	
	public void delProj()
	{
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("DELETE FROM projects WHERE project_id = "
					+ Integer.toString(getId()) + ";");
			
			stmt.executeUpdate("DELETE FROM tasks WHERE project_id = "
					+ Integer.toString(getId()) + ";");
			
			stmt.executeUpdate("DELETE FROM precedence WHERE project_id = "
					+ Integer.toString(getId()) + ";");
			
			stmt.executeUpdate("DELETE FROM user_task WHERE project_id = "
					+ Integer.toString(getId()) + ";");
			
			stmt.executeUpdate("DELETE FROM project_property WHERE project_id = "
					+ Integer.toString(getId()) + ";");
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in delProj()");
			System.exit(0);
		}
		System.out.println("Project " + getName()
				+ " has been deleted successfully.");
	}
	
	public Task[] GANTTAnalysis(){
		return this.ganttChart.GANTTAnalysis(this);
	}

	
	public String toString(){
		String project = new String("- ID# " + project_id + " - Name: " + project_name + " - Owner: " + owner_id);
		return project;					
	}

	public void PERTAnalysis() {
		// TODO Auto-generated method stub
		
		/*
		 * I think we should set the pessimistic and optimistic estimates for the tasks
		 * to +/- 15% of their duration because there are no other guidelines on how to calculate them
		 */
		
	}

	public void organize() {
		// TODO Auto-generated method stub
		
		/*
		 * this should do a forward pass and a backward pass and set appropriate 
		 * values: EarlyStart, EarlyFinish, LateStart, LateFinish
		 * 
		 */
		
		ArrayList<Task> taskList = this.getTasks(); 
		Task lastTask = taskList.get(0);
		int latestFinish = 0;
		int latestStart = 0;
		
		
		for ( Task t : taskList )
		{
			ArrayList<Integer> prereqs = t.getPrereq();
						
			int earlyStart = -1;
			int earlyFinish = -1;
			int start = 0;
			int finish = 0;
			
			for ( int i : prereqs )
			{
				start = this.getTask(i).getEndTime() + 1;
				if ( start > earlyStart )
					earlyStart = start;
				
				finish = earlyStart + t.getEndTime() - t.getStartTime() - 1;
				
				if ( finish > earlyFinish )
					earlyFinish = finish;
			}
			
			if ( prereqs.size() != 0 )
			{
				t.setStartTime(earlyStart);
				t.setEndTime(earlyFinish);
			}
			
			
			if ( earlyFinish > latestFinish )
			{
				latestFinish = earlyFinish;
				latestStart = earlyStart;
				lastTask = t;
			}
		}
		

		for ( Task t : allTasks )
		{
			if ( t.getId() == lastTask.getId() )
			{
				t.setLateFinish(latestFinish);
				t.setLateStart(latestStart);
				break;
			}
		}
		
		Task cTask = lastTask;
		backwardPass(cTask);
	}
	
	private Task backwardPass(Task cTask)
	{
		Task task = this.getTask(cTask);
		if ( cTask.getStartTime() != 0 )
		{
			ArrayList<Integer> prereqs = cTask.getPrereq();
			
			int duration = cTask.getEndTime() - cTask.getStartTime() + 1;
			int lateStart = cTask.getLateFinish() - duration;
			int tfloat = cTask.getLateFinish() - cTask.getEarlyFinish();
			int lateFinish = cTask.getLateFinish();
			
			for ( int i : prereqs )
			{
				Task pTask = this.getTask(i);
				
				if ( pTask.getLateFinish() < 0 )
					pTask.setLateFinish(lateStart);
			}
						
			
			task.setDuration(duration);
			task.setLateFinish(lateFinish);
			task.setLateStart(lateStart);
			task.setFloat(tfloat);
			
			for ( int i : prereqs )
				backwardPass(this.getTask(i));
		}
		
		task.setDuration(task.getEndTime() - task.getStartTime());
		task.setLateStart(task.getLateFinish() - task.getDuration());
		task.setFloat(task.getLateFinish() - task.getEarlyFinish());
		
		return null;
	}
	
	public Task getTask(int taskId) {
		for ( Task t : allTasks )
		{
			if ( t.getId() == taskId )
				return t;
		}
		return null;
	}
	
	public Task getTask(Task task) {
		for ( Task t : allTasks )
		{
			if ( t.getId() == task.getId() )
				return t;
		}
		return null;
	}

	public ArrayList<String> criticalPath() {
		// TODO Auto-generated method stub
		
		/*
		 * This should find all the tasks in the project that have a float of 0
		 * and return an Arraylist<String> containing the Task names
		 */
		this.organize();
		Map<String, Integer> PATH = new HashMap<String, Integer>();
		ArrayList<String> criticalPath = new ArrayList<String>();
		
		Comparator<Map.Entry<String, Integer>> byMapValues = new Comparator<Map.Entry<String, Integer>>() {
	        @Override
	        public int compare(Map.Entry<String, Integer> left, Map.Entry<String, Integer> right) {
	            return left.getValue().compareTo(right.getValue());
	        }
	    };
		
	    List<Map.Entry<String, Integer>> path = new ArrayList<Map.Entry<String, Integer>>();
	    
		for ( Task t : allTasks )
		{
			if ( t.getFloat() == 0 )
				PATH.put(t.getName(), t.getLateFinish());
		}
		
		path.addAll(PATH.entrySet());
		
		Collections.sort(path, byMapValues);
		
		for ( Entry<String, Integer> entry : path ) {
			criticalPath.add(entry.getKey());
		}
		
		return criticalPath;
	}
	

	public double EarnedValueAnalysis() {
		// TODO Auto-generated method stub
		
		/*
		 * This should calculate the earned value of the project using a new project variable called currentDay
		 * if a task a is started its value should be 50% of its completed cost.  So for a task with 100.00$ of total value
		 * the task is worth 0$ if the current day is before the tasks start day.  50$ if the current day is anywhere in in between
		 * the tasks start and end days. and 100$ if the current day is after the tasks end day
		 */
		return 0;
	}
	
	

	
}
