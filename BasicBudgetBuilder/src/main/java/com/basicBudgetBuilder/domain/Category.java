package com.basicBudgetBuilder.domain;

/**
 * Created by Hanzi Jing on 6/04/2017.
 */

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "category",
        uniqueConstraints={
            @UniqueConstraint(columnNames = {"user_id", "name"}),
                @UniqueConstraint(columnNames = {"user_id", "colour"})
        })
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false )
    private String name;

    @Column(nullable = false)
    private String colour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category(){}

    public Category(long id, String name, String colour){
        this.id = id;
        this.name = name;
        this.colour = colour;
    }
    public Category(String name, String colour, User user){
        this.name = name;
        this.colour = colour;
        this.user = user;
    }
}
