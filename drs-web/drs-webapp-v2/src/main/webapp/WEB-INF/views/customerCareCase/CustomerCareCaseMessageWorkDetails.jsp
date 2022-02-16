<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="ccc.id" />: ${caseId} - <spring:message code="ccc.msg" /> #${Message.lineSeq} - DRS
</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="ccc.id" /> : ${caseId} - <spring:message code="ccc.msg" />#${Message.lineSeq}
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
			<div class="col-md-6 ">
				<div class="col-sm-4 text-right">
					<b><spring:message code="ccc.workDetails" /></b>
				</div>
			</div>
		</div>
		<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
			<div class="col-md-6">
				<table class="table table-withoutBorder">
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_VIEWMSG']}')">							
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.MS2SS_statementId" /></b>
						</td>
						<td>
							<c:choose>
								<c:when test="${empty Message.ms2ssStatementId}">
 									<c:choose>
										<c:when test="${Message.isFreeOfCharge eq 'true' }">
											n/a
										</c:when>
										<c:otherwise>
											(TBD)	
										</c:otherwise>
									</c:choose>																 
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${Message.isFreeOfCharge eq 'true' }">
											n/a
										</c:when>												
										<c:otherwise>
											<a href="${pageContext.request.contextPath}/MS2SS-Statements/${Message.ms2ssStatementId}">${Message.ms2ssStatementId}</a>																							
										</c:otherwise>
									</c:choose>																	
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					</sec:authorize>
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.SS2SP_statementId" /></b>
						</td>
						<td>
							<c:choose>
								<c:when test="${empty Message.ss2spStatementId}">
 									<c:choose>
										<c:when test="${Message.isFreeOfCharge eq 'true' }">
											n/a
										</c:when>
										<c:otherwise>
											(TBD)	
										</c:otherwise>
									</c:choose>						 
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${Message.isFreeOfCharge eq 'true' }">
											n/a	
										</c:when>
										<c:otherwise>
											<a href="${pageContext.request.contextPath}/${statementRootUrl}/${Message.ss2spStatementId}">${Message.ss2spStatementId}</a>																						
										</c:otherwise>
									</c:choose>																											
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="text-right"><b><spring:message code="ccc.endTime" /></b></td>
						<td>${Message.endDate}</td>
					</tr>
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_VIEWMSG']}')">																
					<tr>
						<td class="text-right"><b><spring:message code="ccc.timeTaken" /></b></td>
						<td>${Message.timeTaken}</td>
					</tr>
					</sec:authorize>
					<tr>
						<td class="text-right"><b><spring:message code="ccc.relatedSku" /></b></td>
						<td><c:if test="${empty productBaseCodeToSupplierNameMap}">
								${Message.chargeToSKU} ${productSkuCodeToSupplierNameMap[Message.chargeToSKU]}
							</c:if> 
							<c:if test="${empty productSkuCodeToSupplierNameMap}">
								${Message.chargeToSKU} ${productBaseCodeToSupplierNameMap[Message.chargeToSKU]}
							</c:if>
						</td>
					</tr>
				</table>
			</div>
			<div class="col-md-6">
				<table class="table table-withoutBorder">					
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.wordCount" /></b>
						</td>
						<td>${Message.wordCount}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.standardAction" /></b>
						</td>
						<td>${Message.standardActionCount}</td>
					</tr>
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_VIEWMSG']}')">												
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.chargeByWord" /></b>
						</td>
						<td>${Message.DRSChargeByWord}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.costPerHour" /></b>
						</td>
						<td>${Message.costPerHour}</td>
					</tr>
					</sec:authorize>
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.handledBy" /></b>
						</td>
						<td>${Message.createBy}</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.translationFee" /></b>
						</td>
						<td>
							<c:choose>
								<c:when test="${Message.includesTranslationFee eq 'true' }">
									<spring:message code="ccc.yes" />
								</c:when>
								<c:otherwise>
									<spring:message code="ccc.no" />	
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="text-right">
							<b><spring:message code="ccc.freeOfCharge" /></b>
						</td>
						<td>
							<c:choose>
								<c:when test="${Message.isFreeOfCharge eq 'true' }">
									<spring:message code="ccc.yes" />
								</c:when>
								<c:otherwise>
									<spring:message code="ccc.no" />	
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>