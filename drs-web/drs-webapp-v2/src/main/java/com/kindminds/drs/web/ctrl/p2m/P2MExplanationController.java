package com.kindminds.drs.web.ctrl.p2m;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "p2mExplanation")
public class P2MExplanationController {

    @CrossOrigin
    @RequestMapping(value = "/miExplanation")
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getMarketplaceInfoExplanation(){
        return "th/explanation/MarketplaceInfoEx1";
    }

    @CrossOrigin
    @RequestMapping(value = "/paiExplanation1")
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String getProductAdvanceInfoExplanation1(){
        return "th/explanation/ProductAdvanceInfoEx1";
    }
}
