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
			
	jQuery(window).on("load", function(e) {	 		
		var applicationList = ${onboardingApplicationsJson};	
		$.each(applicationList, function(key, application){											
			$("#"+product.serialNumber+"List").click();			
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
						
		$scope.applicationList = ${onboardingApplicationsJson};
					
		$scope.showBPInfo = function(application, event){
			
			var scope = $scope.$new(true);      
	      		scope.application = application;
			
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
	        	row.child( $scope.listBPDetails(application.productInfoMarketSide)).show();
	        	tr.addClass('shown');
	      	}
						
		};
			
		$scope.listBPDetails = function(data){
			if (!data) return ''
			
			var detailsHTMLString = 
			'<table class="table" style="background-color:#d0e9c6">'+
			'<thead>'+
				'<tr>'+
					'<th style="width:300px;">'+'<spring:message code="mpOnboardingApplication.baseProduct" />'+'</th>'+
					'<th style="width:300px;">'+'<spring:message code="mpOnboardingApplication.productStatus" />'+'</th>'+
					'<th style="width:30px;"></th>'+
				'</tr>'+					
			'</thead>';
						
			detailsHTMLString += '</table>';
			
			return detailsHTMLString;
						
		};
						
	});	
</script>			
</head>
<div class="max-width" ng-app="manageProductList" ng-controller="manageProductListCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">
					<spring:message code="mpOnboardingApplication.title" /> 
				</div>
			</div>			
			<div class="col-md-6 text-right">
				<div class="page-heading">					  
					<sec:authorize access="hasAnyRole('${auth['PRODUCT_ONBOARDING_CREATE']}')">					
						<a class="btn btn-large btn-primary btn-lg"
							href="${pageContext.request.contextPath}/CreateCoreProductInformation">
							<spring:message code="productVersion.create" />
						</a>
					</sec:authorize>
				</div>
			</div>			
		</div>
		<div style="padding-bottom: 10px"></div>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-hover" datatable="ng" dt-options="dtOptions" dt-instance="dtInstance">
					<thead>
						<tr>
							<th style="width:30px;"/></th>
							<th style="width:250px;"><spring:message code="mpOnboardingApplication.applicationNumber" /></th>
							<th style="width:350px;"><spring:message code="mpOnboardingApplication.supplier" /></th>
							<th style="width:250px;"><spring:message code="mpOnboardingApplication.progress" /></th>							
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="application in applicationList">
							<td><a id="{{application.serialNumber}}List" href="javascript:;" ng-click="showBPInfo(application, $event)"><i class="fas fa-th-list"></a></td>
							<td>{{application.serialNumber}}</td>
							<td>{{application.supplierKcode}}</td>
							<td>{{application.status}}</td>												
						</tr>
					</tbody>	
				</table>								
			</div>
		</div>
	</div>
</div>