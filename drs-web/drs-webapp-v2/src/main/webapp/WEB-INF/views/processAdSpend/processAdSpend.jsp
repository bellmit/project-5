<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<head>
<title>Process Ad Spend</title>

<style type="text/css">
.page-heading{
	font-weight: bold;

}

#settlement-period-row{
	background-color: #eeeeee;
	border-radius: 5px;
	min-height: 56px;
	
}

#table-section{
   	min-height: 300px;
   	padding: 0px;
   	
}

thead tr{
 	font-size: 14px;
}

#processed-text{
	color: red;
}
    
</style>

<script language="javascript" type="text/javascript">
$(function() {
	
	//if processing settlement
	if(${processing} == 1){
	
		var transactions = JSON.parse(${transactionsJSON});
		
		//console.log(transactions);
		
		//if settlement period has not been processed before 
		if(transactions.length > 0){
			var transactionTable = "";
			
			transactions.forEach(function(transaction){
				
				var transactionCashFlowDirection = "";
				
				//if statement for transaction direction
				if(transaction.cashFlowDirectionKey == "0"){
					transactionCashFlowDirection = "Supplier to MSDC";
				}else{
					transactionCashFlowDirection = "MSDC to Supplier";
				};
				
				var transactionRow = "<tr>\
					<td>" + transaction.msdcKcode + " " + transaction.msdcName + "</td>\
					<td>" + transaction.ssdcKcode + " " + transaction.ssdcName + "</td>\
					<td>" + transaction.splrKcode + " " + transaction.splrName + "</td>\
					<td>" + transaction.utcDate + "</td>\
					<td>" + transactionCashFlowDirection + "</td>\
					<td>" + transaction.note + "</td>\
					<td>" + transaction.lineItems[0].itemName + "</td>\
					<td>" + transaction.lineItems[0].itemNote + "</td>\
					<td>" + transaction.currency + "</td>\
					<td>" + transaction.total + "</td>\
					</tr>";
				
				//append each row to the table
				transactionTable = transactionTable + transactionRow;
				
			});
			
			$( "#transaction-table" ).html(transactionTable);
		
		}else{
			$( "#processed-text" ).html("<h4>This settlement period has been processed.</h4>");
		}
	}

});

</script>

</head>

<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" style="padding-left: 0px;">
				<div class="page-heading"><spring:message code='processAdSpend.pageTitle' /></div>
			</div>
		</div>
		<div class="row" id="settlement-period-row">
			<div class="col-md-6">	
				<h3 style="margin-top: 16px;"><spring:message code='processAdSpend.settlementPeriod' />: ${latestSettlementPeriod.startDate} ~ ${latestSettlementPeriod.endDate}</h3>
			</div>
			<div class="col-md-6" style="padding-top: 13px;">				
				<a href="${pageContext.request.contextPath}/ProcessAdSpend/ViewHSAReport" 
					class="btn btn-primary" style="float: right;">
					View HSA Report
		 		</a>
				<a href="${pageContext.request.contextPath}/ProcessAdSpend/ViewCampaignDetail" 
					class="btn btn-primary" style="float: right;">
		 			<!-- <spring:message code='processAdSpend.viewCampaignDetail' /> -->
		 			View Campaign Report
		 		</a>
		 		<a href="${pageContext.request.contextPath}/ProcessAdSpend/Process" 
		 			class="btn btn-warning" style="float: right;">
		 			<spring:message code='processAdSpend.processAdSpend' />
		 		</a>
			</div>
		</div>
		<br/>
		<div class="row">
			<div class="col-md-12" id="table-section">
				<table class="table table-bordered">
				  <thead>
				    <tr class="success">
				      <th><spring:message code='processAdSpend.msdc' /></th>
				      <th><spring:message code='processAdSpend.ssdc' /></th>
				      <th><spring:message code='processAdSpend.supplier' /></th>
				      <th><spring:message code='processAdSpend.utcDate' /></th>
				      <th><spring:message code='processAdSpend.cashFlowDirection' /></th>
				      <th><spring:message code='processAdSpend.note' /></th>
				      <th><spring:message code='processAdSpend.itemName' /></th>
				      <th><spring:message code='processAdSpend.detailNote' /></th>
				      <th><spring:message code='processAdSpend.currency' /></th>
				      <th><spring:message code='processAdSpend.amount' /></th>
				    </tr>
				  </thead>
				  <tbody id="transaction-table">
				  </tbody>
				</table>
				<div id="processed-text">
				</div>
			</div>
		</div>
	</div>
</div>
