<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code='header.inventoryShipments' /> - DRS
</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>

<link href="<c:url value="/resources/css/tooltip.css"/>" type="text/css" rel="stylesheet">
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>
<script src="<c:url value="/resources/js/accounting.min.js"/>"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css">
<script src="https://cdn.datatables.net/fixedheader/3.1.2/js/dataTables.fixedHeader.min.js"></script>
<script src="https://cdn.datatables.net/plug-ins/1.10.16/sorting/absolute.js"></script>	

<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>

<script language="javascript" type="text/javascript">
	$(document).ready(function() {
		// if($(window).width() >= 1024){
		// $('#InventoryShipments').floatThead({
		// 	scrollingTop : $("#s5_menu_wrap").height(),
		// 	zIndex : 100
		// });
		// $('#InventoryShipments').on("floatThead", function(e, isFloated, $floatContainer) {
		// 	if (isFloated) {
		// 		$floatContainer.addClass("table-floated");
		// 	} else {
		// 		$floatContainer.removeClass("table-floated");
		// 	}
		// })};
		var report = $('#InventoryShipments').DataTable({
			ordering: false,
			searching: true,
			paging: false,
			info: false,
			responsive: true,
			fixedHeader: true,
			columnDefs: [{
				"targets":0,
				"visible":false,
				// "width":"100px"
			},{
				"targets":1,
				"visible":false,
				// "width":"100px"
			},{
				"targets":2,
				"visible":false,
				// "width":"100px"
			},{
				"targets":3,
				"visible":false,
				// "width":"100px"
			},{
				"targets":4,
				"visible":false,
				"width":"60px"
			},{
				// "type":numbersType,
				"targets":5,
				"visible":false,
				"width":"60px"
			},{
				"targets":6,
				"visible":false,
				"width":"40px"
			},{
				"targets":7,
				"visible":false,
				"width":"40px"
			},{
				"targets":8,
				"visible":false,
				// "width":"100px"
			}]
		});
		$('#InventoryShipments_filter').hide();
		report.column(0).visible(true);
		report.column(1).visible(true);
		report.column(2).visible(true);
		report.column(3).visible(true);
		report.column(4).visible(true);
		report.column(5).visible(true);
		report.column(6).visible(true);
		report.column(7).visible(true);
		report.column(8).visible(true);
		$( "#sellerSelector" ).select2({
		    theme: "bootstrap"
		});
		$('[data-toggle="tooltip"]').tooltip();	

        /*
        $("#submitImport").click(function() {
            var fileData = $("#importIVS").prop("files")[0];
            var form_data = new FormData();
            form_data.append("file", fileData);

            $.ajax({
                url : "${pageContext.request.contextPath}/InventoryShipments/ImportIVS",
                dataType : 'script',
                cache : false,
                contentType : false,
                processData : false,
                data : form_data,
                type : 'POST',
                success : function(data) {
                    $("#result-message").html(data);
                }
            });
            $("#importIVS").val("");
        });
        */

		$(".custom-file-input").on("change", function() {
			var fileName = $(this).val().split("\\").pop();
			$(this).siblings(".custom-file-label").addClass("selected").html(fileName);
		});

		var importMessage = $(".import-result-message").html();

		$('#importBtn').on('click', function() {
			console.log('click button');
			$(".import-result-message").html('');
			message = '';
			$('.import-result-wrapper').removeClass('show-result');
			$('.import-result-wrapper').removeClass('import-result-scroll');
			return true;
		})

		if (importMessage !== '') {
			$(".import-result-wrapper").addClass('show-result');
			$(".import-result-wrapper").addClass('import-result-scroll');

		} else {
			console.log('empty file')
		};
	});
</script>

<style>
#shipmentStatus {
	width: 136px;
}

#sellerSelector {
	width: 200px !important;
}

@media only screen and (max-width: 576px) {
  #shipmentStatus {
    width:100%
  }
}

.importArea {
	// float: right;
	width: 100%;
	display: flex;
	flex-direction: column;
	align-item: right;
	justify-content: flex-start;
	background-color: #f2f2f2;
	border: dashed 2px rgba(0,0,0, .2);
    padding: 22px;
}
.importform {
	display: flex;
	justify-content: center;
}
.import-result {
    margin: 1%;
}
.show-result {
	background-color: #f2f2f2;
    border: dashed 2px rgba(0,0,0, .2);
    margin: 1%;
    padding: 0;
}
.import-result-wrapper {
	display: flex;
	justify-content: center;
}
.import-result-scroll {
	max-height: 300px;
  	overflow-y: scroll;
}
.import-result-message {
    font-size: 15px;
    font-weight: 400;
    letter-spacing: 1.2px;
	line-height: 26px;
	margin: 0;
}
.import-btn {
	margin-left: 12px;
}
.custom-file {
    position: relative;
    display: inline-block;
	// width: 100%;
	width: 85%;
    height: calc(1.5em + .75rem + 2px);
    margin-bottom: 0;
}
.custom-file-label {
    position: absolute;
    top: 0;
    right: 0;
    left: 0;
    z-index: 1;
    height: calc(1.5em + .75rem + 2px);
    padding: .375rem .75rem;
    font-weight: 400;
    line-height: 1.5;
    color: #495057;
    background-color: #fff;
    border: 1px solid #ced4da;
	border-radius: .25rem;
	text-align: left;
}
.custom-file-input {
    position: relative;
    z-index: 2;
    width: 100%;
    height: calc(1.5em + .75rem + 2px);
    margin: 0;
	opacity: 0;
}
table.dataTable {    
    margin-top: 0px !important;
    margin-bottom: 0px !important;
}
table.dataTable.no-footer {
    border-bottom: 0px solid #111;
}
table.dataTable thead > tr > th.sorting_asc, table.dataTable thead > tr > th.sorting_desc, table.dataTable thead > tr > th.sorting, table.dataTable thead > tr > td.sorting_asc, table.dataTable thead > tr > td.sorting_desc, table.dataTable thead > tr > td.sorting {
    padding-right: 20px;
}
table.dataTable tfoot th, table.dataTable tfoot td {
    padding: 8px 10px;
    border-top: 2px solid #ccc;
}
table.fixedHeader-floating {
	top:48px !important;
}
table.dataTable thead th{
	border-bottom: 1px solid #eeeded;
}
.table > tbody > tr > td.dataTables_empty {
	border-top:0px;
}

</style>
</head>
<div class="max-width">
	<div class="text-center text-success">
		<h2>${message}</h2>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code='header.inventoryShipments' />
				</div>
			</div>
			<sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_CREATE']}')">
			<div class="col-md-6 text-right">
				<div class="page-heading">
					<a class="btn btn-primary" href="${pageContext.request.contextPath}/InventoryShipments/create">
						<spring:message code="inventoryShipment.createInventoryShipment"/>
					</a>
				</div>
			</div>	
			 </sec:authorize>		
		</div>

		<c:url var="action" value="/InventoryShipments"></c:url>
		<form:form class="form-inline mb-3" action="${action}" method="GET" name="InventoryShipment" modelAttribute="ShipmentIvsSearchCondition">
				
			<sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_SEARCH']}')">					
			<label for="sellerSelector" class="control-label mr-2"><spring:message code="inventoryShipment.seller" /></label>
				<form:select id="sellerSelector" path="sellerCompanyKcode" class="form-control mr-2 ">
					<form:option value="">All</form:option>
					<c:forEach var="sellerKcodeToName" items="${sellerKcodeToNameMap}">		
						<form:option value="${sellerKcodeToName.key}">${sellerKcodeToName.key} ${sellerKcodeToName.value}</form:option>																													
					</c:forEach>				
				</form:select>
			</sec:authorize>

			<label class="control-label my-2 mr-2 ml-md-2"><spring:message code="inventoryShipment.marketRegion" /></label>
			<form:select path="destinationCountry" class="form-control mr-md-2">
				<form:option value="">All</form:option>
				<c:forEach var="destinationCountry" items="${destinationCountryList}">		
					<form:option value="${destinationCountry}"><spring:message code="${destinationCountry}" /></form:option>																																							
				</c:forEach>
			</form:select>

			<label class="control-label my-2 mr-2"><spring:message code="inventoryShipment.shippingMethod" /></label>
			<form:select path="shippingMethod" class="form-control mr-md-2">
				<form:option value="">All</form:option>
				<c:forEach var="shippingMethod" items="${shippingMethodList}">		
					<form:option value="${shippingMethod}"><spring:message code="${shippingMethod}" /></form:option>																																							
				</c:forEach>				
			</form:select>

			<label class="control-label my-2 mr-2"><spring:message code="inventoryShipment.status" /></label>
			<form:select id="shipmentStatus" path="status" class="form-control mr-md-2">
				<form:option value="">All</form:option>
				<c:forEach var="shipmentStatus" items="${shipmentStatusList}">		
					<form:option value="${shipmentStatus}"><spring:message code="${shipmentStatus}" /></form:option>																																							
				</c:forEach>				
			</form:select>

			<div class="float-right">
				<button class="btn btn-primary mr-2 mt-2 mt-md-0 float-right" type="submit"><i class="fas fa-search"></i> <spring:message code="inventoryShipment.search" /></button>
			</div>

			<sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_CREATE']}')">
			<button
				type="button" class="btn btn-default" style="margin-left:10px;float:right;" 
				data-toggle="collapse"
				data-target=".import-box"
				aria-expanded="false"
				id='importBtn'>
				<i class="fas fa-upload" style='margin-right: 12px;'></i>Import Shipment</button>
			</sec:authorize>
		</form:form>

		<div class="row import-box collapse">
			<div class="col-md-12" style='margin-top: 20px;'>
			    <div class="importArea">
					<form
						id='importIVSForm'
						class='importform'
						accept-charset="UTF-8"
						method="POST"
						enctype="multipart/form-data"
						action="${pageContext.request.contextPath}/InventoryShipments/ImportIVS"
						
					>
						<table class="no-head" style='width: 100%;'>
							<tr style='width: 100%;'>
								<td><div class="custom-file">
									<input type="file" class="custom-file-input" name="file">
									<label class="custom-file-label" for="customFile"></label>
								</div></td>
								<td style="width:15%;">
									<input id='import-submit' class="btn btn-primary import-btn" type="submit" value="Upload & Import">
								</td>
								<td>${importTemplate}</td>
							</tr>						
						</table>
					</form>
				</div>
			</div>
		</div>
		<div class="row import-result-wrapper">
			<div class="text-success import-result">
				<p class="import-result-message">${importMessage}</p>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url value="InventoryShipments" var="URL">
							<c:forEach var="IvsSearchCondition" items="${IvsSearchConditions}">
								<c:param name="${IvsSearchCondition.key}" value="${IvsSearchCondition.value}" />
							</c:forEach>
						</c:url>
						<c:url var="firstUrl" value="${URL}&page=1" />
						<c:url var="lastUrl" value="${URL}&page=${totalPages}" />
						<c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
						<div class="text-center">
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}" data-pager="${currentPageIndex - 1}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="${URL}&page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="active page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}" data-pager="${currentPageIndex + 1}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}" data-pager="${totalPages}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</div>
					</c:when>
				</c:choose>				

				<div class="table-responsible">
				<table id="InventoryShipments" class="table table-floated" style='width: 100% !important;'>
					<thead>
						<tr>
							<th><spring:message code='inventoryShipment.inventoryShipmentId' /></th>
							<th><spring:message code='inventoryShipment.invoiceNumber' /></th>
							<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
								<th><spring:message code='inventoryShipment.seller' /></th>
								<th><spring:message code='inventoryShipment.buyer' /></th>
							</sec:authorize>
							<th class="text-center"><spring:message code='inventoryShipment.FCADeliveryDate' /></th>
							<th class="text-center"><spring:message code='inventoryShipment.expectedExportDate' /></th>
							<th class="text-center"><spring:message code='inventoryShipment.marketRegion' /></th>
							<th class="text-center"><spring:message code='inventoryShipment.shippingMethod' /></th>
							<th class="text-center"><spring:message code='inventoryShipment.status' /></th>
							<th class="text-right" ><spring:message code='inventoryShipment.totalAmount' /></th>
							<th class="text-right" ><spring:message code='inventoryShipment.paid' /></th>
							<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
								<th>UNS</th>
							</sec:authorize>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ShipmentOfInventoryList}" var="shipmentOfInventory">
							<tr>
								<td>
									<a href="${pageContext.request.contextPath}/InventoryShipments/${shipmentOfInventory.name}">${shipmentOfInventory.name}</a>											
								</td>
								<td>
								<c:choose>
									<c:when test="${empty shipmentOfInventory.invoiceNumber}">
										<span class="text-danger"><spring:message code='inventoryShipment.waitingInvoice' /></span>	
									</c:when>										
									<c:otherwise>
										${shipmentOfInventory.invoiceNumber}										
									</c:otherwise>
								</c:choose>								
								</td>
								<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
									<td>${shipmentOfInventory.sellerCompanyKcode} ${AllCompanyKcodeToNameMap[shipmentOfInventory.sellerCompanyKcode]}</td>
									<td>${shipmentOfInventory.buyerCompanyKcode} ${AllCompanyKcodeToNameMap[shipmentOfInventory.buyerCompanyKcode]}</td>
								</sec:authorize>
								<td class="text-center">${shipmentOfInventory.fcaDeliveryDate}</td>
								<td class="text-center">${shipmentOfInventory.expectedExportDate}</td>
								<td class="text-center"><spring:message code="${shipmentOfInventory.destinationCountry}" /></td>
								<td class="text-center"><spring:message code="${shipmentOfInventory.shippingMethod}" /></td>
								<td class="text-center"><spring:message code="${shipmentOfInventory.status}" /></td>
								<td class="text-right" >${shipmentOfInventory.total}</td>								
								<td class="text-right" ><a href="${pageContext.request.contextPath}/InventoryShipments/${shipmentOfInventory.name}/InventoryPayment">${shipmentOfInventory.paidTotal}</a></td>
								<sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
									<td class="text-right"><a href="${pageContext.request.contextPath}/UnifiedShipments/${shipmentOfInventory.unsName}/view">${shipmentOfInventory.unsName}</a></td>
								</sec:authorize>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url value="InventoryShipments" var="URL">
							<c:forEach var="IvsSearchCondition" items="${IvsSearchConditions}">
								<c:param name="${IvsSearchCondition.key}" value="${IvsSearchCondition.value}" />
							</c:forEach>
						</c:url>
						<c:url var="firstUrl" value="${URL}&page=1" />
						<c:url var="lastUrl" value="${URL}&page=${totalPages}" />
						<c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item"><a class="page-link" href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
											<li class="page-item"><a class="page-link" href="${prevUrl}" data-pager="${currentPageIndex - 1}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="${URL}&page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="active page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link" href="${pageUrl}" data-pager="${i}"><c:out value="${i}" /></a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}" data-pager="${currentPageIndex + 1}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}" data-pager="${totalPages}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
					</c:when>
				</c:choose>				
			</div>
		</div>
	</div>
</div>