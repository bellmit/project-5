package com.kindminds.drs.persist.data.access.nosql.mongo.dto.product;


import java.util.List;

public class Theme {

    private String theme;

    private List<VariableBp> variables;

    public Theme() {
    }

    public Theme(String theme, List<VariableBp> variables) {
        this.theme = theme;
        this.variables = variables;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String name) {
        this.theme = name;
    }

    public List<VariableBp> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableBp> variables) {
        this.variables = variables;
    }
}

