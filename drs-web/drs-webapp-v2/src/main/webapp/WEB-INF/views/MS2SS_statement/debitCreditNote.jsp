<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<title><c:if test="${debitCreditNote.type eq 'DEBIT'}">
   						Debit Note: DN-${statementName} - DRS
						</c:if> <c:if test="${debitCreditNote.type eq 'CREDIT'}">
   						Credit Note: CN-${statementName} - DRS
						</c:if></title>
<script type='text/javascript'
	src="<c:url value="/resources/js/table-fixed-header.js"/>"></script>
<style type="text/css">
body {
	font-size: 10px;
}
thead {
    text-align: left;
}
</style>
</head>
<div class="container">
	<div id="debitCreditNote">
		<div class="row">
			<table style="width: 100%">
				<tbody>
					<tr>
						<td>
							<div style="padding-left: 40%">
								<img
									src="<c:url value="/resources/images/beanworthy_logo.png"/>"
									style="width: 203px; height: 40px;">
							</div>
						</td>
						<td valign="middle">
							<div style="padding-left: 10%">
								<address>
									BEANWORTHY, LLC<br> 113 BARKSDALE PROFESSIONAL CENTER<br>
									NEWARK, DE 19711-3258, U. S. A.<br> <abbr>Tel:</abbr>
									1-650-814-8177 &nbsp;&nbsp; <abbr>Tax ID:</abbr> 421770061
								</address>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="row">
			<div class="text-center">
				<u style="font-size: 30px"> ${debitCreditNote.type} NOTE </u>
			</div>
		</div>
		<div class="row">
			<table style="width: 100%">
				<tr>
					<td valign="top"><div style="padding-left: 20%">To:</div></td>
					<td>
						<div style="padding-left: 10%">
							<address>
								KINDMINDS INNOVATIONS, INC.<br> 6F., No.45,
								Dexing W. Rd.,<br> Shilin Dist., Taipei City 11158, Taiwan<br>
								<abbr>Tel:</abbr>+886-2-2837-8995 &nbsp;&nbsp; <abbr>Fax:</abbr>+886-2-2837-6257<br>
								sales@kindminds.com
							</address>
						</div>
					</td>
					<td valign="top"><c:if
							test="${debitCreditNote.type eq 'DEBIT'}">
							<strong>D/N No.</strong> DN-${statementName}
						</c:if> <c:if test="${debitCreditNote.type eq 'CREDIT'}">
							<strong>C/N No.</strong> CN-${statementName}
						</c:if> <br> <strong>Date:</strong> ${debitCreditNote.date}</td>
				</tr>
			</table>
		</div>
		<div class="row">
			<table class="table-condensed table-fixed-header" style="width: 100%">
				<thead class="header">
					<tr>
						<th><b>SKU</b></th>
						<th><b>Description</b></th>
						<th><b>Invoice No.</b></th>
						<th><b>Sum for SKU (USD)</b></th>
						<th><b>Total (USD)</b></th>
					</tr>
					<tr>
						<th colspan="5">
							<hr style="border-color: black; margin: 0 0 0 0;">
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${debitCreditNote.items}"
						var="debitCreditNoteItem">
						<tr>
							<td></td>
							<td>${debitCreditNoteItem.description}</td>
							<td>${debitCreditNoteItem.invoiceNumber}</td>
							<td></td>
							<td></td>
						</tr>
						<c:forEach items="${debitCreditNoteItem.skuLines}"
							var="debitCreditNoteSkuLine" varStatus="status">
							<tr>
								<td>${debitCreditNoteSkuLine.skuCode}</td>
								<td>${debitCreditNoteSkuLine.description}</td>
								<td></td>
								<td>${debitCreditNoteSkuLine.sumForSku}</td>
								<td>
								<c:if test="${status.count eq fn:length(debitCreditNoteItem.skuLines)}">
   						            ${debitCreditNoteItem.total}
						        </c:if>
						        </td>
							</tr>
						</c:forEach>
					</c:forEach>
					<tr>
						<td colspan="5"><hr
								style="border-color: black; margin: 0 0 0 0;"></td>
					</tr>
					<tr>
						<td colspan="3"></td>
						<td class="text-right">Total</td>
						<td class="text-right"><u>$${debitCreditNote.total}</u></td>
					</tr>
					<tr>
						<td colspan="3"></td>
						<td class="text-right"><c:if
								test="${debitCreditNote.type eq 'DEBIT'}">
   						Total Amount of Debit
						</c:if> <c:if test="${debitCreditNote.type eq 'CREDIT'}">
   						Total Amount of Credit
						</c:if></td>
						<td class="text-right"><u>$${debitCreditNote.totalAmountOfDebitCredit}</u></td>
					</tr>
				</tbody>
			</table>
			<script language="javascript" type="text/javascript">
				$(document).ready(function() {
					$('.table-fixed-header').fixedHeader();
				});
			</script>
		</div>
	</div>
</div>