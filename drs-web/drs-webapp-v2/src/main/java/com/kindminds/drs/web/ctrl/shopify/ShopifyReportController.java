package com.kindminds.drs.web.ctrl.shopify;

import java.io.IOException;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kindminds.drs.api.usecase.report.shopify.ImportShopifyReportUco;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_RETURN_REPORT'))")
@RequestMapping(value = "/ShopifyReport")
public class ShopifyReportController {
	
	private static final String rootUrl = "ShopifyReport";
	private ImportShopifyReportUco getUco(){return (ImportShopifyReportUco)(SpringAppCtx.get().getBean("importShopifyReportUcoImpl"));}
	
	@RequestMapping(value="")
	public String ListOfAmazonReturnReport(){
		return "ListOfShopifyReport";
	}
	
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST)    	
	public String uploadFile(@RequestParam("file") MultipartFile file,final RedirectAttributes redirectAttributes){
		try {
			String originalFileName = file.getOriginalFilename();
			byte[] fileBytes = file.getBytes();
			String result = this.getUco().save(originalFileName, fileBytes);
			redirectAttributes.addFlashAttribute("message",result);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return "redirect:/"+rootUrl;
	}
	
	@RequestMapping(value="/listOrderReportFiles", method = RequestMethod.GET)
	public @ResponseBody String listUploadedFiles(){
		List<String> uploadedFiles = this.getUco().getOrderReportFileList();
		return JsonHelper.toJson(uploadedFiles);
	}
	
	@RequestMapping(value="/listPaymentTransactionReportFiles", method = RequestMethod.GET)
	public @ResponseBody String listPaymentTransactionReportFiles(){
		List<String> uploadedFiles = this.getUco().getPaymentTransactionReportFileList();
		return JsonHelper.toJson(uploadedFiles);
	}
	
	@RequestMapping(value="/listSalesReportFiles", method = RequestMethod.GET)
	public @ResponseBody String ListSalesReportFiles(){
		List<String> uploadedFiles = this.getUco().getSalesReportFileList();
		return JsonHelper.toJson(uploadedFiles);		
	}
		
	@RequestMapping(value="/importOrderReport/{fileName:.*}")
	public String importOrderReportFile(@PathVariable String fileName){
		this.getUco().importOrderReportFile(fileName);
		return "redirect:/"+rootUrl;
	}
	
	@RequestMapping(value="/deleteOrderReport/{fileName:.*}")
	public String deleteOrderReportFile(@PathVariable String fileName){
		this.getUco().deleteOrderReportFile(fileName);
		return "redirect:/"+rootUrl;
	}
	
	@RequestMapping(value="/importPaymentTransactionReport/{fileName:.*}")
	public String importPaymentTransactionReportFile(@PathVariable String fileName){
		this.getUco().importPaymentTransactionReportFile(fileName);
		return "redirect:/"+rootUrl;
	}
	
	@RequestMapping(value="/deletePaymentTransactionReport/{fileName:.*}")
	public String deletePaymentTransactionReportFile(@PathVariable String fileName){
		this.getUco().deletePaymentTransactionReportFile(fileName);
		return "redirect:/"+rootUrl;
	}
	
	@RequestMapping(value="/importSalesReport/{fileName:.*}")
	public String importSalesReportFile(@PathVariable String fileName){
		this.getUco().importSalesReportFile(fileName);
		return "redirect:/"+rootUrl;
	}
	
	@RequestMapping(value="/deleteSalesReport/{fileName:.*}")
	public String deleteSalesReportFile(@PathVariable String fileName){
		this.getUco().deleteSalesReportFile(fileName);
		return "redirect:/"+rootUrl;
	}
		
}