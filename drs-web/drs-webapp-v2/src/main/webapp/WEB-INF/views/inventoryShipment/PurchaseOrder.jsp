<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
	<title>
		<spring:message code='inventoryShipment.po.title' /> ${poNumber}
	</title>
<style type="text/css">
body {
	font-size: 12px;
}
.text-left {
	text-align: left;
}
</style>	
</head>
<div class="container">
	<div class="row" style="font-size: 14px">
		<table style="width: 100%">
			<tbody>
				<tr>						
					<td valign="middle">
						<div style="padding-left:20%; padding-right:20%">						
							<div class="text-center">善恩創新股份有限公司</div>
							<div class="text-center">KindMinds Innovations, Inc.</div>
							<div class="text-center">通訊地址: 11158 台北市士林區德行西路45號6樓</div>
							<div class="text-center">Tel: 02-28378995 Fax: 02-28376257 統一編號: 80698253</div>													
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>	
	<div class="row">
		<div class="text-center">
				<u style="font-size: 30px"> <spring:message code='inventoryShipment.po.title' /> </u>
		</div>
	</div>
	<div class="row">
		<table style="width: 100%">
			<tr>
				<td>
					<spring:message code='inventoryShipment.po.supplier' />:		
				</td>
				<td>
					${orderInfo.nameLocal}		
				</td>
				<td>
					<spring:message code='inventoryShipment.po.POnumber' />:	
				</td>
				<td>
					${poNumber}	
				</td>
			</tr>
			<tr>
				<td>
					<spring:message code='inventoryShipment.po.contact' />:	
				</td>
				<td>
						
				</td>
				<td>
					<spring:message code='inventoryShipment.po.purchaseDate' />:	
				</td>
				<td>
					${orderInfo.printingDate}	
				</td>
			</tr>
			<tr>
				<td>
					<spring:message code='inventoryShipment.po.phoneNumber' />:	
				</td>
				<td>
					${orderInfo.phoneNumber}	
				</td>
				<td>
					<spring:message code='inventoryShipment.po.deliveryDate' />:	
				</td>
				<td>
					${orderInfo.fcaDeliveryDate}
				</td>
			</tr>
			<tr>
				<td>
					<spring:message code='inventoryShipment.po.address' />:	
				</td>
				<td>
					${orderInfo.address}
				</td>
				<td>
					<spring:message code='inventoryShipment.po.deliveryLocation' />:	
				</td>
				<td>
					${orderInfo.fcaShipmentLocation}
				</td>
			</tr>				
		</table>							
	</div>
	<div class="row">
		<table class="table-condensed table-fixed-header" style="width: 100%">
			<thead class="header">
				<tr>
					<th colspan="5">
						<hr style="border-color: black; margin: 0 0 0 0;">							
					</th>
				</tr>
				<tr>
					<th class="text-left" style="width: 38%"><b><spring:message code='inventoryShipment.po.sku' /></b></th>
					<th class="text-left" style="width: 38%"><b><spring:message code='inventoryShipment.po.productName' /></b></th>
					<th class="text-left" style="width: 8%"><b><spring:message code='inventoryShipment.po.quantity' /></b></th>
					<th class="text-left" style="width: 8%"><b><spring:message code='inventoryShipment.po.unitAmount' /></b></th>
					<th class="text-left" style="width: 8%"><b><spring:message code='inventoryShipment.po.amount' /></b></th>						
				</tr>
				<tr>
					<th colspan="5">
						<hr style="border-color: black; margin: 0 0 0 0;">
					</th>								
				</tr>
			</thead>			
			<tbody>
				<c:forEach items="${orderSkuList}" var="orderSku">	
					<tr>
						<td class="text-left">${orderSku.skuCode}</td>
						<td class="text-left">${orderSku.skuName}</td>
						<td class="text-left">${orderSku.numberUnits}</td>
						<td class="text-left">${orderSku.unitPrice}</td>
						<td class="text-left">${orderSku.subTotal}</td>
					</tr>			
				</c:forEach>
				<tr>
					<td colspan="5">
						<hr style="border-color: black; margin: 0 0 0 0;">
					</td>
				</tr>
				<tr>
					<td style="width: 38%"></td>						
					<td style="width: 38%"><b><spring:message code='inventoryShipment.po.totalwithoutTax' /></b></td>
					<td stlye="width: 8%"></td>
					<td stlye="width: 8%"></td>
					<td style="width: 8%">${orderInfo.subTotal}</td>
				</tr>
				<tr>
					<td style="width: 38%"></td>					
					<td style="width: 38%"><b><spring:message code='inventoryShipment.po.tax' /> (${orderInfo.salesTaxRate}%)</b></td>
					<td stlye="width: 8%"></td>
					<td stlye="width: 8%"></td>
					<td stlye="width: 8%">${orderInfo.amountTax}</td>
				</tr>
				<tr>
					<td style="width: 38%"></td>						
					<td style="width: 38%"><b><spring:message code='inventoryShipment.po.totalwithTax' /></b></td>
					<td stlye="width: 8%"></td>
					<td stlye="width: 8%"></td>
					<td stlye="width: 8%">${orderInfo.amountTotal}</td>
				</tr>
				<tr>
					<td colspan="5">
						<b><spring:message code='inventoryShipment.po.ps' /></b>&nbsp;&nbsp;&nbsp;&nbsp;<spring:message code='inventoryShipment.po.psContent' />
					</td>											
				</tr>				
			</tbody>
		</table>
	</div>
</div>