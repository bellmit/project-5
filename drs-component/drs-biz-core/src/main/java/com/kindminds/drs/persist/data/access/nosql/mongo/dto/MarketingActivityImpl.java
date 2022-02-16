package com.kindminds.drs.persist.data.access.nosql.mongo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.kindminds.drs.api.data.transfer.maketing.MarketingActivity;
import org.bson.types.ObjectId;
import java.util.*;


//@Document(collection = "MarketingActivity")
public class MarketingActivityImpl implements MarketingActivity {

    @JsonSerialize(using = ToStringSerializer.class)
        ObjectId _id      = null;

        String country    = "";
        String code    = "";
        String name    = "";
        String doneBy    = "";

 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH  mm  ss.SSS'Z'")
     Date startDate      = null;

 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH  mm  ss.SSS'Z'")
        Date endDate      = null;

        String platform    = "";
        String activity    = "";
        String  initialAmount    = "";
        String budgetFinalAmount    = "";
        String discount    = "" ;
        String  unitOfMeasure    = "";
          String details    = "" ;
          String link1    = "" ;
          String link2    = "";
          String link3    = "" ;
          String link4    = "" ;
          String link5    = "" ;
          String link6    = "" ;
          String link7    = "" ;
          String link8    = "" ;
          String link9    = "" ;
          String link10    = "" ;
          String considerationsProblemToSolve   = "";
          String originalText    = "" ;
          String newText    = "" ;
          String suggestionsForSupplier    = "";


   @Override
   public ObjectId get_id() {
      return _id;
   }

   @Override
   public String getCountry() {
      return country;
   }

   @Override
   public String getCode() {
      return code;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getDoneBy() {
      return doneBy;
   }

   @Override
   public Date getStartDate() {
      return startDate;
   }

   @Override
   public Date getEndDate() {
      return endDate;
   }

   @Override
   public String getPlatform() {
      return platform;
   }

   @Override
   public String getActivity() {
      return activity;
   }

   @Override
   public String getInitialAmount() {
      return initialAmount;
   }

   @Override
   public String getBudgetFinalAmount() {
      return budgetFinalAmount;
   }

   @Override
   public String getDiscount() {
      return discount;
   }

   @Override
   public String getUnitOfMeasure() {
      return unitOfMeasure;
   }

   @Override
   public String getDetails() {
      return details;
   }

   @Override
   public String getLink1() {
      return link1;
   }

   @Override
   public String getLink2() {
      return link2;
   }

   @Override
   public String getLink3() {
      return link3;
   }

   @Override
   public String getLink4() {
      return link4;
   }

   @Override
   public String getLink5() {
      return link5;
   }

   @Override
   public String getLink6() {
      return link6;
   }

   @Override
   public String getLink7() {
      return link7;
   }

   @Override
   public String getLink8() {
      return link8;
   }

   @Override
   public String getLink9() {
      return link9;
   }

   @Override
   public String getLink10() {
      return link10;
   }

   @Override
   public String getConsiderationsProblemToSolve() {
      return considerationsProblemToSolve;
   }

   @Override
   public String getOriginalText() {
      return originalText;
   }

   @Override
   public String getNewText() {
      return newText;
   }

   @Override
   public String getSuggestionsForSupplier() {
      return suggestionsForSupplier;
   }

   @Override
   public void set_id(ObjectId _id) {
      this._id = _id;
   }

   @Override
   public void setCountry(String country) {
      this.country = country;
   }

   @Override
   public void setCode(String code) {
      this.code = code;
   }

   @Override
   public void setName(String name) {
      this.name = name;
   }

   @Override
   public void setDoneBy(String doneBy) {
      this.doneBy = doneBy;
   }

   @Override
   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   @Override
   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   @Override
   public void setPlatform(String platform) {
      this.platform = platform;
   }

   @Override
   public void setActivity(String activity) {
      this.activity = activity;
   }

   @Override
   public void setInitialAmount(String initialAmount) {
      this.initialAmount = initialAmount;
   }

   @Override
   public void setBudgetFinalAmount(String budgetFinalAmount) {
      this.budgetFinalAmount = budgetFinalAmount;
   }

   @Override
   public void setDiscount(String discount) {
      this.discount = discount;
   }

   @Override
   public void setUnitOfMeasure(String unitOfMeasure) {
      this.unitOfMeasure = unitOfMeasure;
   }

   @Override
   public void setDetails(String details) {
      this.details = details;
   }

   @Override
   public void setLink1(String link1) {
      this.link1 = link1;
   }

   @Override
   public void setLink2(String link2) {
      this.link2 = link2;
   }

   @Override
   public void setLink3(String link3) {
      this.link3 = link3;
   }

   @Override
   public void setLink4(String link4) {
      this.link4 = link4;
   }

   @Override
   public void setLink5(String link5) {
      this.link5 = link5;
   }

   @Override
   public void setLink6(String link6) {
      this.link6 = link6;
   }

   @Override
   public void setLink7(String link7) {
      this.link7 = link7;
   }

   @Override
   public void setLink8(String link8) {
      this.link8 = link8;
   }

   @Override
   public void setLink9(String link9) {
      this.link9 = link9;
   }

   @Override
   public void setLink10(String link10) {
      this.link10 = link10;
   }

   @Override
   public void setConsiderationsProblemToSolve(String considerationsProblemToSolve) {
      this.considerationsProblemToSolve = considerationsProblemToSolve;
   }

   @Override
   public void setOriginalText(String originalText) {
      this.originalText = originalText;
   }

   @Override
   public void setNewText(String newText) {
      this.newText = newText;
   }

   @Override
   public void setSuggestionsForSupplier(String suggestionsForSupplier) {
      this.suggestionsForSupplier = suggestionsForSupplier;
   }
}


