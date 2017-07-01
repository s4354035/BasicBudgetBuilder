package com.basicBudgetBuilder.domain;

/**
 * Created by Hanzi Jing on 3/04/2017.
 */

import com.basicBudgetBuilder.representation.UserRep;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;

    public User(){
    }
    public User(String email, String password, Role role, String name){

        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
    }
    public User(UserRep userRep){
        if(userRep != null){
            this.email = userRep.getEmail();
            this.password = userRep.getPassword();
            this.role = Role.USER;
            this.name = userRep.getName();
        }
    }
}