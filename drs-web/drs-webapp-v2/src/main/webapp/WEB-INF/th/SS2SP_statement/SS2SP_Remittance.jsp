<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='${displayText}' /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
         <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
         <span><spring:message code='${displayText}' /></span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code='${displayText}' />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">
					<spring:message code='ss2spStatement.period' />
					：${report.dateStart} ~ ${report.dateEnd}<br>
					<spring:message code='ss2spStatement.issuer' />
					：${companyKcodeToNameMap[report.isurKcode]}<br>
					<spring:message code='ss2spStatement.receiver' />
					：${companyKcodeToNameMap[report.rcvrKcode]}
				</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center"><spring:message
									code='ss2spStatement.dateSent' /></th>
							<th class="text-center"><spring:message
									code='ss2spStatement.dateReceived' /></th>
							<th><spring:message code='ss2spStatement.rmtReference' /></th>
							<th class="text-center"><spring:message
									code='ss2spStatement.currency' /></th>
							<th class="text-right"><spring:message
									code='ss2spStatement.rmtAmount' /></th>
						</tr>
					</thead>
					<c:forEach items="${report.items}" var="item">
						<tr>
							<td class="text-center">${item.dateSent}</td>
							<td class="text-center">${item.dateRcvd}</td>
							<td>${item.reference}</td>
							<td class="text-center"><spring:message
									code='${item.currency}' /></td>
							<td class="text-right">${item.amountStr}</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="3"></td>
						<td class="text-right"><b><spring:message
									code='remittance.total' /> <spring:message
									code='${report.currency}' /></b></td>
						<td class="text-right"><b>${report.total}</b></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>