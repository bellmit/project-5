<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="remittance.title" /> - DRS
</title>

<script>
	function deleteConfirm(id) {
		$("#dialog-confirm").html("<spring:message code="remittance.deleteWarning" /> ");
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [{
				text : "<spring:message code='remittance.yes' /> ",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/Remittance/delete/"+ id;
				}
			},
			{
				text : "<spring:message code='remittance.no' /> ",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});
	}
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6" style="margin: 0px 25%;">
				<div class="page-heading text-center">
					<spring:message code='remittance.title' />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6" style="margin: 0px 25%;">
				<table class="table no-head">
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.id' /></b>
						</td>
						<td>${Remittance.remittanceId}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.dateSent' /></b>
						</td>
						<td>${Remittance.utcDateSent}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.dateReceived' /></b>
						</td>
						<td>${Remittance.utcDateReceived}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.sender' /></b>
						</td>
						<td>
							${Remittance.sender} ${SenderAndReceiverCompanies[Remittance.sender]}
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.receiver' /></b>
						</td>
						<td>
							${Remittance.receiver}
							${SenderAndReceiverCompanies[Remittance.receiver]}
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.currency' /></b>
						</td>
						<td><spring:message code='${Remittance.currency}' /></td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.amount' /></b>
						</td>
						<td>${Remittance.amount}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.feeAmount' /></b>
						</td>
						<td>${Remittance.feeAmount}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.feeIncluded' /></b>
						</td>
						<td>${Remittance.feeIncluded}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.statementName' /></b>
						</td>
						<td>${Remittance.statementName}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.bankPayment' /></b>
						</td>
						<td>${Remittance.bankPayment}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='remittance.reference' /></b>
						</td>
						<td>${Remittance.reference}</td>
					</tr>
					<tr>
						<td colspan="2">
							<div style="float: right">
								<sec:authorize access="hasAnyRole('${auth['REMITTANCE_EDIT']}')">
									<a class="btn btn-primary" href="${pageContext.request.contextPath}/Remittance/${Remittance.remittanceId}/edit">
										<spring:message code='remittance.edit'/> 
									</a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['REMITTANCE_DELETE']}')">
									<spring:message code='remittance.delete' var='delete' />
									<c:choose>
										<c:when test="${Remittance.utcDateReceived ne ''}">
											<input class="btn btn-link" type="button" value="${delete}" onclick="deleteConfirm('${Remittance.remittanceId}');" />
										</c:when>
									</c:choose>
								</sec:authorize>
								<div id="dialog-confirm"></div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>