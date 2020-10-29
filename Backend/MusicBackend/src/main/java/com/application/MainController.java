package com.application;
import com.application.people.UserRepository;
import com.application.posts.Post;
import com.application.posts.PostRepository;
import com.application.posts.RequestNewPost;
import com.application.posts.RequestNewPostEditor;
import com.application.posts.RequestPostType;
import com.application.posts.RequestPostTypeEditor;
import com.application.posts.files.FileDB;
import com.application.posts.files.FileStorageService;
import com.application.posts.message.ResponseMessage;
import com.application.skill_level.AppliedSkillLevelRepository;
import com.application.skill_level.SkillLevel;
import com.application.skill_level.SkillLevelRepository;
import com.application.tagging.RequestTagApplication;
import com.application.tagging.Tag;
import com.application.tagging.TagRepository;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.application.people.User;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/users") // This means the URLs will start with /experiment
public class MainController {
    @Autowired //This will get the bean called userRepository
    //which was auto generated by spring
    private UserRepository userRepository;
    
    @Autowired
	private PostRepository postRepository;
    
    @Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private SkillLevelRepository skillLevelRepository;
	
	@Autowired
	private AppliedSkillLevelRepository applicationRepository;

    @Autowired
    private FileStorageService storageService;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ProgramaticValidator validator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.registerCustomEditor(RequestNewPost.class, new RequestNewPostEditor(mapper, validator));
		binder.registerCustomEditor(RequestPostType.class, new RequestPostTypeEditor(mapper, validator));
	}
    
    @PostMapping(path="") //Map only POST requests
    @CrossOrigin
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam(required = false) Boolean stayLoggedIn){
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        if(userRepository.findByUsername(name) != null){
            return "username is taken";
        }else if(userRepository.findByEmail(email) != null){
            return "email is already registered";
        }

        User n = new User();
        n.setEmail(email);
        n.setName(name);
        //Encrypt Password
        password = passwordEncryptor.encryptPassword(password);
        n.setPassword(password);
        if(stayLoggedIn != null){
            n.setStaySignedIn(stayLoggedIn);
        }else{
            n.setStaySignedIn(false);
        }
        userRepository.save(n);
        return "Saved";
    }

    //Returns all users
    @JsonView(View.UserView.class)
    @GetMapping(path="")
    @CrossOrigin
    public @ResponseBody Iterable<User> getAllUsers(HttpServletResponse response){
        //Returns a JSON or XML document with the users in it
        response.addHeader("Access-Control-Expose-Headers", "Content-Range");
        response.addHeader("Content-Range", "users 0-20/50");
        return userRepository.findAll();
    }

    //Returns a specific user
    @JsonView(View.UserView.class)
    @GetMapping(path= {"/{userId}"})
    @CrossOrigin
    public @ResponseBody Optional<User> getByUserId(@PathVariable Long userId){
        if(userId != null){
            return userRepository.findById(userId);
        }
        return null;
    }

        //Returns a specific user
    @JsonView(View.UserView.class)
    @GetMapping(path= {"username/{username}"})
    @CrossOrigin
    public @ResponseBody Optional<User> getByUsername(@PathVariable String username){
        if (username != null) {
            return userRepository.findByUsername(userId);
        }
        return null;
    }

    //Deletes a specific user
    @DeleteMapping(path="/{userId}")
    @CrossOrigin
    public @ResponseBody String removeUser(@PathVariable Long userId){
    	Optional<User> u = userRepository.findById(userId);
        if(u.isPresent())
        {
        	User user = u.get();
        	for(Post p : user.getPosts().values())
        	{
            	if(p.getContentPath() != null && !p.getContentType().contains("External"))
            	{
            		storageService.removeFile(p.getContentPath());
            	}
            	p.remove(applicationRepository, postRepository);
        	}
            userRepository.delete(user);
            return "user deleted";
        }else{
            return "user doesn't exist";
        }
    }

    //Deletes all users
    @DeleteMapping(path="")
    @CrossOrigin
    public @ResponseBody String removeAllUsers(){
       for(User user : userRepository.findAll())
       {
    	   for(Post p : user.getPosts().values())
	       	{
	           	if(p.getContentPath() != null && !p.getContentType().contains("External"))
	           	{
	           		storageService.removeFile(p.getContentPath());
	           	}
	           	p.remove(applicationRepository, postRepository);
	       	}
           userRepository.delete(user);
       }
       return "all users have been deleted";
    }

    //Updates all users method stub(not sure what we want this to do yet
    @PutMapping(path="")
    @CrossOrigin
    public @ResponseBody String updateAllUsers(){
        return null;
    }

    //Updates specific user
    @JsonView(View.UserView.class)
    @PutMapping(path="/{userId}")
    @CrossOrigin
    public @ResponseBody User updateUser(@PathVariable Long userId, @RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam Boolean stayLoggedIn, @RequestParam Long newId){
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        User toUpdate = userRepository.findByid(userId);
        if(toUpdate != null){
            toUpdate.setStaySignedIn(stayLoggedIn);
            password = passwordEncryptor.encryptPassword(password);
            toUpdate.setPassword(password);
            toUpdate.setName(name);
            toUpdate.setEmail(email);
            toUpdate.setId(newId);
            userRepository.save(toUpdate);
            return toUpdate;
        }else{
            return null;
        }
    }

    @PostMapping(path="/login")
    @CrossOrigin
    public @ResponseBody String userLogin(@RequestParam String loginID, @RequestParam String password, @RequestParam Boolean stayLoggedIn){
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        if(userRepository.findByUsername(loginID) != null){
            if(encryptor.checkPassword(password, userRepository.findByUsername(loginID).getPassword()) == true){
                User toUpdate = userRepository.findByUsername(loginID);
                toUpdate.setStaySignedIn(stayLoggedIn);
                userRepository.save(toUpdate);
                return "login successful";
            }else{
                return "incorrect password";
            }
        }else if(userRepository.findByEmail(loginID) != null){
            if(encryptor.checkPassword(password, userRepository.findByEmail(loginID).getPassword()) == true){
                User toUpdate = userRepository.findByEmail(loginID);
                toUpdate.setStaySignedIn(stayLoggedIn);
                userRepository.save(toUpdate);
                return "login successful";
            }else{
                return "incorrect password";
            }
        }else{
            return "no user registered under this loginID";
        }
    }

    @PostMapping(path="/changeRememberMe")
    @CrossOrigin
    public @ResponseBody Boolean changeRememberMe(@RequestParam String loginID, @RequestParam Boolean stayLoggedIn){
        if(userRepository.findByUsername(loginID) != null){
            User toUpdate = userRepository.findByUsername(loginID);
            toUpdate.setStaySignedIn(stayLoggedIn);
            userRepository.save(toUpdate);
            return true;
        }else if(userRepository.findByEmail(loginID) != null){
            User toUpdate = userRepository.findByEmail(loginID);
            toUpdate.setStaySignedIn(stayLoggedIn);
            userRepository.save(toUpdate);
            return true;
        }
        return false;
    }

    @PostMapping(path="/addPost")
	public @ResponseBody ResponseEntity<ResponseMessage> addPost(@RequestParam @Valid RequestNewPost post, @RequestParam(name="file", required=false) MultipartFile file)
	{
		String fpath = null;
		String message = "";
		if(file != null)
		{
	        try {
	           FileDB stored = storageService.store(file);
	           fpath = stored.getId();
	            message = "Uploaded the file successfully: " + file.getOriginalFilename() + "; ";
	            
	        } catch (Exception e) {
	            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	        }
		}
		else if(post.getContentPath() != null && post.getContentType().contains("External"))
		{
			fpath = post.getContentPath();
		}
		Post p = new Post(post.getTitle(), post.getContentType(), post.getIsSearch());
		p.setTextContent(((post.getTextContent() == null || post.getTextContent().equals(""))? null : post.getTextContent()));
		User u = userRepository.findById(post.getOwnerId()).get();
		p.setOwner(u);
		p.setContentPath(fpath);
		postRepository.save(p);
		for(RequestTagApplication tag : post.getApplications())
		{
			Tag tg = tagRepository.findByName(tag.getTag().getName()).get();
			SkillLevel level = skillLevelRepository.findByName(tag.getSkill().getName()).get();
			p.addTag(applicationRepository, tg, level, tag.getBounded(), tag.getLowerBounded());
		}
		message += "Post saved.";
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}

}
