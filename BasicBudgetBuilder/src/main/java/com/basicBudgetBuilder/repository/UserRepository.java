package com.basicBudgetBuilder.repository;

/**
 * Created by Hanzi Jing on 3/04/2017.
 */

import com.basicBudgetBuilder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findById(long id);
}