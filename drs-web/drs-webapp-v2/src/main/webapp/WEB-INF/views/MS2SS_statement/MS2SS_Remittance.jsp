<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>${displayText}${kcodeToNameMap[report.isurKcode]} - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">${displayText}${kcodeToNameMap[report.isurKcode]}</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<p class="text-right">Period: ${report.dateStart} ~
					${report.dateEnd}</p>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>Date sent</th>
							<th>Date received</th>
							<th class="text-center">Currency</th>
							<th class="text-right">Remittance amount</th>
						</tr>
					</thead>
					<c:forEach items="${report.items}" var="item">
						<tr>
							<td>${item.dateSent}</td>
							<td>${item.dateRcvd}</td>
							<td class="text-center">${item.currency}</td>
							<td class="text-right">${item.amountStr}</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="2"></td>
						<td class="text-right"><b>Total ${currency} </b></td>
						<td class="text-right"><b>${report.total}</b></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>