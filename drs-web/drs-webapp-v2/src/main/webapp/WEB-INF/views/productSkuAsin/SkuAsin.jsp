<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>
<title>ASIN - DRS</title>
<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
</head>
<script>
$(function() {
	$('[data-toggle="tooltip"]').tooltip();	
	$("#supplierKcode").select2({
	    theme: "bootstrap" 
	});		
});	
</script>
<style>
.vertical-divider {
	border-right: 1px solid lightgray;
}

.desc-text {
	color: #5cb85c;
	ont-size: 14px;
}

/* The switch - the box around the slider */
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

/* Hide default HTML checkbox */
.switch input {display:none;}

/* The slider */
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #2CCFEE;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #ccc;
}

input:focus + .slider {
  box-shadow: 0 0 1px #ccc;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

</style>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">ASIN</div>
			</div>
		</div>
		<div class="row">
		<div class="col-md-8">
		<form class="form-inline" action="asin" method="post">
		<label class="mr-md-2">Marketplace</label>
		<select id="marketplaceId" class="form-control" name="marketplaceId">
								<option value="">---select---</option>
								<c:forEach items="${marketplaces}" var="marketplace">
									<option value="${marketplace.key}" ${marketplace.key==marketplaceId?'selected="selected"':''}>${marketplace.name}</option>
								</c:forEach>
							</select>
							
		<label class="mx-md-2">Supplier</label> 
		<select id="supplierKcode" class="form-control" name="supplierKcode" style="width:150px;">
								<option value="">---select---</option>
								<c:forEach items="${kcodeToSupplierNameMap}" var="kcodeToSupplierName">
									<option value="${kcodeToSupplierName.key}" ${kcodeToSupplierName.key==supplierKcode?'selected="selected"':''}> ${kcodeToSupplierName.key} ${kcodeToSupplierName.value}</option>
								</c:forEach>
							</select>
		
		<input class="btn btn-primary ml-md-2 mt-2 mt-md-0" type="submit" value="Search">
		<small class="form-text text-muted">* Please turn off the storage fee flag for duplicate ASINs and FNSKUs so the supplier is not double-charged for storage fees.</small>
		</form>
		
		
		
				<div class="table-responsive">
				<table class="table table-bordered">
					<thead>						
						<tr>
							<th class="h4">SKU</th>
							<th class="h4">Marketplace SKU</th>
							<th class="h4">Asin</th>
							<th class="h4" data-toggle="tooltip" data-placement="top" data-container="body" title="Fulfillment Network SKU">FNSKU</th>
							<th class="h4" data-toggle="tooltip" data-placement="top" data-container="body" title="Amazon-fulfilled Network Listing Exists?">AFN Listing</th>	
							<th class="h4" data-toggle="tooltip" data-placement="top" data-container="body" title="Merchant-fulfilled Network Listing Exists?">MFN Listing</th>		
							<th class="h4" data-toggle="tooltip" data-placement="top" data-container="body" title="Click to switch the flag On or Off">Include in storage fee?</th>					
						</tr>	
					</thead>
					<tbody id="skuAsinTable">
						<c:forEach var="skuFnskuAsin" items="${skuFnskuAsinList}">
							<tr>
								<td>${skuFnskuAsin.sku}</td>
								<td class="mSku">${skuFnskuAsin.marketplaceSku}</td>
								<td>${skuFnskuAsin.asin}</td>
								<td>${skuFnskuAsin.fnsku}</td>
								<td>${skuFnskuAsin.afn}</td>
								<td>${skuFnskuAsin.mfn}</td>
								<td class="toggleStorage">
									<c:choose>
									<c:when test="${skuFnskuAsin.storage}">
										<label class="switch">
										  <input type="checkbox">
										  <span class="slider"></span>
										</label>
									</c:when>
									<c:otherwise>
										<label class="switch">
										  <input type="checkbox" checked>
										  <span class="slider"></span>
										</label>
									</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>						
					</tbody>					
				</table>
				</div>
				<input type="hidden" id="marketplaceSku" name="marketplaceSku" value="" />
				</form>
<script>
$(function() {
	$('.toggleStorage').on('mousedown', function() {
		$('#marketplaceSku').val($(this).siblings('.mSku').text());
	});
	
	$('.toggleStorage .slider').on('click', function() {
		console.log("slider");
		$.ajax({
			type : "post",
			url : "${pageContext.request.contextPath}/asin/storageFeeFlag",
			data : {
				marketplaceSku : $('#marketplaceSku').val(),
				marketplaceId : $('#marketplaceId').val()
			}
		});
		var buttonText = $(this).val();
		if (buttonText == "Yes") {
			$(this).val("No");
		} else {
			$(this).val("Yes");
		}
	});
});
</script>


<form method="POST" enctype="multipart/form-data" >
<small class="text-muted">File name must contain the marketplace (ex. US, UK, CA) </small>
<div class="row">
<div class="col-md-7">
<select id="marketplaceReport" class="form-control" name="marketplaceReport" >
								<option value="">---select---</option>
								<c:forEach items="${marketplaces}" var="marketplace">
									<option value="${marketplace.key}" ${marketplace.key==marketplaceId?'selected="selected"':''}>${marketplace.name}</option>
								</c:forEach>
							</select>
</div>
<div class="col-md-5">
<input type="file" name="fileReport" class="mt-2 mt-md-0">
</div>
</div>
<div class="row">
<div class="col">

<input class="btn btn-primary mt-2" type="submit" formaction="asin/importFbaFile" value="Add ASIN from FBA report" />
<input class="btn btn-primary mt-2" type="submit" formaction="asin/importInvFile" value="Add ASIN from Inventory report" />
</div>
</div>
 
							
							
							
							<span id="message" class="text-success">${message}</span>
							
</form>	

 
												
			</div>
			<div class="col-md-4 mt-sm-3 mt-md-0">
				<div class="page-heading" style="padding-top:0px">SKU without Asin</div>				
				<table class="table table-bordered">
					<thead>						
						<tr>
							<th class="h4">Marketplace</th>
							<th class="h4">SKU</th>							
						</tr>	
					</thead>
					<tbody>
						<c:forEach var="marketplaceToSku" items="${marketplaceToSkuList}">
							<tr>
								<td>${marketplaceToSku.marketplace}</td>
								<td>${marketplaceToSku.sku}</td>
							</tr>
						</c:forEach>						
					</tbody>					
				</table>
			</div>		
		</div>
	</div>
</div>		
		