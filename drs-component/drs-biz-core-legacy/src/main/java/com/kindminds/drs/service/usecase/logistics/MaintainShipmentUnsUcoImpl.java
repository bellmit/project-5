package com.kindminds.drs.service.usecase.logistics;

import com.kindminds.drs.Context;
import com.kindminds.drs.Country;
import com.kindminds.drs.Currency;
import com.kindminds.drs.Warehouse;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.IvsLineItem;
import com.kindminds.drs.api.v2.biz.domain.model.logistics.ShipmentStatus;
import com.kindminds.drs.core.service.command.ShipmentSkuIdentificationCmdSrv;


import com.kindminds.drs.api.usecase.logistics.MaintainShipmentUnsUco;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.Forwarder;
import com.kindminds.drs.api.v1.model.logistics.ShipmentUns.ShipmentUnsLineItem;
import com.kindminds.drs.api.v1.model.product.stock.ProductSkuStock;
import com.kindminds.drs.api.data.access.rdb.CompanyDao;
import com.kindminds.drs.api.data.access.rdb.CurrencyDao;
import com.kindminds.drs.api.data.access.usecase.logistics.MaintainShipmentUnsDao;
import com.kindminds.drs.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service("maintainShipmentOfUnifiedUco")
public class MaintainShipmentUnsUcoImpl implements MaintainShipmentUnsUco {

	@Autowired private MaintainShipmentUnsDao dao;
	@Autowired private CompanyDao companyRepo;
	@Autowired private CurrencyDao currencyRepo;
	@Autowired private ShipmentSkuIdentificationCmdSrv cmdSrv;

	private final Currency unsCurrency = Currency.USD;
	private final String embeddedShipmentNameForDraft="DRAFT";
	private final String shipmentNamePrefix="UNS";
	private final String draftInvoiceNumberName="(TBD)";
	private final String dateTimeFormat = "yyyy-MM-dd HH:mm z";

	@Override
	public DtoList<ShipmentUns> getList(int pageIndex) {
		int userId = Context.getCurrentUser().getUserId();
		DtoList<ShipmentUns> list = new DtoList<ShipmentUns>();
		list.setTotalRecords(this.dao.queryAllShipmentsCounts(userId));
		list.setPager(new Pager(pageIndex,list.getTotalRecords(),50));
		list.setItems(this.dao.queryShipmentList(userId,list.getPager().getStartRowNum(),list.getPager().getPageSize()));
		return list;
	}

	@Override
	public ShipmentUns get(String shipmentName) {
		return this.dao.query(shipmentName);
	}

	@Override
	public List<IvsLineItem> getIvsLineItem(String ivsName) {
		return dao.queryInventoryShipmentLineItem(ivsName);
	}

	@Override
	public List<String> getAvailableIvsNames(String sellerKcode,String destinationCode) {
//		List<String> countryList = new ArrayList<>();
//		if (destinationCode.equalsIgnoreCase("UK") ||
//				destinationCode.equalsIgnoreCase("DE") ||
//				destinationCode.equalsIgnoreCase("FR") ||
//				destinationCode.equalsIgnoreCase("IT") ||
//				destinationCode.equalsIgnoreCase("ES")) {
//			countryList.add("UK");
//			countryList.add("DE");
//			countryList.add("FR");
//			countryList.add("IT");
//			countryList.add("ES");
//		} else {
//			countryList.add(destinationCode);
//		}
		return this.dao.queryAvailableInventoryShipmentNameList(sellerKcode, destinationCode);
	}

	@Override
	public Map<String,String> getAvailableSkusInInventoryShipment(String shipmentName) {
		return this.dao.queryAvailableSkuToDrsNameMapForInventoryShipment(shipmentName);
	}

	@Override
	public List<ShipmentStatus> getShipmentStatusList() {
		return Arrays.asList(ShipmentStatus.values());
	}

	@Override
	public Map<String, String> getDrsCompanyKcodeToNameMap() {
		return companyRepo.queryDrsCompanyKcodeToShortEnUsNameMap();
	}

	@Override
	public String getCompanyCurrency(String kcode) {
		return this.currencyRepo.queryCompanyCurrency(kcode).name();
	}

	@Override
	public String insertDraft(ShipmentUns shipment) {
		Assert.isTrue(this.validateShipment(shipment));
		Integer nextDraftSerialId = this.dao.queryMaxSerialIdOfDraftShipments(shipment.getSellerCompanyKcode())+1;
		String name = this.generateDraftShipmentName(shipment.getSellerCompanyKcode(),nextDraftSerialId);
		String status = ShipmentStatus.SHPT_DRAFT.name();
		Date exportDate = this.generateExportDate(shipment.getExportDate());
		Date arrivalDate = DateHelper.toDate(shipment.getDestinationReceivedDate(),this.dateTimeFormat);
		Date expectArrivalDate = DateHelper.toDate(shipment.getExpectArrivalDate(),this.dateTimeFormat);
		Assert.notNull(expectArrivalDate, "date must be in format yyyy-MM-dd HH:mm z");
		BigDecimal ddpTotal = this.calculateDDPTotal(shipment.getLineItems());
		BigDecimal cifTotal = this.calculateCIFTotal(shipment.getLineItems());

		Integer warehouseId = getWarehouseId(shipment);

		return this.dao.insert(nextDraftSerialId,name, this.draftInvoiceNumberName, status,
				this.unsCurrency.getKey(), exportDate, arrivalDate, expectArrivalDate,
				shipment, warehouseId, ddpTotal, cifTotal);
	}

	private Integer getWarehouseId(ShipmentUns shipment) {
		return Warehouse.valueOf("FBA_" + shipment.getDestinationCountry()).getKey();
	}

	private BigDecimal calculateDDPTotal(List<ShipmentUnsLineItem> lineItems){
		BigDecimal subtotal = BigDecimal.ZERO;
		for(ShipmentUnsLineItem item:lineItems){
			BigDecimal quantity = new BigDecimal(item.getQuantity().replaceAll(",", ""));
			BigDecimal unitAmount = new BigDecimal(item.getUnitAmount());
			subtotal = subtotal.add(quantity.multiply(unitAmount));
		}
		return subtotal;
	}

	private BigDecimal calculateCIFTotal(List<ShipmentUnsLineItem> lineItems){
		BigDecimal subtotal = BigDecimal.ZERO;
		for(ShipmentUnsLineItem item:lineItems){
			BigDecimal quantity = new BigDecimal(item.getQuantity().replaceAll(",", ""));
			BigDecimal unitCifAmount = StringUtils.hasText(item.getUnitCifAmount())?
					new BigDecimal(item.getUnitCifAmount()):BigDecimal.ZERO;
			subtotal = subtotal.add(quantity.multiply(unitCifAmount));
		}
		return subtotal;
	}

	@Override
	public String update(ShipmentUns shipment) {
		Assert.isTrue(this.validateShipment(shipment));
		ShipmentStatus newStatus = shipment.getStatus();
		ShipmentStatus oldStatus = this.dao.queryStatus(shipment.getName());
		Assert.isTrue(newStatus!=ShipmentStatus.SHPT_FROZEN);

		if(oldStatus==ShipmentStatus.SHPT_DRAFT){
			BigDecimal ddpTotal = this.calculateDDPTotal(shipment.getLineItems());
			BigDecimal cifTotal = this.calculateCIFTotal(shipment.getLineItems());
			this.dao.updateData(shipment, ddpTotal, cifTotal);
		}
		else {
			this.dao.updateStatus(shipment.getName(),newStatus);
			this.updateSourceIvsStatus(shipment);

			//todo arthur
			if(newStatus == ShipmentStatus.SHPT_IN_TRANSIT) {
					this.cmdSrv.createShipmentSkuIdentification(shipment.getName());
			}
		}

		this.dao.updateInvoiceNumver(shipment.getName(), shipment.getInvoiceNumber());
		this.dao.updateTrackingNumber(shipment.getName(), shipment.getTrackingNumber());
		this.dao.updateFbaId(shipment.getName(), shipment.getFbaId());
		this.dao.updateForwarder(shipment.getName(), shipment.getForwarder());
		this.dao.updateExportDate(shipment.getName(), this.generateExportDate(shipment.getExportDate()));
		this.dao.updateExportFxRate(shipment.getName(), shipment.getExportCurrencyExchangeRate());
		this.dao.updateExportFxRateToEur(shipment.getName(), shipment.getExportFxRateToEur());
		this.dao.updateDestinationReceivedDate(shipment.getName(),DateHelper.toDate(shipment.getDestinationReceivedDate(),this.dateTimeFormat));
		this.dao.updateExpectArrivalDate(shipment.getName(), DateHelper.toDate(shipment.getExpectArrivalDate(),this.dateTimeFormat));
		return shipment.getName();
	}

	private void updateSourceIvsStatus(ShipmentUns shipment) {
		if(shipment.getLineItems().isEmpty()) return;
		Set<String> ivsNameSet = new HashSet<>();
		for(ShipmentUnsLineItem item:shipment.getLineItems()) ivsNameSet.add(item.getSourceInventoryShipmentName());
		//todo arthur create shipment sku identicifation
		this.dao.updateIvsStatus(ivsNameSet,shipment.getStatus().name());
	}

	private Date generateExportDate(String exportDate){
		if(exportDate==null) return null;
		return DateHelper.toDate(exportDate+" UTC","yyyy-MM-dd z");
	}

	@Override
	public List<String> getDestinationCountryCodes(String sellerKcode) {
		Assert.isTrue(this.companyRepo.isDrsCompany(sellerKcode));
		String sellerCountryCode = this.dao.queryCountryOfCompany(sellerKcode);
		ArrayList<String> countryCodeList = new ArrayList<String>();
		Country sellerCountry = Country.valueOf(sellerCountryCode);
		if(sellerCountry!=Country.US) countryCodeList.add(Country.US.name());
		if(sellerCountry!=Country.CA) countryCodeList.add(Country.CA.name());
		if(sellerCountry!=Country.EU) countryCodeList.add(Country.EU.name());
		if(sellerCountry!=Country.UK) countryCodeList.add(Country.UK.name());
		return countryCodeList;
	}

	@Override
	public List<String> getActualDestinationCountryCodes(String destinationCountry) {
		ArrayList<String> countryCodeList = new ArrayList<String>();
		if(destinationCountry.equalsIgnoreCase("EU")) {
//			countryCodeList.add("UK");
			countryCodeList.add("DE");
			countryCodeList.add("FR");
//			countryCodeList.add("IT");
//			countryCodeList.add("ES");
		} else {
			countryCodeList.add(destinationCountry);
		}
		return countryCodeList;
	}

	@Override @Transactional("transactionManager")
	public String freeze(String name) {
		Assert.isTrue(this.dao.queryStatus(name)==ShipmentStatus.SHPT_DRAFT,"Error, freezing a non draft shipment");
		String sellerKcode=this.dao.querySellerKcode(name);
		int nextSerialId = this.dao.queryMaxSerialIdOfNonDraftShipments(sellerKcode)+1;
		String newShpName = this.generateNonDraftShipmentName(sellerKcode,nextSerialId);
		this.dao.updateStatus(name, ShipmentStatus.SHPT_FROZEN);
		String newName = this.dao.updateNameAndSerialId(name, newShpName, nextSerialId);
		ShipmentUns frozen = this.dao.query(newName);
		this.updateIvsSoldQty(frozen);
		return newName;
	}

	@Override
	public String deleteDraft(String name) {
		Assert.isTrue(this.dao.queryStatus(name)==ShipmentStatus.SHPT_DRAFT,"Error, freezing a non draft shipment");
		return this.dao.deleteDraft(name);
	}

	private void updateIvsSoldQty(ShipmentUns uns){
		for(ShipmentUnsLineItem item:uns.getLineItems()){
			int ivsQtyOrder = this.dao.queryQtyOrdered(item.getSourceInventoryShipmentName(),
					item.getSkuCode(), item.getBoxNum(), item.getMixedBoxLineSeq());
			int ivsQtySold = this.dao.queryQtySold(item.getSourceInventoryShipmentName(),
					item.getSkuCode(), item.getBoxNum(), item.getMixedBoxLineSeq());

			Assert.isTrue(ivsQtyOrder-ivsQtySold>=Integer.valueOf(item.getQuantity().replaceAll(",", "")),
					"Shipment "+item.getSourceInventoryShipmentName()+" does not has enough "+item.getSkuCode()
							+ ", Current Quantity:"+(ivsQtyOrder-ivsQtySold)+", Request:"+item.getQuantity());

			this.dao.updateSoldQty(item.getSourceInventoryShipmentName(), item.getSkuCode(),
					Integer.valueOf(item.getQuantity().replaceAll(",", "")),
					item.getBoxNum(), item.getMixedBoxLineSeq());
		}
	}

	private boolean validateShipment(ShipmentUns shipment){
		String sellerCountry = this.dao.queryCountryOfCompany(shipment.getSellerCompanyKcode());
		return !sellerCountry.equals(shipment.getDestinationCountry());
	}

	private String generateDraftShipmentName(String sellerKcode,Integer nextDraftSerialId){
		return this.shipmentNamePrefix+"-"+sellerKcode+"-"+this.embeddedShipmentNameForDraft+nextDraftSerialId.toString();
	}

	private String generateNonDraftShipmentName(String sellerKcode,Integer nextDraftSerialId){
		return this.shipmentNamePrefix+"-"+sellerKcode+"-"+nextDraftSerialId.toString();
	}

	@Override
	public String getDrsCompanyKcode(String countryCode) {
		Country country = Country.valueOf(countryCode);
		return this.dao.queryCompanyKcode(country.getKey());
	}

	@Override
	public List<Forwarder> getForwarderList() {
		return Arrays.asList(Forwarder.values());
	}

	@Override
	public ProductSkuStock getProductSkuStock(String shipmentName, String skuCodeByDrs) {
		return this.dao.queryProductSkuStock(shipmentName,skuCodeByDrs);
	}

}