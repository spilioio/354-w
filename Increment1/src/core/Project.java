package core;

public class Project
{
	private String owner_id, project_name;
	private int project_id;
	
	public Project(String owner_id, String project_name)
	{
		this.owner_id = owner_id;
		this.project_name = project_name;
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
		Project p = (Project)otherProject;
		if(p.getId() == project_id)
			return true;
		else return false;
	}
	
}
