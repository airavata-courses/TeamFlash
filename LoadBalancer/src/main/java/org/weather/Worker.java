package org.weather;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.UriSpec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


/**
 * Created by girish on 11/17/16.
 */
@Path("/registerWorker")
public class Worker {

    static ServiceProvider<Void> serviceProvider;
    static String[] urlEndpoints = {"http://52.52.165.77","http://52.52.164.169"};
    @GET
    @Path("/dataIngestor")
    public static String registerDataIngestor() throws Exception
    {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

        curatorFramework.start();
        //System.out.println("after curator framework start");
        int port = 8765;

        for(int i=0;i<urlEndpoints.length;i++)
        {
            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(urlEndpoints[i])
                    .port(port)
                    .name("worker")
                    .build();
            System.out.println("I am inside new function");
            ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("DataIngestor")
                    .client(curatorFramework)
                    .thisInstance(serviceInstance)
                    .build()
                    .start();

        }

        return "data Ingestor success";
    }

    @Path("/stormDetection")
    public static String registerStormDetection() throws Exception
    {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

        curatorFramework.start();

        int port = 7000;

        for(int i=0;i<urlEndpoints.length;i++)
        {
            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(urlEndpoints[i])
                    .port(port)
                    .name("worker")
                    .build();

            ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("StormDetection")
                    .client(curatorFramework)
                    .thisInstance(serviceInstance)
                    .build()
                    .start();

        }

        return "Storm Detection success";
    }

    @Path("/stormCluster")
    public static String registerStormCluster() throws Exception
    {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

        curatorFramework.start();
        //System.out.println("after curator framework start");
        int port = 8000;

        for(int i=0;i<urlEndpoints.length;i++)
        {
            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(urlEndpoints[i])
                    .port(port)
                    .name("worker")
                    .build();

            ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("StormClustering")
                    .client(curatorFramework)
                    .thisInstance(serviceInstance)
                    .build()
                    .start();

        }

        return "Storm Clustering success";
    }

    @Path("/forecastTrigger")
    public static String registerForecastTrigger() throws Exception
    {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

        curatorFramework.start();

        int port = 8080;

        for(int i=0;i<urlEndpoints.length;i++)
        {
            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(urlEndpoints[i])
                    .port(port)
                    .name("worker")
                    .build();

            ServiceDiscoveryBuilder.builder(Void.class)
                    .basePath("ForecastTrigger")
                    .client(curatorFramework)
                    .thisInstance(serviceInstance)
                    .build()
                    .start();

        }

        return "Forecast Trigger success";
    }

    @Path("/runForecast")
    public static String registerRunForecast() throws Exception
    {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);

        curatorFramework.start();

        int port = 8081;

        for(int i=0;i<urlEndpoints.length;i++)
        {
            ServiceInstance serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                    .address(urlEndpoints[i])
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

        return "Run Forecast success";
    }
}

