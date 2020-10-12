package com.application;
import com.application.people.UserRepository;
import com.application.people.User;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping(path="/users") // This means the URLs will start with /experiment
public class MainController {
    @Autowired //This will get the bean called userRepository
    //which was auto generated by spring
    private UserRepository userRepository;

    
    @PostMapping(path="") //Map only POST requests
    @CrossOrigin
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam Boolean stayLoggedIn){
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
        n.setStaySignedIn(stayLoggedIn);
        userRepository.save(n);
        return "Saved";
    }

    //Returns all users
    @GetMapping(path="")
    @CrossOrigin
    public @ResponseBody Iterable<User> getAllUsers(HttpServletResponse response){
        //Returns a JSON or XML document with the users in it
        response.addHeader("Access-Control-Expose-Headers", "Content-Range");
        response.addHeader("Content-Range", "users 0-20/50");
        return userRepository.findAll();
    }

    //Returns a specific user
    @GetMapping(path= {"/{userId}"})
    @CrossOrigin
    public @ResponseBody Optional<User> getByUserId(@PathVariable Long userId){
        if(userId != null){
            return userRepository.findById(userId);
        }
        return null;
    }

    //Deletes a specific user
    @DeleteMapping(path="/{userId}")
    @CrossOrigin
    public @ResponseBody String removeAllUsers(@PathVariable Long userId){
        if(userRepository.findByID(userId) != null){
            userRepository.delete(userRepository.findByID(userId));
            return "user deleted";
        }else{
            return "user doesn't exist";
        }
    }

    //Deletes all users
    @DeleteMapping(path="")
    @CrossOrigin
    public @ResponseBody String removeUser(){
       userRepository.deleteAll();
       return "all users have been deleted";
    }

    //Updates all users method stub(not sure what we want this to do yet
    @PutMapping(path="")
    @CrossOrigin
    public @ResponseBody String updateAllUsers(){
        return null;
    }

    //Updates specific user
    @PutMapping(path="/{userId}")
    @CrossOrigin
    public @ResponseBody User updateUser(@PathVariable Long userId, @RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam Boolean stayLoggedIn, @RequestParam Long newId){
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        User toUpdate = userRepository.findByID(userId);
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


}
