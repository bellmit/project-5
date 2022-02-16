package com.kindminds.drs;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum RevenueGrade2022 {

	GRADE1(    "0","0.08",  "0"),
	GRADE2("10000","0.07","178"),
	GRADE3("24000","0.06","578"),
	GRADE4("36000","0.05","1238"),
	GRADE5("48000","0.04","1448"),
	GRADE6("60000","0.03","1688");

	private BigDecimal startAmountUsd;
	private BigDecimal retainmentRate;
	private BigDecimal progressiveDifferenceUsd;

	private RevenueGrade2022(String startAmountUsd, String retainmentRate, String progressiveDifferenceUsd) {
		this.startAmountUsd=new BigDecimal(startAmountUsd);
		this.retainmentRate=new BigDecimal(retainmentRate);
		this.progressiveDifferenceUsd=new BigDecimal(progressiveDifferenceUsd);
	}
	
	public BigDecimal getRetainmentRate() {
		return retainmentRate;
	}
	
	public static RevenueGrade2022 getGrade(BigDecimal usdAmount){
		if(usdAmount.compareTo(BigDecimal.ZERO)<=0) return RevenueGrade2022.GRADE1;
		RevenueGrade2022 result=null;
		for(RevenueGrade2022 grade: RevenueGrade2022.values()){
			if(grade.isInGradeRange(usdAmount)){
				result=grade;
				break;
			}
		}
		Assert.notNull(result,"RevenueGrade2022 null");
		return result;
	}
	
	public boolean isInGradeRange(BigDecimal usdAmount){
		RevenueGrade2022 nextGrade = this.getNextGrade();
		boolean greaterThanlowerBound = usdAmount.compareTo(this.startAmountUsd)>0;
		boolean smallerThanOrEqualToUpperBound = nextGrade==null?true:usdAmount.compareTo(nextGrade.startAmountUsd)<=0;
		return greaterThanlowerBound&&smallerThanOrEqualToUpperBound;
	}
	
	public RevenueGrade2022 getNextGrade(){
		if(this.ordinal()== RevenueGrade2022.values().length-1)
			return null;
		return RevenueGrade2022.values()[this.ordinal()+1];
	}
	
	public static BigDecimal calculateEffectiveRetainmentRate(BigDecimal usdAmount){
		if(usdAmount.compareTo(BigDecimal.ZERO)<=0) return RevenueGrade2022.GRADE1.retainmentRate;
		RevenueGrade2022 grade = RevenueGrade2022.GRADE1;
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