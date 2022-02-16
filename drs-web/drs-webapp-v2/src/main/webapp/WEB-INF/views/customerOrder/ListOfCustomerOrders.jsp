<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>
 
<script 
	defer
	src="https://use.fontawesome.com/releases/v5.6.3/js/all.js"
	integrity="sha384-EIHISlAOj4zgYieurP0SdoiBYfGJKkgWedPHH4jCzpCXLmzVsw1ouK59MuUtP4a1"
	crossorigin="anonymous">
</script>

<title><spring:message code="customerOrder.title" /> - DRS</title>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css" rel="stylesheet"/>

<!-- <link href="<c:url value="/resources/css/dcp-self-served.css"/>" type="text/css" rel="stylesheet"> -->
<script type='text/javascript' src="<c:url value="/resources/js/jquery.floatThead.js"/>"></script>

<style>
#customerOrder td {
	vertical-align: top;
}

.label-pill {
	padding-right: .6em;
	padding-left: .6em;
	border-radius: 8rem;
	padding-top: 3px;
	padding-bottom: 3px;
}

.popover {
	min-width: 580px;
    max-width: 650px;
    border-radius: 6px;
    border: none;
    box-shadow: 0 0 6px 1px #eee;
}
  
.tDtl{
	background-color: #f7f7f7; 
	padding: 2px; 
	font-weight: bold;
}

.tDtlH{
	background-color: #f7f7f7; padding-top: 2px; padding-bottom: 2px; font-weight: bold;
}
  
</style>

<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>

<script>

jQuery(window).on("load", function(e) {

    if ($('#isSearch').val() == 'true') {
	    $('#elasticSearch').prop('disabled', false);
	    $('.elasticSearch').show();
	    $('.filterSearch').hide();
	    $('.advancedSearch').hide();
	} else {
	    $('#elasticSearch').prop('disabled', true);
	    $('.elasticSearch').hide();
	    $('.filterSearch').show();
	}
	
	updateOrderDateTimeZoneText();
	
	var ListCustomerOrderCondition = ${ ListCustomerOrderCondition };
		
	
	if (ListCustomerOrderCondition.supplierKcode == null) {
		
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/CustomerOrderList/getProductSkuCodeToNameMap',
			contentType : "application/json; charset=utf-8",
			data : {
				productBaseCode : ListCustomerOrderCondition.relatedBaseProductCode,
			},
			dataType : "json",
			success : function(SKUs) {
				$('#SKU').empty();
				$('<option>').val("").text("All")
						.appendTo('#SKU');
				for ( var SKU in SKUs) {
					$('<option>').val(SKU).text(
							SKU + " " + SKUs[SKU])
							.appendTo('#SKU');
				}
				$('#SKU').val(ListCustomerOrderCondition.relatedSkuCode);
				
			}
		});
		
	} else {
		
		$.ajax({
			type : 'get',
			url : '${pageContext.request.contextPath}/CustomerOrderList/getProductBaseCodeToNameMap',
			contentType : "application/json; charset=utf-8",
			data : {
				supplierKcode : ListCustomerOrderCondition.supplierKcode,
			},
			dataType : "json",
			success : function(baseProducts) {
				$('#baseProduct').empty();
				if (!$.isEmptyObject(baseProducts)) {
					$('<option>').val("").text(
							"All").appendTo(
							'#baseProduct');
				}
				for ( var baseProduct in baseProducts) {
					$('<option>')
							.val(baseProduct)
							.text(
									baseProduct
											+ " "
											+ baseProducts[baseProduct])
							.appendTo(
									'#baseProduct');
				}
				
				$('#baseProduct').val(ListCustomerOrderCondition.relatedBaseProductCode);
				
				$.ajax({
					type : 'get',
					url : '${pageContext.request.contextPath}/CustomerOrderList/getProductSkuCodeToNameMap',
					contentType : "application/json; charset=utf-8",
					data : {
						productBaseCode : ListCustomerOrderCondition.relatedBaseProductCode,
					},
					dataType : "json",
					success : function(SKUs) {
						$('#SKU').empty();
						
						$('<option>')
								.val("")
								.text("All")
								.appendTo('#SKU');
						
						for ( var SKU in SKUs) {
							$('<option>')
								.val(SKU)
								.text(SKU + " " + SKUs[SKU])
								.appendTo('#SKU');
						}
						
						$('#SKU').val(ListCustomerOrderCondition.relatedSkuCode);
					}
				});
			}
		});
	}


});
				
				
$(document)
		.ready(
				function() {
					if($(window).width() >= 1024){
                        $('#customerOrder').floatThead({
                            scrollingTop : $("#s5_menu_wrap").height(),
                            zIndex : 100
                        });
                        $('#customerOrder').on(
                            "floatThead",
                            function(e, isFloated, $floatContainer) {
                                if (isFloated) {
                                    $floatContainer
                                            .addClass("table-floated");
                                } else {
                                    $floatContainer
                                            .removeClass("table-floated");
                                }
                            }
                        );
                    }
                    $( "#supplier" ).select2({
                        theme: "bootstrap"
                    });
					$('.select2').css("width", "100%");
					jQuery(
							"#orderDateStart,#orderDateEnd,#transactionDateStart,#transactionDateEnd")
							.datepicker(
									{
										beforeShow : function() {
											setTimeout(function() {
												$('.ui-datepicker').css(
														'z-index', 200);
											}, 0);
										},
										dateFormat : 'yy-mm-dd'
									}).keyup(function(e) {
									    if(e.keyCode == 8 || e.keyCode == 32 || e.keyCode == 46) {
									    	$(this).datepicker('setDate', null);
									    }
									});
					$(
							'#orderDateStart,#orderDateEnd,#transactionDateStart,#transactionDateEnd')
							.attr('readonly', true);
					$('[data-toggle="tooltip"]').tooltip();
					$('[data-toggle="popover"]')
							.popover(
									{
										html : true,
										title : function() {
											return $(this).parent().find(
													'div.poptitle').html();
										},
										container : 'body',
										trigger : 'focus',
										content : function() {
											var src = $(this).parent()
													.find('div.pop');
											if ($(this).attr(
													'data-salesChannel') != 'TrueToSource') {
												$.ajax({
															type : 'get',
															url : '${pageContext.request.contextPath}/CustomerOrderList/getAmazonOrderItemDetail',
															contentType : "application/json; charset=utf-8",
															data : {
																salesChannel : $(
																		this)
																		.attr(
																				'data-salesChannel'),
																orderId : $(
																		this)
																		.attr(
																				'data-orderId'),
																sku : $(
																		this)
																		.attr(
																				'data-sku')
															},
															dataType : "json",
															async : false,
															success : function(
																	amazonOrderItemDetail) {
																src
																		.find(
																				'[data-column="shippingMethod"]')
																		.html(
																				amazonOrderItemDetail.shippingMethod);
																src
																		.find(
																				'[data-column="shippingAddress"]')
																		.html(
																				amazonOrderItemDetail.shippingCity
																						+ ', '
																						+ amazonOrderItemDetail.shippingState
																						+ ', '
																						+ amazonOrderItemDetail.shippingCountry);
																src
																		.find(
																				'[data-column="salesTax"]')
																		.html(
																				amazonOrderItemDetail.saleTax);
																src
																		.find(
																				'[data-column="shippingPrice"]')
																		.html(
																				amazonOrderItemDetail.shippingPrice);
																src
																		.find(
																				'[data-column="shippingTax"]')
																		.html(
																				amazonOrderItemDetail.shippingTax);
																src
																		.find(
																				'[data-column="giftWrapPrice"]')
																		.html(
																				amazonOrderItemDetail.giftWrapPrice);
																src
																		.find(
																				'[data-column="giftWrapTax"]')
																		.html(
																				amazonOrderItemDetail.giftWrapTax);
																src
																		.find(
																				'[data-column="refundReason"]')
																		.html(
																				amazonOrderItemDetail.refundReason);
																src
																		.find(
																				'[data-column="RefundDetailedDisposition"]')
																		.html(
																				amazonOrderItemDetail.refundDetailedDisposition);
																src
																		.find(
																				'[id="fulfillmentService"]')
																		.hide();
																src
																		.find(
																				'[id="totalTax"]')
																		.hide();
															}
														});
											}
											if ($(this).attr(
													'data-salesChannel') == 'TrueToSource') {
												$
														.ajax({
															type : 'get',
															url : '${pageContext.request.contextPath}/CustomerOrderList/getShopifyOrderItemDetail',
															contentType : "application/json; charset=utf-8",
															data : {
																orderId : $(
																		this)
																		.attr(
																				'data-orderId'),
																sku : $(
																		this)
																		.attr(
																				'data-sku')
															},
															dataType : "json",
															async : false,
															success : function(
																	shopifyOrderItemDetail) {
																console
																		.log(shopifyOrderItemDetail);
																src
																		.find(
																				'[data-column="shippingMethod"]')
																		.html(
																				shopifyOrderItemDetail.shippingMethod);
																src
																		.find(
																				'[data-column="shippingAddress"]')
																		.html(
																				shopifyOrderItemDetail.shippingCity
																						+ ', '
																						+ shopifyOrderItemDetail.shippingState
																						+ ', '
																						+ shopifyOrderItemDetail.shippingCountry);
																src
																		.find(
																				'[data-column="fulfillmentService"]')
																		.html(
																				shopifyOrderItemDetail.fulfillmentService);
																src
																		.find(
																				'[data-column="totalTax"]')
																		.html(
																				shopifyOrderItemDetail.tax);
																src
																		.find(
																				'[id="salesTax"]')
																		.hide();
																src
																		.find(
																				'[id="shippingPrice"]')
																		.hide();
																src
																		.find(
																				'[id="shippingTax"]')
																		.hide();
																src
																		.find(
																				'[id="giftWrapPrice"]')
																		.hide();
																src
																		.find(
																				'[id="giftWrapTax"]')
																		.hide();
															}
														});
											}
											return $(this).parent().find('div.pop').html();
										}
										//,
										//template : '<div class="popover" role="tooltip" style="width:600px;max-width:600px"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
									})

				});

	function resetByDRSStaff() {
		$(
				"#supplier,#amazonMerchantSKU,#asin,#salesChannel,#orderStatus,#orderDateStart,#orderDateEnd,#transactionDateStart,#transactionDateEnd,#marketplaceOrderId,#customerName,#buyerEmail,#promotionId")
				.val("");
		$('#baseProduct').empty();
		$('#SKU').empty();
		$('<option>').val("").text("All").appendTo('#baseProduct');
		$('<option>').val("").text("All").appendTo('#SKU');
	};

	function resetBySupplier() {
		$(
				"#baseProduct,#asin,#salesChannel,#orderStatus,#orderDateStart,#orderDateEnd,#transactionDateStart,#transactionDateEnd,#marketplaceOrderId,#customerName,#promotionId")
				.val("");
		$('#SKU').empty();
		$('<option>').val("").text("All").appendTo('#SKU');
	};

	function toggleFilterSearch() {
	    if ($('.filterSearch').is(':hidden')) {
	        $('#isSearch').val('false');
	        $('.elasticSearch').hide();
	        $('.filterSearch').show();
	    } else {
	        $('#isSearch').val('true');
	        $('.elasticSearch').show();
	        $('.filterSearch').hide();
	        $('.advancedSearch').hide();
	    }
		$('#customerOrder').floatThead('reflow');
	    $('#elasticSearch').prop('disabled', function(i, v) { return !v; });

	}


	function showForm() {
		var x = document.getElementById("adv_form");
		if (x.style.display === "flex") {
			x.style.display = "none";
			$('#customerOrder').floatThead('reflow');
		} else {
			x.style.display = "flex";
			$('#customerOrder').floatThead('reflow');
		}
	};
	

	function getBaseProducts() {
		$
			.ajax({
				type : 'get',
				url : '${pageContext.request.contextPath}/CustomerOrderList/getProductBaseCodeToNameMap',
				contentType : "application/json; charset=utf-8",
				data : {
					supplierKcode : $("#supplier").val(),
				},
				dataType : "json",
				
				error : function(xhr) {
					$('#baseProduct').empty();
					$('#SKU').empty();
					$('<option>').val("").text("All").appendTo(
							'#baseProduct');
					$('<option>').val("").text("All").appendTo('#SKU');
				},
				
				success : function(baseProducts) {
					$('#baseProduct').empty();
					$('#SKU').empty();
					$('<option>').val("").text("All").appendTo('#SKU');
					
					if (!$.isEmptyObject(baseProducts)){
						$('<option>').val("").text("All").appendTo(
								'#baseProduct');
					}
					
					for ( var baseProduct in baseProducts) {
						$('<option>').val(baseProduct).text(
								baseProduct + " "
										+ baseProducts[baseProduct])
								.appendTo('#baseProduct');
					}
				}
			});
	};
	
	
	function getSKUs() {
		$
				.ajax({
					type : 'get',
					url : '${pageContext.request.contextPath}/CustomerOrderList/getProductSkuCodeToNameMap',
					contentType : "application/json; charset=utf-8",
					data : {
						productBaseCode : $("#baseProduct").val(),
					},
					dataType : "json",
					success : function(SKUs) {
						$('#SKU').empty();
						$('<option>').val("").text("All").appendTo('#SKU');
						for ( var SKU in SKUs) {
							$('<option>').val(SKU).text(SKU + " " + SKUs[SKU])
									.appendTo('#SKU');
						}
					}
				});
	};

	var orderDateTimeZoneText = [];
	orderDateTimeZoneText['UTC'] = "<spring:message code='customerOrder.orderDateTimeZoneTextUtc' />";
	orderDateTimeZoneText['LOCAL'] = "<spring:message code='customerOrder.orderDateTimeZoneTextLocal' />";

	function updateOrderDateTimeZoneText() {
		var selectedSalesChannel = $("#salesChannel option:selected").text();
		if (selectedSalesChannel == "All"
				|| selectedSalesChannel == "Non-order") {
			$('#orderDateTimeZoneText').text(orderDateTimeZoneText['UTC']);
		} else {
			$('#orderDateTimeZoneText').text(orderDateTimeZoneText['LOCAL']);
		}
	}
</script>



<style>
#adv_form {
	display: none;
}

</style>

</head>
<div class="max-width">
	<div class="container-fluid">
	
		<div class="row">
			<div class="col-md-12">
				<div class="page-heading">
					<spring:message code="customerOrder.searchCustomerOrders" />
				</div>
			</div>
		</div>

		<c:url var="normalSearch" value="/CustomerOrderList"></c:url>
		<form:form id="CustomerOrder" method="GET"
			name="CustomerOrder" class="form-horizontal text-left" modelAttribute="CustomerOrder">

		    <form:hidden id="isSearch" path="isSearch" />
            <div class="row elasticSearch">
                <div class="col-md-12 elasticSearch">
                    <spring:message code='customerOrder.elasticNote' />
                </div>
            </div>

            <div class="row filterSearch" style="display: none;">
                <div class="col-md-12">
                    <spring:message code='customerOrder.note' />
                </div>
            </div>

			<div class="row my-3" >
				<div class="col-md-6">
					<div class="form-group elasticSearch">
						<label for="searchTerms"><spring:message
								code="customerOrder.elasticFields" /></label>
						<form:input id="searchTerms" class="form-control"
							path="searchTerms" />

					</div>
				
					<sec:authorize
						access="hasAnyRole('${auth['CUSTOMER_ORDER_VIEW_DRS_STAFF']}')">
						<div class="form-group filterSearch" style="display: none;">
							<label for="supplier"><spring:message
									code="customerOrder.supplier" /></label>
							<form:select id="supplier" class="form-control"
								path="supplierKcode" onchange="getBaseProducts()">
								<form:option value="">All</form:option>
								<c:forEach var="supplierKcodeToNameMap"
									items="${supplierKcodeToNameMap}">
									<form:option value="${supplierKcodeToNameMap.key}">${supplierKcodeToNameMap.key} ${supplierKcodeToNameMap.value}</form:option>
								</c:forEach>
							</form:select>
						</div>
					</sec:authorize>
					
					<div class="form-group filterSearch" style="display: none;">
						<label for="baseProduct"><spring:message
								code="customerOrder.baseProduct" /></label>
						<form:select id="baseProduct" class="form-control"
							path="relatedBaseProductCode" onchange="getSKUs()">
							<form:option value="">All</form:option>
							<c:forEach var="productBaseCodeToNameMap"
								items="${productBaseCodeToNameMap}">
								<form:option value="${productBaseCodeToNameMap.key}">${productBaseCodeToNameMap.key} ${productBaseCodeToNameMap.value}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class="form-group filterSearch" style="display: none;">
						<label for="SKU"><spring:message code="customerOrder.SKU" /></label>
						<form:select id="SKU" class="form-control" path="relatedSkuCode">
							<form:option value="">All</form:option>
						</form:select>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label for="orderDateTimeZoneText"><spring:message
								code="customerOrder.orderTime" /></label>
						<div class="form-row">
							<div class="col">
								<form:input id="orderDateStart" class="form-control"
									style="display: inline;cursor:default;background-color: white;"
									path="orderDateStart" />
							</div>
							<div class="col-1 text-center">---</div>
							<div class="col">
								<form:input id="orderDateEnd" class="form-control"
									style="display: inline;cursor:default;background-color: white;"
									path="orderDateEnd" />
							</div>
						</div>
					</div>
					
					<div class="form-group filterSearch" style="display: none;">
						<label for="salesChannel"><spring:message
								code="common.marketplace" /></label>
						<form:select id="salesChannel" class="form-control"
							path="salesChannelId" onchange="updateOrderDateTimeZoneText()">
							<form:option value="">All</form:option>
							<c:forEach var="salesChannel" items="${salesChannels}">
								<form:option value="${salesChannel.key}">${salesChannel.displayName}</form:option>
							</c:forEach>
						</form:select>
					</div>
					
					<div class="form-group filterSearch" style="display: none;">
						<label for="orderStatus"><spring:message
								code="customerOrder.orderStatus" /></label>
						<form:select id="orderStatus" class="form-control"
							path="orderStatus" value="${orderStatus}">
							<form:option value="">All</form:option>
							<form:option value="Canceled">Canceled</form:option>
							<form:option value="Shipped">Shipped</form:option>
							<form:option value="Pending">Pending</form:option>
							<form:option value="UNFULFILLABLE">UNFULFILLABLE</form:option>
							<form:option value="COMPLETE_PARTIALLED">COMPLETE_PARTIALLED</form:option>
							<form:option value="PROCESSING">PROCESSING</form:option>
							<form:option value="COMPLETE">COMPLETE</form:option>
							<form:option value="refunded">refunded</form:option>
							<form:option value="paid">paid</form:option>
							<form:option value="partially_refunded">partially_refunded</form:option>
						</form:select>
					</div>

				</div>
			</div>
			<div class="row advancedSearch" id="adv_form">
				<div class="col-md-6">
					<sec:authorize
						access="hasAnyRole('${auth['CUSTOMER_ORDER_VIEW_DRS_STAFF']}')">
						<div class="form-group">
							<label for="amazonMerchantSKU"><spring:message
									code="customerOrder.amazonMerchantSKU" /></label>
							<form:input id="amazonMerchantSKU" class="form-control"
								path="amazonMerchantSKU" />
						</div>
					</sec:authorize>
					<div class="form-group">
						<label for="asin"><spring:message
								code="customerOrder.asin" /></label>
						<form:input id="asin" class="form-control" path="asin" />
					</div>
					<div class="form-group">
						<label for="transactionDateStart"><spring:message
								code="customerOrder.transactionTimeUTC" /></label>
						<div class="form-row">
							<div class="col">
								<form:input id="transactionDateStart" class="form-control"
									style="display: inline;cursor:default;background-color: white;"
									path="transactionDateStart" />
							</div>
							<div class="col-1 text-center">---</div>
							<div class="col">
								<form:input id="transactionDateEnd" class="form-control"
									style="display: inline;cursor:default;background-color: white;"
									path="transactionDateEnd" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="marketplaceOrderId"><spring:message
								code="customerOrder.marketplaceOrderId" /></label>
						<form:input id="marketplaceOrderId" class="form-control"
							path="marketplaceOrderId" />
					</div>
				</div>

				<div class="col-md-6">
					<div class="form-group">
						<label for="customerName"><spring:message
								code="customerOrder.customerName" /></label>
						<form:input id="customerName" class="form-control"
							path="customerName" />
					</div>
					<div class="form-group">
						<sec:authorize
							access="hasAnyRole('${auth['CUSTOMER_ORDER_VIEW_DRS_STAFF']}')">
							<label for="buyerEmail"><spring:message
									code="customerOrder.buyerEmail" /></label>
							<form:input id="buyerEmail" class="form-control"
								path="buyerEmail" />
						</sec:authorize>
					</div>
					<div class="form-group">
						<label for="customerName"><spring:message
								code="customerOrder.promotionId" /></label>
						<form:input id="promotionId" class="form-control"
							path="promotionId" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 text-right">

                    <button class="btn btn-primary elasticSearch" type="submit" formaction="${normalSearch}">
                        <i class="fas fa-search"></i>
                        <spring:message code='customerOrder.search' />
                    </button>
                    <button class="btn btn-primary filterSearch" style="display: none;" type="submit" formaction="${normalSearch}">
                        <i class="fas fa-filter"></i>
                        <spring:message code='customerOrder.submit' />
                    </button>

					<sec:authorize
						access="hasAnyRole('${auth['CUSTOMER_ORDER_VIEW_DRS_STAFF']}')">
						<button type="button" class="btn btn-default filterSearch" style="display: none;"
							onclick="resetByDRSStaff()">
							<spring:message code='customerOrder.clear' />
						</button>
					</sec:authorize>
					<sec:authorize
						access="hasAnyRole('${auth['CUSTOMER_ORDER_VIEW_SUPPLIER']}')">
						<button type="button" class="btn btn-default filterSearch" style="display: none;"
							onclick="resetBySupplier()">
							<spring:message code='customerOrder.clear' />
						</button>
					</sec:authorize>
					<button id="show_button" type="button" class="btn btn-default filterSearch" style="display: none;"
						onclick="showForm()">
						<spring:message code='customerOrder.advanced' />
					</button>
					<input id="orderExport" class="btn btn-success filterSearch" style="display: none;" type="button"
						value="<spring:message code='customerOrder.export'/>" />
					<script>
						$("#orderExport").click(
								function(e) {
									e.preventDefault();
									var urlParams = $("#CustomerOrder")
											.serializeArray();
									var paramString = "";
									for (i = 0; i < urlParams.length; i++) {
										if (i == 0) {
											var param = "?"
													+ urlParams[i]["name"]
													+ "="
													+ urlParams[i]["value"];
										} else {
											var param = "&"
													+ urlParams[i]["name"]
													+ "="
													+ urlParams[i]["value"];
										}
										paramString += param;
									}
									$('#orderIframe').attr(
											'src',
											"${pageContext.request.contextPath}/CustomerOrderList/export"
													+ paramString);
								});
					</script>
					<iframe id="orderIframe" style="border: none; height: 0; width: 0;"></iframe>

                    <button id="toggleSearch" type="button" class="btn btn-default elasticSearch"
                        onclick="toggleFilterSearch()">
                        <spring:message code='customerOrder.filter' />
                    </button>

                    <button id="toggleSearch" type="button" class="btn btn-default filterSearch" style="display: none;"
                        onclick="toggleFilterSearch()">
                        <i class="fas fa-backward"></i>
                    </button>

				</div>
			</div>

		</form:form>

		<div id="customerOrderResultTitle" class="row">
			<div class="col-md-12">
				<div class="page-heading">${searchResultTitle}</div>
				<c:if test="${!empty customerOrders}">
					<span> <spring:message code='customerOrder.pageInfo'
							arguments="${startCountPerPage};${endCountPerPage}"
							argumentSeparator=";" /> <c:choose>
							<c:when test="${totalPages > 1}">
								<spring:message code='customerOrder.pageInfoTotal'
									arguments="${totalRecords}" />
							</c:when>
						</c:choose>
					</span>
				</c:if>
			</div>
		</div>
		<div id="customerOrderResult" class="row">
			<div class="col-md-12">
				<div id="pager">
					<c:choose>
						<c:when test="${totalPages > 1}">
                            <c:url value="/CustomerOrderList" var="URL">
                                <c:forEach var="customerOrderCondition"
                                    items="${customerOrderConditions}">
                                    <c:param name="${customerOrderCondition.key}"
                                        value="${customerOrderCondition.value}" />
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
												<li class="page-item"><a class="page-link"
													href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
												<li class="page-item"><a class="page-link"
													href="${prevUrl}" data-pager="${currentPageIndex - 1}">&lt;</a></li>
											</c:when>
										</c:choose>
										<c:forEach var="i" begin="${startPage}" end="${endPage}">
											<c:url var="pageUrl" value="${URL}&page=${i}" />
											<c:choose>
												<c:when test="${i == currentPageIndex}">
													<li class="active page-item"><a class="page-link"
														href="${pageUrl}" data-pager="${i}"><c:out
																value="${i}" /></a></li>
												</c:when>
												<c:otherwise>
													<li class="page-item"><a class="page-link"
														href="${pageUrl}" data-pager="${i}"><c:out
																value="${i}" /></a></li>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										<c:choose>
											<c:when test="${currentPageIndex != totalPages}">
												<li class="page-item"><a class="page-link"
													href="${nextUrl}" data-pager="${currentPageIndex + 1}">&gt;</a></li>
												<li class="page-item"><a class="page-link"
													href="${lastUrl}" data-pager="${totalPages}">&gt;&gt;</a></li>
											</c:when>
										</c:choose>
									</ul>
								</nav>
							</div>
						</c:when>
					</c:choose>
				</div>
				<div class="table-responsive">
					<table id="customerOrder" class="table table-striped table-floated">
						<thead id="tableHeader">
							<tr style="height: 50px;">
								<th style="vertical-align: bottom; width: 135px;"><spring:message
										code="customerOrder.orderTimeLocal" /></th>
								<th style="vertical-align: bottom; width: 135px;"><spring:message
										code="customerOrder.orderTimeUTC" /></th>
								<th style="vertical-align: bottom; width: 135px;"><spring:message
										code="customerOrder.transactionTimeUTC" /></th>
								<th style="vertical-align: bottom;"><spring:message
										code="customerOrder.marketplaceOrderId" /> <br> <spring:message
										code="customerOrder.buyer" /></th>
								<th style="vertical-align: bottom; width: 265px;"><spring:message
										code="customerOrder.SKUCode" /> <br> <spring:message
										code="customerOrder.productName" /></th>
								<th style="vertical-align: bottom;"><spring:message
										code="customerOrder.orderStatus" /></th>
								<th style="vertical-align: bottom; width: 190px;"><spring:message
										code="common.marketplace" /> <br> <spring:message
										code="customerOrder.promotionId" /></th>
								<th class="text-left" style="vertical-align: bottom;"><spring:message
										code="customerOrder.fulfillmentCenter" /></th>
								<th class="text-right" style="vertical-align: bottom;"><spring:message
										code="customerOrder.qty" /></th>
								<th class="text-right" style="vertical-align: bottom;"><spring:message
										code="customerOrder.actualRetailPrice" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${customerOrders}" var="customerOrder" varStatus="orderCount">
								<c:choose>
									<c:when
										test="${customerOrder.orderStatus == 'Pending' || customerOrder.orderStatus == 'Canceled'}">
										<tr class="text-muted">
									</c:when>
									<c:otherwise>
										<tr>
									</c:otherwise>
								</c:choose>
								<td>${customerOrder.orderTimeLocal}</td>
								<td>${customerOrder.orderTimeUTC}</td>
								<td>${customerOrder.transactionTimeUTC}</td>
								<td>
									<c:choose>
										<c:when
											test="${empty customerOrder.salesChannel || customerOrder.salesChannel == 'eBay'}">
											${customerOrder.marketplaceOrderId}
										</c:when>
										<c:otherwise>
											<a id="ordrerCount${orderCount.index}"
												tabindex="${orderCount.index}" class="popTrigger"
												data-toggle="popover"
												style="text-decoration: none; cursor: pointer;"
												data-salesChannel="${customerOrder.salesChannel}"
												data-orderId="${customerOrder.marketplaceOrderId}"
												data-sku="${customerOrder.SKUCode}">${customerOrder.marketplaceOrderId}</a>
										</c:otherwise>
									</c:choose> <br>${customerOrder.buyer}
									<div class="poptitle" style="display: none;">
										<b>${customerOrder.SKUCode} ${customerOrder.productName}
											on ${customerOrder.salesChannel} </b>
									</div>
									<div class="pop" style="display: none;">
										<div class="row">
											<div class="col-md-12"></div>
										</div>
										<div class="row">
											<div class="col-md-6 text-right">Order ID</div>
											<div class="col-md-6 text-left">${customerOrder.marketplaceOrderId}</div>
										</div>
										<div class="row">
											<div class="col-md-6 text-right">Purchase date</div>
											<div class="col-md-6 text-left">${fn:substring(customerOrder.orderTimeUTC,0,10)}</div>
										</div>
										<div class="row">
											<div class="col-md-6 text-right">Order Status</div>
											<c:choose>
												<c:when test="${customerOrder.orderStatus == 'Refunded'}">
													<div class="col-md-6 text-left" style="color: #cc0000;">
														${customerOrder.orderStatus} <br> <span
															data-column="refundReason"></span>, <span
															data-column="RefundDetailedDisposition"></span>
													</div>
												</c:when>
												<c:otherwise>
													<div class="col-md-6 text-left">
														${customerOrder.orderStatus}</div>
												</c:otherwise>
											</c:choose>
										</div>
										
										<div class="row">
											<div class="col-md-6 text-right">Shipping Service</div>
											<div class="col-md-6 text-left" data-column="shippingMethod"></div>
										</div>
										
										<div class="row">
											<div class="col-md-6 text-right">Customer Name</div>
											<div class="col-md-6 text-left">${customerOrder.buyer}</div>
										</div>
										
										<div class="row">
											<div class="col-md-6 text-right">Ship to</div>
											<div class="col-md-6 text-left" data-column="shippingAddress"></div>
										</div>
										
										<c:if test="${not empty customerOrder.promotionId}">
											<div class="row">
												<div class="col-md-6 text-right">Promotion / Rebates</div>
												<div class="col-md-6 text-left">${customerOrder.promotionId}</div>
											</div>
										</c:if>
										<div id="fulfillmentService" class="row">
											<div class="col-md-6 text-right">Fulfillment Service /
												ID</div>
											<div class="col-md-6 text-left"
												data-column="fulfillmentService"></div>
										</div>
										
										<br />
										
										<div class="row">
											<div class="col-md-12 text-center tDtl">Transaction Details</div>
										</div>
										<div class="row">
											<div class="col-md-2 text-right tDtlH">Quantity</div>
											<div class="col-md-2 text-right tDtlH">Unit
												Price</div>
											<div class="col-md-4 text-center tDtlH">&nbsp;</div>
											<div class="col-md-2 text-right tDtlH">Order</div>
											<div class="col-md-2 text-right tDtlH">
												<c:choose>
													<c:when test="${customerOrder.orderStatus == 'Refunded'}">
									&nbsp;
								</c:when>
													<c:otherwise>
									&nbsp;
								</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div style="padding-bottom: 10px"></div>
										<div class="row">
											<div class="col-md-2 text-right">${customerOrder.qty}</div>
											<div class="col-md-2 text-right">
												<c:choose>
													<c:when
														test="${customerOrder.itemPrice != '' && customerOrder.qty != '' && customerOrder.qty !=  0}">
														<fmt:formatNumber pattern="###,###.00" type="number"
															value="${customerOrder.itemPrice/customerOrder.qty}" />
													</c:when>
												</c:choose>
											</div>
											<div class="col-md-4 text-right">Total product charges</div>
											<div class="col-md-2 text-right">${customerOrder.actualRetailPrice}</div>
											<div class="col-md-2 text-right">&nbsp;</div>
										</div>
										<div id="salesTax" class="row">
											<div class="col-md-2 text-right"></div>
											<div class="col-md-2 text-right"></div>
											<div class="col-md-4 text-right">Sales tax</div>
											<div class="col-md-2 text-right" data-column="salesTax"></div>
											<div class="col-md-2 text-right">&nbsp;</div>
										</div>
										<div id="shippingPrice" class="row">
											<div class="col-md-2 text-right"></div>
											<div class="col-md-2 text-right"></div>
											<div class="col-md-4 text-right">Shipping price</div>
											<div class="col-md-2 text-right" data-column="shippingPrice"></div>
											<div class="col-md-2 text-right">&nbsp;</div>
										</div>
										<div id="shippingTax" class="row">
											<div class="col-md-2 text-right"></div>
											<div class="col-md-2 text-right"></div>
											<div class="col-md-4 text-right">Shipping tax</div>
											<div class="col-md-2 text-right" data-column="shippingTax"></div>
											<div class="col-md-2 text-right">&nbsp;</div>
										</div>
										<div id="giftWrapPrice" class="row">
											<div class="col-md-2 text-right"></div>
											<div class="col-md-2 text-right"></div>
											<div class="col-md-4 text-right">Gift wrap price</div>
											<div class="col-md-2 text-right" data-column="giftWrapPrice"></div>
											<div class="col-md-2 text-right">&nbsp;</div>
										</div>
										<div id="giftWrapTax" class="row">
											<div class="col-md-2 text-right"></div>
											<div class="col-md-2 text-right"></div>
											<div class="col-md-4 text-right">Gift wrap tax</div>
											<div class="col-md-2 text-right" data-column="giftWrapTax"></div>
											<div class="col-md-2 text-right">&nbsp;</div>
										</div>
										<div id="totalTax" class="row">
											<div class="col-md-2 text-right"></div>
											<div class="col-md-2 text-right"></div>
											<div class="col-md-4 text-right">Total tax</div>
											<div class="col-md-2 text-right" data-column="totalTax"></div>
											<div class="col-md-2 text-right">&nbsp;</div>
										</div>
									</div></td>
								<td style="white-space: nowrap; max-width: 200px;">${customerOrder.SKUCode}
									<br> <c:if
										test="${(fn:length(customerOrder.productName)) > 40}">
										<c:set var="shortProductName"
											value="${fn:substring(customerOrder.productName,0,39)}" />
										<span title="${customerOrder.productName}" data-toggle="tooltip">${shortProductName }...</span>
									</c:if> <c:if test="${(fn:length(customerOrder.productName)) <= 40}">
								${customerOrder.productName}
							</c:if>
								</td>
								<c:choose>
									<c:when test="${customerOrder.orderStatus == 'Refunded'}">
										<td style="color: #ff6600;">${customerOrder.orderStatus}</td>
									</c:when>
									<c:otherwise>
										<td>${customerOrder.orderStatus}</td>
									</c:otherwise>
								</c:choose>
								<td style="white-space: nowrap; max-width: 175px;"><sec:authorize
										access="hasAnyRole('${auth['CUSTOMER_ORDER_VIEW_DRS_STAFF']}')">
										<c:choose>
											<c:when
												test="${customerOrder.salesChannel == 'TrueToSource'}">
												<a
													href="${marketplaceToOrderUrlPrefixMap[customerOrder.salesChannel]}${customerOrder.shopifyOrderId}"
													target="_blank">${customerOrder.salesChannel}</a>
											</c:when>
											<c:when test="${customerOrder.salesChannel == 'eBay'}">
										${customerOrder.salesChannel}								
									</c:when>
											<c:otherwise>
												<a
													href="${marketplaceToOrderUrlPrefixMap[customerOrder.salesChannel]}${customerOrder.marketplaceOrderId}"
													target="_blank">${customerOrder.salesChannel}</a>
											</c:otherwise>
										</c:choose>
									</sec:authorize> <sec:authorize
										access="hasAnyRole('${auth['CUSTOMER_ORDER_VIEW_SUPPLIER']}')">
									${customerOrder.salesChannel}								
								</sec:authorize> <br> <c:if
										test="${(fn:length(customerOrder.promotionId)) > 30}">
										<c:set var="shortPromotionId"
											value="${fn:substring(customerOrder.promotionId,0,29)}" />
										<span title="${customerOrder.promotionId}"
											data-toggle="tooltip">${shortPromotionId }...</span>
									</c:if> <c:if test="${(fn:length(customerOrder.promotionId)) <= 30}">
								${customerOrder.promotionId}
							</c:if></td>
								<td>${customerOrder.fulfillmentCenter}</td>
								<td class="text-right">${customerOrder.qty}</td>
								<td class="text-right">${customerOrder.actualRetailPrice}</td>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div id="customerOrderResult" class="row">
					<div class="col-md-12">
						<div id="pager">
							<c:choose>
								<c:when test="${totalPages > 1}">
									<c:url value="/CustomerOrderList" var="URL">
										<c:forEach var="customerOrderCondition"
											items="${customerOrderConditions}">
											<c:param name="${customerOrderCondition.key}"
												value="${customerOrderCondition.value}" />
										</c:forEach>
									</c:url>
									<c:url var="firstUrl" value="${URL}&page=1" />
									<c:url var="lastUrl" value="${URL}&page=${totalPages}" />
									<c:url var="prevUrl"
										value="${URL}&page=${currentPageIndex - 1}" />
									<c:url var="nextUrl"
										value="${URL}&page=${currentPageIndex + 1}" />
									<div class="text-center">
										<nav>
											<ul class="pagination pagination-sm justify-content-center">
												<c:choose>
													<c:when test="${currentPageIndex != 1}">
														<li class="page-item"><a class="page-link"
															href="${firstUrl}" data-pager="1">&lt;&lt;</a></li>
														<li class="page-item"><a class="page-link"
															href="${prevUrl}" data-pager="${currentPageIndex - 1}">&lt;</a></li>
													</c:when>
												</c:choose>
												<c:forEach var="i" begin="${startPage}" end="${endPage}">
													<c:url var="pageUrl" value="${URL}&page=${i}" />
													<c:choose>
														<c:when test="${i == currentPageIndex}">
															<li class="active page-item"><a class="page-link"
																href="${pageUrl}" data-pager="${i}"><c:out
																		value="${i}" /></a></li>
														</c:when>
														<c:otherwise>
															<li class="page-item"><a class="page-link"
																href="${pageUrl}" data-pager="${i}"><c:out
																		value="${i}" /></a></li>
														</c:otherwise>
													</c:choose>
												</c:forEach>
												<c:choose>
													<c:when test="${currentPageIndex != totalPages}">
														<li class="page-item"><a class="page-link"
															href="${nextUrl}" data-pager="${currentPageIndex + 1}">&gt;</a></li>
														<li class="page-item"><a class="page-link"
															href="${lastUrl}" data-pager="${totalPages}">&gt;&gt;</a></li>
													</c:when>
												</c:choose>
											</ul>
										</nav>
									</div>
								</c:when>
							</c:choose>
						</div>
						<div class="row">
							<div class="col-md-12">
								<c:if test="${!empty customerOrders}">
									<span> <spring:message code='customerOrder.pageInfo'
											arguments="${startCountPerPage};${endCountPerPage}"
											argumentSeparator=";" /> <c:choose>
											<c:when test="${totalPages > 1}">
												<spring:message code='customerOrder.pageInfoTotal'
													arguments="${totalRecords}" />
											</c:when>
										</c:choose>
									</span>
								</c:if>
							</div>
						</div>
					</div>
				</div>