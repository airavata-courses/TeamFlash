package com.teamFlash.microservices.registry.services;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.teamFlash.microservices.registry.forecast.JobDetails;
import com.teamFlash.microservices.registry.forecast.JobService;

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
//www.google.com?locationName=bloom&userName=marlon
	public String getInsertJob(Exchange exchange) throws Exception
	{
		System.out.println("getInsertJob...");
		return process2(exchange);
	}

	public String getQueryMeso(Exchange exchange) throws Exception
	{
		return process3(exchange);
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
        System.out.println("after fetching result from fetchLog------>"+sb.toString());
        if(sb.length()>0)
        	return sb.toString();
        else
        	return "no id,no name,no date,no log,No microsevice";
	}
	
	public String process2(Exchange exchange) throws Exception
	{
		String userName = (String) exchange.getIn().getHeader("username");
		String locationName = (String) exchange.getIn().getHeader("location");
		JobService js=new JobService();
		int x=js.createJOB(locationName, userName);
		System.out.println("Unique_ID------>"+x);
		return ""+x;
	}
	
	public String process3(Exchange exchange) throws Exception
	{
		System.out.println("getQueryMeso...");
		String userName = (String) exchange.getIn().getHeader("username");
		String id = (String) exchange.getIn().getHeader("id");
		JobDetails jd=new JobDetails();
		String res=jd.getJobsTasks(userName, id);
		System.out.println("JSON from job list..."+res);
		return res;
	}
	
}
