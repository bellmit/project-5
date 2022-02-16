package com.kindminds.drs.service.usecase.customercare;

import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.Locale;
import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.customercare.MaintainCustomerCareCaseIssueTemplateUco;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueStatus;
import com.kindminds.drs.api.v1.model.customercare.CustomerCareCaseIssue.CustomerCareCaseIssueTemplate;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.UserDao;
import com.kindminds.drs.api.data.access.usecase.customercare.MaintainCustomerCareCaseIssueTemplateDao;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.MailUtil.SignatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service("maintainCustomerCareCaseIssueTemplateUco")
public class MaintainCustomerCareCaseIssueTemplateUcoImpl implements MaintainCustomerCareCaseIssueTemplateUco {

	@Autowired private MaintainCustomerCareCaseIssueTemplateDao dao;
	@Autowired private MailUtil mailUtil;
	@Autowired private MessageSource messageSource;
	@Autowired private CompanyDao companyRepo;
	@Autowired private UserDao userRepo;
	@Autowired @Qualifier("envProperties") private Properties env;

	private static final String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";
	private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";
	
	@Override
	public int create(CustomerCareCaseIssueTemplate template) {
		int id = this.dao.insert(template);
		this.dao.updateIssueStatus(template.getIssueId(),CustomerCareCaseIssueStatus.RESPONSE_TEMPLATE_DONE);
		this.processCreationNotificationToSupplier(id, template.getIssueId());
		return id;
	}

	private void processCreationNotificationToSupplier(int templateId,int issueId){
		boolean sendNotify = Boolean.parseBoolean(((String)this.env.get("SEND_NOTIFY")));
		if(sendNotify){
			try{
				String issueName = this.dao.queryEnUsIssueName(issueId);
				java.util.Locale locale = Context.getCurrentUser().getLocale();
				String subject = this.messageSource.getMessage("mail.Templates.templateDoneToSupplier-Subject", new Object[] {issueName}, locale);
				String body = this.messageSource.getMessage("mail.Templates.templateDoneToSupplier-Body", new Object[] {issueName,String.valueOf(templateId),String.valueOf(templateId)},locale);								
				String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);
				body = this.mailUtil.appendSignature(body,signature);
				List<String> serviceEmailList = this.companyRepo.querySupplierServiceEmailAddressList(this.dao.queryKcodeOfSupplierInIssue(issueId));
				String [] mailTo = serviceEmailList.toArray(new String[serviceEmailList.size()]);
				Assert.isTrue(this.userRepo.isDrsUser(Context.getCurrentUser().getUserId()));
				String currentUserEmail = this.userRepo.queryUserMail(Context.getCurrentUser().getUserId());
				String [] bcc = {currentUserEmail};
				this.mailUtil.SendMimeWithBcc(mailTo,bcc, ADDRESS_NO_REPLY, subject,body);
			}
			catch(Exception e){
				this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "Template Creation email ERROR", e.getMessage()+"\n\n"+e.toString()+"\n\n"+e.fillInStackTrace());
			}
			finally{
			}		
		}			
	}
		
	@Override
	public CustomerCareCaseIssueTemplate get(int id) {
		return this.dao.query(id);
	}

	@Override
	public CustomerCareCaseIssueTemplate update(CustomerCareCaseIssueTemplate template) {
		return this.dao.update(template);
	}

	@Override
	public boolean delete(int id) {
		return this.dao.delete(id);
	}

	@Override
	public List<Locale> getLocaleCodeList() {
		return Arrays.asList(Locale.values());
	}

	@Override
	public List<String> getCaseTypeList() {
		return this.dao.queryCaseTypeList();
	}

	@Override
	public List<String> getMarketRegionList() {
		List<String> countryCodeList = new ArrayList<String>();
		countryCodeList.add(Country.US.name());
		countryCodeList.add(Country.UK.name());
		countryCodeList.add(Country.DE.name());
		countryCodeList.add(Country.FR.name());
		countryCodeList.add(Country.IT.name());
		countryCodeList.add(Country.ES.name());
		countryCodeList.add(Country.CA.name());
		return countryCodeList;
	}

	@Override
	public List<Marketplace> getMarketplaceList() {
		List<Marketplace> marketplaceList = Marketplace.getAmazonMarketplaces();
		marketplaceList.add(Marketplace.TRUETOSOURCE);
		marketplaceList.add(Marketplace.EBAY_COM);
		return marketplaceList;
	}

}
