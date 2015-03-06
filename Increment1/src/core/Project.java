package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Laurence Werner 6063640
 * */
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
			
			System.out.println("AAAAAAAAAAAAAAA " + new_project.getName() + " " + new_project.getOwner());
			
			int id = 0;
			while (rs.next())
				id++;
			
			new_project.setId(id);
			
			project_id = id;
			project_name = new_project.getName();
			owner_id = new_project.getOwner();
			
			stmt.executeUpdate("INSERT INTO projects (project_id, project_name, owner_id) VALUES ("
					+ new_project.getId()
					+ ", '"
					+ new_project.getName()
					+ "', '" + new_project.getOwner() + "');");
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Project created successfully");
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
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in delProj()");
			System.exit(0);
		}
		System.out.println("Project " + getName()
				+ " has been deleted successfully.");
	}
	
	public void setProjectToId(int id)
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
					+ getName() + "', owner_id = '" + getOwner()
					+ "' WHERE project_id = " + Integer.toString(id) + ";");
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in setProjectToId(int id)");
			System.exit(0);
		}
		System.out.println("Successfully modified Project with id# ="
				+ Integer.toString(getId()));
	}
	
}
