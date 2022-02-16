package com.kindminds.drs.service.util;


import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component("mailUtil")
public class MailUtil {
	
	@Autowired private JavaMailSender mailSender ;
	@Autowired private MessageSource messageSource;

	public enum SignatureType { NORMAL,NO_REPLY }

	public void Send(String toAddress,String from, String subject, String msgBody) {
		System.out.println(subject);
		String [] toAddresses = {toAddress};
		this.processSend(toAddresses,from, subject, msgBody);
	}

	public void Send(String [] toAddress,String from, String subject, String msgBody) {
		System.out.println(subject);
		this.processSend(toAddress,from, subject, msgBody);
	}

	private void processSend(String [] toAddress,String from,String subject, String msgBody){

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(toAddress);
		msg.setSubject(subject);
		msg.setText(msgBody);
		mailSender.send(msg);

	}

	public void SendMime(String [] to,String from, String subject, String msgBody) {
		this.processSendMime(to, null, from, subject, msgBody);
	}

	public void SendMimeWithBcc(String [] to, String[] bcc, String from, String subject, String msgBody) {
		Assert.notNull(bcc);
		this.processSendMime(to, bcc, from, subject, msgBody);
	}

	private void processSendMime(String [] to,String [] bcc, String from, String subject, String msgBody){


		MimeMessage mimeMsg = this.mailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(mimeMsg, true, "UTF-8");
			messageHelper.setFrom(from);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(msgBody,true);
			if(bcc!=null) messageHelper.setBcc(bcc);
			this.mailSender.send(mimeMsg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public String appendSignature(String originBody,String signature){
			return originBody + signature;
	}

	public String getSignature(SignatureType type){


		if(type==SignatureType.NO_REPLY){
			 return this.messageSource.getMessage("mail.signatureNoReply", null,Locale.ENGLISH);
		}else{
			 return this.messageSource.getMessage("mail.signatureNormal", null,Locale.TAIWAN);
		}

	}

	public String getSignature(SignatureType type, Locale locale){
		if(type==SignatureType.NO_REPLY){
			return this.messageSource.getMessage("mail.signatureNoReply", null, locale);
		}else{
			return this.messageSource.getMessage("mail.signatureNormal", null, locale);
		}
	}
	
}