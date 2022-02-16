package com.kindminds.drs.web.ctrl;

import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.service.util.SpringAppCtx;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Controller
public class indexController {

    @RequestMapping("/th")
    public String toVue(Model model) {
        model.addAttribute("today", new Date());
        model.addAttribute("welcome", " This is thymeleaf page ");
        return "th/thhome";
    }

    @RequestMapping("/th2")
    public String toVue2(Model model) {


        model.addAttribute("today", new Date());
        model.addAttribute("welcome", " This is thymeleaf page ");
        model.addAttribute("testValue", "im on a boat");
        return "th/test";
    }

    @RequestMapping("/react2")
    public String toReact2(Model model) {

        return "th/reactIndex2";
    }

    @RequestMapping("/thtemp")
    public String toVue3(Model model) {


        model.addAttribute("today", new Date());
        model.addAttribute("welcome", " This is thymeleaf page ");
        return "th/marketingReport/SkuAdvertisingPerformanceReport";
    }

    @RequestMapping("/product")
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String toProduct(Model model) {

        return "th/reactIndex";
    }

    @RequestMapping("/product/apply")
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String toP2M(Model model) {

        return "th/reactIndex";
    }


    @RequestMapping("/h")
    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    public String toHome(Model model) {

        return "th/reactIndex";
    }




}