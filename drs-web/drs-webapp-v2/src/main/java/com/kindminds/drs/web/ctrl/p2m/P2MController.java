package com.kindminds.drs.web.ctrl.p2m;

import com.kindminds.drs.Context;
import com.kindminds.drs.UserInfo;
import com.kindminds.drs.util.Encryptor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.SysexMessage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

@Controller
@RequestMapping(value = "/p2m")
public class P2MController {

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping("/p/a")
    public String toP2MApplication(HttpServletRequest request , @RequestParam(name = "i") String p2mId) {

        String decryptResult = Encryptor.decrypt(p2mId,true);

        UserInfo u =  Context.getCurrentUser();
        String key = u.getUserId() + "_p2mApp";
        String p2mAppId = request.getSession().getAttribute(key) != null ? request.getSession().getAttribute(key).toString():"";
        if(StringUtils.isEmpty(p2mAppId)){
            request.getSession(true).setAttribute(key , decryptResult);
        }else{
            request.getSession().setAttribute(key , decryptResult);
        }

        return "th/reactIndex";
    }

    @Autowired
    ServletContext servletContext;

    private ResponseEntity generateResponseEntity(String kcode , String path , String fileName){

        UserInfo u =  Context.getCurrentUser();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attchement;filename=" +
                URLEncoder.encode(fileName, Charset.forName("UTF-8")));

        if(u.getCompanyKcode().equals(kcode) || u.isDrsUser()){

            byte[] fileBytes = null;
            File result= new File(path);

            // if(result.exists()){else respEntity = new ResponseEntity ("File Not Found", HttpStatus.OK);

            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(path);
                String type=result.toURL().openConnection().guessContentTypeFromName(fileName);
                headers.add("Content-Type",type);

                fileBytes =org.apache.commons.io.IOUtils.toByteArray(inputStream);
            } catch (FileNotFoundException | MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


           // Resource resource =new ServletContextResource(servletContext, path);

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }
    }

    private String getRootFileDir() {
        return System.getProperty("catalina.home");
    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/mp/i/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getMaretplaceInfoImage(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path = this.getRootFileDir() + "/supplier/"  + kcode + "/" + p2mName + "/images/"+fileName ;

        return generateResponseEntity(kcode,path ,fileName);

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/mp/f/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getMaretplaceInfoFile(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/files/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }


    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/is/i/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getInsuranceImage(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/images/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/is/f/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getInsuranceFile(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];

        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/files/" + fileName;

        return generateResponseEntity(kcode,path,fileName);


    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/r/i/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getRegionalImage(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/images/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/r/f/{p2mName}/{fileName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getRegionalFile(@PathVariable String p2mName , @PathVariable String fileName) {
        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/files/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/s/i/{p2mName}/{fileName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getShippingImage(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/images/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/s/f/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getShippingFile(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/files/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/pai/i/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getProductAdvancedInfoImage(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/images/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }

    @PreAuthorize("hasAnyRole(T(com.kindminds.drs.service.util.AuthorityList).auth('DRS_1_2_X'))")
    @RequestMapping(value = "/pai/f/{p2mName}/{fileName}", method = RequestMethod.GET )
    @ResponseBody
    public ResponseEntity<Resource> getProductAdvancedInfoFile(@PathVariable String p2mName , @PathVariable String fileName) {

        String [] p2mNameAry = p2mName.split("-");
        String kcode = p2mNameAry[1];
        String path =  this.getRootFileDir() + "/supplier/" + kcode + "/" + p2mName + "/files/" + fileName;
        return generateResponseEntity(kcode,path,fileName);

    }




     /*
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResponseEntity() {
        HttpHeaders headers = new HttpHeaders();
        InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
        byte[] media = IOUtils.toByteArray(in);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }

     */

    /*
    @GetMapping(
            value = "/r",
            produces = { MediaType.APPLICATION_PDF_VALUE }
    )
    @ResponseBody
    public byte[] getImageAsByteArray() throws IOException {
        final InputStream in = servletContext.getResourceAsStream("/WEB-INF/K486/2020-Scrum-Guide-US.pdf");
        return IOUtils.toByteArray(in);
    }
     */


}
