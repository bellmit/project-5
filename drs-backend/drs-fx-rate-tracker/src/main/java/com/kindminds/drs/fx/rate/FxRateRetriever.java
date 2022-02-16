package com.kindminds.drs.fx.rate;

import com.kindminds.drs.Currency;
import com.kindminds.drs.fx.rate.constant.InterBankRate;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class FxRateRetriever {

    private WebDriver webDriver;

    public FxRateRetriever(){
        //System.setProperty("webdriver.chrome.driver", "C:\\Users\\HyperionFive\\Desktop\\chromedriver.exe");
        //System.setProperty("webdriver.chrome.driver", "/home/arthur/driver/chromedriver");
        System.setProperty("webdriver.chrome.driver", "/home/servicedesk/driver/chromedriver");
        this.webDriver = new ChromeDriver();
        this.webDriver.get("https://www.oanda.com/currency/converter/");
    }

    public void close(){
        this.webDriver.close();
    }

    public String getFxRate(Currency src, Currency dst, String dateSrt, InterBankRate interBankRate) throws InterruptedException{
        this.typeTextAndclickEnter("quote_currency_input", src.name());
        this.typeTextAndclickEnter("base_currency_input", dst.name());
        this.clickElement(By.id("interbank_rates_input"));
        this.clickElement(By.xpath("//span[contains(text(),'"+interBankRate.getPercentageText()+"')]"));
        this.typeTextAndclickEnter("end_date_input", dateSrt);
        String fxRateStr=this.getAttributeValue("base_amount_input");
        return fxRateStr;
    }

    private void typeTextAndclickEnter(String id,String text) throws InterruptedException{
        WebElement webElement = this.webDriver.findElement(By.id(id));
        webElement.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE);
        Thread.sleep(1000);
        webElement.sendKeys(text);
        Thread.sleep(1000);
        webElement.sendKeys(Keys.RETURN);
        Thread.sleep(1000);
    }

    private void clickElement(By by) throws InterruptedException{
        this.webDriver.findElement(by).click();
        Thread.sleep(1000);
    }

    private String getAttributeValue(String id){
        return this.webDriver.findElement(By.id("base_amount_input")).getAttribute("value");
    }
}

