package com.teamFlash.microservices.dataIngestor.app;

import org.apache.camel.main.Main;
import com.teamFlash.microservices.dataIngestor.routes.DataIngestorRouteBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class RunDataIngestor {

	private Main main;
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		RunDataIngestor instance=new RunDataIngestor();
		final String port = (args.length == 1 ? args[0] : "8765");
		instance.registerService();
		instance.boot(port);
	}

	@SuppressWarnings("deprecation")
	public void boot(String port) throws Exception {
	    System.setProperty("port", port);

	    // create a Main instance
	    main = new Main();
	    // enable hangup support so you can press ctrl + c to terminate the JVM
	    main.enableHangupSupport();
	    // add routes
	    main.addRouteBuilder(new DataIngestorRouteBuilder());

	    // run until you terminate the JVM
	    System.out.println(String.format("Starting Camel, using port %s. Use ctrl + c to terminate the JVM", port));
	    main.run();
	  }

		public void registerService()
	    {
	        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
	        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("52.52.144.190:2181", retryPolicy);
	        curatorFramework.start();
	        try
	        {
	           URL url = new URL("http://checkip.amazonaws.com");
	           BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	           String ip = in.readLine();
	           int port = 8765;
	            ServiceInstance serviceInstance = ServiceInstance.builder()
	                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
	                    .address(ip)
	                    .port(port)
	                    .name("worker")
	                    .build();

	            ServiceDiscoveryBuilder.builder(Void.class)
	                    .basePath("DataIngestor")
	                    .client(curatorFramework)
	                    .thisInstance(serviceInstance)
	                    .build()
	                    .start();
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	}
}
