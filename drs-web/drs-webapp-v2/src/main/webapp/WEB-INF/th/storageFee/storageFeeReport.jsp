<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
	<title>Import Amazon monthly storage fee report - DRS</title>
</head>
<div class="max-width">
	<div class="container-fluid">	
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Import Amazon monthly storage fee report</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<c:url var="action" value="AmazonStorageFeeReport/uploadFile"></c:url>
				<form method="POST" enctype="multipart/form-data" action="${action}">
					<table>
						<tr>
							<td class="h4">Warehouse</td>
							<td class="h4">Report file</td>
							<td></td>
						</tr>
						<tr>
							<td>
								<select class="form-control" name="warehouseId" style="width:150px;">
								<option value="">---select---</option>
								<c:forEach items="${warehouses}" var="warehouse">
									<option value="${warehouse.key}">${warehouse.displayName}</option>
								</c:forEach>
								</select>
							</td>
							<td><input type="file" name="file"></td>
							<td><input class="btn btn-primary" type="submit" value="Upload & Import" onclick="this.disabled=true;this.value='Sending, please wait...';this.form.submit();"></td>
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
						<div id="message" class="text-success">${message}</div>
					</div>
				</div>
			</div>
		</div>		
	</div>
</div>