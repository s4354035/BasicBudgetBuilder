package com.basicBudgetBuilder.Service;

import com.basicBudgetBuilder.domain.*;
import com.basicBudgetBuilder.exceptions.BasicBudgetBuilderException;
import com.basicBudgetBuilder.repository.BudgetRepository;
import com.basicBudgetBuilder.repository.CategoryRepository;
import com.basicBudgetBuilder.representation.BudgetRep;
import com.basicBudgetBuilder.service.BudgetService;
import com.basicBudgetBuilder.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Hanzi Jing on 15/+04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class BudgetServiceTest{
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetRepository budgetRepository;


    private User user = null;

    @Before
    public void setup()throws Exception {
        removeAll();
        String email = "test";
        String password = "password";
        String name = "Tester";
        user = userService.findByEmail(email);
        if(user == null) {
            user = new User(email, password, Role.USER, name);
            userService.save(user);
        }
    }

    private void removeAll() throws BasicBudgetBuilderException{
        for (Budget b : budgetRepository.findAll()) {
            budgetRepository.delete(b.getId());
        }
        for (Category c : categoryRepository.findAll()) {
            categoryRepository.delete(c.getId());
        }
    }

    @Test
    public void Test() throws BasicBudgetBuilderException {
        //normal create budget scenario
        BudgetRep budgetRep = new BudgetRep("Green", "#00FF00", "It's Green",
                BigDecimal.valueOf(141.2), Interval.WEEK,  "2017-12-14");
        budgetRep = budgetService.create(budgetRep, user);

        //Successful overlay of old budget scenario
        BudgetRep budgetRep0 = new BudgetRep("Green", "#00FF00", "It's Still Green",
                BigDecimal.valueOf(371.00), Interval.FORTNIGHT,  "2017-12-24");
        budgetRep0 = budgetService.create(budgetRep0, user);

        //Category name clash Ignored Entry Scenario
        BudgetRep budgetRep1 = new BudgetRep("Green", "#0000FF", "It's Blue",
                BigDecimal.valueOf(630.7), Interval.MONTH,"2017-11-23");
        budgetRep1 = budgetService.create(budgetRep1, user);

        //Successful no description Scenario
        BudgetRep budgetRep2 = new BudgetRep("Red", "#FF0000", null,
                BigDecimal.valueOf(1830.7), Interval.QUARTER, "2017-11-23");
        budgetRep2 = budgetService.create(budgetRep2, user);

        // Add a group of Budget entries
        BudgetRep budgetRep3 = new BudgetRep("Blue", "#0000FF", "It's Blue",
                BigDecimal.valueOf(412.22), Interval.MONTH,  "2017-11-14");
        budgetRep3 = budgetService.create(budgetRep3, user);

        BudgetRep budgetRep4 = new BudgetRep("Cyan", "#00FFFF", "It's Cyan",
                BigDecimal.valueOf(45.72), Interval.WEEK,  "2012-12-14");
        budgetRep4 = budgetService.create(budgetRep4, user);

        BudgetRep budgetRep5 = new BudgetRep("Yellow", "#FFFF00", "It's Yellow",
                BigDecimal.valueOf(27146.5), Interval.YEAR,  "2017-12-14");
        budgetRep5 = budgetService.create(budgetRep5, user);

        // successful edit of an entry
        BudgetRep budgetRep6 = new BudgetRep("Gold", "#FFFF11", "It's not Yellow",
                BigDecimal.valueOf(962.30), Interval.MONTH,  "2017-10-12");
        budgetRep6.setId(budgetRep5.getId());
        budgetService.edit(budgetRep6, user);

        BudgetRep budgetRep7 = new BudgetRep("Cyan", "#00FFFF", "It's still Cyan",
                BigDecimal.valueOf(22.32), Interval.WEEK,  "2012-12-14");
        budgetRep7.setId(budgetRep4.getId());
        budgetService.edit(budgetRep7, user);

        //create and then delete an entry
        BudgetRep budgetRep8 = new BudgetRep("Pink", "#EE5555", "It's pink",
                BigDecimal.valueOf(1111.11), Interval.YEAR,  "2017-03-12");
        budgetRep8 = budgetService.create(budgetRep8, user);
        Assert.assertTrue(budgetService.delete(budgetRep8.getId()));
        Assert.assertNull(budgetRepository.findById(budgetRep8.getId()));

    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionClash()throws BasicBudgetBuilderException {

        //set up budget entry
        BudgetRep budgetRep0 = new BudgetRep("Green", "#00FF00", "It's Still Green",
                BigDecimal.valueOf(371.00), Interval.FORTNIGHT, "2017-12-24");
        budgetService.create(budgetRep0, user);

        //Category colour clash Scenario
        BudgetRep budgetRep = new BudgetRep("Blue", "#00FF00", "It's Blue",
                BigDecimal.valueOf(111.11), Interval.FORTNIGHT, "2015-02-11");
        budgetService.create(budgetRep, user);
    }

    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionDuplicate()throws BasicBudgetBuilderException {
        BudgetRep budgetRep = new BudgetRep("Green", "#00FF00", "It's Green",
                BigDecimal.valueOf(141.2), Interval.WEEK, "2017-12-14");
        budgetRep = budgetService.create(budgetRep, user);

        //Duplicated name and date Scenario
        BudgetRep budgetRep0 = new BudgetRep("Green", "#00FF01", "It's also Green",
                BigDecimal.valueOf(111.11), Interval.WEEK, "2017-12-14");
        budgetService.create(budgetRep0, user);
    }

    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionNullCategory()throws BasicBudgetBuilderException {
        BudgetRep budgetRep1 = new BudgetRep(null, "#0201A9", "It's Blue",
                BigDecimal.valueOf(111.11), Interval.MONTH, "2017-12-14");
        budgetService.create(budgetRep1, user);
    }

    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionNullColour()throws BasicBudgetBuilderException {
        BudgetRep budgetRep2 = new BudgetRep("Magenta", null, "It's Magenta",
                BigDecimal.valueOf(111.11), Interval.QUARTER, "2017-12-14");
        budgetService.create(budgetRep2, user);
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionNullAmount()throws BasicBudgetBuilderException {
        BudgetRep budgetRep3 = new BudgetRep("White", "#fCFfFc", "It's White",
                BigDecimal.valueOf(0), Interval.YEAR, "2017-12-14");
        budgetService.create(budgetRep3, user);
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionNullInterval()throws BasicBudgetBuilderException {
        BudgetRep budgetRep4 = new BudgetRep("Black", "#030504", "It's Black",
                BigDecimal.valueOf(111.11), null, "2017-12-14");
        budgetService.create(budgetRep4, user);
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionNullDate()throws BasicBudgetBuilderException {
        BudgetRep budgetRep5 = new BudgetRep("Cyan", "#00FEfe", "It's Cyan",
                BigDecimal.valueOf(111.11), Interval.WEEK, null);
        budgetService.create(budgetRep5, user);
    }
    @Test(expected=NullPointerException.class)
    public void addTestExceptionNullUser()throws BasicBudgetBuilderException {
        BudgetRep budgetRep6 = new BudgetRep("Yellow", "#56789a", "It's Yellow",
                BigDecimal.valueOf(111.11), Interval.YEAR, "2017-12-14");
        budgetService.create(budgetRep6, null);
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionNullBudget()throws BasicBudgetBuilderException {
        budgetService.create(null, user);
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionInvalidColour()throws BasicBudgetBuilderException {
        BudgetRep budgetRep9 = new BudgetRep("Unknown", "#FFFG00", "It's Unknown",
                BigDecimal.valueOf(111.11), Interval.YEAR,  "2017-12-14");
        budgetService.create(budgetRep9, user);
    }
}