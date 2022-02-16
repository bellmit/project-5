package com.kindminds.drs.service.usecase.accounting;

import com.kindminds.drs.api.usecase.ViewDebitCreditNoteUco;
import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote;
import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteItem;
import com.kindminds.drs.api.v1.model.accounting.DebitCreditNote.DebitCreditNoteSkuItems;
import com.kindminds.drs.api.data.access.usecase.accounting.ViewDebitCreditNoteDao;
import com.kindminds.drs.persist.v1.model.mapping.accounting.DebitCreditNoteItemImpl;
import com.kindminds.drs.v1.model.impl.DebitCreditNoteImpl;
import com.kindminds.drs.v1.model.impl.accounting.DebitCreditNoteSkuItemsImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service 
public class ViewDebitCreditNoteUcoImpl implements ViewDebitCreditNoteUco {
	
	@Autowired private ViewDebitCreditNoteDao dao;

	@Override @SuppressWarnings("unchecked")
	public DebitCreditNote getDebitCreditNote(String statementName) {
		List<DebitCreditNoteItemImpl> items = (List<DebitCreditNoteItemImpl>)(List<?>)this.dao.queryDebitCreditNoteItems(statementName);
		for(DebitCreditNoteItemImpl item:items){
			item.setSkuLines(this.dao.queryDebitCreditNoteSkuLines(statementName, item.getInvoiceNumber()));
		}
		DebitCreditNoteImpl note = new DebitCreditNoteImpl();
		note.setDate(this.dao.queryStatementDateEnd(statementName));
		note.setAmountTotal(this.dao.queryNoteAmountTotal(statementName));
		note.setItems((List<DebitCreditNoteItem>)(List<?>)items);
		return note;
	}

	@Override
	public String getTsvReport(String statementName) {

		List<Object[] > columnsList = this.dao.queryDebitCreditNoteSkuItems(statementName);

		List<DebitCreditNoteSkuItems> debitCreditNoteSkuItems = new ArrayList<>();
		for(Object[] columns:columnsList){
			String skuCode = (String)columns[0];
			String invoiceNumber = (String)columns[1];
			BigDecimal sum = (BigDecimal)columns[2];
			debitCreditNoteSkuItems.add(new DebitCreditNoteSkuItemsImpl(skuCode,invoiceNumber,sum));
		}

		try {
			return this.toTsv(debitCreditNoteSkuItems);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	@SuppressWarnings("resource")
	private String toTsv(List<DebitCreditNoteSkuItems> items) throws IOException{
		StringWriter writer = new StringWriter();
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.TDF.withHeader("SKU","Invoice No.","Sum for SKU (USD)"));
		for(DebitCreditNoteSkuItems item:items){
			csvPrinter.printRecord(
					item.getSkuCode(),
					item.getInvoiceNumber(),
					item.getSumForSku());
		}
		writer.flush();
		return writer.toString();				
	}

}
