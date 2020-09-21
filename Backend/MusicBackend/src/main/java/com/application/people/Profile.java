package com.application.people;

//TODO Attach profiles to users
//Not fully implemented, just here to demonstrate merge-requests
public class Profile {
    private String location;
    private String phoneNumber;
    private String profilePicture;

    public Profile(){
        location = null;
        phoneNumber = null;
        profilePicture=null;
    }

    public Profile(String location, String phoneNumber, String profilePicture){
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
    }

    public String getLocation(){ return location;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getProfilePicture(){return profilePicture;}
    public void setLocation(String location){this.location = location;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
    public void setProfilePicture(String profilePicture){this.profilePicture = profilePicture;}

}
