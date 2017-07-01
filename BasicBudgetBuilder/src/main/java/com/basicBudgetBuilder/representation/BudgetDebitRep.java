package com.basicBudgetBuilder.representation;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Budegt debit for report
 * Created by Hanzi Jing on 9/05/2017.
 */
@Data
public class BudgetDebitRep {
    private BigDecimal budget;
    private BigDecimal debit;

    public void setBudget(BigDecimal budget){
        this.budget = budget.setScale(2, RoundingMode.HALF_UP);
    }

    public void setDebit(BigDecimal debit){
        this.debit = debit.setScale(2, RoundingMode.HALF_UP);
    }

}
