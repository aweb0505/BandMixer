package com.application;
import com.application.people.UserRepository;
import com.application.people.User;
import com.application.tagging.Tag;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping(path="/users") // This means the URLs will start with /experiment
public class MainController {
    @Autowired //This will get the bean called userRepository
    //which was auto generated by spring
    private UserRepository userRepository;

    
    @PostMapping(path="/add") //Map only POST requests
    @CrossOrigin
    public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email, @RequestParam String password){
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
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    @CrossOrigin
    public @ResponseBody Iterable<User> getAllUsers(){
        //Returns a JSON or XML document with the users in it
        return userRepository.findAll();
    }

    @PostMapping(path="/remove")
    @CrossOrigin
    public @ResponseBody String removeUser(@RequestParam Long id){
        if(userRepository.findByID(id) != null){
            userRepository.delete(userRepository.findByID(id));
            return "user deleted";
        }else{
            return "user doesn't exist";
        }
    }

    @GetMapping(path="/login")
    @CrossOrigin
    public @ResponseBody String userLogin(@RequestParam String loginID, @RequestParam String password){
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        if(userRepository.findByUsername(loginID) != null){
            if(encryptor.checkPassword(password, userRepository.findByUsername(loginID).getPassword()) == true){
                return "login successful";
            }else{
                return "incorrect password";
            }
        }else if(userRepository.findByEmail(loginID) != null){
            if(encryptor.checkPassword(password, userRepository.findByEmail(loginID).getPassword()) == true){
                return "login successful";
            }else{
                return "incorrect password";
            }
        }else{
            return "no user registered under this loginID";
        }
    }


}
