<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<title>Amazon Search Term Report Import - DRS</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.spin.js"/>"></script>                
<script type='text/javascript' src="<c:url value="/resources/js/spin.js"/>"></script>          
<script>
	jQuery(window).on("load", function(e) {
		
		var target = document.getElementById('spin')
		var spinner = new Spinner(opts).spin(target);
		$('#spin').hide();
		var marketplace = "${marketplace}"
		$("#"+marketplace).prop("checked",true);
		getCampaignsAndSupplier(marketplace);		
		
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
			 top: '11px', // Top position relative to parent in px
			 left: '310px' // Left position relative to parent in px
			};
	
	function getCampaignsAndSupplier(marketplace){
			
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/AmazonSearchTermReport/getCampaignsToSupplierMap',
			contentType : "application/json; charset=utf-8",
			data : { marketplace : marketplace},
			dataType : "json",
			beforeSend: function() {				
				$('#spin').show(); 
			},
	        complete: function() { $('#spin').hide(); },
			success : function(campaignsToSupplierMap) {
				
				$("#campaignNameToSupplierKcode").find('tbody').empty();
				
				for (var campaignNameToSupplier in campaignsToSupplierMap["campaignNameToSupplierKcodeMap"]) {
																		
				var selectElement = $('<select class="form-control" name="'+campaignNameToSupplier+'" ></select>');
				$('<option>').val("").text("(ignore)").appendTo(selectElement);
								
				for(var supplierKcodeToName in campaignsToSupplierMap["supplierKcodeNameMap"]){
					
					if(supplierKcodeToName == campaignsToSupplierMap["campaignNameToSupplierKcodeMap"][campaignNameToSupplier]){
						
						$('<option>').val(supplierKcodeToName).text(supplierKcodeToName + " " + campaignsToSupplierMap["supplierKcodeNameMap"][supplierKcodeToName]).attr("selected", "selected").appendTo(selectElement);												
					
					}else{
						
						$('<option>').val(supplierKcodeToName).text(supplierKcodeToName + " " + campaignsToSupplierMap["supplierKcodeNameMap"][supplierKcodeToName]).appendTo(selectElement);							
						
					}
															
				}
						
				$("#campaignNameToSupplierKcode").find('tbody')
			    .append($('<tr>')
			        .append($('<td>')
			            .append(campaignNameToSupplier)
			        ).append($('<td>')
			            .append(selectElement)
			        )
			    );
							
				}
								
			}				

		});
				
	}		
</script>
</head>
<div class="max-width" ng-app="ShopifyReport" ng-controller="ShopifyReportCtrl">
	<div class="container-fluid">
		<div class="row"><div class="col-md-12"><div class="page-heading">Import Amazon Sponsored Product Ads - Consumer Search Term report</div></div></div>
		<div class="row">
			<div class="col-md-6">
				<form method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/AmazonSearchTermReport/uploadAndImport">
					<table>
						<tr>
							<td class="h4">Report file</td>
							<td class="h4">Marketplace</td>
							<td class="h4">Type</td>
							<td></td>
						</tr>
						<tr>
							<td><input type="file" name="file"></td>
							<td>
								<select class="form-control" name="marketplaceKey" style="width:150px;">
								<option value="">---select---</option>
								<c:forEach items="${marketplaces}" var="marketplace">
									<option value="${marketplace.key}">${marketplace.name}</option>
								</c:forEach>
								</select>
							</td>
							 <td>
                            	<select class="form-control" name="importType" style="width:150px;">
                            	<option value="0">DCP</option>
                            	<option value="1">ECM</option>
                            	</select>
                            </td>
							<td><input class="btn btn-primary" type="submit" value="Upload & Import" ></td>
						</tr>
					</table>
					<h4 class="text-success">${message}</h4>
				</form>
			</div>
		</div>		
		<div class="row"><div class="col-md-12"><div class="page-heading">Campaigns and its supplier</div></div></div>	
		<form method="POST" action="${pageContext.request.contextPath}/AmazonSearchTermReport/updateCampaignNameSupplierMap">				
		<div class="row">
			<div class="col-md-12">
				<c:forEach items="${marketplaces}" var="marketplace">
					<input id="${marketplace.key}" type="radio" name="marketplace" onclick="getCampaignsAndSupplier('${marketplace.key}')" value = '${marketplace.key}'> ${marketplace.name}
				</c:forEach>
				<span id ="spin"></span>						
			</div>
		</div>		
		<div class="row">
			<div class="col-md-6">
			<table id="campaignNameToSupplierKcode" style="padding:10px" >
				<thead>
					<tr>
						<th>Name</th>
						<th>Supplier</th>
					</tr>
				</thead>
				<tbody>										
				</tbody>
			</table>
				<input class="btn btn-primary" style="float:right" type="submit" value="Update"/>				
			</div>
		</div>			
		</form>
	</div>
</div>