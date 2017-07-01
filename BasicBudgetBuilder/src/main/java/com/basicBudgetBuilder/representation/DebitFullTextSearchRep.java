package com.basicBudgetBuilder.representation;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Hanzi Jing on 8/04/2017.
 */
@Data
public class DebitFullTextSearchRep implements FullTextSearchEntity {
    private final FullTextEntityType type = FullTextEntityType.DEBIT;
    private DebitRep debitRep;
    private double score;
    private String description;

    public DebitFullTextSearchRep(){}
    public DebitFullTextSearchRep(double score,
                                  String categoryName,
                                  String categoryColour,
                                  String description,
                                  BigDecimal amount,
                                  String date){
        this.score = score;
        this.description =  "\t" + categoryName + " (" + description + ") " + "; Amount: " + amount.toString() + "; Date: " + date;
        this.debitRep = new DebitRep(
                categoryName,
                categoryColour,
                description,
                amount,
                date
        );
    }
    public DebitFullTextSearchRep(double score,
                                  long id,
                                  long userId,
                                  long categoryId,
                                  String categoryName,
                                  String categoryColour,
                                  String description,
                                  BigDecimal amount,
                                  String date){
        this.score = score;
        this.description =  "\t" + categoryName + " (" + description + ") " + "; Amount: " + amount.toString() + "; Date: " + date;
        this.debitRep = new DebitRep(
                id,
                userId,
                categoryId,
                categoryName,
                categoryColour,
                description,
                amount,
                date
        );
    }
}
