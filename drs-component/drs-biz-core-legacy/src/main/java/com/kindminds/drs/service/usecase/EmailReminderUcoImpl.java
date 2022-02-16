package com.kindminds.drs.service.usecase;


import com.kindminds.drs.api.usecase.EmailReminderUco;
import com.kindminds.drs.api.v1.model.accounting.SupplierLongTermStorageFee;
import com.kindminds.drs.api.data.access.rdb.EmailReminderDao;
import com.kindminds.drs.service.util.MailUtil;
import com.kindminds.drs.service.util.MailUtil.SignatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service("emailReminderUco")
public class EmailReminderUcoImpl implements EmailReminderUco {
	
	@Autowired private EmailReminderDao dao;
	@Autowired private MailUtil mailUtil;
	@Autowired private com.kindminds.drs.api.data.access.rdb.UserDao UserDao;
	
	private static final String MAIL_ADDRESS_NOREPLY = "DRS.network <drs-noreply@tw.drs.network>";
	private static final String ACCOUNT_MANAGERS = "account.managers@tw.drs.network@tw.drs.network";
	private static final String SOFTWARE_ENGINEERS = "software.engineering@drs.network";

//	@Scheduled(cron = "0 0 0 15 1,6,7,12 ?", zone="Asia/Taipei") // january,june,july,december 15 send email
	@Override
	public void automateSendLTSFReminder() {
		sendSupplierLongTermStorageFeeReminder(ACCOUNT_MANAGERS);
	}
	
	@Override
	public void sendLTSFReminderToCurrentUser(Integer currentUserID) {
		String userEmail = UserDao.queryUserMail(currentUserID);
		sendSupplierLongTermStorageFeeReminder(userEmail);
	}
	
	private void sendSupplierLongTermStorageFeeReminder(String email) {
		try {
			String subject = "Long Term Storage Fee Reminder";
			String body = createEmailBody();
			String[] mailTo = {email};
			String[] bcc = {};
			this.mailUtil.SendMimeWithBcc(mailTo, bcc, MAIL_ADDRESS_NOREPLY, subject, body);
		} catch (Exception e) {
			this.mailUtil.Send(SOFTWARE_ENGINEERS, MAIL_ADDRESS_NOREPLY, "Create message from Long Term Storage Fee Reminder email ERROR", e.getMessage() + "\n\n" + e.toString() + "\n\n" + e.fillInStackTrace());
		}
	}
	
	private String createEmailBody() {
		StringBuilder body = new StringBuilder();
		body.append("This is a reminder that long term storage fees will be charged on February 15 and August 15.<br>")
			.append("Here are the suppliers currently projected to be charged in the local currency a total long term storage fee of over $")
			.append(getFeeToSendReminder())
			.append(":<br><br><table width='75%' border='1' style='border-collapse:collapse;' align='center'>")
			.append(createTableRow("<b>Company <b>","<b>Warehouse <b>",
					"<b>Projected 6 Month Storage Fee <b>",
					"<b>Projected 12 Month Storage Fee <b>","<b>Currency <b>"));
		StringBuilder kCodes = new StringBuilder();
		String prefix = "";
		for (String kCode : getIncludedSuppliers().keySet()) {
			kCodes.append(prefix);
			prefix = ",";
			kCodes.append(kCode);
		}
		List<SupplierLongTermStorageFee> suppliers = getSuppliersOverFeeLimit(kCodes.toString());
		for (SupplierLongTermStorageFee supplier : suppliers) {
			body.append(createTableRow(supplier.getKcode(),supplier.getMarketplace(),
					supplier.getSixMonthStorageFee().toPlainString(),
					supplier.getOneYearStorageFee().toPlainString(),supplier.getCurrency()));
		}
		body.append("</table><br>");
		String signature = this.mailUtil.getSignature(SignatureType.NO_REPLY);
		body.append(signature);
		return body.toString();
	}
	
	private StringBuilder createTableRow(String kCode, String market, String sixMonth, String oneYear, String currency) {
		String trStartHTML = "<tr align='center'><td>";
		String trEndHTML = "</td></tr>";
		String tdHTML = "</td><td>";
		StringBuilder row = new StringBuilder();
		return row.append(trStartHTML)
				.append(kCode).append(tdHTML)
				.append(market).append(tdHTML)
				.append(sixMonth).append(tdHTML)
				.append(oneYear).append(tdHTML)
				.append(currency).append(trEndHTML);
	}
	
	@Override
	public Map<String,String> getIncludedSuppliers() {
		return dao.queryIncludedLongTermFeeReminder();
	}
	
	@Override
	public Map<String,String> getExcludedSuppliers() {
		return dao.queryExcludedLongTermFeeReminder();
	}
	
	@Override
	public int updateLongTermStorageReminder(String kCodes) {
		List<String> kCodesList = Arrays.asList(kCodes.split(","));
		return dao.updateLongTermStorageReminder(kCodesList);
	}
	
	@Override
	public List<SupplierLongTermStorageFee> getSuppliersOverFeeLimit(String kCodes) {
		List<String> kCodesList = Arrays.asList(kCodes.split(","));
		return dao.querySuppliersOverFeeLimit(kCodesList, getFeeToSendReminder());
	}
	
	@Override
	public Double getFeeToSendReminder() {
		return dao.queryFeeToSendReminder();
	}
	
	@Override
	public int updateFeeToSendReminder(Double limit) {
		return dao.updateFeeToSendReminder(limit);
	}
	
	
}
