package com.application.people;

import javax.persistence.*;

@Entity
@Table(name="USERS")
public class User 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String email;

    private String username;


    private String password;
    
    public User() {}

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setPassword(String password){this.password = password;}

    public String getPassword(){return password;}

    public String getName(){
        return username;
    }

    public void setName(String name){
        this.username = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

}
