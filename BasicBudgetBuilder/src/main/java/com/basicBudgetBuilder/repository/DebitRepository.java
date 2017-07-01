package com.basicBudgetBuilder.repository;

/**
 * Created by Hanzi Jing on 9/04/2017.
 */

import com.basicBudgetBuilder.domain.Category;
import com.basicBudgetBuilder.domain.Debit;
import com.basicBudgetBuilder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebitRepository extends JpaRepository<Debit, Long> {
    List<Debit>findByUser(User user);
    List<Debit>findByUserAndCategoryIn(User user, List<Category> categories);
    Debit findById(long id);
}

