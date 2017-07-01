package com.basicBudgetBuilder.repository;

import com.basicBudgetBuilder.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Hanzi Jing on 4/28/2017.
 */

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, String> {
    PasswordResetToken findByToken(String token);
    @Transactional
    void deleteByUserId(long userId);
}