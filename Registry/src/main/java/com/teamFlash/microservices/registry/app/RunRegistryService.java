package com.teamFlash.microservices.registry.app;

import org.apache.camel.main.Main;

import com.teamFlash.microservices.registry.routes.RouteRegistryService;
import org.omg.CORBA.portable.OutputStream;

import java.io.FileOutputStream;
import java.io.IOException;

public class RunRegistryService {

	private Main main;
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		RunRegistryService instance=new RunRegistryService();
		final String port = (args.length == 1 ? args[0] : "8550");
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
	    main.addRouteBuilder(new RouteRegistryService());

	    // run until you terminate the JVM
	    System.out.println(String.format("Starting Camel, using port %s. Use ctrl + c to terminate the JVM.", port));
	    main.run();
	  }

}
