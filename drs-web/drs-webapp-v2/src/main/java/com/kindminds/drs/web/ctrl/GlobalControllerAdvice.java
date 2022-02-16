package com.kindminds.drs.web.ctrl;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import akka.actor.ActorRef;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.web.actor.NotificationHandler;
import com.kindminds.drs.web.config.DrsActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.service.util.SpringAppCtx;



@ControllerAdvice
public class GlobalControllerAdvice {
	
	@Autowired
	private UserDao userRepo;
	@ModelAttribute
	 public void addAttributes(Model model, HttpServletRequest request, HttpServletResponse response) {
		try {

			UserInfo u = Context.getCurrentUser();
			model.addAttribute("user", u);
			model.addAttribute("userEmail", userRepo.queryUserMail(u.getUserId()));

			 Properties auth = (Properties)SpringAppCtx.get().getBean("authProperties");
			 model.addAttribute("auth", auth);
			 Locale locale = Context.getCurrentUser().getLocale();
			 model.addAttribute("userLocale", locale);
				LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
				resolver.setLocale(request, response, locale);		
			 model.addAttribute("userLocaleZendesk", locale.toString().replace("_", "-"));

			ActorRef nh = DrsActorSystem.actorSystem.actorOf(NotificationHandler.props(), "NotificationHandler");


		} catch (Exception e) {
		}
	}
}
