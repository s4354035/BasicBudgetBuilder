package com.basicBudgetBuilder.repository;

/**
 * Created by Hanzi Jing on 9/04/2017.
 */

import com.basicBudgetBuilder.domain.AutoDebit;
import com.basicBudgetBuilder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AutoDebitRepository extends JpaRepository<AutoDebit, Long> {
    List<AutoDebit>findByUser(User user);
    AutoDebit findById(long id);
}

