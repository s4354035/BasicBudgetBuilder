package com.basicBudgetBuilder.representation;

import com.basicBudgetBuilder.domain.Interval;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Hanzi Jing on 8/04/2017.
 */
@Data
public class BudgetRep {
    private long id;
    private long userId;
    private long categoryId;
    private String categoryName;
    private String categoryColour;
    private String description;
    private BigDecimal amount;
    private Interval budgetInterval;
    private String effectiveDate;
    private BigDecimal spent;
    private String colour = "#00CC00";

    public BudgetRep(){}
    public BudgetRep(long id,
                     long userId,
                     long categoryId,
                     String categoryName,
                     String categoryColour,
                     String description,
                     BigDecimal amount,
                     Interval budgetInterval,
                     String effectiveDate){
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryColour = categoryColour;
        this.description = description;
        this.amount =  amount != null ? amount.setScale(2, RoundingMode.HALF_UP) : null;
        this.budgetInterval = budgetInterval;
        this.effectiveDate = effectiveDate;
    }
    public BudgetRep(String categoryName,
                     String categoryColour,
                     String description,
                     BigDecimal amount,
                     Interval budgetInterval,
                     String effectiveDate){
        this.categoryName = categoryName;
        this.categoryColour = categoryColour;
        this.description = description;
        this.amount = amount != null ? amount.setScale(2, RoundingMode.HALF_UP) : null;
        this.budgetInterval = budgetInterval;
        this.effectiveDate = effectiveDate;
    }
    public void setAmount(BigDecimal amount){
        this.amount =  amount != null ? amount.setScale(2, RoundingMode.HALF_UP) : null;
    }
    public void setSpent(BigDecimal spent){
        if(spent != null){
            this.spent = spent.setScale(2, RoundingMode.HALF_UP);
            if(amount != null){
                double result =  this.spent.doubleValue()/amount.doubleValue();
                if(result > 1){
                    colour = "#CC0000";
                }
                else if(result > 0.9){
                    colour = "#FF5500";
                }
                else{
                    colour = "#00CC00";
                }
            }
        }
    }
}
