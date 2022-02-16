<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	${productSkuRegionInfo.productCodeByDrs} ${productSkuRegionInfo.productNameBySupplier} <spring:message code="common.marketplace" />: ${productSkuRegionInfo.marketplace.name} - DRS
</title>
<script>
	function deleteConfirm(skuCodeByDrs, country) {
		$("#dialog-confirm").html('<spring:message code="product.deleteMarketRegionWarning" /> ');
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
					location.href = "${pageContext.request.contextPath}/ProductSkuRegionInfo/"+ skuCodeByDrs+ "/"+ country + "/delete";
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
</script>
<style>
	.text-muted {
		font-size: 22px;
		margin-bottom: 8px;
	}
	table tr td {
		font-size: 15px;
	}
	table tbody tr td {
		border: 1px solid #ddd !important;
		padding-left: 12px !important;
	}
	.marketplace-title {
		font-size: 17px;
    	font-weight: 500;
    	margin-top: 18px;
    	color: #2c2c2c;
	}
</style>
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="card">
			<div class="row">
				<div class="col-md-12" style="display: flex;align-items:flex-start;justify-content: space-between;">
					<div class="page-heading">
						<h4 class="card-title">
							${productSkuRegionInfo.productCodeByDrs}
						</h4>
						<h3 class="text-muted">${productSkuRegionInfo.productNameBySupplier}</h3>
						<p class="marketplace-title" style="font-size: 17px;">
							<spring:message code="common.marketplace"/> : ${productSkuRegionInfo.marketplace.name}</p>		
					</div>
					<sec:authorize access="hasAnyRole('${auth['KEY_PRODUCT_STATS_EDIT']}')">
						<div class="row">
							<div class="col-md-12 text-right my-3">
								<a href="${pageContext.request.contextPath}/ProductSkuRegionInfo/${productSkuRegionInfo.productCodeByDrs}/${productSkuRegionInfo.marketplace.key}/edit">
									<input class="btn btn-primary" type="button" value="<spring:message code="product.editMarketRegion" />" />
								</a>
					</sec:authorize>
								<!-- <a id="cancel" href="${pageContext.request.contextPath}/Products" class="btn btn-default mx-1"><spring:message code="product.cancel"/></a> -->
								<a id="cancel" class="btn btn-default mx-1" style="float: right" href="${pageContext.request.contextPath}/SKUs/${productSkuRegionInfo.marketplaceSku}">返回</a>
					
					<sec:authorize access="hasAnyRole('${auth['KEY_PRODUCT_STATS_EDIT']}')">
								<input class="btn btn-danger" type="button" value="<spring:message code="product.deleteMarketRegion" />" onclick="deleteConfirm('${productSkuRegionInfo.productCodeByDrs}','${productSkuRegionInfo.marketplace.key}');" />
								
								<div id="dialog-confirm"></div>
							</div>
						</div>
					</sec:authorize>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6 my-3">
					<table class="table no-head" >
						<tr>
							<td >
								<spring:message code="product.marketplaceStatus" />
							</td>
							<td><spring:message code="${productSkuRegionInfo.status}" /></td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.marketplaceSKU" />
							</td>
							<td>${productSkuRegionInfo.marketplaceSku}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.MSRP" /> [${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.MSRP}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.supplierSuggestedBaseRetailPrice" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.supplierSuggestedBaseRetailPrice}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.currentBaseRetailPrice" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.currentBaseRetailPrice}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.estimatedDrsSRetainment" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.estimatedDrsRetainment}</td>
						</tr>
						
					</table>
				</div>
				<div class="col-md-6 my-3">
					<table class="table no-head">
						<tr>
							<td >
								<spring:message code="product.estimatedMarketplaceCommissionRate"/>
							</td>
							<td>${productSkuRegionInfo.referralRate}%</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.estimatedMarketplaceFees" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.estimatedMarketplaceFees}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.estimatedFulfillmentFees" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.estimatedFulfillmentFees}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.estimatedImportDuty" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.estimatedImportDuty}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.estimatedFreightCharge" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.estimatedFreightCharge}</td>
						</tr>
						<tr>
							<td >
								<spring:message code="product.approxSupplierNetRevenue" />[${productSkuRegionInfo.currency}]
							</td>
							<td>${productSkuRegionInfo.approxSupplierNetRevenue}</td>
						</tr>
					</table>
					
				</div>
			</div>
		</div>
	</div>
</div>