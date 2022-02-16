<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	SKU: ${SKU.codeByDrs} ${SKU.nameBySupplier} - DRS
</title>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<script>
	function deleteConfirm(sku) {
		$("#dialog-confirm").html('<spring:message code="product.deleteSKUWarning" /> ');
		$("#dialog-confirm").dialog({
			open : function() {$('.ui-dialog-buttonset button[name="no"]').focus();},
			resizable : false,
			modal : true,
			title : "Confirmation",
			height : 250,
			width : 400,
			buttons : [
			{
				text : "<spring:message code='product.yes' /> ",
				name : "yes",
				click : function() {
					$(this).dialog('close');
					location.href = "${pageContext.request.contextPath}/SKUs/" + sku + "/delete";
				}
			},
			{
				text : "<spring:message code='product.no' />",
				name : "no",
				click : function() {
					$(this).dialog('close');
				}
			} ]
		});
	}
	
	angular.module('SKUView', []).controller('SKUViewCtrl', function($scope) {
		var SKUJson = ${SKUJson};
		$scope.manufacturingLeadTimeDays = SKUJson.manufacturingLeadTimeDays;
		$scope.containLithiumIonBattery = SKUJson.containLithiumIonBattery;
	});
</script>
</head>
<div class="max-width" ng-app="SKUView" ng-controller="SKUViewCtrl">
	<div class="container-fluid">
		<div class="text-center text-success">
			<h1>${message}</h1>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="page-heading">${SKU.codeByDrs}</div>
				<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
					<h4>(${SKU.nameByDrs})</h4>
				</sec:authorize>
				<c:url var="updateAction" value="/SKUs/updateForMLTAndLithiumIonMetalBatteries"></c:url>
				<form:form action="${updateAction}" name="SKU" modelAttribute="SKU" novalidate="novalidate">
					<table class="table no-head my-3">
						<sec:authorize access="hasAnyRole('${auth['COMPANIES_VIEW']}')">
							<tr>
								<td>
									<b><spring:message code="product.supplierCompany" /></b>
								</td>
								<td>
									<a href="${pageContext.request.contextPath}/Companies/${SKU.supplierKcode}">${SKU.supplierKcode}
										${Company}
									</a>
								</td>
							</tr>
						</sec:authorize>
						<tr>
							<td>
								<b><spring:message code="product.supplierBaseProductCode" /></b>
							</td>
							<td>
								<a href="${pageContext.request.contextPath}/BaseProduct/${SKU.baseProductCode}">
									${SKU.baseProductCode}
								</a>
							</td>
						</tr>
						<tr>
							<td>
								<b><spring:message code="product.supplierSkuCode" /></b>
							</td>
							<td>${SKU.codeBySupplier}</td>
						</tr>
						<tr>
							<td>
								<b><spring:message code="product.drsSkuCode" /></b>
							</td>
							<td>${SKU.codeByDrs}
								<form:hidden path="codeByDrs" />
							</td>
						</tr>
						<tr>
							<td>
								<b><spring:message code="product.supplierProductName" /></b>
							</td>
							<td>${SKU.nameBySupplier}</td>
						</tr>
						<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
							<tr>
								<td>
									<b><spring:message code="product.drsProductName" /></b>
								</td>
								<td>${SKU.nameByDrs}</td>
							</tr>
						</sec:authorize>
						<tr>
							<td>
								<b><spring:message code="product.productEAN" /></b>
							</td>
							<td>
							<sec:authorize access="hasAnyRole('${auth['SKU_EAN_PROVIDER_DRS']}')">
								${SKU.productEAN}
							<c:choose>
								<c:when test="${SKU.eanProvider eq 'SUPPLIER'}">
									<spring:message code="product.eanProviderSupplier_HINT" />
								</c:when>
								<c:otherwise>
									<spring:message code="product.eanProviderDRS_HINT" />
								</c:otherwise>
							</c:choose>
							</sec:authorize>
							<sec:authorize access="hasAnyRole('${auth['SKU_DRS_EAN_PROVIDER_SUPPLIER']}')">
							<c:choose>
								<c:when test="${SKU.eanProvider eq 'SUPPLIER'}">
									${SKU.productEAN}
								</c:when>
								<c:otherwise>
									<spring:message code="product.eanProviderSupplier_view" />
								</c:otherwise>
							</c:choose>
							</sec:authorize>
							</td>
						</tr>
						<sec:authorize access="hasAnyRole('${auth['SKU_MLT_EDIT']}')">
						<tr>
							<td>
								<b><spring:message code="product.MLT" /><span class="text-danger"> *</span></b>
							</td>
							<td>
								<form:input
									id="manufacturingLeadTimeDays" class="form-control"
									path="manufacturingLeadTimeDays"
									ng-model="manufacturingLeadTimeDays"
									ng-pattern="/^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])$|^999$/"
									required="required" />
								<small class="text-danger form-text" ng-show="SKU.manufacturingLeadTimeDays.$error.required && SKU.manufacturingLeadTimeDays.$dirty">
									<spring:message code='product.MLT_req' />
								</small>
								<small class="text-danger form-text" ng-show="SKU.manufacturingLeadTimeDays.$error.pattern">
									<spring:message code='product.MLT_format' />
								</small>
							</td>
						</tr>
						<tr>
							<td>
								<b><spring:message code="product.lithiumIonMetalBatteries" /></b>
							</td>
							<td>
								<form:radiobutton path="containLithiumIonBattery" value="TRUE" /> <spring:message code="product.yes" />
								<form:radiobutton path="containLithiumIonBattery" value="FALSE" /> <spring:message code="product.no" />
							</td>
						</tr>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('${auth['SKU_MLT_VIEW']}')">
						<tr>
							<td>
								<b><spring:message code="product.MLT" /></b>
							</td>
							<td>
								${SKU.manufacturingLeadTimeDays}
							</td>
						</tr>
						<tr>
							<td>
								<b><spring:message code="product.lithiumIonMetalBatteries" /></b>
							</td>
							<td>
								<spring:message code="product.${SKU.containLithiumIonBattery}" />
							</td>
						</tr>
						</sec:authorize>
						<tr>
							<td>
								<b><spring:message code="product.status" /></b>
							</td>
							<td>
								<spring:message code="${SKU.status}" />
							</td>
						</tr>
						<tr>
							<td>
								<b><spring:message code="product.nextAction" /></b>
							</td>
							<td>
								<spring:message code="${SKU.status}_NEXT_ACTION" />
							</td>
						</tr>
						<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NOTES']}')">
						<tr>
							<td>
								<b><spring:message code="product.notes" /></b>
							</td>
							<td>${SKU.internalNote}</td>
						</tr>
						</sec:authorize>
						<tr>
							<td colspan="2">
								<div style="float: right">
									<sec:authorize access="hasAnyRole('${auth['SKU_EDIT']}')">
										<a class="btn btn-default" href="${pageContext.request.contextPath}/SKUs/${SKU.codeByDrs}/edit">
											<spring:message code="product.edit" />
										</a>
									</sec:authorize>
																	<sec:authorize access="hasAnyRole('${auth['SKU_MLT_EDIT']}')">
										<input class="btn btn-primary" type="submit" value="<spring:message code='product.update' />" />
									</sec:authorize>
									<sec:authorize access="hasAnyRole('${auth['SKU_DELETE']}')">
										<c:if test="${status eq 'SKU_DRAFT'}">
											<input class="btn btn-link" type="button" value="<spring:message code="product.delete" />" onclick="deleteConfirm('${SKU.codeByDrs}');" />
										</c:if>
									</sec:authorize>
									<div id="dialog-confirm"></div>
								</div>
							</td>
						</tr>
					</table>
				</form:form>
			</div>
			<div class="col-md-6">
				<sec:authorize access="hasAnyRole('${auth['KEY_PRODUCT_STATS_VIEW']}')">
					<div class="page-heading">
						<spring:message code="common.marketplace" />
					</div>
					<table class="table no-head my-3">
						<c:forEach var="regionInfo" items="${SKU.regionInfoList}">
							<tr>
								<td><b>${regionInfo.marketplace.name}</b></td>
								<td>${regionInfo.marketplaceSku}</td>
								<td><spring:message code="${regionInfo.status}" /></td>
								<td>
									<a href="${pageContext.request.contextPath}/ProductSkuRegionInfo/${SKU.codeByDrs}/${regionInfo.marketplace.key}">
										<spring:message code="product.marketRegionDetails" />
									</a>
								</td>
							</tr>
						</c:forEach>
					</table>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('${auth['KEY_PRODUCT_STATS_EDIT']}')">
				<div class="text-right">
					<a href="${pageContext.request.contextPath}/ProductSkuRegionInfo/create/${SKU.codeByDrs}">
						<button class="btn btn-primary" type="button"><i class="fas fa-plus"></i> <spring:message code="product.createMarketPlace" /></button>
					</a>
				</div>
				</sec:authorize>
			</div>
		</div>
	</div>
</div>