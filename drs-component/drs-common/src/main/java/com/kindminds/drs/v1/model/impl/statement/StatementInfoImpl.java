package com.kindminds.drs.v1.model.impl.statement;

import java.util.Calendar;
import java.util.Date;

import com.kindminds.drs.api.v1.model.report.StatementInfo;
import com.kindminds.drs.util.DateHelper;

public class StatementInfoImpl implements StatementInfo {
	
	private Date start;
	private Date end;
	private String isurKcode;
	private String rcvrKcode;

	public StatementInfoImpl(Date start, Date end, String isurKcode, String rcvrKcode) {
		this.start = start;
		this.end = end;
		this.isurKcode = isurKcode;
		this.rcvrKcode = rcvrKcode;
	}
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	@Override
	public String getDateStart() {
		return DateHelper.toString(this.start,"yyyy-MM-dd","UTC");
	}

	@Override
	public String getDateEnd() {
		Calendar end = Calendar.getInstance();
		end.setTime(this.end);
		end.add(Calendar.DAY_OF_MONTH, -1);
		return DateHelper.toString(end.getTime(),"yyyy-MM-dd","UTC");
	}

	@Override
	public String getIsurKcode() {
		return this.isurKcode;
	}

	@Override
	public String getRcvrKcode() {
		return this.rcvrKcode;
	}

}
