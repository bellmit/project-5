<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<title>Amazon Date Range Report - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					Import Date Range Report
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<form accept-charset="UTF-8" method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/amazon-date-range-report/uploadAndImport">
					<table class="no-head">
						<tr>
							<td class="h4" style="text-align:left;">Report file</td>
							<td class="h4">Marketplace</td>
							<td></td>
						</tr>
						<tr>
							<td><input type="file" name="file"></td>
							<td>
								<select class="form-control" name="marketplaceReport" style="width:150px;">
								<c:forEach items="${marketplaces}" var="marketplace">
								<option value="${marketplace.key}">${marketplace.name}</option>
								</c:forEach>
								</select>
							</td>
							<td><input class="btn btn-primary" type="submit" value="Upload & Import" ></td>
						</tr>
					</table>
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
						<span class="text-success">
							&nbsp${message}
						</span>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					Import Statuses
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<table class="table" style="padding:10px" >
					<thead>
						<tr>
							<th>Marketplace</th>
							<th>Last Report date</th>
							<th>Last Imported On</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${importStatuses}" var="importStatus">
						<tr>
							<td>${importStatus.marketplaceId}</td>
							<td>${importStatus.dateTime}</td>	
							<td>${importStatus.importDate}</td>	
						</tr>
					</c:forEach>										
					</tbody>
				</table>								
			</div>
		</div>
	</div>
</div>