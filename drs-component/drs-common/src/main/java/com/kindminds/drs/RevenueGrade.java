package com.kindminds.drs;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.util.Assert;

public enum RevenueGrade {
	
	GRADE1(    "0","0.15",  "0"),
	GRADE2( "1000","0.14", "10"),
	GRADE3( "2200","0.13", "32"),
	GRADE4( "4600","0.12", "78"),
	GRADE5("10000","0.11","178"),
	GRADE6("14000","0.105","248"),
	GRADE7("18000","0.1","338"),
	GRADE8("24000","0.09","578"),
	GRADE9("30000","0.08","878"),
	GRADE10("36000","0.07","1238"),
	GRADE11("42000","0.065","1448"),
	GRADE12("48000","0.06","1688");
	
	private BigDecimal startAmountUsd;
	private BigDecimal retainmentRate;
	private BigDecimal progressiveDifferenceUsd;
	
	private RevenueGrade(String startAmountUsd,String retainmentRate,String progressiveDifferenceUsd) {
		this.startAmountUsd=new BigDecimal(startAmountUsd);
		this.retainmentRate=new BigDecimal(retainmentRate);
		this.progressiveDifferenceUsd=new BigDecimal(progressiveDifferenceUsd);
	}
	
	public BigDecimal getRetainmentRate() {
		return retainmentRate;
	}
	
	public static RevenueGrade getGrade(BigDecimal usdAmount){
		if(usdAmount.compareTo(BigDecimal.ZERO)<=0) return RevenueGrade.GRADE1;
		RevenueGrade result=null;
		for(RevenueGrade grade:RevenueGrade.values()){
			if(grade.isInGradeRange(usdAmount)){
				result=grade;
				break;
			}
		}
		Assert.notNull(result,"RevenueGrade null");
		return result;
	}
	
	public boolean isInGradeRange(BigDecimal usdAmount){
		RevenueGrade nextGrade = this.getNextGrade();
		boolean greaterThanlowerBound = usdAmount.compareTo(this.startAmountUsd)>0;
		boolean smallerThanOrEqualToUpperBound = nextGrade==null?true:usdAmount.compareTo(nextGrade.startAmountUsd)<=0;
		return greaterThanlowerBound&&smallerThanOrEqualToUpperBound;
	}
	
	public RevenueGrade getNextGrade(){
		if(this.ordinal()==RevenueGrade.values().length-1)
			return null;
		return RevenueGrade.values()[this.ordinal()+1];
	}
	
	public static BigDecimal calculateEffectiveRetainmentRate(BigDecimal usdAmount){
		if(usdAmount.compareTo(BigDecimal.ZERO)<=0) return RevenueGrade.GRADE1.retainmentRate;
		RevenueGrade grade = RevenueGrade.GRADE1;
		while(grade!=null){
			if(grade.isInGradeRange(usdAmount)){
				BigDecimal drsRawRetainment = usdAmount.multiply(grade.retainmentRate).setScale(6,RoundingMode.HALF_UP);
				BigDecimal actualRetainment = drsRawRetainment.add(grade.progressiveDifferenceUsd);
				return actualRetainment.divide(usdAmount,6, RoundingMode.HALF_UP);
			}
			grade = grade.getNextGrade();
		}
		Assert.isTrue(false,"no EffectiveRetainmentRate");
		return null;
	}
	
}