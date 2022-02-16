package com.kindminds.drs;


import java.util.ArrayList;
import java.util.List;

public  final class Criteria {
    public final QueryField field;
    public final String value;

    public Criteria(QueryField field, String value) {
        this.field = field;
        this.value = value;
    }
}


