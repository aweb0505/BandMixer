package com.application.tagging;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.application.skill_level.AppliedSkillLevelRepository;


@Controller
@RequestMapping(path="/tags") 
public class TagController 
{
	@Autowired
    private TagRepository tagRepository;
	
	@Autowired 
	private AppliedSkillLevelRepository applicationsRepository;

    
    @PostMapping(path="/add") //Map only POST requests
    public @ResponseBody String addNewTag(@RequestBody RequestNewTag newTag)
    {
    	Tag n = new Tag(newTag.getName(), newTag.getAcceptsSkill());
		tagRepository.save(n);
		return "Saved";
    }
    
    @PostMapping(path="/update")
    public @ResponseBody String updateTag(@RequestBody RequestUpdateTag updateTag)
    {
    	
    	Optional<Tag> member = tagRepository.findByName(updateTag.getName());
    	Tag toUpdate = member.get();
    	String newName = updateTag.getNewName();
    	String name = updateTag.getName();
    	boolean allowSkill = updateTag.getAllowsSkill();
    	boolean noNameChange = (newName == null || newName.equals("") || newName.equals(name));
		boolean noAllowChange = allowSkill == toUpdate.getAllowskill();
    	if(!noNameChange)
    	{
    		toUpdate.setName(newName);
    	}
    	if(!noAllowChange)
    	{
    		toUpdate.setAllowsSkill(allowSkill);
    	}
    	tagRepository.save(toUpdate);
    	return "Updated";
    }
    
    @PostMapping(path="/remove")
    public @ResponseBody String removeTag(@RequestBody RequestExistentTag tag)
    {
    	
    	tagRepository.findByName(tag.getName()).get().remove(applicationsRepository, tagRepository);
    	return "Tag: " + tag.getName() + " removed.";
    }
    
    @GetMapping(path="/fetch")
    public @ResponseBody Tag getByName(@RequestParam String name)
    {
    	Optional<Tag> tag = tagRepository.findByName(name);
    	return tag.get();
    }

    @GetMapping(path="/all")
    @CrossOrigin
    public @ResponseBody Iterable<Tag> getAllTags(){
        //Returns a JSON or XML document with the users in it
        return tagRepository.findAll();
    }
    

    
    @GetMapping(path="/hasSkill")
    public @ResponseBody boolean allowsSkill(@RequestBody RequestExistentTag tagChoice)
    {
    	Optional<Tag> tag = tagRepository.findByName(tagChoice.getName());
    	return tag.get().getAllowskill();
    }
    
    
}
