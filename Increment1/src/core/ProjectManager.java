package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProjectManager
{
	
	public ProjectManager()
	{
		
	}
	
	/**
	 * Use this method to add a brand new, Java-created, project.
	 * 
	 * @param new_project
	 *            should be create using Project(String, String) constructor
	 * 
	 * @return Project used in input, with an id generated upon insertion into
	 *         DB. At this point, the Project is guaranteed to be in DB.
	 */
	public Project addProject(Project new_project)
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
			
			new_project.setId(id);
			
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
		return new_project;
	}
	
	public void delAllProjects()
	{
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("DELETE FROM projects");
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in delAllProjects()");
			System.exit(0);
		}
		System.out.println("ALL PROJECTS CLEANED");
	}
	
	public void delProj(Project p1)
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
					+ Integer.toString(p1.getId()) + ";");
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in delProj()");
			System.exit(0);
		}
		System.out.println("Project " + p1.getName()
				+ " has been deleted successfully.");
	}
	
	public ArrayList<Project> getProjects()
	{
		ArrayList<Project> a = new ArrayList<Project>(0);
		
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM projects;");
			while (rs.next())
			{
				a.add(new Project(rs.getString("owner_id"), rs
						.getString("project_name"), rs.getInt("project_id")));
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
	
	public void setProj(int id, Project p1)
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
					+ p1.getName() + "', owner_id = '" + p1.getOwner()
					+ "' WHERE project_id = " + Integer.toString(id) + ";");
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in getProjects()");
			System.exit(0);
		}
		System.out.println("Successfully modified Project with id# ="
				+ Integer.toString(p1.getId()));
	}
	
	/**
	 * Get a project in DB from a project's id. Contract: Project used as param
	 * must include a valid DB id
	 */
	public Project getProject(Project p1)
	{
		Project p = null;
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM projects WHERE project_id = " + Integer.toString(p1.getId()) + ";");
			while (rs.next())
			{
				p = new Project(rs.getString("owner_id"),
						rs.getString("project_name"), rs.getInt("project_id"));
			}
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in getProjects()");
			System.exit(0);
		}
		System.out.println("Project " + Integer.toString(p.getId())
				+ " successfully fetched");
		return p;
	}
	
	public Project getProject(int p_id)
	{
		Project p = null;
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM projects WHERE project_id = " + Integer.toString(p_id) + ";");
			
			while (rs.next())
			{
				p = new Project(rs.getString("owner_id"),
						rs.getString("project_name"), rs.getInt("project_id"));
			}
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in getProjects()");
			System.exit(0);
		}
		System.out.println("Project " + Integer.toString(p.getId())
				+ " successfully fetched");
		return p;
	}
	
	public Project getProject(String p_name)
	{
		Project p = null;
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM projects WHERE project_name = " + p_name + ";");
			
			while (rs.next())
			{
				p = new Project(rs.getString("owner_id"),
						rs.getString("project_name"), rs.getInt("project_id"));
			}
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in getProjects()");
			System.exit(0);
		}
		System.out.println("Project " + Integer.toString(p.getId())
				+ " successfully fetched");
		return p;
	}
	
	public ArrayList<Project> getProjects(String user_id)
	{
		ArrayList<Project> a = new ArrayList<Project>(0);
		
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM projects WHERE owner_id = '"
							+ user_id + "';");
			while (rs.next())
			{
				a.add(new Project(rs.getString("owner_id"), rs
						.getString("project_name"), rs.getInt("project_id")));
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
	
	public void delProj(int id)
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
					+ Integer.toString(id) + ";");
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in delProj()");
			System.exit(0);
		}
		System.out.println("Project with id# =" + Integer.toString(id)
				+ " has been deleted successfully.");
	}

	public void clean()
	{
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			stmt.executeUpdate("DELETE FROM projects");
			stmt.executeUpdate("DELETE FROM tasks");
			
			stmt.close();
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in delAllProjects()");
			System.exit(0);
		}
		System.out.println("ALL PROJECTS CLEANED");
	}
	
}
