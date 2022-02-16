<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code="ss2spStatement.serviceExpense" /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
         <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
         <span><spring:message code="ss2spStatement.serviceExpense" /></span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="ss2spStatement.serviceExpense" />
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
									code='ss2spStatement.itemName' /></th>
							<th class="text-center"><spring:message
									code='ss2spStatement.notes' /></th>
							<th class="text-right"><spring:message
									code='ss2spStatement.amount' /></th>									
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${report.items}" var="item">
							<tr>
								<td class="text-center"><spring:message
										code="${item.itemName}" /></td>
								<td class="text-center">${item.note}</td>
								<td class="text-right">${item.amountTotalStr}</td>
							</tr>							
						</c:forEach>						
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8"></div>
			<div class="col-md-2 text-right"><strong><spring:message
											code='ss2spStatement.subtotal' /></strong></div>
			<div class="col-md-2 text-right" style="padding-right:20px">
			 <strong>${report.subtotal}</strong>
			</div>					
		</div>
		<div class="row">
			<div class="col-md-8"></div>
			<div class="col-md-2 text-right"><strong><spring:message
											code='ss2spStatement.tax' /></strong></div>
			<div class="col-md-2 text-right" style="padding-right:20px">
			 <strong>${report.tax}</strong>
			</div>					
		</div>
		<div class="row">
			<div class="col-md-8"></div>
			<div class="col-md-2 text-right"><strong><spring:message
											code='ss2spStatement.totalAmount' /> <spring:message
											code='${report.currency}' /></strong></div>
			<div class="col-md-2 text-right" style="padding-right:20px">
			 <strong>${report.total}</strong>
			</div>					
		</div>
	</div>
</div>