package com.teamFlash.microservices.dataIngestor.services;

import static org.junit.Assert.assertEquals;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

public class DataIngestorServiceTest {

	@Test
	public void test() throws Exception
	{
		CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
		//String url=new DataIngestorService().getData(ex);
		//assertEquals(String.class,url.getClass());
	}
}
