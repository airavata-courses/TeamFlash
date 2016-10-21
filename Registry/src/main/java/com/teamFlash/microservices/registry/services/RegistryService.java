package com.teamFlash.microservices.registry.services;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RegistryService implements Processor{

	public void getAudit(Exchange exchange) throws Exception
	{
		System.out.println("in update registry...");
		process(exchange);
		//return audit;
	}
	public String getFetch(Exchange exchange) throws Exception
	{
		System.out.println("in fetch registry...");
		return process1(exchange);
		//return fetch;
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
	}
	
	public String process1(Exchange exchange) throws Exception
	{
		String userRequestId = (String) exchange.getIn().getHeader("id");
		String userName=(String) exchange.getIn().getHeader("username");
        System.out.println(userRequestId+"------>"+userName);
        LoggerDAO loggerDAO = new LoggerDAO();
        
        StringBuilder sb=new StringBuilder();
        
        sb.append(loggerDAO.fetchLog(userRequestId));
        
        return sb.toString();
	}
	
}
