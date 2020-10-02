package com.application.tagging;

@UpdateExistentSkillLevel(nameField="name", valueField="value", newNameField="newName")
public class RequestSkillLevelUpdate 
{
	private String name;
	private String newName;
	private Integer value;
	
	public RequestSkillLevelUpdate() {}
	
	public RequestSkillLevelUpdate(String nm, String newnm, Integer val)
	{
		name = nm;
		newName = newnm;
		value = val;
	}
	
	public RequestSkillLevelUpdate(String nm, String newnm)
	{
		this(nm, newnm, null);
	}
	
	public RequestSkillLevelUpdate(String nm, Integer val)
	{
		this(nm, null, val);
	}
	
	public RequestSkillLevelUpdate(String nm)
	{
		this(nm, null, null);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}

}
