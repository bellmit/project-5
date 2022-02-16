<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<head>
<title>
	<spring:message code='company.companyInfoMaintenance' /> - DRS
</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='company.companyInfoMaintenance' />
				</div>
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
							<c:forEach items="${Company.serviceEmailList}" var="email">
								${email}<br>									                                                      									                                                     
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
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<spring:message code='company.note' />
			</div>
		</div>
	</div>
</div>