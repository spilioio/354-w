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
	private double totalCost;
	private GanttChart ganttChart;
	private ArrayList<Task> allTasks;
	private ArrayList<Task> theCriticalPath = new ArrayList<Task>();
	
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

	public Double PERTAnalysis() {
		// TODO Auto-generated method stub
		
		/*
		 * I think we should set the pessimistic and optimistic estimates for the tasks
		 * to +/- 15% of their duration because there are no other guidelines on how to calculate them
		 */
		//pessimistic is late_finish
		//optimistic is early_finish
		double pertAnalysis = 0;
		this.organize();
		
		for ( Task t : allTasks )
			pertAnalysis += t.getPERTEstimate();
		
		return pertAnalysis;
		
	}

	public ArrayList<Task> organize() {	
		/*
		 * this should do a forward pass and a backward pass and set appropriate 
		 * values: EarlyStart, EarlyFinish, LateStart, LateFinish
		 * 
		 */
		
		//Forward Pass
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
				
				finish = earlyStart + t.getEndTime() - t.getStartTime();
				
				if ( t.getStartTime() == 0 )
					finish--;
				
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
		
		//set late finish and late start of the latest task
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
		
		setPertValues();
		
		return allTasks;
	}
	
	private void setPertValues() 
	{
		for ( Task t : allTasks )
		{
			t.setOptimisticEstimate(t.getDuration()*1.15);
			t.setPessimisticEstimate(t.getDuration()*0.85);
			t.setLikelyEstimate((t.getOptimisticEstimate() + t.getPessimisticEstimate())/2);
			t.setPERTEstimate();
			t.setPERTVariance();
		}
	}
	
	private void backwardPass(Task cTask)
	{
		Task task = this.getTask(cTask);
		if ( cTask.getStartTime() != 0 )
		{
			ArrayList<Integer> prereqs = cTask.getPrereq();
			
			int duration = cTask.getEndTime() - cTask.getStartTime();
			
			if ( cTask.getStartTime() != 0 )
				duration++;
			
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
		
		for ( String s : criticalPath )
		{
			for ( Task t : allTasks )
			{
				if ( s == t.getName() )
					theCriticalPath.add(t);
			}
		}
		
		return criticalPath;
	}
	

	public double EarnedValueAnalysis(int day) {
		
		/*
		 * This should calculate the earned value of the project using a new project variable called currentDay
		 * if a task a is started its value should be 50% of its completed cost.  So for a task with 100.00$ of total value
		 * the task is worth 0$ if the current day is before the tasks start day.  50$ if the current day is anywhere in in between
		 * the tasks start and end days. and 100$ if the current day is after the tasks end day
		 */
		ArrayList<Task> projTasks = this.organize();
		double tCost = 0;
		double earnedValue = 0;
		double thisCost;
		for(Task t : projTasks){
			thisCost = t.getCost();
			//if the day passed is later than the projects early finish date
			//meaning the project is complete we add the total cost of the Task to earned Value
			if(day > t.getEarlyFinish()){
				earnedValue += thisCost;
			}
			//if the task is started but not complete use 50% of its value
			if(day > t.getEarlyStart()){
				earnedValue += 0.50 * thisCost;
			}
			tCost += thisCost;
		}
		this.totalCost = tCost;
		return earnedValue;
	}

	public void addTask(Task task) {
		this.organize();
		for ( Task t : allTasks )
		{
			if ( t.getId() == task.getId() )
			{
				t.setOptimisticEstimate(task.getOptimisticEstimate());
				t.setLikelyEstimate(task.getLikelyEstimate());
				t.setPessimisticEstimate(task.getPessimisticEstimate());
			}
		}
	}

	public ArrayList<Task> getCriticalPath() {
		this.criticalPath();
		return theCriticalPath;
	}

	public double PERTVariance() {
		double variance = 0;
		
		for ( Task t : theCriticalPath )
			variance += t.getPERTVariance();
		
		return variance;
	}

	public double PERTStandardDeviation() {
		return Math.sqrt(PERTVariance());
	}
	public void setTotalCost(double tC){
		totalCost = tC;
	}
	public double getTotalCost(){
		return this.totalCost;
	}

	
	

	
}
