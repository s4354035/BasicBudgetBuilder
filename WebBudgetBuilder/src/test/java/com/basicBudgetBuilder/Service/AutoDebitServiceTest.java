package com.basicBudgetBuilder.Service;

import com.basicBudgetBuilder.domain.*;
import com.basicBudgetBuilder.exceptions.BasicBudgetBuilderException;
import com.basicBudgetBuilder.repository.AutoDebitRepository;
import com.basicBudgetBuilder.repository.BudgetRepository;
import com.basicBudgetBuilder.repository.CategoryRepository;
import com.basicBudgetBuilder.repository.DebitRepository;
import com.basicBudgetBuilder.representation.AutoDebitRep;
import com.basicBudgetBuilder.representation.BudgetRep;
import com.basicBudgetBuilder.service.AutoDebitService;
import com.basicBudgetBuilder.service.UserService;
import org.assertj.core.util.Lists;
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
import java.util.stream.Collectors;

/**
 * Created by Hanzi Jing on 15/+04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class AutoDebitServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private AutoDebitService autoDebitService;

    @Autowired
    private AutoDebitRepository autoDebitRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private DebitRepository debitRepository;

    private User user = null;
    private Category category = null;
    private Category category0 = null;
    private Category category1 = null;
    private Category category2 = null;
    private Category category3 = null;
    private Category category4 = null;
    private Category category5 = null;

    @Before
    public void setup()throws Exception {
        removeAll();
        String email = "email";
        String password = "password";
        String name = "name";
        user = userService.findByEmail(email);
        if(user == null) {
            user = new User(email, password, Role.USER, name);
            userService.save(user);
        }
        category = categoryRepository.findByNameAndUser("Uncategorized", user);
        if(category == null) {
            category = new Category("Uncategorized", "#555555", user);
            categoryRepository.save(category);
        }
        category0 = new Category("Green", "#00FF00", user);
        category1 = new Category("Red", "#FF0000", user);
        category2 = new Category("White", "#fCFfFc", user);
        category3 = new Category("Blue", "#0000FF", user);
        category4 = new Category("Gold", "#FFFF11", user);
        category5 = new Category("Magenta", "#FF01FF", user);
        categoryRepository.save(category0);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);
    }

    private void removeAll() throws BasicBudgetBuilderException{
        for (AutoDebit a : autoDebitRepository.findAll()) {
            autoDebitRepository.delete(a.getId());
        }
        for (Debit d : debitRepository.findAll()) {
            debitRepository.delete(d.getId());
        }
        for (Budget b : budgetRepository.findAll()) {
            budgetRepository.delete(b.getId());
        }
        for (Category c : categoryRepository.findAll()) {
            categoryRepository.delete(c.getId());
        }
    }

    @Test
    public void TestSuccesses() throws BasicBudgetBuilderException {
        //normal create AutoDebit scenario
        AutoDebitRep autoDebitRep0 = new AutoDebitRep("Green", "#00FF00", "On Green",
                BigDecimal.valueOf(100.0), Interval.WEEK);
        autoDebitRep0 = autoDebitService.create(autoDebitRep0, user);
        Assert.assertTrue(autoDebitRep0.getId() > 0);
        Assert.assertTrue(autoDebitRep0.getCategoryId() > 0);
        Assert.assertTrue(autoDebitRep0.getUserId() > 0);

        //Successful no description Scenario
        AutoDebitRep autoDebitRep1 = new AutoDebitRep("Red", "#FF0000", null,
                BigDecimal.valueOf(3200), Interval.YEAR);
        autoDebitRep1 = autoDebitService.create(autoDebitRep1, user);
        Assert.assertTrue(autoDebitRep1.getId() > 0);
        Assert.assertTrue(autoDebitRep1.getCategoryId() > 0);
        Assert.assertTrue(autoDebitRep1.getUserId() > 0);

        //Test Remaining Interval Cases
        AutoDebitRep autoDebitRep2 = new AutoDebitRep("Blue", "#0000FF", "On Blue",
                BigDecimal.valueOf(400.00), Interval.MONTH);
        autoDebitRep2 = autoDebitService.create(autoDebitRep2, user);
        Assert.assertTrue(autoDebitRep2.getId() > 0);
        Assert.assertTrue(autoDebitRep2.getCategoryId() > 0);
        Assert.assertTrue(autoDebitRep2.getUserId() > 0);

        AutoDebitRep autoDebitRep3 = new AutoDebitRep("Magenta", "#FF01FF", "On Magenta",
                BigDecimal.valueOf(200.00), Interval.FORTNIGHT);
        autoDebitRep3 = autoDebitService.create(autoDebitRep3, user);
        Assert.assertTrue(autoDebitRep3.getId() > 0);
        Assert.assertTrue(autoDebitRep3.getCategoryId() > 0);
        Assert.assertTrue(autoDebitRep3.getUserId() > 0);

        AutoDebitRep autoDebitRep4 = new AutoDebitRep("White", "#fCFfFc", "On White",
                BigDecimal.valueOf(800.00), Interval.QUARTER);
        autoDebitRep4 = autoDebitService.create(autoDebitRep4, user);
        Assert.assertTrue(autoDebitRep4.getId() > 0);
        Assert.assertTrue(autoDebitRep4.getCategoryId() > 0);
        Assert.assertTrue(autoDebitRep4.getUserId() > 0);

        // Edit all values of an entry
        AutoDebitRep autoDebitRep5 = new AutoDebitRep("Gold", "#FFFF11", "On Gold",
                BigDecimal.valueOf(962.30), Interval.MONTH);
        autoDebitRep5.setId(autoDebitRep3.getId());
        autoDebitRep5 = autoDebitService.edit(autoDebitRep5, user);
        Assert.assertTrue(autoDebitRep5.getId() > 0);
        Assert.assertTrue(autoDebitRep5.getCategoryId() > 0);
        Assert.assertTrue(autoDebitRep5.getUserId() > 0);

        // Delete Selected Entry
        Assert.assertTrue(autoDebitService.delete(autoDebitRep0.getId()));
        Assert.assertNull(autoDebitRepository.findById(autoDebitRep0.getId()));

        // Get all entries
        List<Category>categories = Lists.newArrayList(category, category0, category1, category2, category3, category4, category5);
        List<AutoDebitRep> autoDebitReps= autoDebitService.getAllForCategories(user,
                categories.stream().map(a->{
                    BudgetRep rep = new BudgetRep();rep.setCategoryId(a.getId());
                    return rep;
                }).collect(Collectors.toList()));
        Assert.assertEquals(4,autoDebitReps.size());
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionAmountZero()throws BasicBudgetBuilderException {
        AutoDebitRep autoDebitRep = new AutoDebitRep("White", "#fCFfFc", "On White",
                BigDecimal.valueOf(0), Interval.MONTH);
        autoDebitService.create(autoDebitRep, user);
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void addTestExceptionNullInterval()throws BasicBudgetBuilderException {
        AutoDebitRep autoDebitRep = new AutoDebitRep("White", "#fCFfFc", "On White",
                BigDecimal.valueOf(123), null);
        autoDebitService.create(autoDebitRep, user);
    }
    @Test(expected=BasicBudgetBuilderException.class)
    public void editTestException()throws BasicBudgetBuilderException {
        // Add a AutoDebit entry
        AutoDebitRep autoDebitRep = new AutoDebitRep("Magenta", "#FF01FF", "On Magenta",
                BigDecimal.valueOf(1275.34), Interval.MONTH);
        autoDebitRep = autoDebitService.create(autoDebitRep, user);

        //Null entries scenarios
        AutoDebitRep autoDebitRep1 = new AutoDebitRep("White", "#fCefFc", "On White",
                BigDecimal.valueOf(0), Interval.MONTH);
        autoDebitRep1.setId(autoDebitRep.getId());
        autoDebitService.edit(autoDebitRep1, user);
    }
}