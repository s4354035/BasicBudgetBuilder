package com.basicBudgetBuilder.repository;

/**
 * Created by Hanzi Jing on 8/05/2017.
 */

import com.basicBudgetBuilder.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
/**
 * For future usage deleting a whole user
 */
public class DeleteDataForUserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void deleteAllForUser(User user){
        jdbcTemplate.update("DELETE FROM attachment_body WHERE attachment_id in" +
                "( SELECT a.id FROM attachment AS a WHERE a.debit_id in ( SELECT d.id from debit AS d WHERE user_id = ? ))", user.getId());
        jdbcTemplate.update("DELETE FROM attachment WHERE debit_id in ( SELECT d.id from debit AS d WHERE user_id = ? )", user.getId());
        jdbcTemplate.update("DELETE FROM autodebit WHERE user_id = ?", user.getId());
        jdbcTemplate.update("DELETE FROM debit WHERE user_id = ?", user.getId());
        jdbcTemplate.update("DELETE FROM budget WHERE user_id = ?", user.getId());
        jdbcTemplate.update("DELETE FROM category WHERE user_id = ?", user.getId());
        jdbcTemplate.update("DELETE FROM password_reset_token WHERE user_id = ?", user.getId());
        jdbcTemplate.update("DELETE FROM user WHERE id = ?", user.getId());
    }
}
