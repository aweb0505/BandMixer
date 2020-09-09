package com.application.tagging;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path="/tags") 
public class TagController 
{
	@Autowired //This will get the bean called userRepository
    //which was auto generated by spring
    private TagRepository tagRepository;

    
    @PostMapping(path="/add") //Map only POST requests
    public @ResponseBody String addNewTag(@RequestParam String name, @RequestParam(defaultValue="true") boolean allowSkill)
    {
    	if(!tagExists(name))
    	{
    		Tag n = new Tag(name, allowSkill);
    		tagRepository.save(n);
    		return "Saved";
    	}
        return "Already Present";
    }
    
    @PostMapping(path="/update")
    public @ResponseBody String updateTag(@RequestParam String oldName, @RequestParam String newName, @RequestParam boolean allowSkill)
    {
    	if(!tagExists(oldName))
    	{
    		return "No such tag present";
    	}
    	Optional<Tag> member = tagRepository.findByName(oldName);
    	Tag toUpdate = member.get();
    	toUpdate.setName(newName);
    	toUpdate.setAllowsSkill(allowSkill);
    	return "Updated";
    }
    
    @PostMapping(path="/update/name")
    public @ResponseBody String updateTag(@RequestParam String oldName, @RequestParam String newName)
    {
    	if(!tagExists(oldName))
    	{
    		return "No such tag present";
    	}
    	Optional<Tag> member = tagRepository.findByName(oldName);
    	Tag toUpdate = member.get();
    	toUpdate.setName(newName);
    	return "Updated";
    }
    
    @PostMapping(path="/update/skill")
    public @ResponseBody String updateTag(@RequestParam String oldName, @RequestParam boolean allowSkill)
    {
    	if(!tagExists(oldName))
    	{
    		return "No such tag present";
    	}
    	Optional<Tag> member = tagRepository.findByName(oldName);
    	Tag toUpdate = member.get();
    	toUpdate.setAllowsSkill(allowSkill);
    	return "Updated";
    }
    
    @GetMapping(path="fetch/{name}")
    public Tag getByName(@PathVariable String name)
    {
    	Optional<Tag> tag = tagRepository.findByName(name);
    	if(!tag.isPresent())
    	{
    		throw new TagNotFoundException("name-" + name);
    	}
    	return tag.get();
    }

    @GetMapping(path="/all")
    @CrossOrigin
    public @ResponseBody Iterable<Tag> getAllTags(){
        //Returns a JSON or XML document with the users in it
        return tagRepository.findAll();
    }
    
    private boolean tagExists(String name)
    {
    	Optional<Tag> member = tagRepository.findByName(name);
    	return member.isPresent();
    }
    
    private class TagNotFoundException extends IllegalArgumentException
    {
    	public TagNotFoundException() {super();}
    	public TagNotFoundException(String message) {super(message);}
    }
}
