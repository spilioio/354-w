package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import GANTTChart.*;

/**
 * @author Laurence Werner 6063640
 * */
public class Project
{
	private String owner_id, project_name;
	private int project_id;
	private GanttChart ganttChart;
	
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
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM projects;");
			
			int id = 0;
			while (rs.next())
				id++;
			
			project_id = id;
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
	
	public String toString(){
		String project = new String("- ID# " + project_id + " - Name: " + project_name + " - Owner: " + owner_id);
		return project;					
	}
	
}
