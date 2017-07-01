package com.basicBudgetBuilder.representation;

import lombok.Data;

/**
 * Created by Hanzi Jing on 4/25/2017.
 */
@Data
public class UserRep {
    private String name;
    private String password;
    private String confirmPassword;
    private String email;
}
