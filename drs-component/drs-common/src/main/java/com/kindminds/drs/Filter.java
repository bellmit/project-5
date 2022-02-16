package com.kindminds.drs;

import java.util.ArrayList;
import java.util.List;

public class Filter{

    private List criteriaList = new ArrayList<Criteria>();

    public void addCriteria(Criteria c ){
        this.criteriaList.add(c);
    }

    public List<Criteria> getCriteriaList()   {
        return criteriaList;
    }

}
