package com.kindminds.drs.web.ctrl.productV2;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import com.kindminds.drs.api.usecase.product.MaintainProductOnboardingUco;
import com.kindminds.drs.service.util.SpringAppCtx;
import com.kindminds.drs.util.JsonHelper;

@Controller
public class ProductFileController {
	
	 private static final Logger logger = LogManager.getLogger(ProductFileController.class);
	 
	@Autowired @Qualifier("envProperties") private Properties env;
	
	private final String referenceFilePathPropertyName ="REFERENCE_FILE_FOLDER_PATH";
	private final String batteryFilePathPropertyName ="BATTERY_FILE_FOLDER_PATH";
	private final String mainImageFilePathPropertyName ="MAIN_IMAGE_FILE_FOLDER_PATH";
	private final String variationImageFilePathPropertyName ="VARIATION_IMAGE_FILE_FOLDER_PATH";
	private final String otherImageFilePathPropertyName ="OTHER_IMAGE_FILE_FOLDER_PATH";


	private static final int BUFFER_SIZE = 4096;	
	private String getRootFileDir(){ 
		return System.getProperty("catalina.home"); 
	}
	
	
	private MaintainProductOnboardingUco uco  = null ;
	private MaintainProductOnboardingUco getMaintainProductOnboardingUco(){
		if(uco == null)
		 uco = (MaintainProductOnboardingUco)SpringAppCtx.get().getBean("maintainProductOnboardingUco");
		return uco;
	}
	
	@RequestMapping(value="/ProductInfoSourceVersion/uploadReferenceFile",method=RequestMethod.POST)   
	public @ResponseBody String uploadReferenceFile(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException{			
			Iterator<String> itr = request.getFileNames();
			MultipartFile mpf = request.getFile(itr.next());	
		try {
			String originalFileName = mpf.getOriginalFilename();
			byte[] fileBytes = mpf.getBytes();
			String result = this.uploadReferenceFile(originalFileName, fileBytes);			 						 
			if(result == null) return JsonHelper.toJson("fail");
		} catch (IOException e) {
				e.printStackTrace();
		}		 
		return JsonHelper.toJson(mpf.getOriginalFilename()); 		
	} 	
	
	@RequestMapping(value = "/ReferenceFile", method = RequestMethod.GET)
    public void downloadReferenceFile(@RequestParam("fileName") String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{				
		this.downloadReferenceFile(fileName, response);		
	}
	
	@RequestMapping(value="/ProductInfoSourceVersion/removeReferenceFile",method=RequestMethod.POST)   	
	public @ResponseBody String requestRemoveReferenceFile(@RequestParam("referenceFile") String referenceFile){					
		 this.removeReferenceFile(referenceFile);
		 return JsonHelper.toJson(referenceFile);
	}
	
	@RequestMapping(value="/ProductInfoSourceVersion/uploadBatteryFile",method=RequestMethod.POST) 	
	public @ResponseBody String uploadBatteryFile(MultipartHttpServletRequest request, HttpServletResponse response){					
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = request.getFile(itr.next());	
		try {
			String originalFileName = mpf.getOriginalFilename();
			byte[] fileBytes = mpf.getBytes();
			String result = this.uploadBatteryFile(originalFileName, fileBytes);			 						 
			if(result == null) return JsonHelper.toJson("fail");
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return JsonHelper.toJson(mpf.getOriginalFilename());	
	}
	
	@RequestMapping(value = "/BatteryFile", method = RequestMethod.GET)
    public void downloadBatteryFile(@RequestParam("fileName") String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{				
		this.downloadBatteryFile(fileName, response);
	}
	
	@RequestMapping(value="/ProductInfoSourceVersion/removeBatteryFile",method=RequestMethod.POST)
	public @ResponseBody String reqRemoveBatteryFile(@RequestParam("batteryFile") String batteryFile){
		 this.removeBatteryFile(batteryFile);
		 return JsonHelper.toJson(batteryFile);
	}
	
	@RequestMapping(value="/ProductMarketingMaterial/uploadMainImageFile",method=RequestMethod.POST)
	public @ResponseBody String uploadMainImageFile(MultipartHttpServletRequest request, HttpServletResponse response,@RequestParam(value="region",required=false) String region){					
		Iterator<String> itr = request.getFileNames(); 	 
		MultipartFile mpf = request.getFile(itr.next());		
		try {
			String originalFileName = mpf.getOriginalFilename();
			byte[] fileBytes = mpf.getBytes();
			String result = this.uploadMainImageFile(originalFileName, fileBytes, region); 			 						 
			if(result == null) return JsonHelper.toJson("fail");
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return JsonHelper.toJson(mpf.getOriginalFilename());	
	}
	
	@RequestMapping(value = "/MainImageFile", method = RequestMethod.GET)
    public void downloadMainImageFile(@RequestParam("fileName") String fileName,@RequestParam("region") String region,HttpServletRequest request,HttpServletResponse response) throws IOException{				
		this.downloadMainImageFile(fileName, region, response);				
	}
	
	@RequestMapping(value="/ProductMarketingMaterial/removeMainImageFile",method=RequestMethod.POST)	
	public @ResponseBody String reqRemoveMainImageFile(@RequestParam("mainImageFile") String mainImageFile,@RequestParam("region") String region){
		this.removeMainImageFile(mainImageFile,region);
		return JsonHelper.toJson(mainImageFile);	
	}
	
	@RequestMapping(value="/ProductMarketingMaterial/uploadVariationImageFile",method=RequestMethod.POST)	
	public @ResponseBody String uploadVariationImageFile(MultipartHttpServletRequest request, HttpServletResponse response,@RequestParam(value="region",required=false) String region){				
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = request.getFile(itr.next());
		try {
			String originalFileName = mpf.getOriginalFilename();
			byte[] fileBytes = mpf.getBytes();
			String result = this.uploadVariationImageFile(originalFileName, fileBytes, region); 			 						 
			if(result == null) return JsonHelper.toJson("fail");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonHelper.toJson(mpf.getOriginalFilename());		
	}
	
	@RequestMapping(value = "/VariationImageFile", method = RequestMethod.GET)
    public void downloadVariationImageFile(@RequestParam("fileName") String fileName,@RequestParam("region") String region,HttpServletRequest request,HttpServletResponse response) throws IOException{				
		this.downloadVariationImageFile(fileName, region, response);				
	}
	
	@RequestMapping(value="/ProductMarketingMaterial/removeVariationImageFile",method=RequestMethod.POST)		
	public @ResponseBody String reqRemoveVariationImageFile(@RequestParam("variationImageFile") String variationImageFile,@RequestParam("region") String region){
		 this.removeVariationImageFile(variationImageFile,region);
		 return JsonHelper.toJson(variationImageFile);	
	}
	
	@RequestMapping(value="/ProductMarketingMaterial/uploadOtherImageFile",method=RequestMethod.POST)	
	public @ResponseBody String uploadOtherImageFile(MultipartHttpServletRequest request, HttpServletResponse response,@RequestParam(value="region",required=false) String region){				
		Iterator<String> itr = request.getFileNames();	 	 
		MultipartFile mpf = request.getFile(itr.next());		
		try {
			String originalFileName = mpf.getOriginalFilename();
			byte[] fileBytes = mpf.getBytes();
			String result = this.uploadOtherImageFile(originalFileName, fileBytes, region); 			 						 
			if(result == null) return JsonHelper.toJson("fail");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonHelper.toJson(mpf.getOriginalFilename());			
	}
	
	@RequestMapping(value = "/OtherImageFile", method = RequestMethod.GET)
    public void downloadOtherImageFile(@RequestParam("fileName") String fileName,
    		@RequestParam("region") String region,HttpServletRequest request,HttpServletResponse response) throws IOException{				

		this.downloadOtherImageFile(fileName, region, response);				
	}
	
	@RequestMapping(value="/ProductMarketingMaterial/removeOtherImageFile",method=RequestMethod.POST)	
	public @ResponseBody String reqRemoveOtherImageFile(@RequestParam("otherImageFile") String otherImageFile,@RequestParam("region") String region){
		this.removeOtherImageFile(otherImageFile,region);		
		return JsonHelper.toJson(otherImageFile);
	}
	
	
	

	private String uploadReferenceFile(String fileName, byte[] bytes) {
		
		String folderPath = (String)this.env.get(this.referenceFilePathPropertyName);
		File fullPath = new File(this.getRootFileDir()+File.separator+folderPath);
		if(!fullPath.exists()) fullPath.mkdirs();
		File file = new File(fullPath.getAbsolutePath()+File.separator+fileName);
		if(file.exists()) return null;
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
			stream.write(bytes);	
		} catch (IOException e) {
			//logger.info("IOException in MaintainProductOnboardingDaoImpl method uploadReferenceFile: ", e);
		}
		
		return "success";
	}


	private void downloadReferenceFile(String fileName, HttpServletResponse response) throws IOException {
		
	    	String folderPath = (String)this.env.get(this.referenceFilePathPropertyName);
			String fullPath = this.getRootFileDir()+File.separator+folderPath+File.separator+fileName;		
			File downloadFile = new File(fullPath);		
			// MIME type of the file
	        String mimeType= URLConnection.guessContentTypeFromName(fileName);
	        if (mimeType == null) {
	            // Set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	        // set content attributes for the response object
	        response.setContentType(mimeType);
	        response.setContentLength((int) downloadFile.length());	 
	        // set headers for the response object
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                downloadFile.getName());
	        response.setHeader(headerKey, headerValue); 
	        // get output stream of the response
	        try (OutputStream outStream = response.getOutputStream();
	        		FileInputStream inputStream = new FileInputStream(downloadFile)) {
		        byte[] buffer = new byte[BUFFER_SIZE];
		        int bytesRead = -1;
	
		        // write each byte of data  read from the input stream into the output stream
		        while ((bytesRead = inputStream.read(buffer)) != -1) {
		            outStream.write(buffer, 0, bytesRead);
		        }
	        }
	        
	        	
	}


	private void removeReferenceFile(String fileName) {
	
		String folderPath = (String)this.env.get(this.referenceFilePathPropertyName);
		File file = new File(this.getRootFileDir()+File.separator+folderPath+File.separator+fileName);		
		file.delete();
		
	}

	
	private String uploadBatteryFile(String fileName, byte[] bytes) {
		
		String folderPath = (String)this.env.get(this.batteryFilePathPropertyName);
		File fullPath = new File(this.getRootFileDir()+File.separator+folderPath);
		if(!fullPath.exists()) fullPath.mkdirs();
		File file = new File(fullPath.getAbsolutePath()+File.separator+fileName);
		if(file.exists()) return null;
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
			stream.write(bytes);
		} catch (IOException e) {
			//logger.info("IOException in MaintainProductOnboardingDaoImpl method uploadBatteryFile: ", e);
		}
		
		return "success";
	}

	
	private void downloadBatteryFile(String fileName, HttpServletResponse response) throws IOException {
		
		String folderPath = (String)this.env.get(this.batteryFilePathPropertyName);
		String fullPath = this.getRootFileDir()+File.separator+folderPath+File.separator+fileName;		
		File downloadFile = new File(fullPath);		
		// MIME type of the file
	    String mimeType= URLConnection.guessContentTypeFromName(fileName);
	    if (mimeType == null) {
	        // Set to binary type if MIME mapping not found
	        mimeType = "application/octet-stream";
	    }
	    // set content attributes for the response object
	    response.setContentType(mimeType);
	    response.setContentLength((int) downloadFile.length());	 
	    // set headers for the response object
	    String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"",
	            downloadFile.getName());
	    response.setHeader(headerKey, headerValue); 
	    // get output stream of the response
	    try (OutputStream outStream = response.getOutputStream();
	    		FileInputStream inputStream = new FileInputStream(downloadFile)) {
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = -1;
	 
	        // write each byte of data  read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	    }
				
	}

	
	private void removeBatteryFile(String fileName) {
		
		String folderPath = (String)this.env.get(this.batteryFilePathPropertyName);
		File file = new File(this.getRootFileDir()+File.separator+folderPath+File.separator+fileName);		
		file.delete();
		
	}

	
	private String uploadMainImageFile(String fileName, byte[] bytes, String region) {
		
		String folderPath = (String)this.env.get(this.mainImageFilePathPropertyName)+File.separator+region;
		File fullPath = new File(this.getRootFileDir()+File.separator+folderPath);
		if(!fullPath.exists()) fullPath.mkdirs();
		File file = new File(fullPath.getAbsolutePath()+File.separator+fileName);
		if(file.exists()) return null;
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
			stream.write(bytes);	
		} catch (IOException e) {
			//logger.info("IOException in MaintainProductOnboardingDaoImpl method uploadMainImageFile: ", e);
		}
		
		return "success";
	}

	
	private void downloadMainImageFile(String fileName, String region, HttpServletResponse response) throws IOException {
		
		String folderPath = (String)this.env.get(this.mainImageFilePathPropertyName)+File.separator+region;
		String fullPath = this.getRootFileDir()+File.separator+folderPath+File.separator+fileName;		
		File downloadFile = new File(fullPath);		
		// MIME type of the file
	    String mimeType= URLConnection.guessContentTypeFromName(fileName);
	    if (mimeType == null) {
	        // Set to binary type if MIME mapping not found
	        mimeType = "application/octet-stream";
	    }
	    // set content attributes for the response object
	    response.setContentType(mimeType);
	    response.setContentLength((int) downloadFile.length());	 
	    // set headers for the response object
	    String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"",
	            downloadFile.getName());
	    response.setHeader(headerKey, headerValue); 
	    // get output stream of the response
	    try (OutputStream outStream = response.getOutputStream();
		FileInputStream inputStream = new FileInputStream(downloadFile)) {
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = -1;
	 
	        // write each byte of data  read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	    }
	    
				
	}

	
	private void removeMainImageFile(String fileName, String region) {
		
		String folderPath = (String)this.env.get(this.mainImageFilePathPropertyName)+File.separator+region;
		File file = new File(this.getRootFileDir()+File.separator+folderPath+File.separator+fileName);		
		file.delete();
		
	}

	
	private String uploadVariationImageFile(String fileName, byte[] bytes, String region) {
		
		String folderPath = (String)this.env.get(this.variationImageFilePathPropertyName)+File.separator+region;
		File fullPath = new File(this.getRootFileDir()+File.separator+folderPath);
		if(!fullPath.exists()) fullPath.mkdirs();
		File file = new File(fullPath.getAbsolutePath()+File.separator+fileName);
		if(file.exists()) return null;
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))){
			stream.write(bytes);
		} catch (Exception e) {
			//logger.info("IOException in MaintainProductOnboardingDaoImpl method uploadVariationImageFile: ", e);
		}
		
		return "success";
	}


	private void downloadVariationImageFile(String fileName, String region, HttpServletResponse response)
			throws IOException {
		
		String folderPath = (String)this.env.get(this.variationImageFilePathPropertyName)+File.separator+region;
		
		String fullPath = this.getRootFileDir()+File.separator+folderPath+File.separator+fileName;		
		File downloadFile = new File(fullPath);		
		// MIME type of the file
	    String mimeType= URLConnection.guessContentTypeFromName(fileName);
	    if (mimeType == null) {
	        // Set to binary type if MIME mapping not found
	        mimeType = "application/octet-stream";
	    }
	    // set content attributes for the response object
	    response.setContentType(mimeType);
	    response.setContentLength((int) downloadFile.length());	 
	    // set headers for the response object
	    String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"",
	            downloadFile.getName());
	    response.setHeader(headerKey, headerValue); 
	    // get output stream of the response
	    try (OutputStream outStream = response.getOutputStream();
	    		FileInputStream inputStream = new FileInputStream(downloadFile)) {
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = -1;
	 
	        // write each byte of data  read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	    }
	    
	    
	}

	
	private void removeVariationImageFile(String fileName, String region) {
		
		String folderPath = (String)this.env.get(this.variationImageFilePathPropertyName)+File.separator+region;
		File file = new File(this.getRootFileDir()+File.separator+folderPath+File.separator+fileName);		
		file.delete();
		
	}

	
	private String uploadOtherImageFile(String fileName, byte[] bytes, String region) {
		
		String folderPath = (String)this.env.get(this.otherImageFilePathPropertyName)+File.separator+region;
		File fullPath = new File(this.getRootFileDir()+File.separator+folderPath);
		if(!fullPath.exists()) fullPath.mkdirs();
		File file = new File(fullPath.getAbsolutePath()+File.separator+fileName);
		if(file.exists()) return null;
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
			stream.write(bytes);
		} catch (Exception e) {
			logger.catching(e);
			//logger.info("IOException in MaintainProductOnboardingDaoImpl method uploadOtherImageFile: ", e);
		}
		
		return "success";
	}

	
	private void downloadOtherImageFile(String fileName, String region, HttpServletResponse response)
			throws IOException {
		

		String folderPath = (String)this.env.get(this.otherImageFilePathPropertyName)+File.separator+region;
		String fullPath = this.getRootFileDir()+File.separator+folderPath+File.separator+fileName;		
		File downloadFile = new File(fullPath);		
		//logger.debug(fullPath);
		// MIME type of the file
	    String mimeType= URLConnection.guessContentTypeFromName(fileName);
	    if (mimeType == null) {
	        // Set to binary type if MIME mapping not found
	        mimeType = "application/octet-stream";
	    }
	    // set content attributes for the response object
	    response.setContentType(mimeType);
	    response.setContentLength((int) downloadFile.length());	 
	    // set headers for the response object
	    String headerKey = "Content-Disposition";
	    String headerValue = String.format("attachment; filename=\"%s\"",
	            downloadFile.getName());
	    response.setHeader(headerKey, headerValue); 
	    // get output stream of the response
	    try (OutputStream outStream = response.getOutputStream();
	    		FileInputStream inputStream = new FileInputStream(downloadFile)) {
	        byte[] buffer = new byte[BUFFER_SIZE];
	        int bytesRead = -1;
	 
	        // write each byte of data  read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	    }
	    
	    
	}
	
	
	private void removeOtherImageFile(String fileName, String region) {
		
		String folderPath = (String)this.env.get(this.otherImageFilePathPropertyName)+File.separator+region;
		File file = new File(this.getRootFileDir()+File.separator+folderPath+File.separator+fileName);		
		file.delete();
		
	}
		

}