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
		<div class="card">
			<!-- [todo] MOVE TO Nav bar in the future -->
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="card-header">
						<h4 class="card-title">
							<spring:message code='${displayText}' />
						</h4>
					</div>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12">
					<p class="text-right statement-info">
						<spring:message code='ss2spStatement.period' />：${report.dateStart} ~ ${report.dateEnd}<br>
						<spring:message code='ss2spStatement.issuer' />：${companyKcodeToNameMap[report.isurKcode]}<br>
						<spring:message code='ss2spStatement.receiver' />：${companyKcodeToNameMap[report.rcvrKcode]}
					</p>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table dataTable table-striped table-floated">
						<thead id="tableHeader" class="border-top border-bottom">
							<tr>
								<th class="text-left"><spring:message code='ss2spStatement.dateSent' /></th>
								<th class="text-left"><spring:message code='ss2spStatement.dateReceived' /></th>
								<th><spring:message code='ss2spStatement.rmtReference' /></th>
								<th class="text-left"><spring:message code='ss2spStatement.currency' /></th>
								<th class="text-right"><spring:message code='ss2spStatement.rmtAmount' /></th>
							</tr>
						</thead>
						<c:forEach items="${report.items}" var="item">
							<tr>
								<td class="text-left pl-20">${item.dateSent}</td>
								<td class="text-left pl-20">${item.dateRcvd}</td>
								<td class="pl-20">${item.reference}</td>
								<td class="text-left pl-18"><spring:message code='${item.currency}' /></td>
								<td class="text-right pr-18">${item.amountStr}</td>
							</tr>
						</c:forEach>
						<tr style="background-color: transparent !important; border-top: 1px solid #dee2e6;">
							<td colspan="3"></td>
							<td class="text-left pl-18 pt-15">
								<b><spring:message code='remittance.total'/> 
								<spring:message code='${report.currency}'/></b>
							</td>
							<td class="text-right pr-18 pt-15"><b>${report.total}</b></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>