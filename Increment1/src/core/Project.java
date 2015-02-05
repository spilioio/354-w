package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Project
{
	private String owner_id, project_name;
	private int project_id;
	
	public Project(String owner_id, String project_name)
	{
		this.owner_id = owner_id;
		this.project_name = project_name;
		System.out.println("PROJECT CREATED IN JAVA");
	}
	
	public Project(String owner_id, String project_name, int project_id)
	{
		this.owner_id = owner_id;
		this.project_name = project_name;
		this.project_id = project_id;
	}
	
	public void setId(int new_id)
	{
		project_id = new_id;
	}
	
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
		project_name = new_name;
	}
	
	public void setOwner(String new_owner)
	{
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
		ArrayList<Task> a = new ArrayList<Task>(0);
		
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
						rs.getString("description"), rs.getInt("duration"), 
						rs.getInt("project_id"), rs.getString("user_id"), null, false));
			}
			
			a.trimToSize();
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in getProjects()");
			System.exit(0);
		}
		System.out.println("All projects successfully fetched");
		return a;
	}
	
}
