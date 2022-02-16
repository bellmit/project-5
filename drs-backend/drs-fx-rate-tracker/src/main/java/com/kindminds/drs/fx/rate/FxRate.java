package com.kindminds.drs.fx.rate;


import com.kindminds.drs.Currency;
import com.kindminds.drs.fx.rate.constant.InterBankRate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FxRate {

    private FxRateRetriever fxRateRetriever=null;
    private BigDecimal rate;
    private BigDecimal interBankRate;
    private OffsetDateTime date ;

    public BigDecimal getRate(){
        return this.rate;
    }

    public OffsetDateTime getDate(){
        return this.date;
    }

    public  BigDecimal getInterBankRate(){
        return this.interBankRate;
    }



    public FxRate(FxRateRetriever fxRateRetriever) {
        this.fxRateRetriever=fxRateRetriever;
    }

    public void retrieve(Currency src, Currency dst, OffsetDateTime baseDate, InterBankRate interBankRate) throws InterruptedException{

        this.interBankRate = new BigDecimal(interBankRate.getValueText());

        this.date = baseDate;
        String dateSrt =  baseDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        this.rate = new BigDecimal(this.fxRateRetriever.getFxRate(src,dst,dateSrt, interBankRate));

        /*
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            this.date = sdf.parse(dateSrt);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/



        //this.date = this.appendTimeZone(dateSrt);

       // return this.composeCurrencyInsertSql(src.name(), dst.name(), date, fxRate, interBankRate.getValueText());
    }

    private String appendTimeZone(String date){
        return date+" 00:00:00+02";
    }

    /*
    public String composeCurrencyInsertSql(String src,String dst,String date,String rate,String interbankRate){
        String sql = "insert into currency_exchange "
                + "(src_currency_id, dst_currency_id,    date,    rate, interbank_rate) select "
                + "          src.id,          dst.id,'"+date+"',"+rate+","+interbankRate+" "
                + "from currency src, currency dst where src.name='"+src+"' and dst.name='"+dst+"';";
        return sql;
    }
    */
}