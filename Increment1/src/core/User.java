package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;

import GANTTChart.GanttChart;

import java.awt.Color;
import java.awt.GridLayout;

public class User {
	private String userName, userPwd, fname, lname, userID;
	private ArrayList<Task> assignedTasks;
	
	public User(String userName, String userPwd, String fname, String lname){
		this.userName = userName;
		this.userPwd = userPwd;
		this.fname = fname;
		this.lname = lname;
		
		fname = fname.toLowerCase();
		this.userID = fname.substring(0, 1) + "_" + lname;
		
		this.assignedTasks = new ArrayList<Task>();
		
		//Add the user to the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("INSERT INTO users VALUES ('"+userName+ "', '"+userPwd+"', '"+fname+"', '"+lname+"');");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record created successfully");
	}
	

	public User(String userName, String userPwd, String fname, String lname, boolean overrider){
		this.userName = userName;
		this.userPwd = userPwd;
		this.fname = fname;
		this.lname = lname;
	}
	
	public void deleteUser(){
		//delete from database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("DELETE FROM users WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record deleted successfully");
	}
	
	public String getName(){
		return userName;
	}
	
	public String getFirstName(){
		return fname;
	}
	
	public String getLastName(){
		return lname;
	}
	
	public void modifyLastName(String lname){
		this.lname = lname;
		//modify the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE users SET l_name = '"+lname+"' WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record last name updated successfully");
	}
	
	public void modifyFirstName(String fname){
		this.fname = fname;
		//modify the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE users SET f_name = '"+fname+"' WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record first name updated successfully");
	}
	
	public void modifyPwd(String userPwd){
		this.userPwd = userPwd;
		//modify the database
		Connection conn = null;
	    try {
	    	// connect to db (file test.db must lay in the project dir)
	    	// NOTE: it will be created if not exists
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	stmt.executeUpdate("UPDATE users SET user_pwd = '"+userPwd+"' WHERE user_id = '"+userName+"';");
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record password updated successfully");
	}


	public String getUserID() {
		return this.userID;
	}
	
	public ArrayList<Integer> getAssignedTasksDB(){
		Connection conn = null;
    	ArrayList<Integer> taskResults = new ArrayList<Integer>();
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
	    	Statement stmt = conn.createStatement();
	    	ResultSet results = stmt.executeQuery("SELECT task_id FROM user_task WHERE user_id = '" + this.userID + "';");
	    	
	    	int i = 1;
	    	while(results.next()){
	    		taskResults.add(results.getInt(i));
	    	}
	    	
			stmt.close();
			conn.close();
	    }catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	     }
	     System.out.println("Record created successfully");
	     return taskResults;
	}
	
	public ArrayList<Task> getAssignedTasks(){
		return this.assignedTasks;
	}
	
	public void assignTask(Task task){
		
	}
	
	public void displayUser(){
		JFrame mainFrame = new JFrame("Tasks belonging to" + this.userID);
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(Color.GRAY);
		JPanel footerPanel = new JPanel();
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setVisible(true);
		headerPanel.setVisible(true);
		footerPanel.setVisible(true);
		
		GridLayout mainLayout = new GridLayout(2,1);
		GridLayout headerLayout = new GridLayout(1, 3);
		GridLayout footerLayout = new GridLayout(this.getAssignedTasks().size()+1, 2);
		
		mainFrame.setLayout(mainLayout);
		headerPanel.setLayout(headerLayout);
		footerPanel.setLayout(footerLayout);
		
		JLabel taskIDLabel = new JLabel("Task ID");
		JLabel taskNameLabel = new JLabel("Name");
		
		footerPanel.add(taskIDLabel);
		footerPanel.add(taskNameLabel);
		
		for(int i = 0; i < this.getAssignedTasks().size(); i++){
			Task task = this.assignedTasks.get(i);
			JLabel idLabel = new JLabel(String.valueOf(task.getId()));
			JLabel nameLabel = new JLabel(task.getName());
			if(i%2==0){
				idLabel.setBackground(Color.WHITE);
				nameLabel.setBackground(Color.WHITE);
			} else {
				idLabel.setBackground(Color.DARK_GRAY);
				nameLabel.setBackground(Color.DARK_GRAY);
			}

			footerPanel.add(idLabel);
			footerPanel.add(nameLabel);
		}
		
		JLabel userIDLabel = new JLabel(this.userID);
		JLabel fnameLabel = new JLabel(this.fname);
		JLabel lnameLabel = new JLabel(this.lname);
				
		headerPanel.add(userIDLabel);
		headerPanel.add(fnameLabel);
		headerPanel.add(lnameLabel);
		
		mainFrame.add(headerPanel);
		mainFrame.add(footerPanel);
		
		mainFrame.pack();
		
	}
	
	public static void main(String[] args)
	{
		Task task = new Task(100, "CAT", "CATSSSS", 1, 5, 1, "f_lname");
		Task task2 = new Task(100, "BBB", "CATSSSS", 1, 5, 1, "f_lname");

		User user = new User("userName", "userPwd", "fname", "lname");
		user.assignedTasks.add(task);
		user.assignedTasks.add(task2);
		user.displayUser();
		user.deleteUser();
		task.deleteTask();
		task2.deleteTask();
	}
}