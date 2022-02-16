<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>DRS Retainment Rate - DRS</title>

<script>

jQuery(document).ready(function() {
	refreshRetainmentRate();
});

function refreshRetainmentRate(){
	console.log("refreshing----");
	$.getJSON("${pageContext.request.contextPath}/RetainmentRate/rates.json",function(retainmentRates) {
		var $tbody = $("#rate-table").find('tbody'); 
		$tbody.empty();
		$.each(retainmentRates,function(key,rate){
			var $tr = $('<tr>');
			$tr.append( $('<td>').addClass('text-center').text(rate.id) );
			$tr.append( $('<td>').addClass('text-center').text(rate.utcDateStart) );
			$tr.append( $('<td>').addClass('text-center').text(rate.utcDateEnd) );
			$tr.append( $('<td>').addClass('text-center').text(rate.country) );
			$tr.append( $('<td>').addClass('text-center').text(rate.supplierKcode) );
			$tr.append( $('<td>').addClass('text-center').text(rate.originalCurrency) );
			$tr.append( $('<td>').addClass('text-right').text(rate.revenueInOriginalCurrency) );
			$tr.append( $('<td>').addClass('text-right').text(rate.currencyExchangeRateToUsd) );
			$tr.append( $('<td>').addClass('text-right').text(rate.revenueInUsd) );
			$tr.append( $('<td>').addClass('text-right').text(rate.rate) );
			$tbody.append($tr);
		});
	});
}

function calculateAnyway(){
	$.get("${pageContext.request.contextPath}/RetainmentRate/calculateAnyway",function(result) {
		$('#result-text').hide();
		$('#result-text').text(result);
		$('#result-text').fadeIn();
		if(!retainmentRateCalculated(result,"exist")) refreshRetainmentRate();
	},"text");
}

function retainmentRateCalculated(source,pattern){
	return source.indexOf(pattern)>=0;
}

</script>
</head>
<div class="max-width" >
	<div class="text-center text-success">
		<h1>${message}</h1>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">DRS Retainment Rate</div>
			</div>
		</div>
		<form method="POST" action="${pageContext.request.contextPath}/RetainmentRate/calculate">
			<table>
				<tr>
					<td>
						<select class="form-control" name="periodId" >
						<option value="">--- Select ---</option>
						<c:forEach items="${availableSettlementPeriodList}" var="settlementPeriod">
							<option value="${settlementPeriod.id}">${settlementPeriod.startDate} ~ ${settlementPeriod.endDate}</option>
						</c:forEach>
						</select>
					</td>
					<td>
						<input class="btn btn-primary" type="submit" value="Calculate">
					</td>
					<td>
						<input class="btn btn-primary" value="Calculate anyway" onclick="calculateAnyway()">
					</td>
					<td>
						<span id="result-text"></span>
					</td>
				</tr>
			</table>
		</form>
		<table id="rate-table" class="table">
			<thead>
				<tr>
					<th class="text-center">ID</th>
					<th class="text-center">Start (UTC)</th>
					<th class="text-center">End (UTC)</th>
					<th class="text-center">Country</th>
					<th class="text-center">Supplier</th>
					<th class="text-center">ORIG Currency</th>
					<th class="text-right">ORIG Revenue</th>
					<th class="text-right">FX rate to USD</th>
					<th class="text-right">USD Revenue</th>
					<th class="text-right">Rate</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>