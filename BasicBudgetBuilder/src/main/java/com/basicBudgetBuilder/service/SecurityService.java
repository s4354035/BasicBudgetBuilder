package com.basicBudgetBuilder.service;

/**
 * Created by Hanzi Jing on 3/04/2017.
 */
public interface SecurityService{
    public String findLoggedInUsername();
    public void autologin(String username, String password);
}