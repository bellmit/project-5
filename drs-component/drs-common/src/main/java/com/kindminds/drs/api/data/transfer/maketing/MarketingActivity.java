package com.kindminds.drs.api.data.transfer.maketing;

import org.bson.types.ObjectId;
import java.util.*;

public interface MarketingActivity{
  
  ObjectId get_id();

  void set_id( ObjectId var1);

  
  String getCountry();

  void setCountry( String var1);

  
  String getCode();

  void setCode( String var1);

  
  String getName();

  void setName( String var1);

  
  String getDoneBy();

  void setDoneBy( String var1);

  
  Date getStartDate();

  void setStartDate( Date var1);

  
  Date getEndDate();

  void setEndDate( Date var1);

  
  String getPlatform();

  void setPlatform( String var1);

  
  String getActivity();

  void setActivity( String var1);

  
  String getInitialAmount();

  void setInitialAmount( String var1);

  
  String getBudgetFinalAmount();

  void setBudgetFinalAmount( String var1);

  
  String getDiscount();

  void setDiscount( String var1);

  
  String getUnitOfMeasure();

  void setUnitOfMeasure( String var1);

  
  String getDetails();

  void setDetails( String var1);

  
  String getLink1();

  void setLink1( String var1);

  
  String getLink2();

  void setLink2( String var1);

  
  String getLink3();

  void setLink3( String var1);

  
  String getLink4();

  void setLink4( String var1);

  
  String getLink5();

  void setLink5( String var1);

  
  String getLink6();

  void setLink6( String var1);

  
  String getLink7();

  void setLink7( String var1);

  
  String getLink8();

  void setLink8( String var1);

  
  String getLink9();

  void setLink9( String var1);

  
  String getLink10();

  void setLink10( String var1);

  
  String getConsiderationsProblemToSolve();

  void setConsiderationsProblemToSolve( String var1);

  
  String getOriginalText();

  void setOriginalText( String var1);

  
  String getNewText();

  void setNewText( String var1);

  
  String getSuggestionsForSupplier();

  void setSuggestionsForSupplier( String var1);
}
