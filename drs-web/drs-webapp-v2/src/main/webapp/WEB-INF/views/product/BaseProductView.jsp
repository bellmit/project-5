<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<head>
<title>
	<spring:message code="header.baseProduct" />:${BaseProduct.codeByDrs} ${BaseProduct.nameBySupplier} - DRS
</title>

<script>
	function deleteConfirm(baseProductCode) {
		$("#dialog-confirm").html('<spring:message code="product.deleteBaseProductWarning" /> ');
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
					location.href = "${pageContext.request.contextPath}/BaseProduct/" + baseProductCode + "/delete";
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
</head>
<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					${BaseProduct.codeByDrs} ${BaseProduct.nameBySupplier}
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6 offset-md-3 mt-3">
				<table class="table no-head">
					<sec:authorize access="hasAnyRole('${auth['COMPANIES_VIEW']}')">
						<tr>
							<td>
								<b><spring:message code="product.supplierCompany" /></b>
							</td>
							<td>${BaseProduct.supplierKcode}${Company}</td>
						</tr>
					</sec:authorize>
					<tr>
						<td>
							<b><spring:message code="product.productCategory" /></b>
						</td>
						<td>${BaseProduct.category}</td>
					</tr>
					<tr>
						<td>
							<b><spring:message code="product.supplierBaseProductCode" /></b>
						</td>
						<td>${BaseProduct.codeBySupplier}</td>
					</tr>
					<tr>
						<td>
							<b><spring:message code="product.drsBaseProductCode" /></b>
						</td>
						<td>${BaseProduct.codeByDrs}</td>
					</tr>
					<tr>
						<td>
							<b><spring:message code="product.supplierBaseProductName" /></b>
						</td>
						<td>${BaseProduct.nameBySupplier}</td>
					</tr>
					<sec:authorize access="hasAnyRole('${auth['BASE_PRODUCT_DRS_INTERNAL_NOTES']}')">
						<tr>
							<td>
								<b><spring:message code="product.notes" /></b>
							</td>
							<td>${BaseProduct.internalNote}</td>
						</tr>
					</sec:authorize>
					<tr>
						<td colspan="2">
							<div class="py-2" style="float: right">
								<sec:authorize access="hasAnyRole('${auth['SKU_CREATE']}')">
									<a class="btn btn-primary" href="${pageContext.request.contextPath}/SKUs/create?supplierCompanykCode=${BaseProduct.supplierKcode}&baseProductCode=${BaseProduct.codeByDrs}">
										<i class="fas fa-plus"></i> <spring:message code="product.createSku" />
									</a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['BASE_PRODUCT_EDIT']}')">
									<a class="btn btn-default" href="${pageContext.request.contextPath}/BaseProduct/${BaseProduct.codeByDrs}/edit">
										<spring:message code="product.edit" />
									</a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('${auth['BASE_PRODUCT_DELETE']}')">
									<c:if test="${empty BaseProduct.skuList}">
										<input class="btn btn-link" type="button" value="<spring:message code="product.delete" />" onclick="deleteConfirm('${BaseProduct.codeByDrs}');" />
										<div id="dialog-confirm"></div>
									</c:if>
								</sec:authorize>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<c:if test="${!empty BaseProduct.skuList}">
			<div class="row">
				<div class="col-md-12">
					<div class="page-heading" style="padding-top: 0;">SKU</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th><spring:message code="product.drsSkuCode" /></th>
								<th><spring:message code="product.supplierProductName" /></th>
								<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
									<th><spring:message code="product.drsProductName" /></th>
								</sec:authorize>
								<th><spring:message code="product.status" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${BaseProduct.skuList}" var="sku">
								<tr>
									<td><a href="${pageContext.request.contextPath}/SKUs/${sku.codeByDrs}">
											${sku.codeByDrs}
										</a>
									</td>
									<td>${sku.nameBySupplier}</td>
									<sec:authorize access="hasAnyRole('${auth['SKU_DRS_INTERNAL_NAME']}')">
										<td>${sku.nameByDrs}</td>
									</sec:authorize>
									<td><spring:message code="${sku.status}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
				</div>
			</div>
		</c:if>
	</div>
</div>