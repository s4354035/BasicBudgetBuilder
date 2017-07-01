package com.basicBudgetBuilder.service;

import com.basicBudgetBuilder.domain.User;
import com.basicBudgetBuilder.exceptions.BasicBudgetBuilderException;
import com.basicBudgetBuilder.repository.FullTextSearchCustomerRepository;
import com.basicBudgetBuilder.representation.FullTextSearchEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by Hanzi Jing on 16/04/2017.
 */
@Repository
@Service
public class FullTextSearchServiceImpl implements FullTextSearchService {
    @Autowired
    FullTextSearchCustomerRepository repository;

    public List<FullTextSearchEntity>fullTextSearch(User user, String text) throws BasicBudgetBuilderException {
        Map<String, String> errors = Maps.newHashMap();
        List<FullTextSearchEntity>result = Lists.newArrayList();
        try{
            List<FullTextSearchEntity> debits = repository.fullTextSearchDebit(user.getId(), text);
            result.addAll(debits);
            result.sort((o1, o2)->{
                    if(o1.getScore() < o2.getScore()){
                        return 1;
                    }
                    else if(o1.getScore() > o2.getScore()){
                        return -1;
                    }
                    return 0;
                });

        }catch (DataAccessException e){
            errors.put("general", e.getMessage());
            throw new BasicBudgetBuilderException(errors);
        }
        return result;
    }
}
