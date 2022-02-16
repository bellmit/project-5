package com.kindminds.drs.core.query;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommonQuerySrv {

    @Autowired
    private CommonQueries cq;

    public Map<String, String> getAllCompanyKcodeToNameMap() {

        return this.cq.queryAllCompanyKcodeToShortEnUsNameMap();
    }


    public Map<String, String> getSellerKcodeToNameMap() {
        return this.cq.querySupplierKcodeToShortEnUsNameMap();
    }

}
