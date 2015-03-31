package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Task
{
	private String name, description, owner_id; 
	private int start_time, end_time, project_id, is_done, task_id, late_start, late_finish, tfloat;
	private ArrayList<Integer> pre_reqs;
	private ArrayList<User> members;
	
	
	public Task(String name, String description, int start_time, int end_time, int project_id, String owner_id)
	{
		pre_reqs = new ArrayList<Integer>();
		this.name = name;
		this.description = description;
		this.start_time = start_time;
		this.end_time = end_time;
		this.project_id = project_id;
		this.owner_id = owner_id;
		is_done = 0;
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	
	    	// Check Database for the maximum task_id
	    	Statement stmt = conn.createStatement();
	    	ResultSet rs = stmt.executeQuery("SELECT MAX(task_id) FROM tasks;");
	    	
	    	//if there is a maximum task_id meaning this is not the first task we are adding,
	    	//we increment the task_id
	    	if(rs.next()){
	    		int id = rs.getInt(1);
	    		id++;
	    		this.task_id = id;
	    	 	stmt.close();
	    	 	
	    	}
	    	else{
	    		//this only happens when there are no tasks in the database
	    		this.task_id = 1;
	    	 	stmt.close(); 
	    	 	
	    	}
	    	

			
	    	stmt.executeUpdate("INSERT INTO tasks VALUES ("+task_id+ ", '"+name+"', '"+description+"', "+start_time+", "+end_time+", "+project_id+", '"+owner_id+"', "+is_done+");");
	    	stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task created successfully");
	}
	
	/** Used to create a task object without automatically adding it to DB */
	public Task(int task_idOLD, String name, String description, int start_time, int end_time,
			int project_id, String owner_id,
			int is_done)
	{
		task_idOLD = task_id;
		task_id++;
		this.name = name;
		this.description = description;
		this.start_time = start_time;
		this.end_time = end_time;
		this.project_id = project_id;
		this.owner_id = owner_id;
		this.is_done = is_done;
		
		fetchAllPreReqs();
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
	    	stmt.executeUpdate("UPDATE tasks SET description = '"+description+"' WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task description updated successfully");
	}
	
	public void setName(String name){
		this.name = name;
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE tasks SET task_name = '"+name+"' WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task name updated successfully");
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
	
	public void fetchAllPreReqs(){
		pre_reqs = new ArrayList<Integer>();
		Connection conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM precedence WHERE task_id = "
							+ Integer.toString(task_id) + " AND project_id = "+project_id+";");
			
			while (rs.next())
			{
				pre_reqs.add(rs.getInt("pre_req"));
			}
			
			pre_reqs.trimToSize();
			
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in getProjects()");
			System.exit(0);
		}
		System.out.println("All pre_reqs for task "+name+" successfully fetched");
	}
	
	public void finishedTask(){
		boolean can_finish = true;
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM tasks WHERE task_id = (SELECT precedence.pre_req FROM precedence WHERE task_id = "+task_id+" AND project_id = "+project_id+") AND project_id = "+project_id+";");
			
			while (rs.next())
			{
				can_finish = can_finish && (rs.getInt("is_done") == 1);
			}
	    	
	    	if (can_finish){
	    		is_done = 1;
	    		stmt.executeUpdate("UPDATE tasks SET is_done = "+is_done+" WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
	    		System.out.println("Task set as done successfully");
	    	}else 
	    		System.out.println("Task cannot be set as done, all pre-requisites should be done before!");
	    	stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
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
	    	
	    	ResultSet rs = stmt.executeQuery("SELECT * FROM precedence WHERE pre_req = " + task_id + " AND project_id = "+project_id+";");
			while (rs.next())
			{
				ResultSet rs_1 = stmt.executeQuery("SELECT * FROM precedence WHERE task_id = " + task_id + " AND project_id = "+project_id+";");
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
		return this.name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public int getStartTime()
	{
		return this.start_time;
	}
	
	public int getEndTime(){
		return this.end_time;
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
		return task_id;
	}
	
	public int getLateStart()
	{
		return late_start;
	}
	
	public int getLateFinish()
	{
		return late_finish;
	}
	
	public int getEarlyStart() {
		return getStartTime();
	}
	
	public int getEarlyFinish() {
		return getEndTime();
	}
	
	public int getFloat() {
		return tfloat;
	}
	
	public boolean equals(Object otherTask)
	{
		Task t = (Task)otherTask;
		if(t.getId() == task_id)
			return true;
		return false;
	}

	public String toString(){
		String task = new String("ID:" + task_id + "   Task Name: " + name + "  Description: " +
				"  StartTime :" + start_time + "  End Time: " + end_time + "  Owner: " + owner_id);
		return task;
	}
	
	public void setLateStart(int time) {
		this.late_start = time;
	}
	
	public void setLateFinish(int time) {
		this.late_finish = time;
	}
	
	public void setFloat(int time) {
		this.tfloat = time;
	}

	public void setEndTime(int time) {	
		this.end_time = time;
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE tasks SET end_time = '"+time+"' WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task end time updated successfully");
		
	}

	public void setStartTime(int time) {
		this.end_time = time;
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE tasks SET start_time = '"+time+"' WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task start time updated successfully");		
	}

	public void setMember(User user) {
		this.members.add(user);
		
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE user_task SET user_id = '"+user.getUserID()+"' WHERE task_id = "+task_id+" AND project_id = "+project_id+";");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Task end time updated successfully");
	}
	
	public void setPreReqs(Task task){
		this.pre_reqs.add(task.getId());

	}

}
