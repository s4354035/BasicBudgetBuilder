package com.basicBudgetBuilder.service;

import com.basicBudgetBuilder.domain.User;
import com.basicBudgetBuilder.exceptions.BasicBudgetBuilderException;
import com.basicBudgetBuilder.representation.FullTextSearchEntity;

import java.util.List;

/**
 * Created by Hanzi Jing on 6/05/2017.
 */
public interface FullTextSearchService {
    List<FullTextSearchEntity>fullTextSearch(User user, String text)throws BasicBudgetBuilderException;;
}
