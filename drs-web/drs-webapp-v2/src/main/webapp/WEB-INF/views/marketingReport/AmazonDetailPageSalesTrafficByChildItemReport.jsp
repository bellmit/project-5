<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<title>Amazon detail page sales traffic - DRS</title>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.spin.js"/>"></script>                
<script type='text/javascript' src="<c:url value="/resources/js/spin.js"/>"></script> 
<script>	
		var importedSettlementPeriodIdList = '${importedSettlementPeriodIdList}';

		jQuery(window).on("load", function(e) {
			
		 	var target = document.getElementById('spin')
			var spinner = new Spinner(opts).spin(target);
		 	$('#spin').hide();
			var filterMarketplaceId = '${filterMarketplaceId}';
			if(filterMarketplaceId!=null&&filterMarketplaceId.trim()!=''){
				$("input[name=filterMarketplaceId][value="+filterMarketplaceId+"]").attr('checked', 'checked');
				updateSettlementPeriodImportingStatuses(filterMarketplaceId);
			}
		});
		
		var opts = {
				lines: 13, // The number of lines to draw
				length: 2, // The length of each line
				width: 2, // The line thickness
				radius: 5, // The radius of the inner circle
				corners: 1, // Corner roundness (0..1)
				rotate: 0, // The rotation offset
				direction: 1, // 1: clockwise, -1: counterclockwise
				color: '#000', // #rgb or #rrggbb or array of colors
				speed: 1, // Rounds per second
				trail: 59, // Afterglow percentage
				shadow: false, // Whether to render a shadow
				hwaccel: false, // Whether to use hardware acceleration
				className: 'spinner', // The CSS class to assign to the spinner
				zIndex: 2e9, // The z-index (defaults to 2000000000)
				top: '103px', // Top position relative to parent in px
				left: '350px' // Left position relative to parent in px
				};
				
function updateSettlementPeriodImportingStatuses(filterMarketplaceId){
	$.ajax({
		type : 'get',
		url : '${pageContext.request.contextPath}/AmazonDetailPageSalesTrafficByChildItemReport/getSettlementPeriodImportingStatuses',
		contentType : "application/json; charset=utf-8",
		data : { marketplaceId : filterMarketplaceId},
		dataType : "json",
		beforeSend: function() {			
			$('#spin').show(); 
		},
		complete: function() {
			$('#spin').hide();
		},
		success : function(settlementPeriodImportingStatuses) {
			var $importingStatusTableTbody = $("#importingStatusTable").find('tbody'); 
			$importingStatusTableTbody.empty();
			$.each(settlementPeriodImportingStatuses,function(key,settlementPeriodImportingStatus){
				var $tdPeriodStart = $('<td>').addClass('text-center').addClass('col-md-2').text(settlementPeriodImportingStatus.period.startDate);
				var $tdPeriodEnd   = $('<td>').addClass('text-center').addClass('col-md-2').text(settlementPeriodImportingStatus.period.endDate);
				var $tdAction = $('<td>').attr('class','text-center');
				if(settlementPeriodImportingStatus.isImported){
					var $deleteLink = $('<a>').text('DELETE').attr('href','${pageContext.request.contextPath}/AmazonDetailPageSalesTrafficByChildItemReport/delete?marketplaceId='+filterMarketplaceId+'&periodId='+settlementPeriodImportingStatus.period.id);
					$tdAction.append($deleteLink);
				}
				else{
					$tdAction.text('---');
				}
				var $tr = $('<tr>').append($tdPeriodStart).append($tdPeriodEnd).append($tdAction);
				$importingStatusTableTbody.append($tr);
			});
		}
	});
					
}
function getUnOccupiedSettlementPeriodList(marketplaceId){
	$.ajax({
		type: 'get',
		url: '${pageContext.request.contextPath}/AmazonSearchTermReport/getUnOccupiedSettlementPeriods',
		contentType : "application/json; charset=utf-8",
		data: { marketplaceId : marketplaceId},
		dataType: "json",
		success: function(nonOccupiedSettlementPeriods) {
			var unOccupiedSettlementPeriodDropdown = $('#unOccupiedSettlementPeriodDropdown');
			unOccupiedSettlementPeriodDropdown.empty()
			unOccupiedSettlementPeriodDropdown.append($('<option>').text("-- select --").val(""));
			$.each(nonOccupiedSettlementPeriods,function(key,value){
				unOccupiedSettlementPeriodDropdown.append($('<option>').text(value.startDate+"~"+value.endDate).val(value.id));
			});
		}
	});
}
</script>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>
		<div class="row"><div class="col-md-12"><div class="page-heading">Import Amazon detail page sales traffic by child item report</div></div></div>
		<div class="row">
			<div class="col-md-12">
				<form method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/AmazonDetailPageSalesTrafficByChildItemReport/upload">
					<table>
						<tr>
							<td class="h4">Report file</td>
							<td class="h4">Marketplace</td>
							<td class="h4">Unoccupied periods</td>
							<td></td>
						</tr>
						<tr>
							<td><input  type="file" name="file" ></td>
							<td>
								<select class="form-control" name="marketplaceId" style = "width:150px;" onChange="getUnOccupiedSettlementPeriodList(this.options[this.selectedIndex].value)" >
								<option value="">---select---</option>
								<c:forEach var="marketplace" items="${marketplaces}" >
									<option value="${marketplace.key}">${marketplace.name}</option>
								</c:forEach>
								</select>
							</td>
							<td>
								<select id="unOccupiedSettlementPeriodDropdown" class="form-control" name="periodId" style="width:200px;">
								<option value="">-- select --</option>
								</select>
							</td>
							<td>
								<input class="btn btn-primary" type="submit" value="Upload & Import" style="display: inline;">
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">Importing Status</div>
				<c:forEach var="marketplace" items="${marketplaces}" >
				<label>
					<input type="radio" style="vertical-align:middle;" name="filterMarketplaceId" value="${marketplace.key}" onclick="updateSettlementPeriodImportingStatuses(this.value)" > ${marketplace.name}
				</label>
				</c:forEach>
				<span id ="spin"></span>
				<table id="importingStatusTable" class="table">
					<thead>
					<tr>
						<th class="text-center col-md-4">Settlement period</th>
						<th class="text-center">Status</th>
						<th class="text-center">Action</th>
					</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>