<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="${type}" /><spring:message code="product.baseProduct" /> - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	function onSubmit() {
		var codeByDrs = document.getElementById('headCodeByDrs').innerHTML;
		document.getElementById('codeByDrs').value = codeByDrs;
		return true;
	}

	angular.module('baseProduct', []).controller('baseProductCtrl',function($scope) {
		var currentURL = document.URL;
		if (currentURL.indexOf("edit") > -1) {
			$scope.BaseProductCodeExisting = false;
			var BaseProductJson = ${BaseProductJson};
			$scope.supplierKcode = BaseProductJson.supplierKcode;
			$scope.codeBySupplier = BaseProductJson.codeBySupplier;
			$scope.nameBySupplier = BaseProductJson.nameBySupplier;
			$scope.category = BaseProductJson.category;							
		} else {
			$scope.BaseProductCodeExisting = true;
		}

		$scope.getDRSBaseProductCode = function(type) {
			isBaseExist();
			if (type == "Create") {
				$scope.codeByDrs = "BP-" + $scope.supplierKcode + "-" + $scope.codeBySupplier;
			} else {
				document.getElementById('codeByDrs').innerHTML = "BP-" + $scope.supplierKcode + "-" + $scope.codeBySupplier;
			}
		};

		function isBaseExist() {
			var currentURL = document.URL;
			var supplierKcode = document.getElementById('supplierCompany').value;
			var baseCodeBySupplier = document.getElementById('codeBySupplier').value;
			var ajaxUrl = '${pageContext.request.contextPath}/SKUs/isBaseExist/';
			var baseExist = null;
			$.ajax({
				type : 'get',
				async : false,
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {
					supplierKcode : supplierKcode,
					baseCodeBySupplier : baseCodeBySupplier
				},
				dataType : "json",
				success : function(data) {
					baseExist = data;
					if (currentURL.indexOf("edit") > -1) {
						//edit
						var BaseProductJson = ${BaseProductJson};
						if (baseCodeBySupplier != BaseProductJson["codeBySupplier"]) {
							if (baseExist) {
								document.getElementById('baseExist').style.display = "block";
								$scope.BaseProductCodeExisting = true;
							} else {
								document.getElementById('baseExist').style.display = "none";
								$scope.BaseProductCodeExisting = false;
							}
						} else {
							document.getElementById('baseExist').style.display = "none";
							$scope.BaseProductCodeExisting = false;
						}
					} else {
						//add				
						if (baseExist) {
							document.getElementById('baseExist').style.display = "block";
							$scope.BaseProductCodeExisting = true;
						} else {
							document.getElementById('baseExist').style.display = "none";
							$scope.BaseProductCodeExisting = false;
						}
					}
				}
			});
		}
	});
</script>
</head>
<div class="max-width" ng-app="baseProduct" ng-controller="baseProductCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
				<c:choose>
					<c:when test="${type eq 'Create'}">
						<spring:message code="product.createBaseProduct" />
						<c:url var="addAction" value="/BaseProduct/save"></c:url>
					</c:when>
					<c:otherwise>
						<spring:message code="product.editBaseProduct" /> : <span id="headCodeByDrs">${BaseProduct.codeByDrs}</span> ${BaseProduct.nameBySupplier}
						<c:url var="addAction" value="/BaseProduct/update"></c:url>
					</c:otherwise>
				</c:choose>
				</div>
			</div>
		</div>
		<div class="row">
		<div class="col-md-6 offset-md-3">
		<form:form action="${addAction}" name="BaseProduct" modelAttribute="BaseProduct" onsubmit="return onSubmit();" novalidate="novalidate">
			<div class="form-group">
			<label for="supplierCompany"> <spring:message code="product.supplierCompany" /> <span class="text-danger">*</span> </label>
			<c:choose>
									<c:when test="${type eq 'Create'}">
										<form:select id="supplierCompany" class="form-control"
											path="supplierKcode"
											ng-model="supplierKcode"
											ng-change="getDRSBaseProductCode('${type}')"
											required="required">
											<form:option name="" value="" label="--- Select ---" />
											<c:forEach var="SupplierCompany" items="${SupplierCompanies}">
												<form:option name="${SupplierCompany.key}"
													value="${SupplierCompany.key}"
													label="${SupplierCompany.key} ${SupplierCompany.value}" />
											</c:forEach>
										</form:select>
										<small class="text-danger form-text" ng-show="BaseProduct.supplierKcode.$error.required && BaseProduct.supplierKcode.$dirty">
											<spring:message code='product.supplierCompany_req' />
										</small>
									</c:when>
									<c:otherwise>
										<form:select id="supplierCompany" class="form-control"
											 path="supplierKcode"
											disabled="true">
											<form:option name="" value="" label="--- Select ---" />
											<c:forEach var="SupplierCompany" items="${SupplierCompanies}">
												<form:option name="${SupplierCompany.key}"
													value="${SupplierCompany.key}"
													label="${SupplierCompany.key} ${SupplierCompany.value}" />
											</c:forEach>
										</form:select>
										<form:hidden id="supplierCompany" path="supplierKcode" />
									</c:otherwise>
								</c:choose>
			</div>
			<div class="form-group">
			<label for="productCategory"><spring:message code="product.productCategory" /> <span class="text-danger">*</span></label>
			<form:select class="form-control" id="productCategory" path="category" ng-model="category" required="required">
									<form:option value="">--- Select ---</form:option>
									<c:forEach items="${CategoryList}" var="category">
										<form:option value="${category}">${category}</form:option>
									</c:forEach>
								</form:select>
								<small class="text-danger form-text" ng-show="BaseProduct.category.$error.required && BaseProduct.category.$dirty">
									<spring:message code='product.productCategory_req' />
								</small>
			</div>
			<div class="form-group">
			<label for="codebySupplier"><spring:message code="product.supplierBaseProductCode" /> <span class="text-danger">*</span></label>
			<form:input
									id="codeBySupplier" class="form-control"
									path="codeBySupplier"
									ng-model="codeBySupplier"
									ng-change="getDRSBaseProductCode('${type}')"
									ng-pattern="/^[a-zA-Z0-9\-]+$/" required="required" />
								<small id="baseExist" class="text-danger form-text" display: none">
									<spring:message code='product.supplierBaseProductCode_dup' />
								</small>
								<small class="text-danger form-text" ng-show="BaseProduct.codeBySupplier.$error.required && BaseProduct.codeBySupplier.$dirty">
									<spring:message code='product.supplierBaseProductCode_req' />
								</small>
								<small class="text-danger form-text" ng-show="BaseProduct.codeBySupplier.$error.pattern">
									<spring:message code='product.supplierBaseProductCode_format' />
								</small>
			</div>
			<div class="form-group">
			<label for="codeByDrs"><spring:message code="product.drsBaseProductCode" /></label>
			<c:choose>
									<c:when test="${type eq 'Create'}">
										<form:input id="codeByDrs" class="form-control"
											 path="codeByDrs"
											ng-model="codeByDrs" readonly="true" />
									</c:when>
									<c:otherwise>
										<div id="codeByDrs">${BaseProduct.codeByDrs}</div>
										<form:hidden id="codeByDrs" path="codeByDrs" />
									</c:otherwise>
								</c:choose>
			</div>
			<div class="form-group">
			<label for="nameBySupplier"> <spring:message code="product.supplierBaseProductName" /> <span class="text-danger">*</span> </label>
				<form:input
									id="nameBySupplier" class="form-control"
									path="nameBySupplier"
									ng-model="nameBySupplier" required="required" />
								<small class="text-danger form-text" ng-show="BaseProduct.nameBySupplier.$error.required && BaseProduct.nameBySupplier.$dirty">
									<spring:message code='product.supplierBaseProductName_req' />
								</small>
			</div>
			<sec:authorize access="hasAnyRole('${auth['BASE_PRODUCT_DRS_INTERNAL_NOTES']}')">
			<div class="form-group">
			<label for="internalNote"> <spring:message code="product.notes" /> </label>
			<form:textarea id="internalNote" class="form-control" path="internalNote" rows="4" cols="85" />
			</div>
			</sec:authorize>
			<input id="baseSubmit"
									class="btn btn-primary float-right" type="submit"
									value="<spring:message code="product.submit"/>"
									ng-disabled='BaseProductCodeExisting || BaseProduct.$invalid' />
			<a id="cancel" href="${pageContext.request.contextPath}/Products" class="btn btn-default float-right mx-2"><spring:message code="product.cancel"/></a>
		</form:form>
		</div>
		</div>
</div>
</div>
