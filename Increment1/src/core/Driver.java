package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver
{
	/** To capture user input */
	private static Scanner in;
	
	/** The project manager object */
	private static ProjectManager manager;
	
	private static Connection conn;
	
	private static User userLoggedIn;
	
	private static Project currentProject;
	
	public static void main(String[] args)
	{
		// Initialize shared variables
		start();
		
		// Greeting message
		System.out.println("TEAM W   +++   PROJECT MANAGEMENT SOFTWARE");
		System.out.println("");
		System.out.println("               Welcome!");
		
		// Enter user login
		loginFlow();
		
		// End sessions
		end();
	}
	
	/** Run this method once before use. */
	private static void start()
	{
		// Local variables
		in = new Scanner(System.in);
		manager = new ProjectManager();
		
		// Connection to DB
		conn = null;
		try
		{
			// connect to db (file test.db must lay in the project dir)
			// NOTE: it will be created if not exists
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:COMP354");
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in init()");
			System.exit(0);
		}
	}
	
	private static void loginFlow()
	{
		int selection = 0;
		String tInput;
		
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
		System.out.println("");
		System.out.println("");
		
		// Handle input cases
		switch (selection)
		{
			case 1:
				System.out
						.print("Please enter your user id followed by the\nreturn key: ");
				tInput = in.next();
				
				Statement stmt;
				ResultSet rs;
				
				try
				{
					stmt = conn.createStatement();
					rs = stmt
							.executeQuery("SELECT * FROM users WHERE user_id = '"
									+ tInput + "';");
					
					if (rs.next())
					{
						String tPass;
						// Ask for password
						System.out.println("");
						System.out
								.print("Please enter your password followed by the\nreturn key: ");
						tPass = in.next();
						
						while (!tPass.equals(rs.getString("user_pwd")))
						{
							// Ask for password
							System.out.println("");
							System.out.println("INVALID PASSWORD!");
							System.out.println("");
							System.out
									.println("Try Again? (y for yes, any other for no)");
							tPass = in.next();
							if (tPass.equals("y"))
							{
								System.out
										.print("Please enter your password followed by the\nreturn key: ");
								tPass = in.next();
							} else
							{
								// Close variables
								rs.close();
								stmt.close();
								
								// Restart login
								loginFlow();
								return;
							}
						}
						// Correct password has been input...
						System.out.println("Password accepted, LOGGING IN!");
						
						userLoggedIn = new User(rs.getString("user_id"), rs.getString("user_pwd"), rs.getString("f_name"), rs.getString("l_name"), false);
						
						// Close variables
						rs.close();
						stmt.close();
						
						// unto main management screen
						managementFlow();
					} else
					{
						System.out
								.println("User not found. Restarting loggin process.");
						
						// Close variables
						rs.close();
						stmt.close();
						
						// User id not found
						loginFlow();
					}
					
				} catch (Exception e)
				{
					System.err.println(e.getClass().getName() + ": "
							+ e.getMessage() + " in testCreateProject()");
					System.exit(0);
				}
				break;
			case 2:
				// New account creation
				String temp[] = new String[4];
				
				System.out.println("Please enter a user id: ");
				temp[0] = in.next();
				System.out.println("");
				
				System.out.println("Select your password: ");
				temp[1] = in.next();
				System.out.println("");
				
				System.out.println("First name: ");
				temp[2] = in.next();
				System.out.println("");
				
				System.out.println("Last name: ");
				temp[3] = in.next();
				System.out.println("");
				
				// Add new user
				userLoggedIn = new User(temp[0], temp[1], temp[2], temp[3]);
				
				System.out.println("User successfully created! Please log in");
				
				// User id not found
				loginFlow();
				
				break;
			case 3:
				// exit
				end();
				break;
			default:
				// invalid input, restart flow
				System.out.println("Invalid input, please try again.");
				loginFlow();
				return;
		}
	}
	
	private static void managementFlow()
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
		
		switch (selection)
		{
			case 1:
				// TODO browse projects
				browseProjectsFlow();
				break;
			case 2:
				// New account creation
				String temp;
				
				System.out.println("Please enter a project name: ");
				temp = in.next();
				System.out.println("");
				
				System.out.println("temp = " + temp);
				// Create new project object.
				currentProject = new Project(userLoggedIn.getName(), temp);
				
				// Add it to DB via project manager.
				currentProject = manager.addProject(currentProject);
				
				System.out.println("New project successfully created!");
				
				// Take user directly to where he/she can edit the newly created
				// project.
				manageSingleProjectFlow();
				break;
			case 3:
				System.out.println("Logging out...");
				
				// Return to login flow
				loginFlow();
				break;
			default:
				// invalid input, restart flow
				System.out.println("Invalid input, please try again.");
				
				// Restart flow
				managementFlow();
				return;
		}
	}
	
	private static void browseProjectsFlow()
	{
		// For temporarily store collections of projects
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
		
		switch (selection)
		{
			case 1:
				// Get all projects
				aP = manager.getProjects();
				
				// Display all projects
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
					
					if (selection < aP.size() && selection >= 0)
					{
						currentProject = aP.get(selection);
						
						System.out.println("");
						System.out
								.println("Selection confirmed! Entering edit UI..");
						System.out.println("");
						
						manageSingleProjectFlow();
					}
				} else
				{
					System.out.println("");
					System.out
							.println("The project manager is currently empty, returning to main interface.");
					System.out.println("");
					managementFlow();
				}
				
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
					
					if (selection < aP.size() && selection >= 0)
					{
						currentProject = aP.get(selection);
						
						System.out.println("");
						System.out
								.println("Selection confirmed! Entering edit UI..");
						System.out.println("");
						
						manageSingleProjectFlow();
					}
				} else
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
				
				// Get project by id
				currentProject = manager.getProject(selection);
				
				if(currentProject != null)
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
					System.out
							.println("No projects were found.");
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
	
	private static void manageSingleProjectFlow()
	{
		// TODO
		
		// 5 choices:
		// - delete project
		// - edit project owner/name
		// - view tasks associated with project
		// - add new task to project
		// - back to browseProjectsFlow()
		// for storing user input
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
		System.out.println("1- Delete Project  2- Edit Project Owner/Name  3- View Tasks Associated To The Project  4- Add New Task To The Project  5- Back");
		System.out.println("");
		System.out.print("Selection: ");
		selection = in.nextInt();
		
		switch (selection)
		{
			case 1:
				// - delete project
				System.out.println();
				System.out.println("Deleting selected project...");
				manager.delProj(currentProject.getId());
				System.out.println("Project deleted Successfully");
				//Should go to the main menu or something?
				break;
			case 2:
				// - edit project owner/name
				System.out.println();
				System.out.println("Would you like to edit project owner (o) or project name (n)? (o/n?) [Press (b) to go back]");
				
				String usrchoice = in.next();
				
				boolean invalidchoice = true;
				
				while (invalidchoice){
					if (usrchoice.equals("b"))
						manageSingleProjectFlow();
					else if (usrchoice.equals("o")){
						//Connect to the DB and update
					}else if (usrchoice.equals("n")){
						//Connect to the DB and update
					}
				}
				break;
			case 3:
				// - view tasks associated with project
				break;
			case 4:
				// - add new task to project
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
	
	private static void end()
	{
		try
		{
			conn.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage()
					+ " in end()");
			System.exit(0);
		}
		System.out.println("Thank you for using TWPMS! Goodbye.");
		System.exit(0);
	}
	
}
