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
 * Created by girish on 11/19/16.
 */
@Path("/registerFTWorker")
public class Worker {

    static ServiceProvider<Void> serviceProvider;
    static String[] urlEndpoints = {"http://52.52.165.77","http://52.52.164.169"};

    @GET
    public static String registerForecastTrigger() throws Exception
    {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("52.52.144.190:2181", retryPolicy);

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

}
