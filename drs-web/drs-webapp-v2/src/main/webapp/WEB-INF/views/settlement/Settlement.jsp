<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Settlement - DRS</title>
<style type="text/css">

.settlement-block{
	border-radius: 5px;
	background-color:#f8f7f7;
	display: inline-block;
	padding: 20px 20px 20px 20px;
}
#period-section{
	width:540px;
	height:230px;
	margin: 20px 10px 10px 10px;	
	vertical-align:top;
}
#function-section{
	width:540px;
	height:230px;
	margin: 20px 10px 10px 10px;
	vertical-align:top;
}
#ready-status{
	width:1104px;
	margin: 10px 10px 10px 10px;
}
#button-area{
	width:1104px;
	margin: 10px 10px 10px 10px;
}

.reportStatusReady {
    background: #5cb85c;
    width: 10px;
    height: 10px;
   	border-radius: 50%;
   	display: inline-block;
}
.reportStatusNotReady {
    background: #d9534f;
    width: 10px;
    height: 10px;
   	border-radius: 50%;
   	display: inline-block;
}
</style>
<script>

jQuery(document).ready(function() {
 	refreshPeriods();
 	refreshImportStatuses();
});

function refreshPeriods(){
	$.getJSON("${pageContext.request.contextPath}/settlement/getPeriods",function(settlementPeriods) {
		var $settlementPeriodTableTbody = $("#settlementPeriods").find('tbody'); 
		$settlementPeriodTableTbody.empty();
		$.each(settlementPeriods,function(key,settlementPeriod){
			var $tdPeriodStart = $('<td>').addClass('text-left').addClass('col-md-2').text(settlementPeriod.startDate);
			var $tdPeriodEnd   = $('<td>').addClass('text-left').addClass('col-md-2').text(settlementPeriod.endDate);
			var $tr = $('<tr>').append($tdPeriodStart).append($tdPeriodEnd);
			$settlementPeriodTableTbody.append($tr);
		});
	});
}

function tryAddPeriod(){
	$.get("${pageContext.request.contextPath}/settlement/tryAddPeriod",function(result) {
		$('#tryAddPeriodResult').hide();
		$('#tryAddPeriodResult').text(result);
		$('#tryAddPeriodResult').fadeIn();
		refreshPeriods();
	},"text");
}

function refreshImportStatuses(){
	$.getJSON("${pageContext.request.contextPath}/settlement/getAmazonSettlementReportReadyStatus",function(status) {
		var $tbody = $("#ready-status").find('tbody').empty();
		var $headerThList = $('#ready-status').find('thead').find("tr:first").children();
		$.each(status,function(period,readyMarketplaces){
			var $tr = $('<tr>').append($('<td>').addClass('text-center').text(period));
			$tbody.append($tr);
			$headerThList.each(function(i,th){
				if(i==0) return;
				$td = $('<td>').addClass('text-center');
				if($.inArray($(th).text(),readyMarketplaces)==-1){
					$td.append($('<div>').addClass('reportStatusNotReady'));
				} else {
					$td.append($('<div>').addClass('reportStatusReady'));
				}
				$tr.append($td);
			});
		});
		$tbody.hide();
		$tbody.fadeIn();
	});
}

function createMs2ssStatements(){
	$('#settlement-result').text("Processing...");
	$.get("${pageContext.request.contextPath}/settlement/createMs2ssStatements",function(result) {
		$('#settlement-result').hide();
		$('#settlement-result').text(result);
		$('#settlement-result').fadeIn();
		$('#settlement-result').fadeOut(10000);
	},"text").fail(function() {
		$('#settlement-result').text("Error");
		$('#settlement-result').fadeOut(10000);
	});
}

function createSs2spDraftStatements(){
	$('#settlement-result').text("Processing...");
	$.get("${pageContext.request.contextPath}/settlement/createSs2spDraftStatements",function(result) {
		$('#settlement-result').hide();
		$('#settlement-result').text(result);
		$('#settlement-result').fadeIn();
		$('#settlement-result').fadeOut(10000);
	},"text").fail(function() {
		$('#settlement-result').text("Error");
		$('#settlement-result').fadeOut(10000);
	});
}

</script>
</head>
<div class="container">
	<div id="period-section" class="settlement-block">
		<table id="settlementPeriods" class="table">
			<thead>
				<tr>
					<th colspan="2">Recent periods</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<button class="btn btn-primary" type="submit" style="padding:0px 12px;" onclick="tryAddPeriod()" >Try add period</button>
		<span id="tryAddPeriodResult"></span>
	</div>
	<div id="function-section" class="settlement-block">
		<table class="table" style="width:50%">
			<thead>
				<tr>
					<th>Function</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Amazon Return Report</td>
					<td><a href="${pageContext.request.contextPath}/AmazonReturnReport">link</a></td>
				</tr>
				<tr>
					<td>Amazon Settlement Report</td>
					<td><a href="${pageContext.request.contextPath}/amazon-settlement-reports">link</a></td>
				</tr>
				<tr>
					<td>Retainment Rate</td>
					<td><a href="${pageContext.request.contextPath}/RetainmentRate">link</a></td>
				</tr>
				<tr>
					<td>Market Side Transaction</td>
					<td><a href="${pageContext.request.contextPath}/MarketSideTransaction">link</a></td>
				</tr>
				<tr>
					<td>ms2ssdraftstatements</td>
					<td><a href="${pageContext.request.contextPath}/ms2ssdraftstatements">link</a></td>
				</tr>
				<tr>
					<td>ss2spdraftstatements</td>
					<td><a href="${pageContext.request.contextPath}/ss2spdraftstatements">link</a></td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id="ready-status" class="settlement-block">
		<h3>Amazon settlement import status</h3>
		<table class="table">
			<thead>
				<tr>
					<th class="text-center"><button class="btn btn-primary" type="submit" style="padding:2px 12px;" onclick="refreshImportStatuses()" >Refresh</button></th>
					<c:forEach var="amazonMarketplace" items="${amazonMarketplaces}" >
						<th class="text-center">${amazonMarketplace.name}</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<span id="tryAddPeriodResult"></span>
	</div>
	<div id="button-area" class="settlement-block">
		<button class="btn btn-primary" type="submit" style="padding:2px 12px;" onclick="createMs2ssStatements()" >Create MS2SS statements</button>
		<button class="btn btn-primary" type="submit" style="padding:2px 12px;" onclick="createSs2spDraftStatements()" >Create  draft SS2SP settlement </button>
		<span id="settlement-result"></span>
	</div>
</div>