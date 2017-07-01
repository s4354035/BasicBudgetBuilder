package com.basicBudgetBuilder.representation;

import com.basicBudgetBuilder.domain.Interval;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Hanzi Jing on 8/04/2017.
 */
@Data
public class AutoDebitFullTextSearchRep implements FullTextSearchEntity {

    private final FullTextEntityType type = FullTextEntityType.FIXED_COST;
    private double score;
    private String description;
    private AutoDebitRep autoDebitRep;

    public AutoDebitFullTextSearchRep(){
    }
    public AutoDebitFullTextSearchRep(double score,
                                        String categoryName,
                                      String categoryColour,
                                      String description,
                                      BigDecimal amount,
                                      Interval debitInterval){
        autoDebitRep = new AutoDebitRep(
                categoryName,
                categoryColour,
                description,
                amount,
                debitInterval
        );
        this.score = score;
        this.description = "\t" + categoryName + " (" + description + ") " + "; Amount: " + amount.toString() + " per " + debitInterval ;

    }

    public AutoDebitFullTextSearchRep(
            double score,
            long id,
            long userId,
            long categoryId,
            String categoryName,
            String categoryColour,
            String description,
            BigDecimal amount,
            Interval debitInterval
    ){
        this.score = score;
        this.description =  "\t" + categoryName + " (" + description + ") " + "; Amount: " + amount.toString() + " Per " + debitInterval;
        this.autoDebitRep = new AutoDebitRep(
                id,
                userId,
                categoryId,
                categoryName,
                categoryColour,
                description,
                amount,
                debitInterval
        );
    }
}
