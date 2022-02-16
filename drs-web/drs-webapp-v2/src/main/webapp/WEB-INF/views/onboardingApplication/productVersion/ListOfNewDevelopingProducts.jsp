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
<script type='text/javascript' src="<c:url value="/resources/js/angular-datatables.min.js"/>"></script>
<script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
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
		$('#bp-filter').select2();	

	});
	
	jQuery(window).on("load", function(e) {
		var productList = ${baseProductsJson};
		$.each(productList, function(key, product){
			$("#"+product.baseProduct+"List").click();
		});
	});

	angular.module('manageProductList', ['datatables']).controller('manageProductListCtrl', function($scope, DTOptionsBuilder, DTColumnBuilder,$compile) {

		$scope.dtInstance = {};
		$scope.dtOptions = DTOptionsBuilder.newOptions()
		  .withOption('order', [0, 'asc'])
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
		        icon.removeClass('fa-minus-circle').addClass('fa-th-list');
		        row.child.hide();
		        tr.removeClass('shown');
		      }else {
		        icon.removeClass('fa-th-list').addClass('fa-minus-circle');
		        row.child( $scope.listDetails(product) ).show();
		        tr.addClass('shown');
		      }
		}

		$scope.listDetails = function(data){
			if (!data) return ''

			var detailsHTMLString =
			'<table class="table" style="background-color:#d0e9c6">'+
			'<thead>'+
				'<tr>'+
					'<th style="width:300px;">'+'<spring:message code="productVersion.variationProduct" />'+'</th>'+
					'<th style="width:300px;">'+'<spring:message code="productVersion.appliedRegion" />'+'</th>'+
					'<th style="width:30px;"></th>'+
				'</tr>'+
			'</thead>';

			$.each(data.variationProducts, function(productKey, product){

				var regionListString = '';

				$.each(product.applicableRegionList, function(regionKey, region){

					regionListString += $scope.regions[region.replace(/"/g,'')]+ '&nbsp&nbsp';

				});

				detailsHTMLString +=
				'<tr>'+
	               '<td>'+product.SKU+'</td>'+
	               '<td>'+regionListString+'</td>'+
	               '<td>'+'<button type="button" class="btn btn-default btn"><i class="fas fa-pencil-alt"></i></button>'+'</td>'+
	            '</tr>';
			});

			detailsHTMLString += '</table>';

			return detailsHTMLString;

		};

	});
</script>			
</head>
<div class="max-width" ng-app="manageProductList" ng-controller="manageProductListCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="productVersion.title" /> 
				</div>
			</div>
			</div>
			<div class="row">
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
				<table class="table table-hover" datatable="ng" dt-options="dtOptions" dt-instance="dtInstance">
					<thead>
						<tr>
							<th style="width:30px;"/></th>
							<th style="width:250px;"><spring:message code="productVersion.baseProduct" /></th>
							<th style="width:350px;"><spring:message code="productVersion.baseProductName" /></th>
							<th style="width:250px;"></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="product in productList">
							<td><a id="{{product.baseProduct}}List" href="javascript:;" ng-click="showSKUInfo(product, $event)"><i class="fas fa-th-list"></a></td>
							<td>{{product.baseProduct}}</td>
							<td>{{product.productName}}</td>
							<td class="text-right">
								<a class="btn btn-sm btn-default" href="${pageContext.request.contextPath}/CoreProductInformation/{{product.baseProduct}}"><spring:message code="detailsOfBaseProduct.productInformation" /></a>
								<a class="btn btn-sm btn-default" href="${pageContext.request.contextPath}/pmmi/{{product.baseProduct}}?supplierKcode={{product.supplierKcode}}"><spring:message code="detailsOfBaseProduct.productMarketingMaterial" /></a>
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
						<center>
							<nav>
								<ul class="pagination pagination-sm justify-content-center">
									<c:choose>
										<c:when test="${currentPageIndex != 1}">
											<li><a href="${firstUrl}">&lt;&lt;</a></li>
											<li><a href="${prevUrl}">&lt;</a></li>
										</c:when>
									</c:choose>
									<c:forEach var="i" begin="${startPage}" end="${endPage}">
										<c:url var="pageUrl" value="/NewDevelopingProductList?kcode=${companyKcode}&page=${i}" />
										<c:choose>
											<c:when test="${i == currentPageIndex}">
												<li class="active">
													<a href="${pageUrl}"><c:out value="${i}" /></a>
												</li>
											</c:when>
											<c:otherwise>
												<li>
													<a href="${pageUrl}"><c:out value="${i}" /></a>
												</li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<c:choose>
										<c:when test="${currentPageIndex != totalPages}">
											<li><a href="${nextUrl}">&gt;</a></li>
											<li><a href="${lastUrl}">&gt;&gt;</a></li>
										</c:when>
									</c:choose>
								</ul>
							</nav>
						</center>
					</c:when>
				</c:choose>
			</div>
		</div>
	</div>
</div>