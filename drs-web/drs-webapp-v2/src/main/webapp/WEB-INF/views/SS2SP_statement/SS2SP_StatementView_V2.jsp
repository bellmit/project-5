<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='ss2spStatement.statement' />:${statementName} - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<style type="text/css">
#ss2spStatement a:link:after, #ss2spStatement a:visited:after {
    content: "";
}
</style>
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
         <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/statements"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <span>${statementName}</span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='ss2spStatement.statement' />: ${statementName}
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">
					<spring:message code='ss2spStatement.period' />：${report.dateStart} ~ ${report.dateEnd} <br>
					<spring:message code='ss2spStatement.issuer' />：${companyKcodeToNameMap[report.isurKcode]}<br>
					<spring:message code='ss2spStatement.receiver' />：${companyKcodeToNameMap[report.rcvrKcode]}
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="ss2spStatement" class="table table-hover">
					<thead>
						<tr>
							<th><spring:message code='ss2spStatement.itemType' /></th>
							<th class="text-right" style="width: 15%;"><spring:message code='ss2spStatement.statementAmountT' /></th>
							<th><spring:message code='ss2spStatement.notes' /></th>
						</tr>
					</thead>
					<tbody>
						<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare'">
							<c:if test="${not empty report.profitShareItems[0]}">
								<td>
								<spring:message code='ss2spStatement.profitShare' />
								<!--  
								<c:choose>
									<c:when test="${report.profitShareItems[0].amountStr>0}">
										<spring:message code='${report.profitShareItems[0].displayName}' />											
									</c:when>
									<c:otherwise>
										<spring:message code='SS_ProductCostShareForSP' />	
									</c:otherwise>								
								</c:choose>																
								-->
								</td>
								<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare">${report.profitShareItems[0].amountStr}</a></td>
								<td>
									<c:if test="${report.profitShareItems[0].amountStr>0}">
										請開立並寄送三聯式統一發票予本公司，感謝您！<br>
										公司名稱：善恩創新股份有限公司，統一編號：80698253 <br>										
										品名：利潤分享，金額：${report.profitShareAmountUntaxed}，營業稅：${report.profitShareAmountTax}，總計：${report.profitShareItems[0].amountStr} <br>
										公司地址：11158  台北市士林區德行西路45號6樓 <br>									
										</c:if>
								</td>
							</c:if>
						</tr>
						<c:forEach items="${report.shipmentRelatedItems}" var="item">
							<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/shipment/${item.sourceShipmentName}'">
								<td><spring:message code='${item.itemName}' /> <a href="${pageContext.request.contextPath}/InventoryShipments/${item.sourceShipmentName}">${item.sourceShipmentName}</a></td>
								<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/shipment/${item.sourceShipmentName}">${item.amountStr}</a></td>
								<td><spring:message code='${item.noteText}' />${item.sourceShipmentInvoiceNumber}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${report.sellBackRelatedItems}" var="item">
							<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/sellback'">
								<td>-<spring:message code='${item.itemName}' /></td>
								<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/sellback">${item.amountStr}</a></td>
								<td><spring:message code='${item.noteText}' />${item.invoiceNumber}</td>
							</tr>
						</c:forEach>
						<c:forEach items="${report.statementItemsServiceExpense}" var="item">
						    <c:choose>
						    <c:when test="${empty fn:trim(item.invoiceNumber)}">
                                <tr>
                                    <td>-<spring:message code='${item.displayName}' /></td>
                                    <td class="text-right">${item.amountStr}</td>
                                    <td>尚未開立</td>
                                </tr>
						    </c:when>
						    <c:otherwise>
                                <tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/ServiceExpense/${item.invoiceNumber}'">
                                    <td>-<spring:message code='${item.displayName}' /></td>
                                    <td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/ServiceExpense/${item.invoiceNumber}">${item.amountStr}</a></td>
                                    <td>${item.invoiceNumber}</td>
                                </tr>
							</c:otherwise>
						    </c:choose>
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
						<c:choose>
							<c:when test="${report.remittanceIsurToRcvr == '0'}">
							<tr>		
							</c:when>
							<c:otherwise>
							<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/RemittanceReceived'">	
							</c:otherwise>
						</c:choose>
							<td>-<spring:message code='ss2spRemittanceFrom' /></td>
							<c:choose>
								<c:when test="${report.remittanceIsurToRcvr == '0'}">
									<td class="text-right">-${report.remittanceIsurToRcvr}</td>
								</c:when>
								<c:otherwise>
									<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/RemittanceReceived">-${report.remittanceIsurToRcvr}</a></td>
								</c:otherwise>
							</c:choose>
							<td></td>							
						</tr>		
						<c:choose>
							<c:when test="${report.remittanceRcvrToIsur == '0'}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="clickableRow" onclick="document.location.href='${pageContext.request.contextPath}/${rootUrl}/${statementName}/RemittanceSent'">
							</c:otherwise>
						</c:choose>
							<td><spring:message code='ss2spRemittanceTo' /></td>
							<c:choose>
								<c:when test="${report.remittanceRcvrToIsur == '0'}">
									<td class="text-right">${report.remittanceRcvrToIsur}</td>
								</c:when>
								<c:otherwise>
									<td class="text-right"><a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/RemittanceSent">${report.remittanceRcvrToIsur}</a></td>
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
			</div>
		</div>
	</div>
</div>