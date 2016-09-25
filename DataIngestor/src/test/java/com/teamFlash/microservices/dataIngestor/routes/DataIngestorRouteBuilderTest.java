package com.teamFlash.microservices.dataIngestor.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataIngestorRouteBuilderTest extends CamelTestSupport{

	@Produce(uri= "direct:getData")
	private ProducerTemplate start;
	
	@BeforeClass
	public static void setJettyPort()
	{
		System.setProperty("port", "5679");
	}
	
	@Test
	public void canGetDataFromRoute() throws InterruptedException
	{
		Exchange ex=createExchangeWithBody("not relevant");
		start.send(ex);
		
		Object data=ex.getOut().getBody();
		//assertEquals(String.class,data.getClass());
	}
	
	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception
	{
		return new RouteBuilder[]
				{
					new DataIngestorRouteBuilder(), new RouteBuilder()
					{
						public void configure() throws Exception
						{
							from("direct:data").to("mock:result");
						}
					}};		
	}
}
