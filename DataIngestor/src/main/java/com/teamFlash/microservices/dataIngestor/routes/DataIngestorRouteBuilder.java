package com.teamFlash.microservices.dataIngestor.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

import com.teamFlash.microservices.dataIngestor.services.DataIngestorService;

public class DataIngestorRouteBuilder extends RouteBuilder{

	public void configure() throws Exception {
		System.out.println("in DataIngestorRouteBuilder.configure ---");
		from("direct:getData").setExchangePattern(ExchangePattern.InOut).bean(new DataIngestorService(), "getData");
	    // .to("direct:data");
	    // add HTTP interface
	    from("jetty:http://0.0.0.0:{{port}}/data").setExchangePattern(ExchangePattern.InOut).to("direct:getData");
	}
}
