<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>MS2SS draft statement: ${statementId} - DRS</title>

<script>

	function commitConfirmation(statementId) {	
		$("#dialog-confirm").html("This draft statement will become an official statement if being committed, are you sure to commit this draft statement?");
		$("#dialog-confirm").dialog({
			open : function() {
				$('.ui-dialog-buttonset button[name="no"]').focus();
			},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
			{
				text : "Yes",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/ms2ssdraftstatements/"+statementId+"/commit";
				}
			},
			{
				text : "No",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});		
	}
	
	function discardConfirmation(statementId) {	
		$("#dialog-confirm").html("Are you sure to discard this draft statement?");
		$("#dialog-confirm").dialog({
			open : function() {
				$('.ui-dialog-buttonset button[name="no"]').focus();
			},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
			{
				text : "Yes",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/ms2ssdraftstatements/"+statementId+"/discard";
				}
			},
			{
				text : "No",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});		
	}
</script>
</head>
<div class="container">
	<h2>Statement: ${statementName}</h2>
	<p style="text-align: right">
		Period: ${report.dateStart} ~ ${report.dateEnd} <br>
		Issued by ${kcodeToNameMap[report.isurKcode]} to ${kcodeToNameMap[report.rcvrKcode]}
	</p>
	<table class="table">
		<thead>
			<tr>
				<th><b>Item type</b></th>
				<th class="text-right"><b>Amount</b></th>
				<th><b>Notes</b></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${report.statementItems}" var="item">
				<tr>
					<td>${item.displayName}</td>
					<td class="text-right">
						<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/${item.urlPath}">${item.amountStr }</a>
					</td>
					<td class="text-left">${item.note}</td>
				</tr>
			</c:forEach>
			<c:forEach items="${report.statementItemsPaymentOnBehalf}" var="item">
				<tr>
					<td>${item.displayName}</td>
					<td class="text-right">
						<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/${item.urlPath}">${item.amountStr }</a>
					</td>
					<td></td>
				</tr>
			</c:forEach>
			<c:if test="${not empty report.ms2ssStatementItemMsdcPaymentOnBehalf}">
				<tr>
					<td>${report.ms2ssStatementItemMsdcPaymentOnBehalf.name}</td>
					<td class="text-right">
						<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/msdc-payment-on-behalf">${report.ms2ssStatementItemMsdcPaymentOnBehalf.amount}</a>
					</td>
					<td></td>
				</tr>
			</c:if>
			<tr>
				<td><strong><em>Total for this period</em></strong></td>
				<td class="text-right"><strong><em>${report.total}</em></strong></td>
				<td></td>
			</tr>
			<tr>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td>Balance from previous period</td>
				<td style="text-align: right">${report.previousBalance}</td>
				<td></td>
			</tr>
			<tr>
				<td>Remittance received from ${kcodeToNameMap[report.isurKcode]}</td>
				<td class="text-right">${report.remittanceIsurToRcvr}</td>
				<td></td>
			</tr>
			<tr>
				<td>Remittance made to ${kcodeToNameMap[report.isurKcode]}</td>
				<td class="text-right">${report.remittanceRcvrToIsur}</td>
				<td></td>
			</tr>
			<tr>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td style="background-color: #F2F5A9;"><strong>Balance</strong></td>
				<td class="text-right" style="background-color: #F2F5A9">
					<strong>USD ${report.balance}</strong>
				</td>
				<td></td>				
			</tr>
		</tbody>
	</table>
	<div style="float:right">
		<a class="btn btn-danger" onclick="commitConfirmation('${statementName}')">Commit</a>
		<a class="btn btn-primary" onclick="discardConfirmation('${statementName}')">Discard</a>
		<div id="dialog-confirm"></div>				
	</div>
</div>