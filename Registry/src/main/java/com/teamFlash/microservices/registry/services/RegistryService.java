package com.teamFlash.microservices.registry.services;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RegistryService implements Processor{

	public void getAudit(Exchange exchange) throws Exception
	{
		String audit="in audit";
		process(exchange);
		//return audit;
	}
	
	public void process(Exchange exchange) throws Exception
	{
		String test = (String) exchange.getIn().getHeader("test");
        System.out.println("Test..."+test);
        exchange.getOut().setBody("<html><body>Test " + test + " is Camel in Action.</body></html>");
	}
	
}
