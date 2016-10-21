package com.teamFlash.microservices.registry.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

import com.teamFlash.microservices.registry.services.RegistryService;


public class RouteRegistryService extends RouteBuilder{

	public void configure() throws Exception {
		System.out.println("in RouteRegistryService.configure ---");
		//from("direct:getAudit").setExchangePattern(ExchangePattern.InOut).to(ExchangePattern.InOut, "jetty:http://0.0.0.0:{{port}}/result");
	    // add HTTP interface
	    from("jetty:http://0.0.0.0:{{port}}/audit/update")
	    .setExchangePattern(ExchangePattern.InOut)
	    .to("direct:getAudit");
	    
	    from("jetty:http://0.0.0.0:{{port}}/audit/fetch")
	    .setExchangePattern(ExchangePattern.InOut)
	    .to("direct:getFetch");
	    
	    from("direct:getAudit").setExchangePattern(ExchangePattern.InOut).bean(new RegistryService(), "process");
	    from("direct:getFetch").setExchangePattern(ExchangePattern.InOut).bean(new RegistryService(), "process1");
	}
}
