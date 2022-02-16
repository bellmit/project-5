<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>${Company.shortNameLocal} - DRS</title>

<script>
	function deleteConfirm(KCode) {
		$("#dialog-confirm").html("<spring:message code="company.deleteWarning" />");
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
				text : "<spring:message code='company.yes' />",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/Companies/" + KCode + "/delete";
				}
			},
			{
				text : "<spring:message code='company.no' />",
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
			<div class="col-md-12">
				<div class="page-heading">${Company.shortNameLocal}</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-right">
							<b><spring:message code='company.k_code' /></b>
						</td>
						<td>${Company.kcode}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.companyName' /></b>
						</td>
						<td>${Company.nameLocal}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.companyNameEng' /></b>
						</td>
						<td>${Company.nameEnUs}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.shortName' /></b>
						</td>
						<td>${Company.shortNameLocal}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.shortNameEng' /></b>
						</td>
						<td>${Company.shortNameEnUs}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.address' /></b>
						</td>
						<td>${Company.address}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.phoneNumber' /></b>
						</td>
						<td>${Company.phoneNumber}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.serviceMailAddress' /></b>
						</td>
						<td>
							<c:forEach items="${Company.serviceEmailList}" var="serviceEmail">
								${serviceEmail}<br>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.productContactEmailAddress' /></b>
						</td>
						<td>
							<c:choose>
								<c:when test="${Company.productEmailList.size() ne null}">
									<c:forEach items="${Company.productEmailList}" var="productEmail">
										${productEmail}<br>
									</c:forEach>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.couponKeyword' /></b>
						</td>
						<td>
							<c:choose>
								<c:when test="${Company.couponList.size() ne null}">
									<c:forEach items="${Company.couponList}" var="couponKeyword" varStatus="couponKeywordStatus">
										<c:choose>
											<c:when test="${Company.couponList.size() ne couponKeywordStatus.count}">
												${couponKeyword},
											</c:when>
											<c:otherwise>
												${couponKeyword}
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:when>
								<c:otherwise>
									-
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
			<div class="col-md-6">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-right">
							<b><spring:message code='company.country' /></b>
						</td>
						<td>${Company.countryCode}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.currency' /></b>
						</td>
						<td>${Company.currency}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.${Company.countryCode}_officialRegistrationNoType' /></b>
						</td>
						<td>${Company.officialRegistrationNo}</td>
					</tr>
					<tr>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.bankAccountInfo' /></b>
						</td>
						<td></td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.bankName' /></b>
						</td>
						<td>${Company.bankName}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.bankBranchName' /></b>
						</td>
						<td>${Company.bankBranchName}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.bankAccountNumber' /></b>
						</td>
						<td>${Company.accountNumber}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code='company.bankAccount' /></b>
						</td>
						<td>${Company.accountName}</td>
					</tr>
                    <tr>
                       <td class ="text-right">
                         <b><spring:message code='company.activated' /></b>
                       </td>
                       <td>${Company.activated} </td>
					</tr>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-withoutBorder">
					<tr>
						<td class="text-left">
							<label class="control-label"><spring:message code='company.notes' /></label>
						</td>
					</tr>
					<tr>
						<td>
							<div class="panel panel-default">
								<div id="result" class="panel-body">${Company.notes}</div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
			<div class="col-md-12 text-right">
				<a class="btn btn-primary" href="${pageContext.request.contextPath}/Companies/${Company.kcode}/edit">
					<spring:message code='company.edit' />
				</a>
				<input class="btn btn-link" type="button" value="<spring:message code='company.delete' />" onclick="deleteConfirm('${Company.kcode}');" />
				<div id="dialog-confirm"></div>
			</div>
		</div>
	</div>
</div>