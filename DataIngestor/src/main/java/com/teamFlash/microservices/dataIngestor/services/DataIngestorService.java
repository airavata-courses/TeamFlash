package com.teamFlash.microservices.dataIngestor.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.camel.Exchange;

public class DataIngestorService {
	
	public String getData(Exchange exchange) throws Exception
	{
		System.out.println("in DataIngestorService.getData ---");
		//String url="https://noaa-nexrad-level2.s3.amazonaws.com/1999/04/05/KAMX/KAMX19990405_000408.gz";
		//process(exchange);
		return process(exchange);
	}
	
	public String process(Exchange exchange) throws Exception
	{
		System.out.println(exchange.getIn().getBody());
		String date = (String) exchange.getIn().getHeader("date");
		String time = (String) exchange.getIn().getHeader("time");
		String station = (String) exchange.getIn().getHeader("station");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date convertedCurrentDate = sdf.parse(date);
	    String conv_date=sdf.format(convertedCurrentDate );
	    sdf= new SimpleDateFormat("yyyyMMdd");
	    convertedCurrentDate= sdf.parse(conv_date);
	    conv_date=sdf.format(convertedCurrentDate );
        System.out.println("Test...https://noaa-nexrad-level2.s3.amazonaws.com/"+ conv_date +"/"+station+"/ "+station+"/"+station+conv_date+"_"+time+".gz"+"/ is the url");
        return "https://noaa-nexrad-level2.s3.amazonaws.com/"+ conv_date +"/"+station+"/ "+station+"/"+station+conv_date+"_"+time+".gz";
        //exchange.getOut().setBody("<html><body>NEXTRAD URL is : https://noaa-nexrad-level2.s3.amazonaws.com/" + conv_date +"/"+station+"/ "+station+"/"+station+conv_date+"_"+time+".gz"+"/ is the url.</body></html>");
	}

}
