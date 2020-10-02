package com.application.tagging;

@ExistentSkillLevel(nameField="name")
public class RequestSkillLevel 
{
	private String name;
	
	public RequestSkillLevel()
	{
		name = "undefined";
	}
	
	public RequestSkillLevel(String name)
	{
		if(name == null || name.trim().equals(""))
		{
			this.name = "undefined";
		}
		else
		{
			this.name = name;
		}
	}
	
	public void setName(String name)
	{
		if(name == null || name.trim().equals(""))
		{
			this.name = "undefined";
		}
		else
		{
			this.name = name;
		}
	}
	
	public String getName()
	{
		return name;
	}
	
}
