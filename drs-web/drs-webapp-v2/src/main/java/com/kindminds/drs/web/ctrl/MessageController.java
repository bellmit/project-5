package com.kindminds.drs.web.ctrl;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MessageController {
	
	@RequestMapping(value="/accessDeny")
    public String accessDeny(Model model, HttpServletRequest request) {
    	return "accessDeny";
    }
	
	/*
	@RequestMapping(value="/error")
    public String error(Model model, HttpServletRequest request) {
		model.addAttribute("status",request.getAttribute("javax.servlet.error.status_code"));
		Exception ex = (Exception)request.getAttribute("javax.servlet.error.exception");
		StringWriter errors = new StringWriter();
		if(ex!=null){
			ex.printStackTrace(new PrintWriter(errors));
			ex.printStackTrace();
		}
		model.addAttribute("reason", ex!=null?errors.toString():"Web page not found");
    	return "error";
    }*/
}
