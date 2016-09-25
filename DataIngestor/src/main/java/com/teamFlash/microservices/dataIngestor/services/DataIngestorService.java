package com.teamFlash.microservices.dataIngestor.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Exchange;

public class DataIngestorService {
	
	public String getData(Exchange exchange) throws Exception
	{
		System.out.println("in DataIngestorService.getData ---");
		//String url="https://noaa-nexrad-level2.s3.amazonaws.com/1999/04/05/KAMX/KAMX19990405_000408.gz";
		return process(exchange);
	}
	
	public String process(Exchange exchange) throws Exception
	{
		String date = (String) exchange.getIn().getHeader("date");
		String time = (String) exchange.getIn().getHeader("time");
		String station = (String) exchange.getIn().getHeader("station");
		System.out.println(date+"----"+time+"------"+station);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	    Date convertedCurrentDate1 = sdf1.parse(date);
	    SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd");
	    String conv_date2=sdf2.format(convertedCurrentDate1 );
	    SimpleDateFormat sdf3=new SimpleDateFormat("yyyy/MM/dd");
	    String conv_date3=sdf3.format(convertedCurrentDate1);
	    return "https://noaa-nexrad-level2.s3.amazonaws.com/"+conv_date3+"/"+station+"/"+station+conv_date2+"_"+time+".gz";
	}

}
