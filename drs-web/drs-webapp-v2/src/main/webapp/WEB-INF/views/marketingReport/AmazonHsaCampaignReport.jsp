<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>Import Headline Search Ads report - DRS</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	jQuery(window).on("load", function(e) {

		jQuery("#startDateInput,#endDateInput").datepicker({
			beforeShow : function() {
				setTimeout(function() {
					$('.ui-datepicker').css('z-index', 200);
				}, 0);
			},
			dateFormat : 'yy-mm-dd'
		});

	});


</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Import HSA campaign report</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<c:url var="action" value="HsaCampaignReport/uploadFile"></c:url>

				<form method="POST" enctype="multipart/form-data" action="${action}">
					<table>
						<tr>
							<td class="h4">Marketplace</td>
							<td class="h4">Type</td>
							<td class="h4">Report file</td>
							<td></td>
						</tr>
						<tr>
							<td>
								<select class="form-control" name="marketplaceId" style="width:150px;">
								<option value="">---select---</option>
								<c:forEach items="${marketplaces}" var="marketplace">
									<option value="${marketplace.key}">${marketplace.name}</option>
								</c:forEach>
								</select>
							</td>
	                        <td>
								<select class="form-control" name="importType" style="width:150px;">
								<option value="0">Campaign</option>
								<option value="1">Campaign Video</option>
								<option value="2">Display</option>
								</select>
							</td>
							<td><input type="file" name="file"></td>
							<td><input class="btn btn-primary" type="submit" value="Import" onclick="this.disabled=true;this.value='Sending, please wait...';this.form.submit();"></td>

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