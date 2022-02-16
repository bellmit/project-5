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
      <c:when test="${type eq 'Create'}">
        <spring:message code="issue.createNew" /> - DRS
      </c:when>
      <c:otherwise>
        <spring:message code="issue.editIssue" /> - DRS
      </c:otherwise>
    </c:choose>
  </title>
  <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css"
    rel="stylesheet" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
  <script>
    angular.module('issue', []).controller('issueCtrl', function ($scope) {
      var currentURL = document.URL;
      $scope.baseProductLineItems = [];
      $scope.SKULineItems = [];
      $scope.issueNameLineItems = [];
      $scope.relatedProductType = false;
      if (currentURL.indexOf("edit") > -1) {
        var IssueJson = ${ IssueJson };
        $scope.categoryName = IssueJson.categoryId;
        $scope.issueType = IssueJson.typeId;
        $scope.status = IssueJson.status;
        if (IssueJson.supplierKcode != null) $scope.relatedProductType = true;
        var issueTypeList = null;
        $.ajax({
          type: 'get',
          url: '${pageContext.request.contextPath}/Issues/getIssueTypeList/',
          contentType: "application/json; charset=utf-8",
          data: { categoryId: Number($scope.categoryName) },
          dataType: "json",
          success: function (data) {
            issueTypeList = data;
            emptyList("issueType");
            var selectIssueType = document.getElementById("issueType");
            var optIssueType = document.createElement("option");
            optIssueType.value = "";
            optIssueType.textContent = "--- Select ---";
            selectIssueType.appendChild(optIssueType);
            for (var issueType in issueTypeList) {
              if (issueTypeList.hasOwnProperty(issueType)) {
                optIssueType = document.createElement("option");
                optIssueType.value = issueType;
                optIssueType.textContent = issueTypeList[issueType];
                selectIssueType.appendChild(optIssueType);
              }
            }
            $("#issueType").val(IssueJson.typeId);
          }
        });
        var languageList = IssueJson.language;
        var issueNameList = IssueJson.issueName;
        var languageToNameMap = IssueJson.languageToNameMap
        for (var key in issueNameList) {
          if (issueNameList.hasOwnProperty(key)) {
            $scope.issueNameLineItems.push({ language: languageList[key], name: issueNameList[key] });
          }
        }
        if (IssueJson.relatedProductBaseCodeList != null) {
          $("#relatedProduct-BP").prop('checked', true);
          $("#sku").hide();
          $("#baseProduct").show();
          var relatedProductBaseCodeList = IssueJson.relatedProductBaseCodeList;
          for (var baseCode in relatedProductBaseCodeList) {
            if (relatedProductBaseCodeList.hasOwnProperty(baseCode)) {
              $scope.baseProductLineItems.push({ baseProduct: relatedProductBaseCodeList[baseCode] });
            }
          }
          var supplierKcode = IssueJson.supplierKcode;
          var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductBaseCodeToSupplierNameMapInIssue/';
          var productBaseCodeToSupplierNameMap = null;
          $.ajax({
            type: 'get',
            url: ajaxUrl,
            contentType: "application/json; charset=utf-8",
            data: { supplierKcode: supplierKcode },
            dataType: "json",
            success: function (data) {
              productBaseCodeToSupplierNameMap = data;
              for (i = 0; i < $scope.baseProductLineItems.length; i++) {
                emptyList("baseProduct" + i);
                if (Object.keys(productBaseCodeToSupplierNameMap).length > 0) {
                  var selectBaseCode = document.getElementById("baseProduct" + i);
                  var optBaseCode = document.createElement("option");
                  optBaseCode.value = "";
                  optBaseCode.textContent = "--- Select Base Product ---";
                  selectBaseCode.appendChild(optBaseCode);
                }
                for (var key in productBaseCodeToSupplierNameMap) {
                  optBaseCode = document.createElement("option");
                  optBaseCode.value = key;
                  optBaseCode.textContent = key + " " + productBaseCodeToSupplierNameMap[key];
                  selectBaseCode.appendChild(optBaseCode);
                }
                selectBaseCode.value = IssueJson['relatedProductBaseCodeList'][i];
              }
            }
          });
        } else if (IssueJson.relatedProductSkuCodeList != null) {
          $("#relatedProduct-SKU").prop('checked', true);
          $("#sku").show();
          $("#baseProduct").hide();
          var relatedProductSkuCodeList = IssueJson.relatedProductSkuCodeList;
          for (var skuCode in relatedProductSkuCodeList) {
            if (relatedProductSkuCodeList.hasOwnProperty(skuCode)) {
              $scope.SKULineItems.push({ sku: relatedProductSkuCodeList[skuCode] });
            }
          }
          var supplierKcode = IssueJson.supplierKcode;
          var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductSkuCodeToSupplierNameMapInIssue/';
          var productSkuCodeToSupplierNameMap = null;
          $.ajax({
            type: 'get',
            url: ajaxUrl,
            contentType: "application/json; charset=utf-8",
            data: { supplierKcode: supplierKcode },
            dataType: "json",
            success: function (data) {
              productSkuCodeToSupplierNameMap = data;
              for (i = 0; i < $scope.SKULineItems.length; i++) {
                emptyList("sku" + i);
                if (Object.keys(productSkuCodeToSupplierNameMap).length > 0) {
                  var selectSku = document.getElementById("sku" + i);
                  var optSku = document.createElement("option");
                  optSku.value = "";
                  optSku.textContent = "--- Select SKU ---";
                  selectSku.appendChild(optSku);
                }
                for (var key in productSkuCodeToSupplierNameMap) {
                  optSku = document.createElement("option");
                  optSku.value = key;
                  optSku.textContent = key + " " + productSkuCodeToSupplierNameMap[key];
                  selectSku.appendChild(optSku);
                }
                selectSku.value = IssueJson['relatedProductSkuCodeList'][i];
              }
            }
          });
        }
      } else {
        $scope.supplier = '';
        $scope.baseProductLineItems = [];
        $scope.SKULineItems = [];
        $scope.issueNameLineItems = [{ language: '', name: '' }];
      }

      $scope.getRelatedProductList = function () {
        var id = document.querySelector('input[name="relatedProduct"]:checked').id;
        var supplierKcode = document.getElementById('supplier').value;
        if (supplierKcode == "") {
          $scope.relatedProductType = false;
          $scope.baseProductLineItems = [];
          $scope.SKULineItems = [];
        } else {
          $scope.relatedProductType = true;
          if (id == "relatedProduct-BP") {
            $scope.baseProductLineItems = [{ baseProduct: '' }];
            getProductBaseCodeToSupplierNameMap(supplierKcode);
          } else if (id == "relatedProduct-SKU") {
            $scope.SKULineItems = [{ sku: '' }];
            getProductSkuCodeToSupplierNameMap(supplierKcode);
          }
        }
      };

      $scope.checkRelatedProduct = function () {
        var id = document.querySelector('input[name="relatedProduct"]:checked').id;
        var supplierKcode = document.getElementById('supplier').value;
        if (id == "relatedProduct-BP") {
          for (var i = $scope.SKULineItems.length - 1; i >= 0; i--) {
            $scope.SKULineItems.splice(i, 1);
          }
          $('#relatedProductSkuCodeList').val("");
          document.getElementById("sku").style.display = "none";
          document.getElementById("baseProduct").style.display = "block";
          $scope.baseProductLineItems = [{ baseProduct: '' }];
          getProductBaseCodeToSupplierNameMap(supplierKcode);
        } else if (id == "relatedProduct-SKU") {
          for (var i = $scope.baseProductLineItems.length - 1; i >= 0; i--) {
            $scope.baseProductLineItems.splice(i, 1);
          }
          $('#relatedProductBaseCodeList').val("");
          document.getElementById("baseProduct").style.display = "none";
          document.getElementById("sku").style.display = "block";
          $scope.SKULineItems = [{ sku: '' }];
          getProductSkuCodeToSupplierNameMap(supplierKcode);
        }
      };

      $scope.addBaseProductLineItem = function () {
        $scope.baseProductLineItems.push({ baseProduct: '' });
        updateProductBaseCodeToSupplierNameMap($scope.baseProductLineItems.length - 1);
      };

      $scope.removeBaseProductLineItem = function (baseProductLineItem) {
        if ($scope.baseProductLineItems.length > 1) {
          var idx = $scope.baseProductLineItems.indexOf(baseProductLineItem);
          $scope.baseProductLineItems.splice(idx, 1);
        }
        $scope.updateBaseProductList();
      };

      $scope.updateBaseProductList = function () {
        var baseProductArray = new Array();
        for (var key in $scope.baseProductLineItems) {
          if ($scope.baseProductLineItems.hasOwnProperty(key)) {
            if ($scope.baseProductLineItems[key]['baseProduct'] !== "") {
              baseProductArray.push($scope.baseProductLineItems[key]['baseProduct']);
            }
          }
        }
        document.getElementById('relatedProductBaseCodeList').value = baseProductArray.toString();
      };

      $scope.addSKULineItem = function () {
        $scope.SKULineItems.push({ sku: '' });
        updateProductSkuCodeToSupplierNameMap($scope.SKULineItems.length - 1);
      };

      $scope.removeSKULineItem = function (SKULineItem) {
        if ($scope.SKULineItems.length > 1) {
          var idx = $scope.SKULineItems.indexOf(SKULineItem);
          $scope.SKULineItems.splice(idx, 1);
        }
        $scope.updateSkuList();
      };

      $scope.updateSkuList = function () {
        var skuArray = new Array();
        for (var key in $scope.SKULineItems) {
          if ($scope.SKULineItems.hasOwnProperty(key)) {
            if ($scope.SKULineItems[key]['sku'] !== "") {
              skuArray.push($scope.SKULineItems[key]['sku']);
            }
          }
        }
        document.getElementById('relatedProductSkuCodeList').value = skuArray.toString();
      };

      function emptyList(id) {
        document.getElementById(id).options.length = 0;
      }

      function getProductBaseCodeToSupplierNameMap(supplierKcodeParam) {
        var supplierKcode = supplierKcodeParam;
        var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductBaseCodeToSupplierNameMapInIssue/';
        var productBaseCodeToSupplierNameMap = null;
        $.ajax({
          type: 'get',
          url: ajaxUrl,
          contentType: "application/json; charset=utf-8",
          data: { supplierKcode: supplierKcode },
          dataType: "json",
          success: function (data) {
            productBaseCodeToSupplierNameMap = data;
            for (i = 0; i < $scope.baseProductLineItems.length; i++) {
              emptyList("baseProduct" + i);
              if (Object.keys(productBaseCodeToSupplierNameMap).length > 0) {
                var selectBaseProduct = document.getElementById("baseProduct" + i);
                var optBaseProduct = document.createElement("option");
                optBaseProduct.value = "";
                optBaseProduct.textContent = "--- Select Base Product ---";
                selectBaseProduct.appendChild(optBaseProduct);
              }
              for (var key in productBaseCodeToSupplierNameMap) {
                optBaseProduct = document.createElement("option");
                optBaseProduct.value = key;
                optBaseProduct.textContent = key + " " + productBaseCodeToSupplierNameMap[key];
                selectBaseProduct.appendChild(optBaseProduct);
              }
            }//end for(i = 0; i < $scope.baseProductLineItems.length; i++)
          }
        });
      }

      function updateProductBaseCodeToSupplierNameMap(id) {
        var supplierKcode = document.getElementById('supplier').value;
        var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductBaseCodeToSupplierNameMapInIssue/';
        var productBaseCodeToSupplierNameMap = null;
        if (supplierKcode != "") {
          $.ajax({
            type: 'get',
            url: ajaxUrl,
            contentType: "application/json; charset=utf-8",
            data: { supplierKcode: supplierKcode },
            dataType: "json",
            success: function (data) {
              productBaseCodeToSupplierNameMap = data;
              emptyList("baseProduct" + id);
              if (Object.keys(productBaseCodeToSupplierNameMap).length > 0) {
                var selectBaseProduct = document.getElementById("baseProduct" + id);
                var optBaseProduct = document.createElement("option");
                optBaseProduct.value = "";
                optBaseProduct.textContent = "--- Select Base Product ---";
                selectBaseProduct.appendChild(optBaseProduct);
              }
              for (var key in productBaseCodeToSupplierNameMap) {
                optBaseProduct = document.createElement("option");
                optBaseProduct.value = key;
                optBaseProduct.textContent = key + " " + productBaseCodeToSupplierNameMap[key];
                selectBaseProduct.appendChild(optBaseProduct);
              }
            }
          });
        }
      }

      function getProductSkuCodeToSupplierNameMap(supplierKcodeParam) {
        var supplierKcode = supplierKcodeParam;
        var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductSkuCodeToSupplierNameMapInIssue/';
        var productSkuCodeToSupplierNameMap = null;
        $.ajax({
          type: 'get',
          url: ajaxUrl,
          contentType: "application/json; charset=utf-8",
          data: { supplierKcode: supplierKcode },
          dataType: "json",
          success: function (data) {
            productSkuCodeToSupplierNameMap = data;
            for (i = 0; i < $scope.SKULineItems.length; i++) {
              emptyList("sku" + i);
              if (Object.keys(productSkuCodeToSupplierNameMap).length > 0) {
                var selectSku = document.getElementById("sku" + i);
                var optSku = document.createElement("option");
                optSku.value = "";
                optSku.textContent = "--- Select SKU ---";
                selectSku.appendChild(optSku);
              }
              for (var key in productSkuCodeToSupplierNameMap) {
                optSku = document.createElement("option");
                optSku.value = key;
                optSku.textContent = key + " " + productSkuCodeToSupplierNameMap[key];
                selectSku.appendChild(optSku);
              }
            }
          }
        });
      }

      function updateProductSkuCodeToSupplierNameMap(id) {
        var supplierKcode = document.getElementById('supplier').value;
        var ajaxUrl = '${pageContext.request.contextPath}/CustomerCareCases/getProductSkuCodeToSupplierNameMapInIssue/';
        var productSkuCodeToSupplierNameMap = null;
        if (supplierKcode != "") {
          $.ajax({
            type: 'get',
            url: ajaxUrl,
            contentType: "application/json; charset=utf-8",
            data: { supplierKcode: supplierKcode },
            dataType: "json",
            success: function (data) {
              productSkuCodeToSupplierNameMap = data;
              emptyList("sku" + id);
              if (Object.keys(productSkuCodeToSupplierNameMap).length > 0) {
                var selectSku = document.getElementById("sku" + id);
                var optSku = document.createElement("option");
                optSku.value = "";
                optSku.textContent = "--- Select SKU ---";
                selectSku.appendChild(optSku);
              }
              for (var key in productSkuCodeToSupplierNameMap) {
                optSku = document.createElement("option");
                optSku.value = key;
                optSku.textContent = key + " " + productSkuCodeToSupplierNameMap[key];
                selectSku.appendChild(optSku);
              }
            }
          });
        }
      }

      $scope.getIssueTypeList = function () {
        var categoryId = $('#categoryName').val();
        retrieveIssueTypeList(categoryId);
      };

      function retrieveIssueTypeList(categoryIdParam) {
        var ajaxUrl = '${pageContext.request.contextPath}/Issues/getIssueTypeList/';
        var issueTypeList = null;
        $.ajax({
          type: 'get',
          url: ajaxUrl,
          contentType: "application/json; charset=utf-8",
          data: { categoryId: Number(categoryIdParam) },
          dataType: "json",
          success: function (data) {
            issueTypeList = data;
            emptyList("issueType");
            var selectIssueType = document.getElementById("issueType");
            var optIssueType = document.createElement("option");
            optIssueType.value = "";
            optIssueType.textContent = "--- Select ---";
            selectIssueType.appendChild(optIssueType);
            for (var issueType in issueTypeList) {
              if (issueTypeList.hasOwnProperty(issueType)) {
                optIssueType = document.createElement("option");
                optIssueType.value = issueType;
                optIssueType.textContent = issueTypeList[issueType];
                selectIssueType.appendChild(optIssueType);
              }
            }
          }
        });
      }

      $scope.addIssueNameLineItem = function () {
        $scope.issueNameLineItems.push({ language: '', name: '' });
      };

      $scope.removeIssueNameLineItem = function (issueNameLineItem) {
        if ($scope.issueNameLineItems.length > 1) {
          var idx = $scope.issueNameLineItems.indexOf(issueNameLineItem);
          $scope.issueNameLineItems.splice(idx, 1);
        }
        $scope.updateIssueNameList();
      };

      $scope.updateIssueNameList = function () {
        var languageArray = new Array();
        var nameArray = new Array();
        for (var key in $scope.issueNameLineItems) {
          if ($scope.issueNameLineItems.hasOwnProperty(key)) {
            if ($scope.issueNameLineItems[key]['language'] !== "") {
              languageArray.push($scope.issueNameLineItems[key]['language']);
            }
            if ($scope.issueNameLineItems[key]['name'] !== "") {
              nameArray.push($scope.issueNameLineItems[key]['name']);
            }
          }
        }
        $('#language').val(languageArray.toString());
        $('#issueName').val(nameArray.toString());
      };
    });

    $(document).ready(function () {
      //make dropdown combo box
      $("#supplier").select2({ theme: "bootstrap" });
    });
  </script>
  <style>
    select {
      padding: 0.25rem 0.5rem !important;
    }

    .btn {
      padding-right: 0.75rem;
      padding-left: 0.75rem;
      margin: 0;
    }

    .card {
      padding: 2rem;
    }

    .btn-drs {
      padding: 0.6rem 1rem;
    }
  </style>
</head>
<div class="max-width">
  <div class="container-fluid" ng-app="issue" ng-controller="issueCtrl">
    <!-- <div class="row">
      <div class="col-md-12">
        <div class="page-heading">
          <c:choose>
            <c:when test="${type eq 'Create'}">
              <spring:message code="issue.createNew" />
              <c:url var="action" value="/Issues/save"></c:url>
            </c:when>
            <c:otherwise>
              <spring:message code="issue.editIssue" />
              <c:url var="action" value="/Issues/update"></c:url>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div> -->
    <div class="card">
      <div class="card-body">
        <form:form action="${action}" name="Issue" class="form-horizontal text-left" modelAttribute="Issue">
          <div class="row mb-5">
            <div class="col-md-6">

              <div class="form-group row">
                <label class="col-sm-4 col-form-label">
                  <spring:message code="issue.issueCategory" /><span class="text-danger">*</span></label>
                <div class="col-sm-8">
                  <form:hidden path="id" />
                  <form:select id="categoryName" class="form-control" path="categoryId" ng-model="categoryName"
                    ng-change="getIssueTypeList()" required="required">
                    <form:option value="" label="--- Select ---" />
                    <c:forEach items="${categoryIdToNameMap}" var="categoryIdToName">
                      <form:option value="${categoryIdToName.key}">
                        <spring:message code="issue.${categoryIdToName.value}" />
                      </form:option>
                    </c:forEach>
                  </form:select>
                  <small class="text-danger form-text"
                    ng-show="Issue.categoryId.$error.required && Issue.categoryId.$dirty">
                    <spring:message code='issue.issueCategory_req' />
                  </small>
                </div>
              </div>

              <div class="form-group row">
                <label class="col-sm-4 col-form-label">
                  <spring:message code="issue.issueType" /><span class="text-danger">*</span>
                </label>
                <div class="col-sm-8">
                  <form:select id="issueType" class="form-control" path="typeId" ng-model="issueType" required="required">
                  </form:select>
                  <small class="text-danger form-text" ng-show="Issue.typeId.$error.required && Issue.typeId.$dirty">
                    <spring:message code='issue.issueType_req' />
                  </small>
                </div>
              </div>

              <div class="form-group row">
                <label class="col-sm-4 col-form-label"></label>
                <div class="col-sm-8 text-right">
                  <button class="btn btn-default" ng-click="addIssueNameLineItem()">
                    <i class="fas fa-plus"></i>
                  </button>
                  <form:hidden id="language" path="language" />
                  <form:hidden id="issueName" path="issueName" />
                </div>
              </div>

              <div ng-repeat="issueNameLineItem in issueNameLineItems">
                <div class="form-group row d-flex flex-row align-items-center">
                  <label class="col-sm-4 col-form-label">
                    <spring:message code="issue.issueName" /><span class="text-danger">*</span></label>
                  <div class="col-sm-4">
                    <select id="language{{$index}}" class="form-control" name="issueLocale{{$index}}"
                      ng-model="issueNameLineItem.language" ng-change="updateIssueNameList()" required>
                      <option value="">--- Select ---</option>
                      <c:forEach items="${localeCodeList}" var="localeCode">
                        <option value="${localeCode.code}">${localeCode.fullName}</option>
                      </c:forEach>
                    </select>
                    <small class="text-danger form-text"
                      ng-show="Issue.issueLocale{{$index}}.$error.required && Issue.issueLocale{{$index}}.$dirty">
                      <spring:message code='issue.issuelocale_req' />
                    </small>
                  </div>
                  <div class="col-sm-3">
                    <input id="name{{$index}}" class="form-control" name="issueName{{$index}}"
                      ng-model="issueNameLineItem.name" ng-change="updateIssueNameList()" ng-maxlength="100" required>
                    <small class="text-danger"
                      ng-show="Issue.issueName{{$index}}.$error.required && Issue.issueName{{$index}}.$dirty">
                      <spring:message code='issue.issueName_req' />
                    </small> <small class="text-danger form-text" ng-show="Issue.issueName{{$index}}.$error.maxlength">
                      <spring:message code='issue.issueName_length' />
                    </small>
                  </div>
                  <div class="col-sm-1 pl-0 text-right">
                    <button type="button" class="btn btn-default" ng-click="removeIssueNameLineItem(issueNameLineItem)">
                      <i class="fas fa-trash-alt"></i>
                    </button>
                  </div>
                </div>
              </div>

              <div class="form-group row">
                <label class="col-sm-4 col-form-label">
                  <spring:message code="issue.status" /><span class="text-danger">*</span></label>
                <div class="col-sm-8">
                  <form:select id="status" class="form-control" path="status" ng-model="status" required="required">
                    <form:option value="" label="--- Select ---" />
                    <c:forEach items="${issueStatusList}" var="issueStatus">
                      <form:option value="${issueStatus}">
                        <spring:message code="issue.${issueStatus}" />
                      </form:option>
                    </c:forEach>
                  </form:select>
                  <small class="text-danger form-text" ng-show="Issue.status.$error.required && Issue.status.$dirty">
                    <spring:message code='issue.issueStatus_req' />
                  </small>
                </div>
              </div>

            </div>

            <div class="col-md-6">

              <div class="form-group row d-flex flex-row align-items-center">
                <c:choose>
                  <c:when test="${type eq 'Create'}">
                    <label class="col-sm-4 col-form-label py-2 mb-0">
                      <spring:message code="issue.supplier" /><span class="text-danger">*</span></label>
                    <div class="col-sm-8">
                      <form:select id="supplier" class="form-control" style="display: inline;" path="supplierKcode"
                        ng-model="supplier" ng-change="getRelatedProductList()">
                        <form:option value="" label="Any" />
                        <c:forEach var="supplier" items="${supplierKcodeToShortEnUsNameMap}">
                          <form:option value="${supplier.key}" label="${supplier.key} ${supplier.value}" />
                        </c:forEach>
                      </form:select>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <label class="col-sm-4 col-form-label py-2 mb-0">
                      <spring:message code="issue.supplier" /></label>
                    <div class="col-sm-8">
                      <c:choose>
                        <c:when test="${!empty Issue.supplierKcode}">
                          ${Issue.supplierKcode} ${supplierKcodeToShortEnUsNameMap[Issue.supplierKcode]}
                          <form:hidden id="supplier" path="supplierKcode" />
                        </c:when>
                        <c:otherwise>
                          <spring:message code="issue.anyForSupplier" />
                          <form:hidden id="supplier" path="supplierKcode" />
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>

              <div class="form-group row" id="relatedProductTitle" ng-show="supplier !=''">
                <label class="col-sm-4 col-form-label">
                  <spring:message code="issue.relatedProducts" /><span class="text-danger">*</span></label>
                <div class="col-sm-8">
                  <div ng-show="relatedProductType" class="form-check form-check-inline">
                    <input name="relatedProduct" id="relatedProduct-BP" value="Base Product" type="radio"
                      ng-click="checkRelatedProduct()">
                    <label class="form-check-label pl-2" for="relatedProduct-BP">
                      <spring:message code="issue.baseProduct" /></label>
                  </div>
                  <div ng-show="relatedProductType" class="form-check form-check-inline">
                    <input name="relatedProduct" id="relatedProduct-SKU" value="SKU" type="radio"
                      ng-click="checkRelatedProduct()" checked="TRUE">
                    <label class="form-check-label pl-2" for="relatedProduct-SKU">
                      <spring:message code="issue.sku" /></label>
                  </div>
                </div>
              </div>

              <div id="relatedProductField" ng-show="relatedProductType">
                <div id="baseProduct" style="display: none;">
                  <div class="form-group row">
                    <label class="col-sm-4 col-form-label"></label>
                    <div class="col-sm-7"></div>
                    <div class="col-sm-1 text-right">
                      <button type="button" class="btn btn-default" ng-click="addBaseProductLineItem()">
                        <i class="fas fa-plus"></i>
                      </button>
                      <form:hidden id="relatedProductBaseCodeList" path="relatedProductBaseCodeList" />
                    </div>
                  </div>
                  <div ng-repeat="baseProductLineItem in baseProductLineItems">
                    <div class="form-group row">
                      <label class="col-sm-4 col-form-label"></label>
                      <div class="col-sm-7">
                        <select id="baseProduct{{$index}}" class="form-control" name="baseProduct{{$index}}"
                          ng-model="baseProductLineItem.baseProduct" ng-change="updateBaseProductList()" required>
                        </select>
                        <div class="text-danger"
                          ng-show="Issue.baseProduct{{$index}}.$error.required && Issue.baseProduct{{$index}}.$dirty">
                          <spring:message code='issue.issueBaseProduct_req' />
                        </div>
                      </div>
                      <div class="col-sm-1">
                        <button type="button" class="btn btn-default"
                          ng-click="removeBaseProductLineItem(baseProductLineItem)">
                          <i class="fas fa-trash-alt"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>

                <div id="sku">
                  <div class="form-group row">
                    <label class="col-sm-4 col-form-label"></label>
                    <div class="col-sm-7"></div>
                    <div class="col-sm-1 text-right">
                      <button type="button" class="btn btn-default" ng-click="addSKULineItem()">
                        <i class="fas fa-plus"></i>
                      </button>
                      <form:hidden id="relatedProductSkuCodeList" path="relatedProductSkuCodeList" />
                    </div>
                  </div>
                  <div ng-repeat="SKULineItem in SKULineItems">
                    <div class="form-group row">
                      <label for="" class="col-sm-4 col-form-label"></label>
                      <div class="col-sm-7">
                        <select id="sku{{$index}}" class="form-control" name="sku{{$index}}" ng-model="SKULineItem.sku"
                          ng-change="updateSkuList()" required>
                        </select>
                        <small class="text-danger form-text"
                          ng-show="Issue.sku{{$index}}.$error.required && Issue.sku{{$index}}.$dirty">
                          <spring:message code='issue.issueSKU_req' />
                        </small>
                      </div>
                      <div class="col-sm-1">
                        <button type="button" class="btn btn-default" ng-click="removeSKULineItem(SKULineItem)">
                          <i class="fas fa-trash-alt"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <c:if test="${type eq 'Edit'}">
                <div class="form-group row d-flex align-items-center">
                  <label class="col-sm-4 col-form-label py-2 mb-0">
                    <spring:message code="issue.templateOccurrences" /></label>
                  <div class="col-sm-8">${Issue.templateOccurrences}</div>
                </div>
                <div class="form-group row d-flex align-items-center">
                  <label class="col-sm-4 col-form-label py-2 mb-0">
                    <spring:message code="issue.created" />
                  </label>
                  <div class="col-sm-8">${Issue.createdDate} UTC</div>
                </div>
              </c:if>

            </div>
          </div>
          <div class="row">
            <div class="col-md-12 text-right">
              <input class="btn btn-primary btn-drs" type="submit" value="<spring:message code='issue.submit'/>"
                ng-disabled="Issue.$invalid" />
              <c:choose>
                <c:when test="${type eq 'Create'}">
                  <a href="${pageContext.request.contextPath}/Issues"> <input class="btn btn-link" type="button"
                    value="<spring:message code='issue.cancel'/>" />
                  </a>
                </c:when>
                <c:otherwise>
                  <a href="${pageContext.request.contextPath}/Issues/${Issue.id}">
                    <input class="btn btn-link btn-drs" type="button" value="<spring:message code='issue.cancel'/>" />
                  </a>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </form:form>
      </div>
    </div>
  </div>
</div>
