package com.basicBudgetBuilder.service;

import com.basicBudgetBuilder.domain.*;
import com.basicBudgetBuilder.exceptions.BasicBudgetBuilderException;
import com.basicBudgetBuilder.repository.AutoDebitRepository;
import com.basicBudgetBuilder.repository.CategoryRepository;
import com.basicBudgetBuilder.repository.DebitCustomerRepository;
import com.basicBudgetBuilder.repository.DebitRepository;
import com.basicBudgetBuilder.representation.BudgetRep;
import com.basicBudgetBuilder.representation.DebitRep;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Hanzi Jing on 16/04/2017.
 */
@Repository
@Service
public class DebitServiceImpl implements DebitService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DebitRepository debitRepository;

    @Autowired
    private DebitCustomerRepository debitCustomerRepository;

    @Autowired
    private AutoDebitRepository autoDebitRepository;

    /**
     * @param user
     * @return
     * @throws BasicBudgetBuilderException
     */
    public List<DebitRep> getByCategories(User user, List<BudgetRep> budgetReps) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        try {
            if (budgetReps == null || budgetReps.isEmpty()) {
                return getAll(user);
            }
            return getForCategoryIds(user,
                    budgetReps.stream().map(BudgetRep::getCategoryId).collect(Collectors.toList()));
        } catch (DataAccessException e) {
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
    }

    private List<DebitRep>getForCategoryIds(User user, List<Long>categoryIds) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        try {
            List<Debit> debits = debitCustomerRepository.findDebitByCategories(user.getId(), categoryIds);
            List<DebitRep> debitReps = Lists.newArrayList();
            for (Debit debit : debits) {
                DebitRep debitRep = new DebitRep(
                        debit.getId(),
                        user.getId(),
                        debit.getCategory().getId(),
                        debit.getCategory().getName(),
                        debit.getCategory().getColour(),
                        debit.getDescription(),
                        debit.getAmount(),
                        debit.getDate().toString());
                debitReps.add(debitRep);
            }
            return debitReps;
        } catch (DataAccessException e) {
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
    }


    private List<DebitRep> getAll(User user) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        try {
            List<Debit> debits = debitRepository.findByUser(user);
            List<DebitRep> debitReps = Lists.newArrayList();
            for (Debit debit : debits) {
                DebitRep debitRep = new DebitRep(
                        debit.getId(),
                        user.getId(),
                        debit.getCategory().getId(),
                        debit.getCategory().getName(),
                        debit.getCategory().getColour(),
                        debit.getDescription(),
                        debit.getAmount(),
                        debit.getDate().toString());
                debitReps.add(debitRep);
            }
            return debitReps;
        } catch (DataAccessException e) {
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
    }

//    private List<DebitRep> getByCategory(User user, Category category) throws BasicBudgetBuilderException {
//        Map<String, String> errors = Maps.newHashMap();
//        try {
//            List<Debit> debits = debitCustomerRepository.findDebitByCategory(user.getId(), category.getId());
//            List<DebitRep> debitReps = Lists.newArrayList();
//            for (Debit debit : debits) {
//                DebitRep debitRep = new DebitRep(
//                        debit.getId(),
//                        user.getId(),
//                        debit.getCategory().getId(),
//                        debit.getCategory().getName(),
//                        debit.getCategory().getColour(),
//                        debit.getDescription(),
//                        debit.getAmount(),
//                        debit.getDate().toString());
//                debitReps.add(debitRep);
//            }
//            return debitReps;
//        } catch (DataAccessException e) {
//            errors.put("general", e.getMessage());
//            throw new BasicBudgetBuilderException(errors);
//        }
//    }

    public BigDecimal getCurrentSpending(User user, Budget budget) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        try {
            float spent = 0;
            List<Debit> debits = debitCustomerRepository.findDebitByCategoryAndInterval(user.getId(), budget.getCategory().getId(), budget.getBudgetInterval());
            for (Debit debit : debits) {
                spent += debit.getAmount().floatValue();
            }
            return new BigDecimal(spent);
        } catch (DataAccessException e) {
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
    }

    public DebitRep create(DebitRep debitRep, User user) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        try {
            Category category = validate(debitRep, user);
            AutoDebit autoDebit = autoDebitRepository.findById(debitRep.getAutoDebitId());
            Debit debit = new Debit(
                    debitRep.getDescription(),
                    debitRep.getAmount(),
                    Date.valueOf(debitRep.getDate()),
                    user,
                    category,
                    autoDebit);
            debit = debitRepository.save(debit);
            debitRep.setId(debit.getId());
            debitRep.setUserId(debit.getUser().getId());
            return debitRep;
        } catch (DataAccessException e) {
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
    }

    public DebitRep edit(DebitRep debitRep, User user) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        try {
            Category category = validate(debitRep, user);
            AutoDebit autoDebit = autoDebitRepository.findById(debitRep.getAutoDebitId());
            Debit debit = new Debit(
                    debitRep.getId(),
                    debitRep.getDescription(),
                    debitRep.getAmount(),
                    Date.valueOf(debitRep.getDate()),
                    category,
                    autoDebit);
            debit.setUser(user);
            debit = debitRepository.save(debit);
            debitRep.setId(debit.getId());
            debitRep.setUserId(debit.getUser().getId());
            return debitRep;
        } catch (DataAccessException e) {
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
    }

    public Boolean delete(long id) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        try {
            debitRepository.delete(id);
        } catch (Exception e) {
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
        return true;
    }

    private Category validate(DebitRep debitRep, User user) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        Category category = null;
        if (debitRep == null) {
            errors.put("general", "No Input Data");
            throw new BasicBudgetBuilderException(errors);
        }
        if (debitRep.getCategoryName() == null || debitRep.getCategoryName().trim().isEmpty() || categoryRepository.findByNameAndUser(debitRep.getCategoryName(), user) == null) {
            category = categoryRepository.findByNameAndUser("Uncategorized", user);
            debitRep.setCategoryName(category.getName());
            debitRep.setCategoryId(category.getId());
            debitRep.setCategoryColour(category.getColour());
        } else {
            category = categoryRepository.findByNameAndUser(debitRep.getCategoryName(), user);
            debitRep.setCategoryName(category.getName());
            debitRep.setCategoryId(category.getId());
            debitRep.setCategoryColour(category.getColour());
        }
        if (debitRep.getAmount() == null || debitRep.getAmount().floatValue() == 0) {
            errors.put("amount", "must not be zero");
        }
        if (debitRep.getDate() == null) {
            debitRep.setDate(LocalDate.now().toString());
        }
        if (!errors.isEmpty()) {
            throw new BasicBudgetBuilderException(errors);
        }
        return category;
    }

}
