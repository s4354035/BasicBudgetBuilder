package com.basicBudgetBuilder.repository;

/**
 * Created by Hanzi Jing on 9/04/2017.
 */

import com.basicBudgetBuilder.domain.Budget;
import com.basicBudgetBuilder.domain.Category;
import com.basicBudgetBuilder.domain.Interval;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BudgetCustomerRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    RowMapper<Budget> rowMapper = (rs, rowNumber) -> {
        Category category = new Category(rs.getLong("category_id"),
                rs.getString("name"), rs.getString("colour"));
        Budget budget = new Budget(rs.getLong("id"),
                rs.getString("description"),
                rs.getBigDecimal("amount"),
                Interval.values()[rs.getInt("budget_interval")],
                rs.getDate("effective_date"),
                category);
        return budget;
    };

    private String sqlQuery = "SELECT b.category_id, category.colour, category.name, b.id, b.amount, " +
            "b.budget_interval, b.description,  b.effective_date\n" +
            "FROM budget AS b\n" +
            "LEFT JOIN category ON b.category_id=category.id\n" +
            "WHERE b.user_id = ? AND b.effective_date = (SELECT MAX(effective_date) FROM budget WHERE category_id = b.category_id)";

    private String sqlQuerySelected = "SELECT b.category_id, category.colour, category.name, b.id, b.amount, b.auto, b.budget_interval," +
            " b.description,  b.effective_date, b.warning\n" +
            "FROM budget AS b\n" +
            "LEFT JOIN category ON b.category_id=category.id\n" +
            "WHERE b.user_id = ? AND b.id in ( ? ) AND b.effective_date = (SELECT MAX(effective_date) FROM budget WHERE category_id = b.category_id)";

    /**
     * Gets the most recent budget entry for each category
     * @param userId
     * @return
     */
    public List<Budget> findAllByUserId(long userId) {
        return jdbcTemplate.query(sqlQuery, rowMapper, userId);
    }

    public List<Budget> findAllByUserIdAndIds(long userId, List<Long> ids) {
        return jdbcTemplate.query(sqlQuerySelected, rowMapper, userId, Joiner.on(",").join(ids));
    }
}
