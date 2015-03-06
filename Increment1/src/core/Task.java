package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Task
{
	private String name, description, owner_id; 
	private int task_id, duration, project_id, is_done;
	private ArrayList<Integer> pre_reqs;
	
	public Task(int task_id, String name, String description, int duration, int project_id, String owner_id, ArrayList<Integer> pre_reqs)
	{
		this.task_id = task_id;
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.project_id = project_id;
		this.owner_id = owner_id;
		this.pre_reqs = pre_reqs;
		is_done = 0;
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("INSERT INTO tasks VALUES ("+task_id+ ", '"+name+"', '"+description+"', "+duration+", "+project_id+", '"+owner_id+"', "+is_done+");");
	    	if(pre_reqs != null)
	    	for (int i = 0; i < pre_reqs.size(); i++)
	    		stmt.executeUpdate("INSERT INTO precedence VALUES ("+task_id+ ", "+pre_reqs.get(i)+", "+project_id+");");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task created successfully");
	}
	
	/** Used to create a task object without automatically adding it to DB */
	public Task(int task_id, String name, String description, int duration,
			int project_id, String owner_id, ArrayList<Integer> pre_reqs,
			boolean overrider)
	{
		this.task_id = task_id;
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.project_id = project_id;
		this.owner_id = owner_id;
		this.pre_reqs = pre_reqs;
		is_done = 0;
	}
	
	public ArrayList<Integer> getPrereq(){
		return pre_reqs;
	}
	
	public void setDescription(String description){
		this.description = description;
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE tasks SET description = '"+description+"' WHERE task_id = "+task_id+";");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task description updated successfully");
	}
	
	public void addPrereq(int pre_req){
		pre_reqs.add(pre_req);
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("INSERT INTO precedence VALUES ("+task_id+ ", "+pre_req+", "+project_id+");");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task precedence updated successfully");
	}
	
	public void finishedTask(){
		is_done = 1;
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE tasks SET is_done = "+is_done+" WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task set as done successfully");
	}

	public int isDone(){
		return is_done;
	}
	
	public void deleteTask(){
		//delete from database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM precedence WHERE pre_req = " + task_id + "AND project_id = "+project_id+";");
			while (rs.next())
			{
				ResultSet rs_1 = stmt.executeQuery("SELECT * FROM precedence WHERE task = " + task_id + "AND project_id = "+project_id+";");
				while (rs_1.next()){
					//Children tasks inherit precedence from task to delete
					stmt.executeUpdate("INSERT INTO precedence VALUES("+rs.getInt("task_id")+", "+rs_1.getInt("pre_req")+ ", "+ rs.getInt("project_id")+");");
				}
			}
			//delete this task
			stmt.executeUpdate("DELETE FROM tasks WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			
			//delete all users assigned to this task
			stmt.executeUpdate("DELETE FROM user_task WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			
			//delete all precedence from this task
			stmt.executeUpdate("DELETE FROM precedence WHERE task_id = "+ task_id +" AND project_id = "+project_id+";");
			
			//delete all precedence referring to this task
			stmt.executeUpdate("DELETE FROM precedence WHERE pre_req = "+ task_id +" AND project_id = "+project_id+";");
	    	
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task deleted successfully");
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public int getDuration()
	{
		return this.duration;
	}
	
	public int getProjectID()
	{
		return this.project_id;
	}
	
	public String getOwnerID()
	{
		return this.owner_id;
	}
	
	public int getId()
	{
		// TODO Auto-generated method stub
		return task_id;
	}
	
	public boolean equals(Object otherTask)
	{
		Task t = (Task)otherTask;
		if(t.getId() == task_id)
			return true;
		return false;
	}
	
}
