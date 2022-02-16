<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title><spring:message code="productVersion.title" /> - DRS</title>
<link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.min.js"></script>
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>

<script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
<script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>

<script type='text/javascript' src="<c:url value="/resources/js/angular-datatables.min.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
<script>
	$(document).ready(function() {
		$('#baseProduct').floatThead({
			scrollingTop : $("#s5_menu_wrap").height(),
			zIndex : 100
		});
		$('#baseProduct').on("floatThead", function(e, isFloated, $floatContainer) {
			if (isFloated) {
				$floatContainer.addClass("table-floated");
			} else {
				$floatContainer.removeClass("table-floated");
			}
		});		
		$('#bp-filter').val('${companyKcode}');	
		$('#bp-filter').on('change', function () {
	          var kcode = $(this).val();
	          if (kcode) {
	              window.location = "${pageContext.request.contextPath}/NewDevelopingProductList?kcode="+kcode;
	          }
	          return false;
	    });
		$( "#bp_filter" ).select2({
		    theme: "bootstrap"
		});

	});

	/*
	jQuery(window).on("load", function(e) {
		var productList = ${baseProductsJson};
		$.each(productList, function(key, product){
			$("#"+product.baseProduct+"List").click();
		});
	});
	*/

	angular.module('manageProductList', ['datatables']).controller('manageProductListCtrl', function($scope, DTOptionsBuilder, DTColumnBuilder,$compile) {

		$scope.dtInstance = {};
		$scope.dtOptions = DTOptionsBuilder.newOptions()
		  //.withOption('order', [0, 'asc'])
		  .withOption('ordering', false)
		  .withOption('searching', false)
		  .withOption('paging', false)
		  .withOption('info', false)
		  .withOption('fixedHeader', true);

		$scope.productList = ${baseProductsJson};
		$scope.regions = new Array();
		$scope.regions["US"] = '<spring:message code="US" />';
		$scope.regions["UK"] = '<spring:message code="UK" />';
		$scope.regions["CA"] = '<spring:message code="CA" />';
		$scope.regions["DE"] = '<spring:message code="DE" />';
		$scope.regions["FR"] = '<spring:message code="FR" />';
		$scope.regions["IT"] = '<spring:message code="IT" />';
		$scope.regions["ES"] = '<spring:message code="ES" />';

		$scope.showSKUInfo = function(product, event){

			var scope = $scope.$new(true);
		      	scope.product  = product;

		    var link = angular.element(event.currentTarget),
	          	icon = link.find('.fas'),
	          	tr = link.parent().parent(),
	          	table = $scope.dtInstance.DataTable,
	          	row = table.row(tr);

		    if (row.child.isShown()) {
		    	icon.removeClass('fas fa-chevron-circle-down').addClass('fas fa-chevron-circle-right'); 
		        row.child.hide();
		        tr.removeClass('shown');
		      }else {
		    	  icon.removeClass('fas fa-chevron-circle-right').addClass('fas fa-chevron-circle-down');
		        row.child( $scope.listDetails(product) ).show();
		        tr.addClass('shown');
		      }
		}

		$scope.listDetails = function(data){
			if (!data) return ''

			var detailsHTMLString =
				'<table id="sku-list-table" style="width:100%;">'+
				'<thead>'+
					'<tr>'+
						'<th class="sku-head" style:"width:50%;">'+'<spring:message code="productVersion.variationProduct" />'+'</th>'+
						'<th class="sku-head" style:"width:50%;">'+'<spring:message code="productVersion.appliedRegion" />'+
					'</tr>'+					
				'</thead>';

			$.each(data.variationProducts, function(productKey, product){

				var regionListString = '';

				$.each(product.applicableRegionList, function(regionKey, region){

					regionListString += $scope.regions[region.replace(/"/g,'')]+ '&nbsp&nbsp';

				});

				detailsHTMLString +=
					'<tr class="sku-row">'+
		               '<td class="sku-td" style:"width:50%;">'+product.SKU+'</td>'+
		               '<td class="sku-td" style:"width:50%;">'+regionListString+'</td>'+
	            '</tr>';
			});

			detailsHTMLString += '</table>';

			return detailsHTMLString;

		};

	});
</script>	
<style>
#product-list-table {
  background-color: #fff;
  width: 100%;
  text-align: left;
  margin: auto;
 }
 
 #sku-list-table {
  overflow: hidden;
  text-align: left;
  margin: auto;
  border-radius:0;
  table-layout: fixed;
 }
  
.main-head {
  padding: 10px 10px;
}
.sku-head {
  padding: 5px 50px;
  background-color: #8C99A3;
  color: #fff;
}
.sku-row {
    background-color: #fff;
}
.sku-td {
	padding: 5px 50px;
	border-top: 1px solid rgba(128, 128, 128, 0.13);
}
.main-td {
  padding: 10px 10px;
  border-bottom: 1px solid rgba(128, 128, 128, 0.13);
  border-top: 1px solid rgba(128, 128, 128, 0.13);
  color: #494949;
}
.sku-td, .main-td i  {
  color: #494949;
}
.sku-head {
 color: #fff;
}
tr:hover {
//   background-color: #f4f4f4;
}
.no-text {
padding-left: 10px;
padding-right:5px;
}
#base_product {
	font-weight: bold;
}


#product-table-head {
	border: 1px solid rgba(128, 128, 128, 0.13) !important;
}
.btn {
	// font-size: 15px;
    padding: 8px 16px;
}
.btn-outline-default {
	font-weight: 600;
}

table.dataTable thead th {
	padding: 8px 18px !important;
}


</style>		
</head>
<div class="max-width" ng-app="manageProductList" ng-controller="manageProductListCtrl">
	<div class="container-fluid">
		<div class="card">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading">
						<h4 class="card-title"><spring:message code="productVersion.title" /> </h4>
					</div>
				</div>
			</div>
			<div class="row" style="margin-bottom: 15px;">
				<div class="col-md-12 text-right my-2">
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_CREATE']}')">					
						<a class="btn btn-primary"
							href="${pageContext.request.contextPath}/CreateCoreProductInformation">
							<i class="fas fa-plus"></i> <spring:message code="productVersion.create" />
						</a>
					</sec:authorize>
				</div>
			</div>
			<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_DRS']}')">	
			<div class="row">
				<div class="col-md-4 col-md-offset-8 my-2">
						<select id="bp-filter" class="form-control">
							<option value="All">All</option>
							<c:forEach var="supplierKcodeToNameMap" items="${supplierKcodeToNameMap}">
								<option value="${supplierKcodeToNameMap.key}">${supplierKcodeToNameMap.key} ${supplierKcodeToNameMap.value}</option>
							</c:forEach>
						</select>
				</div>
			</div>
			</sec:authorize>
			<div class="row">
				<div class="col-md-12">
				<c:choose>
					<c:when test="${totalPages > 1}">
						<c:url var="firstUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=1" />
						<c:url var="lastUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${totalPages}" />
						<c:url var="prevUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${currentPageIndex - 1}" />
						<c:url var="nextUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${currentPageIndex + 1}" />
						<div class="text-center">
							<nav>
								<ul class="pagination pagination-sm justify-content-center mb-2">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li class="page-item" ><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
											<li class="page-item" ><a class="page-link" href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class=" page-item active">
													<a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a>
												</li>
											</c:when>
											<c:otherwise>
												<li class="page-item" >
													<a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a>
												</li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
											<li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</div>
					</c:when>
				</c:choose>
				<table id="product-list-table" class="table dataTable table-striped table-floated table-width" datatable="ng" dt-options="dtOptions" dt-instance="dtInstance" style="width:100%">
					<thead>
						<tr id="product-table-head">
							<th class="main-head" style="width:10%;"></th>
							<th class="main-head" style="width:30%;"><spring:message code="productVersion.baseProduct" /> </th>
							<th class="main-head" style="width:30%"><spring:message code="productVersion.baseProductName" /></th>
							<th class="main-head" style="width:30%"></th>		
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="product in productList">
							<td class="main-td no-text"><a id="{{product.baseProduct}}List" href="javascript:;" ng-click="showSKUInfo(product, $event)"><i class="fas fa-chevron-circle-right fa-lg"></a></td>	
							<td class="main-td" id="base_product">{{product.baseProduct}}</td>
							<td class="main-td">{{product.productName}}</td>
							<td class="text-right main-td no-text">
								<a class="btn btn-outline-default" href="${pageContext.request.contextPath}/CoreProductInformation/{{product.baseProduct}}"><spring:message code="detailsOfBaseProduct.productInformation" /></a>										
								<a class="btn btn-outline-default" href="${pageContext.request.contextPath}/pmmi/{{product.baseProduct}}?supplierKcode={{product.supplierKcode}}"><spring:message code="detailsOfBaseProduct.productMarketingMaterial" /></a>
							</td>
						</tr>
					</tbody>
				</table>
					<c:choose>
						<c:when test="${totalPages > 1}">
							<c:url var="firstUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=1" />
							<c:url var="lastUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${totalPages}" />
							<c:url var="prevUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${currentPageIndex - 1}" />
							<c:url var="nextUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${currentPageIndex + 1}" />
							<div class="text-center">
								<nav>
									<ul class="pagination pagination-sm justify-content-center mt-2">
										<c:choose>
											<c:when test="${currentPageIndex != 1}">
												<li class="page-item" ><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
												<li class="page-item" ><a class="page-link" href="${prevUrl}">&lt;</a></li>
											</c:when>
										</c:choose>
										<c:forEach var="i" begin="${startPage}" end="${endPage}">
											<c:url var="pageUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${i}" />
											<c:choose>
												<c:when test="${i == currentPageIndex}">
													<li class=" page-item active">
														<a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a>
													</li>
												</c:when>
												<c:otherwise>
													<li class="page-item" >
														<a class="page-link" href="${pageUrl}"><c:out value="${i}" /></a>
													</li>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										<c:choose>
											<c:when test="${currentPageIndex != totalPages}">
												<li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
												<li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
											</c:when>
										</c:choose>
									</ul>
								</nav>
							</div>
						</c:when>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>