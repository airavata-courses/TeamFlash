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
		String userId = (String) exchange.getIn().getHeader("username");
		String uniqueId = (String) exchange.getIn().getHeader("id");
		String microservice = (String) exchange.getIn().getHeader("msvc");
        System.out.println("Test..."+userId+"......"+uniqueId+"......"+microservice);
        //exchange.getOut().setBody("<html><body>Test " + test + " is Camel in Action.</body></html>");
        
        Log log = new Log();
		log.setUserID(userId);
		log.setRequestID(uniqueId);
		log.setMicroservice(microservice);
		log.setLogDescription("This is timestamp testing");
		LoggerDAO loggerDAO = new LoggerDAO();
		loggerDAO.insertLog(log);
		//loggerDAO.deleteLog(1);
	}
	
}
