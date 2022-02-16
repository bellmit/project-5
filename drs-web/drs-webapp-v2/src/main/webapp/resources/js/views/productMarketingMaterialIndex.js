var isDrsUser;
var productDetail = '';
var supplierKcode = '';
var baseProductCode = '';
var skuCode = '';
var region = '';
var features = [];
var searchTerms = [];

// click simulation
$.fn.simulateClick = function() {
    return this.each(function() {
        if('createEvent' in document) {
            var doc = this.ownerDocument,
                evt = doc.createEvent('MouseEvents');
            evt.initMouseEvent('click', true, true, doc.defaultView, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
            this.dispatchEvent(evt);
        } else {
            this.click(); // IE
        }
    });
}

window.addEventListener("DOMContentLoaded", () => {
	const tabs = document.querySelectorAll('[role="tab"]');
	const tabList = document.querySelector('[role="tablist"]');
  
	// Add a click event handler to each tab
	tabs.forEach(tab => {
	  tab.addEventListener("click", changeTabs);
	});
  
	// Enable arrow navigation between tabs in the tab list
	let tabFocus = 0;
  
	tabList.addEventListener("keydown", e => {
	  // Move right
	  if (e.keyCode === 39 || e.keyCode === 37) {
		tabs[tabFocus].setAttribute("tabindex", -1);
		if (e.keyCode === 39) {
		  tabFocus++;
		  // If we're at the end, go to the start
		  if (tabFocus >= tabs.length) {
			tabFocus = 0;
		  }
		  // Move left
		} else if (e.keyCode === 37) {
		  tabFocus--;
		  // If we're at the start, move to the end
		  if (tabFocus < 0) {
			tabFocus = tabs.length - 1;
		  }
		}
  
		tabs[tabFocus].setAttribute("tabindex", 0);
		tabs[tabFocus].focus();
	  }
	});
});
  
  function changeTabs(e) {
	const target = e.target;
	const parent = target.parentNode;
	const grandparent = parent.parentNode;
  
	// Remove all current selected tabs
	parent
	  .querySelectorAll('[aria-selected="true"]')
	  .forEach((t) => {
		  t.setAttribute("aria-selected", false);
		  t.classList.remove('active');
		});
  
	// Set this tab as selected
	target.setAttribute("aria-selected", true);
	target.classList.add('active');
  
	// Hide all tab panels
	grandparent
	  .querySelectorAll('[role="tabpanel"]')
	  .forEach(p => p.setAttribute("hidden", true));
  
	// Show the selected panel
	grandparent.parentNode
	  .querySelector(`#${target.getAttribute("aria-controls")}`)
	  .removeAttribute("hidden");
}

function updateModal(productData, basicPath){
	
	productDetail = JSON.parse(productData.data);
	imageInfo = JSON.parse(productDetail.products)[0];
	
	makeFeatureArray();
	makeSearchTermArray();
	
	console.log("update Modal")
	console.log(productData);
//	console.log(productDetail);
//	console.log(imageInfo);

	// set status
	// only show status for core information
	if(region == 'core'){
		$('#modalMarketingMaterialStatus').show();
		$('#modalMarketingMaterialStatus').text(productData.status)
	}else{
		$('#modalMarketingMaterialStatus').hide();
		$('#modalMarketingMaterialStatus').html('')
	}
	
	// set sku code
	$('#modalSkuCode').text(skuCode)
	
	// set marketing material region
	if (region == 'core') {
		
		$('#modalMarketingMaterialRegion').html(
			'<div class="badge badge-light modal-badge">Core Information</div>'
		);
	} else {
		
		$('#modalMarketingMaterialRegion').html(
			'<div class="badge badge-light modal-badge">' + region + '</div>'
		);
	}

	// if core info
	if (region == 'core') {
		
		// if user is drs user
		if (isDrsUser) {
			
			if (productData.status == 'Pending DRS review' || productData.status == 'Finalized' ) {
				
				var editLink = basicPath +'/EditSourceProductMarketingMaterial/'+baseProductCode+ '/' 
					+ skuCode + '?supplierKcode=' + supplierKcode;
				
				$('#buttonGroupPlaceholder').html(
						'<a type="button" class="btn btn-primary float-right modal-function-buttons" href=' + 
							editLink + '>Edit</a>');
			}
			
		} else {  // if user is supplier
			
			if (productData.status == 'Pending supplier action') {
				var editLink = basicPath +'/EditSourceProductMarketingMaterial/'+baseProductCode+ '/' 
					+ skuCode + '?supplierKcode=' + supplierKcode;
				
				$('#buttonGroupPlaceholder').html(
						'<a type="button" class="btn btn-primary float-right modal-function-buttons" href=' + 
							editLink + '>Edit</a>');
			}
			
		}
		
	// if region marketing material	
	} else {
		
		// if user is drs user
		if (isDrsUser) {
		
			var salesCopyDiscussionLink = basicPath +'/ConfirmMarketingMaterial/'+ baseProductCode + '/' 
			+ skuCode + '/' + region + '?supplierKcode=' + supplierKcode;
			
			var editLink = basicPath +'/EditMarketSideProductMarketingMaterial/'+ baseProductCode + '/' 
			+ skuCode + '/' + region;
		
			$('#buttonGroupPlaceholder').html(
					'<a type="button" class="btn btn-primary float-right modal-function-buttons" href=' + 
					 editLink + '>Edit</a>' +	
				'<a type="button" class="btn btn-primary float-right modal-function-buttons" href=' + 
				salesCopyDiscussionLink + '>Sales Copy Discussion</a>' +
				'<a type="button" class="btn btn-primary float-right modal-function-buttons" data-toggle="modal" data-target="#notifySupplierModal">'+
				 'Notify Supplier</a>');

			// set notifySupplierBtn region
			$('#notifySupplierBtn').attr("region", region)
			
		} else {  // if user is supplier
			
			var salesCopyDiscussionLink = basicPath +'/ConfirmMarketingMaterial/'+ baseProductCode + '/' 
				+ skuCode + '/' + region + '?supplierKcode=' + supplierKcode;
			
			$('#buttonGroupPlaceholder').html(
				'<a type="button" class="btn btn-primary float-right modal-function-buttons" href=' + 
				salesCopyDiscussionLink + '>Sales Copy Discussion</a>');
			
		}
	
	}
	
	//title
	if(productDetail.name != ''){
		$('#skuTitle').text(productDetail.name)
	}else{
		$('#skuTitle').text("No Title");
	}
	
	// short name
	if(region == 'core'){
		$('#skuShortNamePlaceholder').html('');
	
	}else{
		
		if(productDetail.shortName != ''){
			$('#skuShortNamePlaceholder').html(
					'</br><span class="sectionTitle">Short Name: </span> <span id="skuShortName">'+ productDetail.shortName + '</span>');
				
		}else{
			$('#skuShortNamePlaceholder').html(
					'</br><span class="sectionTitle">Short Name: </span> <span id="skuShortName">None</span>');
				
		}
		
	}
	
	//img section
	//console.log(basicPath);
	//https://access.drs.network/MainImageFile?fileName=K132-4_01.JPG&region=soure
	//http://10.0.0.253:8080/drs-sys-web/MainImageFile?fileName=K132-4_01.JPG&region=soure
	//http://localhost:8080/MainImageFile?fileName=K132-4_01.JPG&region=soure
	
	var imageRegion;
	
	// clear previous display image 
	$("#display_img").attr("src", '');
	
	if(region == 'core'){
		imageRegion = 'soure';
	}else{
		imageRegion = region;
	}
	
	// if main img exists
	if(imageInfo.mainImageLineItems.length>0){
		//$('#mainImagePlaceholder').html('<img id="mainImage" src="' + basicPath + 
		//	'/MainImageFile?fileName=K132-4_01.JPG&region=soure" class="product_img">')
		
		var img = imageInfo.mainImageLineItems[0].file;
		
		console.log(imageInfo);
		console.log(img);
		
		$('#mainImagePlaceholder').html('<img id="mainImage" \
				src="https://access.drs.network/MainImageFile?fileName=' + img + '&region=' + imageRegion + 
				'" class="product_img">')		
				
	}else{
		$('#mainImagePlaceholder').text("None");
	}
	
	// render variation img 
	if(imageInfo.variationImageLineItems.length>0){
		
		var img = imageInfo.variationImageLineItems[0].file;
		
		$('#variationImagePlaceholder').html('<img id="variationImage" \
				src="https://access.drs.network/VariationImageFile?fileName=' + img + '&region=' + imageRegion + 
				'" class="product_img">')		
				
	}else{
		$('#variationImagePlaceholder').text("None");
	}
	
	// render other images
	if(imageInfo.otherImageLineItems.length>0){
		
		var html = '';
		
		for(var i = 0; i < imageInfo.otherImageLineItems.length; i++){
			
			var img = imageInfo.otherImageLineItems[i].file;
			
			html = html + '<img id="variationImage" \
				src="https://access.drs.network/OtherImageFile?fileName=' + img + 
				'&region=' + imageRegion + '" class="product_img">';
			
		}
		
		$('#otherImagesPlaceholder').html(html);
				
	}else{
		$('#otherImagesPlaceholder').text("None");
	}
	

	// update big picture when clicked
	$('.product_img').click(function(){
		$("#display_img").attr("src", $(this)[0].src);
	})
	
	// ===============================================================================

	// features
	var featureListHtml = '';
	
	features.forEach(function (feature) {
		if (feature != '') {
			featureListHtml = featureListHtml + "<li>" + feature + "</li>";
		} else {
			featureListHtml = featureListHtml + "<li>None</li>";
		}
	
	})

	$('#featureList').html(featureListHtml)

	// description 
	if (productDetail.description != '') {
		$('#skuDescription').html(productDetail.description)
	} else {
		$('#skuDescription').text("None");
	}
	
	// search term
	var searchTermListHtml = '';
	
	searchTerms.forEach(function (searchTerm) {
		
		if (searchTerm != '') {
			searchTermListHtml = searchTermListHtml + "<li>" + searchTerm + "</li>";
		} else {
			searchTermListHtml = searchTermListHtml + "<li>None</li>";
		}
	})
	$('#searchTermList').html(searchTermListHtml)
	
	// notes 	
	if (productDetail.note != '') {
		$('#skuNotes').text(productDetail.note)
	} else {
		$('#skuNotes').text("None");
	}
};

function makeFeatureArray() {
	features = [];
	features.push(productDetail.feature1);
	features.push(productDetail.feature2);
	features.push(productDetail.feature3);
	features.push(productDetail.feature4);
	features.push(productDetail.feature5);
}

function makeSearchTermArray() {
	searchTerms = [];
	searchTerms.push(productDetail.searchTerms1);
	searchTerms.push(productDetail.searchTerms2);
	searchTerms.push(productDetail.searchTerms3);
	searchTerms.push(productDetail.searchTerms4);
	searchTerms.push(productDetail.searchTerms5);
}
	