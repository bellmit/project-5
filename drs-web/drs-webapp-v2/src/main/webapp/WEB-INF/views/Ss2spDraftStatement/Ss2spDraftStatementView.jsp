<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>SS2SP draft statement: ${statementId} - DRS</title>

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
					location.href = "${pageContext.request.contextPath}/draft-statements/"+statementId+"/commit";
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
					location.href = "${pageContext.request.contextPath}/ss2spdraftstatements/discard/"+ statementId;
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
	<div class="page-heading">Draft statement: ${statementName}</div>
	<p style="text-align: right">
		<spring:message code='ss2spStatement.period' />：${report.dateStart} ~ ${report.dateEnd} <br>
		<spring:message code='ss2spStatement.issuer' />：${companyKcodeToNameMap[report.isurKcode]}<br>
		<spring:message code='ss2spStatement.receiver' />：${companyKcodeToNameMap[report.rcvrKcode]}
	</p>		
	<table class="table">
		<thead>
			<tr>
				<th><spring:message code='ss2spStatement.itemType' /></th>
				<th class="text-right" style="width: 15%;"><spring:message code='ss2spStatement.statementAmountT' /></th>
				<th><spring:message code='ss2spStatement.notes' /></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<c:if test="${not empty report.profitShareItems[0]}">
					<td><spring:message code='${report.profitShareItems[0].displayName}' /></td>
					<td class="text-right">
						<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare">${report.profitShareItems[0].amountStr}</a>
					</td>
					<td>
						<c:if test="${report.profitShareItems[0].amountStr>0}">
							請開立並寄送三聯式統一發票予本公司。品名：利潤分享，金額：${report.profitShareAmountUntaxed}，營業稅：${report.profitShareAmountTax}，總計：${report.profitShareItems[0].amountStr}。公司名稱：善恩創新股份有限公司，統一編號：80698253，公司地址：11158 台北市士林區德行西路45號6樓
						</c:if>
					</td>
				</c:if>
			</tr>
			<c:forEach items="${report.shipmentRelatedItems}" var="item">
				<tr>
					<td><spring:message code='${item.itemName}' />${item.sourceShipmentName}</td>
					<td class="text-right">
						<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/shipment/${item.sourceShipmentName}">${item.amountStr}</a>
					</td>
					<td><spring:message code='${item.noteText}' />${item.sourceShipmentInvoiceNumber}</td>
				</tr>
			</c:forEach>

			<c:forEach items="${report.sellBackRelatedItems}" var="item">
				<tr>
					<td><spring:message code='${item.itemName}' /></td>
					<td class="text-right">
						<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/sellback">${item.amountStr}</a>
					</td>
					<td><spring:message code='${item.noteText}' />${item.invoiceNumber}</td>
				</tr>
			</c:forEach>
			<c:forEach items="${report.statementItemsServiceExpense}" var="item">
				<tr>
					<td>-<spring:message code='${item.displayName}' /></td>
					<td class="text-right">
						<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/serviceexpense/${item.invoiceNumber}">${item.amountStr}</a>
					</td>
					<td>善恩發票號碼：${item.invoiceNumber}</td>
				</tr>
			</c:forEach>
			<tr>
				<td><strong><em><spring:message code='ss2spStatement.totalForThisPeriod' /></em></strong></td>
				<td class="text-right"><strong><em>${report.total}</em></strong></td>
				<td></td>
			</tr>
			<tr>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td><spring:message code='ss2spStatement.balanceFromPreviousPeriod' /></td>
				<td class="text-right">${report.previousBalance}</td>
				<td></td>
			</tr>
			<tr>
				<td>-<spring:message code='ss2spRemittanceFrom' /></td>
				<c:choose>
					<c:when test="${report.remittanceIsurToRcvr == '0'}">
						<td class="text-right">-${report.remittanceIsurToRcvr}</td>
					</c:when>
					<c:otherwise>
						<td class="text-right">
							<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/rmtrcvd">-${report.remittanceIsurToRcvr}</a>
						</td>
					</c:otherwise>
				</c:choose>
				<td></td>
			</tr>
			<tr>
				<td><spring:message code='ss2spRemittanceTo' /></td>
				<c:choose>
					<c:when test="${report.remittanceRcvrToIsur == '0'}">
						<td class="text-right">${report.remittanceRcvrToIsur}</td>
					</c:when>
					<c:otherwise>
						<td class="text-right">
							<a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/rmtsent">${report.remittanceRcvrToIsur}</a>
						</td>
					</c:otherwise>
				</c:choose>
				<td></td>
			</tr>
			<tr>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td style="background-color: #F2F5A9;"><strong><spring:message code='ss2spStatement.balance' /></strong></td>
				<td class="text-right" style="background-color: #F2F5A9"><strong><spring:message code='${report.currency}' /> ${report.balance} </strong></td>
				<td>${balanceNote}</td>
			</tr>
		</tbody>
	</table>
	<div class="row">
		<div class="col-md-12">
			<div style="float:right">
				<a class="btn btn-danger" onclick="commitConfirmation('${statementId}')">Commit</a>
				<a class="btn btn-primary" onclick="discardConfirmation('${statementId}')">Discard</a>
				<div id="dialog-confirm"></div>				
			</div>
		</div>
	</div>
</div>