<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title><spring:message code='ss2spStatement.customerCareCaseItemsTitle' /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script>
	$(document).ready(function() {
		document.addEventListener("scroll", (e) => {
			console.log('scroll');
			const $table = $("#SS2SP-CustomerCareCaseItems");
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
         <a href="${pageContext.request.contextPath}/${rootUrl}/${statementName}/profitshare?country=${country}#customerCareCase"><spring:message code='ss2spStatement.profitShare' /></a> <div class="bnext"><img src="<c:url value="/resources/images/bnext.png"/>"></div>                                            
         <span><spring:message code='ss2spStatement.customerCareCaseItemsTitle' /></span>  
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
						<spring:message code='ss2spStatement.customerCareCaseItemsTitle' />
					</div>
				</div>
			</div> -->
			<div class="row">
				<div class="col-md-12">
					<p class="text-right statement-info">
						<spring:message code='ss2spStatement.period' />: ${report.dateStart} ~ ${report.dateEnd}<br>
						<spring:message code='ss2spStatement.issuer' />: ${companyKcodeToNameMap[report.isurKcode]}<br>
						<spring:message code='ss2spStatement.receiver' />: ${companyKcodeToNameMap[report.rcvrKcode]}
					</p>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="card-body">
						<table id="SS2SP-CustomerCareCaseItems" class="table dataTable table-striped table-floated">
							<thead id="tableHeader" class="border-top border-bottom">
								<tr>
									<th><spring:message code='ss2spStatement.customerCareCaseItemsTime' /></th>
									<th>SKU</th>
									<th><spring:message code='ss2spStatement.sourceItem' /></th>
									<th class="text-center"><spring:message code='ss2spStatement.sourceCurrency' /></th>
									<th class="text-right"> <spring:message code='ss2spStatement.sourceAmount' /> </th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${report.items}" var="item">
									<tr>
										<td class="pl-20">${item.dateTimeUtc}</td>
										<td class="pl-20">${item.productSku}</td>
										<td class="pl-20">
											<a href="${pageContext.request.contextPath}/CustomerCareCases/${item.caseId}#messageId${item.messageLineSeq}">
												<spring:message code='ss2spStatement.case' /> ${item.caseId} <spring:message code='ss2spStatement.message' /> ${item.messageLineSeq}
											</a>
										</td>
										<td class="text-center"><spring:message code='${item.currency}' /></td>
										<td class="text-right pr-18">${item.amountStr}</td>
									</tr>
								</c:forEach>
								<tr style="background-color: transparent !important; border-top: 1px solid #dee2e6;">
									<td colspan="3"></td>
									<td class="text-center pt-15"><b><spring:message code='ss2spStatement.totalSourceAmount' /></b></td>
									<td class="text-right pr-18 pt-15"><b>${report.amountTotal}</b></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>