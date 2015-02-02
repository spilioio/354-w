package core;

public class User {
	private String userName, userPwd, fname, lname;
	public User(String userName, String userPwd, String fname, String lname){
		this.userName = userName;
		this.userPwd = userPwd;
		this.fname = fname;
		this.lname = lname;
		//Add the user to the database
	}
	
	public void deleteUser(){
		//delete from database
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
	}
	
	public void modifyFirstName(String fname){
		this.fname = fname;
	}
	
	public void modifyPwd(String userPwd){
		this.userPwd = userPwd;
	}
}
