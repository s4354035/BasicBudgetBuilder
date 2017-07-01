package com.basicBudgetBuilder.domain;

/**
 * Created by Hanzi Jing on 6/04/2017.
 */

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@Entity
@Table(name = "budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Interval budgetInterval;

    @Column(nullable = false)
    private Date effectiveDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private String categoryName;

    public Budget(){}

    public Budget(String description,
                  BigDecimal amount,
                  Interval budgetInterval,
                  Date effectiveDate,
                  User user,
                  Category category){
        this.description = description;
        this.amount =  amount != null ? amount.setScale(2, RoundingMode.HALF_UP) : null;
        this.budgetInterval = budgetInterval;
        this.effectiveDate = effectiveDate;
        this.user = user;
        this.category = category;
        if(category!= null)
            this.categoryName = category.getName();
    }
    public Budget(long id,
                  String description,
                  BigDecimal amount,
                  Interval budgetInterval,
                  Date effectiveDate,
                  Category category) {
        this.id = id;
        this.description = description;
        this.amount =  amount != null ? amount.setScale(2, RoundingMode.HALF_UP) : null;
        this.budgetInterval = budgetInterval;
        this.effectiveDate = effectiveDate;
        this.category = category;
        if(category!= null)
            this.categoryName = category.getName();
    }
    public void setCategory(Category category){
        this.category = category;
        if(category!= null)
            this.categoryName = category.getName();
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount != null ? amount.setScale(2, RoundingMode.HALF_UP) : null;
    }
}
