package com.kindminds.drs.service.usecase.product;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.product.ConfirmMarketingMaterialUco;
import com.kindminds.drs.api.v1.model.product.ConfirmMarketingMaterialComment;
import com.kindminds.drs.api.data.access.usecase.product.ConfirmMarketingMaterialDao;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.MailUtil.SignatureType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service("confirmMarketingMaterialUco")
public class ConfirmMarketingMaterialUcoImpl implements ConfirmMarketingMaterialUco {

	@Autowired private ConfirmMarketingMaterialDao dao;
	@Autowired private MailUtil mailUtil;
	@Autowired private com.kindminds.drs.api.data.access.rdb.UserDao UserDao;
	@Autowired private MessageSource messageSource;

	private static final String ADDRESS_NO_REPLY = "DRS.network <drs-noreply@tw.drs.network>";

	private static final int DEFAULT_CONFIRM = 0;
	private static final int DRS_MSG_ADDED = 1;
	private static final int SUPPLIER_RESPONDED = 2;
	private static final int PENDING_ONE_DAY = 3;

	private static final String PENDING_SUPPLIER_ACTION = "PENDING_SUPPLIER_ACTION";
	private static final String PENDING_DRS_ACTION = "PENDING_DRS_ACTION";
	private static final String CONTENT_MANAGERS = "content.manager@us.drs.network";
	private static final String ACCOUNT_MANAGERS = "account.managers@tw.drs.network";
	private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";

	//	@Scheduled(cron = "0 0 0 1/1 * ? *", zone="Asia/Taipei")
	public void autoRenotifySuppliers() {
		for (Object[] data : dao.queryProductBaseListToRenotify()) {
			String marketplace = Marketplace.getCountryFromId((Integer)data[0]);
			sendEmail(PENDING_ONE_DAY,
					marketplace, (String)data[1], (String)data[2], (String)data[3]);
		}
	}

	@Override
	public void sendEmail(int emailType, String marketplace, String productBaseCode,
						  String productCodeName, String supplierKCode) {
		int marketplaceId = Marketplace.getIdFromCountry(marketplace);
		List<String> emailList = new ArrayList<>();
		if (emailType == DEFAULT_CONFIRM && !dao.marketingMaterialMarketsideExists(marketplaceId, productBaseCode)) {
			dao.insertMarketingMaterialMarketside(marketplaceId, productBaseCode, productCodeName, supplierKCode);
		}
		if (emailType == SUPPLIER_RESPONDED) {
			int drsStaffId = dao.queryDrsStaffId(marketplaceId, productBaseCode);
			if (drsStaffId == 0) {
				emailList.add(CONTENT_MANAGERS);
			} else {
				emailList.add(UserDao.queryUserMail(drsStaffId));
			}
		} else {
			emailList = getSupplierEmailsByBaseCode(productBaseCode);
		}

		Config config = ConfigFactory.load("application.conf");
		boolean sendNotify = config.getBoolean("drs.sendNotify");
		if (sendNotify) {
			createEmail(emailType, emailList, marketplace, productBaseCode, productCodeName, supplierKCode);
		}
	}

	private List<String> getSupplierEmailsByBaseCode(String productBaseCode) {
		return dao.querySupplierEmailsByBaseCode(productBaseCode);
	}

	private void createEmail(int type, List<String> emails, String marketplace, String productBaseCode, String productCodeName, String supplierKCode) {
		Object[] emailArgs = {productBaseCode,productCodeName,marketplace,supplierKCode};
		if (emails == null || emails.isEmpty()) return;

		try {
			Locale userLocale = Locale.US;
			String[] mailTo = emails.toArray(new String[0]);
			String[] cc = {CONTENT_MANAGERS};
			String bodySrc = "", subjectSrc = "";
			if (type == DEFAULT_CONFIRM) {
				subjectSrc = "confirmMarketingMaterial.confirmSubject";
				bodySrc = "confirmMarketingMaterial.confirmBody";
			} else if (type == DRS_MSG_ADDED) {
				subjectSrc = "confirmMarketingMaterial.msgAddedSubject";
				bodySrc = "confirmMarketingMaterial.msgAddedBody";
			} else if (type == SUPPLIER_RESPONDED) {
				subjectSrc = "confirmMarketingMaterial.splrResponseSubject";
				bodySrc = "confirmMarketingMaterial.splrResponseBody";
				cc = new String[]{ACCOUNT_MANAGERS};
			} else if (type == PENDING_ONE_DAY) {
				subjectSrc = "confirmMarketingMaterial.pendingDaySubject";
				bodySrc = "confirmMarketingMaterial.pendingDayBody";
				cc = new String[]{};
			}
			String subject = messageSource.getMessage(subjectSrc, emailArgs, userLocale);
			String body = messageSource.getMessage(bodySrc, emailArgs, userLocale);
			String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);
			body += signature;

			this.mailUtil.SendMimeWithBcc(mailTo, cc, ADDRESS_NO_REPLY, subject, body);
		} catch (Exception e) {
			this.mailUtil.Send(SOFTWARE_ENGINEERS, ADDRESS_NO_REPLY, "Default supplier marketing material confirmation Reminder email ERROR", e.getMessage() + "\n\n" + e.toString() + "\n\n" + e.fillInStackTrace());
		}
	}

	@Override
	public void addComment(int userId, String marketplace, String productBaseCode, String supplierKCode,
						   String contents, String productCodeName) {
		int marketplaceId = Marketplace.getIdFromCountry(marketplace);
		if (!dao.marketingMaterialMarketsideExists(marketplaceId, productBaseCode)) {
			dao.insertMarketingMaterialMarketside(marketplaceId, productBaseCode,
					productCodeName, supplierKCode);
		}

		Integer materialId = dao.queryMarketingMaterialMarketsideId(marketplaceId, productBaseCode);
		dao.insertComment(materialId, marketplaceId, productBaseCode, userId, contents);

		if (UserDao.isDrsUser(userId)) {
			dao.updateDrsStaffId(userId, marketplaceId, productBaseCode);
			sendEmail(DRS_MSG_ADDED, marketplace, productBaseCode, productCodeName, supplierKCode);
			dao.updateMarketingMaterialStatus(PENDING_SUPPLIER_ACTION, true, marketplaceId, productBaseCode);
		} else if (UserDao.isSupplier(userId)) {
			sendEmail(SUPPLIER_RESPONDED, marketplace, productBaseCode, productCodeName, supplierKCode);
			dao.updateMarketingMaterialStatus(PENDING_DRS_ACTION, false, marketplaceId, productBaseCode);
		}
	}

	@Override
	public List<ConfirmMarketingMaterialComment> getComments(int marketplaceId, String productBaseCode) {
		return dao.queryCommentByProductBaseCode(marketplaceId, productBaseCode);
	}


}