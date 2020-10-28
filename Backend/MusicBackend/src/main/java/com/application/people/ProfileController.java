package com.application.people;

import com.application.View;
import com.application.tagging.TagController;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path="/profiles")
public class ProfileController {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="")
    @CrossOrigin
    public @ResponseBody Iterable<Profile> getAllProfiles(){
        return profileRepository.findAll();
    }

    @GetMapping(path= {"/{userId}"})
    @CrossOrigin
    public @ResponseBody Profile getByUserId(@PathVariable Long userId){
        User user = userRepository.findByid(userId);
        if(user != null){
            return profileRepository.findByOwner(user);
        }
        throw new ProfileController.ProfileNotFoundException("userId not valid");
    }

    @PostMapping(path="/{userId}")
    @CrossOrigin
    public String createProfileForUser(@PathVariable Long userId, @RequestBody Profile profile){
        User user = userRepository.findByid(userId);
        if(profileRepository.findByOwner(user) == null){
            Profile toAdd = new Profile(profile);
            toAdd.setTitle(profile.getTitle());
            toAdd.setIsSearch(profile.getIsSearch());
            profileRepository.save(toAdd);
            return "User profile for user ID number:" + profile.getOwner().getId() + " has been created";
        }else{
            return "User already has a profile";
        }
    }

    @PostMapping(path="/{userId}/update")
    @CrossOrigin
    public @ResponseBody String updateProfileForUser(@PathVariable Long userId, @RequestBody Profile profile){
        User user = userRepository.findByid(userId);
        Profile toUpdate = profileRepository.findByOwner(user);
        if(toUpdate != null){
            toUpdate.setLocation(profile.getLocation());
            toUpdate.setPhoneNumber(profile.getPhoneNumber());
            toUpdate.setProfilePicture(profile.getProfilePicture());
            profileRepository.save(toUpdate);
            return "user " + user.getId() + " has been updated";
        }else{
            return "invalid userId";
        }
    }


    private class ProfileNotFoundException extends IllegalArgumentException
    {
        public ProfileNotFoundException() {super();}
        public ProfileNotFoundException(String message) {super(message);}
    }
}
