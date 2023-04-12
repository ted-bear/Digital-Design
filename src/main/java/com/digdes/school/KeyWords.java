package com.digdes.school;

import java.util.List;

public class KeyWords {
    final static String OR_OPERATOR = "or";
    final static String AND_OPERATOR = "and";
    final static String WHERE_OPERATOR = "where";
    final static String VALUES_OPERATOR = "values";
    final static String INSERT_COMMAND = "insert";
    final static String UPDATE_COMMAND = "update";
    final static String DELETE_COMMAND = "delete";
    final static String SELECT_COMMAND = "select";
    final static String AGE_COLUMN = "age";
    final static String LASTNAME_COLUMN = "lastName";
    final static String ID_COLUMN = "id";
    final static String COST_COLUMN = "cost";
    final static String ACTIVE_COLUMN = "active";
    final static List<String> ALL_WORDS = List.of(OR_OPERATOR, AND_OPERATOR, WHERE_OPERATOR, VALUES_OPERATOR,
            INSERT_COMMAND, UPDATE_COMMAND, DELETE_COMMAND, SELECT_COMMAND, AGE_COLUMN,
            LASTNAME_COLUMN, ID_COLUMN, COST_COLUMN, ACTIVE_COLUMN);

}
