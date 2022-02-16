<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Marketing Report-DRS</title>
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
					<td>Amazon campaign performance report</td>
					<td><a href="${pageContext.request.contextPath}/AmazonCampaignPerformanceReport">link</a></td>
				</tr>
				<tr>
					<td>Amazon HSA Campaign Reports</td>
					<td><a href="${pageContext.request.contextPath}/HsaCampaignReport">link</a></td>
				</tr>
				<tr>
					<td>Amazon HSA Keyword Reports</td>
					<td><a href="${pageContext.request.contextPath}/HsaKeywordReport">link</a></td>
				</tr>
				<tr>
					<td>Amazon search term report</td>
					<td><a href="${pageContext.request.contextPath}/AmazonSearchTermReport">link</a></td>
				</tr>
				<tr>
					<td>Amazon detail page sales traffic by child item report</td>
					<td><a href="${pageContext.request.contextPath}/AmazonDetailPageSalesTrafficByChildItemReport">link</a></td>
				</tr>
				<tr>
					<td>Amazon daily page sales traffic report</td>
					<td><a href="${pageContext.request.contextPath}/AmazonDailyPageSalesTrafficReport">link</a></td>
				</tr>
			</tbody>
		</table>
	</div>
	</div>
</div>