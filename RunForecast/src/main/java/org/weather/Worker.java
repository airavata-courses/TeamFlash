package org.weather;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
/**
 * Created by girish on 11/17/16.
 */

@WebListener
public class Worker implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent servletContext)
    {
        //ServiceProvider<Void> serviceProvider;
        //String[] urlEndpoints = {"52.52.165.77","52.52.164.169"};
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("52.52.144.190:2181", retryPolicy);
        curatorFramework.start();
        try
        {
           URL url = new URL("http://checkip.amazonaws.com");
           BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
           String ip = in.readLine(); 
           int port = 8081; 
            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(ip)
                    .port(port)
                    .name("worker")
                    .build();

            ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("RunForecast")
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

    @Override
    public void contextDestroyed(ServletContextEvent servletContext)
    {
        System.out.println("After exit");
    }
}
