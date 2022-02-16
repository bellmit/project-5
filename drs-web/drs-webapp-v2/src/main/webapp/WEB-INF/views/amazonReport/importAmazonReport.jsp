<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Amazon Report - DRS</title>
</head>
<div class="container">
	<div class="row">
		<div class="col-md-6">
			<h2>Links</h2>
		</div>
	</div>
	<div class="row">
	<div class="col-md-6">
		<table class="table">
			<thead>
				<tr>
					<th>Report name</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Amazon Inventory Health Report</td>
					<td><a href="${pageContext.request.contextPath}/amazon-inventory-health-report">link</a></td>
				</tr>
				<tr>
					<td>Amazon Monthly Storage Fee Report</td>
					<td><a href="${pageContext.request.contextPath}/AmazonStorageFeeReport">link</a></td>
				</tr>
				<tr>
					<td>Amazon Date Range Report</td>
					<td><a href="${pageContext.request.contextPath}/amazon-date-range-report">link</a></td>
				</tr>
			</tbody>
		</table>
	</div>
	</div>
</div>