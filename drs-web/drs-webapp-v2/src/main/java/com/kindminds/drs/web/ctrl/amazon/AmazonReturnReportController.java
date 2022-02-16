package com.kindminds.drs.web.ctrl.amazon;

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

import com.kindminds.drs.api.usecase.report.amazon.ImportAmazonReturnReportUco;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;

@Controller
@PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('AMAZON_RETURN_REPORT'))")
public class AmazonReturnReportController {
	
	private ImportAmazonReturnReportUco getUco(){return (ImportAmazonReturnReportUco)(SpringAppCtx.get().getBean("importAmazonReturnReportUcoImpl"));}
	
	@RequestMapping(value = "/AmazonReturnReport")
	public String ListOfAmazonReturnReport(){
		return "ListOfAmazonReturnReports";
	}
	
	@RequestMapping(value="/AmazonReturnReport/uploadFile",method=RequestMethod.POST)    	
	public String uploadFile(@RequestParam("file") MultipartFile file,final RedirectAttributes redirectAttributes){
		try {
			String originalFileName = file.getOriginalFilename();
			byte[] fileBytes = file.getBytes();
			String result = this.getUco().save(originalFileName, fileBytes);
			redirectAttributes.addFlashAttribute("message", result);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return "redirect:/AmazonReturnReport";
		
	}
	
	@RequestMapping(value = "/AmazonReturnReport/listUploadedFiles", method = RequestMethod.GET)
	public @ResponseBody String listUploadedFiles(){
		List<String> uploadedFiles = this.getUco().getFileList();
		return JsonHelper.toJson(uploadedFiles);	
		
	}
		
	@RequestMapping(value = "/AmazonReturnReport/importFile/{fileName:.*}")
	public String importUploadedFile(@PathVariable String fileName,final RedirectAttributes redirectAttributes){
		String result = this.getUco().importFile(fileName);
		redirectAttributes.addFlashAttribute("message", result);
		return "redirect:/AmazonReturnReport";
		
	}
		
	@RequestMapping(value = "/AmazonReturnReport/deleteUploadedFile/{fileName:.*}")	
	public String deleteUploadedFile(@PathVariable String fileName,final RedirectAttributes redirectAttributes){
		String result = this.getUco().delete(fileName);
		redirectAttributes.addFlashAttribute("message", result);
		return "redirect:/AmazonReturnReport";
		
	}
	
}