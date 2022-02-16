<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
	<title>Process coupon redemption fee transactions - DRS</title>
	<script>
	$(document).ready(function() {
		var isSettled = ${isSettled};
		$("#isSettledMessage").hide();
		if(isSettled == "true"){
			$("#process").prop('disabled', true);
			$("#isSettledMessage").show();			
		} 		
	});	
	</script>	
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Process coupon redemption fee transactions</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<form method="POST" class="form-inline" action="${pageContext.request.contextPath}/ProcessCouponRedemptionFee">
					<label class="control-label mr-2">Settlement period:</label> 
					<div class="form-group"> 
						${settlementPeriod.startDate} ~ ${settlementPeriod.endDate}
						<input type="hidden" name="periodId" value="${settlementPeriod.id}">
					</div>
					<button id="process" type="submit" class="btn btn-primary mx-2">Process</button>
					<small id="isSettledMessage" class="text-danger">The settlement period is settled</small>
				</form>
			</div>
		</div>	
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Result</div>
			</div>			
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th>MSDC</th>
							<th>SSDC</th>
							<th>Supplier</th>
							<th>UTC date</th>
							<th>Cashflow direction</th>
							<th>Currency</th>
							<th>Note</th>
							<th>Item name</th>
							<th>detail note</th>
							<th class="text-right">amount</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
  							<c:when test="${not empty processedCouponRedemptions}">
    							<c:forEach items="${processedCouponRedemptions}" var="processedCouponRedemption">   								
    								<c:forEach items="${processedCouponRedemption.lineItems}" var="lineItem">
    									<tr>
    										<td>${processedCouponRedemption.msdcKcode} ${processedCouponRedemption.msdcName}</td>
    										<td>${processedCouponRedemption.ssdcKcode} ${processedCouponRedemption.ssdcName}</td>
											<td>${processedCouponRedemption.splrKcode} ${processedCouponRedemption.splrName}</td>
											<td>${processedCouponRedemption.utcDate}</td>
											<td>${keyToCashFlowDirectionMap[processedCouponRedemption.cashFlowDirectionKey].displayName}</td>
											<td>${processedCouponRedemption.currency}</td>
											<td>${processedCouponRedemption.note}</td>
											<td>${lineItem.itemName}</td>
											<td>${lineItem.itemNote}</td>
											<td class="text-right">${lineItem.subtotal}</td>
    									</tr>
    								</c:forEach>    								
    							</c:forEach>
  							</c:when> 
  							<c:otherwise>
    							<tr>
									<td colspan="10" class="text-center">
										${resultMessage}
									</td>
								</tr>
  							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>		
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Failed coupon redemption fees</div>
			</div>			
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th>ID</th>
							<th>UTC date</th>
							<th>Currency</th>
							<th>Transaction type</th>
							<th>Marketplace name</th>
							<th>Amount description</th>
							<th class="text-right">Amount</th>				
							<th>Reason</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
  							<c:when test="${not empty failedCouponRedemptions}">
 								<c:forEach items="${failedCouponRedemptions}" var="failedCouponRedemption">
 									<tr>	
 										<td>${failedCouponRedemption.id}</td>
										<td>${failedCouponRedemption.transactionTime}</td>
										<td>${failedCouponRedemption.currencyName}</td>
										<td>CouponRedemptionFee</td>
										<td>${failedCouponRedemption.marketplaceName}</td>
										<td>${failedCouponRedemption.coupon}</td>
										<td class="text-right">${failedCouponRedemption.amount}</td>				
										<td>${failedCouponRedemption.reason}</td>																
 									</tr>
 								</c:forEach>
  							</c:when> 
  							<c:otherwise>
    							<tr>
									<td colspan="8" class="text-center">
										No data found
									</td>
								</tr>
  							</c:otherwise>
						</c:choose>																																				
					</tbody>
				</table>
			</div>		
		</div>
	</div>
</div>