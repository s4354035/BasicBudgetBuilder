package com.basicBudgetBuilder.repository;

/**
 * Created by Hanzi Jing on 9/04/2017.
 */

import com.basicBudgetBuilder.domain.AutoDebit;
import com.basicBudgetBuilder.domain.Category;
import com.basicBudgetBuilder.domain.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AutoDebitCustomerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    // Lambda express implementation of row mapper interface
    private RowMapper<AutoDebit> rowMapper = (rs, rowNumber) -> {
        Category category = new Category(rs.getLong("category_id"),
                rs.getString("name"), rs.getString("colour"));
        return new AutoDebit(rs.getLong("id"),
                rs.getString("description"),
                rs.getBigDecimal("amount"),
                Interval.values()[rs.getInt("debit_interval")],
                category);
    };
    private String sqlQuery =
            "SELECT d.category_id, category.colour, category.name, d.id, d.description, d.amount, d.debit_interval " +
                    "FROM autodebit AS d " +
                    "LEFT JOIN category ON d.category_id=category.id " +
                    "WHERE d.user_id = :uid AND d.category_id in ( :ids ) ";

    /**
     * namedJdbcTemplate was used used to handle multiple entries using in
     * @param userId
     * @param categoryIds
     * @return
     */
    public List<AutoDebit> findAutoDebitByCategory(long userId, List<Long> categoryIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", categoryIds);
        parameters.addValue("uid", userId);
        return namedJdbcTemplate.query(sqlQuery, parameters, rowMapper);
    }

    /**
     * for deleting a while account was not implemented
     * @param userId
     */
    public void deleteByUser(long userId) {
        jdbcTemplate.update("delete from autodebit  where user_id = ? ", userId);
    }
}
