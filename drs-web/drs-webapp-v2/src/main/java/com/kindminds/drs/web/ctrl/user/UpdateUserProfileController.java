package com.kindminds.drs.web.ctrl.user;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kindminds.drs.api.usecase.UpdateUserProfileUco;
import com.kindminds.drs.service.util.SpringAppCtx;

@Controller
public class UpdateUserProfileController {
	@RequestMapping(value="/updatePassword", method = RequestMethod.POST)
	public void showSs2spSettleableBrowserForPoRelated(Model model,
			@RequestParam("username") String userName,
			@RequestParam("password") String password){
		ApplicationContext ctx = SpringAppCtx.get();
		//UpdateUserProfileUco uco  = (UpdateUserProfileUco)ctx.getBean("updateUserProfileUco");
		//uco.changeUserPassword(userName, password);
		return;
	}
}
