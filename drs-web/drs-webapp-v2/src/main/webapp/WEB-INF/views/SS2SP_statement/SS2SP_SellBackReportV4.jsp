<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='${report.title}' /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script>
	$(document).ready(function() {
		document.addEventListener("scroll", (e) => {
			console.log('scroll');
			const $table = $("#SS2SP-SellBckReport");
			const os = window.navigator.userAgent;
			if (os.indexOf("Linux") != -1 || os.indexOf("Mac") != -1) {
				if ($table.offset().top >= 0) {
					$table.floatThead();
				} else {
					$table.floatThead("destroy");
				}
				} else {
				if ($table.offset().top <= 0) {
					$table.floatThead();
				} else {
					$table.floatThead("destroy");
				}
			}
		}, true);
	});
</script>
</head>
<div class="row">
    <div class="col-md-12 max-width" >   
    <div class="breadcrumb">
         <a href="${pageContext.request.contextPath}"><spring:message code="productVersion.homepage" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div> 
         <a href="${pageContext.request.contextPath}/${rootUrl}"><spring:message code="ss2spStatement.statements" /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                 
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}">${statementName}</a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                          
         <span><spring:message code='${report.title}' /></span>  
    </div>
    </div>
</div>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<!-- [todo] MOVE TO Nav bar in the future -->
			<!-- <div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<spring:message code='${report.title}' />
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
					<table id="SS2SP-SellBckReport" class="table dataTable table-striped table-floated">
						<thead id="tableHeader" class="border-top border-bottom">
							<tr>
								<th><spring:message code='ss2spStatement.ivsName' /></th>
								<th>SKU</th>
								<th><spring:message code='ss2spStatement.skuName' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.itemType' /></th>
								<th class="text-center"><spring:message code='ss2spStatement.amountPerUnit' /></th>
								<th class="text-right"><spring:message code='ss2spStatement.quantity' /></th>
								<th class="text-right"><spring:message code='ss2spStatement.amount' /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${report.lineItems}" var="item">
								<tr>
									<td class="pl-20">${item.ivsName}</td>
									<td class="pl-20">${item.sku}</td>
									<td class="pl-20">${item.skuName}</td>
									<td class="text-center pl-20"><spring:message code='${item.itemType}' /></td>
									<td class="text-right pr-18">${item.unitAmount}</td>
									<td class="text-right pr-18">${item.quantity}</td>
									<td class="text-right pr-18">${item.subtotal}</td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="5"></td>
								<td class="text-right pl-18 pt-15"><strong><spring:message code='ss2spStatement.totalAmount' /> <spring:message code='${report.currency.name()}' /></strong></td>
								<td class="text-right pr-18 pt-15"><strong>${report.total}</strong></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="row" style="padding-top:25px;"></div>
			<div class="row">
				<div class="col-md-12" style="font-size: 14px; color: #535353;">
					<spring:message code="ss2spStatement.reference" />
				</div>
			</div>
		</div>
	</div>
</div>