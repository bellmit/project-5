<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="${type}" />SKU - DRS
</title>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	function onSubmit() {
		var codeByDrs = document.getElementById('originalCodeByDrs').innerHTML;
		document.getElementById('codeByDrs').value = codeByDrs;
		return true;
	}

	angular.module('SKU', []).controller('SKUCtrl',function($scope) {

		var currentURL = document.URL;

		if (currentURL.indexOf("edit") > -1) {

			$scope.SkuExist = false;
			$scope.baseProductCodeFill = false;
			var SKUJson = ${SKUJson};
			$scope.supplierKcode = SKUJson.supplierKcode;
			$scope.codeBySupplier = SKUJson.codeBySupplier;
			$scope.nameBySupplier = SKUJson.nameBySupplier;
			$scope.productEAN = SKUJson.productEAN;
			$scope.manufacturingLeadTimeDays = SKUJson.manufacturingLeadTimeDays;
			var ajaxUrl = '${pageContext.request.contextPath}/SKUs/getBaseCodeList/'
			var baseCodeList = null;

		$.ajax({
			type : 'get',
			url : ajaxUrl,
			contentType : "application/json; charset=utf-8",
			data : {
				supplierKcode : SKUJson.supplierKcode
			},
			dataType : "json",
			success : function(data) {
				baseCodeList = data;
				document.getElementById("baseProductCode").options.length = 0;
				if (baseCodeList.length > 0) {
					var select = document.getElementById("baseProductCode");
					var opt = document.createElement("option");
					opt.value = "";
					opt.textContent = "--- Select ---";
					select.appendChild(opt);
				}
				for ( var key in baseCodeList) {
					if (baseCodeList.hasOwnProperty(key)) {
						opt = document.createElement("option");
						opt.value = baseCodeList[key];
						opt.textContent = baseCodeList[key];
						select.appendChild(opt);
					}
				}
				select.value = SKUJson["baseProductCode"];
			}
		});
		} else {
			$scope.SkuExist = true;
			$scope.baseProductCodeFill = true;
			var SupplierKcode = "${SupplierKcode}";
			var BaseProductCode = "${BaseProductCode}";														
			if (SupplierKcode != "none"&& BaseProductCode != "none") {
				$scope.supplierKcode = SupplierKcode;
				$scope.baseProductCodeFill = false;
				var supplierKcode = SupplierKcode;
				var ajaxUrl = '${pageContext.request.contextPath}/SKUs/getBaseCodeList/'
				var baseCodeList = null;
			$.ajax({
				type : 'get',
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {
					supplierKcode : supplierKcode
				},
				dataType : "json",
				success : function(data) {
					baseCodeList = data;
					document.getElementById("baseProductCode").options.length = 0;
					if (baseCodeList.length > 0) {
						var select = document.getElementById("baseProductCode");
						var opt = document.createElement("option");
						opt.value = "";
						opt.textContent = "--- Select ---";
						select.appendChild(opt);
					}
					for ( var key in baseCodeList) {
						if (baseCodeList.hasOwnProperty(key)) {
							opt = document.createElement("option");
							opt.value = baseCodeList[key];
							opt.textContent = baseCodeList[key];
							select.appendChild(opt);
						}
					}
					select.value = BaseProductCode;
				}
			});
			}
		}

		$scope.getDRSBaseProductCodeList = function(type) {
			getBaseCodeList();
			isSkuExist();
			assignCodeByDrs(type);			
		};

		$scope.validateSupplierSKUCode = function(type) {
			isSkuExist();
			assignCodeByDrs(type);			
		};

		function assignCodeByDrs(type){
			if (type == "Create") {
				$scope.codeByDrs = $scope.supplierKcode + "-" + $scope.codeBySupplier;
			} else {
				document.getElementById('codeByDrs').innerHTML = $scope.supplierKcode + "-" + $scope.codeBySupplier;
			}			
		}
				
		$scope.changeBaseProductCode = function() {
			if ($scope.baseProductCode != '') {
				document.getElementById('baseProductCodeFill').style.display = "none";
				$scope.baseProductCodeFill = false;
			} else {
				document.getElementById('baseProductCodeFill').style.display = "block";
				$scope.baseProductCodeFill = true;
			}
		};

		function getBaseCodeList() {
			var supplierKcode = document.getElementById('supplierCompany').value;
			var ajaxUrl = '${pageContext.request.contextPath}/SKUs/getBaseCodeList/'
			var baseCodeList = null;
			$.ajax({
				type : 'get',
				async : false,
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {supplierKcode : supplierKcode},
				dataType : "json",
				success : function(data) {
					baseCodeList = data;
					document.getElementById("baseProductCode").options.length = 0;
					if (baseCodeList.length > 0) {
						var select = document.getElementById("baseProductCode");
						var opt = document.createElement("option");
						opt.value = "";
						opt.textContent = "--- Select ---";
						select.appendChild(opt);
					}
					for ( var key in baseCodeList) {
						if (baseCodeList.hasOwnProperty(key)) {
							opt = document.createElement("option");
							opt.value = baseCodeList[key];
							opt.textContent = baseCodeList[key];
							select.appendChild(opt);
						}
					}
					$scope.baseProductCodeFill = true;
				}
			});
		}

		function isSkuExist() {
			var currentURL = document.URL;
			var supplierKcode = document.getElementById('supplierCompany').value;
			var skuCodeBySupplier = document.getElementById('supplierSKUCode').value;
			var ajaxUrl = '${pageContext.request.contextPath}/SKUs/isSkuExist/';
			var skuExist = null;
			$.ajax({
				type : 'get',
				async : false,
				url : ajaxUrl,
				contentType : "application/json; charset=utf-8",
				data : {
					supplierKcode : supplierKcode,
					skuCodeBySupplier : skuCodeBySupplier
				},
				dataType : "json",
				success : function(data) {
					skuExist = data;
					if (currentURL.indexOf("edit") > -1) {
						//edit	
						var SKUJson = ${SKUJson};
						if (skuCodeBySupplier != SKUJson["codeBySupplier"]) {
							if (skuExist) {
								document.getElementById('skuExist').style.display = "block";
								$scope.SkuExist = true;
							} else {
								document.getElementById('skuExist').style.display = "none";
								$scope.SkuExist = false;
							}
						} else {
							document.getElementById('skuExist').style.display = "none";
							$scope.SkuExist = false;
						}

					} else {
						//add	
						if (skuExist) {
							document.getElementById('skuExist').style.display = "block";
							$scope.SkuExist = true;
						} else {
							document.getElementById('skuExist').style.display = "none";
							$scope.SkuExist = false;
						}
					}
				}
			});
		}
	});
</script>
</head>
<div class="max-width" ng-app="SKU" ng-controller="SKUCtrl">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
				<c:choose>
					<c:when test="${type eq 'Create'}">
						<spring:message code="product.createSkuTitle" />
						<c:url var="addAction" value="/SKUs/save"></c:url>
					</c:when>
					<c:otherwise>
						<spring:message code="product.editSku" /> : <span id="originalCodeByDrs">${SKU.codeByDrs}</span> ${SKU.nameBySupplier}
						<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
							<h3 class="text-muted">(${SKU.nameByDrs})</h3>
						</sec:authorize>
						<c:url var="addAction" value="/SKUs/update"></c:url>
					</c:otherwise>
				</c:choose>
				</div>
			</div>
		</div>

		<form:form action="${addAction}" name="SKU" modelAttribute="SKU" onsubmit="return onSubmit();" novalidate="novalidate">
			<div class="row">
				<div class="col-md-6">
				<div class="form-group">
				<label for="supplierCompany"><spring:message code="product.supplierCompany" /><span class="text-danger"> *</span></label>
				<c:choose>
									<c:when test="${type eq 'Create'}">
										<form:select id="supplierCompany" class="form-control"
											path="supplierKcode"
											ng-model="supplierKcode"
											ng-change="getDRSBaseProductCodeList('${type}')"
											required="required">
											<form:option name="" value="" label="--- Select ---" />
											<c:forEach var="SupplierCompany" items="${SupplierCompanies}">
												<form:option name="${SupplierCompany.key}"
													value="${SupplierCompany.key}"
													label="${SupplierCompany.key} ${SupplierCompany.value}" />
											</c:forEach>
										</form:select>

										<small class="text-danger form-text" ng-show="SKU.supplierKcode.$error.required && SKU.supplierKcode.$dirty">
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
				<label for="baseProductCode"><spring:message code="product.drsBaseProductCode" /><span class="text-danger"> *</span></label>
				<c:choose>
									<c:when test="${type eq 'Create'}">
										<form:select id="baseProductCode" class="form-control"
											path="baseProductCode"
											ng-model="baseProductCode"
											ng-change="changeBaseProductCode()">
										</form:select>
										<small id="baseProductCodeFill" class="text-danger form-text" display: none">
											<spring:message code='product.supplierBaseProductCode_req' />
										</small>
									</c:when>
									<c:otherwise>
										<form:select id="baseProductCode" class="form-control"
											 path="baseProductCode"
											disabled="true">
										</form:select>
										<form:hidden id="baseProductCode" path="baseProductCode" />
									</c:otherwise>
								</c:choose>
				</div>
				<div class="form-group">
				<label for="suppliedSKUCode"><spring:message code="product.supplierSkuCode" /><span class="text-danger"> *</span></label>
				<c:choose>
									<c:when test="${(SKU.status eq 'SKU_DRAFT') || (type eq 'Create')}">
										<form:input id="supplierSKUCode" class="form-control"
											path="codeBySupplier"
											ng-model="codeBySupplier"
											ng-change="validateSupplierSKUCode('${type}')"
											ng-maxlength="29" ng-pattern="/^[a-zA-Z0-9\-]+$/"
											required="required" />

										<small id="skuExist" class="text-danger form-text" style="display: none">
											<spring:message code='product.supplierSkuCode_dup' />
										</small>
										<small class="text-danger form-text" ng-show="SKU.codeBySupplier.$error.required && SKU.codeBySupplier.$dirty">
											<spring:message code='product.supplierSkuCode_req' />
										</small>
										<small class="text-danger form-text" ng-show="SKU.codeBySupplier.$error.maxlength">
											<spring:message code='product.supplierSkuCode_format30' />
										</small>
										<small class="text-danger form-text" ng-show="SKU.codeBySupplier.$error.pattern">
											<spring:message code='product.supplierSkuCode_format' />
										</small>
									</c:when>
									<c:when test="${SKU.status ne 'SKU_DRAFT'}">
										<form:input id="supplierSKUCode" class="form-control"
											path="codeBySupplier"
											disabled="true" />
										<form:hidden id="supplierSKUCode" path="codeBySupplier" />
									</c:when>
								</c:choose>
				</div>
				<div class="form-group">
				<label for="codeByDrs"><spring:message code="product.drsSkuCode" /><span class="text-danger"> *</span></label>
							<c:choose>
									<c:when test="${type eq 'Create'}">
										<form:input id="codeByDrs" class="form-control"
											path="codeByDrs"
											ng-model="codeByDrs" readonly="true" />

									</c:when>
									<c:otherwise>
										<div id="codeByDrs">${SKU.codeByDrs}</div>
										<form:hidden id="codeByDrs" path="codeByDrs" />
									</c:otherwise>
								</c:choose>
				</div>
				<div class="form-group">
				<label for="supplierProductName"><spring:message code="product.supplierProductName" /><span class="text-danger"> *</span></label>
												<form:input
									id="supplierProductName" class="form-control"
									path="nameBySupplier"
									ng-model="nameBySupplier" required="required" />

								<small class="text-danger form-text" ng-show="SKU.nameBySupplier.$error.required && SKU.nameBySupplier.$dirty">
									<spring:message code='product.supplierProductName_req' />
								</small>
				</div>
				</div>
				<div class="col-md-6">
				<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
				<div class="form-group">
				<label for="nameByDrs"><spring:message code="product.drsProductName" /></label>
				<form:input
									id="nameByDrs" class="form-control"
									path="nameByDrs" />
				</div>
				</sec:authorize>
				<div class="form-group">
				<label for="productEAN"><spring:message code="product.productEAN" /></label>
				<form:input
									id="productEAN" class="form-control"
									path="productEAN"
									ng-model="productEAN" ng-pattern="/^[0-9\X]+$/"
									ng-minlength="12" ng-maxlength="13" />
								</div>

				<div class="form-group">
								<div class="form-check form-check-inline">
								<form:radiobutton
									class="form-check-input"
									path="EanProvider" id="radiodrs" value="DRS" checked="checked"/>
									<label for="radiodrs" class="form-check-label"><spring:message
									code="product.eanProviderDRS" /></label>
								</div>
								<div class="form-check form-check-inline">
								<form:radiobutton
									class=" form-check-input"
									path="EanProvider" id="radiosupplier" value="SUPPLIER" />
									<label for="radiosupplier" class="form-check-label"> <spring:message
									code="product.eanProviderSupplier" /></label>
								</div>

								<small class="text-danger form-text" ng-show="SKU.productEAN.$error.pattern">
									<spring:message code='product.productEAN_format' />
								</small>
								<small class="text-danger form-text" ng-show="SKU.productEAN.$error.maxlength || SKU.productEAN.$error.minlength">
									<spring:message code='product.productEAN_format12or13' />
								</small>
				</div>
				<div class="form-group">
				<label for="manufacturingLeadTimeDays"><spring:message code="product.MLT" /><span class="text-danger"> *</span></label>
				<form:input id="manufacturingLeadTimeDays"
									class="form-control"
									path="manufacturingLeadTimeDays"
									ng-model="manufacturingLeadTimeDays"
									ng-pattern="/^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])$|^999$/"
									required="required" />

								<small class="text-danger text-form" ng-show="SKU.manufacturingLeadTimeDays.$error.required && SKU.manufacturingLeadTimeDays.$dirty">
									<spring:message code='product.MLT_req' />
								</small>
								<small class="text-danger text-form" ng-show="SKU.manufacturingLeadTimeDays.$error.pattern">
									<spring:message code='product.MLT_format' />
								</small>
				</div>
				<div class="form-group">
				<label class="pr-2" for="batterystatus"><spring:message code="product.lithiumIonMetalBatteries" /></label>


				<div class="form-check">
				<form:radiobutton class="form-check-input" path="containLithiumIonBattery" value="TRUE" /> <spring:message code="product.yes" />
				</div>
				<div class="form-check">
				<form:radiobutton class="form-check-input" path="containLithiumIonBattery" value="FALSE" /> <spring:message code="product.no" />
				</div>

				</div>



				<div class=form-group>
				<label for="status"><spring:message code="product.status" /> </label>
				<form:select id="status"
									path="status" class="form-control"
									>
									<c:forEach items="${SkuStatusList}" var="SkuStatus">
										<form:option value="${SkuStatus}">
											<spring:message code="${SkuStatus}" />
										</form:option>
									</c:forEach>
				</form:select>
				</div>
				<div class="form-group">
				<label for="internalNote"><spring:message code="product.notes" /></label>
										<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NOTES']}')">
								<form:textarea id="internalNote" class="form-control" path="internalNote" rows="4" cols="85" />
						</sec:authorize>
				</div>
				<div>
								<input id="skuSubmit"
								class="btn btn-primary" type="submit"
								value="<spring:message code="product.submit" />"
								style="float: right"
								ng-disabled='SkuExist || baseProductCodeFill || SKU.$invalid' />
								<a id="cancel" href="${pageContext.request.contextPath}/Products" class="btn btn-default float-right mx-2"><spring:message code="product.cancel"/></a>
				</div>

				</div>
			</div>
		</form:form>
	</div>
</div>