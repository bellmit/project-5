<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>
  <title>
    <c:choose>
      <c:when test="${type ne 'Create'}">
        <c:choose>
          <c:when
            test="${InventoryShipment.status eq 'SHPT_DRAFT' || InventoryShipment.status eq 'SHPT_AWAIT_PLAN' || InventoryShipment.status eq 'SHPT_PLANNING'}">
            <spring:message code='inventoryShipment.inventoryShipment' />: ${InventoryShipment.name} - DRS
          </c:when>
          <c:otherwise>
            <spring:message code='inventoryShipment.inventoryPurchaseOrder' />: ${InventoryShipment.name} - DRS
          </c:otherwise>
        </c:choose>
      </c:when>
      <c:otherwise>
        <spring:message code='inventoryShipment.createInventoryShipment' /> - DRS
      </c:otherwise>
    </c:choose>
  </title>
  <link href="<c:url value="/resources/css/newstyle.css"/>" type="text/css" rel="stylesheet">
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
  <script>

    $(document).ready(function () {
      $("body").tooltip({ selector: '[data-toggle=tooltip]' });
    });

    function onSubmit() {
      switch (document.pressed) {
        case 'saveDraft':
          document.InventoryShipment.action = "${pageContext.request.contextPath}/InventoryShipments/saveDraft";
          break;
        case 'updateDraft':
          document.InventoryShipment.action = "${pageContext.request.contextPath}/InventoryShipments/updateDraft";
          break;
        case 'update':
          document.InventoryShipment.action = "${pageContext.request.contextPath}/InventoryShipments/update";
          break;
        default:
      }
      return true;
    }

    var app = angular.module('inventoryShipment', []);
    app.controller('inventoryShipmentCtrl', function ($scope) {

      $('[data-toggle="tooltip"]').tooltip();

      $('#expectedExportDate,#fcaDeliveryDate,#boxesNeedRepackaging').attr('readonly', true);

      $scope.mixedContentBoxCartonCountFirst = 1;
      $scope.mixedContentBoxCartonCountOther = 0;
      $scope.isDrsUser = ${ isDrsUser };
      $scope.isSupplier = ${ isSupplier };

      $scope.addMixedContentBox = function () {
        $scope.mixedContentBoxes.push([{ boxNum: 0, mixedBoxLineSeq: 1, requireRepackaging: false, cartonDimensionCm1: null, cartonDimensionCm2: null, cartonDimensionCm3: null, cartonCounts: null, quantity: 0, amountUntaxed: 0, cartonNumberStart: 0, cartonNumberEnd: 0, isGUIInvoiceIsRequired: false, guifileName: null }]);
        $scope.assignMixedContentBoxNumber();
        $scope.calculateTotalCartonCount();
        $scope.isAtLeastOneSKU();
      };

      $scope.removeMixedContentBox = function (item) {

        var idx = $scope.mixedContentBoxes.indexOf(item);
        $scope.mixedContentBoxes.splice(idx, 1);
        $scope.assignMixedContentBoxNumber();
        $scope.calculateTotalCartonCount();
        $scope.calculateTotalQuantity();
        $scope.subtractAmount();
        $scope.calculateRepackgingFee();
        $scope.isAtLeastOneSKU();

      };

      $scope.addLineItem = function () {
        $scope.lineItems.push({ boxNum: $scope.lineItems.length + 1, mixedBoxLineSeq: 0, requireRepackaging: false, cartonCounts: null, quantity: 0, amountUntaxed: 0, cartonNumberStart: 0, cartonNumberEnd: 0, isGUIInvoiceIsRequired: false, isGUIInvoiceEnable: false, guifileName: null });
        $scope.assignMixedContentBoxNumber();
        $scope.isAtLeastOneSKU();
      };

      $scope.addMixedContentBoxLineItem = function (mixedContentBox) {

        $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].push({ boxNum: 0, mixedBoxLineSeq: mixedContentBox.length + 1, requireRepackaging: false, cartonDimensionCm1: null, cartonDimensionCm2: null, cartonDimensionCm3: null, cartonCounts: null, quantity: 0, amountUntaxed: 0, cartonNumberStart: 0, cartonNumberEnd: 0, isGUIInvoiceIsRequired: false, isGUIInvoiceEnable: false, guifileName: null });
        $scope.assignMixedContentBoxNumber();
        $scope.assignRequireRepackaging(mixedContentBox);
        $scope.assignCartonNumber(mixedContentBox);
        $scope.assignCartonDimension(mixedContentBox);
        $scope.assignPerCartonGrossWeightKg(mixedContentBox);
        $scope.isAtLeastOneSKU();
      };

      $scope.removeLineItem = function (item) {

        var idx = $scope.lineItems.indexOf(item);
        $scope.lineItems.splice(idx, 1);
        $scope.assignUnMixedContentBoxNumber();
        $scope.assignMixedContentBoxNumber();
        $scope.calculateTotalCartonCount();
        $scope.calculateTotalQuantity();
        $scope.subtractAmount();
        $scope.calculateRepackgingFee();
        $scope.isAtLeastOneSKU();
        $scope.verifySKUDuplicateForUnMixedBox();
        $scope.verifyFCAprice();

      };

      $scope.removeMixedContentBoxLineItem = function (mixedContentBox, item) {

        var idx = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].indexOf(item);
        $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].splice(idx, 1);
        $scope.assignMixedContentBoxLineSeq(mixedContentBox, item);
        $scope.calculateTotalCartonCount();
        $scope.calculateTotalQuantity();
        $scope.subtractAmount();
        $scope.calculateRepackgingFee();
        $scope.isAtLeastOneSKU();
        $scope.verifySKUDuplicateForMixedBox(mixedContentBox);
        $scope.verifyFCAprice();

      };

      $scope.calculateRepackgingFee = function () {

        var totalUnmixedContentBoxCartonCounts = 0;
        for (var i = 0; i < $scope.lineItems.length; i++) {
          if ($scope.lineItems[i].requireRepackaging == true) totalUnmixedContentBoxCartonCounts += Number($scope.lineItems[i].cartonCounts);
        }

        var totalMixedContentBoxCartonCounts = 0;
        for (var i = 0; i < $scope.mixedContentBoxes.length; i++) {
          if ($scope.mixedContentBoxes[i].length > 0) {
            if ($scope.mixedContentBoxes[i][0].requireRepackaging == true) totalMixedContentBoxCartonCounts += Number($scope.mixedContentBoxes[i][0].cartonCounts);
          }
        }

        $scope.boxesNeedRepackaging = totalUnmixedContentBoxCartonCounts + totalMixedContentBoxCartonCounts;
        $scope.repackagingFee = $scope.boxesNeedRepackaging * 100;

      };

      $scope.removePONumber = function () {

        if ($scope.requiredPO == false) $scope.PONumber = null;

      };

      $scope.assignUnMixedContentBoxNumber = function () {

        for (var i = 0; i < $scope.lineItems.length; i++) {
          $scope.lineItems[i].boxNum = i + 1;
        }

      };

      $scope.assignMixedContentBoxNumber = function () {

        var boxNumStart = $scope.lineItems.length;

        for (var i = 0; i < $scope.mixedContentBoxes.length; i++) {
          for (var j = 0; j < $scope.mixedContentBoxes[i].length; j++) {
            $scope.mixedContentBoxes[i][j].boxNum = boxNumStart + 1;
          }
          boxNumStart++;
        }

      };

      $scope.assignMixedContentBoxLineSeq = function (mixedContentBox, item) {

        var mixedContentBoxLineItems = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)];
        for (var i = 0; i < mixedContentBoxLineItems.length; i++) {
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].mixedBoxLineSeq = i + 1;
        }

      };

      $scope.assignRequireRepackaging = function (mixedContentBox) {

        for (var i = 0; i < $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].length; i++) {
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].requireRepackaging = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][0].requireRepackaging;
        }
        $scope.calculateRepackgingFee();
      };


      $scope.assignCartonNumber = function (mixedContentBox) {

        for (var i = 0; i < $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].length; i++) {
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].cartonNumberStart = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][0].cartonNumberStart;
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].cartonNumberEnd = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][0].cartonNumberEnd;
        }

      };

      $scope.assignCartonDimension = function (mixedContentBox) {

        for (var i = 0; i < $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].length; i++) {
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].cartonDimensionCm1 = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][0].cartonDimensionCm1;
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].cartonDimensionCm2 = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][0].cartonDimensionCm2;
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].cartonDimensionCm3 = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][0].cartonDimensionCm3;
        }

      };

      $scope.assignPerCartonGrossWeightKg = function (mixedContentBox) {

        for (var i = 0; i < $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].length; i++) {
          $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].perCartonGrossWeightKg = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][0].perCartonGrossWeightKg;
        }

      };

      $scope.calculateQuantity = function (scope) {
        var quantity = Number(scope.item.perCartonUnits) * Number(scope.item.cartonCounts)
        scope.item.quantity = isNaN(quantity) ? 0 : quantity;
        scope.calculateAmount(scope);
        scope.calculateTotalCartonCount();
        scope.calculateTotalQuantity();
        scope.calculateSubtotal();
        scope.calculateTaxAmount();
        scope.calculateTotalAmount();
        scope.calculateRepackgingFee();
      };

      $scope.calculateMixedContentBoxQuantity = function (scope) {
        var quantity = Number(scope.mixedContentBoxItem.perCartonUnits) * 1;
        scope.mixedContentBoxItem.quantity = isNaN(quantity) ? 0 : quantity;
        scope.calculateMixedContentBoxAmount(scope)
        scope.calculateTotalCartonCount();
        scope.calculateTotalQuantity();
      };

      $scope.calculateAmount = function (scope) {
        var amount = Number(scope.item.quantity) * Number(scope.item.unitAmount);
        scope.item.amountUntaxed = isNaN(amount) ? 0 : Math.round(amount * Math.pow(10, 6)) / Math.pow(10, 6);
        scope.calculateSubtotal();
        scope.calculateTaxAmount();
        scope.calculateTotalAmount();
        $scope.verifyFCAprice();
      };

      $scope.calculateMixedContentBoxAmount = function (scope) {
        var amount = Number(scope.mixedContentBoxItem.quantity) * Number(scope.mixedContentBoxItem.unitAmount);
        scope.mixedContentBoxItem.amountUntaxed = isNaN(amount) ? 0 : Math.round(amount * Math.pow(10, 6)) / Math.pow(10, 6);
        scope.calculateSubtotal();
        scope.calculateTaxAmount();
        scope.calculateTotalAmount();
        $scope.verifyFCAprice();
      };

      $scope.calculateSubtotal = function () {

        var unmixedContentBoxSubtotal = 0;
        for (var i = 0; i < $scope.lineItems.length; i++) {
          unmixedContentBoxSubtotal += Number($scope.lineItems[i].amountUntaxed);
        }

        var mixedContentBoxSubtotal = 0;
        for (var i = 0; i < $scope.mixedContentBoxes.length; i++) {
          for (var j = 0; j < $scope.mixedContentBoxes[i].length; j++) {
            mixedContentBoxSubtotal += Number($scope.mixedContentBoxes[i][j].amountUntaxed);
          }
        }

        var subtotal = unmixedContentBoxSubtotal + mixedContentBoxSubtotal;
        $scope.subtotal = Math.round(subtotal);

      };

      $scope.calculateTaxAmount = function () {
        var amountTax = (Number($scope.subtotal) * Number($scope.salesTaxPercentage)) / 100;
        $scope.taxAmount = isNaN(amountTax) ? 0 : Math.round(amountTax);
      };

      $scope.calculateTotalCartonCount = function () {

        var totalUnmixedContentBoxCartonCounts = 0;
        for (var i = 0; i < $scope.lineItems.length; i++) {
          totalUnmixedContentBoxCartonCounts += Number($scope.lineItems[i].cartonCounts);
        }

        var totalMixedContentBoxCartonCounts = 0;
        for (var i = 0; i < $scope.mixedContentBoxes.length; i++) {
          if ($scope.mixedContentBoxes[i].length > 0) {
            totalMixedContentBoxCartonCounts += 1;
          }
        }

        $scope.totalCartonCount = totalUnmixedContentBoxCartonCounts + totalMixedContentBoxCartonCounts;

      };

      $scope.calculateTotalQuantity = function () {

        var totalUnmixedContentBoxQuantity = 0;
        for (var i = 0; i < $scope.lineItems.length; i++) {
          var unmixedContentBoxQuantity = Number($scope.lineItems[i].perCartonUnits) * Number($scope.lineItems[i].cartonCounts);
          unmixedContentBoxQuantity = isNaN(unmixedContentBoxQuantity) ? 0 : Number($scope.lineItems[i].perCartonUnits) * Number($scope.lineItems[i].cartonCounts);
          totalUnmixedContentBoxQuantity += unmixedContentBoxQuantity;
        }

        var totalMixedContentBoxQuantity = 0;
        for (var i = 0; i < $scope.mixedContentBoxes.length; i++) {
          for (var j = 0; j < $scope.mixedContentBoxes[i].length; j++) {
            var mixedContentBoxQuantity = Number($scope.mixedContentBoxes[i][j].perCartonUnits) * 1;
            mixedContentBoxQuantity = isNaN(mixedContentBoxQuantity) ? 0 : Number($scope.mixedContentBoxes[i][j].perCartonUnits) * 1;
            totalMixedContentBoxQuantity += mixedContentBoxQuantity;
          }
        }

        $scope.totalQuantity = totalUnmixedContentBoxQuantity + totalMixedContentBoxQuantity;

      };

      $scope.calculateTotalAmount = function () {
        var totalAmount = Number($scope.subtotal) + Number($scope.taxAmount);
        $scope.totalAmount = Math.round(totalAmount);
      };

      $scope.changeSalesTaxPercentage = function () {
        $scope.calculateTaxAmount();
        $scope.calculateTotalAmount();
      };

      $scope.subtractAmount = function () {
        $scope.calculateSubtotal();
        $scope.calculateTaxAmount();
        $scope.calculateTotalAmount();
      };

      $scope.verifySKUForUnMixedBox = function (item) {
        $scope.verifySKUDuplicateForUnMixedBox();
        $scope.verifyFCAprice();
        $scope.isGUIInvoiceIsRequiredForUnMixedBox(item);
      };

      $scope.verifySKUForMixedBox = function (item, mixedContentBox) {
        $scope.verifySKUDuplicateForMixedBox(mixedContentBox);
        $scope.verifyFCAprice();
        $scope.isGUIInvoiceIsRequiredForMixedBox(item);
      };

      $scope.isAtLeastOneSKU = function () {

        var skuCount = 0;
        skuCount = $scope.lineItems.length
        for (var i = 0; i < $scope.mixedContentBoxes.length; i++) {
          skuCount += $scope.mixedContentBoxes[i].length;
        }

        skuCount == 1 ? $scope.atLeastOneSKUWarning = false : $scope.atLeastOneSKUWarning = true;
        skuCount == 0 ? $scope.noSKU = false : $scope.noSKU = true;

      };

      $scope.verifySKUDuplicateForUnMixedBox = function () {

        var sorted, i, j;
        var currentSKUs = [];
        sorted = $scope.lineItems.concat();
        for (i = 0; i < $scope.lineItems.length; i++) {
          currentSKUs.push($scope.lineItems[i].skuCode);
        }
        var duplicatedIndex = [];
        var noneDuplicatedIndex = [];
        for (i = 0; i < currentSKUs.length; i++) {
          for (j = 0; j < currentSKUs.length; j++) {
            if (i != j) {
              if (typeof currentSKUs[i] !== 'undefined' && typeof currentSKUs[j] !== 'undefined') {
                if (currentSKUs[i] == currentSKUs[j]) {
                  if (duplicatedIndex.indexOf(i) === -1) duplicatedIndex.push(i);
                } else {
                  if (noneDuplicatedIndex.indexOf(i) === -1) noneDuplicatedIndex.push(i);
                }
              }
            }
          }
        }
        var k, m, n;
        for (k = 0; k < noneDuplicatedIndex.length; k++) {
          sorted[noneDuplicatedIndex[k]].isDuplicate = false;
        }
        for (m = 0; m < duplicatedIndex.length; m++) {
          sorted[duplicatedIndex[m]].isDuplicate = true;
        }
        if (duplicatedIndex.length == 0) {
          for (n = 0; n < currentSKUs.length; n++) {
            sorted[n].isDuplicate = false;
          }
          $scope.duplicated = false;
        } else {
          $scope.duplicated = true;
        }

      };

      $scope.verifySKUDuplicateForMixedBox = function (mixedContentBox) {

        var sorted, i, j;
        var currentSKUs = [];
        sorted = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].concat();
        for (i = 0; i < $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].length; i++) {
          currentSKUs.push($scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][i].skuCode);
        }
        var duplicatedIndex = [];
        var noneDuplicatedIndex = [];
        for (i = 0; i < currentSKUs.length; i++) {
          for (j = 0; j < currentSKUs.length; j++) {
            if (i != j) {
              if (typeof currentSKUs[i] !== 'undefined' && typeof currentSKUs[j] !== 'undefined') {
                if (currentSKUs[i] == currentSKUs[j]) {
                  if (duplicatedIndex.indexOf(i) === -1) duplicatedIndex.push(i);
                } else {
                  if (noneDuplicatedIndex.indexOf(i) === -1) noneDuplicatedIndex.push(i);
                }
              }
            }
          }
        }
        var k, m, n;
        for (k = 0; k < noneDuplicatedIndex.length; k++) {
          sorted[noneDuplicatedIndex[k]].isDuplicate = false;
        }
        for (m = 0; m < duplicatedIndex.length; m++) {
          sorted[duplicatedIndex[m]].isDuplicate = true;
        }
        if (duplicatedIndex.length == 0) {
          for (n = 0; n < currentSKUs.length; n++) {
            sorted[n].isDuplicate = false;
          }
          $scope.duplicated = false;
        } else {
          $scope.duplicated = true;
        }

      };

      $scope.verifyFCAprice = function () {

        var SKUtoFCAprice = [];
        for (i = 0; i < $scope.lineItems.length; i++) {
          SKUtoFCAprice.push({ skuCode: $scope.lineItems[i].skuCode, FCAprice: Number($scope.lineItems[i].unitAmount) });
        }
        for (var j = 0; j < $scope.mixedContentBoxes.length; j++) {
          for (var k = 0; k < $scope.mixedContentBoxes[j].length; k++) {
            SKUtoFCAprice.push({ skuCode: $scope.mixedContentBoxes[j][k].skuCode, FCAprice: Number($scope.mixedContentBoxes[j][k].unitAmount) });
          }
        }

        for (var m = 0; m < SKUtoFCAprice.length; m++) {

          for (var n = 0; n < SKUtoFCAprice.length; n++) {

            if (m != n) {

              if (typeof SKUtoFCAprice[m].skuCode !== 'undefined' && typeof SKUtoFCAprice[n].skuCode !== 'undefined' && !isNaN(SKUtoFCAprice[m].FCAprice) && !isNaN(SKUtoFCAprice[n].FCAprice)) {

                if (SKUtoFCAprice[m].skuCode == SKUtoFCAprice[n].skuCode && SKUtoFCAprice[m].FCAprice != SKUtoFCAprice[n].FCAprice) {
                  $scope.FCAPriceNoneEqual = true;
                  break;
                } else if (SKUtoFCAprice[m].skuCode == SKUtoFCAprice[n].skuCode && SKUtoFCAprice[m].FCAprice == SKUtoFCAprice[n].FCAprice) {
                  $scope.FCAPriceNoneEqual = false;
                }

              }

            }


          }

        }

      };

      function emptyList(id) {
          document.getElementById(id).options.length = 0;
        /* if (document.getElementById(id) == true) {
        } */
      }

      function getShippingMethodList() {
        var destinationCountry = $("#destinationCountry").val();
        var ajaxUrl = '${pageContext.request.contextPath}/InventoryShipments/getShippingMethodList/';
        var shippingMethodList = null;
        $.ajax({
          type: 'get',
          url: ajaxUrl,
          contentType: "application/json; charset=utf-8",
          data: { destinationCountry: destinationCountry },
          dataType: "json",
          success: function (data) {
            emptyList("shippingMethod");
            var springMessage = new Array();
            springMessage["EXPRESS"] = '<spring:message code="EXPRESS" />';
            springMessage["AIR_CARGO"] = '<spring:message code="AIR_CARGO" />';
            springMessage["SEA_FREIGHT"] = '<spring:message code="SEA_FREIGHT" />';
            shippingMethodList = data;
            var selectShippingMethod = document.getElementById("shippingMethod");
            var optShippingMethod = document.createElement("option");
            optShippingMethod.value = "";
            optShippingMethod.textContent = "--- Select ---";
            selectShippingMethod.appendChild(optShippingMethod);
            for (var shippingMethod in shippingMethodList) {
              if (shippingMethodList.hasOwnProperty(shippingMethod)) {
                optShippingMethod = document.createElement("option");
                optShippingMethod.value = shippingMethodList[shippingMethod];
                optShippingMethod.textContent = springMessage[shippingMethodList[shippingMethod]];
                selectShippingMethod.appendChild(optShippingMethod);
              }
            }
            $("#shippingMethod").val($scope.shippingMethod);
          }
        });
      }

      function getFCADeliveryLocations() {
        var ajaxUrl = '${pageContext.request.contextPath}/InventoryShipments/getFcaDeliveryLocationIdToLocationMap/';
        var FCADeliveryLocations = null;
        $.ajax({
          type: 'get',
          url: ajaxUrl,
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function (data) {
            emptyList("fcaDeliveryLocation");
            FCADeliveryLocations = data;
            var selectFCADeliveryLocation = document.getElementById("fcaDeliveryLocation");
            var optFCADeliveryLocation = document.createElement("option");
            optFCADeliveryLocation.value = "";
            optFCADeliveryLocation.textContent = "--- Select ---";
            selectFCADeliveryLocation.appendChild(optFCADeliveryLocation);
            for (var location in FCADeliveryLocations) {
              optFCADeliveryLocation = document.createElement("option");
              optFCADeliveryLocation.value = location;
              optFCADeliveryLocation.textContent = FCADeliveryLocations[location];
              selectFCADeliveryLocation.appendChild(optFCADeliveryLocation);
            }
            $("#fcaDeliveryLocation").val($scope.fcaDeliveryLocationId);
          }
        });
      }

      function getDaysToPrepare() {
        var shippingMethod = $scope.shippingMethod;
        if (shippingMethod == null || shippingMethod == "") {
          return;
        }
        if ($scope.fcaDeliveryLocationId != 4) {
          $("#expectedExportDate").prop("disabled", false);
          $("#expectedExportDate").css({ "cursor": "default", "background-color": "white" });
          // $('#expectedExportDate').datepicker('destroy');
          var ajaxUrl = '${pageContext.request.contextPath}/InventoryShipments/getDaysToPrepare/';
          $.ajax({
            type: 'get',
            url: ajaxUrl,
            contentType: "application/json; charset=utf-8",
            data: { shippingMethod: shippingMethod },
            dataType: "json",
            success: function (data) {
              /* New datepicker */
              $(function() {
                $("#expectedExportDate").datetimepicker(
                  {
                    format: 'YYYY-MM-DD', // Originally'MM/DD/YYYY', changed to comply with DRS's date format
                    icons: {
                      time: "fa fa-clock-o",
                      date: "fa fa-calendar",
                      up: "fa fa-chevron-up",
                      down: "fa fa-chevron-down",
                      previous: 'fa fa-chevron-left',
                      next: 'fa fa-chevron-right',
                      today: 'fa fa-screenshot',
                      clear: 'fa fa-trash',
                      close: 'fa fa-remove'
                    }
                  }
                );
                console.log("Yes1.");
              });
              /* jQuery("#expectedExportDate").datepicker({
                beforeShow: function () {
                  setTimeout(function () {
                    $('.ui-datepicker').css('z-index', 200);
                  }, 0);
                },
                dateFormat: 'yy-mm-dd',
                minDate: data
              }); */
            }
          });
        } else {
          $("#expectedExportDate").prop("disabled", false);
          $("#expectedExportDate").css({ "cursor": "default", "background-color": "white" });
          /* New datepicker */
          $(function() {
            $("#expectedExportDate").datetimepicker(
              {
                format: 'YYYY-MM-DD', // Originally'MM/DD/YYYY', changed to comply with DRS's date format
                icons: {
                  time: "fa fa-clock-o",
                  date: "fa fa-calendar",
                  up: "fa fa-chevron-up",
                  down: "fa fa-chevron-down",
                  previous: 'fa fa-chevron-left',
                  next: 'fa fa-chevron-right',
                  today: 'fa fa-screenshot',
                  clear: 'fa fa-trash',
                  close: 'fa fa-remove'
                }
              }
            );
            console.log("Yes2.");
          });
          /* jQuery("#expectedExportDate").datepicker({
            beforeShow: function () {
              setTimeout(function () {
                $('.ui-datepicker').css('z-index', 200);
              }, 0);
            },
            dateFormat: 'yy-mm-dd'
          }); */

          $("#fcaDeliveryDate").prop( "disabled", false );
          $("#fcaDeliveryDate").css({"cursor": "default", "background-color": "white"});
          /* New datepicker */
          $(function() {
            $("#fcaDeliveryDate").datetimepicker(
              {
                format: 'YYYY-MM-DD', // Originally'MM/DD/YYYY', changed to comply with DRS's date format
                icons: {
                  time: "fa fa-clock-o",
                  date: "fa fa-calendar",
                  up: "fa fa-chevron-up",
                  down: "fa fa-chevron-down",
                  previous: 'fa fa-chevron-left',
                  next: 'fa fa-chevron-right',
                  today: 'fa fa-screenshot',
                  clear: 'fa fa-trash',
                  close: 'fa fa-remove'
                }
              }
            );
            console.log("Yes3.");
          });
          /* jQuery("#fcaDeliveryDate").datepicker({
            beforeShow: function() {
              setTimeout(function(){
                $('.ui-datepicker').css('z-index', 200);
                  }, 0);
              },
              dateFormat : 'yy-mm-dd'
            }); */
        }
      }

      $scope.getFCADeliveryLocations = function () {
        $scope.fcaDeliveryLocationId = "";
        $scope.expectedExportDate = "";
        var ajaxUrl = '${pageContext.request.contextPath}/InventoryShipments/getFcaDeliveryLocationIdToLocationMap/';
        var FCADeliveryLocations = null;
        $.ajax({
          type: 'get',
          url: ajaxUrl,
          dataType: "json",
          success: function (data) {
            emptyList("fcaDeliveryLocation");
            $("#fcaDeliveryDate").val("");
            FCADeliveryLocations = data;
            var selectFCADeliveryLocation = document.getElementById("fcaDeliveryLocation");
            var optFCADeliveryLocation = document.createElement("option");
            optFCADeliveryLocation.value = "";
            optFCADeliveryLocation.textContent = "--- Select ---";
            selectFCADeliveryLocation.appendChild(optFCADeliveryLocation);
            for (var location in FCADeliveryLocations) {
              optFCADeliveryLocation = document.createElement("option");
              optFCADeliveryLocation.value = location;
              optFCADeliveryLocation.textContent = FCADeliveryLocations[location];
              selectFCADeliveryLocation.appendChild(optFCADeliveryLocation);
            }
          }
        });
        if (shippingMethod != "" && destinationCountry != "") {
          $("#expectedExportDate").prop("disabled", true);
          $("#expectedExportDate").removeAttr("style");
          $("#expectedExportDate").css({ "display": "inline" });
          // $('#expectedExportDate').datepicker('destroy');
          $('#fcaDeliveryDate').attr('readonly', true);
          $("#fcaDeliveryDate").removeAttr("style");
          $("#fcaDeliveryDate").css({ "display": "inline" });
          // $('#fcaDeliveryDate').datepicker('destroy');
        } else {
          $("#expectedExportDate").prop("disabled", true);
          $("#expectedExportDate").removeAttr("style");
          $("#expectedExportDate").css({ "display": "inline" });
          // $('#expectedExportDate').datepicker('destroy');
          $('#expectedExportDate').val("");
          $("#fcaDeliveryDate").val("");
          $('#fcaDeliveryDate').attr('readonly', true);
          // $("#fcaDeliveryDate").datepicker('destroy');
        }
      };

      $scope.getShippingMethodList = function () {
        emptyList("shippingMethod");
        $("#expectedExportDate").prop("disabled", true);
        $("#expectedExportDate").removeAttr("style");
        $("#expectedExportDate").css({ "display": "inline" });
        // $('#expectedExportDate').datepicker('destroy');
        $('#expectedExportDate').val("");
        $('#fcaDeliveryDate').attr('readonly', true);
        $("#fcaDeliveryDate").removeAttr("style");
        $("#fcaDeliveryDate").css({ "display": "inline" });
        // $('#fcaDeliveryDate').datepicker('destroy');
        $('#fcaDeliveryDate').val("");

        $scope.expectedExportDate = "";
        $("#fcaDeliveryDate").val("");
        var destinationCountry = $("#destinationCountry").val();
        var ajaxUrl = '${pageContext.request.contextPath}/InventoryShipments/getShippingMethodList/';
        var shippingMethodList = null;
        $("#shipping-rates").html("");
        $.ajax({
          type: 'get',
          url: ajaxUrl,
          contentType: "application/json; charset=utf-8",
          data: {
            destinationCountry: destinationCountry
          },
          dataType: "json",
          success: function (data) {
            var springMessage = new Array();
            springMessage["EXPRESS"] = '<spring:message code="EXPRESS" />';
            springMessage["AIR_CARGO"] = '<spring:message code="AIR_CARGO" />';
            springMessage["SEA_FREIGHT"] = '<spring:message code="SEA_FREIGHT" />';
            shippingMethodList = data;
            var selectShippingMethod = document.getElementById("shippingMethod");
            var optShippingMethod = document.createElement("option");
            optShippingMethod.value = "";
            optShippingMethod.textContent = "--- Select ---";
            selectShippingMethod.appendChild(optShippingMethod);
            for (var shippingMethod in shippingMethodList) {
              if (shippingMethodList.hasOwnProperty(shippingMethod)) {
                optShippingMethod = document.createElement("option");
                optShippingMethod.value = shippingMethodList[shippingMethod];
                optShippingMethod.textContent = springMessage[shippingMethodList[shippingMethod]];
                selectShippingMethod.appendChild(optShippingMethod);
              }
            }
            $("#shippingMethod").val("");
            $("#shipping-rates").html($scope.shippingRates[destinationCountry]);
          }
        });
      };

      $scope.resetSelectOptions = function () {
        $("#shippingMethod").val("");
        $('#expectedExportDate').attr('readonly', true);
        $("#expectedExportDate").removeAttr("style");
        $("#expectedExportDate").css({ "display": "inline" });
        // $('#expectedExportDate').datepicker('destroy');
        $('#expectedExportDate').val("");
        $('#fcaDeliveryDate').attr('readonly', true);
        $("#fcaDeliveryDate").removeAttr("style");
        $("#fcaDeliveryDate").css({ "display": "inline" });
        // $('#fcaDeliveryDate').datepicker('destroy');
        $('#fcaDeliveryDate').val("");
        var FCADeliveryLocationId = $("#fcaDeliveryLocation").val();
        if (FCADeliveryLocationId == 4) {
          $("#shipping-rates").fadeTo(0, 0);
          $("#exportDateDesc").fadeTo(0, 0);
        } else {
          $("#shipping-rates").fadeTo(200, 1);
          $("#exportDateDesc").fadeTo(200, 1);
        }
      };

      $scope.getDaysToPrepare = function () {
        getDaysToPrepare();
      }

      $scope.getFCADeliveryDate = function () {
        var destinationCountry = $("#destinationCountry").val();
        var shippingMethod = $("#shippingMethod").val();
        var FCADeliveryLocationId = $("#fcaDeliveryLocation").val();
        var expectedExportDate = $("#expectedExportDate").val();
        var ajaxUrl = '${pageContext.request.contextPath}/InventoryShipments/getFcaDeliveryDate/';
        var FCADeliveryDate = null;
        $("#fcaDeliveryDate").val("");

        if (FCADeliveryLocationId != 4) {
          getDaysToPrepare();
          if (expectedExportDate == "") {
            return;
          }
          $('#fcaDeliveryDate').attr('readonly', true);
          $("#fcaDeliveryDate").removeAttr("style");
          $("#fcaDeliveryDate").css({ "display": "inline" });
          // $('#fcaDeliveryDate').datepicker('destroy');
          $.ajax({
            type: 'get',
            url: ajaxUrl,
            contentType: "application/json; charset=utf-8",
            data: {
              destinationCountry: destinationCountry,
              shippingMethod: shippingMethod,
              FCADeliveryLocationId: FCADeliveryLocationId,
              expectedExportDate: expectedExportDate
            },
            dataType: "json",
            success: function (data) {
              FCADeliveryDate = data;
              $("#fcaDeliveryDate").val(FCADeliveryDate);
            }
          });

        } else {

          $("#expectedExportDate").prop("disabled", false);
          $("#expectedExportDate").css({ "cursor": "default", "background-color": "white" });
          /* New datepicker */
          $(function() {
            $("#expectedExportDate").datetimepicker(
              {
                format: 'YYYY-MM-DD', // Originally'MM/DD/YYYY', changed to comply with DRS's date format
                icons: {
                  time: "fa fa-clock-o",
                  date: "fa fa-calendar",
                  up: "fa fa-chevron-up",
                  down: "fa fa-chevron-down",
                  previous: 'fa fa-chevron-left',
                  next: 'fa fa-chevron-right',
                  today: 'fa fa-screenshot',
                  clear: 'fa fa-trash',
                  close: 'fa fa-remove'
                }
              }
            );
            console.log("Yes4.");
          });
          // $('#expectedExportDate').datepicker('destroy');
          /* jQuery("#expectedExportDate").datepicker({
            beforeShow: function () {
              setTimeout(function () {
                $('.ui-datepicker').css('z-index', 200);
              }, 0);
            },
            dateFormat: 'yy-mm-dd'
          }); */
			    /*$('#fcaDeliveryDate').attr('readonly', false);
			    $("#fcaDeliveryDate").css({"cursor": "default", "background-color": "white"});
			    jQuery("#fcaDeliveryDate").datepicker({
					beforeShow: function() {
						setTimeout(function(){
							$('.ui-datepicker').css('z-index', 200);
			    	    }, 0);
			    	},
			    	dateFormat : 'yy-mm-dd'
			    });*/
          $("#fcaDeliveryDate").val(expectedExportDate);

        }

      };

      $scope.isGUIInvoiceIsRequiredForUnMixedBox = function (item) {
        console.log(item);
        var sku = item.skuCode;
        $.ajax({
          type: 'get',
          url: '${pageContext.request.contextPath}/InventoryShipments/isGUIInvoiceIsRequired',
          contentType: "application/json; charset=utf-8",
          data: {
            sku: sku
          },
          dataType: "json",
          success: function (data) {
            item.isGUIInvoiceEnable = data;
            item.isGUIInvoiceIsRequired = false;
            if (item.isGUIInvoiceEnable == true && $scope.status == "SHPT_PLANNING") item.isGUIInvoiceIsRequired = true;
            $scope.$apply();
          }
        });
      };

      $scope.isGUIInvoiceIsRequiredForMixedBox = function (mixedContentBoxItem) {
        var sku = mixedContentBoxItem.skuCode;
        $.ajax({
          type: 'get',
          url: '${pageContext.request.contextPath}/InventoryShipments/isGUIInvoiceIsRequired',
          contentType: "application/json; charset=utf-8",
          data: {
            sku: sku
          },
          dataType: "json",
          success: function (data) {
            mixedContentBoxItem.isGUIInvoiceEnable = data;
            mixedContentBoxItem.isGUIInvoiceIsRequired = false;
            if (mixedContentBoxItem.isGUIInvoiceEnable == true && $scope.status == "SHPT_PLANNING") mixedContentBoxItem.isGUIInvoiceIsRequired = true;
            $scope.$apply();
          }
        });
      };

      $scope.uploadGUIInvoiceFile = function (id, item) {

        var fileData = $("#GUIInvoiceFile" + id).prop("files")[0];
        var form_data = new FormData();
        form_data.append("file", fileData)

        $.ajax({
          url: "${pageContext.request.contextPath}/InventoryShipments/uploadGUIInvoiceFile",
          dataType: 'script',
          cache: false,
          contentType: false,
          processData: false,
          data: form_data,
          type: 'POST',
          success: function (data) {
            var result = JSON.parse(data);
            var idx = $scope.lineItems.indexOf(item);
            $scope.lineItems[idx].guifileName = fileData['name'];
            $scope.$apply();
          }
        });
      };

      $scope.uploadGUIInvoiceFileInMixedBox = function (id, mixedContentBox, item) {

        var fileData = $("#GUIInvoiceFile" + id).prop("files")[0];
        var form_data = new FormData();
        form_data.append("file", fileData)

        $.ajax({
          url: "${pageContext.request.contextPath}/InventoryShipments/uploadGUIInvoiceFile",
          dataType: 'script',
          cache: false,
          contentType: false,
          processData: false,
          data: form_data,
          type: 'POST',
          success: function (data) {
            var result = JSON.parse(data);
            var idx = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].indexOf(item);
            $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][idx].guifileName = fileData['name'];
            $scope.$apply();
          }
        });
      };

      $scope.removeGUIInvoiceFile = function (GUIInvoiceFileName, item) {

        $.ajax({
          type: 'POST',
          url: '${pageContext.request.contextPath}/InventoryShipments/removeGUIInvoiceFile',
          data: {
            GUIInvoiceFileName: GUIInvoiceFileName
          },
          success: function (data) {
            var result = JSON.parse(data);
            var idx = $scope.lineItems.indexOf(item);
            $scope.lineItems[idx].guifileName = null;
            $scope.$apply();
          }

        });

      };

      $scope.removeGUIInvoiceFileInMixedBox = function (GUIInvoiceFileName, mixedContentBox, item) {

        $.ajax({
          type: 'POST',
          url: '${pageContext.request.contextPath}/InventoryShipments/removeGUIInvoiceFile',
          data: {
            GUIInvoiceFileName: GUIInvoiceFileName
          },
          success: function (data) {
            var result = JSON.parse(data);
            var idx = $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)].indexOf(item);
            $scope.mixedContentBoxes[$scope.mixedContentBoxes.indexOf(mixedContentBox)][idx].guifileName = null;
            $scope.$apply();
          }

        });

      };

      var currentURL = document.URL;
      $scope.shippingRate = "";
      $scope.shippingRates = [];
      $scope.shippingRates["US"] = '<spring:message code="inventoryShipment.shippingRatesUS" />';
      $scope.shippingRates["UK"] = '<spring:message code="inventoryShipment.shippingRatesUK" />';
      $scope.shippingRates["CA"] = '<spring:message code="inventoryShipment.shippingRatesCA" />';
      $scope.shippingRates["DE"] = '<spring:message code="inventoryShipment.shippingRatesUK" />';
      $scope.shippingRates["FR"] = '<spring:message code="inventoryShipment.shippingRatesUK" />';
      $scope.shippingRates["EU"] = '<spring:message code="inventoryShipment.shippingRatesUK" />';
      getFCADeliveryLocations();
      if (currentURL.indexOf("edit") > -1) {
        $scope.duplicated = false;
        //$scope.atLeastOneSKU = false;
        $scope.FCAPriceNoneEqual = false;
        var InventoryShipmentJson = ${ InventoryShipmentJson };
        console.log(InventoryShipmentJson);
        $scope.destinationCountry = InventoryShipmentJson.destinationCountry;
        $("#shipping-rates").html($scope.shippingRates[$scope.destinationCountry]);
        $scope.shippingMethod = InventoryShipmentJson.shippingMethod;
        $scope.invoiceNumber = InventoryShipmentJson.invoiceNumber;
        $scope.expectedExportDate = InventoryShipmentJson.expectedExportDate;
        $scope.fcaDeliveryLocationId = InventoryShipmentJson.fcaDeliveryLocationId;
        getShippingMethodList();

        getDaysToPrepare();

        $scope.boxesNeedRepackaging = InventoryShipmentJson.boxesNeedRepackaging;
        $scope.repackagingFee = InventoryShipmentJson.repackagingFee;
        $scope.requiredPO = InventoryShipmentJson.requiredPO;
        $scope.PONumber = InventoryShipmentJson.ponumber;

        $scope.lineItems = [];
        $scope.mixedContentBoxes = [];
        var lineItems = InventoryShipmentJson.lineItems;

        for (i = 0; i < lineItems.length; i++) {
          if (lineItems[i].mixedBoxLineSeq == 0) {
            console.log(lineItems[i]);
            $scope.lineItems.push(lineItems[i]);
            $scope.isGUIInvoiceIsRequiredForUnMixedBox(lineItems[i]);
          }
        }

        for (i = 0; i < lineItems.length; i++) {
          if (lineItems[i].mixedBoxLineSeq != 0) {
            if (typeof $scope.mixedContentBoxes[lineItems[i].boxNum] == "undefined") $scope.mixedContentBoxes[lineItems[i].boxNum] = [];
            $scope.mixedContentBoxes[lineItems[i].boxNum].push(lineItems[i]);
            $scope.isGUIInvoiceIsRequiredForMixedBox(lineItems[i]);
          }
        }

        $scope.mixedContentBoxes = $scope.mixedContentBoxes.filter(function (el) { return el; });

        $scope.calculateTotalCartonCount();
        $scope.calculateTotalQuantity();
        $scope.isAtLeastOneSKU();
        $scope.subtotal = InventoryShipmentJson.subtotal;
        $scope.salesTaxPercentage = InventoryShipmentJson.salesTaxPercentage;
        $scope.taxAmount = InventoryShipmentJson.salesTax;
        $scope.totalAmount = InventoryShipmentJson.total;
        $scope.status = InventoryShipmentJson.status;
      } else {
        $scope.duplicated = true;
        $scope.boxesNeedRepackaging = 0;
        $scope.repackagingFee = 0;
        $scope.requiredPO = false;
        $scope.atLeastOneSKUWarning = false;
        $scope.noSKU = true;
        $scope.FCAPriceNoneEqual = false;
        var defaultSalesTaxPercentage = ${ defaultSalesTaxPercentage };
        $scope.lineItems = [{ boxNum: 1, mixedBoxLineSeq: 0, requireRepackaging: false, cartonCounts: null, quantity: 0, amountUntaxed: 0, cartonNumberStart: 0, cartonNumberEnd: 0, isGUIInvoiceIsRequired: false, isGUIInvoiceEnable: false, guifileName: null }];
        $scope.mixedContentBoxes = [];
        $scope.totalCartonCount = 0;
        $scope.totalQuantity = 0;
        $scope.subtotal = 0;
        $scope.salesTaxPercentage = defaultSalesTaxPercentage;
        $scope.taxAmount = 0;
        $scope.totalAmount = 0;
        $scope.status = null;
      }
    });
  </script>
  <style>
    tbody {
      border: 0 !important;
    }

    tbody > tr > td {
      border: 0;
    }

    .card {
      padding: 2rem;
    }

    .info {
      margin-bottom: 1.5rem;
    }

    .info > div:first-child {
      font-size: 1rem;
      font-weight: 900;
    }

    .info > div:last-child {
      width: 200px;
      text-align: right;
    }

    .drs-btn-primary {
      padding: 0.6rem 1rem;
    }

    .sum-list {
      display: table;
      width: 100%;
      text-align: center;
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
  </style>
</head>

<div class="max-width" style="max-width:1600px !important;padding-left:20px;padding-right:20px;" ng-app="inventoryShipment" ng-controller="inventoryShipmentCtrl">
  <div class="container-fluid">
    <!-- Card start -->
    <div class="card">
      <div class="card-body">
        <!-- Top left message -->
        <div class="row max-width">
          <div class="col-md-12">
            <div class="page-heading">
              <!-- <c:choose>
                <c:when test="${type ne 'Create'}">
                  <c:choose>
                    <c:when
                      test="${InventoryShipment.status eq 'SHPT_DRAFT' || InventoryShipment.status eq 'SHPT_AWAIT_PLAN' || InventoryShipment.status eq 'SHPT_PLANNING'}">
                      <spring:message code='inventoryShipment.inventoryShipment' />
                    </c:when>
                    <c:otherwise>
                      <spring:message code='inventoryShipment.inventoryPurchaseOrder' />
                    </c:otherwise>
                  </c:choose>
                </c:when>
                <c:otherwise>
                  <spring:message code='inventoryShipment.createInventoryShipment' />
                </c:otherwise>
              </c:choose> -->
              <a style="font-size:13pt;" href="<c:url value="/resources/files/HelpIVS.pdf"/>" target="_blank">
                <span class="fas fa-question-circle"></span>
                <spring:message code="inventoryShipment.help" />
              </a>
            </div>
            <span class="text-danger">
              <spring:message code='inventoryShipment.validation' />
            </span>
          </div>
        </div>

        <hr class="my-4">

        <form:form name="InventoryShipment" modelAttribute="InventoryShipment" onsubmit="return onSubmit();" novalidate="novalidate" enctype="multipart/form-data">
          <!-- First row -->
          <div class="row info-row">
            <div class="col-12 col-xl-4 d-flex flex-column">
              <!-- Inventory Shipment ID 編號 -->
              <c:choose>
                <c:when test="${type ne 'Create'}">
                  <div class="d-flex justify-content-between align-items-center info">
                    <div>
                      <spring:message code='inventoryShipment.inventoryShipmentId' /></b>
                    </div>
                    <div class="active">
                      ${InventoryShipment.name}
                      <form:hidden path="name" />
                    </div>
                  </div>
                </c:when>
              </c:choose>
              <!-- FCA Delivery Location 交貨地點 -->
              <div class="d-flex justify-content-between align-items-center info">
                <div>
                  <spring:message code='inventoryShipment.FCADeliveryLocation' /><span class="text-danger">*</span>
                </div>
                <c:choose>
                  <c:when
                    test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                    <div class="active">
                      ${FCADeliveryLocationIdToLocationMap[InventoryShipment.fcaDeliveryLocationId]}
                      <form:hidden path="fcaDeliveryLocationId" />
                    </div>
                  </c:when>
                  <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                    <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                      <div class="active">
                        ${FCADeliveryLocationIdToLocationMap[InventoryShipment.fcaDeliveryLocationId]}
                        <form:hidden path="fcaDeliveryLocationId" />
                      </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <div>
                        <form:select id="fcaDeliveryLocation" class="form-control" style="display: inline;"
                          path="fcaDeliveryLocationId" ng-model="fcaDeliveryLocationId" ng-change="resetSelectOptions();"
                          required="required">
                        </form:select>
                        <div class="text-danger"
                          ng-show="InventoryShipment.fcaDeliveryLocationId.$error.required && InventoryShipment.fcaDeliveryLocationId.$dirty">
                          <spring:message code='inventoryShipment.FCADeliveryLocation_req' />
                        </div>
                      </div>
                    </sec:authorize>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <form:select id="fcaDeliveryLocation" class="form-control" style="display: inline;"
                        path="fcaDeliveryLocationId" ng-model="fcaDeliveryLocationId" ng-change="resetSelectOptions();"
                        required="required">
                      </form:select>
                      <div class="text-danger"
                        ng-show="InventoryShipment.fcaDeliveryLocationId.$error.required && InventoryShipment.fcaDeliveryLocationId.$dirty">
                        <spring:message code='inventoryShipment.FCADeliveryLocation_req' />
                      </div>
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
              <!-- Destination 目的地國家 -->
              <div class="d-flex flex-column mb-4">
                <div class="d-flex justify-content-between align-items-center info mb-2">
                  <div>
                    <spring:message code='inventoryShipment.destination' /><span class="text-danger">*</span>
                  </div>
                  <c:choose>
                    <c:when
                      test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                      <div class="active">
                        <spring:message code="${InventoryShipment.destinationCountry}" />
                        <form:hidden path="destinationCountry" />
                      </div>
                    </c:when>
                    <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                      <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                        <div class="active">
                          <spring:message code="${InventoryShipment.destinationCountry}" />
                          <form:hidden path="destinationCountry" />
                        </div>
                      </sec:authorize>
                      <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                        <div>
                          <form:select id="destinationCountry" class="form-control" style="display: inline;"
                            path="destinationCountry" ng-model="destinationCountry" ng-change="getShippingMethodList()"
                            required="required">
                            <form:option value="">--- Select ---</form:option>
                            <c:forEach var="destinationCountry" items="${destinationCountryList}">
                              <form:option value="${destinationCountry}">
                                <spring:message code="${destinationCountry}" />
                              </form:option>
                            </c:forEach>
                          </form:select>
                          <div class="text-danger"
                            ng-show="InventoryShipment.destinationCountry.$error.required && InventoryShipment.destinationCountry.$dirty">
                            <spring:message code='inventoryShipment.destination_req' />
                          </div>
                        </div>
                      </sec:authorize>
                    </c:when>
                    <c:otherwise>
                      <div>
                        <form:select id="destinationCountry" class="form-control" style="display: inline;"
                          path="destinationCountry" ng-model="destinationCountry" ng-change="getShippingMethodList()"
                          required="required">
                          <form:option value="">--- Select ---</form:option>
                          <c:forEach var="destinationCountry" items="${destinationCountryList}">
                            <form:option value="${destinationCountry}">
                              <spring:message code="${destinationCountry}" />
                            </form:option>
                          </c:forEach>
                        </form:select>
                        <div class="text-danger"
                          ng-show="InventoryShipment.destinationCountry.$error.required && InventoryShipment.destinationCountry.$dirty">
                          <spring:message code='inventoryShipment.destination_req' />
                        </div>
                      </div>
                    </c:otherwise>
                  </c:choose>
                </div>
                <div id="shipping-rates" style="width: 200px; align-self: flex-end;"></div>
              </div>
              <!-- Shipping Method 運送方式 -->
              <div class="d-flex justify-content-between align-items-center info">
                <div>
                  <spring:message code='inventoryShipment.shippingMethod' /><span class="text-danger">*</span>
                </div>
                <c:choose>
                  <c:when
                    test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                    <div class="active">
                      <spring:message code="${InventoryShipment.shippingMethod}" />
                      <form:hidden path="shippingMethod" />
                    </div>
                  </c:when>
                  <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                    <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                      <div class="active">
                        <spring:message code="${InventoryShipment.shippingMethod}" />
                        <form:hidden path="shippingMethod" />
                      </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <div>
                        <form:select id="shippingMethod" class="form-control" style="display: inline;"
                          path="shippingMethod" ng-model="shippingMethod" ng-mouseup="getDaysToPrepare()"
                          required="required">
                        </form:select>
                        <div class="text-danger"
                          ng-show="InventoryShipment.shippingMethod.$error.required && InventoryShipment.shippingMethod.$dirty">
                          <spring:message code='inventoryShipment.shippingMethod_req' />
                        </div>
                      </div>
                    </sec:authorize>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <form:select id="shippingMethod" class="form-control" style="display: inline;" path="shippingMethod"
                        ng-model="shippingMethod" ng-mouseup="getDaysToPrepare()" required="required">
                      </form:select>
                      <div class="text-danger"
                        ng-show="InventoryShipment.shippingMethod.$error.required && InventoryShipment.shippingMethod.$dirty">
                        <spring:message code='inventoryShipment.shippingMethod_req' />
                      </div>
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <div class="col-12 col-xl-4 d-flex flex-column">
              <!-- Expected Export Date 預計出口日期 -->
              <div class="d-flex flex-column mb-4">
                <div class="d-flex justify-content-between align-items-center info mb-2">
                  <div>
                    <spring:message code='inventoryShipment.expectedExportDate' /><span class="text-danger">*</span>
                  </div>
                  <c:choose>
                    <c:when
                      test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                      <div class="active">
                        ${InventoryShipment.expectedExportDate}
                        <form:hidden path="expectedExportDate" />
                      </div>
                    </c:when>
                    <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                      <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                        <div class="active">
                          ${InventoryShipment.expectedExportDate}
                          <form:hidden path="expectedExportDate" />
                        </div>
                      </sec:authorize>
                      <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                        <div>
                          <form:input id="expectedExportDate" class="form-control" style="display: inline;"
                            path="expectedExportDate" ng-model="expectedExportDate" ng-change="getFCADeliveryDate()"
                            required="required" disabled="true" />
                          <div class="text-danger"
                            ng-show="InventoryShipment.expectedExportDate.$error.required && InventoryShipment.expectedExportDate.$dirty">
                            <spring:message code='inventoryShipment.expectedExportDate_req' />
                          </div>
                        </div>
                      </sec:authorize>
                    </c:when>
                    <c:otherwise>
                      <div>
                        <form:input id="expectedExportDate" class="form-control" style="display: inline;"
                          path="expectedExportDate" ng-model="expectedExportDate" ng-change="getFCADeliveryDate()"
                          required="required" disabled="true" />
                        <div class="text-danger"
                          ng-show="InventoryShipment.expectedExportDate.$error.required && InventoryShipment.expectedExportDate.$dirty">
                          <spring:message code='inventoryShipment.expectedExportDate_req' />
                        </div>
                      </div>
                    </c:otherwise>
                  </c:choose>
                </div>
                <div id="exportDateDesc" style="width: 200px; align-self: flex-end;">
                  <spring:message code='inventoryShipment.referToShippingSchedule' />
                </div>
              </div>
              <!-- Delivery Date 交貨日 -->
              <div class="d-flex justify-content-between align-items-center info">
                <div class="text-right">
                  <spring:message code='inventoryShipment.FCADeliveryDate' /><span class="text-danger">*</span>
                </div>
                <c:choose>
                  <c:when
                    test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                    <div class="active">
                      ${InventoryShipment.fcaDeliveryDate}
                      <form:hidden path="fcaDeliveryDate" />
                    </div>
                  </c:when>
                  <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                    <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                      <div class="active">
                        ${InventoryShipment.fcaDeliveryDate}
                        <form:hidden path="fcaDeliveryDate" />
                      </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <div>
                        <form:input id="fcaDeliveryDate" class="form-control" style="display:inline;"
                          path="fcaDeliveryDate" />
                      </div>
                    </sec:authorize>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <form:input id="fcaDeliveryDate" class="form-control" style="display:inline;"
                        path="fcaDeliveryDate" />
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
              <!-- Required PO 需要採購訂單? -->
              <div class="d-flex justify-content-between align-items-center info">
                <div>
                  <spring:message code='inventoryShipment.requiredPO' />
                </div>
                <c:choose>
                  <c:when
                    test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                    <div class="active">
                      <spring:message code='inventoryShipment.${InventoryShipment.requiredPO}' />
                      <form:hidden path="requiredPO" />
                    </div>
                  </c:when>
                  <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                    <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                      <div class="active">
                        <spring:message code='inventoryShipment.${InventoryShipment.requiredPO}' />
                        <form:hidden path="requiredPO" />
                      </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <div>
                        <form:checkbox id="requiredPO" path="requiredPO" ng-model="requiredPO"
                          ng-change="removePONumber()" />
                      </div>
                    </sec:authorize>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <form:checkbox id="requiredPO" path="requiredPO" ng-model="requiredPO"
                        ng-change="removePONumber()" />
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <div class="col-12 col-xl-4 d-flex flex-column">
              <!-- Boxes Need Repackaging 換箱箱數 -->
              <div class="d-flex justify-content-between align-items-center info">
                <div>
                  <spring:message code='inventoryShipment.boxesNeedRepackaging' />
                </div>
                <c:choose>
                  <c:when
                    test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                    <div class="active">
                      ${InventoryShipment.boxesNeedRepackaging}
                      <form:hidden path="boxesNeedRepackaging" />
                    </div>
                  </c:when>
                  <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                    <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                      <div class="active">
                        ${InventoryShipment.boxesNeedRepackaging}
                        <form:hidden path="boxesNeedRepackaging" />
                      </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <div>
                        <form:input id="boxesNeedRepackaging" class="form-control"
                          style="display:inline;text-align:right;" path="boxesNeedRepackaging"
                          ng-model="boxesNeedRepackaging" />
                      </div>
                    </sec:authorize>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <form:input id="boxesNeedRepackaging" class="form-control" style="display:inline;text-align:right;"
                        path="boxesNeedRepackaging" ng-model="boxesNeedRepackaging" />
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
              <!-- Repackaging Fee 換箱費用 -->
              <div class="d-flex justify-content-between align-items-center info">
                <div>
                  <spring:message code='inventoryShipment.repackagingFee' />
                </div>
                <c:choose>
                  <c:when
                    test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                    <div class="active">
                      ${InventoryShipment.repackagingFee}
                      <form:hidden path="repackagingFee" />
                    </div>
                  </c:when>
                  <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                    <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                      <div class="active">
                        ${InventoryShipment.repackagingFee}
                        <form:hidden path="repackagingFee" />
                      </div>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <div>
                        <form:input id="repackagingFee" class="form-control" style="display:inline;text-align:right;"
                          path="repackagingFee" ng-model="repackagingFee" ng-readonly="isSupplier" />
                      </div>
                    </sec:authorize>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <form:input id="repackagingFee" class="form-control" style="display:inline;text-align:right;"
                        path="repackagingFee" ng-model="repackagingFee" ng-readonly="isSupplier" />
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
              <!-- Special Request 特殊要求 -->
              <div class="d-flex justify-content-between align-items-center info">
                <div>
                  <spring:message code='inventoryShipment.specialRequest' />
                  <label class="col-sm-4 col-form-label" data-html="true" data-toggle="tooltip" data-placement="top"
                    title="<spring:message code="inventoryShipment.special_request_hint" />">
                  <span class="fas fa-info-circle fa-1x" style="color:#428bca;"></span>
                  </label>
                </div>
                <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                  <div class="active">
                    ${InventoryShipment.specialRequest}
                    <form:hidden path="specialRequest" />
                  </div>
                </sec:authorize>
                <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                  <c:choose>
                    <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                      <div class="active">
                        ${InventoryShipment.specialRequest}
                        <form:hidden path="specialRequest" />
                      </div>
                    </c:when>
                    <c:otherwise>
                      <div>
                        <form:textarea class="form-control p-1" path="specialRequest" rows="5" cols="30" style="max-height: 5rem;" />
                      </div>
                    </c:otherwise>
                  </c:choose>
                </sec:authorize>
              </div>
              <!-- PO Number 採購訂單號碼 -->
              <div class="d-flex justify-content-between align-items-center info">
                <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                  <div>
                    <spring:message code='inventoryShipment.PONumber' />
                  </div>
                  <c:choose>
                    <c:when
                      test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                      <div class="active">
                        ${InventoryShipment.PONumber}
                        <form:hidden path="PONumber" />
                      </div>
                    </c:when>
                    <c:otherwise>
                      <div>
                        <form:input id="PONumber" class="form-control" path="PONumber" ng-model="PONumber" ng-required="requiredPO" ng-disabled="!requiredPO" />
                        <div class="text-danger" ng-show="InventoryShipment['PONumber'].$error.required && InventoryShipment['PONumber'].$dirty">
                          <spring:message code='inventoryShipment.PONumber_req' />
                        </div>
                      </div>
                    </c:otherwise>
                  </c:choose>
                </sec:authorize>

                <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                  <c:choose>
                    <c:when test="${type ne 'Create'}">
                      <div class="text-right">
                        <spring:message code='inventoryShipment.PONumber' />
                      </div>
                      <div class="active">
                        ${InventoryShipment.PONumber}
                        <form:hidden path="PONumber" />
                      </div>
                    </c:when>
                  </c:choose>
                </sec:authorize>

                <sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_PURCHASE_ORDER']}')">
                  <c:if
                    test="${InventoryShipment.status == 'SHPT_AWAIT_PLAN' || InventoryShipment.status == 'SHPT_PLANNING' || InventoryShipment.status == 'SHPT_CONFIRMED' || InventoryShipment.status == 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status == 'SHPT_IN_TRANSIT' || InventoryShipment.status == 'SHPT_RECEIVING' || InventoryShipment.status == 'SHPT_RECEIVED' || InventoryShipment.status == 'SHPT_EXCEPTION'}">
                    <div ng-if="PONumber != null">
                      <a class="btn btn-primary btn-sm" target="_blank"
                        href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}/{{PONumber}}/PurchaseOrder">
                        <spring:message code="inventoryShipment.printPO" />
                      </a>
                    </div>
                  </c:if>
                </sec:authorize>
              </div>
            </div>
          </div>

          <!-- Second row -->
          <c:choose>
            <c:when test="${type ne 'Create'}">
              <div class="row info-row">
                <div class="col-12 col-xl-4 d-flex flex-column">
                  <div class="d-flex justify-content-between align-items-center info">
                    <div>
                      <spring:message code='inventoryShipment.status' />
                    </div>
                    <c:choose>
                      <c:when test="${status eq 'noDRAFT'}">
                        <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                          <div>
                            <form:select id="status" class="form-control" path="status">
                              <c:choose>
                                <c:when test="${InventoryShipment.status eq 'SHPT_CONFIRMED'}">
                                  <form:option value="SHPT_CONFIRMED">
                                    <spring:message code="SHPT_CONFIRMED" />
                                  </form:option>
                                  <form:option value="SHPT_AWAIT_PICK_UP">
                                    <spring:message code="SHPT_AWAIT_PICK_UP" />
                                  </form:option>
                                  <form:option value="SHPT_IN_TRANSIT">
                                    <spring:message code="SHPT_IN_TRANSIT" />
                                  </form:option>
                                  <form:option value="SHPT_RECEIVING">
                                    <spring:message code="SHPT_RECEIVING" />
                                  </form:option>
                                  <form:option value="SHPT_RECEIVED">
                                    <spring:message code="SHPT_RECEIVED" />
                                  </form:option>
                                  <form:option value="SHPT_EXCEPTION">
                                    <spring:message code="SHPT_EXCEPTION" />
                                  </form:option>
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_AWAIT_PLAN'}">
                                  <form:option value="SHPT_AWAIT_PLAN">
                                    <spring:message code="SHPT_AWAIT_PLAN" />
                                  </form:option>
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <form:option value="SHPT_PLANNING">
                                    <spring:message code="SHPT_PLANNING" />
                                  </form:option>
                                </c:when>
                                <c:when
                                  test="${InventoryShipment.status ne 'SHPT_AWAIT_PLAN' && InventoryShipment.status ne 'SHPT_PLANNING' && InventoryShipment.status ne 'SHPT_CONFIRMED'}">
                                  <form:option value="SHPT_AWAIT_PICK_UP">
                                    <spring:message code="SHPT_AWAIT_PICK_UP" />
                                  </form:option>
                                  <form:option value="SHPT_IN_TRANSIT">
                                    <spring:message code="SHPT_IN_TRANSIT" />
                                  </form:option>
                                  <form:option value="SHPT_RECEIVING">
                                    <spring:message code="SHPT_RECEIVING" />
                                  </form:option>
                                  <form:option value="SHPT_RECEIVED">
                                    <spring:message code="SHPT_RECEIVED" />
                                  </form:option>
                                  <form:option value="SHPT_EXCEPTION">
                                    <spring:message code="SHPT_EXCEPTION" />
                                  </form:option>
                                </c:when>
                              </c:choose>
                            </form:select>
                          </div>
                        </sec:authorize>
                        <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                          <div class="active">
                            <spring:message code="${InventoryShipment.status}" />
                          </div>
                        </sec:authorize>
                      </c:when>
                      <c:otherwise>
                        <div class="active">
                          <spring:message code="${InventoryShipment.status}" />
                        </div>
                      </c:otherwise>
                    </c:choose>
                  </div>

                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                    <!-- Seller -->
                    <c:choose>
                      <c:when test="${type ne 'Create' && status ne 'DRAFT' && status ne 'null'}">
                        <div class="d-flex justify-content-between align-items-center info">
                          <div>
                            <spring:message code='inventoryShipment.seller' />
                          </div>
                          <div>
                            ${InventoryShipment.sellerCompanyKcode}
                            ${AllCompanyKcodeToNameMap[InventoryShipment.sellerCompanyKcode]}
                            <form:hidden path="sellerCompanyKcode" />
                          </div>
                        </div>
                      </c:when>
                    </c:choose>

                    <!-- Buyer -->
                    <c:choose>
                      <c:when test="${type ne 'Create' && status ne 'DRAFT' && status ne 'null'}">
                        <div class="d-flex justify-content-between align-items-center info">
                          <div>
                            <spring:message code='inventoryShipment.buyer' />
                          </div>
                          <div>
                            ${InventoryShipment.buyerCompanyKcode}
                            ${AllCompanyKcodeToNameMap[InventoryShipment.buyerCompanyKcode]}
                            <form:hidden path="buyerCompanyKcode" />
                          </div>
                        </div>
                      </c:when>
                    </c:choose>
                  </sec:authorize>
                </div>

                <div class="col-12 col-xl-4 d-flex flex-column">
                  <!-- Date created -->
                  <div class="d-flex justify-content-between align-items-center info">
                    <div>
                      <spring:message code='inventoryShipment.dateCreated' />
                    </div>
                    <div>
                      ${InventoryShipment.dateCreated}
                    </div>
                  </div>
                  <!-- Invoice DRS_USER -->
                  <div class="d-flex justify-content-between align-items-center info">
                    <div>
                      <spring:message code='inventoryShipment.invoiceNumber' />
                    </div>
                    <c:choose>
                      <c:when test="${status eq 'noDRAFT'}">
                        <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                          <div class="danger">
                            <form:input id="invoiceNumber" class="form-control" path="invoiceNumber"
                              ng-model="invoiceNumber" ng-pattern="/^[a-zA-Z0-9]+$/" />
                            <div class="text-danger" ng-show="InventoryShipment['invoiceNumber'].$error.pattern">
                              <spring:message code='inventoryShipment.InvoiceNumber_format' />
                            </div>
                          </div>
                        </sec:authorize>
                        <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                          <div class="active">
                            ${InventoryShipment.invoiceNumber}
                          </div>
                        </sec:authorize>
                      </c:when>
                      <c:otherwise>
                        <div class="active">
                          ${InventoryShipment.invoiceNumber}
                        </div>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>

                <div class="col-12 col-xl-4 d-flex flex-column">
                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                    <c:choose>
                      <c:when test="${InventoryShipment.status eq 'SHPT_CONFIRMED'}">
                        <div class="d-flex justify-content-between align-items-center info">
                          <div></div>
                          <div style="color: #2A6496;">
                            <spring:message code='inventoryShipment.invoiceNumber_hint' />
                          </div>
                        </div>
                      </c:when>
                    </c:choose>
                  </sec:authorize>
                  <!-- Date Purchased -->
                  <div class="d-flex justify-content-between align-items-center info">
                    <div>
                      <spring:message code='inventoryShipment.datePurchased' />
                    </div>
                    <div class="active">
                      ${InventoryShipment.datePurchased}
                    </div>
                  </div>
                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                    <div class="d-flex justify-content-between align-items-center info">
                      <form:hidden path="internalNote" />
                    </div>
                  </sec:authorize>
                  <sec:authorize access="hasAnyRole('${auth['ADMIN_ONLY']}')">
                    <c:choose>
                      <c:when
                        test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                        <div class="d-flex justify-content-between align-items-center info">
                          <div>
                            <spring:message code='inventoryShipment.shippingCost' />
                          </div>
                          <div>${shippingCost}</div>
                        </div>
                      </c:when>
                    </c:choose>
                  </sec:authorize>
                </div>
              </div>

              <hr class="mt-0 mb-4">

              <!-- Internal note 內部備註 -->
              <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                <div class="row">
                  <div class="col-12 d-flex flex-column info mb-0">
                    <div class="mb-4">
                      <spring:message code='inventoryShipment.internalNote' />
                    </div>
                    <div class="danger w-100 mb-4" style="height: 12rem">
                      <form:textarea path="internalNote" class="form-control p-1" rows="10" cols="30" style="max-height: 12rem;" />
                    </div>
                  </div>
                </div>
              </sec:authorize>
            </c:when>
          </c:choose>

          <hr class="mt-0 mb-5">

          <!-- Don't know what message this is... -->
          <div class="row text-danger" ng-show="FCAPriceNoneEqual">
            <div class="col-md-12">
              <spring:message code='inventoryShipment.FCAPriceNoneEqual' />
            </div>
          </div>

          <!-- Non-mixed products -->
          <div class="row">
            <div class="col-md-12">
              <table class="table table-striped m-0">
                <thead>
                  <tr id="newSKU">
                    <th style="width: 15%;">
                      <spring:message code='inventoryShipment.sku' /><span class="text-danger">*</span>
                    </th>
                    <th style="width: 5%;">
                      <spring:message code='inventoryShipment.requireRepackaging' /><span class="text-danger">*</span>
                    </th>
                    <th class="text-center" style="width: 5%;">
                      <spring:message code='inventoryShipment.cartonNumber' /><span class="text-danger">*</span>
                    </th>
                    <th class="text-center" style="width: 20%;">
                      <spring:message code='inventoryShipment.cartonDimensions' /><span class="text-danger">*</span>
                    </th>
                    <th class="text-center" style="width: 5%;">
                      <spring:message code='inventoryShipment.grossWeightPerCarton' /><span class="text-danger">*</span>
                    </th>
                    <th class="text-center" style="width: 5%;">
                      <spring:message code='inventoryShipment.unitsPerCarton' /><span class="text-danger">*</span>
                    </th>
                    <th class="text-center" style="width: 5%;">
                      <spring:message code='inventoryShipment.numberOfCartons' /><span class="text-danger">*</span>
                    </th>
                    <th class="text-center" style="width: 5%;">
                      <spring:message code='inventoryShipment.quantity' />
                    </th>
                    <th class="text-center" style="width: 5%;">
                      <spring:message code='inventoryShipment.uniPrice' /><span class="text-danger">*</span>
                    </th>
                    <th class="text-center" style="width: 5%;">
                      <spring:message code='inventoryShipment.amount' />
                    </th>
                    <th style="width: 10%;">
                      <spring:message code='inventoryShipment.GUIInvoiceNumber' />
                    </th>
                    <th style="width: 10%;">
                      <spring:message code='inventoryShipment.GUIInvoice' />
                    </th>
                    <th style="width: 5%;"></th>
                  </tr>
                </thead>

                <tbody class="border-0">
                  <tr id="newSKU{{$id}}" ng-repeat="item in lineItems">
                    <td>
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum" ng-model="item.boxNum"
                            value="{{item.boxNum}}" type="text"></input>
                          <input type="hidden" id="mixedBoxLineSeq{{$id}}" name="lineItem[{{$id}}].mixedBoxLineSeq"
                            ng-model="item.mixedBoxLineSeq" value="{{item.mixedBoxLineSeq}}" type="text"></input>
                          {{item.skuCode}}
                          <input type="hidden" name="lineItem[{{$id}}].skuCode" value="{{item.skuCode}}" />
                          {{item.nameBySupplier}}
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                            <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum" ng-model="item.boxNum"
                              value="{{item.boxNum}}" type="text"></input>
                            <input type="hidden" id="mixedBoxLineSeq{{$id}}" name="lineItem[{{$id}}].mixedBoxLineSeq"
                              ng-model="item.mixedBoxLineSeq" value="{{item.mixedBoxLineSeq}}" type="text"></input>
                            {{item.skuCode}}
                            <input type="hidden" name="lineItem[{{$id}}].skuCode" value="{{item.skuCode}}" />
                            {{item.nameBySupplier}}
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum" ng-model="item.boxNum"
                              value="{{item.boxNum}}" type="text"></input>
                            <input type="hidden" id="mixedBoxLineSeq{{$id}}" name="lineItem[{{$id}}].mixedBoxLineSeq"
                              ng-model="item.mixedBoxLineSeq" value="{{item.mixedBoxLineSeq}}" type="text"></input>
                            <select id="SKU{{$id}}" class="form-control sku-selector" name="lineItem[{{$id}}].skuCode"
                              ng-model="item.skuCode" ng-change="verifySKUForUnMixedBox(item)" required>
                              <option value="">--- Select ---</option>
                              <c:forEach var="ActiveSkuCodeToName" items="${ActiveSkuCodeToNameMap}">
                                <option value="${ActiveSkuCodeToName.key}">${ActiveSkuCodeToName.key}
                                  ${ActiveSkuCodeToName.value}</option>
                              </c:forEach>
                            </select>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].skuCode'].$error.required && InventoryShipment['lineItem[{{$id}}].skuCode'].$dirty">
                              <spring:message code='inventoryShipment.SKU_req' />
                            </div>
                            <div class="text-danger" ng-if="item.isDuplicate">
                              <spring:message code='inventoryShipment.SKU_dup' />
                            </div>
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum" ng-model="item.boxNum"
                            value="{{item.boxNum}}" type="text"></input>
                          <input type="hidden" id="mixedBoxLineSeq{{$id}}" name="lineItem[{{$id}}].mixedBoxLineSeq"
                            ng-model="item.mixedBoxLineSeq" value="{{item.mixedBoxLineSeq}}" type="text"></input>
                          <select id="SKU{{$id}}" class="form-control sku-selector" name="lineItem[{{$id}}].skuCode"
                            ng-model="item.skuCode" ng-change="verifySKUForUnMixedBox(item)" required>
                            <option value="">--- Select ---</option>
                            <c:forEach var="ActiveSkuCodeToName" items="${ActiveSkuCodeToNameMap}">
                              <option value="${ActiveSkuCodeToName.key}">${ActiveSkuCodeToName.key}
                                ${ActiveSkuCodeToName.value}</option>
                            </c:forEach>
                          </select>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].skuCode'].$error.required && InventoryShipment['lineItem[{{$id}}].skuCode'].$dirty">
                            <spring:message code='inventoryShipment.SKU_req' />
                          </div>
                          <div class="text-danger" ng-if="item.isDuplicate">
                            <spring:message code='inventoryShipment.SKU_dup' />
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <c:choose>
                      <c:when
                        test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                        <td class="text-center">
                          <span ng-if="item.requireRepackaging == true">
                            <spring:message code='inventoryShipment.true' /></span>
                          <span ng-if="item.requireRepackaging == false">
                            <spring:message code='inventoryShipment.false' /></span>
                          <input type="hidden" name="lineItem[{{$id}}].requireRepackaging"
                            value="{{item.requireRepackaging}}" />
                        </td>
                      </c:when>
                      <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                        <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                          <td class="text-center">
                            <span ng-if="item.requireRepackaging == true">
                              <spring:message code='inventoryShipment.true' /></span>
                            <span ng-if="item.requireRepackaging == false">
                              <spring:message code='inventoryShipment.false' /></span>
                            <input type="hidden" name="lineItem[{{$id}}].requireRepackaging"
                              value="{{item.requireRepackaging}}" />
                          </td>
                        </sec:authorize>
                        <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                          <td class="text-center">
                            <input id="requireRepackaging{{$id}}" type="checkbox"
                              name="lineItem[{{$id}}].requireRepackaging" ng-model="item.requireRepackaging"
                              ng-change="calculateRepackgingFee()">
                          </td>
                        </sec:authorize>
                      </c:when>
                      <c:otherwise>
                        <td class="text-center">
                          <input id="requireRepackaging{{$id}}" type="checkbox" name="lineItem[{{$id}}].requireRepackaging"
                            ng-model="item.requireRepackaging" ng-change="calculateRepackgingFee()">
                        </td>
                      </c:otherwise>
                    </c:choose>
                    <td class="text-center active">
                      {{item.cartonNumberStart}} ~ {{item.cartonNumberEnd}}
                    </td>
                    <td class="text-center">
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          {{item.cartonDimensionCm1}} x {{item.cartonDimensionCm2}} x {{item.cartonDimensionCm3}}
                          <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm1"
                            value="{{item.cartonDimensionCm1}}" />
                          <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm2"
                            value="{{item.cartonDimensionCm2}}" />
                          <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm3"
                            value="{{item.cartonDimensionCm3}}" />
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                            {{item.cartonDimensionCm1}} x {{item.cartonDimensionCm2}} x {{item.cartonDimensionCm3}}
                            <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm1"
                              value="{{item.cartonDimensionCm1}}" />
                            <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm2"
                              value="{{item.cartonDimensionCm2}}" />
                            <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm3"
                              value="{{item.cartonDimensionCm3}}" />
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input name="lineItem[{{$id}}].cartonDimensionCm1" class="form-control"
                              style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:center;"
                              ng-model="item.cartonDimensionCm1" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value=""
                              type="text" size="3" required> x
                            <input name="lineItem[{{$id}}].cartonDimensionCm2" class="form-control"
                              style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:center;"
                              ng-model="item.cartonDimensionCm2" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value=""
                              type="text" size="3" required> x
                            <input name="lineItem[{{$id}}].cartonDimensionCm3" class="form-control"
                              style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:center;"
                              ng-model="item.cartonDimensionCm3" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value=""
                              type="text" size="3" required>
                            <div class="text-danger"
                              ng-show="(InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$dirty)">
                              <spring:message code='inventoryShipment.cartonDimensions_req' />
                            </div>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.pattern">
                              <spring:message code='inventoryShipment.cartonDimensions_format' />
                            </div>
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input name="lineItem[{{$id}}].cartonDimensionCm1" class="form-control"
                            style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:center;"
                            ng-model="item.cartonDimensionCm1" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text"
                            size="3" required> x
                          <input name="lineItem[{{$id}}].cartonDimensionCm2" class="form-control"
                            style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:center;"
                            ng-model="item.cartonDimensionCm2" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text"
                            size="3" required> x
                          <input name="lineItem[{{$id}}].cartonDimensionCm3" class="form-control"
                            style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:center;"
                            ng-model="item.cartonDimensionCm3" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text"
                            size="3" required>
                          <div class="text-danger"
                            ng-show="(InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$dirty)">
                            <spring:message code='inventoryShipment.cartonDimensions_req' />
                          </div>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.pattern">
                            <spring:message code='inventoryShipment.cartonDimensions_format' />
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td class="text-center">
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          {{item.perCartonGrossWeightKg}}
                          <input type="hidden" name="lineItem[{{$id}}].perCartonGrossWeightKg"
                            value="{{item.perCartonGrossWeightKg}}" />
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                            {{item.perCartonGrossWeightKg}}
                            <input type="hidden" name="lineItem[{{$id}}].perCartonGrossWeightKg"
                              value="{{item.perCartonGrossWeightKg}}" />
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input name="lineItem[{{$id}}].perCartonGrossWeightKg" class="form-control"
                              style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.perCartonGrossWeightKg"
                              ng-pattern="/^(0*[1-9][0-9]*(\.[0-9]+)?|0*\.[0-9]*[1-9][0-9]*)$/" value="" type="text" size="10" required></input>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$dirty">
                              <spring:message code='inventoryShipment.grossWeightPerCarton_req' />
                            </div>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.pattern">
                              <spring:message code='inventoryShipment.grossWeightPerCarton_format' />
                            </div>
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input name="lineItem[{{$id}}].perCartonGrossWeightKg" class="form-control"
                            style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.perCartonGrossWeightKg"
                            ng-pattern="/^(0*[1-9][0-9]*(\.[0-9]+)?|0*\.[0-9]*[1-9][0-9]*)$/" value="" type="text" size="10" required></input>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$dirty">
                            <spring:message code='inventoryShipment.grossWeightPerCarton_req' />
                          </div>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.pattern">
                            <spring:message code='inventoryShipment.grossWeightPerCarton_format' />
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td class="text-center">
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          {{item.perCartonUnits}}
                          <input type="hidden" name="lineItem[{{$id}}].perCartonUnits" value="{{item.perCartonUnits}}" />
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                            {{item.perCartonUnits}}
                            <input type="hidden" name="lineItem[{{$id}}].perCartonUnits" value="{{item.perCartonUnits}}" />
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input id="units{{$id}}" name="lineItem[{{$id}}].perCartonUnits" class="form-control"
                              style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.perCartonUnits"
                              ng-pattern="/^[1-9][0-9]*$/" ng-change="calculateQuantity(this)" value="" type="text" size="3"
                              required></input>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$dirty">
                              <spring:message code='inventoryShipment.unitsPerCarton_req' />
                            </div>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.pattern">
                              <spring:message code='inventoryShipment.unitsPerCarton_format' />
                            </div>
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input id="units{{$id}}" name="lineItem[{{$id}}].perCartonUnits" class="form-control"
                            style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.perCartonUnits"
                            ng-pattern="/^[1-9][0-9]*$/" ng-change="calculateQuantity(this)" value="" type="text" size="3"
                            required></input>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$dirty">
                            <spring:message code='inventoryShipment.unitsPerCarton_req' />
                          </div>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.pattern">
                            <spring:message code='inventoryShipment.unitsPerCarton_format' />
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td class="text-center">
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          {{item.cartonCounts}}
                          <input type="hidden" name="lineItem[{{$id}}].cartonCounts" value="{{item.cartonCounts}}" />
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                            {{item.cartonCounts}}
                            <input type="hidden" name="lineItem[{{$id}}].cartonCounts" value="{{item.cartonCounts}}" />
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input id="number{{$id}}" name="lineItem[{{$id}}].cartonCounts" class="form-control"
                              style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.cartonCounts"
                              ng-pattern="/^[1-9][0-9]*$/" ng-change="calculateQuantity(this)" value="" type="text" size="3"
                              required></input>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].cartonCounts'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonCounts'].$dirty">
                              <spring:message code='inventoryShipment.numberOfCartons_req' />
                            </div>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].cartonCounts'].$error.pattern">
                              <spring:message code='inventoryShipment.numberOfCartons_format' />
                            </div>
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input id="number{{$id}}" name="lineItem[{{$id}}].cartonCounts" class="form-control"
                            style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.cartonCounts"
                            ng-pattern="/^[1-9][0-9]*$/" ng-change="calculateQuantity(this)" value="" type="text" size="3"
                            required></input>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].cartonCounts'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonCounts'].$dirty">
                            <spring:message code='inventoryShipment.numberOfCartons_req' />
                          </div>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].cartonCounts'].$error.pattern">
                            <spring:message code='inventoryShipment.numberOfCartons_format' />
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td class="text-center active">
                      {{item.quantity}}
                      <input type="hidden" id="quantity{{$id}}" name="lineItem[{{$id}}].quantity" ng-model="item.quantity"
                        value="{{item.quantity}}" type="text" size="3"></input>
                    </td>
                    <td class="text-center">
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          {{item.unitAmount}}
                          <input type="hidden" name="lineItem[{{$id}}].unitAmount" value="{{item.unitAmount}}" />
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                            {{item.unitAmount}}
                            <input type="hidden" name="lineItem[{{$id}}].unitAmount" value="{{item.unitAmount}}" />
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input id="unitPrice{{$id}}" name="lineItem[{{$id}}].unitAmount" class="form-control"
                              style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.unitAmount"
                              ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" ng-change="calculateAmount(this)" value=""
                              type="text" size="5" required></input>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.required && InventoryShipment['lineItem[{{$id}}].unitAmount'].$dirty">
                              <spring:message code='inventoryShipment.uniPrice_req' />
                            </div>
                            <div class="text-danger"
                              ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.pattern">
                              <spring:message code='inventoryShipment.uniPrice_format' />
                            </div>
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input id="unitPrice{{$id}}" name="lineItem[{{$id}}].unitAmount" class="form-control"
                            style="padding: 6px 2px 6px 2px;text-align:center;" ng-model="item.unitAmount"
                            ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" ng-change="calculateAmount(this)" value="" type="text"
                            size="5" required></input>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.required && InventoryShipment['lineItem[{{$id}}].unitAmount'].$dirty">
                            <spring:message code='inventoryShipment.uniPrice_req' />
                          </div>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.pattern">
                            <spring:message code='inventoryShipment.uniPrice_format' />
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td class="text-center active">
                      {{item.amountUntaxed}}
                      <input type="hidden" id="amount{{$id}}" name="lineItem[{{$id}}].amountUntaxed"
                        ng-model="item.amountUntaxed" value="{{item.amountUntaxed}}" type="text" size="8"></input>
                    </td>
                    <td>
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          {{item.guiinvoiceNumber}}
                          <input type="hidden" name="lineItem[{{$id}}].GUIInvoiceNumber"
                            value="{{item.guiinvoiceNumber}}" />
                        </c:when>
                        <c:otherwise>
                          <input id="GUIInvoiceNumber{{$id}}" name="lineItem[{{$id}}].GUIInvoiceNumber" class="form-control"
                            style="display:inline;width:100%;padding: 6px 0px 6px 0px;text-align:center;"
                            ng-model="item.guiinvoiceNumber" ng-pattern="/^[a-zA-Z0-9]+$/"
                            ng-required="item.isGUIInvoiceIsRequired" ng-readonly="!item.isGUIInvoiceEnable" type="text"
                            size="5"></input><span ng-show="item.isGUIInvoiceIsRequired" class="text-danger">*</span>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].GUIInvoiceNumber'].$error.required && InventoryShipment['lineItem[{{$id}}].GUIInvoiceNumber'].$dirty">
                            <spring:message code='inventoryShipment.GUIInvoiceNumber_req' />
                          </div>
                          <div class="text-danger"
                            ng-show="InventoryShipment['lineItem[{{$id}}].GUIInvoiceNumber'].$error.pattern">
                            <spring:message code='inventoryShipment.GUIInvoiceNumber_format' />
                          </div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                          {{item.guifileName}}
                          <input type="hidden" id="GUIFileName{{$id}}" name="lineItem[{{$id}}].GUIFileName"
                            ng-model="item.guifileName" value="{{item.guifileName}}" type="text" size="50"
                            ng-required="item.isGUIInvoiceIsRequired"></input>
                        </c:when>
                        <c:otherwise>
                          <input type="file" id="GUIInvoiceFile{{$id}}" style="width: 250px;" name="lineItem[{{$id}}].GUIInvoiceFile"
                            ng-model="item.GUIInvoiceFile"
                            ng-disabled="!item.isGUIInvoiceEnable || item.guifileName != null"
                            ng-readonly="!item.isGUIInvoiceEnable || item.guifileName != null"><span
                            ng-show="item.isGUIInvoiceIsRequired" class="text-danger">*</span>
                          <button type="button" class="btn btn-primary btn-sm mt-2"
                            ng-disabled="!item.isGUIInvoiceEnable || item.guifileName != null"
                            ng-click="uploadGUIInvoiceFile($id,item)">
                            <span>
                              <spring:message code='inventoryShipment.upload' /></span>
                          </button>
                          {{item.guifileName}}
                          <button type="button" class="btn btn-link btn-sm mt-2" style="padding:0px 12px;"
                            ng-show="item.guifileName != null" ng-click="removeGUIInvoiceFile(item.guifileName,item)">
                            <span class="glyphicon glyphicon-remove">
                              <spring:message code='inventoryShipment.remove' /></span>
                          </button>
                          <input type="hidden" id="GUIFileName{{$id}}" name="lineItem[{{$id}}].GUIFileName"
                            ng-model="item.guifileName" value="{{item.guifileName}}" type="text" size="50"
                            ng-required="item.isGUIInvoiceIsRequired"></input>
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input id="remove{{$id}}" class="btn btn-default btn-sm mx-3" type="button"
                              value="<spring:message code='inventoryShipment.remove'/>" ng-click="removeLineItem(item)"
                              ng-mouseover="removeSKU=true" ng-mouseleave="removeSKU=false">
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input id="remove{{$id}}" class="btn btn-default btn-sm mx-3" type="button"
                            value="<spring:message code='inventoryShipment.remove'/>" ng-click="removeLineItem(item)"
                            ng-mouseover="removeSKU=true" ng-mouseleave="removeSKU=false">
                        </c:otherwise>
                      </c:choose>
                      <span class="row text-danger" ng-show="!atLeastOneSKUWarning">
                        <spring:message code='inventoryShipment.atLeastOneSKU' /></span>
                    </td>
                  </tr>

                  <!-- 非混箱新增按鈕 -->
                  <tr>
                    <td colspan="13" class="border-0 bg-white">
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input class="btn btn-default btn-sm m-3" id="add" type="button"
                              value="<spring:message code='inventoryShipment.add'/>" style="float: right"
                              ng-click="addLineItem()" />
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input class="btn btn-default btn-sm m-3" id="add" type="button"
                            value="<spring:message code='inventoryShipment.add'/>" style="float: right"
                            ng-click="addLineItem()" />
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>

                  <!-- Mixed content box underneath message -->
                  <tr class="border-top">
                    <td class="text-info bg-white py-4" colspan="13">
                      ---<spring:message code='inventoryShipment.mixedContentBox' />---
                    </td>
                  </tr>

                  <!-- Mixed content box -->
                  <tr id="mixedContentBox{{$id}}" ng-repeat="mixedContentBox in mixedContentBoxes" class="border-0">
                    <td colspan="13" class="p-0 border-0">
                      <table class="table table-striped m-0">
                        <thead class="border-top">
                          <!-- 移除混箱 -->
                          <tr>
                            <td colspan="13" class="border-0 bg-white">
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                  </sec:authorize>
                                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                    <input class="btn btn-default btn-sm my-3" id="add" type="button"
                                      value="<spring:message code='inventoryShipment.removeMixedContentBox' />"
                                      ng-click="removeMixedContentBox(mixedContentBox)" />
                                  </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                  <input class="btn btn-default btn-sm my-3" id="add" type="button"
                                    value="<spring:message code='inventoryShipment.removeMixedContentBox' />"
                                    ng-click="removeMixedContentBox(mixedContentBox)" />
                                </c:otherwise>
                              </c:choose>
                            </td>
                          </tr>
                          <tr>
                            <th style="width: 15%;">
                              <spring:message code='inventoryShipment.sku' /><span class="text-danger">*</span>
                            </th>
                            <th style="width: 5%;">
                              <spring:message code='inventoryShipment.requireRepackaging' /><span
                                class="text-danger">*</span>
                            </th>
                            <th class="text-center" style="width: 5%;">
                              <spring:message code='inventoryShipment.cartonNumber' /><span class="text-danger">*</span>
                            </th>
                            <th class="text-center" style="width: 20%;">
                              <spring:message code='inventoryShipment.cartonDimensions' /><span class="text-danger">*</span>
                            </th>
                            <th class="text-center" style="width: 5%;">
                              <spring:message code='inventoryShipment.grossWeightPerCarton' /><span
                                class="text-danger">*</span>
                            </th>
                            <th class="text-center" style="width: 5%;">
                              <spring:message code='inventoryShipment.unitsPerCarton' /><span class="text-danger">*</span>
                            </th>
                            <th class="text-center" style="width: 5%;">
                              <spring:message code='inventoryShipment.numberOfCartons' /><span class="text-danger">*</span>
                            </th>
                            <th class="text-center" style="width: 5%;">
                              <spring:message code='inventoryShipment.quantity' />
                            </th>
                            <th class="text-center" style="width: 5%;">
                              <spring:message code='inventoryShipment.uniPrice' /><span class="text-danger">*</span>
                            </th>
                            <th class="text-center" style="width: 5%;">
                              <spring:message code='inventoryShipment.amount' />
                            </th>
                            <th style="width: 10%;">
                              <spring:message code='inventoryShipment.GUIInvoiceNumber' />
                            </th>
                            <th style="width: 10%;">
                              <spring:message code='inventoryShipment.GUIInvoice' />
                            </th>
                            <th style="width: 5%;"></th>
                          </tr>
                        </thead>
                        <tbody class="border-0">
                          <tr id="newSKU{{$id}}" ng-repeat="mixedContentBoxItem in mixedContentBox">
                            <td>
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                  <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum"
                                    ng-model="mixedContentBoxItem.boxNum" value="{{mixedContentBoxItem.boxNum}}"
                                    type="text"></input>
                                  <input type="hidden" id="mixedBoxLineSeq{{$id}}" name="lineItem[{{$id}}].mixedBoxLineSeq"
                                    ng-model="mixedContentBoxItem.mixedBoxLineSeq"
                                    value="{{mixedContentBoxItem.mixedBoxLineSeq}}" type="text"></input>
                                  {{mixedContentBoxItem.skuCode}}
                                  <input type="hidden" name="lineItem[{{$id}}].skuCode"
                                    value="{{mixedContentBoxItem.skuCode}}" />
                                  {{mixedContentBoxItem.nameBySupplier}}
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                    <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum"
                                      ng-model="mixedContentBoxItem.boxNum" value="{{mixedContentBoxItem.boxNum}}"
                                      type="text"></input>
                                    <input type="hidden" id="mixedBoxLineSeq{{$id}}"
                                      name="lineItem[{{$id}}].mixedBoxLineSeq"
                                      ng-model="mixedContentBoxItem.mixedBoxLineSeq"
                                      value="{{mixedContentBoxItem.mixedBoxLineSeq}}" type="text"></input>
                                    {{mixedContentBoxItem.skuCode}}
                                    <input type="hidden" name="lineItem[{{$id}}].skuCode"
                                      value="{{mixedContentBoxItem.skuCode}}" />
                                    {{mixedContentBoxItem.nameBySupplier}}
                                  </sec:authorize>
                                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                    <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum"
                                      ng-model="mixedContentBoxItem.boxNum" value="{{mixedContentBoxItem.boxNum}}"
                                      type="text"></input>
                                    <input type="hidden" id="mixedBoxLineSeq{{$id}}"
                                      name="lineItem[{{$id}}].mixedBoxLineSeq"
                                      ng-model="mixedContentBoxItem.mixedBoxLineSeq"
                                      value="{{mixedContentBoxItem.mixedBoxLineSeq}}" type="text"></input>
                                    <select id="SKU{{$id}}" class="form-control sku-selector"
                                      name="lineItem[{{$id}}].skuCode" ng-model="mixedContentBoxItem.skuCode"
                                      ng-change="verifySKUForMixedBox(mixedContentBoxItem,mixedContentBox)" required>
                                      <option value="">--- Select ---</option>
                                      <c:forEach var="ActiveSkuCodeToName" items="${ActiveSkuCodeToNameMap}">
                                        <option value="${ActiveSkuCodeToName.key}">${ActiveSkuCodeToName.key}
                                          ${ActiveSkuCodeToName.value}</option>
                                      </c:forEach>
                                    </select>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].skuCode'].$error.required && InventoryShipment['lineItem[{{$id}}].skuCode'].$dirty">
                                      <spring:message code='inventoryShipment.SKU_req' />
                                    </div>
                                    <div class="text-danger" ng-if="mixedContentBoxItem.isDuplicate">
                                      <spring:message code='inventoryShipment.SKU_dup' />
                                    </div>
                                  </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                  <input type="hidden" id="boxNum{{$id}}" name="lineItem[{{$id}}].boxNum"
                                    ng-model="mixedContentBoxItem.boxNum" value="{{mixedContentBoxItem.boxNum}}"
                                    type="text"></input>
                                  <input type="hidden" id="mixedBoxLineSeq{{$id}}" name="lineItem[{{$id}}].mixedBoxLineSeq"
                                    ng-model="mixedContentBoxItem.mixedBoxLineSeq"
                                    value="{{mixedContentBoxItem.mixedBoxLineSeq}}" type="text"></input>
                                  <select id="SKU{{$id}}" class="form-control sku-selector" name="lineItem[{{$id}}].skuCode"
                                    ng-model="mixedContentBoxItem.skuCode"
                                    ng-change="verifySKUForMixedBox(mixedContentBoxItem,mixedContentBox)" required>
                                    <option value="">--- Select ---</option>
                                    <c:forEach var="ActiveSkuCodeToName" items="${ActiveSkuCodeToNameMap}">
                                      <option value="${ActiveSkuCodeToName.key}">${ActiveSkuCodeToName.key}
                                        ${ActiveSkuCodeToName.value}</option>
                                    </c:forEach>
                                  </select>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].skuCode'].$error.required && InventoryShipment['lineItem[{{$id}}].skuCode'].$dirty">
                                    <spring:message code='inventoryShipment.SKU_req' />
                                  </div>
                                  <div class="text-danger" ng-if="mixedContentBoxItem.isDuplicate">
                                    <spring:message code='inventoryShipment.SKU_dup' />
                                  </div>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <c:choose>
                              <c:when
                                test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                <td
                                  ng-class="{true: 'text-center', false: 'text-center active'}[mixedContentBox.indexOf(mixedContentBoxItem)==0]">
                                  <span ng-if="mixedContentBoxItem.requireRepackaging == true">
                                    <spring:message code='inventoryShipment.true' /></span>
                                  <span ng-if="mixedContentBoxItem.requireRepackaging == false">
                                    <spring:message code='inventoryShipment.false' /></span>
                                  <input type="hidden" name="lineItem[{{$id}}].requireRepackaging"
                                    value="{{mixedContentBoxItem.requireRepackaging}}" />
                                </td>
                              </c:when>
                              <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                  <td
                                    ng-class="{true: 'text-center', false: 'text-center active'}[mixedContentBox.indexOf(mixedContentBoxItem)==0]">
                                    <span ng-if="mixedContentBoxItem.requireRepackaging == true">
                                      <spring:message code='inventoryShipment.true' /></span>
                                    <span ng-if="mixedContentBoxItem.requireRepackaging == false">
                                      <spring:message code='inventoryShipment.false' /></span>
                                    <input type="hidden" name="lineItem[{{$id}}].requireRepackaging"
                                      value="{{mixedContentBoxItem.requireRepackaging}}" />
                                  </td>
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                  <td
                                    ng-class="{true: 'text-center', false: 'text-center active'}[mixedContentBox.indexOf(mixedContentBoxItem)==0]">
                                    <input id="requireRepackaging{{$id}}" type="checkbox"
                                      name="lineItem[{{$id}}].requireRepackaging"
                                      ng-model="mixedContentBoxItem.requireRepackaging" ng-readonly="!$first"
                                      ng-change="assignRequireRepackaging(mixedContentBox)">
                                  </td>
                                </sec:authorize>
                              </c:when>
                              <c:otherwise>
                                <td
                                  ng-class="{true: 'text-center', false: 'text-center active'}[mixedContentBox.indexOf(mixedContentBoxItem)==0]">
                                  <input id="requireRepackaging{{$id}}" type="checkbox"
                                    name="lineItem[{{$id}}].requireRepackaging"
                                    ng-model="mixedContentBoxItem.requireRepackaging" ng-readonly="!$first"
                                    ng-change="assignRequireRepackaging(mixedContentBox)">
                                </td>
                              </c:otherwise>
                            </c:choose>
                            <td class="text-center active">
                              {{mixedContentBoxItem.cartonNumberStart}} ~ {{mixedContentBoxItem.cartonNumberEnd}}
                            </td>
                            <td
                              ng-class="{true: 'text-center', false: 'text-center active'}[mixedContentBox.indexOf(mixedContentBoxItem)==0]">
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                  {{mixedContentBoxItem.cartonDimensionCm1}} x {{mixedContentBoxItem.cartonDimensionCm2}} x
                                  {{mixedContentBoxItem.cartonDimensionCm3}}
                                  <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm1"
                                    value="{{mixedContentBoxItem.cartonDimensionCm1}}" />
                                  <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm2"
                                    value="{{mixedContentBoxItem.cartonDimensionCm2}}" />
                                  <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm3"
                                    value="{{mixedContentBoxItem.cartonDimensionCm3}}" />
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                    {{mixedContentBoxItem.cartonDimensionCm1}} x {{mixedContentBoxItem.cartonDimensionCm2}}
                                    x {{mixedContentBoxItem.cartonDimensionCm3}}
                                    <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm1"
                                      value="{{mixedContentBoxItem.cartonDimensionCm1}}" />
                                    <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm2"
                                      value="{{mixedContentBoxItem.cartonDimensionCm2}}" />
                                    <input type="hidden" name="lineItem[{{$id}}].cartonDimensionCm3"
                                      value="{{mixedContentBoxItem.cartonDimensionCm3}}" />
                                  </sec:authorize>
                                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                    <input name="lineItem[{{$id}}].cartonDimensionCm1" class="form-control"
                                      style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:right;"
                                      ng-model="mixedContentBoxItem.cartonDimensionCm1" ng-readonly="!$first"
                                      ng-change="assignCartonDimension(mixedContentBox)"
                                      ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text" size="3" required> x
                                    <input name="lineItem[{{$id}}].cartonDimensionCm2" class="form-control"
                                      style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:right;"
                                      ng-model="mixedContentBoxItem.cartonDimensionCm2" ng-readonly="!$first"
                                      ng-change="assignCartonDimension(mixedContentBox)"
                                      ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text" size="3" required> x
                                    <input name="lineItem[{{$id}}].cartonDimensionCm3" class="form-control"
                                      style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:right;"
                                      ng-model="mixedContentBoxItem.cartonDimensionCm3" ng-readonly="!$first"
                                      ng-change="assignCartonDimension(mixedContentBox)"
                                      ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text" size="3" required>
                                    <div class="text-danger"
                                      ng-show="(InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$dirty)">
                                      <spring:message code='inventoryShipment.cartonDimensions_req' />
                                    </div>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.pattern">
                                      <spring:message code='inventoryShipment.cartonDimensions_format' />
                                    </div>
                                  </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                  <input name="lineItem[{{$id}}].cartonDimensionCm1" class="form-control"
                                    style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:right;"
                                    ng-model="mixedContentBoxItem.cartonDimensionCm1" ng-readonly="!$first"
                                    ng-change="assignCartonDimension(mixedContentBox)"
                                    ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text" size="3" required> x
                                  <input name="lineItem[{{$id}}].cartonDimensionCm2" class="form-control"
                                    style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:right;"
                                    ng-model="mixedContentBoxItem.cartonDimensionCm2" ng-readonly="!$first"
                                    ng-change="assignCartonDimension(mixedContentBox)"
                                    ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text" size="3" required> x
                                  <input name="lineItem[{{$id}}].cartonDimensionCm3" class="form-control"
                                    style="width:20%;display: inline;padding: 6px 2px 6px 2px;text-align:right;"
                                    ng-model="mixedContentBoxItem.cartonDimensionCm3" ng-readonly="!$first"
                                    ng-change="assignCartonDimension(mixedContentBox)"
                                    ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/" value="" type="text" size="3" required>
                                  <div class="text-danger"
                                    ng-show="(InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$dirty) || (InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.required && InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$dirty)">
                                    <spring:message code='inventoryShipment.cartonDimensions_req' />
                                  </div>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].cartonDimensionCm1'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm2'].$error.pattern || InventoryShipment['lineItem[{{$id}}].cartonDimensionCm3'].$error.pattern">
                                    <spring:message code='inventoryShipment.cartonDimensions_format' />
                                  </div>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td
                              ng-class="{true: 'text-center', false: 'text-center active'}[mixedContentBox.indexOf(mixedContentBoxItem)==0]">
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                  {{mixedContentBoxItem.perCartonGrossWeightKg}}
                                  <input type="hidden" name="lineItem[{{$id}}].perCartonGrossWeightKg"
                                    value="{{mixedContentBoxItem.perCartonGrossWeightKg}}" />
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                    {{mixedContentBoxItem.perCartonGrossWeightKg}}
                                    <input type="hidden" name="lineItem[{{$id}}].perCartonGrossWeightKg"
                                      value="{{mixedContentBoxItem.perCartonGrossWeightKg}}" />
                                  </sec:authorize>
                                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                    <input name="lineItem[{{$id}}].perCartonGrossWeightKg" class="form-control"
                                      style="padding: 6px 2px 6px 2px;text-align:right;"
                                      ng-model="mixedContentBoxItem.perCartonGrossWeightKg" ng-readonly="!$first"
                                      ng-change="assignPerCartonGrossWeightKg(mixedContentBox)"
                                      ng-pattern="/^(0*[1-9][0-9]*(\.[0-9]+)?|0*\.[0-9]*[1-9][0-9]*)$/" value="" type="text" size="10"
                                      required></input>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$dirty">
                                      <spring:message code='inventoryShipment.grossWeightPerCarton_req' />
                                    </div>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.pattern">
                                      <spring:message code='inventoryShipment.grossWeightPerCarton_format' />
                                    </div>
                                  </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                  <input name="lineItem[{{$id}}].perCartonGrossWeightKg" class="form-control"
                                    style="padding: 6px 2px 6px 2px;text-align:right;"
                                    ng-model="mixedContentBoxItem.perCartonGrossWeightKg" ng-readonly="!$first"
                                    ng-change="assignPerCartonGrossWeightKg(mixedContentBox)"
                                    ng-pattern="/^(0*[1-9][0-9]*(\.[0-9]+)?|0*\.[0-9]*[1-9][0-9]*)$/" value="" type="text" size="10"
                                    required></input>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$dirty">
                                    <spring:message code='inventoryShipment.grossWeightPerCarton_req' />
                                  </div>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].perCartonGrossWeightKg'].$error.pattern">
                                    <spring:message code='inventoryShipment.grossWeightPerCarton_format' />
                                  </div>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td class="text-center">
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                  {{mixedContentBoxItem.perCartonUnits}}
                                  <input type="hidden" name="lineItem[{{$id}}].perCartonUnits"
                                    value="{{mixedContentBoxItem.perCartonUnits}}" />
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                    {{mixedContentBoxItem.perCartonUnits}}
                                    <input type="hidden" name="lineItem[{{$id}}].perCartonUnits"
                                      value="{{mixedContentBoxItem.perCartonUnits}}" />
                                  </sec:authorize>
                                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                    <input id="units{{$id}}" name="lineItem[{{$id}}].perCartonUnits" class="form-control"
                                      style="padding: 6px 2px 6px 2px;text-align:right;"
                                      ng-model="mixedContentBoxItem.perCartonUnits" ng-pattern="/^[1-9][0-9]*$/"
                                      ng-change="calculateMixedContentBoxQuantity(this)" value="" type="text" size="3"
                                      required></input>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$dirty">
                                      <spring:message code='inventoryShipment.unitsPerCarton_req' />
                                    </div>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.pattern">
                                      <spring:message code='inventoryShipment.unitsPerCarton_format' />
                                    </div>
                                  </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                  <input id="units{{$id}}" name="lineItem[{{$id}}].perCartonUnits" class="form-control"
                                    style="padding: 6px 2px 6px 2px;text-align:right;"
                                    ng-model="mixedContentBoxItem.perCartonUnits" ng-pattern="/^[1-9][0-9]*$/"
                                    ng-change="calculateMixedContentBoxQuantity(this)" value="" type="text" size="3"
                                    required></input>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.required && InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$dirty">
                                    <spring:message code='inventoryShipment.unitsPerCarton_req' />
                                  </div>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].perCartonUnits'].$error.pattern">
                                    <spring:message code='inventoryShipment.unitsPerCarton_format' />
                                  </div>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td class="text-center active">
                              {{$first?  mixedContentBoxItem.cartonCounts = mixedContentBoxCartonCountFirst : mixedContentBoxItem.cartonCounts = mixedContentBoxCartonCountOther}}
                              <input type="hidden" id="number{{$id}}" name="lineItem[{{$id}}].cartonCounts"
                                class="form-control" ng-model="mixedContentBoxItem.cartonCounts"
                                value="{{mixedContentBoxItem.cartonCounts}}" type="text"></input>
                            </td>
                            <td class="text-center active">
                              {{mixedContentBoxItem.quantity}}
                              <input type="hidden" id="quantity{{$id}}" name="lineItem[{{$id}}].quantity"
                                ng-model="mixedContentBoxItem.quantity" value="{{mixedContentBoxItem.quantity}}" type="text"
                                size="3"></input>
                            </td>
                            <td class="text-center">
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                  {{mixedContentBoxItem.unitAmount}}
                                  <input type="hidden" name="lineItem[{{$id}}].unitAmount"
                                    value="{{mixedContentBoxItem.unitAmount}}" />
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                    {{mixedContentBoxItem.unitAmount}}
                                    <input type="hidden" name="lineItem[{{$id}}].unitAmount"
                                      value="{{mixedContentBoxItem.unitAmount}}" />
                                  </sec:authorize>
                                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                    <input id="unitPrice{{$id}}" name="lineItem[{{$id}}].unitAmount" class="form-control"
                                      style="padding: 6px 2px 6px 2px;text-align:right;"
                                      ng-model="mixedContentBoxItem.unitAmount" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/"
                                      ng-change="calculateMixedContentBoxAmount(this)" value="" type="text" size="5"
                                      required></input>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.required && InventoryShipment['lineItem[{{$id}}].unitAmount'].$dirty">
                                      <spring:message code='inventoryShipment.uniPrice_req' />
                                    </div>
                                    <div class="text-danger"
                                      ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.pattern">
                                      <spring:message code='inventoryShipment.uniPrice_format' />
                                    </div>
                                  </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                  <input id="unitPrice{{$id}}" name="lineItem[{{$id}}].unitAmount" class="form-control"
                                    style="padding: 6px 2px 6px 2px;text-align:right;"
                                    ng-model="mixedContentBoxItem.unitAmount" ng-pattern="/^(?:[1-9]\d*)?(?:\.\d+)?$/"
                                    ng-change="calculateMixedContentBoxAmount(this)" value="" type="text" size="5"
                                    required></input>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.required && InventoryShipment['lineItem[{{$id}}].unitAmount'].$dirty">
                                    <spring:message code='inventoryShipment.uniPrice_req' />
                                  </div>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].unitAmount'].$error.pattern">
                                    <spring:message code='inventoryShipment.uniPrice_format' />
                                  </div>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td class="text-center active">
                              {{mixedContentBoxItem.amountUntaxed}}
                              <input type="hidden" id="amount{{$id}}" name="lineItem[{{$id}}].amountUntaxed"
                                ng-model="mixedContentBoxItem.amountUntaxed" value="{{mixedContentBoxItem.amountUntaxed}}"
                                type="text" size="8"></input>
                            </td>
                            <td>
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                  {{mixedContentBoxItem.guiinvoiceNumber}}
                                  <input type="hidden" name="lineItem[{{$id}}].GUIInvoiceNumber"
                                    value="{{mixedContentBoxItem.guiinvoiceNumber}}" />
                                </c:when>
                                <c:otherwise>
                                  <input id="GUIInvoiceNumber{{$id}}" name="lineItem[{{$id}}].GUIInvoiceNumber"
                                    class="form-control"
                                    style="display:inline;width:100%;padding: 6px 0px 6px 0px;text-align:center;"
                                    ng-required="mixedContentBoxItem.isGUIInvoiceIsRequired"
                                    ng-model="mixedContentBoxItem.guiinvoiceNumber" ng-pattern="/^[a-zA-Z0-9]+$/"
                                    ng-readonly="!mixedContentBoxItem.isGUIInvoiceEnable" type="text" size="5"></input><span
                                    ng-show="mixedContentBoxItem.isGUIInvoiceIsRequired" class="text-danger">*</span>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].GUIInvoiceNumber'].$error.required && InventoryShipment['lineItem[{{$id}}].GUIInvoiceNumber'].$dirty">
                                    <spring:message code='inventoryShipment.GUIInvoiceNumber_req' />
                                  </div>
                                  <div class="text-danger"
                                    ng-show="InventoryShipment['lineItem[{{$id}}].GUIInvoiceNumber'].$error.pattern">
                                    <spring:message code='inventoryShipment.GUIInvoiceNumber_format' />
                                  </div>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td>
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                  {{mixedContentBoxItem.guifileName}}
                                  <input type="hidden" id="GUIFileName{{$id}}" name="lineItem[{{$id}}].GUIFileName"
                                    ng-model="mixedContentBoxItem.guifileName" value="{{mixedContentBoxItem.guifileName}}"
                                    type="text" size="50" ng-required="mixedContentBoxItem.isGUIInvoiceIsRequired"></input>
                                </c:when>
                                <c:otherwise>
                                  <input type="file" id="GUIInvoiceFile{{$id}}" style="width: 250px;" name="lineItem[{{$id}}].GUIInvoiceFile"
                                    ng-model="mixedContentBoxItem.GUIInvoiceFile"
                                    ng-disabled="!mixedContentBoxItem.isGUIInvoiceEnable || mixedContentBoxItem.guifileName != null"
                                    ng-readonly="!mixedContentBoxItem.isGUIInvoiceEnable || mixedContentBoxItem.guifileName != null"><span
                                    ng-show="mixedContentBoxItem.isGUIInvoiceIsRequired" class="text-danger">*</span>
                                  <button type="button" class="btn btn-primary btn-sm mt-2"
                                    ng-disabled="!mixedContentBoxItem.isGUIInvoiceEnable || mixedContentBoxItem.guifileName != null"
                                    ng-click="uploadGUIInvoiceFileInMixedBox($id,mixedContentBox,mixedContentBoxItem)">
                                    <span>
                                      <spring:message code='inventoryShipment.upload' /></span>
                                  </button>
                                  {{mixedContentBoxItem.guifileName}}
                                  <button type="button" class="btn btn-link btn-sm mt-2" style="padding:0px 12px;"
                                    ng-show="mixedContentBoxItem.guifileName != null"
                                    ng-click="removeGUIInvoiceFileInMixedBox(mixedContentBoxItem.guifileName,mixedContentBox,mixedContentBoxItem)">
                                    <span class="glyphicon glyphicon-remove">
                                      <spring:message code='inventoryShipment.remove' /></span>
                                  </button>
                                  <input type="hidden" id="GUIFileName{{$id}}" name="lineItem[{{$id}}].GUIFileName"
                                    ng-model="mixedContentBoxItem.guifileName" value="{{mixedContentBoxItem.guifileName}}"
                                    type="text" size="50" ng-required="mixedContentBoxItem.isGUIInvoiceIsRequired"></input>
                                </c:otherwise>
                              </c:choose>
                            </td>

                            <!-- 混箱, 移除 -->
                            <td>
                              <c:choose>
                                <c:when
                                  test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                                </c:when>
                                <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                  <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                  </sec:authorize>
                                  <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                    <input id="remove{{$id}}" class="btn btn-default btn-sm mx-3" type="button"
                                      value="<spring:message code='inventoryShipment.remove'/>"
                                      ng-click="removeMixedContentBoxLineItem(mixedContentBox,mixedContentBoxItem)"
                                      ng-mouseover="removeSKU=true" ng-mouseleave="removeSKU=false">
                                  </sec:authorize>
                                </c:when>
                                <c:otherwise>
                                  <input id="remove{{$id}}" class="btn btn-default btn-sm mx-3" type="button"
                                    value="<spring:message code='inventoryShipment.remove'/>"
                                    ng-click="removeMixedContentBoxLineItem(mixedContentBox,mixedContentBoxItem)"
                                    ng-mouseover="removeSKU=true" ng-mouseleave="removeSKU=false">
                                </c:otherwise>
                              </c:choose>
                              <span class="row text-danger" ng-show="!atLeastOneSKUWarning">
                                <spring:message code='inventoryShipment.atLeastOneSKU' /></span>
                            </td>
                          </tr>
                        </tbody>

                        <!-- 混箱, 新增 -->
                        <tr>
                          <td colspan="13" class="border-0 bg-white">
                            <c:choose>
                              <c:when
                                test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                              </c:when>
                              <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                                <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                                </sec:authorize>
                                <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                                  <input class="btn btn-default btn-sm m-3" id="add" type="button"
                                    value="<spring:message code='inventoryShipment.add'/>" style="float: right"
                                    ng-click="addMixedContentBoxLineItem(mixedContentBox)" />
                                </sec:authorize>
                              </c:when>
                              <c:otherwise>
                                <input class="btn btn-default btn-sm m-3" id="add" type="button"
                                  value="<spring:message code='inventoryShipment.add'/>" style="float: right"
                                  ng-click="addMixedContentBoxLineItem(mixedContentBox)" />
                              </c:otherwise>
                            </c:choose>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <!-- 新增混箱 -->
                  <tr>
                    <td colspan="13" class="border-bottom-0 bg-white">
                      <c:choose>
                        <c:when
                          test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                        </c:when>
                        <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                          <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                          </sec:authorize>
                          <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                            <input class="btn btn-default btn-sm m-3" id="add" type="button"
                              value="<spring:message code='inventoryShipment.addMixedContentBox' />" style="float: right"
                              ng-click="addMixedContentBox()" />
                          </sec:authorize>
                        </c:when>
                        <c:otherwise>
                          <input class="btn btn-default btn-sm m-3" id="add" type="button"
                            value="<spring:message code='inventoryShipment.addMixedContentBox' />" style="float: right"
                            ng-click="addMixedContentBox()" />
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <h5 class="font-weight-bolder mb-4">SUMMARY</h5>

          <!-- 小計 -->
          <div class="sum-list">
            <ul>
              <li><spring:message code='inventoryShipment.numberOfCartons' /></li>
              <li><spring:message code='inventoryShipment.quantity' /></li>
              <li><spring:message code='inventoryShipment.subtotal' /></li>
              <li>
                <c:choose>
                  <c:when
                    test="${InventoryShipment.status eq 'SHPT_CONFIRMED' || InventoryShipment.status eq 'SHPT_AWAIT_PICK_UP' || InventoryShipment.status eq 'SHPT_IN_TRANSIT' || InventoryShipment.status eq 'SHPT_RECEIVING' || InventoryShipment.status eq 'SHPT_RECEIVED' || InventoryShipment.status eq 'SHPT_EXCEPTION'}">
                    <spring:message code='inventoryShipment.taxAmount' /> ( ${InventoryShipment.salesTaxPercentage}
                    <form:hidden path="salesTaxPercentage" />%)
                  </c:when>
                  <c:when test="${InventoryShipment.status eq 'SHPT_PLANNING'}">
                    <sec:authorize access="hasAnyRole('${auth['SUPPLIER_USER']}')">
                      <spring:message code='inventoryShipment.taxAmount' /> ( ${InventoryShipment.salesTaxPercentage}
                      <form:hidden path="salesTaxPercentage" />%)
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('${auth['DRS_USER']}')">
                      <spring:message code='inventoryShipment.taxAmount' /> (
                      <form:input id="salesTaxPercentage" class="form-control"
                        style="display:inline;width:25%;padding: 6px 2px 6px 2px;text-align:right;"
                        path="salesTaxPercentage" ng-model="salesTaxPercentage"
                        ng-pattern="/^(?:[0-9]\d*)?(?:\.\d+)?$/" ng-change="changeSalesTaxPercentage()"
                        required="required" /> %)
                    </sec:authorize>
                  </c:when>
                  <c:otherwise>
                    <spring:message code='inventoryShipment.taxAmount' /><span class="text-danger">*</span> (
                    <form:input id="salesTaxPercentage" class="form-control"
                      style="display:inline;width:25%;padding: 6px 2px 6px 2px;text-align:right;"
                      path="salesTaxPercentage" ng-model="salesTaxPercentage" ng-pattern="/^(?:[0-9]\d*)?(?:\.\d+)?$/"
                      ng-change="changeSalesTaxPercentage()" required="required" /> %)
                  </c:otherwise>
                </c:choose>
                <div class="text-danger"
                  ng-show="InventoryShipment.salesTaxPercentage.$error.required && InventoryShipment.salesTaxPercentage.$dirty">
                  <spring:message code='inventoryShipment.salesTaxPercentage_req' />
                </div>
                <div class="text-danger" ng-show="InventoryShipment.salesTaxPercentage.$error.pattern">
                  <spring:message code="inventoryShipment.salesTaxPercentage_format" />
                </div>
              </li>
              <li><spring:message code='inventoryShipment.total' /></li>
            </ul>
            <ul>
              <li>{{totalCartonCount}}</li>
              <li>{{totalQuantity}}</li>
              <li>
                {{subtotal}}
                <form:hidden id="subtotal" path="subtotal" ng-model="subtotal" size="8" value="{{subtotal}}" />
              </li>
              <li>
                {{taxAmount}}
                <form:hidden id="taxAmount" path="salesTax" ng-model="taxAmount" size="8" value="{{taxAmount}}" />
              </li>
              <li>
                {{totalAmount}}
                <form:hidden id="totalAmount" path="total" ng-model="totalAmount" size="8" value="{{totalAmount}}" />
              </li>
            </ul>
          </div>

          <hr class="mt-5 mb-4">

          <!-- Bottom right button group -->
          <c:choose>
            <c:when test="${type eq 'Create'}">
              <div style="float: right">
                <input class="btn btn-primary drs-btn-primary mr-3" type="submit" onclick="document.pressed=this.name" name="saveDraft"
                  value="<spring:message code="inventoryShipment.saveDraft" />" ng-disabled='duplicated || !noSKU ||
                FCAPriceNoneEqual || InventoryShipment.$invalid' />
              </div>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${status eq 'DRAFT'}">
                  <div style="float: right">
                    <input class="btn btn-primary drs-btn-primary mr-3" type="submit" onclick="document.pressed=this.name" name="updateDraft"
                      value="<spring:message code="inventoryShipment.submit" />" ng-disabled='duplicated || !noSKU ||
                    FCAPriceNoneEqual || InventoryShipment.$invalid' />
                    <a class="btn btn-link drs-btn-primary mr-3"
                      href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}">
                      <spring:message code="inventoryShipment.cancel" /></a>
                  </div>
                </c:when>
                <c:otherwise>
                  <div style="float: right">
                    <sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_DEPOSITION']}')">
                      <a class="btn btn-link drs-btn-primary mr-3" href="<c:url value="/resources/files/G3-deposition-GUI.pdf"/>"
                        target="_blank">
                        <spring:message code="inventoryShipment.deposition" />
                      </a>
                    </sec:authorize>
                    <input class="btn btn-primary drs-btn-primary mr-3" type="submit" onclick="document.pressed=this.name" name="update"
                      value="<spring:message code="inventoryShipment.submit" />" ng-disabled='duplicated || !noSKU ||
                    FCAPriceNoneEqual || InventoryShipment.$invalid' />
                    <a class="btn btn-link drs-btn-primary mr-3"
                      href="${pageContext.request.contextPath}/InventoryShipments/${InventoryShipment.name}">
                      <spring:message code="inventoryShipment.cancel" /></a>
                  </div>
                </c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
        </form:form>

        <!-- Bottom left warning message -->
        <div class="row">
          <div class="col-md-12 text-danger">
            <spring:message code='inventoryShipment.note' />
          </div>
        </div>
      </div>
    </div>

  <!-- Container end -->
  </div>
</div>
