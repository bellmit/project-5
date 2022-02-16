<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Market side transaction - DRS</title>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
$(document).ready(function() {
    $("body").tooltip({ selector: '[data-toggle=tooltip]' });
});
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Market side transaction</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<form method="POST" class="form-inline" action="${pageContext.request.contextPath}/MarketSideTransaction/actions">
					<div class="form-group">
						<select name="periodId" class="form-control" >
						<option value="">--- Select ---</option>
						<c:forEach items="${settlementPeriodList}" var="sp">
							<option value="${sp.id}">${sp.startDate} ~ ${sp.endDate}</option>
						</c:forEach>
						</select>
					</div>
					<button type="submit" class="btn btn-success" name="action" value="find" style="margin-right:0;">Generate</button>
					<button type="submit" class="btn btn btn-danger" name="action" value="delete" style="margin-right:0;">Delete</button>
					<button type="submit" class="btn btn-primary" name="action" value="process" style="margin-right:0;">Process</button>					
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Result</div>
			</div>
			<div class="col-md-12">
				<div class="panel panel-default">
					<div id="result" class="panel-body">
						<div id="message" class="text-success">${message}</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Non-processed transactions</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center">ID</th>
							<th class="text-center">UTC Date</th>
							<th>Type</th>
							<th>Source</th>
							<th class="text-center">source ID</th>
							<th>SKU</th>
							<th>Description</th>
							<th>Exception Message</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${nonProcessedTransactionList}" var="t">
						<tr>
							<td class="text-center">${t.id}</td>
							<td class="text-center">${t.transactionDate}</td>
							<td>${t.type}</td>
							<td>${t.source}</td>
							<td class="text-center">${t.sourceId}</td>
							<td>${t.sku}</td>
							<td>${t.description}</td>
							<td>${t.exceptionMessage}
								<label class="col-sm-4 col-form-label" data-html="true" data-toggle="tooltip"
								data-placement="top"
								title="${t.stackTracke}">
                            		<span class="fas fa-info-circle fa-1x" style="color:#428bca;"></span>
                                 </label>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>