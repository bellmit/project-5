<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@	taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>
  <title>
    <c:choose>
      <c:when
        test="${InventoryShipment.status eq 'SHPT_DRAFT' || InventoryShipment.status eq 'SHPT_AWAIT_PLAN' || InventoryShipment.status eq 'SHPT_PLANNING'}">
        <spring:message code='inventoryShipment.inventoryShipment' />: ${InventoryShipment.name} - DRS
      </c:when>
      <c:otherwise>
        <spring:message code='inventoryShipment.inventoryPurchaseOrder' />: ${InventoryShipment.name} - DRS
      </c:otherwise>
    </c:choose>
  </title>

  <link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>

  <script>
    var app = angular.module('inventoryShipmentView', []);
    app.controller('inventoryShipmentViewCtrl', function ($scope) {

      var InventoryShipmentJson = ${ InventoryShipmentJson };
      var lineItems = InventoryShipmentJson.lineItems;
      var totalQuantity = 0;
      var totalCartonCounts = 0;

      for (i = 0; i < lineItems.length; i++) {
        var quantity = Number(lineItems[i].quantity);
        var cartonCounts = Number(lineItems[i].cartonCounts);
        totalQuantity += quantity;
        totalCartonCounts += cartonCounts;
      }
      $("#totalQuantity").html(totalQuantity);
      $("#totalCartonCounts").html(totalCartonCounts);

      $scope.lineItems = [];
      $scope.mixedContentBoxes = [];

      for (i = 0; i < lineItems.length; i++) {
        if (lineItems[i].mixedBoxLineSeq == 0) $scope.lineItems.push(lineItems[i]);
      }

      for (i = 0; i < lineItems.length; i++) {
        if (lineItems[i].mixedBoxLineSeq != 0) {
          if (typeof $scope.mixedContentBoxes[lineItems[i].boxNum] == "undefined") $scope.mixedContentBoxes[lineItems[i].boxNum] = [];
          $scope.mixedContentBoxes[lineItems[i].boxNum].push(lineItems[i]);
        }
      }

      $scope.mixedContentBoxes = $scope.mixedContentBoxes.filter(function (el) { return el; });

    });

    function submitConfirm(shipmentId) {
      $("#dialog-confirm").html('<spring:message code="inventoryShipment.submitISWarning" /> ');
      $("#dialog-confirm").dialog({
        open: function () { $('.ui-dialog-buttonset button[name="no"]').focus(); },
        resizable: false,
        modal: true,
        title: "Confirmation",
        height: 250,
        width: 400,
        buttons: [
          {
            text: "<spring:message code='inventoryShipment.yes' /> ",
            name: "yes",
            click: function () {
              $(this).dialog('close');
              location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/submit";
            }
          },
          {
            text: "<spring:message code='inventoryShipment.no' /> ",
            name: "no",
            click: function () {
              $(this).dialog('close');
            }
          }
        ]
      });
    }

    function acceptConfirm(shipmentId) {
      $("#dialog-confirm").html('<spring:message code="inventoryShipment.acceptISWarning" /> ');
      $("#dialog-confirm").dialog({
        open: function () { $('.ui-dialog-buttonset button[name="no"]').focus(); },
        resizable: false,
        modal: true,
        title: "Confirmation",
        height: 250,
        width: 400,
        buttons: [
          {
            text: "<spring:message code='inventoryShipment.yes' /> ",
            name: "yes",
            click: function () {
              $(this).dialog('close');
              location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/accept";
            }
          },
          {
            text: "<spring:message code='inventoryShipment.no' /> ",
            name: "no",
            click: function () {
              $(this).dialog('close');
            }
          }
        ]
      });
    }

    function confirmConfirm(shipmentId) {
      $("#dialog-confirm").html('<spring:message code="inventoryShipment.confirmISWarning" /> ');
      $("#dialog-confirm").dialog({
        open: function () { $('.ui-dialog-buttonset button[name="no"]').focus(); },
        resizable: false,
        modal: true,
        title: "Confirmation",
        height: 250,
        width: 400,
        buttons: [
          {
            text: "<spring:message code='inventoryShipment.yes' /> ",
            name: "yes",
            click: function () {
              $(this).dialog('close');
              location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/confirm";
            }
          },
          {
            text: "<spring:message code='inventoryShipment.no' /> ",
            name: "no",
            click: function () {
              $(this).dialog('close');
            }
          }
        ]
      });
    }

    function deleteConfirm(shipmentId) {
      $("#dialog-confirm").html('<spring:message code="inventoryShipment.deleteISWarning" /> ');
      $("#dialog-confirm").dialog({
        open: function () { $('.ui-dialog-buttonset button[name="no"]').focus(); },
        resizable: false,
        modal: true,
        title: "Confirmation",
        height: 250,
        width: 400,
        buttons: [
          {
            text: "<spring:message code='inventoryShipment.yes' /> ",
            name: "yes",
            click: function () {
              $(this).dialog('close');
              location.href = "${pageContext.request.contextPath}/InventoryShipments/" + shipmentId + "/delete";
            }
          },
          {
            text: "<spring:message code='inventoryShipment.no' /> ",
            name: "no",
            click: function () {
              $(this).dialog('close');
            }

          }
        ]
      });
    }
  </script>

  <style>
    /* .ivs-tb tr td {
      border: none;
    } */

    .ivs-tb tr td:first-child {
      font-weight: 500;
      text-align: left;
    }

    .ivs-tb tr td:last-child {
      text-align: left;
    }

    /* .mixed-tb tr td:first-child {
      border-top: 0;
    } */

    /* .card {
      padding: 2.5rem;
    } */

    /* .font-1 {
      font-size: 1.15rem;
    } */

    .btn {
      padding: 11px 22px;
    }
    .info > div:first-child {
      font-weight: 900;
    }
    .noselect {
      -webkit-touch-callout: none; /* iOS Safari */
      -webkit-user-select: none; /* Safari */
      -khtml-user-select: none; /* Konqueror HTML */
      -moz-user-select: none; /* Old versions of Firefox */
      -ms-user-select: none; /* Internet Explorer/Edge */
      user-select: none; /* Non-prefixed version, currently supported by Chrome, Opera and Firefox */
    }

    .sum-list {
      display: table;
      width: 100%;
      /* text-align: center; */
    }
    .sum-list ul {
      display: table-row;
    }
    .sum-list ul:first-child {
      background: #fff;
      color: #000;
      font-weight: 900;
    }

    .sum-list ul:last-child {
      background: #f2f2f2;
      color: #000;
    }

    .sum-list ul:last-child > li:last-child {
      font-weight: 900;
    }

    .sum-list ul > li {
      display: table-cell;
      padding: 0.8rem 1rem;
    }

    /* add css 2020.02.03 */
    .non-padding {
      padding: 0px !important;
    }
    .info-column {
      max-width: 350px;
      margin: 10px 15px 0 0;
    }
    .verify-product-info-link {
      font-weight: 600;
    }
    .info {
      margin-bottom: 1rem;
      color: #535353;
      font-size: 15px;
    }
    .inventoryShipment-info-content-wrapper {
      display: flex;
      justify-content: left;
    }
    .inventoryShipment-info-content {
      min-width: 182px;
      color: #141414;
      font-size: 15px;
    }
    .card-title-inline {
      display: flex;
      flex-direction: row;
    }
    table thead tr th {
      color: #535353;
      border-bottom: 2px solid #dee2e6;
    }
    .sum-list-title {
      color: #535353;
    }
    .sum-list-contnet {
      color: #141414;
    }
    .bg-white {
      border: none !important;
    }
    /* reset borders */
    /* table tbody+tbody {
      border: none !important;
    }
    table td {
      border: none !important;
    }
    thead .border-top {
      border-top: 1px solid #dee2e6;
    }
    thead .border-bottom {
      border-bottom: 1px solid #dee2e6;
    }
    tbody .border-bottom {
      border-bottom: 1px solid #dee2e6;
    } */
    .mixedContentBoxItemSpace {
      padding: 2px 0 !important;
      /* background-color: rgba(0,0,0, 0.07) !important; */
    }
  </style>
</head>

<div class="max-width" style="max-width:1600px !important;padding-left:20px;padding-right:20px;"
  ng-app="inventoryShipmentView" ng-controller="inventoryShipmentViewCtrl">
  <!-- <div class="text-center text-success">
    <h1>${message}</h1>
  </div> -->
  <div class="container-fluid">
    <!-- Info card start -->
    <div class="card pt-3 pb-4">
      <div class="card-body">
        <!-- Row 1 start -->
        <div class="row">
          <div class="col-md-12">
            <div class="page-heading">
              <div class="card-title title-space">
                <!-- <h6>INFORMATION</h6> -->
                <a style="font-size:13pt;" href="<c:url value="/resources/files/HelpIVS.pdf"/>" target="_blank">
                  <i class="fas fa-question-circle"></i>
                  <spring:message code="inventoryShipment.help" />
                </a>
              </div>
              <!-- <a style="font-size:13pt;" href="<c:url value="/resources/files/HelpIVS.pdf"/>" target="_blank">
                <i class="fas fa-question-circle"></i>
                <spring:message code="inventoryShipment.help" />
              </a> -->
            </div>
          </div>
        </div>
        <!-- Row 1 end -->

        <hr class="my-3">

        <!-- Info row start -->
        <div class="row info-row">
          <div class="col-12 col-xl-4 d-flex flex-column info-column">
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.inventoryShipmentId' />
              </div>
              <div class="inventoryShipment-info-content-wrapper">
                <div class="inventoryShipment-info-content">${InventoryShipment.name}</div>
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.FCADeliveryLocation' />
              </div>
              <div class="inventoryShipment-info-content-wrapper">
                <div class="inventoryShipment-info-content">
                  ${FCADeliveryLocationIdToLocationMap[InventoryShipment.fcaDeliveryLocationId]}
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.destination' />
              </div>
              <div class="inventoryShipment-info-content-wrapper">
                <div class="inventoryShipment-info-content">
                  <spring:message code="${InventoryShipment.destinationCountry}" />
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.shippingMethod' />
              </div>
              <div class="inventoryShipment-info-content-wrapper">
                <div class="inventoryShipment-info-content">
                  <spring:message code="${InventoryShipment.shippingMethod}" />
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.expectedExportDate' />
              </div>
              <div class="inventoryShipment-info-content-wrapper">
                <div class="inventoryShipment-info-content">
                  ${InventoryShipment.expectedExportDate}
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.FCADeliveryDate' />
              </div>
              <div class="inventoryShipment-info-content-wrapper">
                <div class="inventoryShipment-info-content">
                  ${InventoryShipment.fcaDeliveryDate}
                </div>
              </div>
            </div>
          </div>
          <div class="col-12 col-xl-4 d-flex flex-column info-column">
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.specialRequest' />
                <label data-toggle="tooltip" data-placement="top" title="<spring:message code='inventoryShipment.special_request_hint' />">
                <span class="glyphicon glyphicon-info-sign" style="color:#428bca;"></span>
                </label>
              </div>
              <div style="max-width: 200px;">
                ${InventoryShipment.specialRequest}
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.boxesNeedRepackaging' />
              </div>
              <div class="inventoryShipment-info-content">
                ${InventoryShipment.boxesNeedRepackaging}
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.repackagingFee' />
              </div>
              <div class="inventoryShipment-info-content">
                ${InventoryShipment.repackagingFee}
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.requiredPO' />
              </div>
              <div class="inventoryShipment-info-content">
                <spring:message code='inventoryShipment.${InventoryShipment.requiredPO}' />
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.PONumber' />
              </div>
              <div class="inventoryShipment-info-content">
                ${InventoryShipment.PONumber}
              </div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.status' />
              </div>
              <div class="inventoryShipment-info-content">
                <spring:message code="${InventoryShipment.status}" />
              </div>
            </div>
          </div>
          <div class="col-12 col-xl-4 d-flex flex-column info-column">
            <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
              <div class="d-flex flex-column">
                <div class="d-flex justify-content-between info">
                  <div>
                    <spring:message code='inventoryShipment.seller' />
                  </div>
                  <div class="inventoryShipment-info-content">
                    ${InventoryShipment.sellerCompanyKcode}
                    ${AllCompanyKcodeToNameMap[InventoryShipment.sellerCompanyKcode]}
                  </div>
                </div>
                <div class="d-flex justify-content-between info">
                  <div>
                    <spring:message code='inventoryShipment.buyer' />
                  </div>
                  <div class="inventoryShipment-info-content">
                    ${InventoryShipment.buyerCompanyKcode}
                    ${AllCompanyKcodeToNameMap[InventoryShipment.buyerCompanyKcode]}
                  </div>
                </div>
              </div>
            </sec:authorize>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.dateCreated' />
              </div>
              <div class="inventoryShipment-info-content">${InventoryShipment.dateCreated}</div>
            </div>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.invoiceNumber' />
              </div>
              <div class="inventoryShipment-info-content">${InventoryShipment.invoiceNumber}</div>
            </div>
            <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
              <c:choose>
                <c:when test="${InventoryShipment.status eq 'SHPT_CONFIRMED'}">
                  <div class="d-flex info" style="color: #2A6496;">
                    <spring:message code='inventoryShipment.invoiceNumber_hint' />
                  </div>
                </c:when>
              </c:choose>
            </sec:authorize>
            <div class="d-flex justify-content-between info">
              <div>
                <spring:message code='inventoryShipment.datePurchased' />
              </div>
              <div class="inventoryShipment-info-content">${InventoryShipment.datePurchased}</div>
            </div>
            <sec:authorize access="hasAnyRole('${auth['ADMIN_ONLY']}')">
              <c:choose>
                <c:when
                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                  <div class="d-flex justify-content-between info">
                    <div>
                      <spring:message code='inventoryShipment.shippingCost' /></b>
                    </div>
                    <div>${shippingCost}</div>
                  </div>
                </c:when>
              </c:choose>
            </sec:authorize>
          </div>
        </div>

        <hr>

        <!-- Internal Note -->
        <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
          <div class="row">
            <div class="col-12 d-flex flex-column info mb-0">
              <div style="margin-bottom: 1.5rem;">
                <spring:message code='inventoryShipment.internalNote' />
              </div>
              <div>${InventoryShipment.internalNote}</div>
            </div>
          </div>
        </sec:authorize>
      </div>
    </div>
    <!-- Info card end -->

    <hr class="my-4">

    <!-- Table card start -->
    <div class="card">
      <div class="card-body">
        <!-- Non-mixed products -->
        <div class="row">
          <div class="col-md-12">
            <!-- Row 3 table -->
            <table class="table table-striped m-0">
              <!-- Row 3 table heading -->
              <thead class="border-top border-bottom">
                <tr>
                  <th style="width: 8%;">
                    <spring:message code='inventoryShipment.sku' />
                  </th>
                  <th style="width: 12%;">
                    <spring:message code='inventoryShipment.skuName' />
                  </th>
                  <th style="width: 4%;">
                    <spring:message code='inventoryShipment.requireRepackaging' />
                  </th>
                  <th class="text-center" style="width: 5%;">
                    <spring:message code='inventoryShipment.cartonNumber' />
                  </th>
                  <th class="text-center" style="width: 10%;">
                    <spring:message code='inventoryShipment.cartonDimensions' />
                  </th>
                  <th class="text-right" style="width: 5%;">
                    <spring:message code='inventoryShipment.grossWeightPerCarton' />
                  </th>
                  <th class="text-right" style="width: 3%;">
                    <spring:message code='inventoryShipment.unitsPerCarton' />
                  </th>
                  <th class="text-right" style="width: 3%;">
                    <spring:message code='inventoryShipment.numberOfCartons' />
                  </th>
                  <th class="text-right" style="width: 3%;">
                    <spring:message code='inventoryShipment.quantity' />
                  </th>
                  <th class="text-right" style="width: 5%;">
                    <spring:message code='inventoryShipment.uniPrice' />
                  </th>
                  <th class="text-center" style="width: 6%;">
                    <spring:message code='inventoryShipment.amount' />
                  </th>
                  <th class="text-right" style="width: 4%;">
                    <spring:message code='inventoryShipment.GUIInvoiceNumber' />
                  </th>
                  <th class="text-right" style="width: 3%;">
                    <spring:message code='inventoryShipment.GUIInvoice' />
                  </th>
                  <th class="text-center" style="width: 5%;">
                    <span>產品狀態</span>
                  </th>
                  <th class="text-center" style="width: 5%">
                    <span>確認產品</span>
                  </th>
                </tr>
              </thead>
              <!-- Row 3 table body -->
              <tbody class="border-bottom">
                <tr ng-repeat="lineItem in lineItems">
                  <td>{{lineItem.skuCode}}</td>
                  <td>{{lineItem.nameBySupplier}}</td>
                  <td ng-if="lineItem.requireRepackaging == true" class="text-center">
                    <spring:message code='inventoryShipment.true' />
                  </td>
                  <td ng-if="lineItem.requireRepackaging == false" class="text-center">
                    <spring:message code='inventoryShipment.false' />
                  </td>
                  <td class="text-center">{{lineItem.cartonNumberStart}} ~ {{lineItem.cartonNumberEnd}}</td>
                  <td class="text-center">{{lineItem.cartonDimensionCm1}} x {{lineItem.cartonDimensionCm2}} x
                    {{lineItem.cartonDimensionCm3}}</td>
                  <td class="text-right">{{lineItem.perCartonGrossWeightKg}}</td>
                  <td class="text-right">{{lineItem.perCartonUnits}}</td>
                  <td class="text-right">{{lineItem.cartonCounts}}</td>
                  <td class="text-right">{{lineItem.quantity}}</td>
                  <td class="text-right">{{lineItem.unitAmount}}</td>
                  <td class="text-center">{{lineItem.amountUntaxed}}</td>
                  <td class="text-right">{{lineItem.guiinvoiceNumber}}</td>
                  <td class="text-right">
                    <a href="${pageContext.request.contextPath}/InventoryShipments/downloadGUIInvoiceFile?shipmentName=${shipmentId}&GUIInvoiceFileName={{lineItem.guifileName}}">
                      {{lineItem.guifileName}}</a>
                  </td>
                  <td class="text-center">
                    <span class="verify-status">New</span>
                  </td>
                  <td class="text-center">
                    <span><a class="verify-product-info-link" target="_blank" href="${pageContext.request.contextPath}/shipment/ivp">Verify</a></span>
                  </td>
                </tr>
              </tbody>

              <tbody>
                <!-- Mixed products -->
                <tr ng-show="mixedContentBoxes.length > 0" class="bg-white">
                  <td class="text-info border-top-0" colspan="15">
                    <p class="mt-5" style="font-size: 17px;">---
                      <spring:message code='inventoryShipment.mixedContentBox' />---</p>
                  </td>
                </tr>

                <!-- Mixed content box looping -->
                <tr ng-repeat="mixedContentBox in mixedContentBoxes">
                  <td colspan="15" class="p-0 non-padding">
                    <table class="table table-striped ivs-tb m-0">
                      <!-- Mixed content box looping heading -->
                      <thead class="border-top border-bottom">
                        <td class="non-padding my-5 bg-white" colspan="15" ></td>
                        <tr class="bg-white">
                          <th style="width: 8%;">
                            <spring:message code='inventoryShipment.sku' />
                          </th>
                          <th style="width: 12%;">
                            <spring:message code='inventoryShipment.skuName' />
                          </th>
                          <th style="width: 4%;">
                            <spring:message code='inventoryShipment.requireRepackaging' />
                          </th>
                          <th class="text-center" style="width: 5%;">
                            <spring:message code='inventoryShipment.cartonNumber' />
                          </th>
                          <th class="text-center" style="width: 10%;">
                            <spring:message code='inventoryShipment.cartonDimensions' />
                          </th>
                          <th class="text-right" style="width: 5%;">
                            <spring:message code='inventoryShipment.grossWeightPerCarton' />
                          </th>
                          <th class="text-right" style="width: 3%;">
                            <spring:message code='inventoryShipment.unitsPerCarton' />
                          </th>
                          <th class="text-right" style="width: 3%;">
                            <spring:message code='inventoryShipment.numberOfCartons' />
                          </th>
                          <th class="text-right" style="width: 3%;">
                            <spring:message code='inventoryShipment.quantity' />
                          </th>
                          <th class="text-right" style="width: 5%;">
                            <spring:message code='inventoryShipment.uniPrice' />
                          </th>
                          <th class="text-center" style="width: 6%;">
                            <spring:message code='inventoryShipment.amount' />
                          </th>
                          <th class="text-right" style="width: 4%;">
                            <spring:message code='inventoryShipment.GUIInvoiceNumber' />
                          </th>
                          <th class="text-right" style="width: 3%;">
                            <spring:message code='inventoryShipment.GUIInvoice' />
                          </th>
                          <th class="text-center" style="width: 5%;">
                            <span>產品狀態</span>
                          </th>
                          <th class="text-center" style="width: 5%">
                            <span>確認產品</span>
                          </th>
                        </tr>
                      </thead>
                      <!-- Mixed content box looping body -->
                      <tbody class="border-bottom">
                        <tr ng-repeat="mixedContentBoxItem in mixedContentBox">
                          <td>{{mixedContentBoxItem.skuCode}}</td>
                          <td>{{mixedContentBoxItem.nameBySupplier}}</td>
                          <td class="text-center" ng-if="mixedContentBoxItem.requireRepackaging == true">
                            <spring:message code='inventoryShipment.true' />
                          </td>
                          <td class="text-center" ng-if="mixedContentBoxItem.requireRepackaging == false">
                            <spring:message code='inventoryShipment.false' />
                          </td>
                          <td class="text-center">{{mixedContentBoxItem.cartonNumberStart}} ~
                            {{mixedContentBoxItem.cartonNumberEnd}}</td>
                          <td class="text-center">{{mixedContentBoxItem.cartonDimensionCm1}} x
                            {{mixedContentBoxItem.cartonDimensionCm2}} x {{mixedContentBoxItem.cartonDimensionCm3}}</td>
                          <td class="text-right">{{mixedContentBoxItem.perCartonGrossWeightKg}}</td>
                          <td class="text-right">{{mixedContentBoxItem.perCartonUnits}}</td>
                          <td class="text-right">{{mixedContentBoxItem.cartonCounts}}</td>
                          <td class="text-right">{{mixedContentBoxItem.quantity}}</td>
                          <td class="text-right">{{mixedContentBoxItem.unitAmount}}</td>
                          <td class="text-center">{{mixedContentBoxItem.amountUntaxed}}</td>
                          <td class="text-right">{{mixedContentBoxItem.guiinvoiceNumber}}</td>
                          <td class="text-right">
                            <a
                              href="${pageContext.request.contextPath}/InventoryShipments/downloadGUIInvoiceFile?shipmentName=${shipmentId}&GUIInvoiceFileName={{mixedContentBoxItem.guifileName}}">
                              {{mixedContentBoxItem.guifileName}}</a>
                          </td>
                          <td class="text-center">
                            <span class="verify-status">New</span>
                          </td>
                          <td class="text-center">
                            <span><a class="verify-product-info-link" target="_blank" href="${pageContext.request.contextPath}/shipment/ivp">Verify</a></span>
                          </td>
                        </tr>
                      </tbody>
                        <tr>
                          <td colspan="15" class="border-0 my-5 bg-white mixedContentBoxItemSpace"></td>
                        </tr>
                    </table>
                  </td>
                </tr>

                <tr class="border-0">
                  <td colspan="15" class="border-0 my-5 bg-white"></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <h6 class="mb-4" style="padding-top: 25px">Summary</h6>
        <hr class="mb-4">
        <div class="sum-list">
          <ul>
            <li class="sum-list-title"><spring:message code='inventoryShipment.numberOfCartons' /></li>
            <li class="sum-list-title"><spring:message code='inventoryShipment.quantity' /></li>
            <li class="sum-list-title"><spring:message code='inventoryShipment.subtotal' /></li>
            <li class="sum-list-title"><spring:message code='inventoryShipment.taxAmount' />(${InventoryShipment.salesTaxPercentage}%)</liclass="sum-list-title">
            <li class="sum-list-title"><spring:message code='inventoryShipment.total' /></li>
            <li class="sum-list-title"><spring:message code='inventoryShipment.paid' /></li>
          </ul>
          <ul>
            <li id="totalCartonCounts" class="sum-list-content"></li>
            <li id="totalQuantity" class="sum-list-content"></li>
            <li class="sum-list-content">${InventoryShipment.subtotal}</li>
            <li class="sum-list-content">${InventoryShipment.salesTax}</li>
            <li class="sum-list-content">${InventoryShipment.total}</li>
            <li class="sum-list-content">
              <a target="_blank"
              href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/InventoryPayment">${InventoryShipment.paidTotal}</a>
            </li>
          </ul>
        </div>

        <div id="dialog-confirm"></div>

        <hr class="mt-5 mb-4">

        <!-- Button selection start -->
        <c:choose>
          <c:when test="${InventoryShipment.status eq 'SHPT_DRAFT'}">
            <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
              <div style="float: right">
                <input class="btn btn-success" type="button" value="<spring:message code="
                  inventoryShipment.requestPickup" />" onclick="submitConfirm('${InventoryShipment.name}');" />
                <a class="btn btn-primary"
                  href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/editDraft">
                  <spring:message code="inventoryShipment.editInventoryShipment" />
                </a>
                <input class="btn btn-link" type="button" value="<spring:message code="
                  inventoryShipment.deleteInventoryShipment" />" onclick="deleteConfirm('${shipmentId}');" />
                <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                  <spring:message code="inventoryShipment.backToInventoryShipment" />
                </a>
              </div>
            </sec:authorize>
            <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
              <div style="float: right">
                <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                  <spring:message code="inventoryShipment.backToInventoryShipment" />
                </a>
              </div>
            </sec:authorize>
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${InventoryShipment.status eq 'SHPT_AWAIT_PLAN'}">
                <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                  <div style="float: right">
                    <a class="btn btn-primary"
                      href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
                      <spring:message code="inventoryShipment.editInventoryShipment" />
                    </a>
                    <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                      <spring:message code="inventoryShipment.backToInventoryShipment" />
                    </a>
                  </div>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                  <div style="float: right">
                    <!--<input class="btn btn-success" type="button" value="<spring:message code=" inventoryShipment.accept" />" onclick="acceptConfirm('${InventoryShipment.name}');" />-->
                    <a class="btn btn-primary"
                      href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
                      <spring:message code="inventoryShipment.editInventoryShipment" />
                    </a>
                    <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                      <spring:message code="inventoryShipment.backToInventoryShipment" />
                    </a>
                  </div>
                </sec:authorize>
              </c:when>
              <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                  <div style="float: right">
                    <a class="btn btn-primary"
                      href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
                      <spring:message code="inventoryShipment.editInventoryShipment" />
                    </a>
                    <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                      <spring:message code="inventoryShipment.backToInventoryShipment" />
                    </a>
                  </div>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                  <div style="float: right">
                    <input class="btn btn-success" type="button" value="<spring:message code="
                      inventoryShipment.confirmInventoryShipment" />" onclick="confirmConfirm('${InventoryShipment.name}');"
                    />
                    <a class="btn btn-primary"
                      href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
                      <spring:message code="inventoryShipment.editInventoryShipment" />
                    </a>
                    <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                      <spring:message code="inventoryShipment.backToInventoryShipment" />
                    </a>
                  </div>
                </sec:authorize>
              </c:when>
              <c:otherwise>
                <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                  <div style="float: right">
                    <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                      <spring:message code="inventoryShipment.backToInventoryShipment" />
                    </a>
                  </div>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                  <div style="float: right">
                    <a class="btn btn-primary"
                      href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/edit">
                      <spring:message code="inventoryShipment.editInventoryShipment" />
                    </a>
                    <a class="btn btn-link" href="${pageContext.request.contextPath}/InventoryShipments">
                      <spring:message code="inventoryShipment.backToInventoryShipment" />
                    </a>
                  </div>
                </sec:authorize>
              </c:otherwise>
            </c:choose>
          </c:otherwise>
        </c:choose>
        <!-- Button selection end -->

        <!-- Warning message at bottom left -->
        <div class="row">
          <div class="col-md-12 text-danger">
            <spring:message code='inventoryShipment.note' />
          </div>
        </div>
      </div>
    </div>
    <!-- Table card end -->
  </div>
</div>
