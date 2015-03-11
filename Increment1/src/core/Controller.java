package core;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import GUI.BrowseProjectsWindow;
import GUI.LoginVerificationWindow;
import GUI.StartUpWindow;
import GUI.UserCreationWindow;

public class Controller {
	
	private Connection conn;
	private Scanner in; 
	private ProjectManager manager;
	private User userLoggedIn;
	private Project currentProject;
	private Task currentTask;
	
	public Controller(){
		in = new Scanner(System.in);
	}
	
	public void start() {
		// Local variables
		manager = new ProjectManager();
		
		// Connection to DB
		conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in init()");
			System.exit(0);
		}
	}
	
	public void startUpFlow() {
		int selection = 0;
		StartUpWindow startUp = new StartUpWindow();
		startUp.setVisible(true);
		
		// Screen message
		System.out.println("You need to login to continue...");
		System.out.println("");
		System.out.println("Please make your selection by inputing the");
		System.out.println("# of the task you want to perform followed");
		System.out.println("by the return key.");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("1- Login     2- Create account     3- Exit");
		System.out.println("");
		System.out.print("Selection: ");
		
		// User input
		selection = in.nextInt();
		in.nextLine();
		System.out.println("");
		System.out.println("");
		
		
		
		// Handle input cases
		switch (selection)
		{
			case 1:
				userLogInFlow();
				break;
			case 2:
				createNewUserFlow();
				
				break;
			case 3:
				// exit
				end();
				break;
			default:
				// invalid input, restart flow
				System.out.println("Invalid input, please try again.");
				startUpFlow();
				return;
		}
	}
	
	public void managementFlow()
	{
		int selection = 0;
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("      Project Management Interface");
		System.out.println("Please make your selection by inputing the");
		System.out.println("# of the task you want to perform followed");
		System.out.println("by the return key.");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("1- Browse projects  2- New project  3- Logout");
		System.out.println("");
		System.out.print("Selection: ");
		selection = in.nextInt();
		in.nextLine();
		switch (selection)
		{
			case 1:
				userLogInFlow();
				// TODO browse projects
				browseProjectsFlow();
				break;
			case 2:
				// New account creation
				String temp;
				
				System.out.println("Please enter a project name: ");
				temp = in.nextLine();
				System.out.println("");
				
				System.out.println("temp = " + temp);
				// Create new project object.
				currentProject = new Project(userLoggedIn.getName(), temp);
				
				System.out.println("New project successfully created!");
				
				// Take user directly to where he/she can edit the newly created
				// project.
				manageSingleProjectFlow();
				break;
			case 3:
				System.out.println("Logging out...");
				
				// Return to login flow
				startUpFlow();
				break;
			default:
				// invalid input, restart flow
				System.out.println("Invalid input, please try again.");
				
				// Restart flow
				managementFlow();
				return;
		}
	}
	
	public void browseProjectsFlow()
	{
		// For temporarily store collections of projects
		BrowseProjectsWindow browseProject = new BrowseProjectsWindow();
		browseProject.setVisible(true);
		browseProject.updateListDisplay(getAllProjectStrings());
		ArrayList<Project> aP;
		
		// for storing user input
		int selection = 0;
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("          Project Browser");
		System.out.println("Please make your selection by inputing the");
		System.out.println("# of the task you want to perform followed");
		System.out.println("by the return key.");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("1- Show All   2- Owned   3- By ID   4- Back");
		System.out.println("");
		System.out.print("Selection: ");
		selection = in.nextInt();
		in.nextLine();
		switch (selection)
		{
			case 1:
				
				break;
			case 2:
				// Get all projects owned by user
				aP = manager.getProjects(userLoggedIn.getName());
				
				// Display projects
				for (int i = 0; i < aP.size(); i++)
				{
					System.out.println(Integer.toString(i) + "- ID# "
							+ Integer.toString(aP.get(i).getId()) + " - Name: "
							+ aP.get(i).getName() + " - Owner: "
							+ aP.get(i).getOwner());
				}
				
				if (aP != null && aP.size() > 0)
				{
					System.out.println("");
					System.out
							.println("You may now select a project to edit using");
					System.out
							.println("the corresponding # from the list, followe");
					System.out.println("-d by the return key.");
					System.out.println("");
					
					System.out.print("Selection: ");
					selection = in.nextInt();
					in.nextLine();
					if (selection < aP.size() && selection >= 0)
					{
						currentProject = aP.get(selection);
						
						System.out.println("");
						System.out
								.println("Selection confirmed! Entering edit UI..");
						System.out.println("");
						
						manageSingleProjectFlow();
					}
				}
				else
				{
					System.out.println("");
					System.out
							.println("No projects were found, returning to main interface.");
					System.out.println("");
					managementFlow();
				}
				break;
			case 3:
				
				System.out.println("");
				System.out.println("Please enter the project's id now: ");
				System.out.println("");
				selection = in.nextInt();
				in.nextLine();
				// Get project by id
				currentProject = manager.getProject(selection);
				
				if (currentProject != null)
				{
					System.out.println("");
					System.out
							.println("Selection confirmed! Entering edit UI..");
					System.out.println("");
					
					manageSingleProjectFlow();
				}
				else
				{
					System.out.println("");
					System.out.println("No projects were found.");
					System.out.println("");
					browseProjectsFlow();
				}
				break;
			case 4:
				// Go back
				System.out.println("Returning to home screen");
				
				// Return to login flow
				managementFlow();
				break;
			default:
				// invalid input, restart flow
				System.out.println("Invalid input, please try again.");
				
				// Restart flow
				browseProjectsFlow();
				return;
		}
	}
	
	public void manageSingleProjectFlow()
	{
		int selection = 0;
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("          Single Project Manager");
		System.out.println("Please make your selection by inputing the");
		System.out.println("# of the task you want to perform followed");
		System.out.println("by the return key.");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out
				.println("1- Delete Project  2- Edit Project Owner/Name  3- View Tasks Associated To The Project  4- Add New Task To The Project  5- Back");
		System.out.println("");
		System.out.print("Selection: ");
		selection = in.nextInt();
		in.nextLine();
		switch (selection)
		{
			case 1:
				// - delete project
				System.out.println();
				System.out.println("Deleting selected project...");
				manager.delProj(currentProject.getId());
				System.out.println("Project deleted Successfully");
				manageSingleProjectFlow();
				break;
			case 2:
				// - edit project owner/name
				System.out.println();
				System.out
						.println("Would you like to edit project owner (o) or project name (n)? (o/n?) [Press (b) to go back]");
				
				String usrchoice = in.next();
				
				boolean invalidchoice = true;
				
				while (invalidchoice)
				{
					if (usrchoice.equals("b"))
					{
						manageSingleProjectFlow();
						return;
					}
					else if (usrchoice.equals("o"))
					{
						invalidchoice = false;
						// Connect to the DB and update
						System.out
								.println("Please give a valid username to set as new project owner:");
						String owner = in.next();
						Statement stmt;
						ResultSet rs;
						try
						{
							stmt = conn.createStatement();
							rs = stmt
									.executeQuery("SELECT * FROM users WHERE user_id = '"
											+ owner + "';");
							
							if (rs.next())
							{
								currentProject.setOwner(owner);
								
								stmt.executeUpdate("UPDATE projects SET owner_id = '"
										+ owner
										+ "' WHERE project_id = "
										+ currentProject.getId() + ";");
								System.out
										.println("Project Owner Successfully Updated!");
								stmt.close();
							}
							else
							{
								System.out.println("User not found.");
								
								// Close variables
								rs.close();
								stmt.close();
								
								// User id not found
								manageSingleProjectFlow();
							}
							
						}
						catch (Exception e)
						{
							System.err.println(e.getClass().getName() + ": "
									+ e.getMessage()
									+ " in manageSingleProjectFlow()");
							System.exit(0);
						}
					}
					else if (usrchoice.equals("n"))
					{
						invalidchoice = false;
						// Connect to the DB and update
						System.out.println("Please give a new project name: ");
						String newname = in.next();
						Statement stmt;
						try
						{
							stmt = conn.createStatement();
							stmt.executeUpdate("UPDATE projects SET project_name = '"
									+ newname
									+ "' WHERE project_id = "
									+ currentProject.getId() + ";");
							stmt.close();
							
							currentProject.setName(newname);
							
						}
						catch (Exception e)
						{
							System.err.println(e.getClass().getName() + ": "
									+ e.getMessage()
									+ " in manageSingleProjectFlow()");
							System.exit(0);
						}
					}
					System.out.println("Returning to home screen");
					
					// Return to login flow
					browseProjectsFlow();
				}
				break;
			case 3:
				// - view tasks associated with project
				ArrayList<Task> aT = currentProject.getTasks();
				
				if (aT != null && aT.size() > 0)
				{
					for (int i = 0; i < aT.size(); i++)
					{
						System.out.println(Integer.toString(i) + "| id: "
								+ aT.get(i).getId() + " | name: "
								+ aT.get(i).getName() + " | description: "
								+ aT.get(i).getDescription()
								+ " | start_time: "
								+ Integer.toString(aT.get(i).getStartTime())
								+ " | end_time: "
								+ Integer.toString(aT.get(i).getEndTime()));
					}
					System.out.println("");
					System.out
							.println("You may now select a task to edit using");
					System.out
							.println("the corresponding # from the list, foll");
					System.out.println("-owed by the return key.");
					System.out.println("");
					
					System.out.print("Selection: ");
					selection = in.nextInt();
					in.nextLine();
					while (selection >= aT.size() || selection < 0)
					{
						System.out.println("");
						System.out
								.println("Invalid selection, please try again.");
						System.out.println("");
						
						System.out.print("Selection: ");
						selection = in.nextInt();
						in.nextLine();
					}
					
					// Set task to guaranteed good selection
					currentTask = aT.get(selection);
					
					// go to edit task flow
					editTaskFlow();
				}
				else
				{
					// No tasks associated with this project
					System.out.println("");
					System.out.println("No tasks associated with this project");
					System.out.println("Going back");
					
					manageSingleProjectFlow();
					return;
				}
				
				break;
			case 4:
				// - add new task to project
				// task_id, name, description, duration, project_id, owner_id,
				// pre-reqs
				
				// To figure out unique id
				/*
				 * ArrayList<Task> aT2 = currentProject.getTasks(); int tId; if
				 * (aT2 != null) tId = aT2.size(); else tId = 0;
				 */
				
				// Find out what user wants
				String tName,
				tDesc;
				int tStartTime,
				tEndTime;
				
				System.out.println("");
				System.out.println("Please give the task a name:");
				System.out.println("");
				tName = in.nextLine();
				
				System.out.println("");
				System.out.println("Please describe the task:");
				System.out.println("");
				tDesc = in.nextLine();
				
				boolean illegalValues = true;
				do
				{
					System.out.println("");
					System.out
							.println("Please define the start_time of the task:");
					System.out.println("");
					tStartTime = in.nextInt();
					in.nextLine();
					
					System.out.println("");
					System.out
							.println("Please define the end_time of the task:");
					System.out.println("");
					tEndTime = in.nextInt();
					in.nextLine();
					illegalValues = (tStartTime < 0) || (tEndTime < 0)
							|| (tStartTime > tEndTime);
				}
				while (illegalValues);
				// Create the new task
				/*
				 * currentTask = new Task((currentProject.getId() * 1000) + tId,
				 * tName, tDesc, tStartTime, tEndTime, currentProject.getId(),
				 * userLoggedIn.getName());
				 */
				
				currentTask = new Task(0, tName, tDesc, tStartTime, tEndTime,
						currentProject.getId(), userLoggedIn.getName());
				
				System.out.println("");
				System.out
						.println("Success! Let's take a closer look at that task...");
				System.out.println("");
				
				// Goto edit task flow
				editTaskFlow();
				break;
			case 5:
				// - back to browseProjectsFlow()
				System.out.println("Returning to home screen");
				
				// Return to login flow
				browseProjectsFlow();
				break;
			default:
				// invalid input, restart flow
				System.out.println("Invalid input, please try again.");
				
				// Restart flow
				manageSingleProjectFlow();
				return;
		}
	}
	
	/**
	 * INCOMPLETE - Allows users the ability to edit, delete or view specific
	 * tasks, and allows access to the edit task prereq flow, which will allow
	 * to edit the pre-requisites of a specific task.
	 * 
	 * MAKE SURE THAT: pre_req.end_time < task.start_time Guidelines: 1- Look in
	 * the DB for pre_req task and get its start time 2- Look in the DB for the
	 * current task and get its end time 3- Make sure that pre_req.end_time <
	 * task.start_time 4- Update or ask the user for correct input
	 */
	
	/* This has to be done by the programmers!; */
	
	public void editTaskFlow()
	{
		int selection = 0;
		String tInput;
		
		// Screen message
		System.out.println("");
		System.out.println("            Task edit menu");
		System.out.println("");
		System.out.println("Please make your selection by inputing the");
		System.out.println("# of the task you want to perform followed");
		System.out.println("by the return key.");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("1- Delete task\n2- Change name\n"
				+ "3- Change description\n4- Change duration\n"
				+ "5- Edit Pre-Required Tasks\n6- Go back");
		System.out.println("");
		System.out.print("Selection: ");
		selection = in.nextInt();
		in.nextLine();
		switch (selection)
		{
			case 1:
				// Delete task
				currentTask.deleteTask();
				System.out.println("Task deleted! Returning to project view.");
				
				// Go back;
				manageSingleProjectFlow();
				return;
			case 2:
				// Change task name
				break;
			case 3:
				// Change task description
				break;
			case 4:
				// Chanage task duration
				break;
			case 5:
				// Go into the pre-req task editor
				editTaskPreReqsFlow();
				break;
			case 6:
				System.out.println("Returning to single project manager.");
				// Go back;
				manageSingleProjectFlow();
				return;
		}
		
	}
	
	/**
	 * INCOMPLETE - Allows adding and deleting pre-requisite tasks for a
	 * specific task.
	 */
	public void editTaskPreReqsFlow()
	{
		// TODO
	}
	
	public void createNewUserFlow(){
		// New account creation
		UserCreationWindow userCreation = new UserCreationWindow();
		userCreation.setVisible(true);
	}
	
	
	/**
	 * The main calls this flow to initiate userLogin and open
	 * the User log in window.
	 */
	public void userLogInFlow(){
		
		LoginVerificationWindow loginVerif = new LoginVerificationWindow();
		loginVerif.setVisible(true);
	}
	
	public void end()
	{
		try
		{
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in end()");
			System.exit(0);
		}
		System.out.println("Thank you for using TWPMS! Goodbye.");
		System.exit(0);
	}
	
	/**
	 * userLoginVerification is called by the GUI when the proceed button is
	*  pressed from the LoginVerificationWindow.  
	*  It compares the values of the userName and Password fields to
	*  those stores in the database.
	*/
	public void userLoginVerification(String usrID, String pwd){
		
	
		System.out.println("started userLoginVerification()");
		
		Statement stmt;
		ResultSet rs;
		
		try
		{
			stmt = conn.createStatement();
			rs = stmt
					.executeQuery("SELECT * FROM users WHERE user_id = '"
							+ usrID + "';");
			
			if (rs.next())
			{
				
				
				//if the password is invalid display failed login window
				//and loop back to LoginVerificationWindow
				if(!pwd.equals(rs.getString("user_pwd")))
				{
					//TODO: Make a Failed Login Window and query the user for a new log in
					System.out.println("");
					System.out.println("INVALID PASSWORD!");
					System.out.println("");
					
					
					
					// Close variables
					rs.close();
					stmt.close();
					
					// Restart login
					userLogInFlow();
				}
				
				// Correct password has been input...
				System.out.println("Password accepted, LOGGING IN!");
				
				userLoggedIn = new User(rs.getString("user_id"),
						rs.getString("user_pwd"),
						rs.getString("f_name"), rs.getString("l_name"),
						false);
				
				// Close variables
				rs.close();
				stmt.close();
					
				// unto main management screen
				managementFlow();
			}
			else
			{	
				System.out
						.println("User not found. Restarting loggin process.");
				
				// Close variables
				rs.close();
				stmt.close();
				
				// User id not found
				userLogInFlow();
			}
			
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": "
					+ e.getMessage() + " in testCreateProject()");
			System.exit(0);
		}
				
	}
	
	public void createNewUser(String userName, String password, String fName, String lName){

		Statement stmt;
		ResultSet rs;
		
		//attempt to create new User
		try
		{
			stmt = conn.createStatement();
			rs = stmt
					.executeQuery("SELECT * FROM users WHERE user_id = '"
							+ userName + "';");
			//TODO:
			//if UserName already exist display userCreationFailed
			// window and loop back to userCreationWindow
			if (rs.next())
			{
				System.out.println("");
				System.out
						.println("USERNAME ALREADY IN USE! TRY AGAIN!");
				System.out.println("");
				
				//go back to user creation
				createNewUserFlow();
				
			}
			
			// Add new user to database (guaranteed non-unique) and make it active user
			userLoggedIn = new User(userName, password, fName, lName);
			
			System.out
					.println("User successfully created! Please log in");
			
			managementFlow();
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		// User id not found
		startUpFlow();
		
	}

	public ArrayList<String> getAllProjectStrings(){
		
		ArrayList<Project> aP;
		ArrayList<String> pS = new ArrayList<String>();
		
		// Get all projects
		aP = manager.getProjects();
		
		// Display all projects
		for (int i = 0; i < aP.size(); i++)
		{
			pS.add(Integer.toString(i) + "- ID# "
					+ Integer.toString(aP.get(i).getId()) + " - Name: "
					+ aP.get(i).getName() + " - Owner: "
					+ aP.get(i).getOwner());
		}
		return pS;
		
	}

}
