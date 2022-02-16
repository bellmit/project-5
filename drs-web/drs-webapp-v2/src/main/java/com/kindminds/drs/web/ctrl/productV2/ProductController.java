package com.kindminds.drs.web.ctrl.productV2;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.Marketplace;
import com.kindminds.drs.api.usecase.product.MaintainProductBaseUco;
import com.kindminds.drs.api.usecase.product.MaintainProductSkuUco;
import com.kindminds.drs.api.usecase.product.MaintainProductMarketplaceInfoUco;
import com.kindminds.drs.api.v1.model.product.BaseProduct;
import com.kindminds.drs.api.v1.model.product.ProductMarketplaceInfo;
import com.kindminds.drs.api.v1.model.product.SKU;
import com.kindminds.drs.api.v1.model.common.DtoList;
import com.kindminds.drs.api.v1.model.common.Pager;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;
import com.kindminds.drs.web.data.dto.BaseProductImpl;
import com.kindminds.drs.web.data.dto.SKUImpl;
import com.kindminds.drs.web.data.dto.product.ProductMarketplaceInfoImpl;

@Controller
public class ProductController {
		
	private MaintainProductBaseUco getMaintainProductBaseUco(){		
		return (MaintainProductBaseUco) (SpringAppCtx.get().getBean("maintainProductBaseUcoImpl"));
	}
	
	private MaintainProductSkuUco getMaintainProductSkuUco(){
		return (MaintainProductSkuUco) (SpringAppCtx.get().getBean("maintainProductSkuUcoImpl"));		
	}
	
	private MaintainProductMarketplaceInfoUco getMaintainProductMarketplaceInfoUco(){
		return (MaintainProductMarketplaceInfoUco) (SpringAppCtx.get().getBean("maintainProductMarketplaceInfoUcoImpl"));		
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('PRODUCTS_LIST'))")
	@RequestMapping(value = "/Products")
	public String listProducts(@RequestParam(value="page",defaultValue="1",required=false) Integer pageIndex, Model model) {				
		DtoList<BaseProduct> baseProductList = this.getMaintainProductBaseUco().getList(pageIndex);
		Pager page = baseProductList.getPager();
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPageIndex", page.getCurrentPageIndex());
		model.addAttribute("startPage", page.getStartPage());
		model.addAttribute("endPage", page.getEndPage());
		model.addAttribute("BaseProductList", baseProductList.getItems());
		return "ListOfProducts";
	}
	
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('BASE_PRODUCT_CREATE'))")
	@RequestMapping(value = "/BaseProduct/create", method = RequestMethod.GET)
	public String createBaseProduct(@ModelAttribute("BaseProduct") BaseProductImpl baseProduct,Model model) {	
		Map<String, String> supplierCompanies = this.getMaintainProductBaseUco().getSupplierKcodeToNameMap();
		List<String> categoryList = this.getMaintainProductBaseUco().getCategoryList();				
		model.addAttribute("SupplierCompanies", supplierCompanies);
		model.addAttribute("BaseProduct", baseProduct);
		model.addAttribute("BaseProductJson", JsonHelper.toJson(baseProduct));
		model.addAttribute("CategoryList", categoryList);
		model.addAttribute("type", "Create");
		return "BaseProduct";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('BASE_PRODUCT_CREATE'))")
	@RequestMapping(value = "/BaseProduct/save", method = RequestMethod.POST)
	public String saveBaseProduct(@ModelAttribute("BaseProduct") BaseProductImpl baseProduct,Model model) {		
		this.getMaintainProductBaseUco().insert(baseProduct);
		return "redirect:/BaseProduct/"+baseProduct.getCodeByDrs();
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('BASE_PRODUCT_EDIT'))")
	@RequestMapping(value = "/BaseProduct/update", method = RequestMethod.POST)
	public String updateBaseProduct(@ModelAttribute("BaseProduct") BaseProductImpl baseProduct,Model model) {		
		String DRSCode = this.getMaintainProductBaseUco().update(baseProduct);
		return "redirect:/BaseProduct/"+DRSCode;
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('BASE_PRODUCT_VIEW'))")
	@RequestMapping(value = "/BaseProduct/{baseProductCode}")
	public String showBaseProduct(@PathVariable String baseProductCode,Model model) {

		BaseProduct baseProduct = this.getMaintainProductBaseUco().get(baseProductCode.trim());
		Map<String, String> supplierCompanies = this.getMaintainProductBaseUco().getSupplierKcodeToNameMap();

		String company = supplierCompanies.get(baseProduct.getSupplierKcode());

		model.addAttribute("Company", company);
		model.addAttribute("BaseProduct", baseProduct);

		return "BaseProductView";
	}
    
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('BASE_PRODUCT_EDIT'))")
	@RequestMapping(value = "/BaseProduct/{baseProductCode}/edit")
	public String editBaseProduct(@PathVariable String baseProductCode,Model model) {		
		BaseProduct baseProduct = this.getMaintainProductBaseUco().get(baseProductCode);
		Map<String, String> supplierCompanies = this.getMaintainProductBaseUco().getSupplierKcodeToNameMap(); 
		List<String> categoryList = this.getMaintainProductBaseUco().getCategoryList();
		String company = supplierCompanies.get(baseProduct.getSupplierKcode());
		model.addAttribute("Company", company);
		model.addAttribute("SupplierCompanies", supplierCompanies);
		model.addAttribute("BaseProduct", baseProduct);
		model.addAttribute("BaseProductJson", JsonHelper.toJson(baseProduct));
		model.addAttribute("CategoryList", categoryList);		
		model.addAttribute("type", "Edit");		
		return "BaseProduct";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('BASE_PRODUCT_DELETE'))")
	@RequestMapping(value = "/BaseProduct/{baseProductCode}/delete")
	public String deleteBaseProduct(@PathVariable String baseProductCode,Model model,final RedirectAttributes redirectAttributes, Locale locale) {		
		String supplierBaseProductName = this.getMaintainProductBaseUco().get(baseProductCode).getNameBySupplier();
		this.getMaintainProductBaseUco().delete(baseProductCode);
		MessageSource source = (MessageSource) SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("product.deleteBaseProduct",new Object[] { baseProductCode, supplierBaseProductName },locale);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/Products";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_CREATE'))")
	@RequestMapping(value = "/SKUs/create", method = RequestMethod.GET)
	public String createSKU(@ModelAttribute("SKU") SKUImpl sku,@RequestParam(value="supplierCompanykCode",required=false) String supplierCompanyKcode,@RequestParam(value="baseProductCode",required=false) String baseProductCode,Model model) {		
		Map<String, String> supplierCompanies = this.getMaintainProductSkuUco().getSupplierKcodeToNameMap();
		List<String> skuStatusList = this.getMaintainProductSkuUco().getSkuStatusList();	
		if(supplierCompanyKcode == null)supplierCompanyKcode = "none";
		if(baseProductCode == null)baseProductCode = "none";	
		model.addAttribute("SupplierKcode", supplierCompanyKcode);
		model.addAttribute("BaseProductCode", baseProductCode);
		model.addAttribute("SupplierCompanies", supplierCompanies);
		model.addAttribute("SkuStatusList", skuStatusList);
		model.addAttribute("SKU", sku);
		model.addAttribute("SKUJson", JsonHelper.toJson(sku));
		model.addAttribute("type", "Create");


		return "SKU";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_CREATE'))")
	@RequestMapping(value = "/SKUs/save", method = RequestMethod.POST)
	public String saveSKU(@ModelAttribute("SKU") SKUImpl sku, Model model) {
		this.getMaintainProductSkuUco().insert(sku);
		return "redirect:/SKUs/"+sku.getCodeByDrs();
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_EDIT'))")
	@RequestMapping(value = "/SKUs/update", method = RequestMethod.POST)
	public String updateSKU(@ModelAttribute("SKU") SKUImpl sku, Model model) {				
		this.getMaintainProductSkuUco().update(sku);
		return "redirect:/SKUs/"+sku.getCodeByDrs();
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_MLT_EDIT'))")
	@RequestMapping(value = "/SKUs/updateForMLTAndLithiumIonMetalBatteries", method = RequestMethod.POST)
	public String updateSKUForMLTAndLithiumIonMetalBatteries(@ModelAttribute("SKU") SKUImpl sku, Model model){						
		String DRSSKUCode = this.getMaintainProductSkuUco().updateSkuMltAndContainLithium(sku);	
		return "redirect:/SKUs/"+DRSSKUCode;	
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_VIEW'))")
	@RequestMapping(value = "/SKUs/{sku}")
	public String showSKU(@ModelAttribute("SKU") SKUImpl skuImpl, @PathVariable String sku,Model model) {		
		SKU SKUs = this.getMaintainProductSkuUco().get(sku);		
		Map<String, String> supplierCompanies = this.getMaintainProductSkuUco().getSupplierKcodeToNameMap();
		String company = supplierCompanies.get(SKUs.getSupplierKcode());	
		model.addAttribute("status", SKUs.getStatus());
		model.addAttribute("Company", company);
		model.addAttribute("SKU", SKUs);
		model.addAttribute("SKUJson", JsonHelper.toJson(SKUs));		
		return "SKUView";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_EDIT'))")
	@RequestMapping(value = "/SKUs/{sku}/edit")
	public String editSKU(@PathVariable String sku,Model model) {		
		SKU SKUs = this.getMaintainProductSkuUco().get(sku);
		Map<String, String> supplierCompanies = this.getMaintainProductSkuUco().getSupplierKcodeToNameMap();	
		List<String> skuStatusList = this.getMaintainProductSkuUco().getSkuStatusList();		
		model.addAttribute("SupplierCompanies", supplierCompanies);
		model.addAttribute("SkuStatusList", skuStatusList);
		model.addAttribute("SKU", SKUs);
		model.addAttribute("SKUJson", JsonHelper.toJson(SKUs));
		model.addAttribute("type", "Edit");		
		return "SKU";
	}

	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('SKU_DELETE'))")
	@RequestMapping(value = "/SKUs/{sku}/delete")
	public String deleteSKU(@PathVariable String sku,final RedirectAttributes redirectAttributes, Locale locale) {		
		String supplierProductName = this.getMaintainProductSkuUco().get(sku).getNameBySupplier();				
		this.getMaintainProductSkuUco().delete(sku);
		MessageSource source = (MessageSource) SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("product.deleteSKU", new Object[] {sku, supplierProductName }, locale);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/Products";
	}
								
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('KEY_PRODUCT_STATS_EDIT'))")
	@RequestMapping(value = "/ProductSkuRegionInfo/create/{sku}", method = RequestMethod.GET)
	public String createProductSkuRegionInfo(@ModelAttribute("ProductSkuRegionInfo") ProductMarketplaceInfoImpl productSkuRegionInfo,@PathVariable String sku,Model model){		
		List<Marketplace> marketplaceList = this.getMaintainProductMarketplaceInfoUco().getMarketplaceList();
		List<String> productSkuRegionStatusList = this.getMaintainProductMarketplaceInfoUco().getProductSkuMarketplaceStatusList();		
		model.addAttribute("productSkuRegionStatusList",productSkuRegionStatusList);
		model.addAttribute("marketplaceList",marketplaceList);
		model.addAttribute("productCodeByDrs",sku);
		model.addAttribute("ProductSkuRegionInfoJson", JsonHelper.toJson(productSkuRegionInfo));
		model.addAttribute("type", "Create");	
		return "ProductSkuRegionInfo";				
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('KEY_PRODUCT_STATS_EDIT'))")
	@RequestMapping(value = "/ProductSkuRegionInfo/save", method = RequestMethod.POST)
	public String saveProductSkuRegionInfo(@ModelAttribute("ProductSkuRegionInfo") ProductMarketplaceInfoImpl productSkuRegionInfo,Model model){				
		String referralRate = productSkuRegionInfo.getReferralRate();
		Double referralRateDouble = Double.parseDouble(referralRate);
		referralRateDouble = referralRateDouble/100;
		productSkuRegionInfo.setReferralRate(String.valueOf(referralRateDouble));											
		this.getMaintainProductMarketplaceInfoUco().insert(productSkuRegionInfo);				
		return "redirect:/ProductSkuRegionInfo/"+productSkuRegionInfo.getProductCodeByDrs()+"/"+productSkuRegionInfo.getMarketplace().getKey();			
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('KEY_PRODUCT_STATS_VIEW'))")
	@RequestMapping(value = "/ProductSkuRegionInfo/{skuCodeByDrs}/{marketplaceId}")
	public String showProductSkuRegionInfo(Model model,@PathVariable String skuCodeByDrs,@PathVariable Integer marketplaceId){										
		ProductMarketplaceInfo productSkuRegionInfo = this.getMaintainProductMarketplaceInfoUco().get(skuCodeByDrs, marketplaceId);										
		ProductMarketplaceInfoImpl productSkuRegionInfoWeb = new ProductMarketplaceInfoImpl(productSkuRegionInfo);			
		model.addAttribute("productSkuRegionInfo", productSkuRegionInfoWeb);		
		return "ProductSkuRegionInfoView";	
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('KEY_PRODUCT_STATS_EDIT'))")
	@RequestMapping(value = "/ProductSkuRegionInfo/{skuCodeByDrs}/{marketplaceId}/edit")
	public String editProductSkuRegionInfo(Model model,@PathVariable String skuCodeByDrs,@PathVariable Integer marketplaceId){						
		ProductMarketplaceInfo productSkuRegionInfo = this.getMaintainProductMarketplaceInfoUco().get(skuCodeByDrs, marketplaceId);
		List<Marketplace> marketplaceList = this.getMaintainProductMarketplaceInfoUco().getMarketplaceList();	
		List<String> productSkuRegionStatusList = this.getMaintainProductMarketplaceInfoUco().getProductSkuMarketplaceStatusList();	
		ProductMarketplaceInfoImpl productSkuRegionInfoWeb = new ProductMarketplaceInfoImpl(productSkuRegionInfo);					
		model.addAttribute("productSkuRegionStatusList",productSkuRegionStatusList);
		model.addAttribute("marketplaceList",marketplaceList);
		model.addAttribute("ProductSkuRegionInfo", productSkuRegionInfoWeb);
		model.addAttribute("ProductSkuRegionInfoJson", JsonHelper.toJson(productSkuRegionInfoWeb));		
		return "ProductSkuRegionInfo";		
	}
		
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('KEY_PRODUCT_STATS_EDIT'))")
	@RequestMapping(value = "/ProductSkuRegionInfo/update", method = RequestMethod.POST)
	public String updateProductSkuRegionInfo(@ModelAttribute("ProductSkuRegionInfo") ProductMarketplaceInfoImpl productSkuRegionInfo,Model model){						
		String referralRate = productSkuRegionInfo.getReferralRate();
		Double referralRateDouble = Double.parseDouble(referralRate);
		referralRateDouble = referralRateDouble/100;
		productSkuRegionInfo.setReferralRate(String.valueOf(referralRateDouble));			
		this.getMaintainProductMarketplaceInfoUco().update(productSkuRegionInfo);		
		return "redirect:/ProductSkuRegionInfo/"+productSkuRegionInfo.getProductCodeByDrs()+"/"+productSkuRegionInfo.getMarketplace().getKey();	
	}
	
	@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('KEY_PRODUCT_STATS_EDIT'))")
	@RequestMapping(value = "/ProductSkuRegionInfo/{skuCodeByDrs}/{marketplaceId}/delete")
	public String deleteProductSkuRegionInfo(@PathVariable String skuCodeByDrs,@PathVariable Integer marketplaceId,final RedirectAttributes redirectAttributes, Locale locale){						
		this.getMaintainProductMarketplaceInfoUco().delete(skuCodeByDrs, marketplaceId);
		MessageSource source = (MessageSource) SpringAppCtx.get().getBean("messageSource");
		String message = source.getMessage("product.deleteMarketRegionMessage", new Object[] { Marketplace.fromKey(marketplaceId).getName() }, locale);
		redirectAttributes.addFlashAttribute("message", message);				
		return "redirect:/SKUs/"+skuCodeByDrs;				
	}
				
	@RequestMapping(value = "/SKUs/isBaseExist", method = RequestMethod.GET)	
	public @ResponseBody String isBaseExist(@RequestParam("supplierKcode") String supplierKcode, @RequestParam("baseCodeBySupplier") String baseCodeBySupplier){		
		boolean baseExist = this.getMaintainProductBaseUco().isBaseExist(supplierKcode, baseCodeBySupplier);				
		return JsonHelper.toJson(baseExist);		
	}
			
	@RequestMapping(value = "/SKUs/getBaseCodeList", method = RequestMethod.GET)
	public @ResponseBody String getBaseCodeList(@RequestParam("supplierKcode") String supplierKcode) {		
		List<String> baseCodeList = this.getMaintainProductSkuUco().getBaseCodeList(supplierKcode);	
		return JsonHelper.toJson(baseCodeList);		
	}

	@RequestMapping(value = "/SKUs/isSkuExist", method = RequestMethod.GET)
	public @ResponseBody String isSkuExist(@RequestParam("supplierKcode") String supplierKcode,@RequestParam("skuCodeBySupplier") String skuCodeBySupplier) {
		boolean skuExist = this.getMaintainProductSkuUco().isSkuExist(supplierKcode, skuCodeBySupplier);		
		return JsonHelper.toJson(skuExist);	
	}
	
	@RequestMapping(value = "/ProductSkuRegionInfo/getMarketplaceCurrency", method = RequestMethod.GET)
	public @ResponseBody String getMarketplaceCurrency(@RequestParam("marketplace") String marketplace){		
		String MarketplaceCurrency = this.getMaintainProductMarketplaceInfoUco().getMarketplaceCurrency(marketplace);		
		return JsonHelper.toJson(MarketplaceCurrency);		
	}
			
}